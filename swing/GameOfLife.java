import java.util.*;
import java.io.*;
import java.net.*;

public class GameOfLife {
    private PatternStore store;
    private World world;
    private ArrayList<World> cachedWorlds = new ArrayList<>();

    public GameOfLife(PatternStore store) {
        this.store = store;
    }

    public GameOfLife(World w) {
        this.world = w;
    }

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

    public void print() {

        System.out.println("- " + world.getGenerationCount());

        for (int i = 0; i < world.getHeight(); i++) {
            for (int j = 0; j < world.getWidth(); j++) {

                if (world.getCell(j, i)) {
                    System.out.print(".");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public void play() throws IOException, PatternFormatException, CloneNotSupportedException {
        String response = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please select a pattern to play (l to list):");
        int index;
        while (!response.equals("q")) {
            response = in.readLine();
            System.out.println(response);
            if (response.equals("f")) {
                if (this.world == null) {
                    System.out.println("Please select a pattern to play (l to list):");

                } else {
                    index = this.cachedWorlds.indexOf(this.world);
                    if (index >= 0 && index <= (this.cachedWorlds.size() - 2)) {
                        this.world = this.cachedWorlds.get(index + 1);
                    } else {
                        this.cachedWorlds.add(this.copyWorld(false));
                        this.world = this.cachedWorlds.get(this.cachedWorlds.size() - 1);
                        this.world.nextGeneration();
                    }
                    this.print();
                }
            } else if (response.equals("l")) {
                ArrayList<Pattern> names = (ArrayList<Pattern>) store.getPatternsNameSorted();
                int i = 0;
                for (Pattern p : names) {
                    if (p == null)
                        continue;
                    System.out.println(i + " " + p.getName() + "  (" + p.getAuthor() + ")");
                    i++;
                }
            } else if (response.startsWith("p")) {
                ArrayList<Pattern> patterns = store.getPatternsNameSorted();
                Integer number = Integer.parseInt(response.split(" ")[1]);
                Pattern pattern = patterns.get(number);

                if (pattern.getWidth() * pattern.getHeight() > 64) {
                    this.cachedWorlds.add(new ArrayWorld(pattern));
                } else {
                    this.cachedWorlds.add(new PackedWorld(pattern));
                }

                this.world = this.cachedWorlds.get(this.cachedWorlds.size() - 1);
                this.print();
            } else if (response.startsWith("b")) {
                if (this.world == null) {
                    System.out.println("Please select a pattern to play (l to list):");
                } else {
                    index = this.cachedWorlds.indexOf(this.world);
                    if (index > 0) {
                        this.world = this.cachedWorlds.get(index - 1);
                    } else if (index == 0) {
                        System.out.println("This is the first Generation, nothing to undo");
                    }
                    this.print();
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java GameOfLife <path/url to store>");
            return;
        }
        try {
            PatternStore ps = new PatternStore(args[0]);
            GameOfLife gol = new GameOfLife(ps);
            gol.play();
        } catch (Exception ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to load pattern store");
        }
    }
}