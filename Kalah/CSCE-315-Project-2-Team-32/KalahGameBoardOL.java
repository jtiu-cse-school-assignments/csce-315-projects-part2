package kalah.common;

import java.lang.reflect.Array;
import java.util.Random;

public class KalahGameBoardOL {
	
	/* **************************************************************************** Data Members */
	
	private int holes[];
	private int home;
	private int enemyHome;
	private int indexOfLastDrop;
	private int indexOfOpposite;
	private int playCounter;
	private int numHoles;
	
	private boolean player1Turn;
	private boolean goAgain;
	private boolean pieRule;
	
	private String player1Name;
	private String player2Name;
	
	
	/* **************************************************************************** Constructors */
	
	public KalahGameBoardOL(String _player1Name, String _player2Name, int numberOfHoles, int numberOfSeeds, boolean randomize, boolean _pieRule) {
		
		player1Name = _player1Name;
		player2Name = _player2Name;
		
		numHoles = numberOfHoles;
		holes = new int[(numberOfHoles*2)+2];
		home = numberOfHoles;
		enemyHome = holes.length - 1;
		
		if(!randomize) {
			for(int i = 0; i < holes.length; i++) {
				holes[i] = numberOfSeeds;
			}
		}
		else {
			int totalSeedsForOneSide = (numberOfHoles*numberOfSeeds)+1;
			Random rand = new Random();
			int seedAllocator;
			int populateAllHoles = numberOfHoles;
			int player1Population = 0;
			int player2Population = home+1;
			
			while(populateAllHoles > 0) {
				
				seedAllocator = rand.nextInt(totalSeedsForOneSide);
				holes[player1Population] = seedAllocator;
				player1Population++;
				holes[player2Population] = seedAllocator;
				player2Population++;
				totalSeedsForOneSide -= seedAllocator;
				
				populateAllHoles--;
			}
		}
		
		holes[home] = 0;
		holes[enemyHome] = 0;
		indexOfLastDrop = 0;
		indexOfOpposite = 0;
		playCounter = 0;
		goAgain = false;
		player1Turn = true;
		
		pieRule = _pieRule;
	}
	
	public KalahGameBoardOL(String _name, int numberOfHoles, int numberOfSeeds, boolean randomize, boolean turn,int[] orderOfRandomSeeds) {
		
		player1Name = _name;
		player2Name = "Player 2";
		
		numHoles = numberOfHoles;
		holes = new int[(numberOfHoles*2)+2];
		home = numberOfHoles;
		enemyHome = holes.length-1;
		
		if(!randomize) {
			for(int i = 0; i < holes.length; i++) {
				holes[i] = numberOfSeeds;
			}
		}
		else {
			
			int counter = 0;
			int i = 0;
			int j = home+1;
			while(counter < orderOfRandomSeeds.length) {
				holes[i] = orderOfRandomSeeds[counter];
				holes[j] = orderOfRandomSeeds[counter];
				counter++;
				i++;
				j++;
			}
		}
		
		holes[home] = 0;
		holes[enemyHome] = 0;
		indexOfLastDrop = 0;
		indexOfOpposite = 0;
		goAgain = false;
		player1Turn = turn;
	}
	
	/* ********************************************************************************* Getters */
	public boolean getGoAgain() {
		
		return true;
	}
	
	public boolean getTurn() {
		return player1Turn;
	}
	
	public int getHomeIndex() {
		return home;
	}
	
	public int getEnemyHomeIndex() {
		return enemyHome;
	}
	
	public int getHome() {
		return holes[home];
	}
	
	public int getEnemyHome() {
		return holes[enemyHome];
	}
	
	public int[] getHolesArray() {
		return holes;
	}
	
	
	/* ********************************************************************************* Setters */
	
	
	
	/* *********************************************************** Helper Functions */
	
	public void indexOfLastOpposite(int _index) {
		int holesLength = holes.length-1;
		indexOfOpposite = (holesLength - _index) -1;
	}
	
	public void switchTurns() {
		if(goAgain == false) {
			if(player1Turn == true) {
				player1Turn = false;
			}
			else {
				player1Turn = true;
			}
		}
	}
	
	public void takeOpposite() {
		if(player1Turn) {
			holes[home] += holes[indexOfOpposite]; 
			holes[indexOfOpposite] = 0;
			holes[home] += 1;
			holes[indexOfLastDrop] = 0;
		}
		else {
			holes[enemyHome] += holes[indexOfOpposite]; 
			holes[indexOfOpposite] = 0;
			holes[enemyHome] += 1;
			holes[indexOfLastDrop] = 0;
		}
	}
	
