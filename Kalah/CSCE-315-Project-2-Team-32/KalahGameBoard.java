package kalah.common;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.Scanner;

public class KalahGameBoard {
	
	/* **************************************************************************** Data Members */
	
	private int pits[];
	private int size;
	private int seedsPerPit;

	private int store1;
	private int store2;
	public boolean playerTurn;
	private int timeLimit;
	
	private String p1;
	private String p2;
	private boolean randomized;
	private int indexOfLastDrop;
	private int indexOfOpposite;
	public int playCount;
	public boolean goAgain;
	
	/* **************************************************************************** Constructors */
	
	public KalahGameBoard(String _player1, String _player2, int _pits, int _seedsPerPit,
			boolean randomize) {

		p1 = _player1;
		p2 = _player2;

		seedsPerPit = _seedsPerPit;
		pits = new int[(_pits * 2) + 2];
		size = _pits;
		store1 = _pits;
		store2 = Array.getLength(pits) - 1;
		
		if(!randomize) {
			for(int i = 0; i < Array.getLength(pits); ++i) {
				pits[i] = seedsPerPit;
			}
		}
		else {
			int totalSeeds = size * seedsPerPit;
			Random rand = new Random();
			int seedAllocator;
			
			//randomizing for player1
			for(int i = 0; i < totalSeeds; i++) {
				int ind = rand.nextInt(size -1);
				pits[ind] = pits[ind] + 1;
			}
			
			//randomizing for player2
			int j = 0;
			for(int i = store1 + 1; i < Array.getLength(pits); ++i) {
				pits[i] = pits[j];
				j++;
			}
		}
		pits[store1] = 0;
		pits[store2] = 0;
		
		playerTurn = true;
		indexOfLastDrop = 0;
		indexOfOpposite = 0;
		playCount = 0;
		goAgain = false;
	}

	public KalahGameBoard(String info) {
		Scanner infoStream = new Scanner(info);
		infoStream.next();
		size = infoStream.nextInt();
		seedsPerPit = infoStream.nextInt();
		
		timeLimit = infoStream.nextInt();
		
		String position = infoStream.next();
		if(position.equals("F")) playerTurn = true;
		else playerTurn = false;

		String random = infoStream.next();
		if(random.equals("R")) randomized = true;
		else randomized = false;


		pits = new int[(size * 2) + 2];

		if(randomized) {
			for(int i = 0; i < size; i++) {
				int temp = infoStream.nextInt();
				pits[i] = temp;
				pits[size + 1 + i] = temp;
			}
		}
		else {
			for(int i = 0; i < size; i++) {
				int temp = seedsPerPit;
				pits[i] = temp;
				pits[size + 1 + i] = temp;
			}			
		}

		store1 = size;
		store2 = size*2 + 1;

		pits[store1] = 0;
		pits[store2] = 0;
		
		indexOfLastDrop = 0;
		indexOfOpposite = 0;
		playCount = 0;
		goAgain = false;
		infoStream.close();
	}
	
	public KalahGameBoard(KalahGameBoard game) {
		pits = game.getPitArray();
		size = game.getSize();
		seedsPerPit = game.seedsPerPit;
		store1 = game.store1;
		store2 = game.store2;
		playerTurn = game.playerTurn;
		timeLimit = 10000;
		
		p1 = game.p1;
		p2 = game.p2;
		randomized = game.randomized;
		indexOfLastDrop = game.indexOfLastDrop;
		indexOfOpposite = game.indexOfOpposite;
		playCount = game.playCount;
		goAgain = game.goAgain;
	}
	
	/* ********************************************************************************* Getters */

	public int[] getPitArray() {
		return pits;
	}
	
	public int getPlayCount() {
		return playCount;
	}

	public int getSize() {
		return size;
	}
	
	public int getIndexSeedAmount(int _index) {
		/*
		 * 	returns the amount of seeds in that index
		 */
		
		return pits[_index];
	}

	public boolean getTurn() {
		return playerTurn;
	}
	
	public int getHomeIndex() {
		return store1;
	}
	
	public int getEnemyHomeIndex() {
		return store2;
	}
	public int getHome() {
		return pits[store1];
	}
	
	public int getEnemyHome() {
		return pits[store2];
	}
	/* ********************************************************************************* Setters */
	
	public void setIndexOfLastDrop(int _index) {
		/*
		 * Sets the opposite index of the last drop
		 */

		indexOfLastDrop = _index;
		
	}
	
	public void setIndexOfOpposite(int _index) {
		/*
		 * Sets the opposite index of the last drop
		 */
		int numOfPits = Array.getLength(pits) - 2;
		
		indexOfOpposite = numOfPits - _index;
	}
	
	/* *********************************************************** Helper Functions */
	
	public void increaseTurn() {
		playCount++;
	}
	
