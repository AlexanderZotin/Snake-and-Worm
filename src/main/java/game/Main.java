package game;

import game.view.PlayingField.Size;
import static game.ProgramConstants.DRAW_NET_BY_DEFAULT;

import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.okButtonText", "Понятно");
        Application.start(Size.MIN, DRAW_NET_BY_DEFAULT, null);
    }
}
