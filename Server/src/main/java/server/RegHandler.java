package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RegHandler implements HttpHandler {

	private static final int HTTP_OK_STATUS = 200;
	private static final String REG_IS_OK = "OK";
	private static final String REG_IS_BAD = "User with such name already exists";
	private static final String DATE = "date";
	private static final String DATE_DELIMETR = "-";
	private static final String DELIMETR = "=";

	@Override
	public void handle(HttpExchange t) throws IOException {
		System.out
		.println("===================================================================================================================================================================================================");

		System.out.println("someone registrating");
		
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String,String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));
		
		WorkSql work = new WorkSql();
		Calendar date = Calendar.getInstance();
		String[] dates = params.get("birth").split("-");
		date.set(Integer.parseInt(dates[2]),
				Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[0]));
		Calendar calendar = date;
		int month = calendar.get(Calendar.MONTH) + 1;
		System.out.println(DATE + DELIMETR
				+ calendar.get(Calendar.DATE) + DATE_DELIMETR
				+ month + DATE_DELIMETR
				+ calendar.get(Calendar.YEAR));
		User newUser = new User(params.get("login"), params.get("pwd"), params.get("fName"), params.get("lName"), params.get("phone"), 0, date);
		if(hasE(params.get("login")) || hasE(params.get("pwd")) || hasE(params.get("fName")) || hasE(params.get("lName"))){
				t.sendResponseHeaders(HTTP_OK_STATUS,
						"wrong input type".getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write("wrong input type".getBytes());
				os.close();
				System.out.println("wrong input type");
				return;
		}
		if (work.addUser(newUser) != 0) {
			t.sendResponseHeaders(HTTP_OK_STATUS,
					REG_IS_BAD.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(REG_IS_BAD.getBytes());
			os.close();
			System.out.println(REG_IS_BAD);
		} else{
			t.sendResponseHeaders(HTTP_OK_STATUS,
					REG_IS_OK.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(REG_IS_OK.getBytes());
			os.close();
			System.out.println("registrated");
		}

	}
	
	private static Pattern pattern0_9__a_z__A_Z = Pattern.compile(
            "[\\w\\u005F\\u002E]+", Pattern.UNICODE_CASE);

    public static boolean hasE(String str) {
        String[] a = str.split(" ");
        boolean res = false;
        for (String b : a) {
            Matcher m = pattern0_9__a_z__A_Z.matcher(b);
            if (m.matches()) {
                ;
            } else {
                return true;
            }
        }
        return res;
    }

}
