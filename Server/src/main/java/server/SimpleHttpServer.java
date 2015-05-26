package main.java.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

//@SuppressWarnings("restriction")
public class SimpleHttpServer {
	private static final String REG_CONTEXT = "/reg";
	private static final String LOG_CONTEXT = "/log";
	private static final String EVENT_CREATE = "/event/create";
	private static final String EVENT_CONTEXT = "/event";
	private static final String EVENT_LIST = "/event_list";
	private HttpServer httpServer;

	public SimpleHttpServer(int port, HttpHandler handler) {
		try {
			httpServer = HttpServer.create(new InetSocketAddress(port), 0);
			httpServer.createContext(REG_CONTEXT, new RegHandler());
			httpServer.createContext(LOG_CONTEXT, new LogHandler());
			httpServer.createContext("/app", new HttpRequestHandler());
			httpServer.createContext(EVENT_CREATE, new CreateEventHandler());
			/*httpServer.createContext(HELLO_CONTEXT, new HelloHandler());
			httpServer.createContext(MY_PAGE_CONTEXT, new MyPageHandler());*/
			httpServer.createContext("/post", new MapHandler());
			httpServer.createContext(EVENT_CONTEXT, new EventHandler());
			httpServer.createContext(EVENT_LIST, new EventListHandler());
			
			
			//httpServer.setExecutor(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void start() {
		this.httpServer.start();
	}

}