	public boolean isLegal(int index) {
		if(!playerTurn) index = size + index + 1;
		if(index >= pits.length || index < 0) return false;
		if(pits[index] == 0) {
			return false;
		}
		
		return true;
	}
	
	public void nextPlayerTurn() {
		/*
		 * 	Checks to see if goAgain is set to false
		 * 		If it is, then switch the turn
		 * 		If not, then it's still the player's turn
		 * 
		 */
		
		if(goAgain == false) {
			if(playerTurn ){
				playerTurn = false;
			}
			else {
				playerTurn = true;
			}
		}
	}
	
	public void nextPlayer() {
		playerTurn = !playerTurn;
	}
	public void takeOpposite() {
		/*
		 * 	If isZero() returns true, then take the opposite index's
		 * 	beads and the indexOfLastDrop()'s beads and put it 
		 * 	in the player's home pit
		 */
		
		if(playerTurn) {
			pits[store1] += pits[indexOfOpposite]; 
			pits[indexOfOpposite] = 0;
			pits[store1] += 1;
			pits[indexOfLastDrop] = 0;
		}
		else {
			pits[store2] += pits[indexOfOpposite]; 
			pits[indexOfOpposite] = 0;
			pits[store2] += 1;
			pits[indexOfLastDrop] = 0;
		}
	}
	
	public void increment(int index, int seedCount) {
		/*
		 * 	Increments every pit after the selected pit by 1
		 * 	
		 * 	Do not increment other player's home pit if passed
		 */

		pits[index] = 0;
		goAgain = false;
		
		if(playerTurn) {
			while(seedCount > 0) {
				if(index == pits.length-1) {
					index = -1;
				}
				if(index != store2-1) { // -1 because pits[index + 1] is the one incremented
					pits[index+1]++;
					index++;
					setIndexOfLastDrop(index);
					seedCount--;
				}
				else {
					index++;
				}
			}
		}
		else {
			while(seedCount > 0) {
				if(index == pits.length-1) { // -1 because pits[index + 1] is the one incremented
					index = -1;
				}
				if(index != store1-1) {		
					pits[index+1]++;
					index++;
					setIndexOfLastDrop(index);
					seedCount--;
				}
				else {
					index++;
				}
			}
		}
		
		if(playerTurn && indexOfLastDrop == store1) {
			goAgain();
		}
		else if(!playerTurn && indexOfLastDrop == store2) {
			goAgain();
		}

		setIndexOfOpposite(indexOfLastDrop);
		if(!goAgain && pits[indexOfLastDrop] == 1 && pits[indexOfOpposite] != 0) {
			if(playerTurn == true) {
				if(indexOfLastDrop < store1)
					takeOpposite();
			}
			else {
				if(indexOfLastDrop > store1 && indexOfLastDrop != store2)
					takeOpposite();
			}
		}
		playCount++;
	}

	public boolean increment2(int index) {
		goAgain = false;
		if(!playerTurn) index = size + index + 1;
		int seedCount = pits[index];
		pits[index] = 0;
		index++;
		if(playerTurn) {
			while(seedCount > 0) {
				if(index != store2) {
					pits[index] = pits[index] + 1;
					seedCount--;
					setIndexOfLastDrop(index);
					index++;
					if(index == pits.length-1) index = 0;
				}
				else index++;
			}
			if(indexOfLastDrop == store1) {
				goAgain = true;
			}
			setIndexOfOpposite(indexOfLastDrop);
			if( indexOfLastDrop < size && pits[indexOfLastDrop] == 1 && pits[indexOfOpposite] > 0) takeOpposite();

			return goAgain;
		}
		else {
			while(seedCount > 0) {
				if(index != store1) {		
					pits[index] = pits[index] + 1;
					setIndexOfLastDrop(index);
					seedCount--;
					index++;
					if(index == pits.length) index = 0;
				}
				else index++;
			}
			if(indexOfLastDrop == store2) {
				goAgain = true;
			}
			setIndexOfOpposite(indexOfLastDrop);
			if( indexOfLastDrop > store1 && pits[indexOfLastDrop] == 1 && pits[indexOfOpposite] > 0) takeOpposite();
			
			return goAgain;
		}
	}
	public void goAgain() {
		/*
		 * 	If plater's indexOfLastDrop() is at the home pit, then
		 * 	goAgain is set to 1
		 * 
		 */
		
		goAgain = true;
	}
	
	public int selectIndex(char letter) {
		
		/*
		 * 	returns the index associated with the user's selected letter
		 * 
		 */
		
		if(playerTurn) {
			switch(letter) {
			case 'a':
				return 0;
			case 'b':
				return 1;
			case 'c':
				return 2;
			case 'd':
				return 3;
			case 'e':
				return 4;
			case 'f':
				return 5;
			default:
				break;
			}	
		}
		else {
			switch(letter) {
			case 'a':
				return 7;
			case 'b':
				return 8;
			case 'c':
				return 9;
			case 'd':
				return 10;
			case 'e':
				return 11;
			case 'f':
				return 12;
			default:
				break;
			}
		}
		
		return 0;
	}

	

