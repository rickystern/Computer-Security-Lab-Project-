package application.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;
	private int port;
	
	public Server() {
		this.port = 10000;
		
		try {
			this.serverSocket = new ServerSocket(this.port);
			
			this.acceptConnection();
		}
		catch (IOException exception) {
			System.err.println("Error creating server with port: "+this.port);
			exception.printStackTrace();
		}
		catch(Exception exception) {
			System.err.println("An unknown exception was thrown");
			exception.printStackTrace();
		}
	}
	
	public void acceptConnection() throws IOException, ClassNotFoundException {
		int counter = 1;
		
//		System.out.println("Waiting for Clients\n");
		
		while(counter < 100) {
			System.out.println("Waiting for Client: #"+counter);
			Socket socket = this.serverSocket.accept();
			
			new Thread(new ServerThread(socket)).start();
			counter++;
		}
		
		this.serverSocket.close();
	}
	
	public static void main(String [] args) {
		new Server();
	}
}