package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SearchPageHandler implements HttpHandler{
	@Override
	public void handle(HttpExchange t) throws IOException {

		System.out.println("SearchPageHandler");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		System.out.println("b = " + new String(b));
		params = Mapper.queryToMap(new String(b));

		WorkSql work = new WorkSql();
		
	}

}
