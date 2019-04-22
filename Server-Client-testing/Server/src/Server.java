import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {

	private ServerSocket server = null;
	private Socket client = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	
	
	public static void main(String args[])
	{
		Server myServer = null;
		Scanner scan = new Scanner(System.in);
		   try
		   {
			   System.out.println("Please enter an integer for the port number.");
			   int portnum = scan.nextInt();
			   scan.close();
			   myServer = new Server(portnum);
		   }
		   catch(Exception e)
		   {
			   System.out.println("Error reading port number. Please enter an integer.");
		   }
	}
	
	
	//constructor
	public Server(int port)
	{
		try
		{
			//setup
			server = new ServerSocket(port);
			client = server.accept();
			System.out.println("client accepted");
			streamIn = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		
			//reading from client
			String msg = "";
			
			while(!msg.equals(".over"))
			{
				try
				{
					msg = streamIn.readUTF();
					System.out.println(msg);
				}
	            catch(IOException i) 
	            { 
	                System.out.println(i); 
	            } 
			}
			
			//closing connection
			System.out.println("Closing connection"); 
            client.close(); 
            streamIn.close();
			
			
		}
		catch(IOException e)
		{  
			System.out.println("Error: " + e.getMessage()); 
		}
		
	}
	
}
