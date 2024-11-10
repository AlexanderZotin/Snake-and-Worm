package game;

import java.awt.Dimension;
import javax.swing.SwingUtilities;

import game.controller.Controller;
import game.controller.GameController;
import game.controller.Listener;
import game.model.*;
import game.view.*;
import lombok.NonNull;

import static game.ProgramConstants.SIZE_OF_SQUARE;

public class Application {
    private static Controller controller;
    
    public static void start(@NonNull PlayingField.Size size, boolean drawNet, ModelRepository models) {
        if(models == null) {
            models = defaultModels(size.getDimension());
        }
        ModelManager.getInstance().setModels(models);
        SwingUtilities.invokeLater(() -> {
            View view = new Window(new PlayingField(size, drawNet));
            controller = new GameController(view);
            view.subscribeToListener(new Listener(controller));
            view.update();
            view.setVisible(true);;
        });
    }

    private static ModelRepository defaultModels(@NonNull Dimension fieldSize) {
        GameProcess gameProcess = new GameProcess(new Game(Game.Level.LEVEL_1));
        Snake snake = new Snake();
        Worm worm = new Worm(fieldSize.width - SIZE_OF_SQUARE * 4,
                fieldSize.height - SIZE_OF_SQUARE);
        Apple[] apples = new Apple[] {new Apple(Apple.Type.RED), new Apple(Apple.Type.BLUE), new Apple(Apple.Type.PEOPLE)};
        Bonus bonus = new Bonus();
        ModelRepository models = new ModelRepository(gameProcess, snake, worm, apples, bonus, fieldSize);
        ModelManager.getInstance().setModels(models);
        for(Apple current : models.getApples()) {
            current.regenerateCoordinates();
        }
        return models;
    }

    public static void restart(PlayingField.Size newSize) {
        if(newSize == null) {
            newSize = PlayingField.Size.getConstantByDimension(
                    ModelManager.getInstance().getModels().getFieldSize());
        }
        boolean drawNet = controller.getView().getPlayingField().isDrawNet();
        controller.dispose();
        ModelRepository models = recreateModels(newSize.getDimension());
        start(PlayingField.Size.getConstantByDimension(models.getFieldSize()), drawNet, models);
    }

    private static ModelRepository recreateModels(@NonNull Dimension fieldSize) {
        ModelRepository currentModels = ModelManager.getInstance().getModels();
        ModelRepository defaultModels = defaultModels(fieldSize);
        Apple[] apples = currentModels.getApples();
        for(Apple current : apples) {
            current.regenerateCoordinates();
            defaultModels.setApples(apples);
        }
        Game game = new Game(currentModels.getGameProcess().getGame().getLevel());
        defaultModels.setGameProcess(new GameProcess(game));
        return defaultModels;
    }
}
