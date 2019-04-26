//Saurabh Shah


import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.lang.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
	
	//console to server
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
        	System.out.println("Enter \".over\" to disconnect.");
        	
            ExecutorService pool = Executors.newFixedThreadPool(2);
            
            pool.execute(new ChatClient(socket));
            pool.execute(new HeartBeat(socket));
            
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
                	if(msg.length() > 150) //control message length
            		{
            			System.out.println("System: Please limit messages to 150 characters");
            			System.out.println("System: First 150 characters will be sent.");
            			msg = msg.substring(0,149);
            		}
                	
                	//send to server
                    out.println("m" + msg); //normal message
                    
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
	
	
	
	//reads from server
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
        		String header = "";
            	long sysTime = System.currentTimeMillis();
        		
        		//handler - loops and checks for input on console and server
        		while (true) 
        		{
            	
            	
	            	if(in.hasNextLine()) //reading from server and sending to console
	                {
	                	
	                	//read from server
	                	msg = in.nextLine();
	                	header = msg.substring(0,1);
                		msg = msg.substring(1);
	                	
	                	//ignore empty string
	                	if(msg.equals(""))
	            		{
	            			continue;
	            		}
	                	//handles heartbeat
                		if(header.equals("b"))
                		{
                			sysTime = System.currentTimeMillis();
                			//System.out.println("beat");
                			continue;
                		}
                		else
                		{
                			if((System.currentTimeMillis() - sysTime) > 2000)  //no beat for 4 seconds
                			{
                				System.out.println("No heartbeat from Server. Disconnecting.");
                				break;
                			}
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
	
	//sends to Server
	private static class HeartBeat implements Runnable {
        private Socket socket;
        
        
        //constructor
        public HeartBeat(Socket socket) 
        {
            this.socket = socket;
        }

        //thread part
        @Override
        public void run() 
        {
        	
        	try 
            {
        		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        		
        		while(true)
                {
                	Thread.sleep(1000);
            		//sends to client
            		out.println("ba"); //sending an empty header
                	
                }
                
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
	
	
}
