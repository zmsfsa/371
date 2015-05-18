package main.java.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RegHandler implements HttpHandler {

	private static final int HTTP_OK_STATUS = 200;
	private static final String REG_IS_OK = "OK";
	private static final String REG_IS_BAD = "NO";

	@Override
	public void handle(HttpExchange t) throws IOException {
		URI uri = t.getRequestURI();
		String[] words = uri.getPath().split("-");
		System.out.println("zapr = " + uri.getPath());
		int result = 0;
		for (String a : words) {
			System.out.println(a);
		}
		if (words.length != 4) {
			t.sendResponseHeaders(HTTP_OK_STATUS, REG_IS_BAD.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(REG_IS_BAD.getBytes());
			os.close();
		} else {
			User newUser = new User(words[0].substring("/reg/".length()),
					words[1], words[2], words[3]);
			WorkSql work = new WorkSql();
			if (work.addUser(newUser) < 0) {
				result = -1;
			}

			if (result > -1) {
				t.sendResponseHeaders(HTTP_OK_STATUS,
						REG_IS_OK.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(REG_IS_OK.getBytes());
				os.close();
			} else {
				t.sendResponseHeaders(HTTP_OK_STATUS,
						REG_IS_BAD.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(REG_IS_BAD.getBytes());
				os.close();
			}
		}

	}

}
