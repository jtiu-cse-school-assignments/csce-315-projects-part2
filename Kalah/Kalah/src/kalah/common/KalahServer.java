package kalah.common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class KalahServer {
	protected ServerSocket serverSocket;
	protected Socket client;
	protected PrintStream clientSender;
	protected Scanner clientListener;
	protected String player1 = "Vin";
	protected String player2 = "Rajiv";

	protected String command;
	protected String ack;
	protected Scanner kbd = new Scanner(System.in);
	protected KalahGameBoard board;

	protected int size;
	protected boolean randomize;
	protected int timePerMove;
	protected int seedsPerPit;
	
	protected boolean playerTurn;
	protected boolean end;
	protected int index;
	
	public KalahServer() {
		System.out.println("How many pits for each player?");
		//size = kbd.nextInt();
		size = 4;

		System.out.println("How many seeds per pit?");
		//seedsPerPit = kbd.nextInt();
		seedsPerPit = 2;

		System.out.println("How much time per move? (ms)");
		//timePerMove = kbd.nextInt();
		timePerMove  = 1000;

		System.out.println("Randomize? yes/no");
		/*kbd.nextLine();
		String random = kbd.nextLine();

		if(random.equalsIgnoreCase("yes"))
			randomize = true;
		else
			randomize = false;
		*/
		randomize = true;
		board = new KalahGameBoard(player1, player2, size, seedsPerPit, randomize);
		board.printBoard();
	}
	public void setUp() throws IOException {
		System.out.println("Enter a port number to use: ");
		int portNum = 8002;
		//Establishes the server connection on the given port
		serverSocket = new ServerSocket(portNum);

		System.out.println("Waiting for a connection... ");
		client = serverSocket.accept(); //client1 connects at this point
		clientSender = new PrintStream(client.getOutputStream(), true);
		clientListener = new Scanner(client.getInputStream());
		clientSender.println("WELCOME");

		//take the ready messages
		command = clientListener.nextLine(); 
		System.out.println(command);

		String info = "Info " + size + " " + seedsPerPit + " " + timePerMove;
		int[] pitArray = board.getPitArray();

		String holeConfig;
		if(randomize) {
			holeConfig = " S R";
			for(int i = 0; i < size; i++) {
				holeConfig = holeConfig + " " + pitArray[i];
			}
		}
		else holeConfig = " S S";

		clientSender.println(info+holeConfig);
	}
	
	public int getTimeLimit() {
		return timePerMove;
	}
	public void close() throws IOException {
		serverSocket.close();
		clientListener.close();
		kbd.close();
	}
	public void sendMove() {
		clientSender.println(command);
		clientSender.println(ack);
		ack = clientListener.nextLine();
		System.out.println("Player 1 said " + ack);
	}
	
	public void clientMove() {
		System.out.println("Waiting on Player 2 to Move");
		while(!clientListener.hasNext());

		command = clientListener.nextLine();
		System.out.println(command);
		Scanner stream = new Scanner(command);
		ack = "OK";

		while(stream.hasNext()) {
			index = stream.nextInt();
			if(!board.isLegal(index-1)) {
				ack = "ILLEGAL";
				break;
			}
			board.increment2(index - 1);
		}
		stream.close();
		board.printBoard();
		clientSender.println(ack);	
	}
	public void serverMove() {
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
		clientSender.println(moves);
		String ack = clientListener.nextLine();
		System.out.println("client said " + ack);
		int result = board.checkGameOver();
		String secondMessage = "OK";
		if(result == 1) {
			secondMessage = "WINNER";
		}
		else if (result == 2) {
			secondMessage = "LOSER";
		}
		else if (result == 3) {
			secondMessage = "TIE";
		}
		
		clientSender.println(secondMessage);
	}

	public void play() {
		int gameOver = -1;
		board.increaseTurn();
		do {
			playerTurn = board.getTurn();
			if(playerTurn) serverMove();
			else clientMove();
			board.nextPlayer();
			board.increaseTurn();
			
			gameOver = board.checkGameOver();

		} while(gameOver == 0 && index != -1);
	}
	public static void main(String[] args) throws IOException{
		KalahServer s = new KalahServer();
		s.setUp();
		s.play();
		s.close();
	}

}