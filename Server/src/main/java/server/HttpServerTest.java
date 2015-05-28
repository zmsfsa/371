package main.java.server;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.Photo;
import main.java.WorkSql;

public class HttpServerTest {

	private static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		SimpleHttpServer simpleHttpServer = new SimpleHttpServer(PORT);

		simpleHttpServer.start();
		System.out.println("Server is started and listening on port " + PORT);

	}
		

}
