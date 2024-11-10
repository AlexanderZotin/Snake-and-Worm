package game.model;

import java.awt.Dimension;

import lombok.Data;
import lombok.NonNull;

@Data
@NonNull
public class GameProcess {
    private final Game game;
    private volatile boolean started;
    private volatile boolean paused;
    
    public void changeLevel(Game.Level level) {
        game.setLevel(level);
    }

    public long getTimeToProcess() {
        return game.getLevel().getTimeForTimer();
    }

    public boolean process() {
        ModelManager manager = ModelManager.getInstance();
        ModelRepository models = manager.getModels();
        workWithBonus(models);
        workWithSnake(models);
        if(game.isOver()) {
            return false;
        }
        workWithWorm(models);
        return true;
    }

    private void workWithBonus(ModelRepository models) {
        Bonus bonus = models.getBonus();
        if(bonus.shouldAppearOnThePlayingField(game.getScore())) {
            bonus.appear();
            bonus.generateScoreToAppear(models.getGameProcess().getGame().getScore());
        }
    }

    private void workWithSnake(ModelRepository models) {
        Snake snake = models.getSnake();
        snake.tryToEat().ifPresent(apple -> {
                    apple.regenerateCoordinates();
                    game.setScore(snake.getXCoordinates().size() - 4);
                });
        snake.move();
    }

    private void workWithWorm(ModelRepository models) {
        Worm worm = models.getWorm();
        Dimension fieldSize = models.getFieldSize();
        worm.changeClimbingIfNeed(fieldSize);
        worm.changeMotionIfNeed(fieldSize);
        worm.move();
    }
}