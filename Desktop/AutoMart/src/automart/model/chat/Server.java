package automart.model.chat;


import java.io.*;
import java.net.*;
import java.util.*;

/** The Listener Class - the connectoin-oriented chat server 
* gets ready to receive informatin by listening to a port */


public class Server {
	
		
	/** declare instance variable ServerSocket ss 
	which we will use for accepting new connections */
	private ServerSocket ss;
	
	// mapping from sockets to DataOutputStream to avoid having to create
	// a DataOutputStream each time we want to write to a stream
	private Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();
	
	/** Constructor - Server takes in single parameter, the port number to listen to */ 
	public Server(int port) throws IOException {
		
		//listen to port - 
		listen(port);
	}
	
	/** while-accept loop to wait for a connection to come in and give use
		the Sockets that are connected to clients. Throws an IOException 
		which signals failed or interrupted I/O operations has occured.  */
	private void listen(int port) throws IOException {
		
		/* SocketServer method is called and returns 
		new Socket object that represents new connection. */
		ss = new ServerSocket(port);
		
		//Notify Server is ready
		System.out.println("AutoMart ChatServer Listening on "+ss);
		
		/* Keep accepting conections forever to create multiple Threads 
		A Thread is a part of a program that executes independently. It contains
		information needed to serve individial user. Java program allows application 
		to have multiple threads of execution running concurrently. */
		while(true) {
			
			/* After the new connection has been dealt with accept() gets the next 
			connection, and therefore, deals with things happening simultaneously, referred to
			as serialization */
			Socket s = ss.accept();
			
			//Notify Server has connected to Client
			System.out.println("Connection with Dealer from "+s);
			
			//Create a DataOutputStream for writing data to the
			//other side
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			
			//Save this stream so not needed to be make again
			outputStreams.put(s, dout);
			
			/* Everytime a connection comes in we create new new thread. Calls on 
			Model to deal with this */
			new ServerThread(this, s);
		}
	}
	
	//Get enumeration of all OutputStreams, one for each
	//client connected
	Enumeration<DataOutputStream> getOutputStreams() {
		return outputStreams.elements();
	}
	
	//Send message to all clients (utility routine)
	void sendToAll(String message) {
		
		//syncronize on this because other threads might be 
		// calling removeConnection() and this would crash
		synchronized(outputStreams) {
			
			//for each client
			for(Enumeration<DataOutputStream> e = getOutputStreams(); e.hasMoreElements();) {
				
				// ... get the output stream...
				DataOutputStream dout = e.nextElement();
				
				// ... and send message
				try {
					dout.writeUTF(message);
				} catch(IOException ie) {System.out.println(ie);}
			}
		}
	}
	
	//Remove a socket, ant its corresponding output stream, from our
	//list. This is usually called by a connection thread that has
	//discovered the the client connection is dead.
	
	void removeConnection(Socket s) {
		
		//Synchronize so we don't mess up sendToAll() while it walks
		// down the list of all output streams
		synchronized(outputStreams) {
			
			//Notify
			System.out.println("Removing connection "+s);
			
			//Remove it from hashtable
			outputStreams.remove(s);
			
			//Make sure it's closed
			try{
				s.close();
			} catch(IOException ie) {
				System.out.println("Error closing "+s);
				ie.printStackTrace();
			}
		}
	}
	
	/* Main: Initiates the Server so it can be used as its own stand-alone application */ 
	static public void main(String args[]) throws Exception {
		
		//Get the port # from command line
		//int port = Integer.parseInt(args[0]);
		int port = 5000;		
				
		//Create a Server object, which will automatically begin 
		//accepting connections
		new Server(port);
		
		//new Client("localhost", 5000);
	}
}