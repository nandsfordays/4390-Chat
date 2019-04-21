//Saurabh Shah 

import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread
{  
	private ChatServer server = null;
	private Socket socket = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;

	//constructor
   public ChatServerThread(ChatServer _server, Socket _socket)
   {  
	   super();
	   server = _server;
	   socket = _socket;
   }
   
   //connects input and output streams to socket
   public void open() throws IOException
   {  
	   streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	   streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
   }
   
   public void send(String msg)
   {   
	   try
       {  
		   streamOut.writeUTF(msg);
		   streamOut.flush();
       }
       catch(IOException e)
       {  
    	   System.out.println("error sending: " + e.getMessage());
    	   stop();
       }
   }
   
   
   
   //runs when thread runs
   public void run()
   {  System.out.println("Server Thread running.");
      while (true)
      {  
    	  try
    	  {  
    		  server.handle(streamIn.readUTF());
    	  }
    	  catch(IOException e)
    	  {  
    		  System.out.println("error reading: " + e.getMessage());
    		  stop();
    	  }
      }
   }
   
  //closes socket and input/output streams
   public void close() throws IOException
   {  
	   if (socket != null)    
	   {
		   socket.close();
	   }
	   
	   if (streamIn != null)  
	   {
		   streamIn.close();
	   }
	   
	   if (streamOut != null) 
	   {
		   streamOut.close();
	   }
   }
}