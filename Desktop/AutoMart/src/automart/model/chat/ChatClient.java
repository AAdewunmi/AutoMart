package automart.model.chat;


import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import java.io.*;

public class ChatClient extends JFrame implements WindowListener, MouseListener, KeyListener {
	
	//private TextArea ta 		= null;
	//private TextField tf 		= null;
	//private String username 	= null;
	
	private TextArea ta;
	private TextField tf;
		
	public ChatClient(String message) {
		super(message);
		
		this.addWindowListener(this);
		this.setSize(800,600);
		this.setBackground(new Color(204,153,244));
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ta = new TextArea();
		ta.setEditable(false);
		this.add(ta, "Center");
		ta.setFont(new Font("Arial", Font.PLAIN, 16));
		
		Panel p = new Panel();
		p.setLayout(new FlowLayout());
		
		tf = new TextField(30);
		tf.addKeyListener(this);
		tf.setFont(new Font("Arial", Font.PLAIN, 16));
		
		p.add(tf);
		p.setBackground(new Color(204,0,204));
		Button send = new Button("Send");
		send.addMouseListener(this);
		p.add(send);
		Button clear = new Button("Clear");
		clear.addMouseListener(this);
		p.add(clear);
		
		this.add(p, "South");
		this.setVisible(true);
		tf.requestFocus();
						
	}
	
	/*public static void main(String[] args) {
		ChatClient chat = new ChatClient("AutoMart");
	}*/

	@Override
	public void keyTyped(KeyEvent e) {

		
	}

	@Override
	public void keyPressed(KeyEvent e) {

		
	}

	@Override
	public void keyReleased(KeyEvent e) {

		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		
	}

	@Override
	public void mousePressed(MouseEvent e) {

		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		
	}

	@Override
	public void mouseExited(MouseEvent e) {

		
	}

	@Override
	public void windowOpened(WindowEvent e) {

		
	}

	@Override
	public void windowClosing(WindowEvent e) {

		
	}

	@Override
	public void windowClosed(WindowEvent e) {

		
	}

	@Override
	public void windowIconified(WindowEvent e) {

		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {

		
	}

	@Override
	public void windowActivated(WindowEvent e) {

		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

		
	}

}