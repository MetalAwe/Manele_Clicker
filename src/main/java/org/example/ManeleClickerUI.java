package org.example;

import javax.swing.*;
import java.awt.*;

public class ManeleClickerUI {

    GameLogic logic = new GameLogic();
    SoundManager sound = new SoundManager();

    JFrame window;
    JLabel countLabel, statsLabel;
    JButton clickButton, upgradeButton, factoryButton;
    JProgressBar heatBar;
    JPanel statsPanel;
    Dimension btnSize = new Dimension(350, 60);

    static void main(String[] args) {
        new ManeleClickerUI().createUI();
    }

    public void createUI() {
        String[] myManeleSongs = {
                "src/main/Game Resources/A_Înflorit_Cerul.wav",
                "src/main/Game Resources/Altă_Valoare.wav",
                "src/main/Game Resources/Magie_În_.wav",
                "src/main/Game Resources/Regina_Razelor_de_Soare.wav",
                "src/main/Game Resources/Stâlpul_Echipei.wav",
                "src/main/Game Resources/Împăratul_Din_Primărie.wav"
        };
        sound.loadPlaylist(myManeleSongs);
        window = new JFrame("Manele Clicker: Hype Edition 🎶");
        window.setSize(900, 750);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new GridBagLayout());
        window.getContentPane().setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // 1. TOP: SCORE
        countLabel = new JLabel("Manele: 0", SwingConstants.CENTER);
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        countLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weighty = 0.1;
        window.add(countLabel, gbc);

        // 2. LEFT: CLICKER
        clickButton = new JButton("🎶");
        clickButton.setFont(new Font("SansSerif", Font.PLAIN, 100));
        clickButton.setFocusPainted(false);
        clickButton.addActionListener(e -> {
            logic.doClick();
            refreshUI();
        });
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 2; gbc.weightx = 0.5; gbc.weighty = 0.7;
        window.add(clickButton, gbc);

        // 3. RIGHT: SHOP (Build it fully BEFORE adding to window)
        JPanel upgradeListPanel = new JPanel();
        upgradeListPanel.setLayout(new BoxLayout(upgradeListPanel, BoxLayout.Y_AXIS));
        upgradeListPanel.setBackground(new Color(40, 40, 40));

        // Initialize buttons
        upgradeButton = createUpgradeButton();
        upgradeButton.addActionListener(e -> {
            if (logic.buyAutoClicker()) refreshUI();
        });

        factoryButton = createUpgradeButton();
        factoryButton.addActionListener(e -> {
            if (logic.buyFactory()) refreshUI();
        });

        // Add buttons to the panel
        upgradeListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        upgradeListPanel.add(upgradeButton);
        upgradeListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        upgradeListPanel.add(factoryButton);

        // Put panel in ScrollPane
        JScrollPane scroll = new JScrollPane(upgradeListPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // Add ScrollPane to Window (JUST ONCE)
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 2;
        gbc.weightx = 0.5;
        window.add(scroll, gbc);

        // 4. BOTTOM: STATS
        statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        statsPanel.setBackground(new Color(45, 45, 45));

        statsLabel = new JLabel("MPS: 0");
        statsLabel.setForeground(Color.LIGHT_GRAY);

        // The Hype Bar (Visual for "Fast Clicking")
        heatBar = new JProgressBar(0, 100);
        heatBar.setPreferredSize(new Dimension(200, 20));
        heatBar.setStringPainted(true);
        heatBar.setString("HYPE");

        statsPanel.add(statsLabel);
        statsPanel.add(heatBar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.gridheight = 1;
        gbc.weighty = 0.1;
        window.add(statsPanel, gbc);

        // Timer
        Timer timer = new Timer(1000, e -> {
            logic.processAutoClick();
            refreshUI();
        });
        timer.start();

        new Timer(100, e -> {
            logic.decayHeat();

            // Music only plays if heat is > 50
            boolean isHyped = logic.heat >= 50;
            sound.updateMusicState(isHyped);

            refreshUI();
        }).start();

        refreshUI();
        window.setVisible(true);
    }

    private JButton createUpgradeButton() {
        JButton btn = new JButton();
        btn.setMaximumSize(btnSize);
        btn.setPreferredSize(btnSize);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setOpaque(true); // FIX: Makes background colors visible
        btn.setBorderPainted(false); // Helps background colors show on some systems
        return btn;
    }

    public void refreshUI() {
        countLabel.setText("Manele: " + String.format("%, d", logic.counter));
        statsLabel.setText("MPS: " + logic.getMPS() + " | Autos: " + logic.autoClickersOwned);

        // Update the Hype Bar
        heatBar.setValue((int)logic.heat);
        if(logic.heat >= 50) {
            heatBar.setForeground(Color.ORANGE);
        } else {
            heatBar.setForeground(Color.DARK_GRAY);
        }

        countLabel.setText("Manele: " + logic.counter);
        statsLabel.setText("MPS: " + logic.getMPS() + " | Autos: " + logic.autoClickersOwned + " | Factories: " + logic.factoriesOwned);

        upgradeButton.setText("Auto-Clicker (" + logic.autoClickerPrice + ")");
        factoryButton.setText("Factory (" + logic.factoryPrice + ")");

        updateColor(upgradeButton, logic.autoClickerPrice);
        updateColor(factoryButton, logic.factoryPrice);
    }

    private void updateColor(JButton btn, int price) {
        if (logic.counter >= price) {
            btn.setBackground(new Color(46, 204, 113)); // Green
            btn.setForeground(Color.BLACK);
        } else {
            btn.setBackground(new Color(192, 57, 43)); // Red
            btn.setForeground(Color.WHITE);
        }
    }
}