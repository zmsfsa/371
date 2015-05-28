package main.java.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.WorkSql;

public class SendPhoto implements Runnable {

	private int port;
	private int photoId;

	public SendPhoto(int port, int photoId) {
		this.port = port;
		this.photoId = photoId;
	}

	@Override
	public void run() {
		ServerSocket serv = null;
		Socket socket = null;

		WorkSql work = new WorkSql();
		byte[] photo = work.getPhoto(photoId).getPhotoFile();
		try {
			serv = new ServerSocket(port);
			System.out.println("started");

			socket = serv.accept();
			System.out.println("connected");

			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(photo.length);
			dos.write(photo);
			System.out.println("written " + photo.length);

			InputStream is = socket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			System.out.println("I sent and wait");
			int ready = dis.readInt();
			System.out.println("ready = " + ready);
			dos.close();
			os.close();
			dis.close();
			is.close();
			System.out.println("closed send streams");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				serv.close();
				System.out.println("sockets send closed");
			} catch (Exception e) {
			}
		}
	}

}
