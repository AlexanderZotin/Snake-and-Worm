package game.view;

import game.Main;
import game.model.Game;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class Listener extends KeyAdapter implements ActionListener, KeyListener {
    private final Window window;

    public Listener(Window window) {
        this.window = Objects.requireNonNull(window, "Параметр window не должен быть null!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        switch (source) {
            case JButton button -> {
                if(button.equals(window.getGoButton()))  Main.getController().goPressed();
                else if(button.equals(window.getButtonHelp())) Main.getController().helpRequested();
            }
            case JComboBox<?> comboBox -> comboBoxTouched(comboBox);
            case JCheckBox drawNet -> {
                Main.getController().getView().setDrawNet(drawNet.isSelected());
                window.repaint();
            }
            default -> throw new UnsupportedOperationException("Неподдерживаемый компонент: " + source);
        }
        window.requestFocus();
    }

    private void comboBoxTouched(JComboBox<?> comboBox) {
        Object selected = comboBox.getSelectedItem();
        assert selected != null;
        String selectedItem = selected.toString();
        if(comboBox.equals(window.getChoiceLevel())) {
            levelComboBoxTouched(selectedItem);
        } else if(comboBox.equals(window.getChangeApplesCount())) {
            applesCountComboBoxTouched(selectedItem);
        } else if(comboBox.equals(window.getChangeFieldSize())) {
            fieldSizeComboBoxTouched(selectedItem);
        }
    }

    private void levelComboBoxTouched(String selectedItem) {
        int numberOfLevel = Integer.parseInt(selectedItem.substring(selectedItem.length() - 1));
        Main.getController().levelChanged(Game.Level.getLevelByNumber(numberOfLevel));
    }

    private void applesCountComboBoxTouched(String selectedItem) {
        int eachColorCount = switch(selectedItem) {
            case "Яблоки: одно каждого цвета" -> 1;
            case "Яблоки: по два каждого цвета" -> 2;
            case "Яблоки: по три каждого цвета" -> 3;
            default -> throw new UnsupportedOperationException("Неизвестная опция: " + selectedItem);
        };
        Main.getController().applesCountChanged(eachColorCount);
    }

    private void fieldSizeComboBoxTouched(String selectedItem) {
        PlayingField.Size size = switch (selectedItem) {
            case "Поле: 22×22 квадрата" -> PlayingField.Size.MIN;
            case "Поле: 30×26 квадратов" -> PlayingField.Size.MIDDLE;
            case "Поле: 43×26 квадратов" -> PlayingField.Size.MAX;
            default -> throw new UnsupportedOperationException(selectedItem);
        };
        Main.getController().fieldSizeChanged(size);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP -> Main.getController().upArrowPressed();
            case KeyEvent.VK_DOWN -> Main.getController().downArrowPressed();
            case KeyEvent.VK_RIGHT -> Main.getController().rightArrowPressed();
            case KeyEvent.VK_LEFT -> Main.getController().leftArrowPressed();
            case KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT -> Main.getController().goPressed();
        }
    }
}
