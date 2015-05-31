package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class OtherPageHandler implements HttpHandler {
	private static final String PHOTO = "photo";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final String DEL = "/";
	private static final String ADD_FRIEND = "addFriend";
	private static final String OK = "OK";
	private static final String ADD_PHOTO = "addPhoto";
	private static final String LOGIN = "login";
	private static final String DELETE_PHOTO = "deletePhoto";
	private static final int HTTP_OK_STATUS = 200;
	private static final String EVENT_NAME = "eventName";

	@Override
	public void handle(HttpExchange t) throws IOException {
		System.out
				.println("===================================================================================================================================================================================================");

		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));
		WorkSql work = new WorkSql();

		if (params.get(ADD_FRIEND) != null) {
			System.out.println("adding friend ");
			
			System.out.println("adding friend is ready");
			return;
		}
		
		

	}
}
