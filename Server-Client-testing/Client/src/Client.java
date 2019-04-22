
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
	
	private Socket socket = null;
	private Scanner streamIn = null;
	private DataOutputStream streamOut = null;
	
	public static void main(String args[])
	{
		Client myClient = null;
		Scanner scan = new Scanner(System.in);
		   try
		   {
			   System.out.println("Please enter an integer for the port number.");
			   int portnum = scan.nextInt();
			   scan.close();
			   myClient = new Client("127.0.0.1",portnum);
		   }
		   catch(Exception e)
		   {
			   System.out.println("Error reading port number. Please enter an integer.");
		   }
	}
	
	//constructor
	public Client(String address, int port)
	{
		try
		{
			//setup
			socket = new Socket(address,port);
			streamIn = new Scanner(System.in);
			streamOut = new DataOutputStream(socket.getOutputStream());
			
			//write to socket
			String msg = "";
			
			while(!msg.equals(".over"))
			{
				try 
				{
					if(streamIn.hasNext())
					{
						msg = streamIn.next();
						streamOut.writeUTF(msg);
					}
				}
				catch(Exception e)
				{
					System.out.println("Error reading port number. Please enter an integer.");
				}
			}
			
			//close connection
			try
	        { 
	            streamIn.close(); 
	            streamOut.close(); 
	            socket.close(); 
	        } 
	        catch(IOException i) 
	        { 
	            System.out.println(i); 
	        } 
			
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		
		
		
	}
	
	
}