	public int checkGameOver() {
		boolean player1SideDone = true;
		boolean player2SideDone = true;
		int i = 0;
		int indexOfPlayer2Start = home+1;
		
		while(i < numHoles) {
			if(holes[i] != 0) {
				player1SideDone = false;
			}
			if(holes[indexOfPlayer2Start] != 0) {
				player2SideDone = false;
			}
			
			i++;
			indexOfPlayer2Start++;
		}
		
		i = 0;
		indexOfPlayer2Start = home+1;
		
		if(player1SideDone || player2SideDone) {
			while(i < numHoles) {
				holes[home] += holes[i];
				holes[i] = 0;
				holes[enemyHome] += holes[indexOfPlayer2Start];
				holes[indexOfPlayer2Start] = 0;
				
				i++;
				indexOfPlayer2Start++;
			}
		}
		else {
			return 0;
		}
		
		if(holes[home] > holes[enemyHome]) {
			return 1;
		}
		else if(holes[home] < holes[enemyHome]) {
			return 2;
		}
		else if(holes[home] == holes[enemyHome]) {
			return 3;
		}
		
		return 0;
	}
	
	public void moveManager(int _index) {
		goAgain = false;
		int numSeedsInLastDrop = 0;
		int seedCount = holes[_index];
		
		holes[_index] = 0;
		_index++;
		
		if(seedCount == 0) {
			goAgain = true;
			return;
		}
		
		if(player1Turn) {
			while(seedCount > 0) {
				if(_index == holes.length-1) {
					numSeedsInLastDrop = holes[_index];
					indexOfLastDrop = _index;
					indexOfLastOpposite(_index);
					_index = 0;
					seedCount--;
				}
				else if(_index != enemyHome) {
					numSeedsInLastDrop = holes[_index];
					indexOfLastDrop = _index;
					holes[_index]++;
					indexOfLastOpposite(_index);
					_index++;
					seedCount--;
				}
			}
		}
		else {
			while(seedCount > 0) {
				if(_index == holes.length-1) {
					numSeedsInLastDrop = holes[_index];
					indexOfLastDrop = _index;
					indexOfLastOpposite(_index);
					holes[_index]++;
					_index=0;
					seedCount--;
				}
				else if(_index != home) {
					numSeedsInLastDrop = holes[_index];
					holes[_index]++;
					indexOfLastDrop = _index;
					indexOfLastOpposite(_index);
					_index++;
					seedCount--;
				} 
				else {
					_index++;
				}
			}
		}
		
		if(player1Turn && numSeedsInLastDrop == 0 && indexOfLastDrop < home) {
			if(holes[indexOfOpposite] != 0) {
				takeOpposite();
			}
		}
		else if(!player1Turn && numSeedsInLastDrop == 0 && indexOfLastDrop > home && indexOfLastDrop < enemyHome) {
			if(holes[indexOfOpposite] != 0) {
				takeOpposite();
			}
		}
		
		if(player1Turn && indexOfLastDrop == home) {
			goAgain = true;
		}
		else if(!player1Turn && indexOfLastDrop == enemyHome) {
			goAgain = true;
		}
		else {
			switchTurns();
		}
		
		playCounter++;
		
	}
	
	public void onlineMoveManager(int _index) {
		goAgain = false;
		int numSeedsInLastDrop = 0;
		int seedCount = holes[_index];
		
		holes[_index] = 0;
		_index++;
		
		if(player1Turn) {
			while(seedCount > 0) {
				if(_index == holes.length-1) {
					numSeedsInLastDrop = holes[_index];
					indexOfLastDrop = _index;
					indexOfLastOpposite(_index);
					_index = 0;
					seedCount--;
				}
				else if(_index != enemyHome) {
					numSeedsInLastDrop = holes[_index];
					indexOfLastDrop = _index;
					holes[_index]++;
					indexOfLastOpposite(_index);
					_index++;
					seedCount--;
				}
			}
		}
		else {
			while(seedCount > 0) {
				if(_index == holes.length-1) {
					numSeedsInLastDrop = holes[_index];
					indexOfLastDrop = _index;
					indexOfLastOpposite(_index);
					holes[_index]++;
					_index=0;
					seedCount--;
				}
				else if(_index != home) {
					numSeedsInLastDrop = holes[_index];
					holes[_index]++;
					indexOfLastDrop = _index;
					indexOfLastOpposite(_index);
					_index++;
					seedCount--;
				} 
				else {
					_index++;
				}
			}
		}
		
		if(player1Turn && numSeedsInLastDrop == 0 && indexOfLastDrop < home) {
			if(holes[indexOfOpposite] != 0) {
				takeOpposite();
			}
		}
		else if(!player1Turn && numSeedsInLastDrop == 0 && indexOfLastDrop > home && indexOfLastDrop < enemyHome) {
			if(holes[indexOfOpposite] != 0) {
				takeOpposite();
			}
		}
		
		if(player1Turn && indexOfLastDrop == home) {
			goAgain = true;
		}
		else if(!player1Turn && indexOfLastDrop == enemyHome) {
			goAgain = true;
		}
		else {
			goAgain = false;
			switchTurns();
		}
		
		playCounter++;
	}
}

