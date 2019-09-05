public class HelloWorld{
	public static void main(String[] args){
		//client server stuff will go here:
		
		//client-server variables
		Socket server;
		PrintStream serverSender;
		Scanner serverListener;
		
		//variables to add
		String input;
		//make these global on the GUI?
		int pitArraySize;
		int pits[];
		int seedsPerPit;
		int player_1_store;
		int player_2_store;
		
		//After the "Ready" button is pushed in the GUI...	
		server = new Socket(hostAddress, portNum);
		serverSender = new Printstream(server.getOutputStream());
		serverListener = new Scanner(server.getInputStream());
		
		input = serverListener.nextLine();
		JOPtionPane.showMessageDialog(null, input); //WELCOME
		
		input = serverListener.nextLine();
		JOptionPane.showMessageDialog(null, input); //INFO message
		
		//gets the array size from the INFO message
		pitArraySize = (Integer.parseInt(input.substring(5,6)) * 2) + 2 
		pits = new int[pitArraySize];
		seedsPerPit = Integer.parseInt(input.substring(7, 8));
		
		player_1_store = Integer.parseInt(input.substring(5,6);
		player_2_store = Array.getLength(pits) - 1;
		
		//if the board will be randomized, parse the INFO message accordingly
		if(input.charAt(input.length()-1) != 'S'){
			//size * 2
			int start = (input.length() - 1) - (pitArraySize - 2);
			String randomizedArray = input.substring(start, input.length()-1);
			//should now have something like "6 4 7 6 5 8 "
			char c;
			int j = 0;
			for(int i = 0; i < player_1_store; i++){
				c = randomizedArray.charAt(j);
				pits[i] = Character.getNumericValue(c);
				j += 2;
			}
			j = 0;
			for(int i = player_1_store + 1; i < Array.getLength(pits); ++i){
				pits [i] = pits[j];
				j++;			
			}
			
		}
		else{
			//set the pits array normally
			for(int i = 0; i < pitArraySize; ++i){
				if(i == player_1_store || i == player_2_store)
					continue;
				pits[i] = seedsPerPit;
			}
		}
		//at this point, we should have the correct pits[] array, and is ready to be
		//displayed in the GUI
		
		serverSender.println("READY"); 
		
		//start game and actively listen for input. I'm not sure how I'll
		//implement this
		
		//the INF command, tells the GUI it will recieve an 
		//update on the array, so it can display it accordingly
		
		//have some way of telling the client to make a move
		int move; //get this from the button the user presses!
		serverSender.println(move); //sends to kalahServer line 122
		
		input = serverListener.nextLine();
		JOptionPane.showMessageDialog(null, input);
		//if(input.equals("ILLEGAL");
		//stop the game, you lose
		//also, read the "LOSER" message that comes right after
		
		if(input.equals("INF")){
			for(int i = 0; i < pitArraySize; ++i){
				pits[i] = serverListener.nextInt();
			}
			serverListener.nextLine(); //clear line
		}
	}
}



//check CLIENT-SERVER variables
//check main
//check onlineWelcomeScreen button handler