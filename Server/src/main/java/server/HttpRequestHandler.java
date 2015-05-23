package main.java.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

//@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {

	private static final int HTTP_OK_STATUS = 200;

	public void handle(HttpExchange t) throws IOException {

		URI uri = t.getRequestURI();
		String response = createResponseFromQueryParams(uri);

		t.sendResponseHeaders(HTTP_OK_STATUS, response.getBytes().length);
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	private String createResponseFromQueryParams(URI uri) {

		String query = uri.getQuery();
		System.out.println("hello " + query);
		if (query != null) {
			System.out.println("Query: " + query);
			if (query.equals("hi"))
				return "continue";
			
		}
		return "no";
	}

}
