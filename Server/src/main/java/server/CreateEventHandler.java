package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import main.java.Event;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CreateEventHandler implements HttpHandler {
	private static final String HANDLER_NAME = "CreateEventHandler";
	private static final int HTTP_OK_STATUS = 200;
	private static final String WRONG_ARG = "wrong args";
	private static final String EVENT_IS_OK = "OK";
	private static final String WRONG_DATE = "wrong date";
	private static final String EVENT_IS_BAD = "This name is already used";

	public void handle(HttpExchange t) throws IOException {
		
		
		
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String,String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));
		
		WorkSql work = new WorkSql();
		Calendar date = Calendar.getInstance();
		String[] dates = params.get("eventDate").split("-");
		date.set(Integer.parseInt(dates[2]),
				Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[0]));
		Event event = new Event(params.get("eventName"), date);

		if (work.addEvent(event) == 0) {
			work.makeInclude(params.get("login"), event.getNameEvent());
			t.sendResponseHeaders(HTTP_OK_STATUS,
					EVENT_IS_OK.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(EVENT_IS_OK.getBytes());
			os.close();
		} else {
			t.sendResponseHeaders(HTTP_OK_STATUS,
					EVENT_IS_BAD.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(EVENT_IS_BAD.getBytes());
			os.close();
		}
		
	}
}
