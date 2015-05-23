package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MapHandler implements HttpHandler {
	
	private static final int HTTP_OK_STATUS = 200;
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		
		System.out.println("someone posted");
		
		System.out.println("method = " + t.getRequestMethod());
		System.out.println("length - " + t.getRequestBody().available());
		Map<String,String> params = new HashMap<String, String>();
		InputStream is = t.getRequestBody();
		
		byte[] b = new byte[t.getRequestBody().available()];
		is.read(b);
		System.out.println("is = " + new String(b));
		params = Mapper.queryToMap(new String(b));
		System.out.println("login = " + params.get("user"));
		String a = params.get("message");
		t.sendResponseHeaders(HTTP_OK_STATUS, a.getBytes().length);
		OutputStream os = t.getResponseBody();
		os.write(a.getBytes());
		os.close();
		
	}
}
