import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.border.*;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.util.*;
import java.io.*;
import java.net.*;
public class GUILife extends JFrame {
	private PatternStore store;
    private World world;
	private ArrayList<World> cachedWorlds = new ArrayList<>();
	
	/*public GUILife(PatternStore ps) {
		super("Game of Life");
		this.store = ps;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024,768);
	}*/
	
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
	
	
	
	public GUILife(PatternStore ps) {
		super("Game of Life");
		store=ps;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024,768);
		add(createPatternsPanel(),BorderLayout.WEST);
		add(createControlPanel(),BorderLayout.SOUTH);
		add(createGamePanel(),BorderLayout.CENTER);
	}
		
		
	private void addBorder(JComponent component, String title) {
		Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border tb = BorderFactory.createTitledBorder(etch,title);
		component.setBorder(tb);
	}
	
	private JPanel createGamePanel() {
		// TODO
		return new JPanel(); // temporary return
	}
	
	private JPanel createPatternsPanel() {
		JPanel patt = new JPanel();
		addBorder(patt,"Patterns");
		// TODO
		return patt;
	}
	
	private JPanel createControlPanel() {
		JPanel ctrl = new JPanel();
		addBorder(ctrl,"Controls");
		// TODO

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
        if (args.length != 1) {
            System.out.println("Usage: java GameOfLife <path/url to store>");
            return;
        }
        try {
			//args[0] = args[0] ? args[0]: "https://bit.ly/2FJERFh";
            PatternStore ps = new PatternStore("https://bit.ly/2FJERFh");
            GUILife gui = new GUILife(ps);
            //.play();
			gui.setVisible(true);
        } catch (Exception ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to load pattern store");
        }
    }

}