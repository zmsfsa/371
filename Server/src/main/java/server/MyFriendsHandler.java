package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MyFriendsHandler implements HttpHandler {
	private static final String DEL = "/";
	private static final String LOGIN = "login";
	private static final String FNAME = "fName";
    private static final String LNAME = "lName";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final int HTTP_OK_STATUS = 200;
	private static final String SERVER_PROBLEM = "problem with server database";

	@Override
	public void handle(HttpExchange t) throws IOException {

		System.out.println("MyFriendsHandler");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));

		WorkSql work = new WorkSql();
		User user = work.getUserByLogin(params.get("login"));
		
		if (user == null) {
			t.sendResponseHeaders(HTTP_OK_STATUS,
					SERVER_PROBLEM.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(SERVER_PROBLEM.getBytes());
			os.close();
		} else {
			StringBuilder sendBuild = new StringBuilder("");
			List<User> list = work.getFriendsOf(user.getIdUser());
			for(User u : list){
				sendBuild.append(FNAME + DELIMETR + u.getFName() + AND + LNAME + DELIMETR + u.getLName() + AND + LOGIN + u.getLogin() + DEL);
			}
			String send = new String(sendBuild);
			t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(send.getBytes());
			os.close();
		}
	}
}
