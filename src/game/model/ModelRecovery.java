package game.model;

import java.util.Objects;

public class ModelRecovery {
    private GameProcess gameProcess;
    private Snake snake;
    private Worm worm;
    private Apple[] apples;
    private Bonus bonus;

    public ModelRecovery(GameProcess gameProcess, Snake snake, Worm worm, Apple[] apples, Bonus bonus) {
        setGameProcess(gameProcess);
        setSnake(snake);
        setWorm(worm);
        setApples(apples);
        setBonus(bonus);
    }

    public GameProcess getGameProcess() {
        return gameProcess;
    }

    public void setGameProcess(GameProcess gameProcess) {
        this.gameProcess = Objects.requireNonNull(gameProcess, "Параметр gameProcess не должен быть null!");
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = Objects.requireNonNull(snake, "Параметр snake не должен быть null!");
    }

    public Worm getWorm() {
        return worm;
    }

    public void setWorm(Worm worm) {
        this.worm = Objects.requireNonNull(worm, "Параметр worm не должен быть null!");
    }

    public Apple[] getApples() {
        return apples;
    }

    public void setApples(Apple[] apples) {
        this.apples = Objects.requireNonNull(apples, "Параметр apples не должен быть null!");
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }
}
