import java.awt.*;
import java.applet.*;
import java.net.*;
import javax.swing.*;
import java.io.*;


public class TrafficRequester implements Runnable
{
	static boolean bConnected = false;
	static Socket mySocket = null;
    static PrintWriter out = null;
    static BufferedReader in = null;
	static String sMyId = null;
	static String sIP = null;
	
	static String fromServer = null;
    static String fromUser = null;
    static String sConnection = "Not Connected to the chat server!";
	
	Thread thread;
   
	    
        
    public void init()
    {
    }

	//***********************************************
	// trapping button actions
	//
    //***********************************************
    public boolean action(Event evt, Object arg) {
    	String sTemp = null;
		
		//How to close the connection to the remote guy.
			try {
				if (bConnected)
					mySocket.close();
			} catch (IOException e) {}
		
		
		//********************************************
		// connect button pressed
		//******************************************
		if (arg == "Connect" && !bConnected) {
					
			try {
				// Need IP address of remote guy.
				sIP = JOptionPane.showInputDialog("Enter IP of chat server:");
				//Get Name of local guy/sender/the device executing this code? 
				sMyId = JOptionPane.showInputDialog("Enter your name:");
				//Port that the remote guy is listening on. 
				int nPort = 4444; // default 
				//Connect to the socket/remote guy. 
				mySocket = new Socket(sIP, nPort);
				
				// optional - setting socket timeout to 5 secs
				// this is not necessary because application
				// runs with multiple threads
				//
				//mySocket.setSoTimeout(5000);
								
				bConnected = true;
				//Creating the PrintWriter
				out = new PrintWriter(mySocket.getOutputStream(), true);
				//Self identify to the remote guy. Not necessary.
				out.println(sMyId);
				sConnection = "Connected to the chat server!";			
				
				//
				// define new thread
				//
				thread = new Thread(this);
				thread.start();	
				
			} catch (IOException e) {}		
		}// end of connect button
        return true;
    }
    
    
	//************************************************
	// main
	//
	// main application method for the class
	// it will initialize whole environment
	//
	//************************************************
	public static void main(String args[]){
		String sTemp = null;
		//
		// define window and call standard methods
		//
		TrafficRequester app = new TrafficRequester();
		app.init();
		//app.start();
	
	}// end of main

	public void stop() {
		thread.stop();
	}// end of stop
	
	public void run() {
		boolean bLoopForever = true;
		while (bLoopForever){
			checkServer();
		}
	}
	
	public static void checkServer(){
	try {
			//To send a message to the server, do PrintWriter.println(MessageString);	
		out.println("ACK"+ sMyId + " says: " + fromUser);
		}catch (Exception e) { }

	}// end of checkserver
}// end of class MyClientWin
