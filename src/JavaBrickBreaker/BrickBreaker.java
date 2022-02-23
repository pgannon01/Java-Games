package JavaBrickBreaker;

import javax.swing.JFrame;

public class BrickBreaker {

	public static void main(String[] args) {
		JFrame obj = new JFrame();
		Gameplay gameplay = new Gameplay();
		
		// Setup JFrame window boundaries and settings
		obj.setBounds(10, 10, 710, 610);
		obj.setTitle("Breakout Ball");
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(gameplay);

	}

}
