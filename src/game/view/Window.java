package game.view;

import game.model.GameProcess;
import game.model.Game;
import game.model.ModelRecovery;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

public class Window extends JFrame implements View {
    private final JButton go = new JButton("Старт (ctrl или SHIFT)");
    private final JLabel labelWithScore = new JLabel("Счёт: 0");
    private final JButton buttonHelp = new JButton("Как играть");
    private final JCheckBox net = new JCheckBox("Сетка");

    private final JComboBox<String> choiceLevel = new UserComboBox<>(new String[] {
            "Уровень 1", "Уровень 2", "Уровень 3", "Уровень 4", "Уровень 5", "Уровень 6"});
    private final JComboBox<String> changeFieldSize = new UserComboBox<>(new String[] {
            "Поле: 22×22 квадрата", "Поле: 30×26 квадратов", "Поле: 43×26 квадратов"});
    private final JComboBox<String> changeApplesCount = new UserComboBox<>(new String[] {
            "Яблоки: одно каждого цвета", "Яблоки: по два каждого цвета", "Яблоки: по три каждого цвета"});

    private final PlayingField playingField;
    private JPanel generalPanel;
    private JPanel generalSouthPanel;

    public Window(PlayingField playingField) {
        this.playingField = Objects.requireNonNull(playingField, "Параметр playingField не должен быть null!");

        SwingUtilities.invokeLater(() -> {
            generalPanel = new JPanel(new BorderLayout());
            generalSouthPanel = new JPanel(new BorderLayout());
            constructSouthPanels();
            playingField.add(generalSouthPanel);
            generalPanel.add("Center", playingField);
            generalPanel.add("South", go);
            setContentPane(generalPanel);

            setOptimalSize();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Змейка и червячок. ©А.А. Зотин, 2023 — 2024");
            setFocusable(true);
            pack();
        });
    }
    
    private void constructSouthPanels() {
        int fromLineToSouthPanel = (playingField.sizeOfField() == PlayingField.Size.MAX)? 30 : 10;
        SpringLayout spLayout = new SpringLayout();
        playingField.setLayout(spLayout);
        spLayout.putConstraint(SpringLayout.NORTH, generalSouthPanel, 
                playingField.sizeOfField().getY() + fromLineToSouthPanel,
                SpringLayout.NORTH, generalPanel);
        spLayout.putConstraint(SpringLayout.WEST, generalSouthPanel, 0,
                SpringLayout.WEST, generalPanel);
        constructFirstSouthPanel();
    }

    private  void constructFirstSouthPanel() {
        int hGap = (playingField.sizeOfField() == PlayingField.Size.MIDDLE)? 65 : 30;
        JPanel firstSouthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, hGap, 10));
        firstSouthPanel.add(choiceLevel);
        firstSouthPanel.add(labelWithScore);
        firstSouthPanel.add(net);
        firstSouthPanel.add(buttonHelp);
        if(playingField.sizeOfField() == PlayingField.Size.MAX) {
            firstSouthPanel.add(changeFieldSize);
            firstSouthPanel.add(changeApplesCount);
        } else {
            constructSecondSouthPanel(hGap);
        }
        generalSouthPanel.add("North", firstSouthPanel);
    }

    private void constructSecondSouthPanel(int hGap) {
        JPanel secondSouthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, hGap, 8));
        secondSouthPanel.add(changeFieldSize);
        secondSouthPanel.add(changeApplesCount);
        generalSouthPanel.add("South", secondSouthPanel);
    }

    private void setOptimalSize() {
        setResizable(false);
        int xSize = playingField.sizeOfField().getX();
        int ySize = playingField.sizeOfField().getY() + 150;
        generalPanel.setPreferredSize(new Dimension(xSize, ySize));
    }

    @Override
    public void subscribeToListener(Listener listener) {
        SwingUtilities.invokeLater(() -> {
            go.addActionListener(listener);
            buttonHelp.addActionListener(listener);
            net.addActionListener(listener);
            choiceLevel.addActionListener(listener);
            changeApplesCount.addActionListener(listener);
            changeFieldSize.addActionListener(listener);
            addKeyListener(listener);
        });
    }

    @Override
    public void update(ModelRecovery models) {
        SwingUtilities.invokeLater(() -> {
            updateApplesCountComboBox(models);
            updateFieldSizeComboBox();
            updateLevelComboBox(models);
            GameProcess gameProcess = models.getGameProcess();
            labelWithScore.setText("Счёт: " + gameProcess.getGame().getScore());
            if (playingField.drawNet()) net.setSelected(true);
            updateGoButton(gameProcess);
        });
    }

    private void updateLevelComboBox(ModelRecovery models) {
        Game.Level level = models.getGameProcess().getGame().getLevel();
        choiceLevel.setSelectedItem("Уровень " + level.getNum());
    }

    private void updateApplesCountComboBox(ModelRecovery models) {
        int applesCount = models.getApples().length;
        String selected = switch (applesCount) {
            case 3 -> "Яблоки: одно каждого цвета";
            case 6 -> "Яблоки: по два каждого цвета";
            case 9 -> "Яблоки: по три каждого цвета";
            default -> throw new UnsupportedOperationException("Неожиданное количество яблок: " + applesCount);
        };
        changeApplesCount.setSelectedItem(selected);
    }

    private void updateFieldSizeComboBox() {
        PlayingField.Size size = playingField.sizeOfField();
        String selected = switch (size) {
            case MIN -> "Поле: 22×22 квадрата";
            case MIDDLE -> "Поле: 30×26 квадратов";
            case MAX -> "Поле: 43×26 квадратов";
            //noinspection UnnecessaryDefault
            default -> throw new UnsupportedOperationException("Неизвестная константа: " + size);
        };
        changeFieldSize.setSelectedItem(selected);
    }

    private void updateGoButton(GameProcess gameProcess) {
        if (!gameProcess.isStarted()) go.setText("Старт (ctrl или SHIFT)");
        else if (!gameProcess.isPaused()) go.setText("Стоп (ctrl или SHIFT)");
        else go.setText("Продолжить (ctrl или SHIFT)");
    }

    @Override
    public void setVisible(boolean visible) {
        SwingUtilities.invokeLater(() -> {
            super.setVisible(visible);
            if (visible) {
                setLocationRelativeTo(null);
                requestFocus();
            }
        });
    }

    @Override
    public void setDrawNet(boolean drawNet) {
        playingField.setDrawNet(drawNet);
    }

    @Override
    public PlayingField getPlayingField() {
        return playingField;
    }

    public JButton getGoButton() {
        return go;
    }

    public JButton getButtonHelp() {
        return buttonHelp;
    }

    public JComboBox<String> getChoiceLevel() {
        return choiceLevel;
    }

    public JComboBox<String> getChangeFieldSize() {
        return changeFieldSize;
    }

    public JComboBox<String> getChangeApplesCount() {
        return changeApplesCount;
    }
}