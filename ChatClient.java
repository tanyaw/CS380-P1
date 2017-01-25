import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;

public final class ChatClient implements Runnable {
	protected Socket socket;
	
	//Constructor
	//Assigns socket to be used by Client when connected to Server
	public ChatClient(Socket socket) {
		this.socket = socket;
	}
	
	//Override
	//Each Client Thread receives data from the Server and prints it
	public void run() {
		try {  //Try-catch, when Server sends null or any errors
			while(true) {
				//Receives data from server
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String serverData = br.readLine();
				
				if(serverData != null){
					//serverData holds value - [TIMESTAMP] userName: userMessage
					System.out.println(serverData);
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) throws Exception {
		try (Socket socket = new Socket("codebank.xyz", 38001)) {
		    String userName, userInput;
			Scanner scan = new Scanner(System.in);
			
			//Sends data to server
			OutputStream os = socket.getOutputStream();
			PrintStream out = new PrintStream(os, true, "UTF-8");
			
			//Sends username to Server
			System.out.printf("Enter username: ");
			userName = scan.nextLine();
			out.println(userName);	
			
			//Client Thread begins chat to Server
			Thread thread = new Thread(new ChatClient(socket));
			thread.start();
			
			//Reads in messages user inputs
			while(true) {
				userInput = scan.nextLine();

				if(userInput.toUpperCase().equals("EXIT")) {	
					//Disconnect from Server
					System.exit(0);
				} else {
					out.println(userInput);
				}
		   }
		} catch(IOException e) {
			e.printStackTrace();
		}	
    }
}

