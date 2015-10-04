package p1_319;

import javax.swing.*;

public class test {

	public static void main(String[] args){
		GameStructure x = new GameStructure();
		x.NewDoND("Kellen", 19);

		JButton[] buttons = x.generate_case_buttons(x);

		x.saveDoND("test.txt");
		x.loadDoND("test.txt");

		for(int i = 0; i < 26; i++){
			System.out.println(x.cases[1][i] + " " + i + " " + x.cases[0][i]);
		}
		
		System.out.println(x.banker());
	}
	
	
}
