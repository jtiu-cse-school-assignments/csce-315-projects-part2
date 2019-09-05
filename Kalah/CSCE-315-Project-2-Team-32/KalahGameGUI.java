/* ***************************************************************************************
 *                                  WELCOME VIN AND FRANK
 * 
 * 	Instructions for client stuff:
 * 		* The global variables are under the partition "/// CLIENT-SERVER Variables"
 * 		1) Go to the method called "onlineMenuScreen()"
 * 			a) Go to the button handler for btnOnlineConnect
 * 				* This is where I initialized the client-side variables (Sockets and streams)
 * 				* I get the WELCOME reply from server here and make it appear in the GUI
 * 				* I get the INFO reply from server here and make it appear in the GUI
 * 			* Pressing the connect button will get all the info from server and open up 
 * 				the GUI screen called onlineBufferScreen
 * 			b) Go to the method called "onlineBufferScreen()"
 * 				- Go to the btnREADY button handler
 * 				* When this button is pressed, it will send "READY" to server
 * 		2) Every time a button is pressed, it goes to the action handler called onlineListener
 * 			* It sends the pressed button's index to server then waits for a reply
 * 			* It will then update the board on the server's side
 *  
 * ****************************************************************************************/

package kalah.GUI;

import kalah.common.*;
import java.util.*;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.io.IOException;
import java.io.PrintStream;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JList;
import javax.imageio.IIOException;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import javax.swing.Timer;

import javax.swing.JOptionPane;

public class KalahGameGUI {

	/*//////////////////////////////////////////////////////////////////////////////////////////////////// GUI Variables */

	private JFrame frame;
	
	private JPanel welcomeScreen;
	private JButton btnOnline;
	private JButton btnLocal;
	
	private JPanel onlineMenuScreen;
	private JButton btnOnlineBack;
	private JButton btnOnlineConnect;
	private JTextField tfPort;
	private JTextField tfHost;
	
	private JPanel localMenuScreen;
	private JButton btnLocalSinglePlayer;
	private JButton btnLocalMultiplayer;
	private JButton btnLocalClientPlayer;
	private JButton btnLocalBack;
	private JButton btnContinuePlaying;
	
	private JPanel settingsScreen;
	private JLabel lblNumberOfHoles;
	private JLabel lblSeedCount;
	private JLabel lblRandomize;
	private JLabel lblTimeLimit;
	private JLabel lblS;
	private JLabel lblPieRule;
	private JLabel lblPlayer1Name;
	private JLabel lblPlayer2Name;
	private JLabel lblPortNumber;
	private JTextField tfPortNumber;
	private JComboBox cbNumberOfHoles;
	private JComboBox cbSeedCount;
	private JComboBox cbRandomize;
	private JComboBox cbTimeLimit;
	private JComboBox cbPieRule;
	private JTextField tfTimeLimit;
	private JTextField tfPlayer1Name;
	private JTextField tfPlayer2Name;
	private JButton btnSettingsStart;
	private JButton btnSettingsBack;
	private String forLocalHome;
	private String forLocalEnemyHome;
	
	private String forOnlineHome;
	private String forOnlineEnemyHome;
	
	private JPanel onlineBoardScreen;
	private JLabel lblPlayerNameDisplayOnline;
	private JTextField tfOnlineHome;
	private JTextField tfOnlineEnemyHome;
	private JButton btnOnlineHoles[];
	private JTextField tfOnlineOtherHoles[]; 
	
	private JPanel localBoardScreen;
	private JLabel lblPlayerNameDisplay;
	private JButton btnLocalHoles[];
	private JButton btnQuit;
	private JTextField tfLocalOtherHoles[];
	private JTextField tfLocalEnemyHome;
	private JTextField tfLocalHome;
	
	public void processCommand(String reply) throws IOException {
		System.out.println("Processed " + reply);
		if(reply.equals("ILLEGAL") ) {
    		onlineBoardScreen.setVisible(false);
    		onlineBoardScreen();
    		onlineBoardScreen.setVisible(true);
			JOptionPane.showMessageDialog(null, "LOSER");
			onlineBoardScreen.setVisible(false);
			onlineMenuScreen.setVisible(true);
		}
		else if(reply.equals("TIME")) {
    		onlineBoardScreen.setVisible(false);
    		onlineBoardScreen();
    		onlineBoardScreen.setVisible(true);
			JOptionPane.showMessageDialog(null, "TIME");
			onlineBoardScreen.setVisible(false);
			onlineMenuScreen.setVisible(true);
		}
		else if(reply.equals("LOSER")) {
    		onlineBoardScreen.setVisible(false);
    		onlineBoardScreen();
    		onlineBoardScreen.setVisible(true);
			JOptionPane.showMessageDialog(null, "LOSER");
			onlineBoardScreen.setVisible(false);
			onlineMenuScreen.setVisible(true);
		}
		else if(reply.equals("WINNER")) {
    		onlineBoardScreen.setVisible(false);
    		onlineBoardScreen();
    		onlineBoardScreen.setVisible(true);
			JOptionPane.showMessageDialog(null, "WINNER");
			onlineBoardScreen.setVisible(false);
			onlineMenuScreen.setVisible(true);
		}
		else if(reply.equals("TIE")) {
    		onlineBoardScreen.setVisible(false);
    		onlineBoardScreen();
    		onlineBoardScreen.setVisible(true);
			JOptionPane.showMessageDialog(null, "TIE");
			onlineBoardScreen.setVisible(false);
			onlineMenuScreen.setVisible(true);
		}
		else if(reply.equals("OK")){
			forOnlineHome = Integer.toString(client.board.getHome());
			forOnlineEnemyHome = Integer.toString(client.board.getEnemyHome());
			client.board.increaseTurn();
			//client.board.nextPlayerTurn();
 			turn = client.board.getTurn();
 			System.out.println("Turn: " + turn);
 			//if(turn) System.out.println("still player1 turn");
			//client.board.printBoard();
			
			onlineBoardScreen.setVisible(false);
			onlineBoardScreen();
			onlineBoardScreen.setVisible(true);

		}
	}
	private ActionListener onlineListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	
        		int index = 0;
        	
