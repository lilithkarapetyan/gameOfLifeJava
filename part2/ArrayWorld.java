public class ArrayWorld extends World {

    private boolean[][] world;
	private boolean[] deadRow;
    public ArrayWorld(String serial) throws PatternFormatException {
        super(serial);
        this.world = new boolean[this.getHeight()][this.getWidth()];
		this.deadRow = new boolean[this.getWidth()];
        this.getPattern().initialise(this);
        this.manageRows(this.world);
    }

    public ArrayWorld(Pattern pattern) throws PatternFormatException {
        super(pattern);
        this.world = new boolean[this.getHeight()][this.getWidth()];
		this.deadRow = new boolean[this.getWidth()];
        this.getPattern().initialise(this);
		this.manageRows(this.world);
    }

    public ArrayWorld(ArrayWorld otherWorld) throws PatternFormatException {
        super(otherWorld.getPattern());
        this.world = new boolean[this.getHeight()][this.getWidth()];
		this.deadRow = new boolean[this.getWidth()];
        this.generation = otherWorld.getGenerationCount();
        int width = otherWorld.getWidth();
        int height = otherWorld.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.setCell(j, i, otherWorld.getCell(j, i));
            }
        }
		this.manageRows(this.world);
    }

    public ArrayWorld clone() throws CloneNotSupportedException {
        ArrayWorld cloned = null;
        try {
            cloned = new ArrayWorld(this.getPattern());
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
		this.manageRows(cloned.world);
        return cloned;
    }
	
	
	public void manageRows(boolean[][] world){
		boolean flag = false;
		for (int i = 0; i < world.length; i++) {
			flag = false;
			for (int j = 0; j < world[i].length; j++) {
				if (world[i][j])
					flag = true;
			}
			if(!flag)
				world[i] = this.deadRow;
        }
	}
	
	
    public boolean getCell(int col, int row) {
        if (row < 0 || row >= this.getHeight()) {
            return false;
        }
        if (col < 0 || col >= this.getWidth()) {
            return false;
        }
        return this.world[row][col];
    }

    public void setCell(int col, int row, boolean value) {
        if (row < 0 || row >= getHeight()) {
            return;
        }
        if (col < 0 || col >= getWidth()) {
            return;
        }
		if(world[row] == this.deadRow && value){
			world[row] = new boolean[world[row].length];
		}
		this.world[row][col] = value;
		this.manageRows(this.world);
    }

    protected void nextGenerationImpl() {
        boolean[][] newField = new boolean[this.getHeight()][this.getWidth()];

        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                newField[i][j] = this.computeCell(j, i);

            }
        }
        this.world = newField;
    }
}
