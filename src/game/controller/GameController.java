package game.controller;

import game.Main;
import game.model.*;
import game.view.View;
import game.view.PlayingField;
import static game.ProgramConstants.HELP_TEXT;

import java.awt.Dimension;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class GameController implements Controller {
    private final View view;
    private ModelRecovery models;
    private Timer gameProcessTimer;

    public GameController(View view, ModelRecovery models) {
        this.view = Objects.requireNonNull(view, "Параметр view не может быть null!");
        this.models = Objects.requireNonNull(models, "Параметр models не должен быть null!");
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public ModelRecovery getModels() {
        return models;
    }

    @Override
    public void levelChanged(Game.Level newLevel) {
        models.getGameProcess().changeLevel(newLevel);
        scheduleGameProcess();
    }

    @Override
    public void applesCountChanged(int eachColorCount) {
        Dimension fieldSize = getFieldSize();
        models.setApples(Apple.createApples(eachColorCount, models, fieldSize));
        view.repaint();
    }

    private Dimension getFieldSize() {
        PlayingField.Size size = view.getPlayingField().sizeOfField();
        return new Dimension(size.getX(), size.getY());
    }

    @Override
    public void fieldSizeChanged(PlayingField.Size newSize) {
        GameProcess gameProcess = models.getGameProcess();
        gameProcess.setPauseStatus(true);
        view.update(models);
        if(!gameProcess.isStarted() || askToRestart()) {
            Main.restart(view, newSize, models);
        }
    }

    private boolean askToRestart() {
        int answer = JOptionPane.showConfirmDialog(null,
                "Чтобы изменить размер поля, нужно начать игру заново.\nВыполнить действие?",
                "Изменение размера поля", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return answer == JOptionPane.YES_OPTION;
    }

    @Override
    public void helpRequested() {
        models.getGameProcess().setPauseStatus(true);
        view.update(models);
        JOptionPane.showMessageDialog(null, HELP_TEXT, "Как играть", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void goPressed() {
        GameProcess gameProcess = models.getGameProcess();
        if(!gameProcess.isStarted()) {
            gameProcess.setStartStatus(true);
            scheduleGameProcess();
        } else {
            gameProcess.setPauseStatus(!gameProcess.isPaused());
        }
        view.update(models);
    }

    private void scheduleGameProcess() {
        GameProcess process = models.getGameProcess();
        if(!process.isStarted()) return;
        if(gameProcessTimer != null) gameProcessTimer.cancel();
        gameProcessTimer = new Timer();
        Dimension fieldSize = getFieldSize();
        gameProcessTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(process.isPaused()) return;
                Optional<ModelRecovery> updatedModels = process.process(fieldSize, models);
                updatedModels.ifPresentOrElse(updated -> {
                            models = updated;
                            view.repaint();
                            view.update(models);
                        }, () -> gameProcessTimer.cancel());
            }
        }, 0, process.getTimeToProcess());
    }

    @Override
    public void upArrowPressed() {
        Snake snake = models.getSnake();
        if(isGameOn() && isXHeadCoordinateCorrect(snake.getXCoordinates())
                && snake.getMotion() != Snake.Motion.DOWN) {
            snake.setMotion(Snake.Motion.UP);
        }
    }

    private boolean isXHeadCoordinateCorrect(List<Integer> xCoordinates) {
        return !xCoordinates.get(0).equals(xCoordinates.get(1));
    }

    private boolean isGameOn() {
        GameProcess gameProcess = models.getGameProcess();
        return gameProcess.isStarted() && !gameProcess.isPaused();
    }

    @Override
    public void downArrowPressed() {
        Snake snake = models.getSnake();
        if(isGameOn() && isXHeadCoordinateCorrect(snake.getXCoordinates())
                && snake.getMotion() != Snake.Motion.UP) {
            snake.setMotion(Snake.Motion.DOWN);
        }
    }

    @Override
    public void rightArrowPressed() {
        Snake snake = models.getSnake();
        if (isGameOn() && isYHeadCoordinateCorrect(snake.getYCoordinates())
                && snake.getMotion() != Snake.Motion.LEFT) {
            snake.setMotion(Snake.Motion.RIGHT);
        }
    }

    private boolean isYHeadCoordinateCorrect(List<Integer> yCoordinates) {
        return !yCoordinates.get(0).equals(yCoordinates.get(1));
    }

    @Override
    public void leftArrowPressed() {
        Snake snake = models.getSnake();
        if (isGameOn() && isYHeadCoordinateCorrect(snake.getYCoordinates())
                && snake.getMotion() != Snake.Motion.RIGHT) {
            snake.setMotion(Snake.Motion.LEFT);
        }
    }
}
