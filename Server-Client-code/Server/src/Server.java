//Saurabh Shah

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.lang.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

	//runs firsts, starts up the executor and reads port number
	
	
	//console to client
    public static void main(String[] args) throws Exception 
    {
    	//reads port number
    	Scanner scan = new Scanner(System.in);
    	int portnum = -1;
    	
    	try
    	{
    		System.out.println("Please enter an integer for the port number.");
    		portnum = scan.nextInt();
    		//clear buffer
    		scan.nextLine();
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
            ExecutorService pool = Executors.newFixedThreadPool(2);
            
            
        	//sends socket to ChatServer
        	client = server.accept();
            pool.execute(new ChatServer(client));
            pool.execute(new HeartBeat(client));
            
          //read from console and send to client
            String msg = "";
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            
            while(true)
            {
            	//will run while thread runs
            	if(scan.hasNextLine()) 
            	{
            		
            		//read from console
            		msg = scan.nextLine();
            		
            		//nullifies empty strings
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
            		
            		//sends to client
            		out.println("m" + msg); //normal message
            		
            		//exit condition
            		if(msg.equals(".over"))
            		{
            			System.out.println("Connection terminated");
            			break;
            		}
            		
            	}
            }
            
         scan.close();   
        }
        catch(Exception e)
        {
 		   	System.out.println("Error :" + e.getMessage());
        }
        
    }

    
    //reads from client
    private static class ChatServer implements Runnable {
        private Socket socket;
        
        
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
        	System.out.println("Enter \".over\" to disconnect.");
        	String msg = "";
        	long sysTime = System.currentTimeMillis();
        	
            String header = "";
            try 
            {
                Scanner in = new Scanner(socket.getInputStream());
                
                
                //handler. Loops until exit condition is reached
                while (true) 
                {
                	if(in.hasNextLine()) //read from client and send to console
                	{
                		//read from client
                		msg = in.nextLine();
                		header = msg.substring(0,1);
                		msg = msg.substring(1);
                		
                		//nullifies empty strings. Don't know if necessary
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
                			if((System.currentTimeMillis() - sysTime) > 2000)  //no beat for 2 seconds
                			{
                				System.out.println("No heartbeat from Client. Disconnecting.");
                				break;
                			}
                		}
                		
                    	//writes to console
                    	System.out.println("Client: "+msg);
                    	
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
	
	//sends to client
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
