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
    private long milliseconds;

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
                stopwatch.resumeThis();
            } else if (startButton.getText().equals(START_STATUS)) {
                startButton.setText(PAUSE_STATUS);
                stopwatch.start();
            } else {
                startButton.setText(START_STATUS);
                stopwatch.pause();
                stopwatch.stop();
            }
        });

        lapButton.addActionListener(e -> {
            if (count >= 1) {
                String[] previous = ((String) tableModel.getValueAt(count - 1, 2)).split(":");
                long milliPrev = Integer.parseInt(previous[0]) * 3600000
                        + Integer.parseInt(previous[1]) * 60000 + Integer.parseInt(previous[2]) * 1000;

                long aux = milliseconds - milliPrev;

                tableModel.addRow(new String[]{"" + (count + 1), String.format("%02d:%02d:%02d", (aux / 3600000),
                        (aux / 60000) % 60, (aux / 1000) % 60), stopwatchLabel.getText()});
            } else {
                tableModel.addRow(new String[]{"" + (count + 1), stopwatchLabel.getText(), stopwatchLabel.getText()});
            }
            count++;
        });

        resetButton.addActionListener(e -> {
            if (stopwatch != null) {
                stopwatch.stopThis();
                startButton.setText(START_STATUS);
                stopwatchLabel.setText(STOPWATCH_INITIAL_STATUS);
                millisecondsLabel.setText(MILLISECONDS_INITIAL_STATUS);
                stopwatch = null;
                milliseconds = 0;
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
        }

        public synchronized void resumeThis() {
            pause = false;
            notify();
        }

        public void stopThis() {
            running = false;
        }

        @Override
        public void run() {
            try {
                while (running) {
                    synchronized (this) {
                        if (pause) { wait(); }
                    }
                    stopwatchLabel.setText(String.format("%02d:%02d:%02d", (milliseconds / 3600000),
                            (milliseconds / 60000) % 60, (milliseconds / 1000) % 60));
                    millisecondsLabel.setText(String.format("%03d", milliseconds % 1000));
                    milliseconds += 2;
                    sleep(2);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
