import java.net.*;
import java.io.*;


public class ChatClientThread {
	private Socket socket = null;
	private ChatClient client = null;
	private DataInputStream streamIn = null;
	
	public ChatClientThread(ChatClient client, Socket socket) {
		this.client = client;
		this.socket = socket;
		open();
		try {
			client.start();
		} catch (IOException i) {
			System.out.println("Error starting: " + i.getMessage());
		}
	}
	
	public void open() {
		try {
			streamIn = new DataInputStream(socket.getInputStream());
		} catch(IOException i) {
			System.out.println("Error getting input stream: " + i);
			client.stop();
		}
	}
	
	public void close() {
		try {
			if (streamIn != null) {
				streamIn.close();
			}
		} catch(IOException i) {
			System.out.println("Error closing input stream: " + i);
		}
	}
	public void run() {
		while(true) {
			try {
				client.handle(streamIn.readUTF());
			} catch(IOException i) {
				System.out.println("Listening Error: " + i.getMessage());
				client.stop();
			}
		}
	}
}