        		for(int i = 0; i < numberOfHoles; i ++) {
        			if(btnOnlineHoles[i] == (JButton)e.getSource()) {
                      index = i;
                      break;
        			}
        		}
        		
        		client.addMove(index);
        		goAgain = client.board.increment2(index);
        		forOnlineHome = Integer.toString(client.board.getHome());
	    		forOnlineEnemyHome = Integer.toString(client.board.getEnemyHome());
	    		
        		if(!goAgain) {
	    			try {
						String reply = client.sendMoves();
						processCommand(reply);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	    		}
        		else {
            		onlineBoardScreen.setVisible(false);
            		try {
						onlineBoardScreen();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            		onlineBoardScreen.setVisible(true);
        		}	
        }
    };
    private ActionListener serverListener = new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
			String result;
			try {
				result = client.getMove();
				processCommand(result);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
    		}
    };
	private ActionListener localListener = new ActionListener() {
        
		public void actionPerformed(ActionEvent e) {
        	
        		int index = 0;

	    		for(int i = 0; i < numberOfHoles; i ++) {
	    			if(btnLocalHoles[i] == (JButton)e.getSource()) {
	                  index = i;
	                  break;
	    			}
	    		}
	    	        		
	    		goAgain = localGame.increment2(index);
	    		
	    		int result = localGame.checkGameOver();
	    		forLocalHome = Integer.toString(localGame.getHome());
	    		forLocalEnemyHome = Integer.toString(localGame.getEnemyHome());
	    		
	    		switch(result) {
	    		case 0:
		    		localBoardScreen.setVisible(false);
		    		localBoardScreen();
		    		localBoardScreen.setVisible(true);
	    			break;
	    		case 1:
		    		localBoardScreen.setVisible(false);
		    		localBoardScreen();
		    		localBoardScreen.setVisible(true);
	    			JOptionPane.showMessageDialog(null, "Player 1 Wins");
	    			localBoardScreen.setVisible(false);
	    			localMenuScreen.setVisible(true);
	    			break;
	    		case 2:
		    		localBoardScreen.setVisible(false);
		    		localBoardScreen();
		    		localBoardScreen.setVisible(true);
	    			JOptionPane.showMessageDialog(null, "Player 2 Wins");
	    			localBoardScreen.setVisible(false);
	    			localMenuScreen.setVisible(true);
	    			break;
	    		case 3:
		    		localBoardScreen.setVisible(false);
		    		localBoardScreen();
		    		localBoardScreen.setVisible(true);
	    			JOptionPane.showMessageDialog(null, "Tie");
	    			localBoardScreen.setVisible(false);
	    			localMenuScreen.setVisible(true);
	    			break;
	    		default:
	    			break;
	    		}
	    }
    };
    	
	/*////////////////////////////////////////////////////////////////////////////////////////// Kalah.Common Variables */

	private KalahGameBoard localGame;
    
	/*//////////////////////////////////////////////////////////////////////////////////////////// GUI Helper Variables */
	
	private boolean online;
	private boolean AI;
	private boolean randomize;
	private boolean pieRule;
	private boolean timeLimit;
	private boolean quit;
	private boolean turn;
	
	private int numberOfHoles;
	private int seedCount;
	private int time = 5000;
	private boolean goAgain;
	
	private String player1Name;
	private String player2Name;
	private JTextField tfPlayerName;
