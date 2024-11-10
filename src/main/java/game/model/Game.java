package game.model;

import static game.ProgramConstants.SIZE_OF_SQUARE;
import java.awt.Dimension;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
@NonNull
public class Game {
    private volatile Level level = Level.LEVEL_1;
    private volatile int score = 0;
   
    public enum Level {
        LEVEL_1(1, 250),
        LEVEL_2(2, 200),
        LEVEL_3(3, 150),
        LEVEL_4(4, 100),
        LEVEL_5(5, 75),
        LEVEL_6(6, 50);

        private final @Getter int num;
        private final @Getter int timeForTimer;
        
        Level(int num, int timeForTimer) {
            this.num = num;
            this.timeForTimer = timeForTimer;
        }

        public static Level getLevelByNumber(int number) {
            for(Level current : Level.values()) {
                if(current.num == number) return current;
            }
            throw new NoSuchElementException("Level  " + number + " doesn't consist!");
        }
    }
    
    public Game(Level level) {
        setLevel(level);
    }

    public boolean isOver() {
        ModelRepository models = ModelManager.getInstance().getModels();
        Snake snake = models.getSnake();
        return hitToWall(snake, models.getFieldSize()) 
                || hitToHimself(snake) 
                || hitToWorm(snake, models.getWorm());
    }

    private boolean hitToWall(Snake snake, Dimension fieldSize) {
        List<Integer> xCoordinatesOfSnake = snake.getXCoordinates();
        List<Integer> yCoordinatesOfSnake = snake.getYCoordinates();
        return (xCoordinatesOfSnake.getFirst() < 0) || (yCoordinatesOfSnake.getFirst() < 0)
                || (xCoordinatesOfSnake.getFirst() > (fieldSize.width - SIZE_OF_SQUARE))
                || (yCoordinatesOfSnake.getFirst() > (fieldSize.height - SIZE_OF_SQUARE));
    }

    private boolean hitToHimself(Snake snake) {
        List<Integer> xCoordinatesOfSnake = snake.getXCoordinates();
        List<Integer> yCoordinatesOfSnake = snake.getYCoordinates();
        /*Отсчёт в цикле начинается с одного, ведь 0 - это голова
        и её координаты всегда будут равны самим себе*/
        for(int i = 1; i < xCoordinatesOfSnake.size(); i++) {
            if(xCoordinatesOfSnake.getFirst().equals(xCoordinatesOfSnake.get(i))
                    && yCoordinatesOfSnake.getFirst().equals(yCoordinatesOfSnake.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hitToWorm(Snake snake, Worm worm) {
        List<Integer> xCoordinatesOfSnake = snake.getXCoordinates();
        List<Integer> yCoordinatesOfSnake = snake.getYCoordinates();
        int[] xCoordinatesOfWorm = worm.getXCoordinates();
        int[] yCoordinatesOfWorm = worm.getYCoordinates();
        for(int i = 0; i < xCoordinatesOfWorm.length; i++) {
            if(xCoordinatesOfSnake.getFirst().equals(xCoordinatesOfWorm[i])
                    && yCoordinatesOfSnake.getFirst().equals(yCoordinatesOfWorm[i])) {
                return true;
            }
        }
        return false;
    }
}