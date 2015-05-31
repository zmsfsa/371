package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Photo;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AlbumHandler implements HttpHandler {

	private static final String PHOTO = "photo";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final String DEL = "/";
	private static final String LOGIN = "login";
	private static final int HTTP_OK_STATUS = 200;
	private static final String EVENT_NAME = "eventName";

	@Override
	public void handle(HttpExchange t) throws IOException {
		System.out
				.println("===================================================================================================================================================================================================");
		System.out.println("AlbumHandler");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		System.out.println("bytes in message " + is.available());
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));

		WorkSql work = new WorkSql();
		StringBuilder sendBuilder = new StringBuilder("");
		List<Photo> photoList = work.getPhotoByEvent(work.getEventByName(
				params.get(EVENT_NAME)).getIdEvent());
		for (Photo onePhoto : photoList) {
			if (onePhoto != null)
				sendBuilder.append(PHOTO + DELIMETR + onePhoto.getPhotoId()
						+ DEL);

		}
		
		String send = new String(sendBuilder);
		t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
		OutputStream os = t.getResponseBody();
		os.write(send.getBytes());
		os.close();
		System.out.println("AlbumHandler sent " + send);

	}

}
