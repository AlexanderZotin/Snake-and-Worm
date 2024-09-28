package game.model;

import static game.ProgramConstants.ROUNDING_OF_SQUARE;
import static game.ProgramConstants.SIZE_OF_SQUARE;
import static game.ProgramConstants.SECONDS_FOR_BONUS;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class Bonus extends Apple {
    private int atWhatScoreAppear;
    private boolean appeared;
    
    public Bonus() {
        super(Apple.Type.SUPER_BONUS);
        /*
        НЕЛЬЗЯ оставлять значения по умолчанию. Ведь на поле есть
        координата (0; 0) и змейка может до неё добраться.
        Тогда она может получить бонус, который ещё не появился!
        */
        setXCoordinate(-SIZE_OF_SQUARE);
        setYCoordinate(-SIZE_OF_SQUARE);
        generateScoreToAppear(0);
    }

    public void generateScoreToAppear(int currentScore) {
        atWhatScoreAppear = 10 + currentScore + (int) (Math.random() * 10);
    }

    @Override
    public void paint(Graphics g) {
        if(appeared) {
            g.setColor(getType().getColor());
            g.fillRoundRect(getXCoordinate(), getYCoordinate(),
                    SIZE_OF_SQUARE, SIZE_OF_SQUARE, ROUNDING_OF_SQUARE, ROUNDING_OF_SQUARE);
            g.setColor(Color.RED);
            g.drawRoundRect(getXCoordinate(), getYCoordinate(),
                    SIZE_OF_SQUARE, SIZE_OF_SQUARE, ROUNDING_OF_SQUARE, ROUNDING_OF_SQUARE);
        }
    }

    public boolean shouldAppearOnThePlayingField(int score) {
        return score == atWhatScoreAppear;
    }

    public boolean isAppeared() {
        return appeared;
    }

    public void appear(Dimension fieldSize, ModelRecovery models) {
        appeared = true;
        regenerateCoordinates(fieldSize, models);
        scheduleDisappear(models.getGameProcess());
    }

    private void scheduleDisappear(GameProcess gameProcess) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int seconds = SECONDS_FOR_BONUS;

            @Override
            public void run() {
                if(!gameProcess.isPaused()) {
                    if (seconds == 0) {
                        disappear();
                        timer.cancel();
                    }
                    seconds--;
                }
            }
        }, 0, 1000);
    }

    public void disappear() {
        appeared = false;
        //Отрицательные координаты нужны, чтобы змейка не могла съесть не появившийся бонус
        setXCoordinate(-SIZE_OF_SQUARE);
        setYCoordinate(-SIZE_OF_SQUARE);
    }
}
