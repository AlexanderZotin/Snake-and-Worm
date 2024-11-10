package game.view;

import game.controller.Listener;
import javax.swing.JButton;
import javax.swing.JComboBox;

public interface View {
    void update();
    void subscribeToListener(Listener listener);
    void setDrawNet(boolean drawNet);
    void showHelp();

    void dispose();
    void repaint();
    void requestFocus();
    void setVisible(boolean visible);
    
    PlayingField getPlayingField();
    JButton getGoButton();
    JButton getButtonHelp();
    JComboBox<String> getChoiceLevel();
    JComboBox<String> getChangeFieldSize();
    JComboBox<String> getChangeApplesCount();
}
