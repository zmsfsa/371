package main.java.server;

import java.util.List;

import main.java.Include;
import main.java.WorkSql;

public class HttpServerTest {

	private static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		SimpleHttpServer simpleHttpServer = new SimpleHttpServer(PORT,
				new HttpRequestHandler());

		simpleHttpServer.start();
		System.out.println("Server is started and listening on port " + PORT);
	}
		

}
