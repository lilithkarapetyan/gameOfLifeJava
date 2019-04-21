import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private World world = null;

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // Paint the background white

        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (this.world != null) {
            int height =  this.getHeight()/(this.world.getHeight() + 1);
            int width =  this.getWidth()/this.world.getWidth();
            int size = Math.min(height, width);
            int i,j;
            for (i = 0; i < this.world.getHeight(); i++) {
                for (j = 0; j < this.world.getWidth(); j++) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(j * size , i * size, size, size);
                    if (this.world.getCell(j, i)) {
                        g.setColor(Color.BLACK);
                        g.fillRect(j * size + 1, i * size + 1, size - 2, size-2);
                    }
                }
            }
            g.setColor(Color.BLACK);
            g.drawString("Generation - " + this.world.getGenerationCount(), size , (i+1)*size);
        }
        // Sample drawing statements
        /*g.fillRect(140, 140, 30, 30);
        g.fillRect(260, 140, 30, 30);
        g.setColor(Color.BLACK);
        g.drawLine(150, 300, 280, 300);
        g.drawString("@@@", 135, 120);
        g.drawString("@@@", 255, 120);
        */
    }

    public void display(World w) {
        this.world = w;
        repaint();
    }
}