	public int checkGameOver() {
		/*
		 *  Returns true if the game ends; false otherwise
		 */
		
		boolean gameOver = true;
		int playerWEmptyPits = 0;
		
		for(int i = 0; i < store1; i++) {
			if(pits[i] != 0) {
				gameOver = false;
				break;
			}
		}
		
		if(gameOver) {
			playerWEmptyPits = 1;
		}
		
		else {
			for(int i = store1 + 1; i < store2; i++) {
				if(pits[i] != 0) {
					gameOver = false;
					break;
				}
			}
			
			if(gameOver) {
				playerWEmptyPits = 2;
			}
		}
		
		if(gameOver) {

			if(playerWEmptyPits == 1) {
				for(int i = store1 + 1; i < store2; ++i) {
					pits[store2] += pits[i];
					pits[i] = 0;
				}
			}
			
			else {
				for(int i = 0; i < store1; ++i) {
					pits[store1] += pits[i];
					pits[i] = 0;
				}				
			}
			
			if(pits[store1] > pits[store2]) {
				//System.out.println(player1.getName() + " wins! ");
				return 1;
			}
			else if(pits[store2] > pits[store1]){
				//System.out.println(player2.getName() + " wins! ");
				return 2;
			}
			else {
				//System.out.println("The game is a tie! ");
				return 3;
			}
		}
		else {
			return 0;
		}
	}
	
	public void switchUsers() {
		String player1TempName = p1;
		p1 = p2;
		p2 = player1TempName;

	}
	
	public void printBoard() {
		/*
		 *	Prints the game board depending on the player's turn
		 *
		 *	This is not final
		 */
		if(playCount == 0) {
			System.out.println("                    WELCOME                     ");
			System.out.println();
			System.out.println("+----------------------------------------------+");
			System.out.print("|     |    ");
			for(int i = 2*size; i > size; i-- ) {
				System.out.print(pits[i] + "    ");
			}
			System.out.println("|     |");
			System.out.println("|     |                                  |     |");
			System.out.print("|  " + pits[2*size + 1] + "  |"); 
			System.out.print("                                  | " + pits[size] + "   |");
			System.out.println();
			System.out.println("|     |                                  |     |");
			System.out.print("|     |    ");
			for(int i = 0; i < size; i++) {
				System.out.print(pits[i] + "    ");
			}
			System.out.print("|     |");
			System.out.println();
			System.out.println("+----------------------------------------------+");
		}
		else if(playerTurn) { 
			System.out.println();
			System.out.println("+----------------------------------------------+");
			System.out.print("|     |    ");
			for(int i = 2*size; i > size; i-- ) {
				System.out.print(pits[i] + "    ");
			}
			System.out.println("|     |");
			System.out.println("|     |                                  |     |");
			System.out.print("|  " + pits[2*size+1] + "  |"); 
			System.out.print("                                  | " + pits[size] + "   |");
			System.out.println();
			System.out.println("|     |                                  |     |");
			System.out.print("|     |    ");
			for(int i = 0; i < size; i++) {
				System.out.print(pits[i] + "    ");
			}
			System.out.print("|     |");
			System.out.println();
			System.out.println("+----------------------------------------------+");
		}
		else {
			System.out.println();
			System.out.println("+----------------------------------------------+");
			System.out.print("|     |    ");
			for(int i = size-1; i >= 0; i-- ) {
				System.out.print(pits[i] + "    ");
			}
			System.out.println("|     |");
			System.out.println("|     |                                  |     |");
			System.out.print("|  " + pits[size] + "  |"); 
			System.out.print("                                  | " + pits[2*size+1] + "   |");
			System.out.println();
			System.out.println("|     |                                  |     |");
			System.out.print("|     |    ");
			for(int i = size+1; i < 2*size+1; i++) {
				System.out.print(pits[i] + "    ");
			}
			System.out.print("|     |");
			System.out.println();
			System.out.println("+----------------------------------------------+");
		}
	}

	public void simplePrint() {
		System.out.println();
		System.out.println("+----------------------------------------------+");
		System.out.print("|     |    ");
		for(int i = 2*size; i > size; i-- ) {
			System.out.print(pits[i] + "    ");
		}
		System.out.println("|     |");
		System.out.println("|     |                                  |     |");
		System.out.print("|  " + pits[2*size+1] + "  |"); 
		System.out.print("                                  | " + pits[size] + "   |");
		System.out.println();
		System.out.println("|     |                                  |     |");
		System.out.print("|     |    ");
		for(int i = 0; i < size; i++) {
			System.out.print(pits[i] + "    ");
		}
		System.out.print("|     |");
		System.out.println();
		System.out.println("+----------------------------------------------+");
	}

}
