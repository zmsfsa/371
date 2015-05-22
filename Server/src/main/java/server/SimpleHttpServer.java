package main.java.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

//@SuppressWarnings("restriction")
public class SimpleHttpServer {
	private static final String REG_CONTEXT = "/reg/";
	private static final String LOG_CONTEXT = "/log";
	private static final String HELLO_CONTEXT = "/hello";
	private static final String MY_PAGE_CONTEXT = "/me/";
	private static final String MAP_CONTEXT = "/map/";
	private static final String EVENT_CREATE = "/event/create";
	private static final String IVENT_CONTEXT = "/event";
	private HttpServer httpServer;

	public SimpleHttpServer(int port, HttpHandler handler) {
		try {
			httpServer = HttpServer.create(new InetSocketAddress(port), 0);
			httpServer.createContext(REG_CONTEXT, new RegHandler());
			httpServer.createContext(LOG_CONTEXT, new LogHandler());
			httpServer.createContext("/app", new HttpRequestHandler());
			httpServer.createContext(EVENT_CREATE, new CreateEventHandler());
			/*httpServer.createContext(HELLO_CONTEXT, new HelloHandler());
			httpServer.createContext(MY_PAGE_CONTEXT, new MyPageHandler());
			httpServer.createContext(MAP_CONTEXT, new MapHandler());*/
			httpServer.createContext(IVENT_CONTEXT, new EventHandler());
			
			
			//httpServer.setExecutor(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void start() {
		this.httpServer.start();
	}

}
