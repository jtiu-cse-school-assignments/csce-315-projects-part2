package kalah.common;

import java.io.IOException;
import java.net.UnknownHostException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class KalahPlayer {
	private Socket server;
	private PrintStream serverSender;
	private Scanner serverListener;
	private String name;
	private String moves;
	public KalahGameBoard board;
	private Scanner kbd = new Scanner(System.in);
	private int index = -1;
	private int timeLimit;

	public KalahPlayer() throws UnknownHostException, IOException {
		/*System.out.println("What is name of the player");
		name = kbd.next();
		kbd.nextLine();
		System.out.println("Local Host: enter 1 for yes, 0 for no");
		int local = kbd.nextInt();

		String hostAddress;
		if(local == 1) {
			hostAddress = null;
		}
		else {
			System.out.println("Enter host");
			hostAddress = kbd.next();
			kbd.nextLine();
		}
		System.out.println("Enter the port number");
		int portNum = kbd.nextInt();
		*/
		String hostAddress = null;
		try {
			server = new Socket(hostAddress, 8002);
		} 
		catch (UnknownHostException name) {
			System.out.println("Couldnt connect to server");
			throw(name);
		}
		catch (IOException name) {
			System.out.println("Couldnt communicate with server");
			throw(name);
		}  
		try {
			serverSender = new PrintStream(server.getOutputStream(), true);
			serverListener = new Scanner(server.getInputStream());
		} 
		catch (IOException name) {
			System.out.println("Couldnt communicate with server");
			throw(name);
		} 
	
	}
	
	public KalahPlayer(String hostAddress, int portNumber, String n, int time) throws UnknownHostException, IOException {
		name = n;
		timeLimit = time;
		try {
			server = new Socket(hostAddress, portNumber);
		} 
		catch (UnknownHostException name) {
			System.out.println("Couldnt connect to server");
			throw(name);
		}
		catch (IOException name) {
			System.out.println("Couldnt communicate with server");
			throw(name);
		}  
		try {
			serverSender = new PrintStream(server.getOutputStream(), true);
			serverListener = new Scanner(server.getInputStream());
		} 
		catch (IOException name) {
			System.out.println("Couldnt communicate with server");
			throw(name);
		} 	
	}
	public int getTimeLimit() {
		return timeLimit;
	}
 	public String recieveCommand() {
		if (!serverListener.hasNext()) {
			return null;
		}
		String command = serverListener.nextLine();
		serverSender.println("OK");
		System.out.println(command);
		return command;
	}
	public void setUp() throws IOException {
		String welcome = serverListener.nextLine();
		if(!welcome.equals("WELCOME")) {
			String message = "Socket command miscommunication";
			System.out.println(message);
			IOException err = new IOException(message);
			throw(err);
		}

		System.out.println(welcome);
		serverSender.println("READY");
		String info = serverListener.nextLine();
		System.out.println(info);
		board = new KalahGameBoard(info);
	}
	public String getMove() throws IOException {
		String command = recieveCommand();
		if(command.equals(null)) {
			return "TIME";
		}
		else {
			Scanner stream = new Scanner(command);
			while(stream.hasNext()) {
				index = stream.nextInt();
				if(!board.increment2(index - 1)) break;
			}
			stream.close();
			String ack = serverListener.nextLine();
			return ack;
		}
	}
	/*
	public String sendMoves() throws IOException {
		System.out.println("Its your move");
		String moves = "";
		do { 
			board.printBoard();
			String choices = "Select:";
			for(int i = 1; i <= board.getSize(); i++) choices = choices + " " + i;

			System.out.println(choices);
			System.out.println("or -1 for quit?");
			index = kbd.nextInt();
			
			moves = moves + index + " ";
			if(index == -1) break;

		} while(board.increment2(index - 1));
		board.printBoard();
		serverSender.println(moves);
		String ack = serverListener.nextLine();
		System.out.println(ack);
		return ack;
	}   
	*/
	public void play() throws IOException{
		board.increaseTurn();
		boolean gameOver = false;
		String ack;
		do {
			if(board.getTurn()) {
				ack = sendMoves();
			}
			else {
				ack = getMove();
			}
			board.nextPlayer();
			board.increaseTurn();
			if(!ack.equals("OK")) gameOver = true;

		} while(!gameOver && index != -1);
		System.out.println();
	} 
	
	public void addMove(int index) {
		moves = moves + index + " ";
	}
	
	public String sendMoves() throws IOException {
		serverSender.println(moves);
		String ack = serverListener.nextLine();
		return ack;
	}
	/*public void main(String[] args) throws UnknownHostException, IOException {
		KalahPlayer client = new KalahPlayer();
		client.setUp();
		client.play();
	}
	*/
}

