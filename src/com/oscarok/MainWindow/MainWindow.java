package com.oscarok.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        private long milliseconds;
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
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            DateFormat milliFormat = new SimpleDateFormat("SSS");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            while (running) {
                try {
                    millisecondsLabel.setText(milliFormat.format(new Date(milliseconds)));
                    stopwatchLabel.setText(format.format(new Date(milliseconds)));
                    milliseconds += 2;
                    sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
