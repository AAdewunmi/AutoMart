package automart.model.chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
//import testabc.*;

public class Client extends Panel implements Runnable {
	
	// Components for the visual display of the chat windows instance variables
	private TextField tf = new TextField();
	private TextArea ta = new TextArea();
	
	private String message;
	
	//The socket connecting us to the server
	private Socket socket;
	
	private ChatClient chat;
	
	//The streams we communicate with server which come from the socket
	private DataOutputStream dout;
	private DataInputStream din;
	
	//Constructor
	public Client(String host, int port) {
		
		//Set up the screen
		//this.setLayout(new BorderLayout());
		//this.add(tf, "North"); //on the instruction this was the other way around
		//this.add(ta, "Center");
		//this.setVisible(true);
		
		//chat(message);	
		
		//ChatClient chat = new ChatClient(message);
		//chat.super(message);
		//new ChatClient(this.message);
		
		
		
		//receive message when someone types a line and hits return,
		//using anonymous class as a callback
		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processMessage(e.getActionCommand());
			}
		});
		
		//Connect to the server
		try {
			//Initiate the connection
			socket = new Socket(host, port);

			
			//Notify connection was made
			System.out.println("connected to AutoMart server "+socket);
			
			//grab streams and create Datainput/Output streams
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			
			//Start a background thread for receiving messages
			new Thread(this).start();
			
		} catch(Exception ie) {
			System.out.println("Error connecting to AutoMart server:" + ie);
			//return false;
		}
		
		
	}
	
	/*
	private void chat(String message) {
	
		new ChatClient(super.message);
	}*/
	
	//Gets called when user types something
	private void processMessage(String message) {
		try {
			
			//Send it to the server
			dout.writeUTF(message);
			
			//Clear out text input field
			tf.setText("");
		} catch(IOException ie) {System.out.println(ie);}
	}
	
	//Background thread runs this: show message from other window
	public void run() {
		try {
			
			//Receive messages one-by-one, forever
			while(true) {
				
				//Get the next message
				String message = din.readUTF();
				
				//Print it to the next window
				ta.append(message+"\n");
			}
		} catch(IOException ie) {System.out.println(ie);}
	}
	
	/*
	public void init() {
		int port = 5000;
		String host = "localhost";
		setLayout(new BorderLayout());
		add("Center", new Client(host, port));
	}
	*/
	
	
	
	//Main routine
	//Usage: java client <port>
	static public void main(String[] args) throws Exception {
		
		//Get the port # 5000
		int port = 5000;
		String host = "localhost";
		
		new Client("localhost", 5000);
		
		//Create a Server object, which will automatically begin 
		//accepting connections
		//Server server = new Server(port);
		
		//new ClientApplet();
		//new Client(host, port);
		
		
	}

	
		
}
