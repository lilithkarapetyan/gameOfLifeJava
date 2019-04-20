import java.io.*;
import java.net.*;
import java.util.*;

public class PatternStore {
    private ArrayList<Pattern> patterns = new ArrayList<>();

    public PatternStore(String source) throws Exception {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            loadFromURL(source);
        } else {
            loadFromDisk(source);
        }
    }

    public PatternStore(Reader source) throws Exception {
        load(source);
    }

    private void load(Reader r) throws Exception {

        BufferedReader bufRead = null;
        bufRead = new BufferedReader(r);

        String line;
        while ((line = bufRead.readLine()) != null) {
            try {
                patterns.add(new Pattern(line));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        bufRead.close();

    }

    private void loadFromURL(String url) throws Exception {

        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        Reader r = new InputStreamReader(conn.getInputStream());
        load(r);
    }

    private void loadFromDisk(String filename) throws Exception {
        Reader r = new FileReader(filename);
        load(r);
    }

    public ArrayList<Pattern> getPatternsNameSorted(){
        ArrayList<Pattern> patternsCopy = new ArrayList<>(this.patterns);
        Collections.sort(patternsCopy);
        return patternsCopy;
    }

    public String[] getPatternAuthors() {
        String[] authors = new String[patterns.size()];
        for (int i = 0; i < patterns.size(); i++) {
            authors[i] = patterns.get(i).getAuthor();
        }
        Arrays.sort(authors);
        return authors;
    }

    public String[] getPatternNames() {
        return getPatternAuthors();
    }
 
    public static void main(String args[]) throws Exception {
        PatternStore p = new PatternStore(args[0]);
        String[] names = p.getPatternAuthors();
        for (String s : names) {
            System.out.println(s);
        }

    }
}
