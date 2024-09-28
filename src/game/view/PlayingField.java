package game.view;

import game.Main;
import game.model.Apple;
import game.model.ModelRecovery;
import static game.ProgramConstants.SIZE_OF_SQUARE;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Objects;

public class PlayingField extends JPanel {
    private final Size size;
    private boolean drawNet;
    
    public enum Size {
        MIN(SIZE_OF_SQUARE * 22, SIZE_OF_SQUARE * 22),
        MIDDLE(SIZE_OF_SQUARE * 30, SIZE_OF_SQUARE * 26),
        MAX(SIZE_OF_SQUARE * 43, SIZE_OF_SQUARE * 26);
        
        private final int x;
        private final int y;
        
        Size(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
    }

    public PlayingField(Size size, boolean drawNet) {
        this.size = Objects.requireNonNull(size, "Параметр size не может быть null!");
        this.drawNet = drawNet;
    }

    /*
    Название size() недопустимо, потому что в классе java.awt.Component
    есть методы size() и getSize(), возвращающие java.awt.Dimension. А этот класс
    унаследован от JPanel, JPanel как раз унаследован от Component...
    */
    public Size sizeOfField() { return size; } 
    
    public boolean drawNet() { return drawNet; }
    public void setDrawNet(boolean drawNet) { this.drawNet = drawNet; }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawLine(0, size.getY(), size.getX(), size.getY());
        drawNet(g);
        ModelRecovery modelRecovery = Main.getController().getModels();
        for(Apple currentApple : modelRecovery.getApples()) {
            currentApple.paint(g);
        }
        modelRecovery.getBonus().paint(g);
        modelRecovery.getSnake().paint(g);
        modelRecovery.getWorm().paint(g);
    }

    private void drawNet(Graphics g) {
        if(drawNet) {
            for(int i = SIZE_OF_SQUARE; i <= size.getY(); i += SIZE_OF_SQUARE) {
                g.drawLine(0, i, size.getX(), i);
            }
            for(int i = SIZE_OF_SQUARE; i <= size.getX(); i += SIZE_OF_SQUARE) {
                g.drawLine(i, 0, i, size.getY());
            }
        }
    }
}
