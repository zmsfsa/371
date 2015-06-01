package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Event;
import main.java.Include;
import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class IncludeListHandler implements HttpHandler {
	private static final String PHOTO = "photo";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final String DEL = "/";
	private static final String IN = "in";
	private static final String OK = "OK";
	private static final String ADD_PHOTO = "addPhoto";
	private static final String OTHER_LOGIN = "login";
	private static final String FNAME = "fName";
	private static final String LNAME = "lName";
	private static final String LOGIN = "login";
	private static final String DELETE_PHOTO = "deletePhoto";
	private static final int HTTP_OK_STATUS = 200;
	private static final String EVENT_NAME = "eventName";

	@Override
	public void handle(HttpExchange t) throws IOException {
		System.out
				.println("===================================================================================================================================================================================================");

		System.out.println("IncludeListHandler");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));
		WorkSql work = new WorkSql();
		Event event = work.getEventByName(params.get(EVENT_NAME));
		List<Include> inList = work.getIncludeByEvent(event.getNameEvent());

		StringBuilder sendBuilder = new StringBuilder("");
		for (Include in : inList) {
			User user = work.getUser(in.getIdUser());
			if(user != null)
				sendBuilder.append(OTHER_LOGIN + DELIMETR + user.getLogin() + AND
					+ FNAME + DELIMETR + user.getFName() + AND + LNAME
					+ DELIMETR + user.getLName() + AND + PHOTO + DELIMETR
					+ user.getPhotoId() + DEL);
		}
		
		String send = new String(sendBuilder);
		
		t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
		OutputStream os = t.getResponseBody();
		os.write(send.getBytes());
		os.close();
		System.out.println("IncludeListHandler sent " + send);

	}

}
