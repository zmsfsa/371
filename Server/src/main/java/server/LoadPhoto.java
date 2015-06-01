package main.java.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import main.java.Event;
import main.java.Photo;
import main.java.WorkSql;

public class LoadPhoto implements Runnable {

	private int port;
	private String thingName;
	private boolean ava;

	public LoadPhoto(int port, String name, boolean ava) {
		this.port = port;
		this.thingName = name;
		this.ava = ava;
	}

	@Override
	public void run() {
		System.out
		.println("===================================================================================================================================================================================================");

		System.out.println("Load photo");
		WorkSql work = new WorkSql();
		ServerSocket server = null;
		Socket socket = null;
		Event event = null;
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

			if (ava == false) {
				event = work.getEventByName(thingName);
				Photo addPhoto = null;
				if (event != null)
					addPhoto = new Photo(data, event.getIdEvent());
				if(addPhoto != null)
					work.addPhoto(addPhoto);
			} else {
				Photo addPhoto = new Photo(data, 0);
				work.addPhoto(addPhoto, thingName);
			}

			System.out.println("data length = " + data.length);
		} catch (IOException e) {
			System.out.println("exception in loading image " + e.getMessage());

		} finally {
			try {
				socket.close();
				server.close();
				System.out.println("closed load sockets");
			} catch (IOException e) {
			}
		}
		
		if(event != null){
			List<Photo> phList = work.getPhotoByEvent(event.getIdEvent());
			int size = phList.size();
			Random r = new Random();
			int number = r.nextInt(size);
			Photo p = phList.get(number);
			event.setPhotoId(p.getPhotoId());
			work.updateEvent(event);
		}

	}
}
