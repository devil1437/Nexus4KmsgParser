
package com.ntu.main;

import com.ntu.parser.ParseKmsgLog;
import com.ntu.utils.StateTimeCalculator;

import java.awt.EventQueue;
import java.util.logging.Logger;

public class Main {
    private static final String TAG = "Main";

    private static final Logger LOG = Logger.getLogger(TAG);

    public static final boolean ENABLE_STATISTICS_OUTPUT = true;

    public static ParseKmsgLog PARSER = null;

    public static MainFrame MAIN_FRAME;

    public static StateTimeCalculator STATE_TIME_CALCULATOR;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    STATE_TIME_CALCULATOR = new StateTimeCalculator();
                    MAIN_FRAME = new MainFrame();
                    MAIN_FRAME.setVisible(true);
                    LOG.info("Start.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
