package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LogHandler implements HttpHandler {

	private static final int HTTP_OK_STATUS = 200;
	private static final String LOG_IS_OK = "continue";
	private static final String LOG_IS_BAD = "NO";
	private static final String WRONG_PWD = "wrong password";
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		
		URI uri = t.getRequestURI();
		String[] words = uri.getQuery().split("=");
		InputStream is = t.getRequestBody();
		
		
		if(words.length != 2){
			t.sendResponseHeaders(HTTP_OK_STATUS, LOG_IS_BAD.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(LOG_IS_BAD.getBytes());
			os.close();
			System.out.println("not 2");
		}
		else{
			WorkSql work = new WorkSql();
			User user = work.getUserByLogin(words[0]);
			if(user.getPassword().equals(words[1])){
				int id = user.getIdUser();
				String response = LOG_IS_OK + "=" + id;
				t.sendResponseHeaders(HTTP_OK_STATUS, response.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
				System.out.println("good");
			}
			else{
				t.sendResponseHeaders(HTTP_OK_STATUS, WRONG_PWD.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(WRONG_PWD.getBytes());
				os.close();
				System.out.println("in else");
			}
		}
	}

}