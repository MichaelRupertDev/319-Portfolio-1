package p1_319;

import com.sun.tools.javac.util.ArrayUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * COMS 319 Portfolio 1
 *
 * Deal or No Deal Game Structure Class
 *
 * A class created to keep track of which cases are open, keep track of the banker offer,
 * and keep overall track of the game. 
 *
 *
 * @author Kellen Johnson & Michael Rupert
 *
 */
public class GameStructure {

	public double bankOffer;
	public String player;
	public double[][] cases;
	public double[][] casesInOrder;
	public int casesLeft;
	public int userCase;

	final double[] values = {.01, 1, 5, 10, 25, 50, 75, 100, 200, 300, 400, 500, 750, 1000, 5000, 10000, 25000, 50000, 75000, 100000, 200000, 300000, 400000, 500000, 750000, 1000000};
//	.01 //	1 //	5 //	10 //	25
//	50 //	75 //	100 //	200 //	300
//	400 //	500 //	750 //	1000 //	5000
//	10000 // 25000 //	50000 //	75000 //	100000
//	200000 //	300000 //	400000 //	500000 //	750000
//	1000000


	/**
	 * Sets player to the name of the player
	 * Sets bank offer to 0
	 * Sets every case to true - true cases are in play
	 * cases is a two dimensional array with 2 rows and 25 columns
	 * 		[0][i] ----- case in play (1 if yes, 0 if no)
	 * 		[1][i] ----- case value (.01 to 1,000,000)
	 *
	 *
	 * @param player - name of player
	 * @param choice - players choice of which case is theres. Their case can be used to barter banker.
	 * @return a new game state of Deal or No Deal
	 */
	public GameStructure NewDoND(String player, int choice){

		this.player = player;
		casesLeft = 0;
		cases = new double[2][26];
		casesInOrder = new double[2][26];
		userCase = choice;

		Random rand = new Random();			// fills the cases with values for the game
		int index;

		for(int i = 0; i < 26; i++){
			casesInOrder[0][i] = 1;
			casesInOrder[1][i] =  values[i];
		}

		for(int z = 0; z < 26; z++){	// sets all case values to -1
			cases[1][z] = -1;
		}
		while(casesLeft < 26){			// finds all cases with -1 value and sets them to a value until all values are taken
			index = rand.nextInt(26);
			if(cases[1][index] == -1){
				cases[0][index] = 1;
				cases[1][index] = 0.0 + values[casesLeft];
				casesLeft++;
			}

		}
		return this;
	}


	/**
	 * creates a value that is a simulated offer from the banker
	 * The offer is calculated by taking every case that is in play and adding them together. Then, offer is divided
	 * by casesLeft to return the offer. The offer is essentially the average of the cases remaining.
	 *
	 */
	public double banker(){
		double offer = 0;

		for(int i = 0; i < 26; i++){
			if(cases[0][i] == 1){
				offer = offer + cases[1][i];
			}
		}

		offer = offer / casesLeft;
		if(casesLeft > 18){
			offer = offer / 6;
		}
		else if(casesLeft > 13){
			offer = offer / 5;
		}
		else if(casesLeft > 9){
			offer = offer / 4;
		}
		else if(casesLeft > 6){
			offer = offer / 3;
		}
		else if(casesLeft > 4){
			offer = offer / 2;
		}

		return offer;
	}

	// 25 - 6 = 19
	// 19 - 5 = 14
	// 14 - 4 = 10
	// 10 - 3 = 7
	// 7 - 2 = 5
	// 5 - 1 = 4
	// if no deal and 1 case left, then return caseLeft amount


	/**
	 * Generates JButtons for each of the cases in play.
	 * @param game - Current GameStructure
	 * @return Array of JButtons that represent the cases.
	 */
	public JButton[] generate_case_buttons(GameStructure game){
		JButton[] buttons = new JButton[26];
		for(int i = 0; i < 26; i++){
			if(i == game.userCase)
			{
				buttons[i] = null;
			}
			else if(this.cases[0][i] == 1)
			{
				JButton button = new JButton("" + i);
				ImageIcon img = new ImageIcon("briefcase08.gif");
				button.setVerticalTextPosition(SwingConstants.NORTH);
				button.setIcon(img);
				button.setSize(10, 10);
				buttons[i] = button;
			}
			else
			{
				buttons[i] = null;
			}
		}
		return buttons;
	}

	/**
	 *
	 * Only use on files that were created with the save feature within this class.
	 *
	 * The save files created are very specific. If any tampering happens, the Load will through errors unless still within correct format.
	 * However, tampering could lead to unfair gameplay.
	 *
	 *
	 * @param filename - the name of the file to be loaded. Must be a text file. Example: test.txt
	 * @return success or failure of the load function
	 */
	public GameStructure loadDoND(String filename){

		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(", ");
			//scanner.nextLine();
			this.player = scanner.next();
			this.casesLeft = scanner.nextInt();
			this.userCase = scanner.nextInt();

			scanner.nextLine();
			for(int i = 0; i < 2; i++){
				for(int j = 0; j < 26; j++){
					cases[i][j] = scanner.nextDouble();
				}
				if(i == 0){
					scanner.nextLine();
				}
			}

			for(int i = 0; i < 26; i++){
				casesInOrder[1][i] =  values[i];
				for(int j = 0; j < 26; j++){
					if(casesInOrder[1][i] == cases[1][j]){
						casesInOrder[0][i] = cases[0][j];
					}
				}
			}

			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something is wrong with your save file. It is either corrupt or it was inserted incorrectly");
			return null;
		}

