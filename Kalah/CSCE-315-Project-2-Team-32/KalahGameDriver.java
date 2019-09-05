package kalah.common;
import java.util.*;

public class KalahGameDriver {

	public static void main(String args[]) {
		
		Scanner input = new Scanner(System.in);
		
		boolean randomize;
		
		KalahPlayer player1 = new KalahPlayer();
		KalahPlayer player2 = new KalahPlayer();
		
		player1.setName("Player 1");
		player2.setName("Player 2");
		
		System.out.println();
		System.out.println("Ready to play? yes/no");
		String answer = input.next(); 
		
		System.out.println("How many pits on each side? ");
		int pits = input.nextInt();
		
		System.out.println("How many seeds on each pit? ");
		int seedsPerPit = input.nextInt();
		
		System.out.println("Would you like to randomize the seeds on each hole? yes/no");
		input.nextLine();
		String random = input.nextLine();
		
		if(random.equalsIgnoreCase("yes"))
			randomize = true;
		else
			randomize = false;
		
		KalahGameBoard board = new KalahGameBoard(player1, player2, pits, seedsPerPit, randomize);
		
		board.printBoard();
		
		char index;
		boolean gameOver = false;
		
		if(answer.equals("yes")) {
			board.gameStart();
			
			do {
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				//board.print();
				board.printBoard();
				
				System.out.println("select: a b c d e f");
				System.out.println("or q for quit?");
				index = input.next().charAt(0);
				
				if(board.playCount == 1) { 
					System.out.println("Player 2: Would you like to apply the Pie Rule? ");
					input.nextLine();
					String pieRule = input.nextLine();
					if(pieRule.equalsIgnoreCase("yes")) {
						board.switchUsers();
					}
				}
				
				
				switch(index) {
				case 'a':
					int in = board.selectIndex('a');
					int amountOfSeeds = board.getIndexSeedAmount(in);
					if(amountOfSeeds == 0) break;
					board.increment(in, amountOfSeeds);
					board.nextPlayerTurn();
					break;
				case 'b':
					int in2 = board.selectIndex('b');
					int amountOfSeeds2 = board.getIndexSeedAmount(in2);
					board.increment(in2, amountOfSeeds2);
					board.nextPlayerTurn();
					break;
				case 'c':
					int in3 = board.selectIndex('c');
					int amountOfSeeds3 = board.getIndexSeedAmount(in3);
					board.increment(in3, amountOfSeeds3);
					board.nextPlayerTurn();
					break;
				case 'd':
					int in4 = board.selectIndex('d');
					int amountOfSeeds4 = board.getIndexSeedAmount(in4);
					board.increment(in4, amountOfSeeds4);
					board.nextPlayerTurn();
					break;
				case 'e':
					int in5 = board.selectIndex('e');
					int amountOfSeeds5 = board.getIndexSeedAmount(in5);
					board.increment(in5, amountOfSeeds5);
					board.nextPlayerTurn();
					break;
				case 'f':
					int in6 = board.selectIndex('f');
					int amountOfSeeds6 = board.getIndexSeedAmount(in6);
					board.increment(in6, amountOfSeeds6);
					board.nextPlayerTurn();
					break;
				default:
					System.out.println("Invalid input.");
					break;
						
				}
				
				gameOver = board.checkGameOver();
			} while(index != 'q' && !gameOver);
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			board.printBoard();
		}
		
		input.close();
	}
}
