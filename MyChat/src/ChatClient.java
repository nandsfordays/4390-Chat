import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ChatClient implements Runnable
{  
	private Socket socket = null;
	private Thread thread = null;
	private Scanner  console   = null;  //private DataInputStream  console   = null;
	private DataOutputStream streamOut = null;
	private ChatClientThread client    = null;

	public static void main(String args[])
	{  
	   ChatClient client = null;
	   Scanner scan = new Scanner(System.in);
	   try
	   {
		   System.out.println("Please enter an integer for the port number.");
		   int portnum = scan.nextInt();
		   scan.close();
		   client = new ChatClient("127.0.0.1",portnum);
	   }
	   catch(Exception e)
	   {
		   System.out.println("Error reading port number. Please enter an integer.");
	   }
	   
	}
	
	//constructor
   public ChatClient(String serverName, int serverPort)
   {  
	   System.out.println("Establishing connection. Please wait ...");
	   try
	   {  
		   socket = new Socket(serverName, serverPort);
		   System.out.println("Connected: " + socket);
		   start();
	   }
	   catch(UnknownHostException uhe)
	   {  
		   System.out.println("Host unknown: " + uhe.getMessage()); 
	   }
	   catch(IOException ioe)
	   {  
		   System.out.println("Unexpected exception: " + ioe.getMessage()); 
	   }
   }
   
   
   public void start() throws IOException
   {  
	   console   = new Scanner(System.in);    //console   = new DataInputStream(System.in);
	   streamOut = new DataOutputStream(socket.getOutputStream());
	   if (thread == null)
	   {  
		   client = new ChatClientThread(this, socket);
		   thread = new Thread(this);                   
		   thread.start();
	   }
   }
   
   
   public void run()
   {  
	   String msg = "";
	   while (thread != null)
	   {  
		   try
		   {  
			   if(console.hasNext())        //PLACE WHERE IM SENDING
			   {
				   msg = console.nextLine();
				   streamOut.writeUTF(msg);
				   streamOut.flush();
			   }
			   //hard coded message
			   else
			   {
				   msg = "Error reading from console";
				   streamOut.writeUTF(msg);
				   streamOut.flush();
			   }
			   
		   }
		   catch(IOException ioe)
		   {  
			   System.out.println("Sending error: " + ioe.getMessage());
			   stop();
		   }
	   }
   }
   
   
   public void handle(String msg)
   {  
	   if (msg.equals(".bye"))
	   {  
		   System.out.println("Good bye. Press RETURN to exit ...");
		   stop();
	   }
	   else
	   {
		   System.out.println(msg);
	   }
   }
   
   
   
   
   
   public void stop()
   {  
	   if (thread != null)
	   {  
		   //thread.stop();  
		   thread = null;
	   }
	   try
	   {  
		   if (console   != null)  console.close();
		   if (streamOut != null)  streamOut.close();
		   if (socket    != null)  socket.close();
	   }
	   catch(IOException ioe)
	   {  
		   System.out.println("Error closing ..."); 
	   }
	   client.close();  
	   //client.stop();
   }
   
   
   
}