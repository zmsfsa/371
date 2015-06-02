package main.java.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpServerTest {

	private static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		SimpleHttpServer simpleHttpServer = new SimpleHttpServer(PORT);

		simpleHttpServer.start();
		System.out.println("Server is started and listening on port " + PORT);
		

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
