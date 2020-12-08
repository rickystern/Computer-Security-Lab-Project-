package application.network;

import java.io.IOException;
import java.net.Socket;

public class Network {
	private String host;
	private int port;
	
	public Network(String type) {
		this.host = "192.168.111.42";
		this.port = 10000;
		
		try {
			Socket socket = new Socket(this.host, this.port);
			new Thread(new NetworkThread(type, socket)).start();;
		}
		catch (IOException exception) {
			System.out.println("Error connecting to server");
		}
	}
}