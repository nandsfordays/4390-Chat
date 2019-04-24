//Saurabh Shah


import java.net.*;
import java.io.*;
import java.util.Scanner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
	
	public static void main(String[] args) throws Exception 
	{
		//reads port number
		Scanner scan = new Scanner(System.in);
		int portnum = -1;
		
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
		Socket socket = new Socket("127.0.0.1", portnum);
        try 
        {
            System.out.println("Enter lines of text");  
            
            ExecutorService pool = Executors.newFixedThreadPool(1);
            
            pool.execute(new ChatClient(socket));
            
            //CODE for reading from console
            String msg = "";
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    		while(true)
    		{
    			if(scan.hasNextLine()) //reading from console and sending to server
                {
                	
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
                    	System.out.println("Connection terminated");
            			break;
            		}
                    
                }
    		}
            
            
        }
        catch(Exception e)
		{
			System.out.println("Error :" + e.getMessage());
		}
        
        scan.close();
        
        
    }
	
	
	
	
	private static class ChatClient implements Runnable {
        private Socket socket;
        
        //reading from console
        //Scanner console = new Scanner(System.in);
        
        //constructor
        public ChatClient(Socket socket) 
        {
            this.socket = socket;
        }

        //thread part. ON LEVEL - both server and client should be equals
        @Override
        public void run() 
        {
        	try
        	{
        		Scanner in = new Scanner(socket.getInputStream());
        		//PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        		String msg = "";
            
        		//handler - loops and checks for input on console and server
        		while (true) 
        		{
            	
            	
	            	if(in.hasNextLine()) //reading from server and sending to console
	                {
	                	
	                	//read from server
	                	msg = in.nextLine();
	                	
	                	//ignore empty string
	                	if(msg.equals(""))
	            		{
	            			continue;
	            		}
	                	
	                	//send to console
	                    System.out.println("Server: "+msg);
	                    
	                    //exit condition
	                    if(msg.equals(".over"))
	            		{
	                    	System.out.println("Connection terminated");
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
        
        }
	}
	
	
	
}
