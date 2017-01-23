package automart.model.chat;


import java.io.*;
import java.net.*;

/* The Per-Thread Class - Model which multithreads to deal with ultiple users.
Purpose of chat server is using threads to communicate with multipe users*/
public class ServerThread extends Thread {
	
	//the Server instance variable
	private Server server;
	
	//Client connected instance variable
	private Socket socket;
	
	/** Constructor takes two parameters, the Server object and Socket Object*/
	public ServerThread(Server server, Socket socket) {
		
		//Save parameters
		this.server = server;
		this.socket = socket;
		
		/* Point at which new thread comes into being. 
		This thread excutes run() mehthos of this object */
		start();
	}
	
	//This runs in separate thread when start() is called
	//in constructor
	public void run() {
		
		try {
			
			/* Use DataInputStream ro read messages from client */ 
			DataInputStream din = new DataInputStream(socket.getInputStream());
			
			/* repeat procss indefineetly */
			while(true) {
				
				/* Read message from client */
				String message = din.readUTF();
				
				/* Notify message is being sent to all dealers/clients */
				System.out.println("Sending to Dealer "+message);
				
				/* Server actually sends it to all Clients */
				server.sendToAll(message);
			}
		/* An EOFException signals that end of file or end 
		of streams has been reached unexpectedly during input */ 
		} catch(EOFException ie) {
			//This doesn't need error message
			
		} catch (IOException ie) {
			ie.printStackTrace();
		
		} finally {
			/* Calls server to remoce dead connections */
			server.removeConnection(socket);
		}
	}
}
			