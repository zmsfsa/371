package main.java.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import main.java.Event;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EventHandler implements HttpHandler {
	
	private static final int HTTP_OK_STATUS = 200;
	private static final String EVENT_IS_OK = "OK";
	private static final String EVENT_IS_BAD = "NO";
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		URI uri = t.getRequestURI();
		String req = uri.getQuery();
		System.out.println("request was " + req);
		WorkSql work = new WorkSql();
		Event event = work.getEventByName(req);
		if(event == null){
			t.sendResponseHeaders(HTTP_OK_STATUS, EVENT_IS_BAD.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(EVENT_IS_BAD.getBytes());
			os.close();
		}
		else{
			Calendar calendar = event.getDateEvent();
			Date date = calendar.getTime();
			String dat = date.getDate() + "-" + date.getMonth() + "-" + date.getYear();
			System.out.println(date.getYear() + " = year");
			t.sendResponseHeaders(HTTP_OK_STATUS, dat.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(dat.getBytes());
			os.close();
		}
	}

}
