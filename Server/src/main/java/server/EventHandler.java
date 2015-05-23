package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Event;
import main.java.Include;
import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EventHandler implements HttpHandler {

	private static final String HANDLER_NAME = "EventHandler";
	private static final int HTTP_OK_STATUS = 200;
	private static final String EVENT_IS_BAD = "No such event";
	private static final String DATE_DELIMETR = "-";
	private static final String DATA_CONFLICT = "wrong data came";
	private static final String DELIMETR = "=";
	private static final String DATE = "date";
	private static final String IN = "in";
	private static final String OUT = "out";

	@Override
	public void handle(HttpExchange t) throws IOException {

		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String,String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));
		
		WorkSql work = new WorkSql();
		Event event = work.getEventByName(params.get("eventName"));
		if (event == null) {
			t.sendResponseHeaders(HTTP_OK_STATUS,
					EVENT_IS_BAD.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(EVENT_IS_BAD.getBytes());
			os.close();
		} else {
			Calendar calendar = event.getDateEvent();
			int month = calendar.get(Calendar.MONTH) + 1;
			StringBuilder sendBuilder = new StringBuilder(DATE + DELIMETR);
			List<Include> checkList = work.getIncludeByEvent(event
					.getNameEvent());
			int k = 0;
			int idUser = work.getUserByLogin(params.get("login")).getIdUser();
			for (Include a : checkList)
				if (a.getIdUser() == idUser)
					k = 1;
			if (k == 1) {
				sendBuilder.append(IN + DELIMETR
						+ calendar.get(Calendar.DATE) + DATE_DELIMETR
						+ month + DATE_DELIMETR
						+ calendar.get(Calendar.YEAR) + DELIMETR);
			} else {
				sendBuilder.append(OUT + DELIMETR
						+ calendar.get(Calendar.DATE) + DATE_DELIMETR
						+ month + DATE_DELIMETR
						+ calendar.get(Calendar.YEAR));
			}

			for (Include a : checkList) {
				User u = work.getUser(a.getIdUser());
				sendBuilder.append(DELIMETR + u.getFName() + " " + u.getLName());
			}

			String send = new String(sendBuilder);
			t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(send.getBytes());
			os.close();
		}
	}

}
