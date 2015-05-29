package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class MyPageHandler implements HttpHandler {

	private static final String PHOTO = "photo";
	private static final String DELIMETR = "=";
	private static final String FNAME = "fName";
	private static final String LNAME = "lName";
	private static final String DATE_DELIMETR = "-";
	private static final String PHONE = "phone";
	private static final String AND = "&";
	private static final String ADD_PHOTO = "addPhoto";
	private static final String BIRTH = "birth";
	private static final String DEL = "/";
	private static final String LOGIN = "login";
	private static final String EVENTS = "events";
	private static final int HTTP_OK_STATUS = 200;
	private static final String PHOTO_ID = "photo_id";

	@Override
	public void handle(HttpExchange t) throws IOException {

		System.out
				.println("===================================================================================================================================================================================================");
		System.out.println("MyPageHandler");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		System.out.println("bytes in message " + is.available());
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));

		System.out.println("login: " + params.get(LOGIN));
		System.out.println("add: " + params.get(ADD_PHOTO));
		WorkSql work = new WorkSql();
		User user = work.getUserByLogin(params.get(LOGIN));

		if (params.get(ADD_PHOTO).equals("YES")) {

			int port = 8080 - user.getIdUser();
			String res = "" + port;

			t.sendResponseHeaders(HTTP_OK_STATUS, res.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(res.getBytes());
			os.close();
			new Thread(new LoadPhoto(port, params.get(LOGIN))).start();

		} else {
			int photoId;
			StringBuilder sendBuild = new StringBuilder("");

			photoId = user.getPhotoId();
			Calendar calendar = user.getBirth();
			System.out.println("before calendar = " + calendar);
			if (calendar != null) {
				int month = calendar.get(Calendar.MONTH) + 1;
				sendBuild.append(BIRTH + DELIMETR + calendar.get(Calendar.DATE)
						+ DATE_DELIMETR + month + DATE_DELIMETR
						+ calendar.get(Calendar.YEAR) + AND);
			}
			if (photoId != 0)
				sendBuild.append(PHOTO + DELIMETR + user.getPhotoId() + AND
						+ FNAME + DELIMETR + user.getFName() + AND + LNAME
						+ DELIMETR + user.getLName() + AND + PHONE + DELIMETR
						+ user.getPhone() + AND + EVENTS + DELIMETR);
			else
				sendBuild.append(PHOTO + DELIMETR + 0 + AND + FNAME + DELIMETR
						+ user.getFName() + AND + LNAME + DELIMETR
						+ user.getLName() + AND + PHONE + DELIMETR
						+ user.getPhone() + AND + EVENTS + DELIMETR);

			List<Include> inList = work.getIncludeByLogin(params.get(LOGIN));
			for (Include i : inList) {
				if (!i.getHeight().equals("0") || !i.getWidth().equals("0")) {
					Event event = work.getEvent(i.getIdEvent());
					sendBuild.append(event.getNameEvent() + "-"
							+ event.getPhotoId() + ",");
				}
			}
			String send = new String(sendBuild);
			System.out.println("sent " + send);
			t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(send.getBytes());
			os.close();
			System.out.println("MyPage is over");

		}
	}
}
