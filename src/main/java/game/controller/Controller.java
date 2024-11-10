package game.controller;

import game.model.Game;
import game.view.PlayingField;
import game.view.View;

public interface Controller {
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
    
    void dispose();
}