;
	
	/*///////////////////////////////////////////////////////////////////////////////////////// CLIENT-SERVER Variables */

	private static String hostAddress;
	private static int portNum;
	
	
	private KalahPlayer client;
	
	
	/*////////////////////////////////////////////////////////////////////////////////////////////////////////// Main */
	
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KalahGameGUI window = new KalahGameGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*/////////////////////////////////////////////////////////////////////////////////////////////////// Constructor */

	public KalahGameGUI() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		welcomeScreen();
		onlineMenuScreen();
		localMenuScreen();
		settingsScreen();
	}

	/*/////////////////////////////////////////////////////////////////////////////// Initialize components by panels */

	public void welcomeScreen() {
		
		welcomeScreen = new JPanel();
		welcomeScreen.setBackground(Color.BLACK);
		frame.getContentPane().add(welcomeScreen, "name_69122380779768");
		welcomeScreen.setLayout(null);
		
		btnOnline = new JButton("Client");
		btnOnline.setBounds(0, 226, 450, 29);
		welcomeScreen.add(btnOnline);
		btnOnline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				online = true;
				welcomeScreen.setVisible(false);
				onlineMenuScreen.setVisible(true);
			}
		});
		
		btnLocal = new JButton("Server");
		btnLocal.setBounds(0, 250, 450, 29);
		welcomeScreen.add(btnLocal);
		btnLocal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				online = false;
				welcomeScreen.setVisible(false);
				localMenuScreen.setVisible(true);
			}
		});
		
		JLabel welcomeScreenBackgroundPicture = new JLabel("");
		welcomeScreenBackgroundPicture.setIcon(new ImageIcon(KalahGameGUI.class.getResource("/kalah/resources/Kalah.png")));
		welcomeScreenBackgroundPicture.setBounds(0, 0, 450, 193);
		welcomeScreen.add(welcomeScreenBackgroundPicture);
	}
	
	public void onlineMenuScreen() throws IOException{
		
		/* 	Path:
		 * 		welcomeScreen -> Online
		 */
		
		onlineMenuScreen = new JPanel();
		onlineMenuScreen.setBackground(Color.BLACK);
		frame.getContentPane().add(onlineMenuScreen, "name_69122380779768");
		onlineMenuScreen.setLayout(null);
		
		tfPort = new JTextField();
		tfPort.setText("Enter Port Number");
		tfPort.setToolTipText("Spefifies the port the user wants to connect in to (Ex. 80)\n");
		tfPort.setForeground(Color.BLACK);
		tfPort.setBounds(139, 203, 126, 26);
		onlineMenuScreen.add(tfPort);
		tfPort.setColumns(10);
		tfPort.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tfPort.setText(null);
			}
		});
		
		tfHost = new JTextField();
		tfHost.setText("Enter Host Address");
		tfHost.setToolTipText("Specifies the IP Address the user wants to connect to (EX. 255.255.255.255)");
		tfHost.setBounds(5, 203, 135, 26);
		onlineMenuScreen.add(tfHost);
		tfHost.setColumns(10);
		tfHost.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tfHost.setText(null);
			}
		});
		
		tfPlayerName = new JTextField();
		tfPlayerName.setToolTipText("Enter your name here (Ex. Julian)");
		tfPlayerName.setText("Enter Name");
		tfPlayerName.setBounds(264, 203, 92, 26);
		onlineMenuScreen.add(tfPlayerName);
		tfPlayerName.setColumns(10);
		tfPlayerName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tfPlayerName.setText(null);
			}
		});
		
		btnOnlineBack = new JButton("Back");
		btnOnlineBack.setBounds(0, 226, 450, 29);
		onlineMenuScreen.add(btnOnlineBack);
		btnOnlineBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfPort.setText("Enter Port Number");
				tfHost.setText("Enter Host Address");
				tfPlayerName.setText("Enter Name");
				onlineMenuScreen.setVisible(false);
				welcomeScreen.setVisible(true);
			}
		});
		
		btnOnlineConnect = new JButton("Connect");
		btnOnlineConnect.setBounds(0, 249, 450, 29);
		onlineMenuScreen.add(btnOnlineConnect);
		btnOnlineConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hostAddress = tfHost.getText();
				portNum = Integer.parseInt(tfPort.getText());
				try {
					client = new KalahPlayer();
					client.setUp();
					online = true;
					turn = true;
					time = client.getTimeLimit();
					forOnlineHome = Integer.toString(client.board.getHome());
					forOnlineEnemyHome = Integer.toString(client.board.getEnemyHome());
					numberOfHoles = client.board.getSize();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				onlineMenuScreen.setVisible(false);
				try {
					onlineBoardScreen();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				onlineBoardScreen.setVisible(true);
			}
		});
		
		JLabel onlineMenuScreenBackgroundPicture = new JLabel("");
		onlineMenuScreenBackgroundPicture.setIcon(new ImageIcon(KalahGameGUI.class.getResource("/kalah/resources/Kalah.png")));
		onlineMenuScreenBackgroundPicture.setBounds(0, 0, 450, 193);
		onlineMenuScreen.add(onlineMenuScreenBackgroundPicture);
		
		onlineMenuScreen.setVisible(false);
	}
		
	public void localMenuScreen() {
		
		/* 	Path:
		 * 		welcomeScreen -> Local
		 */
		
		localMenuScreen = new JPanel();
		frame.getContentPane().add(localMenuScreen);
		localMenuScreen.setBackground(Color.BLACK);
		localMenuScreen.setLayout(null);
		
		btnContinuePlaying = new JButton("Continue Playing");
		btnContinuePlaying.setBounds(0, 154, 450, 29);
		localMenuScreen.add(btnContinuePlaying);
		btnContinuePlaying.setVisible(false);
		btnContinuePlaying.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				localMenuScreen.setVisible(false);
				localBoardScreen.setVisible(true);
			}
		});
		
		btnLocalBack = new JButton("Back");
		btnLocalBack.setBounds(0, 178, 450, 29);
		localMenuScreen.add(btnLocalBack);
		btnLocalBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(quit == true) {
					Object[] options = { "OK", "CANCEL" };
					int n =JOptionPane.showOptionDialog(null, "Click OK to quit current game session", "Warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[0]);
					
					if(n==0) {
						btnContinuePlaying.setVisible(false);
						localMenuScreen.setVisible(false);
						welcomeScreen.setVisible(true);
					}
				}
				else {
					btnContinuePlaying.setVisible(false);
					localMenuScreen.setVisible(false);
					welcomeScreen.setVisible(true);
				}
			}
		});
		
		btnLocalSinglePlayer = new JButton("Play Agaisnt AI");
		btnLocalSinglePlayer.setBounds(0, 202, 450, 29);
		localMenuScreen.add(btnLocalSinglePlayer);
		btnLocalSinglePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AI = true;
				localMenuScreen.setVisible(false);
				settingsScreen.setVisible(true);
				lblPlayer2Name.setVisible(false);
				tfPlayer2Name.setVisible(false);
				lblPortNumber.setVisible(false);
				tfPortNumber.setVisible(false);
				lblPlayer1Name.setBounds(10, 208, 103, 16);
				tfPlayer1Name.setBounds(108, 205, 338, 21);
			}
		});
		
		btnLocalMultiplayer = new JButton("Play Agaisnt Local Opponent");
		btnLocalMultiplayer.setBounds(0, 226, 450, 29);
		localMenuScreen.add(btnLocalMultiplayer);
		btnLocalMultiplayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AI = false;
				localMenuScreen.setVisible(false);
				settingsScreen.setVisible(true);
				lblPlayer2Name.setVisible(true);
				lblPortNumber.setVisible(false);
				tfPortNumber.setVisible(false);
				lblPlayer2Name.setBounds(10, 208, 103, 16);
				tfPlayer2Name.setVisible(true);
				tfPlayer2Name.setBounds(108, 205, 338, 21);
				lblPlayer1Name.setBounds(10, 185, 103, 16);
				tfPlayer1Name.setBounds(108, 181, 338, 21);
			}
		});
		
		btnLocalClientPlayer = new JButton("Play Agaisnt Remote Client");
		btnLocalClientPlayer.setBounds(0, 250, 450, 29);
		localMenuScreen.add(btnLocalClientPlayer);
		btnLocalClientPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AI = false;
				localMenuScreen.setVisible(false);
				settingsScreen.setVisible(true);
				lblPlayer2Name.setVisible(false);
				tfPlayer2Name.setVisible(false);
				lblPlayer1Name.setBounds(10, 185, 103, 16);
				tfPlayer1Name.setBounds(108, 181, 338, 21);
				
				lblPortNumber.setVisible(true);
				lblPortNumber.setBounds(10, 208, 103, 16);
				tfPortNumber.setVisible(true);
				tfPortNumber.setBounds(108, 205, 338, 21);
				
			}
		});
		
		JLabel localMenuScreenBackgroundPicture = new JLabel("");
		localMenuScreenBackgroundPicture.setIcon(new ImageIcon(KalahGameGUI.class.getResource("/kalah/resources/Kalah.png")));
		localMenuScreenBackgroundPicture.setBounds(0, 0, 450, 193);
		localMenuScreen.add(localMenuScreenBackgroundPicture);
		
		localMenuScreen.setVisible(false);
	}
	
	public void settingsScreen() {
		
		/* 	Path:
		 * 		welcomeScreen -> Online/Local -> Single Player/Multiplayer
		 */
		
		settingsScreen = new JPanel();
		settingsScreen.setBackground(Color.BLACK);
		frame.getContentPane().add(settingsScreen, "name_69122380779770");
		settingsScreen.setLayout(null);
		
		lblNumberOfHoles = new JLabel("Number of Holes: ");
		lblNumberOfHoles.setForeground(Color.WHITE);
		lblNumberOfHoles.setBounds(33, 31, 152, 16);
		settingsScreen.add(lblNumberOfHoles);
		
		lblSeedCount = new JLabel("Seed Count:");
		lblSeedCount.setForeground(Color.WHITE);
		lblSeedCount.setBounds(33, 60, 164, 16);
		settingsScreen.add(lblSeedCount);
		
		lblRandomize = new JLabel("Randomize Seeds:");
		lblRandomize.setForeground(Color.WHITE);
		lblRandomize.setBounds(33, 88, 122, 16);
		settingsScreen.add(lblRandomize);
		
		lblTimeLimit = new JLabel("Time Limit: ");
		lblTimeLimit.setForeground(Color.WHITE);
		lblTimeLimit.setBounds(33, 117, 75, 16);
		settingsScreen.add(lblTimeLimit);
		
		lblS = new JLabel("seconds");
		lblS.setForeground(Color.WHITE);
		lblS.setBounds(227, 116, 61, 16);
		settingsScreen.add(lblS);
		lblS.setVisible(false);
		
		lblPlayer1Name = new JLabel();
		lblPlayer1Name.setText("Player 1 Name: ");
		lblPlayer1Name.setBackground(Color.WHITE);
		lblPlayer1Name.setForeground(Color.WHITE);
		lblPlayer1Name.setBounds(10, 208, 103, 16);
		settingsScreen.add(lblPlayer1Name);
		
		lblPlayer2Name = new JLabel();
		lblPlayer2Name.setText("Player 2 Name: ");
		lblPlayer2Name.setBackground(Color.WHITE);
		lblPlayer2Name.setForeground(Color.WHITE);
		lblPlayer2Name.setBounds(10, 185, 103, 16);
		settingsScreen.add(lblPlayer2Name);
		
		lblPortNumber = new JLabel();
		lblPortNumber.setText("Port Number: ");
		lblPortNumber.setBackground(Color.WHITE);
		lblPortNumber.setForeground(Color.WHITE);
		lblPortNumber.setBounds(10, 185, 103, 16);
		settingsScreen.add(lblPortNumber);
		
		lblPieRule = new JLabel("Pie Rule: ");
		lblPieRule.setForeground(Color.WHITE);
		lblPieRule.setBounds(33, 144, 75, 16);
		settingsScreen.add(lblPieRule);
		
		cbNumberOfHoles = new JComboBox();
		cbNumberOfHoles.setModel(new DefaultComboBoxModel(new String[] {"Default", "4", "5", "6", "7", "8", "9"}));
		cbNumberOfHoles.setBounds(300, 30, 100, 20);
		settingsScreen.add(cbNumberOfHoles);
		
		cbSeedCount = new JComboBox();
		cbSeedCount.setModel(new DefaultComboBoxModel(new String[] {"Default", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
		cbSeedCount.setBounds(300, 59, 100, 20);
		settingsScreen.add(cbSeedCount);
		
		cbRandomize = new JComboBox();
		cbRandomize.setModel(new DefaultComboBoxModel(new String[] {"Default", "Enable", "Disable"}));
		cbRandomize.setBounds(300, 87, 100, 20);
		settingsScreen.add(cbRandomize);
		
		cbTimeLimit = new JComboBox();
		cbTimeLimit.setModel(new DefaultComboBoxModel(new String[] {"Default", "Enable", "Disable"}));
		cbTimeLimit.setBounds(300, 115, 100, 20);
		settingsScreen.add(cbTimeLimit);
		cbTimeLimit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbTimeLimit.getSelectedIndex() == 1) {
					tfTimeLimit.setVisible(true);
					lblS.setVisible(true);
					
				}
				else {
					tfTimeLimit.setVisible(false);
					lblS.setVisible(false);
				}
			}
		});
		
		cbPieRule = new JComboBox();
		cbPieRule.setModel(new DefaultComboBoxModel(new String[] {"Default", "Enable", "Disable"}));
		cbPieRule.setBounds(300, 143, 100, 20);
		settingsScreen.add(cbPieRule);
		
		tfTimeLimit = new JTextField();
		tfTimeLimit.setBounds(115, 115, 100, 21);
		settingsScreen.add(tfTimeLimit);
		tfTimeLimit.setColumns(10);
		tfTimeLimit.setVisible(false);
		
		tfPlayer1Name = new JTextField();
		tfPlayer1Name.setBounds(108, 205, 338, 21);
		settingsScreen.add(tfPlayer1Name);
		tfPlayer1Name.setColumns(10);
		
		tfPlayer2Name = new JTextField();
		tfPlayer2Name.setBounds(108, 181, 338, 21);
		settingsScreen.add(tfPlayer2Name);
		tfPlayer2Name.setColumns(10);
		
		tfPortNumber = new JTextField();
		tfPortNumber.setBounds(108, 181, 338, 21);
		settingsScreen.add(tfPortNumber);
		tfPortNumber.setColumns(10);
		
		btnSettingsBack = new JButton("Back");
		btnSettingsBack.setBounds(0, 226, 450, 29);
		settingsScreen.add(btnSettingsBack);
		btnSettingsBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfPlayer1Name.setText(null);
				tfPlayer2Name.setText(null);
				settingsScreen.setVisible(false);
				localMenuScreen.setVisible(true);
			}
		});
		
		btnSettingsStart = new JButton("Start");
		btnSettingsStart.setBounds(0, 250, 450, 29);
		settingsScreen.add(btnSettingsStart);
		btnSettingsStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(online == false) {
					if(cbNumberOfHoles.getSelectedIndex() == 0) {
						numberOfHoles = 6;
					}
					else {
						String numberOfHolesTemp = (String)cbNumberOfHoles.getSelectedItem();
						numberOfHoles = Integer.parseInt(numberOfHolesTemp);
					}
					
					if(cbSeedCount.getSelectedIndex() == 0) {
						seedCount = 4;
					}
					else {
						String seedCountTemp = (String)cbSeedCount.getSelectedItem();
						seedCount = Integer.parseInt(seedCountTemp);
					}
					
					if(cbRandomize.getSelectedItem() == "Enable") {
						randomize = true;
					}
					else {
						randomize = false;
					}
					
					if(cbTimeLimit.getSelectedItem() == "Enable") {
						timeLimit = true;
						time = Integer.parseInt(tfTimeLimit.getText());
						time *= 100;
					}
					else {
						timeLimit = false;
					}
					
					if(cbPieRule.getSelectedItem() == "Enable") {
						pieRule = true;
					}
					else {
						pieRule = false;
					}
					
					player1Name = tfPlayer1Name.getText();
					player2Name = tfPlayer2Name.getText();
					
//					System.out.println("Online: " + online);
//					System.out.println("AI: " + AI);
//					System.out.println("numberOfHoles: " + numberOfHoles);
//					System.out.println("seedCount: " + seedCount);
//					System.out.println("randomize: " + randomize);
//					System.out.println("timeLimit: " + timeLimit);
//					System.out.println("Time in ms: " + time);
//					System.out.println("pieRule: " + pieRule);
//					System.out.println("player 1 name: " + player1Name);
//					System.out.println("player 2 name: " + player2Name);
//					
					localGame = new KalahGameBoard(player1Name, player2Name, numberOfHoles, seedCount, randomize);
					turn = localGame.getTurn();
					forLocalHome = Integer.toString(localGame.getHome());
					forLocalEnemyHome = Integer.toString(localGame.getEnemyHome());

					
					settingsScreen.setVisible(false);
					localBoardScreen();
					localBoardScreen.setVisible(true);
				}
			}
		});
		
		JLabel settingsScreenBackgroundPicture = new JLabel("");
		settingsScreenBackgroundPicture.setIcon(new ImageIcon(KalahGameGUI.class.getResource("/kalah/resources/Kalah2.png")));
		settingsScreenBackgroundPicture.setBounds(0, 0, 450, 193);
		settingsScreen.add(settingsScreenBackgroundPicture);
		
		settingsScreen.setVisible(false);
	}
	
	public void onlineBoardScreen() throws IOException {
		
		client.board.printBoard();
		//System.out.println("gets here");
		onlineBoardScreen = new JPanel();
		frame.getContentPane().add(onlineBoardScreen);
		onlineBoardScreen.setBackground(Color.BLACK);
		onlineBoardScreen.setLayout(null);
		
		onlineBoardScreen.removeAll();
		//System.out.println("gets here");
		if(turn) {
			lblPlayerNameDisplayOnline = new JLabel("Player 1's Turn");
			lblPlayerNameDisplayOnline.setFont(new Font("Herculanum", Font.PLAIN, 30));
			lblPlayerNameDisplayOnline.setHorizontalAlignment(SwingConstants.CENTER);
			lblPlayerNameDisplayOnline.setBounds(64, 120, 315, 41);
			onlineBoardScreen.add(lblPlayerNameDisplayOnline);
			lblPlayerNameDisplayOnline.setForeground(Color.WHITE);
			
			new Timer(time, serverListener).start();
		}
		else {
			lblPlayerNameDisplayOnline = new JLabel("Player 2's Turn");
			System.out.println("gets here");
			lblPlayerNameDisplayOnline.setFont(new Font("Herculanum", Font.PLAIN, 30));
			lblPlayerNameDisplayOnline.setHorizontalAlignment(SwingConstants.CENTER);
			lblPlayerNameDisplayOnline.setBounds(64, 120, 315, 41);
			onlineBoardScreen.add(lblPlayerNameDisplayOnline);
			lblPlayerNameDisplayOnline.setForeground(Color.WHITE);
		}
		
		tfOnlineEnemyHome = new JTextField(forOnlineEnemyHome);
		tfOnlineEnemyHome.setHorizontalAlignment(SwingConstants.CENTER);
		tfOnlineEnemyHome.setBounds(390, 94, 51, 81);
		tfOnlineEnemyHome.setEnabled(false);
		onlineBoardScreen.add(tfOnlineEnemyHome);
		tfOnlineEnemyHome.setColumns(10);
		
		tfOnlineHome = new JTextField(forOnlineHome);
		tfOnlineHome.setHorizontalAlignment(SwingConstants.CENTER);
		tfOnlineHome.setEnabled(false);
		tfOnlineHome.setBounds(10, 94, 51, 81);
		onlineBoardScreen.add(tfOnlineHome);
		tfOnlineHome.setColumns(10);
		
		int buttonPosition = 0;
		int player2ButtonStart = client.board.getHomeIndex();
		
		btnOnlineHoles = new JButton[numberOfHoles];
		for(int i = 0; i < numberOfHoles; i++) {
			btnOnlineHoles[i] = new JButton(String.valueOf(client.board.getPitArray()[player2ButtonStart+1]));
			btnOnlineHoles[i].addActionListener(onlineListener);
			btnOnlineHoles[i].setBounds(50 + buttonPosition, 190, 30, 30);
			onlineBoardScreen.add(btnOnlineHoles[i]);
			switch(numberOfHoles) {
			case 4:
				buttonPosition += 105;
				break;
			case 5:
				buttonPosition += 81;
				break;
			case 6:
				buttonPosition += 65;
				break;
			case 7:
				buttonPosition += 54;
				break;
			case 8:
				buttonPosition += 46;
				break;
			case 9:
				buttonPosition += 39;
				break;
			default:
				break;
			}
			player2ButtonStart++;
		}
		
		int player2HolesPOV1 = client.board.getHomeIndex();
		int textFieldPosition = 0;
		tfOnlineOtherHoles = new JTextField[numberOfHoles];
		for(int i = 0; i < numberOfHoles; i++) {
			tfOnlineOtherHoles[i] = new JTextField();
			tfOnlineOtherHoles[i].setHorizontalAlignment(SwingConstants.CENTER);
			tfOnlineOtherHoles[i].setText(Integer.toString(client.board.getPitArray()[player2HolesPOV1-1]));
			tfOnlineOtherHoles[i].setBounds(50 + textFieldPosition, 60, 30, 30);
			tfOnlineOtherHoles[i].setColumns(10);
			tfOnlineOtherHoles[i].setEditable(false);
			onlineBoardScreen.add(tfOnlineOtherHoles[i]);
			switch(numberOfHoles) {
			case 4:
				textFieldPosition += 105;
				break;
			case 5:
				textFieldPosition += 81;
				break;
			case 6:
				textFieldPosition += 65;
				break;
			case 7:
				textFieldPosition += 54;
				break;
			case 8:
				textFieldPosition += 46;
				break;
			case 9:
				textFieldPosition += 39;
				break;
			default:
				break;
			}
			player2HolesPOV1--;
		}
		

		
		JLabel lblonlineBoardScreenPicture = new JLabel("");
		lblonlineBoardScreenPicture.setIcon(new ImageIcon(KalahGameGUI.class.getResource("/kalah/resources/KalahBoardEmpty.png")));
		lblonlineBoardScreenPicture.setBounds(0, 43, 450, 200);
		onlineBoardScreen.add(lblonlineBoardScreenPicture);
		
		onlineBoardScreen.setVisible(false);

	}
	public void localBoardScreen() {
		localBoardScreen = new JPanel();
		frame.getContentPane().add(localBoardScreen);
		localBoardScreen.setBackground(Color.BLACK);
		localBoardScreen.setLayout(null);
		
		localBoardScreen.removeAll();
		
		if(!goAgain) {
			localGame.nextPlayer();
			localGame.increaseTurn();
		}
		
		turn = localGame.getTurn();
		if(turn) {
			tfLocalEnemyHome = new JTextField(forLocalEnemyHome);
			tfLocalEnemyHome.setHorizontalAlignment(SwingConstants.CENTER);
			tfLocalEnemyHome.setBounds(10, 94, 51, 81);
			tfLocalEnemyHome.setEnabled(false);
			localBoardScreen.add(tfLocalEnemyHome);
			tfLocalEnemyHome.setColumns(10);
			
			tfLocalHome = new JTextField(forLocalHome);
			tfLocalHome.setHorizontalAlignment(SwingConstants.CENTER);
			tfLocalHome.setEnabled(false);
			tfLocalHome.setBounds(390, 94, 51, 81);
			localBoardScreen.add(tfLocalHome);
			tfLocalHome.setColumns(10);
			
			int buttonPosition = 0;
			btnLocalHoles = new JButton[numberOfHoles];
			for(int i = 0; i < numberOfHoles; i++) {
				btnLocalHoles[i] = new JButton(String.valueOf(localGame.getPitArray()[i]));
				btnLocalHoles[i].addActionListener(localListener);
				btnLocalHoles[i].setBounds(50 + buttonPosition, 190, 30, 30);
				localBoardScreen.add(btnLocalHoles[i]);
				switch(numberOfHoles) {
				case 4:
					buttonPosition += 105;
					break;
				case 5:
					buttonPosition += 81;
					break;
				case 6:
					buttonPosition += 65;
					break;
				case 7:
					buttonPosition += 54;
					break;
				case 8:
					buttonPosition += 46;
					break;
				case 9:
					buttonPosition += 39;
					break;
				default:
					break;
				}
			}
			
			int player2HolesPOV1 = localGame.getEnemyHomeIndex();
			int textFieldPosition = 0;
			tfLocalOtherHoles = new JTextField[numberOfHoles];
			for(int i = 0; i < numberOfHoles; i++) {
				tfLocalOtherHoles[i] = new JTextField();
				tfLocalOtherHoles[i].setHorizontalAlignment(SwingConstants.CENTER);
				tfLocalOtherHoles[i].setText(Integer.toString(localGame.getPitArray()[player2HolesPOV1-1]));
				tfLocalOtherHoles[i].setBounds(50 + textFieldPosition, 60, 30, 30);
				tfLocalOtherHoles[i].setColumns(10);
				tfLocalOtherHoles[i].setEditable(false);
				localBoardScreen.add(tfLocalOtherHoles[i]);
				switch(numberOfHoles) {
				case 4:
					textFieldPosition += 105;
					break;
				case 5:
					textFieldPosition += 81;
					break;
				case 6:
					textFieldPosition += 65;
					break;
				case 7:
					textFieldPosition += 54;
					break;
				case 8:
					textFieldPosition += 46;
					break;
				case 9:
					textFieldPosition += 39;
					break;
				default:
					break;
				}
				player2HolesPOV1--;
			}
			
			lblPlayerNameDisplay = new JLabel(player1Name + "'s Turn");
			lblPlayerNameDisplay.setFont(new Font("Herculanum", Font.PLAIN, 30));
			lblPlayerNameDisplay.setHorizontalAlignment(SwingConstants.CENTER);
			lblPlayerNameDisplay.setBounds(64, 120, 315, 41);
			localBoardScreen.add(lblPlayerNameDisplay);
			lblPlayerNameDisplay.setForeground(Color.WHITE);
		}
		else {
			
			tfLocalEnemyHome = new JTextField(forLocalEnemyHome);
			tfLocalEnemyHome.setHorizontalAlignment(SwingConstants.CENTER);
			tfLocalEnemyHome.setBounds(390, 94, 51, 81);
			tfLocalEnemyHome.setEnabled(false);
			localBoardScreen.add(tfLocalEnemyHome);
			tfLocalEnemyHome.setColumns(10);
			
			tfLocalHome = new JTextField(forLocalHome);
			tfLocalHome.setHorizontalAlignment(SwingConstants.CENTER);
			tfLocalHome.setEnabled(false);
			tfLocalHome.setBounds(10, 94, 51, 81);
			localBoardScreen.add(tfLocalHome);
			tfLocalHome.setColumns(10);
			
			int player2ButtonStart = localGame.getHomeIndex();
			int buttonPosition = 0;
			btnLocalHoles = new JButton[numberOfHoles];
			for(int i = 0; i < numberOfHoles; i++) {
				btnLocalHoles[i] = new JButton(String.valueOf(localGame.getPitArray()[player2ButtonStart+1]));
				btnLocalHoles[i].addActionListener(localListener);
				btnLocalHoles[i].setBounds(50 + buttonPosition, 190, 30, 30);
				localBoardScreen.add(btnLocalHoles[i]);
				switch(numberOfHoles) {
				case 4:
					buttonPosition += 105;
					break;
				case 5:
					buttonPosition += 81;
					break;
				case 6:
					buttonPosition += 65;
					break;
				case 7:
					buttonPosition += 54;
					break;
				case 8:
					buttonPosition += 46;
					break;
				case 9:
					buttonPosition += 39;
					break;
				default:
					break;
				}
				player2ButtonStart++;
			}
			
			int player1HolesPOV2 = localGame.getHomeIndex();
			int textFieldPosition = 0;
			tfLocalOtherHoles = new JTextField[numberOfHoles];
			for(int i = 0; i < numberOfHoles; i++) {
				tfLocalOtherHoles[i] = new JTextField();
				tfLocalOtherHoles[i].setHorizontalAlignment(SwingConstants.CENTER);
				tfLocalOtherHoles[i].setText(Integer.toString(localGame.getPitArray()[player1HolesPOV2-1]));
				tfLocalOtherHoles[i].setBounds(50 + textFieldPosition, 60, 30, 30);
				tfLocalOtherHoles[i].setColumns(10);
				tfLocalOtherHoles[i].setEditable(false);
				localBoardScreen.add(tfLocalOtherHoles[i]);
				switch(numberOfHoles) {
				case 4:
					textFieldPosition += 105;
					break;
				case 5:
					textFieldPosition += 81;
					break;
				case 6:
					textFieldPosition += 65;
					break;
				case 7:
					textFieldPosition += 54;
					break;
				case 8:
					textFieldPosition += 46;
					break;
				case 9:
					textFieldPosition += 39;
					break;
				default:
					break;
				}
				player1HolesPOV2--;
			}
		
			lblPlayerNameDisplay = new JLabel(player2Name + "'s Turn");
			lblPlayerNameDisplay.setFont(new Font("Herculanum", Font.PLAIN, 30));
			lblPlayerNameDisplay.setHorizontalAlignment(SwingConstants.CENTER);
			lblPlayerNameDisplay.setBounds(64, 120, 315, 41);
			localBoardScreen.add(lblPlayerNameDisplay);
			lblPlayerNameDisplay.setForeground(Color.WHITE);
		}
		
		btnQuit = new JButton("QUIT");
		btnQuit.setBounds(369, 6, 75, 29);
		localBoardScreen.add(btnQuit);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit = true;
				localBoardScreen.setVisible(false);
				btnContinuePlaying.setVisible(true);
				localMenuScreen.setVisible(true);
			}
		});
		
		JLabel lblLocalBoardScreenPicture = new JLabel("");
		lblLocalBoardScreenPicture.setIcon(new ImageIcon(KalahGameGUI.class.getResource("/kalah/resources/KalahBoardEmpty.png")));
		lblLocalBoardScreenPicture.setBounds(0, 43, 450, 200);
		localBoardScreen.add(lblLocalBoardScreenPicture);
		
		localBoardScreen.setVisible(false);
		
	}
}