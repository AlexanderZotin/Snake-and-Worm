package game.controller;

import game.model.Game;
import game.view.PlayingField;
import game.view.View;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Listener extends KeyAdapter implements ActionListener, KeyListener {
    private final Controller linkedController;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        View view = linkedController.getView();
        switch (source) {
            case JButton button -> {
                if(button.equals(view.getGoButton()))  linkedController.goPressed();
                else if(button.equals(view.getButtonHelp())) linkedController.helpRequested();
            }
            case JComboBox<?> comboBox -> comboBoxTouched(comboBox);
            case JCheckBox drawNet -> {
                view.setDrawNet(drawNet.isSelected());
                view.repaint();
            }
            default -> throw new UnsupportedOperationException("Unsupported component: " + source);
        }
        view.requestFocus();
    }

    private void comboBoxTouched(JComboBox<?> comboBox) {
        View view = linkedController.getView();
        Object selected = comboBox.getSelectedItem();
        assert selected != null;
        String selectedItem = selected.toString();
        if(comboBox.equals(view.getChoiceLevel())) {
            levelComboBoxTouched(selectedItem);
        } else if(comboBox.equals(view.getChangeApplesCount())) {
            applesCountComboBoxTouched(selectedItem);
        } else if(comboBox.equals(view.getChangeFieldSize())) {
            fieldSizeComboBoxTouched(selectedItem);
        }
    }

    private void levelComboBoxTouched(String selectedItem) {
        int numberOfLevel = Integer.parseInt(selectedItem.substring(selectedItem.length() - 1));
        linkedController.levelChanged(Game.Level.getLevelByNumber(numberOfLevel));
    }

    private void applesCountComboBoxTouched(String selectedItem) {
        int eachColorCount = switch(selectedItem) {
            case "Яблоки: одно каждого цвета" -> 1;
            case "Яблоки: по два каждого цвета" -> 2;
            case "Яблоки: по три каждого цвета" -> 3;
            default -> throw new UnsupportedOperationException("Unknown option: " + selectedItem);
        };
        linkedController.applesCountChanged(eachColorCount);
    }

    private void fieldSizeComboBoxTouched(String selectedItem) {
        PlayingField.Size size = switch (selectedItem) {
            case "Поле: 22×22 квадрата" -> PlayingField.Size.MIN;
            case "Поле: 30×26 квадратов" -> PlayingField.Size.MIDDLE;
            case "Поле: 43×26 квадратов" -> PlayingField.Size.MAX;
            default -> throw new UnsupportedOperationException(selectedItem);
        };
        linkedController.fieldSizeChanged(size);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP -> linkedController.upArrowPressed();
            case KeyEvent.VK_DOWN -> linkedController.downArrowPressed();
            case KeyEvent.VK_RIGHT -> linkedController.rightArrowPressed();
            case KeyEvent.VK_LEFT -> linkedController.leftArrowPressed();
            case KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT -> linkedController.goPressed();
        }
    }
}
