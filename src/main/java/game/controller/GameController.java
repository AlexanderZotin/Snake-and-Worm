package game.controller;

import game.Application;
import game.model.*;
import game.view.View;
import game.view.PlayingField;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lombok.NonNull;

public class GameController implements Controller {
    private View view;
    private Timer gameProcessTimer;
    
    public GameController(@NonNull View view) {
        this.view = view;
    }
    
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void levelChanged(@NonNull Game.Level newLevel) {
        getModels().getGameProcess().changeLevel(newLevel);
        scheduleGameProcess();
    }

    private ModelRepository getModels() {
        return ModelManager.getInstance().getModels();  
    }
    
    @Override
    public void applesCountChanged(int eachColorCount) {
        ModelManager manager = ModelManager.getInstance();
        ModelRepository models = manager.getModels();
        models.setApples(Apple.createApples(eachColorCount));
        view.repaint();
    }
    
    @Override
    public void fieldSizeChanged(@NonNull PlayingField.Size newSize) {
        GameProcess gameProcess = ModelManager.getInstance().getModels().getGameProcess();
        gameProcess.setPaused(true);
        view.update();
        if(!gameProcess.isStarted() || askToRestart()) {
            Application.restart(newSize);
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
        ModelRepository models = getModels();
        models.getGameProcess().setPaused(true);
        view.update();
        view.showHelp();
    }

    @Override
    public void goPressed() {
        GameProcess gameProcess = ModelManager.getInstance().getModels().getGameProcess();
        if(!gameProcess.isStarted()) {
            gameProcess.setStarted(true);
            scheduleGameProcess();
        } else {
            gameProcess.setPaused(!gameProcess.isPaused());
        }
        view.update();
    }

    private void scheduleGameProcess() {
        GameProcess process = getModels().getGameProcess();
        if(!process.isStarted()) return;
        if(gameProcessTimer != null) gameProcessTimer.cancel();
        gameProcessTimer = new Timer();
        gameProcessTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!process.isPaused()) {
                    boolean processSuccessful = process.process();
                    if(processSuccessful) {
                        view.repaint();
                        view.update();
                    } else {
                        ifLoose();
                    }
                }
            }
        }, 0, process.getTimeToProcess());
    }

    private void ifLoose() {
        Game game = ModelManager.getInstance().getModels().getGameProcess().getGame();
        int answer = JOptionPane.showConfirmDialog(null,
                "Игра окончена! \nВаш счёт в этой игре: " + game.getScore() + "." +" \nНачать ещё раз?",
                "", JOptionPane.YES_NO_OPTION);
        if(answer == JOptionPane.YES_NO_OPTION) {
            Application.restart(null);
        } 
        else System.exit(0);
    }
    
    @Override
    public void upArrowPressed() {
        Snake snake = getSnake();
        if(isGameOn() && isXHeadCoordinateCorrect(snake.getXCoordinates())
                && snake.getMotion() != Snake.Motion.DOWN) {
            snake.setMotion(Snake.Motion.UP);
        }
    }

    private boolean isXHeadCoordinateCorrect(List<Integer> xCoordinates) {
        return !xCoordinates.get(0).equals(xCoordinates.get(1));
    }

    private boolean isGameOn() {
        GameProcess gameProcess = getModels().getGameProcess();
        return gameProcess.isStarted() && !gameProcess.isPaused();
    }

    @Override
    public void downArrowPressed() {
        Snake snake = getSnake();
        if(isGameOn() && isXHeadCoordinateCorrect(snake.getXCoordinates())
                && snake.getMotion() != Snake.Motion.UP) {
            snake.setMotion(Snake.Motion.DOWN);
        }
    }

    private Snake getSnake() {
        return getModels().getSnake();
    }
    
    @Override
    public void rightArrowPressed() {
        Snake snake = getSnake();
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
        Snake snake = getSnake();
        if (isGameOn() && isYHeadCoordinateCorrect(snake.getYCoordinates())
                && snake.getMotion() != Snake.Motion.RIGHT) {
            snake.setMotion(Snake.Motion.LEFT);
        }
    }
    
    @Override
    public void dispose() {
        if(gameProcessTimer != null) gameProcessTimer.cancel();
        gameProcessTimer = null;
        view.dispose();
        view = null;
    }
}
