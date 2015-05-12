package fff;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

//@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {

	private static final String F_NAME = "fname";
	private static final String L_NAME = "lname";

	private static final int PARAM_NAME_IDX = 0;
	private static final int PARAM_VALUE_IDX = 1;

	private static final int HTTP_OK_STATUS = 200;

	private static final String AND_DELIMITER = "&";
	private static final String EQUAL_DELIMITER = "=";

	public void handle(HttpExchange t) throws IOException {

		URI uri = t.getRequestURI();
		String response = createResponseFromQueryParams(uri);
		System.out.println("Response: " + response + ", tostring= "
				+ uri.toString() + ", getpath= " + uri.getPath()
				+ ", getQuery =" + uri.getQuery() + ", getAthority="
				+ uri.getAuthority() + ", getScheme = " + uri.getScheme());
		t.sendResponseHeaders(HTTP_OK_STATUS, response.getBytes().length);
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	private String createResponseFromQueryParams(URI uri) {

		String fName = "";
		String lName = "";
		String query = uri.getPath();
		if (query != null) {
			System.out.println("Query: " + query);
			String answer = query.substring("/app/".length());
			System.out.println("answer = " + answer);
			String[] queryParams = answer.split("-");

			System.out.println("answer1 = "
					+ (queryParams.length > 0 ? queryParams[0] : null)
					+ ", answer2 = "
					+ (queryParams.length > 1 ? queryParams[1] : null));

			int nameCount = 0;
			if (queryParams.length > 0) {
				for (String qParam : queryParams) {
					if (nameCount == 0) {
						fName = qParam;
						nameCount++;
					}
					if (nameCount == 1)
						lName = qParam;
				}
			}
		}
		return "Hello, " + fName + " " + lName;
	}

}
