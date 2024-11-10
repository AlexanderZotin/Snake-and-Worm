package game.model;

import static game.ProgramConstants.ROUNDING_OF_SQUARE;
import static game.ProgramConstants.SIZE_OF_SQUARE;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
@NonNull
public class Apple implements Paintable {
    private volatile int xCoordinate;
    private volatile int yCoordinate;
    private final Type type;
    
    public enum Type {
        RED(Color.RED), 
        BLUE(Color.BLUE), 
        PEOPLE(new Color(100, 0, 200)),
        SUPER_BONUS(new Color(255, 230, 15));
        
        private final @Getter Color color;
        
        Type(Color color) {
            this.color = color;
        }
    } 

    @Override
    public void paint(Graphics g) {
        g.setColor(type.getColor());
        g.fillRoundRect(xCoordinate, yCoordinate,
                SIZE_OF_SQUARE, SIZE_OF_SQUARE, ROUNDING_OF_SQUARE, ROUNDING_OF_SQUARE);
    }

    public void regenerateCoordinates() {
        Dimension fieldSize = ModelManager.getInstance().getModels().getFieldSize();
        do {
            xCoordinate = 1 + (int) 
                    (Math.random() * ((fieldSize.width - SIZE_OF_SQUARE * 2.0) / SIZE_OF_SQUARE));
            xCoordinate *= SIZE_OF_SQUARE;
            if(xCoordinate == 0) xCoordinate += SIZE_OF_SQUARE;
            yCoordinate = 1 + (int) 
                    (Math.random() * ((double) (fieldSize.height - SIZE_OF_SQUARE) / SIZE_OF_SQUARE));
            yCoordinate *= SIZE_OF_SQUARE;
            if(yCoordinate == 0) yCoordinate += SIZE_OF_SQUARE;
        } while(!areCoordinatesCorrect());
    }
  
    private boolean areCoordinatesCorrect() {
        ModelRepository models = ModelManager.getInstance().getModels();
        return doNotMatchesWithMovables(models.getSnake())
                && doNotMatchesWithWorm(models.getWorm())
                && doNotMatchesWithOther(models.getApples())
                && doNotMatchesWithBonus(models.getBonus());
    }

    private boolean doNotMatchesWithMovables(Snake snake) {
        List<Integer> xCoordinatesOfSnake = snake.getXCoordinates();
        List<Integer> yCoordinatesOfSnake = snake.getYCoordinates();
        for(int i = 0; i < xCoordinatesOfSnake.size(); i++) {
            if(xCoordinatesOfSnake.get(i) == xCoordinate
                    && yCoordinatesOfSnake.get(i) == yCoordinate) {
                return false;
            }
        }
        return true;
    }

    private boolean doNotMatchesWithWorm(Worm worm) {
        int[] xCoordinatesOfWorm = worm.getXCoordinates();
        int[] yCoordinatesOfWorm = worm.getYCoordinates();
        for(int i = 0; i < xCoordinatesOfWorm.length; i++) {
            if(xCoordinatesOfWorm[i] == xCoordinate
                    && yCoordinatesOfWorm[i] == yCoordinate) {
                return false;
            }
        }
        return true;
    }

    private boolean doNotMatchesWithOther(Apple[] allApples) {
        for(Apple currentApple : allApples) {
            if(currentApple == null || currentApple == this) continue;
            if(this.xCoordinate == currentApple.xCoordinate 
                    && this.yCoordinate == currentApple.yCoordinate) {
                return false;    
            }
        }
        return true;
    }

    private boolean doNotMatchesWithBonus(Bonus bonus) {
        if(bonus != null && this != bonus && bonus.isAppeared()) {
            return this.xCoordinate != bonus.getXCoordinate()
                    || this.yCoordinate != bonus.getYCoordinate();
        }
        return true;
    }

    public static Apple[] createApples(int eachColorCount) {
        if(eachColorCount <= 0) {
            throw new IllegalArgumentException("Некорректное значение параметра eachColorCount:  " + eachColorCount);
        }
        Apple[] apples = new Apple[eachColorCount * 3];
        for(int i = 0; i < apples.length; i++) {
            Apple.Type currentType;
            if(i < eachColorCount)  currentType = Type.RED;
            else if(i < eachColorCount * 2) currentType = Type.BLUE;
            else currentType = Type.PEOPLE;
            apples[i] = new Apple(currentType);
            apples[i].regenerateCoordinates();
        }
        return apples;
    }
}