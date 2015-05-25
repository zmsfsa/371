package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

public class EventListHandler implements HttpHandler {

	private static final String DEL = "/";
	private static final String NAME = "name";
	private static final String DATE = "date";
	private static final String DATE_DELIMETR = "-";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final int HTTP_OK_STATUS = 200;
	private static final String SERVER_PROBLEM = "problem with server database";

	@Override
	public void handle(HttpExchange t) throws IOException {

		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		System.out.println("b = " + new String(b));
		params = Mapper.queryToMap(new String(b));

		WorkSql work = new WorkSql();
		User user = work.getUserByLogin(params.get("login"));

		if (user == null) {
			t.sendResponseHeaders(HTTP_OK_STATUS,
					SERVER_PROBLEM.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(SERVER_PROBLEM.getBytes());
			os.close();
		} else {
			StringBuilder sendBuild = new StringBuilder("");
			List<Include> list = work.getIncludeByLogin(params.get("login"));
			for (Include inc : list) {
				Event event = work.getEvent(inc.getIdEvent());
				if (event != null) {
					Calendar calendar = event.getDateEvent();
					int month = calendar.get(Calendar.MONTH) + 1;
					sendBuild.append(NAME + DELIMETR + event.getNameEvent()
							+ AND + DATE + DELIMETR
							+ +calendar.get(Calendar.DATE) + DATE_DELIMETR
							+ month + DATE_DELIMETR
							+ calendar.get(Calendar.YEAR) + DEL);
				} else {
					ArrayList<Include> l = new ArrayList<Include>();
					l.add(inc);
					work.deleteInclude(l);
				}
			}
			String send = new String(sendBuild);
			t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(send.getBytes());
			os.close();
		}
	}
}
