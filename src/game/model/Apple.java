package game.model;

import static game.ProgramConstants.ROUNDING_OF_SQUARE;
import static game.ProgramConstants.SIZE_OF_SQUARE;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.Objects;

public class Apple implements Paintable {
    private int xCoordinate;
    private int yCoordinate;
    private final Type type;
    
    public enum Type {
        RED(Color.RED), 
        BLUE(Color.BLUE), 
        PEOPLE(new Color(100, 0, 200)),
        SUPER_BONUS(new Color(255, 230, 15));
        
        private final Color color;
        
        Type(Color color) {
            this.color = color;
        }
        
        public Color getColor() {
            return color;
        }
    } 
    
    public Apple(Type type) {
        this.type = Objects.requireNonNull(type, "Параметр type не может быть null!");
    }
    
    public int getXCoordinate() {
        return xCoordinate;
    }
    
    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    
    public int getYCoordinate() {
        return yCoordinate;
    }
    
    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    
    public Type getType() {
        return type;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(type.getColor());
        g.fillRoundRect(xCoordinate, yCoordinate,
                SIZE_OF_SQUARE, SIZE_OF_SQUARE, ROUNDING_OF_SQUARE, ROUNDING_OF_SQUARE);
    }

    public void regenerateCoordinates(Dimension fieldSize, ModelRecovery modelRecovery) {
        do {
            xCoordinate = 1 + (int) 
                    (Math.random() * ((fieldSize.width - SIZE_OF_SQUARE * 2.0) / SIZE_OF_SQUARE));
            xCoordinate *= SIZE_OF_SQUARE;
            if(xCoordinate == 0) xCoordinate += SIZE_OF_SQUARE;
            yCoordinate = 1 + (int) 
                    (Math.random() * ((double) (fieldSize.height - SIZE_OF_SQUARE) / SIZE_OF_SQUARE));
            yCoordinate *= SIZE_OF_SQUARE;
            if(yCoordinate == 0) yCoordinate += SIZE_OF_SQUARE;
        } while(!areCoordinatesCorrect(modelRecovery));
    }
  
    private boolean areCoordinatesCorrect(ModelRecovery modelRecovery) {
        return doNotMatchesWithMovables(modelRecovery.getSnake())
                && doNotMatchesWithWorm(modelRecovery.getWorm())
                && doNotMatchesWithOther(modelRecovery.getApples())
                && doNotMatchesWithBonus(modelRecovery.getBonus());
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

    public static Apple[] createApples(int eachColorCount, ModelRecovery currentModels, Dimension fieldSize) {
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
            apples[i].regenerateCoordinates(fieldSize, currentModels);
        }
        return apples;
    }

    @Override
    public String toString() {
        return ("Apple: " + type.name() + " [" + xCoordinate + ", " + yCoordinate + "]");
    }
    
    @Override 
    public boolean equals(Object otherObject) {
        if(this == otherObject) return true;
        if(otherObject == null) return false;
        if(this.getClass() != otherObject.getClass()) return false;
        Apple otherApple = (Apple) otherObject;
        if(otherApple.getType() != this.getType()) return false;
        return (this.xCoordinate == otherApple.getXCoordinate()
                && this.yCoordinate == otherApple.getYCoordinate());
    }
    
    @Override 
    public int hashCode() {
        return xCoordinate * yCoordinate + type.name().length();
    }
}