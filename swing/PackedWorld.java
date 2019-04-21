import java.io.IOException;

public class PackedWorld extends World {
    private long world;

    public PackedWorld(Pattern pattern) throws PatternFormatException {
        super(pattern);
        this.getPattern().initialise(this);
    }
                    

    public PackedWorld(String format) throws PatternFormatException {
        super(format);
        if (this.getWidth() * this.getHeight() > 64) {
            throw new PatternFormatException(
                    this.getHeight() + "-by-" + this.getWidth() + " is too big for a packed long");
        }

        this.getPattern().initialise(this);
    }

    public PackedWorld(PackedWorld otherWorld) throws IOException{
        super(otherWorld.getPattern());
        if (this.getWidth() * this.getHeight() > 64) {
            throw new IOException(this.getHeight() + "-by-" + this.getWidth() + " is out of the possible range; Maximum number is 8x8");
        }
        this.generation = otherWorld.getGenerationCount();
        int width = otherWorld.getWidth();
        int height = otherWorld.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.setCell(j, i, otherWorld.getCell(j, i));
            }
        }
    }

    public PackedWorld clone() throws CloneNotSupportedException{
        PackedWorld cloned = null;
        try {
            cloned = new PackedWorld(this.getPattern());
        } catch (PatternFormatException e) {
            System.out.println(e.getMessage());
        }
        cloned.generation = this.getGenerationCount();
        int width = this.getWidth();
        int height = this.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cloned.setCell(j, i, this.getCell(j, i));
            }
        }
        return cloned;
    }

    public boolean getCell(int col, int row) {
        if (row < 0 || row >= this.getHeight()) {
            return false;
        }
        if (col < 0 || col >= this.getWidth()) {
            return false;
        }
        if (((world >>> (row * this.getWidth() + col)) & 1L) == 1L)
            return true;
        else
            return false;
    }

    public void setCell(int col, int row, boolean value) {
        int pos = 0;
        if (row * this.getWidth() + col < this.getWidth() * this.getHeight() && row * this.getWidth() + col >= 0)
            pos = 1 << row * this.getWidth() + col;
        if (value)
            this.world = this.world | pos;
        else
            this.world = this.world & (~pos);

    }

    protected void nextGenerationImpl() {
        long newLong = 0L;
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                if (this.computeCell(j, i)) {
                    newLong = (1L << i * this.getWidth() + j) | newLong;
                }
            }
        }
        this.world = newLong;
    }
}