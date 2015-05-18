package main.java.server;

public class HttpServerTest {

	private static final String CONTEXT = "/app";
	private static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		
		SimpleHttpServer simpleHttpServer = new SimpleHttpServer(PORT, CONTEXT,
				new HttpRequestHandler());

		simpleHttpServer.start();
		System.out.println("Server is started and listening on port "+ PORT);
	}

}
