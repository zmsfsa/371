package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import main.java.Event;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CreateEventHandler implements HttpHandler {
	private static final int HTTP_OK_STATUS = 200;
	private static final String EVENT_IS_OK = "OK";
	private static final String EVENT_DATE = "eventDate";
	private static final String LOGIN = "login";
	private static final String EVENT_IS_BAD = "This name is already used";
	private static final String ADDRES = "addres";
	private static final String EVENT_NAME = "eventName";

	public void handle(HttpExchange t) throws IOException {
		System.out
		.println("===================================================================================================================================================================================================");

		System.out.println("CreateEventHandler");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();

		params = Mapper.queryToMap(new String(b, "UTF-8"));

		WorkSql work = new WorkSql();
		Calendar date = Calendar.getInstance();
		String[] dates = params.get(EVENT_DATE).split("-");
		String width = params.get("width");
		String height = params.get("height");
		date.set(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]) - 1,
				Integer.parseInt(dates[0]));
		Event event;
		System.out.println("event name = " + params.get(EVENT_NAME));
		if ((width == null) || (height == null))
			event = new Event(params.get(EVENT_NAME), date, params.get(ADDRES),
					"0", "0");
		else
			event = new Event(params.get(EVENT_NAME), date, params.get(ADDRES),
					width, height);

		System.out.println("event with " + params.get(EVENT_NAME) + ",date "
				+ date + ", address " + params.get(ADDRES) + ", width " + width
				+ " " + height);
		if (work.addEvent(event) == 0) {
			work.makeInclude(params.get(LOGIN), event.getNameEvent());
			t.sendResponseHeaders(HTTP_OK_STATUS, EVENT_IS_OK.getBytes().length);
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
