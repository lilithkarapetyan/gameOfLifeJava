import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.*;
import java.awt.event.*;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.util.*;
import java.io.*;
import java.net.*;

public class GUILife extends JFrame {
	private PatternStore store;
	private World world;
	private ArrayList<World> cachedWorlds = new ArrayList<>();
	private GamePanel gamePanel;
	/*
	 * public GUILife(PatternStore ps) { super("Game of Life"); this.store = ps;
	 * setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1024,768); }
	 */
	private World copyWorld(boolean useCloning) throws IOException, CloneNotSupportedException, PatternFormatException {
		World copy = null;
		if (!useCloning) {
			if (this.world instanceof ArrayWorld) {
				copy = new ArrayWorld((ArrayWorld) this.world);
			} else if (this.world instanceof PackedWorld) {
				copy = new PackedWorld((PackedWorld) this.world);
			}
		} else {
			copy = this.world.clone();
		}
		return copy;
	}

	public GUILife(PatternStore ps) throws Exception {
		super("Game of Life");
		store = ps;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024, 500);
		add(createPatternsPanel(), BorderLayout.WEST);
		add(createControlPanel(), BorderLayout.SOUTH);
		add(createGamePanel(), BorderLayout.CENTER);
	}

	private void addBorder(JComponent component, String title) {
		Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border tb = BorderFactory.createTitledBorder(etch, title);
		component.setBorder(tb);
	}

	private JPanel createGamePanel() {
		this.gamePanel = new GamePanel();
		try {
			this.cachedWorlds.add(new ArrayWorld(store.getPatterns()[0]));
			this.world = this.cachedWorlds.get(0);
			gamePanel.display(this.world);
		} catch (Exception ex) {

		}
		addBorder(gamePanel, "Game Panel");
		return gamePanel;
	}

	private JPanel createPatternsPanel() {
		JPanel patt = new JPanel();
		patt.setLayout(new BorderLayout());
		patt.add(new JScrollPane(new JList(this.store.getPatternsNameSorted().toArray())));
		addBorder(patt, "Patterns");
		return patt;
	}

	private JPanel createControlPanel() throws Exception {
		JPanel ctrl = new JPanel();
		addBorder(ctrl, "Controls");
		ctrl.setLayout(new GridLayout(1, 3));
		JButton back = new JButton("Back");
		JButton play = new JButton("Play");
		JButton fwd = new JButton("Forward");

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					moveBack();
					gamePanel.display(world);
				}
				catch(Exception ex){
					System.out.println("BAck button does not work " + ex.getClass());
				}
			}
		});
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					moveBack();
					gamePanel.display(world);
				}
				catch(Exception ex){
					System.out.println("Play button does not work " + ex.getClass());
				}
			}
		});
		fwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					moveForward();
					gamePanel.display(world);
				}
				catch(Exception ex){
					System.out.println("FWD button does not work " + ex.getClass());
				}
			}
		});
		ctrl.add(back);
		ctrl.add(play);
		ctrl.add(fwd);

		return ctrl;
	}

	private void moveBack() throws IOException, PatternFormatException, CloneNotSupportedException {
		int index = this.cachedWorlds.indexOf(this.world);
		if (index > 0) {
			this.world = this.cachedWorlds.get(index - 1);
		} else if (index == 0) {
			System.out.println("This is the first Generation, nothing to undo");
		}
	}

	private void moveForward() throws IOException, PatternFormatException, CloneNotSupportedException {
		int index = this.cachedWorlds.indexOf(this.world);
		if (index >= 0 && index <= (this.cachedWorlds.size() - 2)) {
			this.world = this.cachedWorlds.get(index + 1);
		} else {
			this.cachedWorlds.add(this.copyWorld(false));
			this.world = this.cachedWorlds.get(this.cachedWorlds.size() - 1);
			this.world.nextGeneration();
		}
	}

	public static void main(String args[]) throws Exception {
		// if (args.length != 1) {
		// System.out.println("Usage: java GameOfLife <path/url to store>");
		// return;
		// }
		try {
			// args[0] = args[0] ? args[0]: "https://bit.ly/2FJERFh";
			PatternStore ps = new PatternStore("https://bit.ly/2FJERFh");
			GUILife gui = new GUILife(ps);
			// .play();
			gui.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load pattern store");
		}
	}

}