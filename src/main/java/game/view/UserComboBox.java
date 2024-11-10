package game.view;

import javax.swing.JComboBox;

/**
 * This combo-box implementation dones'nt invoke
 * a listener when item is selected by program
 * */
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
