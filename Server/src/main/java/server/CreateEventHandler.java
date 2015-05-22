package main.java.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.Date;
import java.util.Calendar;

import main.java.Event;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CreateEventHandler implements HttpHandler{
	private static final int HTTP_OK_STATUS = 200;
	private static final String WRONG_ARG = "wrong args";
	private static final String EVENT_IS_OK = "OK";
	private static final String EVENT_IS_BAD = "NO";

	public void handle(HttpExchange t) throws IOException {

		URI uri = t.getRequestURI();
		String req = uri.getQuery();
		System.out.println("request was " + req);
		String[] words = req.split("=");
		Calendar date = Calendar.getInstance();
		String[] dates = words[2].split("-");
		date.set(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]));
		
		if(words.length != 3){
			t.sendResponseHeaders(HTTP_OK_STATUS, WRONG_ARG.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(WRONG_ARG.getBytes());
			os.close();
		}	
		else{
			WorkSql work = new WorkSql();
			Event a = new Event(words[1], date);
			if(work.addEvent(a) == 0){
				work.makeInclude(words[0], a.getNameEvent());
				t.sendResponseHeaders(HTTP_OK_STATUS, EVENT_IS_OK.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(EVENT_IS_OK.getBytes());
				os.close();
			}
			else{
				t.sendResponseHeaders(HTTP_OK_STATUS, EVENT_IS_BAD.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(EVENT_IS_BAD.getBytes());
				os.close();
			}
		}
	}
}
