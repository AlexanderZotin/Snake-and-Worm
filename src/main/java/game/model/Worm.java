package game.model;

import static game.ProgramConstants.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Worm implements Movable, Paintable {
    private volatile int[] xCoordinates;
    private volatile int[] yCoordinates;
    private volatile int[][] coordinatesOfEyes;
    private volatile Motion motion = Motion.LEFT;
    private volatile Climbing climbing = Climbing.UP;
  
    public enum Motion {LEFT, RIGHT}
    public enum Climbing {UP, DOWN}
    
    public Worm(int xHeadCoordinate, int yHeadCoordinate) {
        xCoordinates = new int[] {
                xHeadCoordinate, xHeadCoordinate + SIZE_OF_SQUARE,
                xHeadCoordinate + SIZE_OF_SQUARE * 2, xHeadCoordinate + SIZE_OF_SQUARE * 3};
        yCoordinates = new int[] {
                yHeadCoordinate,  yHeadCoordinate, yHeadCoordinate, yHeadCoordinate};
        int y = yHeadCoordinate + (int) Math.round(SIZE_OF_SQUARE / 100.0 * 30.0);
        int x1 = xHeadCoordinate + (int) Math.round(SIZE_OF_SQUARE / 100.0 * 20);
        int x2 = xHeadCoordinate + (int) Math.round(SIZE_OF_SQUARE / 100.0 * 55.0);
        coordinatesOfEyes = new int[][] {{x1, y}, {x2, y}};
    }
    
   @Override
   public void move() {
        for(int i = xCoordinates.length - 1; i > 0; i--) {
            xCoordinates[i] = xCoordinates[i - 1];
            yCoordinates[i] = yCoordinates[i - 1];
        } 
        switch (motion) {
            case LEFT -> {
                xCoordinates[0] -= SIZE_OF_SQUARE;
                coordinatesOfEyes[0][0] -= SIZE_OF_SQUARE;
                coordinatesOfEyes[1][0] -= SIZE_OF_SQUARE;
            }
            case RIGHT -> {
                xCoordinates[0] += SIZE_OF_SQUARE;
                coordinatesOfEyes[0][0] += SIZE_OF_SQUARE;
                coordinatesOfEyes[1][0] += SIZE_OF_SQUARE;
            }
            default -> throw new UnsupportedOperationException("Motion " + motion + " is unknown!");
        }
    }
    
    public void changeMotionIfNeed(@NonNull Dimension fieldSize) {
        if(!needChangeMotion(fieldSize)) return;
        for(int i = xCoordinates.length - 1; i > 0; i--) {
            xCoordinates[i] = xCoordinates[i - 1];
        }
        motion = (motion == Motion.LEFT)? Motion.RIGHT : Motion.LEFT;
        switch (climbing) {
            case UP -> {
                coordinatesOfEyes[0][1] -= SIZE_OF_SQUARE;
                coordinatesOfEyes[1][1] -= SIZE_OF_SQUARE;
                yCoordinates[0] -= SIZE_OF_SQUARE;
            }
            case DOWN -> {
                coordinatesOfEyes[0][1] += SIZE_OF_SQUARE;
                coordinatesOfEyes[1][1] += SIZE_OF_SQUARE;
                yCoordinates[0] += SIZE_OF_SQUARE;
            }
            default -> throw new UnsupportedOperationException("Climbing " + climbing + " is unknown!");
        }
    }

    private boolean needChangeMotion(Dimension fieldSize) {
        return (xCoordinates[0] == 0)
                || (xCoordinates[0] == (fieldSize.width - SIZE_OF_SQUARE));
    }

    public void changeClimbingIfNeed(@NonNull Dimension fieldSize) {
        if((yCoordinates[0] == 0) 
                && (xCoordinates[0] == (fieldSize.width - SIZE_OF_SQUARE))) {
            climbing = Climbing.DOWN;
        } else if((xCoordinates[0] == 0) 
                && (yCoordinates[0] == (fieldSize.height - SIZE_OF_SQUARE))) {
            climbing = Climbing.UP;
        }  
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(150, 0, 50));
        for(int i = 0; i < xCoordinates.length; i++) {
            g.fillRoundRect(xCoordinates[i], yCoordinates[i],
                    SIZE_OF_SQUARE, SIZE_OF_SQUARE, ROUNDING_OF_SQUARE, ROUNDING_OF_SQUARE);
        }
        g.setColor(Color.BLACK);
        g.fillOval(coordinatesOfEyes[0][0], coordinatesOfEyes[0][1], WIDTH_OF_EYE, HEIGHT_OF_EYE);
        g.fillOval(coordinatesOfEyes[1][0], coordinatesOfEyes[1][1], WIDTH_OF_EYE, HEIGHT_OF_EYE);
    }
}