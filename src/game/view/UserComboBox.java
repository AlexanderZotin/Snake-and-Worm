package game.view;

import javax.swing.JComboBox;

public class UserComboBox<E> extends JComboBox<E> {

    public UserComboBox(E[] items) {
        super(items);
    }

    @Override
    protected void fireActionEvent() {
        Object selectedItem = getSelectedItem();
        if (this.hasFocus() && selectedItem != null) {
            super.fireActionEvent();
        }
    }
}
