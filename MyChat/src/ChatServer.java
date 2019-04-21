//Saurabh Shah

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatServer implements Runnable  //so threads can be used
{  
	private ChatServerThread client = null;
	private ServerSocket server = null;
	private Thread thread = null;

	public static void main(String args[])
	{  
		ChatServer server = null;
		Scanner scan = new Scanner(System.in);
	    try
	    {
	    	System.out.println("Please enter an integer for the port number.");
	    	int portnum = scan.nextInt();
	    	scan.close();
	    	server = new ChatServer(portnum);
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Error reading port number. Please enter an integer.");
	    }
	    
	}
	
	//constructor for chatserver
   public ChatServer(int port)
   {  
	   try
	   {  
		   System.out.println("Binding to port " + port);
		   server = new ServerSocket(port);  
		   System.out.println("Server started");
		   start(); 
	   }
	   catch(IOException e)
	   {  
		   System.out.println("Cannot bind to port " + port + ": " + e.getMessage()); 
	   }
   }
   
 //adds client
   private void addThread(Socket socket)
   {  
	   
	   System.out.println("Client accepted: " + socket);
	   client = new ChatServerThread(this, socket);
	   try
	   {  
		   client.open(); 
		   client.start();
	   }
	   catch(IOException e)
	   {  
		   System.out.println("Error opening thread: " + e); 
	   } 
	  
   }
   
   //starts thread for client
   public void start()
   {  
	   if (thread == null)
	   {  
		   thread = new Thread(this); 
		   thread.start();
	   }
   }
   
   //executes when thread is started
   public void run()
   {  
	   while (thread != null)
	   {  
		   try
		   {  
			   System.out.println("Waiting for a client ..."); 
			   addThread(server.accept()); 
		   }
		   catch(IOException ioe)
		   {  
			   System.out.println("Server accept error: " + ioe); stop(); 
		   }
	   }
   }
   
   //executes when thread terminates
   public void stop()
   {  
	   if (thread != null)
	   {  
		   thread.stop(); 
		   thread = null;
	   }
   }
   
   
   
   public void handle(String input)  //this just forwards the message to the client thread
   {  
	   if (input.equals(".bye")) //exit case
	   {  
		   client.send(".bye");
	   }
	   else
       {
		   //client.send(input); 
		   System.out.println(input);
       } 
       
             
   }
   
   
   //remove client
   public synchronized void remove(int ID)
   {  
	    
	   ChatServerThread toTerminate = client;
	   System.out.println("Removing client thread");
	   
	   try
	   {  
		   toTerminate.close(); 
	   }
	   catch(IOException e)
	   {  
		   System.out.println("Error closing thread: "+ e); 
	   }
	   
	   toTerminate.stop();
   }
   
   
   
   
}