package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Event;
import main.java.Friend;
import main.java.Include;
import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class OtherPageHandler implements HttpHandler {
	private static final String PHOTO = "photo";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final String FNAME = "fName";
	private static final String LNAME = "lName";
	private static final String BIRTH = "birth";
	private static final String DATE_DELIMETR = "-";
	private static final String DEL = "/";
	private static final String ADD_FRIEND = "addFriend";
	private static final String OK = "OK";
	private static final String ADD_PHOTO = "addPhoto";
	private static final String OTHER_LOGIN = "otherLogin";
	private static final String PHONE = "phone";
	private static final String LOGIN = "login";
	private static final String DELETE_PHOTO = "deletePhoto";
	private static final String EVENTS = "events";
	private static final int HTTP_OK_STATUS = 200;
	private static final String MISTAKE = "mistake";
	private static final String EVENT_NAME = "eventName";
	private static final String IS_FRIEND = "isFriend";
	private static final String DELETE_FRIEND = "deleteFriend";

	@Override
	public void handle(HttpExchange t) throws IOException {
		System.out
				.println("===================================================================================================================================================================================================");

		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));
		WorkSql work = new WorkSql();
		//add friends
		if (params.get(ADD_FRIEND) != null) {
			System.out.println("adding friend ");
			int id1 = work.getUserByLogin(params.get(LOGIN)).getIdUser();
			int id2 = work.getUserByLogin(params.get(OTHER_LOGIN)).getIdUser();
			Friend fr = new Friend(id1, id2);
			if(work.addFriend(fr) == 0){
				t.sendResponseHeaders(HTTP_OK_STATUS, OK.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(OK.getBytes());
				os.close();
			}else{
				t.sendResponseHeaders(HTTP_OK_STATUS, MISTAKE.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(MISTAKE.getBytes());
				os.close();
			}
			System.out.println("adding friend is ready");
			return;
		}
		//delete friends
		if(params.get(DELETE_FRIEND) != null){
			System.out.println("deleting friends ");
			int id1 = work.getUserByLogin(params.get(LOGIN)).getIdUser();
			int id2 = work.getUserByLogin(params.get(OTHER_LOGIN)).getIdUser();
			Friend fr = new Friend(id1, id2);
			if(work.deleteFriend(fr) == 0){
				t.sendResponseHeaders(HTTP_OK_STATUS, OK.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(OK.getBytes());
				os.close();
			}else{
				t.sendResponseHeaders(HTTP_OK_STATUS, MISTAKE.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(MISTAKE.getBytes());
				os.close();
			}
			System.out.println("deleting friend is ready");
			return;
		}
		//show page
		User otherUser = work.getUserByLogin(params.get(OTHER_LOGIN));
		
		int photoId;
		StringBuilder sendBuild = new StringBuilder("");
		
		boolean friend = false;
		List<User> usrL = work.getFriendsOf(work.getUserByLogin(params.get(OTHER_LOGIN)).getIdUser());
		for(User u : usrL){
			if(u.getLogin().equals(params.get(LOGIN)))
				friend = true;
		}

		if(friend)
			sendBuild.append(IS_FRIEND + DELIMETR + 1 + AND);
		else
			sendBuild.append(IS_FRIEND + DELIMETR + 0 + AND);

		photoId = otherUser.getPhotoId();
		Calendar calendar = otherUser.getBirth();
		if (calendar != null) {
			int month = calendar.get(Calendar.MONTH) + 1;
			sendBuild.append(BIRTH + DELIMETR + calendar.get(Calendar.DATE)
					+ DATE_DELIMETR + month + DATE_DELIMETR
					+ calendar.get(Calendar.YEAR) + AND);
		}
		if (photoId != 0)
			sendBuild.append(PHOTO + DELIMETR + otherUser.getPhotoId() + AND
					+ FNAME + DELIMETR + otherUser.getFName() + AND + LNAME
					+ DELIMETR + otherUser.getLName() + AND + PHONE + DELIMETR
					+ otherUser.getPhone() + AND + EVENTS + DELIMETR);
		else
			sendBuild.append(PHOTO + DELIMETR + 0 + AND + FNAME + DELIMETR
					+ otherUser.getFName() + AND + LNAME + DELIMETR
					+ otherUser.getLName() + AND + PHONE + DELIMETR
					+ otherUser.getPhone() + AND + EVENTS + DELIMETR);

		List<Include> inList = work.getIncludeByLogin(params.get(OTHER_LOGIN));
		for (Include i : inList) {
			if (!i.getHeight().equals("0") || !i.getWidth().equals("0")) {
				Event event = work.getEvent(i.getIdEvent());
				sendBuild.append(event.getNameEvent() + "-"
						+ event.getPhotoId() + ",");
			}
		}
		String send = new String(sendBuild);
		System.out.println("sent " + send);
		t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
		OutputStream os = t.getResponseBody();
		os.write(send.getBytes());
		os.close();
		System.out.println("OtherPage sent");
		
		

	}
}
