//Saurabh Shah


import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) throws Exception 
	{
		//reads port number
		Scanner scan = new Scanner(System.in);
		int portnum = -1;
		String msg = "";
		int state = -1;
		
		try
		{
			System.out.println("Please enter an integer for the port number.");
			portnum = scan.nextInt();
			scan.nextLine();
			
		}
		catch(Exception e)
		{
			System.out.println("Error reading port number. Please enter an integer.");
		}
		
		
		//ON LEVEL - server and client are equals
        try (Socket socket = new Socket("127.0.0.1", portnum)) 
        {
            System.out.println("Enter lines of text");  
            
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            //handler - loops and checks for input on console and server
            while (state != 0) 
            {
            	if(scan.hasNextLine()) //reading from console and sending to server
                {
                	state = 1; 
                	//read from console
                	msg = scan.nextLine();
                	
                	//ignore empty strings
                	if(msg.equals(""))
            		{
            			continue;
            		}
                	
                	//send to server
                    out.println(msg);
                    
                    //exit condition
                    if(msg.equals(".over"))
            		{
            			break;
            		}
                    
                }
            	
            	if(in.hasNextLine()) //reading from server and sending to console
                {
                	state = 2; 
                	//read from server
                	msg = in.nextLine();
                	
                	//ignore empty string
                	if(msg.equals(""))
            		{
            			continue;
            		}
                	
                	//send to console
                    System.out.println(msg);
                    
                    //exit condition
                    if(msg.equals(".over"))
            		{
            			break;
            		}
                    
                }
            	
            }
            in.close();
        }
        catch(Exception e)
		{
			System.out.println("Error :" + e.getMessage());
		}
        
        scan.close();
        
        
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	private Socket socket = null;
	private Scanner streamIn = null;
	private DataOutputStream streamOut = null;
	private DataInputStream socketIn = null;
	
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
			socketIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			
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
					else //hard coded message in case read fails
					{
						msg = "error reading string";
						streamOut.writeUTF(msg);
					}
					
					//reading from server
					msg = socketIn.readUTF();
					System.out.println(msg);
				}
				catch(Exception e)
				{
					System.out.println("Error :" + e.getMessage());
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
	
	*/
	
}
