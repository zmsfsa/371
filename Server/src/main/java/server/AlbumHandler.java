package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Event;
import main.java.Include;
import main.java.Photo;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AlbumHandler implements HttpHandler {

	private static final String PHOTO = "photo";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final String DEL = "/";
	private static final String IN = "in";
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
		Event event = work.getEventByName(params.get(EVENT_NAME));

		if (params.get(DELETE_PHOTO) != null) {
			System.out.println("deleting photo");
			ArrayList<Photo> delList = new ArrayList<Photo>();
			String[] photos = params.get(PHOTO).split("-");
			for (String photo : photos) {
				if (!photo.equals("")) {
					delList.add(work.getPhoto(Integer.parseInt(photo)));
					System.out.println("added to delList "
							+ Integer.parseInt(photo));
				}
			}
			work.deletePhoto(delList);
			t.sendResponseHeaders(HTTP_OK_STATUS, OK.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(OK.getBytes());
			os.close();
			return;
		}

		if (params.get(ADD_PHOTO) != null) {

			System.out.println("add photo");
			int port = 8080 - event.getIdEvent();
			String res = "" + port;

			t.sendResponseHeaders(HTTP_OK_STATUS, res.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(res.getBytes());
			os.close();
			new Thread(new LoadPhoto(port, params.get(EVENT_NAME), false))
					.start();

		} else {
			System.out.println("AlbumHandler");
			StringBuilder sendBuilder = new StringBuilder("");
			List<Include> inList = work.getIncludeByLogin(params.get(LOGIN));
			boolean in = false;
			for (Include inside : inList) {
				if (inside.getIdEvent() == event.getIdEvent())
					if (!inside.getHeight().equals("0"))
						in = true;
			}
			System.out.println();
			if (in == true)
				sendBuilder.append(IN + DELIMETR + 1 + AND);
			else
				sendBuilder.append(IN + DELIMETR + 0 + AND);
			List<Photo> photoList = work.getPhotoByEvent(event.getIdEvent());
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
}
