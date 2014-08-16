package ui;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	public Frame(String s) {
		super(s);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	public void setLookAndFeel(String s) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (s.equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}
	}

}
