package game;

import game.model.ModelRecovery;
import game.model.Snake;
import game.model.Worm;
import game.model.Apple;
import game.model.Bonus;
import game.model.Game;
import game.model.GameProcess;
import game.view.Listener;
import game.view.View;
import game.view.Window;
import game.view.PlayingField;
import game.controller.*;
import static game.ProgramConstants.DRAW_NET_BY_DEFAULT;
import static game.ProgramConstants.SIZE_OF_SQUARE;

import javax.swing.UIManager;
import java.awt.Dimension;

public class Main {
    private static Controller controller;
    
    public static void main(String [] args) {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.okButtonText", "Понятно");
        start(PlayingField.Size.MIN, DRAW_NET_BY_DEFAULT,
                defaultModels(new Dimension(PlayingField.Size.MIN.getX(), PlayingField.Size.MIN.getY())));
    }

    public static void start(PlayingField.Size size, boolean drawNet, ModelRecovery models) {
        PlayingField playingField = new PlayingField(size, drawNet);
        Window view = new Window(playingField);
        view.update(models);
        controller = new GameController(view, models);
        view.subscribeToListener(new Listener(view));
        view.setVisible(true);
    }

    private static ModelRecovery defaultModels(Dimension fieldSize) {
        GameProcess gameProcess = new GameProcess(new Game(Game.Level.LEVEL_1));
        Snake snake = new Snake();
        Worm worm = new Worm(fieldSize.width - SIZE_OF_SQUARE * 4,
                fieldSize.height - SIZE_OF_SQUARE);
        Apple[] apples = new Apple[] {new Apple(Apple.Type.RED), new Apple(Apple.Type.BLUE), new Apple(Apple.Type.PEOPLE)};
        Bonus bonus = new Bonus();
        ModelRecovery modelRecovery = new ModelRecovery(gameProcess, snake, worm, apples, bonus);
        for(Apple current : modelRecovery.getApples()) {
            current.regenerateCoordinates(fieldSize, modelRecovery);
        }
        return modelRecovery;
    }

    public static void restart(View currentView, PlayingField.Size newSize, ModelRecovery models) {
        currentView.dispose();
        models = recreateModels(new Dimension(newSize.getX(), newSize.getY()), models);
        start(newSize, currentView.getPlayingField().drawNet(), models);
    }

    private static ModelRecovery recreateModels(Dimension fieldSize, ModelRecovery currentModels) {
        ModelRecovery defaultModels = defaultModels(fieldSize);
        Apple[] apples = currentModels.getApples();
        for(Apple current : apples) {
            current.regenerateCoordinates(fieldSize, defaultModels);
            defaultModels.setApples(apples);
        }
        defaultModels.setGameProcess(new GameProcess(new Game(currentModels.getGameProcess().getGame().getLevel())));
        return defaultModels;
    }

    public static Controller getController() {
        return controller;
    }
}
