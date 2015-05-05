import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

	private static ServerSocket server;
	private static Socket client;

	public static void main(String[] args) throws IOException {
		try {
			server = new ServerSocket(5555);
			InetAddress address = server.getInetAddress();
			//client = new Socket(address, 5556);
			System.out.println("wait on ip = " + address.getHostAddress());
			Socket forServ = server.accept();

			InputStream servIn = forServ.getInputStream();
			OutputStream servOut = forServ.getOutputStream();

			//InputStream cliIn = client.getInputStream();
			//OutputStream cliOut = client.getOutputStream();
			String hello = "good bye";
			byte[] cliByte = new byte[1024];

			servOut.write(hello.getBytes(), 0, hello.getBytes().length);
			//cliIn.read(cliByte);
			System.out.println(" hello from server = " + new String(cliByte));
			forServ.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		try {
			
			server.close();
			//client.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
}