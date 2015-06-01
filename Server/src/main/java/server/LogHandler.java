package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LogHandler implements HttpHandler {
	private SimpleHttpServer myServ;

	public LogHandler(SimpleHttpServer myServ) {
		this.myServ = myServ;
	}

	private static final String REG_CONTEXT = "/reg";
	private static final String LOG_CONTEXT = "/log";
	private static final String EVENT_CREATE = "/event/create";
	private static final String EVENT_CONTEXT = "/event";
	private static final String INCLUDE_LIST = "/includeList";
	private static final String EVENT_LIST = "/event_list";
	private static final String MY_FRIENDS = "/friends";
	private static final String ALBUM_CONTEXT = "/album";
	private static final String MY_PAGE = "/myPage";
	private static final String OTHER_PAGE = "/otherPage";
	private static final String PHOTO = "/photo";
	private static final String SEARCH_CONTEXT = "/search";

	private static final int HTTP_OK_STATUS = 200;
	private static final String LOG_IS_OK = "continue";
	private static final String WRONG_PWD = "wrong password";
	private static final String NO_SUCH_USER = "no users with this name";

	@Override
	public void handle(HttpExchange t) throws IOException {
		System.out
		.println("===================================================================================================================================================================================================");

		System.out.println("someone logging");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));
		WorkSql work = new WorkSql();
		User user = work.getUserByLogin(params.get("login"));
		if (user == null) {
			t.sendResponseHeaders(HTTP_OK_STATUS,
					NO_SUCH_USER.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(NO_SUCH_USER.getBytes());
			os.close();
			return;
		}
		if (user.getPassword().equals(params.get("pwd"))) {
			t.sendResponseHeaders(HTTP_OK_STATUS, LOG_IS_OK.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(LOG_IS_OK.getBytes());
			os.close();
			String login = params.get("login");
			myServ.httpServer.createContext(PHOTO, new ImageSender());
			myServ.httpServer.createContext(EVENT_CREATE + login,
					new CreateEventHandler());
			myServ.httpServer.createContext(MY_FRIENDS + login,
					new MyFriendsHandler());
			myServ.httpServer.createContext(MY_PAGE + login,
					new MyPageHandler());
			myServ.httpServer.createContext(EVENT_CONTEXT + login,
					new EventHandler());
			myServ.httpServer.createContext(EVENT_LIST + login,
					new EventListHandler());
			myServ.httpServer.createContext(SEARCH_CONTEXT + login,
					new SearchPageHandler());
			myServ.httpServer.createContext(ALBUM_CONTEXT + login,
					new AlbumHandler());
			myServ.httpServer.createContext(OTHER_PAGE + login,
					new OtherPageHandler());
			myServ.httpServer.createContext(INCLUDE_LIST + login,
					new IncludeListHandler());
			System.out.println("logged in");
		} else {

			t.sendResponseHeaders(HTTP_OK_STATUS, WRONG_PWD.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(WRONG_PWD.getBytes());
			os.close();

		}
	}

}