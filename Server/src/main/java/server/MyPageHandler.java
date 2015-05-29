package main.java.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import main.java.Photo;
import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MyPageHandler implements HttpHandler {

	private static final String PHOTO = "photo";
	private static final String DELIMETR = "=";
	private static final String FNAME = "fName";
	private static final String LNAME = "lName";
	private static final String PHONE = "phone";
	private static final String AND = "&";
	private static final String ADD_PHOTO = "addPhoto";
	private static final String DEL = "/";
	private static final String LOGIN = "login";
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

			t.sendResponseHeaders(HTTP_OK_STATUS, "".getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write("".getBytes());
			os.close();
			new Thread(new LoadPhoto(8081, params.get(LOGIN))).start();

		} else {
			int photoId;
			StringBuilder sendBuild = new StringBuilder("");

			photoId = user.getPhotoId();
			if(photoId != 0)
				sendBuild.append(PHOTO + DELIMETR + user.getPhotoId() + AND + FNAME + DELIMETR
					+ user.getFName() + AND + LNAME + DELIMETR
					+ user.getLName() + AND + PHONE + DELIMETR
					+ user.getPhone() + AND);
			else
				sendBuild.append(PHOTO + DELIMETR + 0 + AND + FNAME + DELIMETR
						+ user.getFName() + AND + LNAME + DELIMETR
						+ user.getLName() + AND + PHONE + DELIMETR
						+ user.getPhone() + AND);
			String send = new String(sendBuild);
			System.out.println("sent " + send);
			t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(send.getBytes());
			os.close();
			//if(photoId != 0)
				//new Thread(new SendPhoto(8081, photoId)).start();
			System.out.println("MyPage is over");

		}
	}
}
