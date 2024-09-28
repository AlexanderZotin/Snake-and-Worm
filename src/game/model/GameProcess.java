package game.model;

import game.Main;
import game.controller.Controller;

import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.util.Objects;
import java.util.Optional;

public class GameProcess {
    private final Game game;
    private boolean isStarted;
    private boolean paused;

    public GameProcess(Game game) {
        this.game = Objects.requireNonNull(game, "Параметр game не может быть null!");
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStartStatus(boolean started) {
        isStarted = started;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPauseStatus(boolean paused) {
        this.paused = paused;
    }

    public Game getGame() {
        return game;
    }

    public void changeLevel(Game.Level level) {
        game.setLevel(level);
    }

    public long getTimeToProcess() {
        return game.getLevel().getTimeForTimer();
    }

    public Optional<ModelRecovery> process(Dimension fieldSize, ModelRecovery models) {
        workWithBonus(fieldSize, models);
        workWithSnake(fieldSize, models);
        if(game.isOver(fieldSize, models)) {
            ifLoose();
            return Optional.empty();
        }
        workWithWorm(fieldSize, models);
        return Optional.of(models);
    }

    private void workWithBonus(Dimension fieldSize, ModelRecovery models) {
        Bonus bonus = models.getBonus();
        if(bonus.shouldAppearOnThePlayingField(game.getScore())) {
            bonus.appear(fieldSize, models);
            bonus.generateScoreToAppear(models.getGameProcess().getGame().getScore());
        }
        models.setBonus(bonus);
    }

    private void workWithSnake(Dimension fieldSize, ModelRecovery models) {
        Snake snake = models.getSnake();
        snake.tryToEat(models.getApples(), models.getBonus())
                .ifPresent(apple -> {
                    apple.regenerateCoordinates(fieldSize, models);
                    game.setScore(models.getSnake().getXCoordinates().size() - 4);
                });
        snake.move();
        models.setSnake(snake);
    }

    private void ifLoose() {
        int answer = JOptionPane.showConfirmDialog(null,
                "Игра окончена! \nВаш счёт в этой игре: " + game.getScore() + "." +" \nНачать ещё раз?",
                "", JOptionPane.YES_NO_OPTION);
        if(answer == JOptionPane.YES_NO_OPTION) {
            Controller controller = Main.getController();
            Main.restart(controller.getView(), controller.getView().getPlayingField().sizeOfField(),
                    controller.getModels());
        }
        else System.exit(0);
    }

    private void workWithWorm(Dimension fieldSize, ModelRecovery modelRecovery) {
        Worm worm = modelRecovery.getWorm();
        worm.changeClimbingIfNeed(fieldSize);
        worm.changeMotionIfNeed(fieldSize);
        worm.move();
        modelRecovery.setWorm(worm);
    }
}