		return this;
	}


	/**
	 * Saves the current DoND game to a text file
	 * The save will overwrite any previous version if the filename already exists within the directory
	 *
	 * @param filename - the name of the file that will be saved
	 * @return success or failure of save action.
	 */
	public boolean saveDoND(String filename){

		try {
			FileWriter f = new FileWriter(filename);

			f.write(player + ", ");
			f.write(casesLeft + ", ");
			f.write(userCase + ", ");
			f.write("\n");
			f.flush();

			for(int i = 0; i < 2; i++){
				for(int j = 0; j < 26; j++){
					f.write("" + cases[i][j] + ", ");

				}
				f.write("\n");
				f.flush();
			}

			f.close();
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * the action of a player choosing a case to open.
	 *
	 * @param x - Number of the to be chosen.
	 * @return value of the case chosen, returns 0 if the case isn't actually in play, returns -1 if the case was out of bounds
	 */
	public double chooseCase(int x){

		if(x < 0 || x > 25){
			return -1;
		}
		else if(cases[0][x] == 1 && x != userCase){
			cases[0][x] = 0;


			for(int i = 0; i < 26; i++){
				if(casesInOrder[1][i] == cases[1][x]){
					casesInOrder[0][i] = 0;
				}
			}

			casesLeft--;
			return cases[1][x];
		}
		else{
			return 0;
		}
	}
	/**
	 * Draws Jbuttons for each of the cases in play.
	 * @param game - The current GameStructure
	 * @param window - The JFrame that will hold the case JButtons
	 **/
	public static void drawCases(GameStructure game, JFrame window){
		window.getContentPane().removeAll();
		JButton[] buttons = game.generate_case_buttons(game);
		JPanel case_panel = new JPanel();
		case_panel.setBackground(Color.DARK_GRAY);
		case_panel.setLayout(new GridLayout(3, 5));
		JPanel buttons_panel = new JPanel();
		JButton save_btn = new JButton("Save");
		JButton load_btn = new JButton("Load");
		buttons_panel.setLayout(new BorderLayout());
		buttons_panel.add(save_btn, BorderLayout.EAST);
		buttons_panel.add(load_btn, BorderLayout.WEST);
		window.add(buttons_panel, BorderLayout.SOUTH);

		save_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog filename_dialog = new JDialog();
				filename_dialog.setLayout(new BorderLayout());
				filename_dialog.setSize(300, 200);
				JPanel buttons_panel = new JPanel();
				JButton save_btn = new JButton("Save");
				JButton exit_btn = new JButton("Exit");
				buttons_panel.setLayout(new BorderLayout());
				buttons_panel.add(save_btn, BorderLayout.EAST);
				buttons_panel.add(exit_btn, BorderLayout.WEST);

				filename_dialog.add(new JLabel("Please enter desired filename ending in .txt"), BorderLayout.NORTH);
				filename_dialog.add(buttons_panel, BorderLayout.SOUTH);

				JTextField name_field = new JTextField();
				filename_dialog.add(name_field, BorderLayout.CENTER);
				filename_dialog.setVisible(true);

				save_btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						game.saveDoND(name_field.getText());
						filename_dialog.setVisible(false);
					}
				});

				exit_btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						filename_dialog.setVisible(false);
					}
				});
			}
		});

		load_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog filename_dialog = new JDialog();
				filename_dialog.setLayout(new BorderLayout());
				filename_dialog.setSize(300, 200);
				JPanel buttons_panel = new JPanel();
				JButton load_btn = new JButton("Load");
				JButton exit_btn = new JButton("Exit");
				buttons_panel.setLayout(new BorderLayout());
				buttons_panel.add(load_btn, BorderLayout.EAST);
				buttons_panel.add(exit_btn, BorderLayout.WEST);

				filename_dialog.add(buttons_panel, BorderLayout.SOUTH);
				filename_dialog.add(new JLabel("Please enter desired filename ending in .txt"), BorderLayout.NORTH);

				JTextField name_field = new JTextField();
				filename_dialog.add(name_field, BorderLayout.CENTER);
				filename_dialog.setVisible(true);

				load_btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						game.loadDoND(name_field.getText());
						drawCases(game, window);
						filename_dialog.setVisible(false);
					}
				});

				exit_btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						filename_dialog.setVisible(false);
					}
				});
			}
		});
		for(JButton button: buttons){
			if(button != null) {
				case_panel.add(button);
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(game.casesLeft == 2)
						{
							JDialog d = new JDialog();
							d.setLayout(new BorderLayout());
							JLabel case_message = new JLabel();
							case_message.setText("Your case: " + game.userCase + " held a value of $" + game.cases[1][game.userCase]);
							d.add(case_message, BorderLayout.NORTH);
							JButton close_btn = new JButton("Exit");
							d.add(close_btn, BorderLayout.SOUTH);
							d.setSize(400, 400);
							d.setVisible(true);
							close_btn.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									System.exit(0);
								}
							});
						}
						else if (game.casesLeft == 19 || game.casesLeft == 14 || game.casesLeft == 10 || game.casesLeft == 7 || game.casesLeft <= 5) {
							int num = Integer.parseInt(button.getText());
							double val = game.chooseCase(num);
							JDialog d = new JDialog();
							d.setLayout(new BorderLayout());
							JLabel case_message = new JLabel();
							case_message.setText("Case: " + num + " held a value of " + val + "!");
							d.add(case_message, BorderLayout.NORTH);

							JLabel banker_msg = new JLabel();
							banker_msg.setText("The bank has a new offer for you.\n    $" + game.banker());
							d.add(banker_msg, BorderLayout.CENTER);

							JPanel buttons_panel = new JPanel();

							//Take the deal, accept Banker's Offer, end game.
							JButton deal_btn = new JButton("Deal!");
							deal_btn.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									JDialog over = new JDialog();
									over.setLayout(new BorderLayout());
									JButton close_btn = new JButton("Close");
									close_btn.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											System.exit(0);
										}
									});
									JLabel lab = new JLabel("You won " + game.banker() + "!");
									over.add(lab, BorderLayout.CENTER);
									over.add(close_btn, BorderLayout.SOUTH);
									over.setSize(200, 130);
									over.setVisible(true);
								}
							});

							//Dont take the deal, generate new cases minus case chosen on previous turn, keep playing, unless there are no cases left.

							JButton no_deal_btn = new JButton("No Deal!");
							no_deal_btn.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									d.setVisible(false);
									game.chooseCase(Integer.parseInt(button.getText()));
									window.getContentPane().removeAll();
									GameStructure.drawCases(game, window);
								}
							});

							buttons_panel.add(deal_btn, BorderLayout.WEST);
							buttons_panel.add(no_deal_btn, BorderLayout.EAST);
							d.add(buttons_panel, BorderLayout.SOUTH);
							d.pack();
							d.setVisible(true);
						} // end if cases left
						else {
							int case_num = Integer.parseInt(button.getText());
							game.chooseCase(case_num);
							JDialog d = new JDialog();
							d.setLayout(new BorderLayout());
							JLabel case_message = new JLabel();
							case_message.setText("Case " + game.userCase + " held a value of $" + game.cases[1][case_num]);
							d.add(case_message, BorderLayout.CENTER);
							JButton close_btn = new JButton("Exit");
							d.add(close_btn, BorderLayout.SOUTH);
							d.setSize(300, 200);
							d.setVisible(true);
							close_btn.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									d.setVisible(false);
								}
							});

							System.out.println("Cases Left: " + game.casesLeft);
							System.out.println("Case chosen: " + Integer.parseInt(button.getText()));
							window.getContentPane().removeAll();
							GameStructure.drawCases(game, window);
						}
					} //end actionPerformed
				}); //end case button actionListener
			}
		}
		DefaultListModel<String> model = new DefaultListModel<>();
		window.add(case_panel, BorderLayout.CENTER);
		for(int j = 0; j < 26; j++) {
			if(game.casesInOrder[0][j] == 1)
				model.addElement("" + game.casesInOrder[1][j]);
			else
				model.addElement("---------");
		}
		JPanel values_remaining_panel = new JPanel();
		JList<String> values_list = new JList<>(model);
		values_remaining_panel.add(values_list);
		window.add(values_remaining_panel, BorderLayout.EAST);
		window.pack();
	}
	public static void main(String[] args){
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.DARK_GRAY);
		window.setSize(512, 512);
		window.setLayout(new BorderLayout());
		GameStructure game = new GameStructure();
		JDialog case_dialog = new JDialog();
		case_dialog.setSize(200, 200);
		case_dialog.setLayout(new BorderLayout());
		JPanel button_panel = new JPanel();
		button_panel.setLayout(new BorderLayout());
		JButton ok_btn = new JButton("OK");
		JButton close_btn = new JButton("Exit");
		JLabel prompt = new JLabel("Enter a case number 1 - 26");
		JTextField field = new JTextField();

		case_dialog.add(prompt, BorderLayout.NORTH);
		case_dialog.add(field, BorderLayout.CENTER);

		button_panel.add(ok_btn, BorderLayout.EAST);
		button_panel.add(close_btn, BorderLayout.WEST);
		case_dialog.add(button_panel, BorderLayout.SOUTH);

		case_dialog.setVisible(true);

		ok_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.userCase = Integer.parseInt(field.getText());
				case_dialog.setVisible(false);
				drawCases(game, window);
				window.setVisible(true);
			}
		});
		close_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		game.NewDoND("Player 1", 16);
		System.out.println("play!");
	}


}
