package game.view;

import game.model.Apple;
import game.model.ModelManager;
import game.model.ModelRepository;
import static game.ProgramConstants.SIZE_OF_SQUARE;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@NonNull
@RequiredArgsConstructor
@AllArgsConstructor
public class PlayingField extends JPanel {
    private final Size fieldSize;
    private @Setter boolean drawNet;
    
    public enum Size {
        MIN(SIZE_OF_SQUARE * 22, SIZE_OF_SQUARE * 22),
        MIDDLE(SIZE_OF_SQUARE * 30, SIZE_OF_SQUARE * 26),
        MAX(SIZE_OF_SQUARE * 43, SIZE_OF_SQUARE * 26);
        
        private @Getter final int x;
        private @Getter final int y;
        private @Getter final Dimension dimension;
        
        Size(int x, int y) {
            this.x = x;
            this.y = y;
            dimension = new Dimension(x, y);
        }
        
        public static Size getConstantByDimension(@NonNull Dimension dimension) {
            for(Size current : Size.values()) {
                if(current.dimension.equals(dimension)) return current;
            }
            throw new NoSuchElementException("There is'nt constant with this dimension: " + dimension);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawLine(0, fieldSize.getY(), fieldSize.getX(), fieldSize.getY());
        drawNet(g);
        ModelRepository models = ModelManager.getInstance().getModels();
        for(Apple currentApple : models.getApples()) {
            currentApple.paint(g);
        }
        models.getBonus().paint(g);
        models.getSnake().paint(g);
        models.getWorm().paint(g);
    }

    private void drawNet(Graphics g) {
        if(drawNet) {
            for(int i = SIZE_OF_SQUARE; i <= fieldSize.getY(); i += SIZE_OF_SQUARE) {
                g.drawLine(0, i, fieldSize.getX(), i);
            }
            for(int i = SIZE_OF_SQUARE; i <= fieldSize.getX(); i += SIZE_OF_SQUARE) {
                g.drawLine(i, 0, i, fieldSize.getY());
            }
        }
    }
}
