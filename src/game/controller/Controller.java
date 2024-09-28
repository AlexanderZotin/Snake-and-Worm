package game.controller;

import game.model.Game;
import game.model.ModelRecovery;
import game.view.PlayingField;
import game.view.View;

public interface Controller {
    ModelRecovery getModels();
    View getView();

    void levelChanged(Game.Level newLevel);
    void applesCountChanged(int eachColorCount);
    void fieldSizeChanged(PlayingField.Size newSize);
    void helpRequested();
    void goPressed();

    void rightArrowPressed();
    void leftArrowPressed();
    void upArrowPressed();
    void downArrowPressed();
}
