
import java.net.*;
import java.io.*;

public class Server {
	
	private Socket clientsocket = null;
	private ServerSocket serversocket = null;
	private DataInputStream instream = null;
	
	public Server(int port)
	{
		try
		{
			serversocket = new ServerSocket(port);
			System.out.println("Server started"); 
			  
            System.out.println("Waiting for client"); 
  
            clientsocket = serversocket.accept(); 
            System.out.println("Client accepted"); 
            
            //read from client
		}
		
		
		
	}
	
	
	
	
	
	
	

}
