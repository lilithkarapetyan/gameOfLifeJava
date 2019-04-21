import java.util.Timer;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class GUILife extends JFrame {
	private PatternStore store;
	private World world;
	private ArrayList<World> cachedWorlds = new ArrayList<>();
	private GamePanel gamePanel;
	private JButton playButton;
	private Timer timer;
	private boolean playing = false;

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
		this.gamePanel.display(this.world);
		addBorder(gamePanel, "Game Panel");
		return gamePanel;
	}

	private JPanel createPatternsPanel() {
		JPanel patt = new JPanel();
		patt.setLayout(new BorderLayout());
		JList list = new JList(this.store.getPatternsNameSorted().toArray());
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (timer != null) {
					timer.cancel();
					playing = false;
					playButton.setText("Play");
					cachedWorlds.clear();
				}

				JList<Pattern> l = (JList<Pattern>) e.getSource();
				Pattern p = l.getSelectedValue();
				try {
					if (p.getWidth() * p.getHeight() > 64) {
						cachedWorlds.add(new ArrayWorld(p));
					} else {
						cachedWorlds.add(new PackedWorld(p));
					}
				} catch (Exception ex) {

				}

				world = cachedWorlds.get(cachedWorlds.size() - 1);
				gamePanel.display(world);
			}
		});
		patt.add(new JScrollPane(list));
		addBorder(patt, "Patterns");
		return patt;
	}

	private JPanel createControlPanel() throws Exception {
		JPanel ctrl = new JPanel();
		addBorder(ctrl, "Controls");
		ctrl.setLayout(new GridLayout(1, 3));
		JButton backButton = new JButton("Back");
		JButton fwdButton = new JButton("Forward");
		playButton = new JButton("Play");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (world != null) {
					try {
						moveBack();
						gamePanel.display(world);
					} catch (Exception ex) {
						System.out.println("BAck button does not work " + ex.getClass());
					}
				}
			}
		});
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (world != null) {
					runOrPause();
					gamePanel.display(world);
				}
			}
		});
		fwdButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (world != null) {
					try {
						moveForward();
						gamePanel.display(world);
					} catch (Exception ex) {
						System.out.println("FWD button does not work " + ex.getClass());
					}
				}
			}
		});
		ctrl.add(backButton);
		ctrl.add(playButton);
		ctrl.add(fwdButton);

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

	private void runOrPause() {
		if (playing) {
			timer.cancel();
			playing = false;
			playButton.setText("Play");
		} else {
			playing = true;
			playButton.setText("Stop");
			timer = new Timer(true);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					try {
						moveForward();
						gamePanel.display(world);
					} catch (Exception ex) {
					}
				}
			}, 0, 500);
		}
	}

	public static void main(String args[]) throws Exception {
		try {
			PatternStore ps = new PatternStore("https://bit.ly/2FJERFh");
			GUILife gui = new GUILife(ps);
			gui.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load pattern store");
		}
	}

}