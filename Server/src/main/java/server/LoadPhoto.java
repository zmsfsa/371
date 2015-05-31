package main.java.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.Photo;
import main.java.WorkSql;

public class LoadPhoto implements Runnable {

	private int port;
	private String login;

	public LoadPhoto(int port, String login) {
		this.port = port;
		this.login = login;
	}

	@Override
	public void run() {
		WorkSql work = new WorkSql();
		ServerSocket server = null;
		Socket socket = null;
		byte[] data = new byte[1];
		try {
			server = new ServerSocket(port);
			System.out.println("Server Waiting for image");

			socket = server.accept();
			System.out.println("Client connected.");

			InputStream in = socket.getInputStream();
			DataInputStream dis = new DataInputStream(in);

			int len = dis.readInt();

			data = new byte[len];
			dis.readFully(data);
			dis.close();
			in.close();

			Photo addPhoto = new Photo(data, 0);
			work.addPhoto(addPhoto, login);

			System.out.println("data length = " + data.length);
		} catch (IOException e) {

		} finally {
			try {
				socket.close();
				server.close();
				System.out.println("closed recieve sockets");
			} catch (IOException e) {
			}
		}

	}
}
