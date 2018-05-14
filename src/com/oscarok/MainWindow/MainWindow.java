package com.oscarok.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainWindow {
    private final static String STOPWATCH_INITIAL_STATUS = "00:00:00";
    private final static String MILLISECONDS_INITIAL_STATUS = "000";
    private final static String START_STATUS = "Start";
    private final static String PAUSE_STATUS = "Pause";
    private final static String[] COLUMN_NAME = {"#", "Time", "Total time"};

    private Stopwatch stopwatch;
    private int count;

    private JPanel mainPanel;
    private JPanel buttons;
    private JButton startButton;
    private JButton resetButton;
    private JLabel millisecondsLabel;
    private JLabel stopwatchLabel;
    private JTable lapTable;
    private JButton lapButton;
    private JPanel Timer;
    private JPanel Table;

    public MainWindow() {

        DefaultTableModel tableModel = new DefaultTableModel(0, 0);
        tableModel.setColumnIdentifiers(COLUMN_NAME);
        lapTable.setModel(tableModel);

        startButton.addActionListener(e -> {
            if (stopwatch == null) {
                stopwatch = new Stopwatch();
            }

            if (stopwatch.isPause()) {
                stopwatch.setPause(false);
                startButton.setText(PAUSE_STATUS);
                stopwatch.resume();
            } else if (startButton.getText().equals(START_STATUS)) {
                startButton.setText(PAUSE_STATUS);
                stopwatch.start();
            } else {
                startButton.setText(START_STATUS);
                stopwatch.pause();
            }
        });

        lapButton.addActionListener(e -> {
            if (count >= 1) {
                String[] previous = ((String) tableModel.getValueAt(count - 1, 2)).split(":");
                String[] actual = stopwatchLabel.getText().split(":");
                int preSec = Integer.parseInt(previous[0]) * 120 + Integer.parseInt(previous[1]) * 60 + Integer.parseInt(previous[2]);
                int actSec = Integer.parseInt(actual[0]) * 120 + Integer.parseInt(actual[1]) * 60 + Integer.parseInt(actual[2]);
                int aux = actSec - preSec;
                int hours = aux / 120;
                int minutes = aux / 60;
                int seconds = aux % 60;

                StringBuilder stringBuilder = new StringBuilder();

                if (hours < 10) {
                    stringBuilder.append(0).append(hours);
                } else {
                    stringBuilder.append(hours);
                }

                stringBuilder.append(":");

                if (minutes < 10) {
                    stringBuilder.append(0).append(minutes);
                } else {
                    stringBuilder.append(minutes);
                }

                stringBuilder.append(":");

                if (seconds < 10) {
                    stringBuilder.append(0).append(seconds);
                } else {
                    stringBuilder.append(seconds);
                }

                tableModel.addRow(new String[] {"" + (count + 1), stringBuilder.toString(), stopwatchLabel.getText()});
            } else {
                tableModel.addRow(new String[] {"" + (count + 1), stopwatchLabel.getText(), stopwatchLabel.getText()});
            }
            count++;
        });

        resetButton.addActionListener(e -> {
            if (stopwatch != null) {
                stopwatch.stop();
                startButton.setText(START_STATUS);
                stopwatchLabel.setText(STOPWATCH_INITIAL_STATUS);
                millisecondsLabel.setText(MILLISECONDS_INITIAL_STATUS);
                stopwatch = null;
            }
            tableModel.setNumRows(0);
            count = 0;
        });
    }

    private void initComponents() {
        startButton.setBorderPainted(false);
    }

    public static void main(String[] args) {
        MainWindow m = new MainWindow();
        m.initComponents();
        JFrame frame = new JFrame("Stopwatch");
        frame.setContentPane(m.mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private class Stopwatch extends Thread {
        private int milliseconds;
        private int seconds;
        private int minutes;
        private int hours;
        private boolean running = true;
        private boolean pause = false;

        public Stopwatch() {
            super("Stopwatch");
        }

        public boolean isPause() {
            return pause;
        }

        public void setPause(boolean pause) {
            this.pause = pause;
        }

        public void pause() {
            pause = true;
            suspend();
        }

        @Override
        public void run() {
            while (running) {
                try {
                    milliseconds += 2;

                    if (milliseconds > 999) {
                        seconds++;
                        milliseconds = 0;
                    }

                    if (seconds > 59) {
                        minutes++;
                        seconds = 0;
                    }

                    if (minutes > 59) {
                        hours++;
                        minutes = 0;
                    }

                    assignTime();
                    sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTime() {
            StringBuilder stringBuilder = new StringBuilder();

            if (milliseconds < 10) {
                stringBuilder.append("00").append(milliseconds);
            } else if (milliseconds < 100) {
                stringBuilder.append("0").append(milliseconds);
            } else {
                stringBuilder.append(milliseconds);
            }

            millisecondsLabel.setText(stringBuilder.toString());
            stringBuilder.setLength(0);

            if (hours < 10) {
                stringBuilder.append(0).append(hours);
            } else {
                stringBuilder.append(hours);
            }

            stringBuilder.append(":");

            if (minutes < 10) {
                stringBuilder.append(0).append(minutes);
            } else {
                stringBuilder.append(minutes);
            }

            stringBuilder.append(":");

            if (seconds < 10) {
                stringBuilder.append(0).append(seconds);
            } else {
                stringBuilder.append(seconds);
            }

            stopwatchLabel.setText(stringBuilder.toString());
        }
    }
}
