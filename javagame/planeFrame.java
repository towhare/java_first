package javagame;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class planeFrame extends JFrame{
	static JLabel Score = new JLabel("����:");
	public planeFrame(){
		setTitle("���ķɻ���ս");
		GamePanel panel = new GamePanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);
		pack();
		panel.add(Score);
	}
	public static void main(String[] args) {
		planeFrame e1 = new planeFrame();
		e1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		e1.setVisible(true);
	}

}
