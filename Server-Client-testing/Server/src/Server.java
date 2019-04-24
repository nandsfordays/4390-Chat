//Saurabh Shah

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

	//runs firsts, starts up the executor and reads port number
	
    public static void main(String[] args) throws Exception 
    {
    	//reads port number
    	Scanner scan = new Scanner(System.in);
    	int portnum = -1;
    	
    	try
    	{
    		System.out.println("Please enter an integer for the port number.");
    		portnum = scan.nextInt();
    		
    	}
    	catch(Exception e)
    	{
    		System.out.println("Error reading port number. Please enter an integer.");
    	}
    	
    	
    	//initialize ServerSocket and start thread
        try
        {
        	ServerSocket server = new ServerSocket(portnum);
        	Socket client = null;
        	
            System.out.println("The server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(1);
            
            //string msg for holding messages between client and server
            String msg = "";
            
            while (true) 
            {
            	//sends socket to ChatServer
            	client = server.accept();
                pool.execute(new ChatServer(client));
                
            }
            
        }
        catch(Exception e)
        {
 		   	System.out.println("Error :" + e.getMessage());
        }
    }

    
    
    private static class ChatServer implements Runnable {
        private Socket socket;
        int state = -1; //state does absolutely nothing, but it helped with debugging
        
        //reading from console
        Scanner console = new Scanner(System.in);
        
        //constructor
        public ChatServer(Socket socket) 
        {
            this.socket = socket;
        }

        //thread part. ON LEVEL - both server and client should be equals
        @Override
        public void run() 
        {
        	System.out.println("Connected: " + socket);
        	String msg = "";
            try 
            {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                
                //handler. Loops until exit condition is reached
                while (state != 0) 
                {
                	if(in.hasNextLine()) //read from client and send to console
                	{
                		state = 1;
                		//read from client
                		msg = in.nextLine();
                    	
                		//nullifies empty strings
                		if(msg.equals(""))
                		{
                			continue;
                		}
                		
                		
                    	//writes to console
                    	System.out.println(msg);
                    	
                    	//exit condition
                    	if(msg.equals(".over"))
                		{
                			break;
                		}
                    	
                	}
                	
                	if(console.hasNextLine()) //read from console and send to client
                	{
                		state = 2;
                		//read from console
                		msg = console.nextLine();
                		
                		//nullifies empty strings
                		if(msg.equals(""))
                		{
                			continue;
                		}
                		
                		//sends to client
                		out.println(msg);
                		
                		//exit condition
                		if(msg.equals(".over"))
                		{
                			break;
                		}
                		
                	}
                	
                	
                }
                
                in.close(); //is this needed?
            } 
            catch (Exception e) 
            {
                System.out.println("Error:" + socket);
            } 
            finally 
            {
                try 
                {
                	socket.close(); 
                	
                } 
                catch (IOException e) 
                {
                	
                }
                
                System.out.println("Closed: " + socket);
            }
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
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
			streamOut = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
			
			//reading from client
			String msg = "";
			
			while(!msg.equals(".over"))
			{
				try
				{
					msg = streamIn.readUTF();
					System.out.println(msg);
					
					//writing message back
					streamOut.writeUTF(msg.toUpperCase());
				}
	            catch(IOException i) 
	            { 
	                System.out.println(i); 
	                break;
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
	*/
}
