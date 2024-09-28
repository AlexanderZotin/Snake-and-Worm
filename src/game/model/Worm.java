package game.model;

import static game.ProgramConstants.WIDTH_OF_EYE;
import static game.ProgramConstants.HEIGHT_OF_EYE;
import static game.ProgramConstants.SIZE_OF_SQUARE;
import static game.ProgramConstants.ROUNDING_OF_SQUARE;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Arrays;

public class Worm implements Movable, Paintable {
    private final int[] xCoordinatesOfWorm;
    private final int[] yCoordinatesOfWorm;
    private final int[][] coordinatesOfEyes;
    private Motion motion = Motion.LEFT;
    private Climbing climbing = Climbing.UP;
  
    public enum Motion {LEFT, RIGHT}
    public enum Climbing {UP, DOWN}
    
    public Worm(int xHeadCoordinate, int yHeadCoordinate) {
        xCoordinatesOfWorm = new int[] {
                xHeadCoordinate, xHeadCoordinate + SIZE_OF_SQUARE,
                xHeadCoordinate + SIZE_OF_SQUARE * 2, xHeadCoordinate + SIZE_OF_SQUARE * 3};
        yCoordinatesOfWorm = new int[] {
                yHeadCoordinate,  yHeadCoordinate, yHeadCoordinate, yHeadCoordinate};
        int y = yHeadCoordinate + (int) Math.round(SIZE_OF_SQUARE / 100.0 * 30.0);
        int x1 = xHeadCoordinate + (int) Math.round(SIZE_OF_SQUARE / 100.0 * 20);
        int x2 = xHeadCoordinate + (int) Math.round(SIZE_OF_SQUARE / 100.0 * 55.0);
        coordinatesOfEyes = new int[][] {{x1, y}, {x2, y}};
    }
    
    public int[] getXCoordinates() {
        return xCoordinatesOfWorm;
    }
  
    public int[] getYCoordinates() {
        return yCoordinatesOfWorm;
    }
    
   @Override
   public void move() {
        for(int i = xCoordinatesOfWorm.length - 1; i > 0; i--) {
            xCoordinatesOfWorm[i] = xCoordinatesOfWorm[i - 1];
            yCoordinatesOfWorm[i] = yCoordinatesOfWorm[i - 1];
        } 
        switch (motion) {
            case LEFT -> {
                xCoordinatesOfWorm[0] -= SIZE_OF_SQUARE;
                coordinatesOfEyes[0][0] -= SIZE_OF_SQUARE;
                coordinatesOfEyes[1][0] -= SIZE_OF_SQUARE;
            }
            case RIGHT -> {
                xCoordinatesOfWorm[0] += SIZE_OF_SQUARE;
                coordinatesOfEyes[0][0] += SIZE_OF_SQUARE;
                coordinatesOfEyes[1][0] += SIZE_OF_SQUARE;
            }
            default -> throw new UnsupportedOperationException("Движение " + motion + " неизвестно!");
        }
    }
    
    public void changeMotionIfNeed(Dimension fieldSize) {
        if(!needChangeMotion(fieldSize)) return;
        for(int i = xCoordinatesOfWorm.length - 1; i > 0; i--) {
            xCoordinatesOfWorm[i] = xCoordinatesOfWorm[i - 1];
        }
        motion = (motion == Motion.LEFT)? Motion.RIGHT : Motion.LEFT;
        switch (climbing) {
            case UP -> {
                coordinatesOfEyes[0][1] -= SIZE_OF_SQUARE;
                coordinatesOfEyes[1][1] -= SIZE_OF_SQUARE;
                yCoordinatesOfWorm[0] -= SIZE_OF_SQUARE;
            }
            case DOWN -> {
                coordinatesOfEyes[0][1] += SIZE_OF_SQUARE;
                coordinatesOfEyes[1][1] += SIZE_OF_SQUARE;
                yCoordinatesOfWorm[0] += SIZE_OF_SQUARE;
            }
            default -> throw new UnsupportedOperationException("Сторона " + climbing + " неизвестна!");
        }
    }

    private boolean needChangeMotion(Dimension fieldSize) {
        return (xCoordinatesOfWorm[0] == 0)
                || (xCoordinatesOfWorm[0] == (fieldSize.width - SIZE_OF_SQUARE));
    }

    public void changeClimbingIfNeed(Dimension fieldSize) {
        if((yCoordinatesOfWorm[0] == 0) 
                && (xCoordinatesOfWorm[0] == (fieldSize.width - SIZE_OF_SQUARE))) {
            climbing = Climbing.DOWN;
        } else if((xCoordinatesOfWorm[0] == 0) 
                && (yCoordinatesOfWorm[0] == (fieldSize.height - SIZE_OF_SQUARE))) {
            climbing = Climbing.UP;
        }  
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(150, 0, 50));
        for(int i = 0; i < xCoordinatesOfWorm.length; i++) {
            g.fillRoundRect(xCoordinatesOfWorm[i], yCoordinatesOfWorm[i],
                    SIZE_OF_SQUARE, SIZE_OF_SQUARE, ROUNDING_OF_SQUARE, ROUNDING_OF_SQUARE);
        }
        g.setColor(Color.BLACK);
        g.fillOval(coordinatesOfEyes[0][0], coordinatesOfEyes[0][1], WIDTH_OF_EYE, HEIGHT_OF_EYE);
        g.fillOval(coordinatesOfEyes[1][0], coordinatesOfEyes[1][1], WIDTH_OF_EYE, HEIGHT_OF_EYE);
    }

    @Override
    public String toString() {
        return "Worm {x = " + Arrays.toString(xCoordinatesOfWorm)
                + "; y = " + Arrays.toString(yCoordinatesOfWorm)
                + "; motion = " + motion.name() + "; climbing = " + climbing.name() + "}";
    }
}