package com.varunchandran.blackjack;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public final class Main extends JFrame implements ActionListener {

  private static final long serialVersionUID = 1L;
  private static BufferedImage[] cards;
  Random random;
  private JButton actionButton;
  private JPanel cardPanel;
  private int count;
  private JLabel countLabel;
  private JTextField cpmField;
  private int index = 0;
  private int score;
  private JLabel scoreLabel;
  private Timer timer;
  private boolean[] track;
  private JLabel valueLabel;
  private int value;
  public Main() {
    setTitle("Card Counting Trainer");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    add(getControlPanel(), BorderLayout.PAGE_START);
    add(initImagePanel(), BorderLayout.CENTER);
    add(getStatusPanel(), BorderLayout.LINE_END);

    pack();
    setVisible(true);
  }

  private static final void loadCards() throws IOException {
    cards = new BufferedImage[52];
    for (int i = 0; i < 52; i++) {
      cards[i] = ImageIO.read(Main.class.getResourceAsStream("/classic-cards/" + (i + 1) + ".png"));
    }
  }

  public static void main(String[] args) throws IOException {
    loadCards();
    new Main();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand() == null) {
      doTimerTask();
      return;
    }

    switch (e.getActionCommand()) {
      case "Start":
        start();
        return;

      case "Stop":
        stop();
        return;

      default:
        System.out.println(e.getActionCommand());
    }
  }

  private void doTimerTask() {
    if (count > 52) {
      stop();
      return;
    }

    index = random.nextInt(52);
    while (track[index] == true) {
      index = random.nextInt(52);
    }

    value = getCardValue(index);
    score += value;

    countLabel.setText("Count: " + count);
    valueLabel.setText("Value: " + value);
    scoreLabel.setText("Score: " + score);
    cardPanel.repaint();

    track[index] = true;
    count++;
  }

  private int getCardValue(int index) {
    int ordinal = (int) Math.ceil(index / 4);
    return Deck.values()[ordinal].getValue();
  }

  private JPanel getControlPanel() {
    cpmField = new JTextField(5);
    cpmField.setText("52");
    actionButton = new JButton();
    actionButton.setText("Start");
    actionButton.addActionListener(this);
    JLabel label = new JLabel("cards per minute");
    JPanel panel = new JPanel();
    panel.add(cpmField);
    panel.add(label);
    panel.add(actionButton);
    return panel;
  }

  private Component getStatusPanel() {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(80, 10));
    GridLayout grid = new GridLayout(3, 1);
    panel.setLayout(grid);
    countLabel = new JLabel();
    valueLabel = new JLabel();
    scoreLabel = new JLabel();
    panel.add(countLabel);
    panel.add(scoreLabel);
    panel.add(valueLabel);
    return panel;
  }

  private Component initImagePanel() {
    cardPanel = new JPanel() {

      private static final long serialVersionUID = 1L;

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(cards[index], 100, 0, null);
      }
    };
    cardPanel.setPreferredSize(new Dimension(0, 106));
    return cardPanel;
  }

  private void start() {
    count = 1;
    score = 0;
    value = 0;
    track = new boolean[52];
    random = new Random();
    int tick = (int) ((double) 60 / Integer.parseInt(cpmField.getText()) * 1000);

    timer = new Timer(tick, this);
    timer.start();
    actionButton.setText("Stop");
  }

  private void stop() {
    timer.stop();
    actionButton.setText("Start");
  }
}
