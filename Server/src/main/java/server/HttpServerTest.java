package main.java.server;

public class HttpServerTest {

	private static final int PORT = 8081;

	public static void main(String[] args) throws Exception {
		SimpleHttpServer simpleHttpServer = new SimpleHttpServer(PORT);

		simpleHttpServer.start();
		System.out.println("Server is started and listening on port " + PORT);

	}
		

}
