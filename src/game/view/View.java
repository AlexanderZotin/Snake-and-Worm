package game.view;

import game.model.ModelRecovery;

public interface View {
    void update(ModelRecovery models);
    void subscribeToListener(Listener listener);
    PlayingField getPlayingField();
    void setDrawNet(boolean drawNet);

    void dispose();
    void repaint();
    void setVisible(boolean visible);
}
