package game.model;

import static game.ProgramConstants.SIZE_OF_SQUARE;
import static game.ProgramConstants.ROUNDING_OF_SQUARE;
import static game.ProgramConstants.WIDTH_OF_EYE;
import static game.ProgramConstants.HEIGHT_OF_EYE;
import static game.ProgramConstants.POINTS_FOR_BONUS;

import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class Snake implements Movable, Paintable {
    private final List<Integer> xCoordinates = new ArrayList<>
            (Arrays.asList(SIZE_OF_SQUARE * 3, SIZE_OF_SQUARE * 2, SIZE_OF_SQUARE, 0));
    private final List<Integer> yCoordinates  = new ArrayList<>(Arrays.asList(0, 0, 0, 0));
    private final int[][] coordinatesOfEyes;
    private Motion motion = Motion.RIGHT;

    public enum Motion {UP, DOWN, LEFT, RIGHT}
    
    public Snake() {
        int x1 = SIZE_OF_SQUARE * 3 + (int) Math.round(SIZE_OF_SQUARE / 5.0);
        int x2 = SIZE_OF_SQUARE * 3 + (int) Math.round(SIZE_OF_SQUARE / 100.0 * 55.0);
        int y = (int) Math.round(SIZE_OF_SQUARE / 100.0 * 30.0);
        coordinatesOfEyes = new int[][]{{x1, y}, {x2, y}};
    }

    public List<Integer> getXCoordinates() {
        return xCoordinates;
    }
  
    public List<Integer> getYCoordinates() {
        return yCoordinates;
    }

    public Motion getMotion() {
        return motion;
    }

    public void setMotion(Motion motion) {
        this.motion = Objects.requireNonNull(motion,  "Параметр motion не может быть null!");
    }
    
    @Override
    public void move() {
        for(int i = xCoordinates.size() - 1; i > 0; i--) {
            yCoordinates.set(i, yCoordinates.get(i - 1));
            xCoordinates.set(i, xCoordinates.get(i - 1));
        }
        switch(motion) {
            case RIGHT -> {
                xCoordinates.set(0, xCoordinates.getFirst() + SIZE_OF_SQUARE);
                coordinatesOfEyes[0][0] += SIZE_OF_SQUARE;
                coordinatesOfEyes[1][0] += SIZE_OF_SQUARE;
            }
            case LEFT -> {
                xCoordinates.set(0, xCoordinates.getFirst() - SIZE_OF_SQUARE);
                coordinatesOfEyes[0][0] -= SIZE_OF_SQUARE;
                coordinatesOfEyes[1][0] -= SIZE_OF_SQUARE;
            }
            case UP -> {
                yCoordinates.set(0, yCoordinates.getFirst() - SIZE_OF_SQUARE);
                coordinatesOfEyes[0][1] -= SIZE_OF_SQUARE;
                coordinatesOfEyes[1][1] -= SIZE_OF_SQUARE;
            }
            case DOWN -> {
                yCoordinates.set(0, yCoordinates.getFirst() + SIZE_OF_SQUARE);
                coordinatesOfEyes[0][1] += SIZE_OF_SQUARE;
                coordinatesOfEyes[1][1] += SIZE_OF_SQUARE;
            }
            default -> throw new UnsupportedOperationException("Движение " + motion + " неизвестно!");
        }
    }

    @Override
    public  void paint(Graphics g) {
        Objects.requireNonNull(g);
        g.setColor(Color.ORANGE);
        for(int i = 0; i < xCoordinates.size(); i++) {
            g.fillRoundRect(xCoordinates.get(i), yCoordinates.get(i),
                    SIZE_OF_SQUARE, SIZE_OF_SQUARE, ROUNDING_OF_SQUARE, ROUNDING_OF_SQUARE);
        }
        g.setColor(Color.BLACK);
        g.fillOval(coordinatesOfEyes[0][0], coordinatesOfEyes[0][1], WIDTH_OF_EYE, HEIGHT_OF_EYE);
        g.fillOval(coordinatesOfEyes[1][0], coordinatesOfEyes[1][1], WIDTH_OF_EYE, HEIGHT_OF_EYE);
    }

    public Optional<Apple> tryToEat(Apple[] allApples, Bonus bonus) {
        Objects.requireNonNull(allApples, "Параметр allApples не может быть null!");
        Objects.requireNonNull(bonus, "Параметр bonus не может быть null!");
        for (Apple current : allApples) {
            if(current.getXCoordinate() == xCoordinates.getFirst()
                    && current.getYCoordinate() == yCoordinates.getFirst()) {
                eat(current);
                return Optional.of(current);
            }
        }
        if (xCoordinates.getFirst() == bonus.getXCoordinate()
                && yCoordinates.getFirst() == bonus.getYCoordinate()) {
            eat(bonus);
            return Optional.of(bonus);
        }
        return Optional.empty();
    }

    private void eat(Apple apple) {
        Objects.requireNonNull(apple, "Параметр apple не может быть null");
        int currentScore = xCoordinates.size() - 4;
        switch(apple.getType()) {
            case RED -> addOne();
            case BLUE -> {
                //Если счёт 0, размер змейки не меняется
                if(currentScore != 0) {
                    removeOne();
                }
            }
            case PEOPLE -> {
                //Исходный размер змейки 4, змейка не может быть меньше
                int toRemove = Math.min(currentScore, 5);
                removeMany(toRemove);
            }
            case SUPER_BONUS -> {
                Bonus bonus = (Bonus) apple;
                bonus.disappear();
                bonus.generateScoreToAppear(currentScore + POINTS_FOR_BONUS);
                addBonusPoints();
            }
            default -> throw new UnsupportedOperationException("Тип яблока " + apple.getType() + " неизвестен!");
        }
    }
    
    private void addOne() {
        xCoordinates.add(xCoordinates.getLast());
        yCoordinates.add(yCoordinates.getLast());
    }

    private void addBonusPoints() {
        for(int i = 0; i < POINTS_FOR_BONUS; i++) {
            xCoordinates.add(xCoordinates.getLast());
            yCoordinates.add(yCoordinates.getLast());
        }
    }

    private void removeOne() {
        xCoordinates.removeLast();
        yCoordinates.removeLast();
    }

    private void removeMany(int toRemove) {
        for(int i = 0; i < toRemove; i++) {
            removeOne();
        }
    }

    @Override
    public String toString() {
        return "Snake {x = " + xCoordinates + "; y = " + yCoordinates
                + "; motion = " + motion.name() + "}";
    }
}