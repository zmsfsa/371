package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Event;
import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SearchPageHandler implements HttpHandler {

	private static final String FNAME = "fName";
	private static final String LNAME = "lName";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final String PHOTO = "photo";
	private static final String LOGIN = "login";
	private static final String EVENT_NAME = "eventName";
	private static final String DEL = "/";
	private static final int HTTP_OK_STATUS = 200;

	@Override
	public void handle(HttpExchange t) throws IOException {

		System.out.println("SearchPageHandler");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));

		WorkSql work = new WorkSql();
		if (params.get(FNAME) != null) {
			StringBuilder sendBuild = new StringBuilder("");
			List<User> usL = work.findUser();
			ArrayList<User> result = new ArrayList<User>();
			String fname = params.get(FNAME);
			String lname = params.get(LNAME);
			for (User u : usL) {
				if ((u.getFName().equals(fname))
						|| (u.getLName().equals(fname))
						|| (u.getFName().equals(lname))
						|| (u.getLName().equals(lname)))
					sendBuild.append(LOGIN + DELIMETR + u.getLogin() + AND
							+ FNAME + DELIMETR + u.getFName() + AND + LNAME
							+ DELIMETR + u.getLName() + AND + PHOTO + DELIMETR
							+ u.getPhotoId() + DEL);
			}
			String send = new String(sendBuild);
			t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(send.getBytes());
			os.close();
			return;
		} else {
			StringBuilder sendBuild = new StringBuilder("");
			List<Event> evL = work.findEvent();
			ArrayList<Event> result = new ArrayList<Event>();
			String eName = params.get(EVENT_NAME);
			for (Event e : evL) {
				if (e.getNameEvent().contains(eName))
					sendBuild.append(EVENT_NAME + DELIMETR + e.getNameEvent()
							+ AND + PHOTO + DELIMETR + e.getPhotoId() + DEL);
			}
			String send = new String(sendBuild);
			t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(send.getBytes());
			os.close();
			return;
		}

	}

}
