package main.java.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ImageSender implements HttpHandler {

	private static final int HTTP_OK_STATUS = 200;
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		URI uri = t.getRequestURI();
		int id = Integer.parseInt(uri.getQuery());
		WorkSql work = new WorkSql();
		
		byte[] photo = work.getPhoto(id).getPhotoFile();
		
		t.sendResponseHeaders(HTTP_OK_STATUS, photo.length);
		OutputStream os = t.getResponseBody();
		os.write(photo);
		os.close();
		
	}
	

}
