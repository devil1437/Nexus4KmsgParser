
package com.ntu.main;

import com.ntu.alarm.Alarm;
import com.ntu.common.Record;
import com.ntu.common.State;
import com.ntu.common.WakeUpReason;
import com.ntu.parser.ParseKmsgLog;
import com.ntu.utils.Statistics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MainFrame extends JFrame implements ActionListener {

    /**
     * Auto-generated UID
     */
    private static final long serialVersionUID = -7372667153833101567L;

    private static final JFileChooser FILE_CHOOSER = new JFileChooser();

    private static final Logger LOG = Logger.getLogger("MainFrame");

    private final JPanel mContentPane;

    private final JPanel mResultPanel;

    private final JButton mBtnOpen, mBtnAutoTest;

    private final JCheckBox logFilterCheckBox, logAfterTiltCheckBox, useNewTiltCheckBox;

    private final JComboBox groundTruthList;

    private final JLabel mLblInfo;

    private ImageIcon[] mStateIcon;

    private final JTable mTbStateTime;

    private final JTable mTbSecondTierTime;

    private final String[] initMode = {
            "Still", "Walk", "Run", "Bike", "Vehicle"
    };

    int modeGroundTruth = 0;

    /**
     * Create the frame.
     */
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 600);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnAbout = new JMenu("About");
        menuBar.add(mnAbout);

        JMenuItem mntmAboutMe = new JMenuItem("About me");
        mnAbout.add(mntmAboutMe);
        mContentPane = new JPanel();
        mContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mContentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 232
        };
        gbl_contentPane.rowHeights = new int[] {
                0, 0, 0, 0, 0, 0, 47, 0, 0
        };
        gbl_contentPane.columnWeights = new double[] {
                1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                1.0, 1.0, 1.0
        };
        gbl_contentPane.rowWeights = new double[] {
                0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE
        };
        mContentPane.setLayout(gbl_contentPane);

        JButton btnOpen = new JButton("Open");
        btnOpen.addActionListener(this);

        // set autotest button and combo box
        mBtnAutoTest = new JButton("AutoTest");
        mBtnAutoTest.addActionListener(this);
        groundTruthList = new JComboBox(initMode);
        groundTruthList.setSelectedIndex(0);
        groundTruthList.addActionListener(this);

        logFilterCheckBox = new JCheckBox();
        logFilterCheckBox.setName("log Feature");
        logFilterCheckBox.setText("log Feature");
        logAfterTiltCheckBox = new JCheckBox();
        logAfterTiltCheckBox.setName("log AfterTilt");
        logAfterTiltCheckBox.setText("log AfterTilt");
        useNewTiltCheckBox = new JCheckBox();
        useNewTiltCheckBox.setName("New Tilt function");
        useNewTiltCheckBox.setText("New Tilt function");
        useNewTiltCheckBox.setSelected(true);

        JLabel lblNewLabel = new JLabel("This is testing framework for NTU wake up .");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.gridwidth = 6;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        mContentPane.add(lblNewLabel, gbc_lblNewLabel);
        GridBagConstraints gbc_btnOpen = new GridBagConstraints();
        gbc_btnOpen.gridwidth = 2;
        gbc_btnOpen.insets = new Insets(0, 0, 5, 5);
        gbc_btnOpen.gridx = 0;
        gbc_btnOpen.gridy = 2;
        mContentPane.add(btnOpen, gbc_btnOpen);

        // put autotest button and combo box on panel
        GridBagConstraints gbc_btnAuto = new GridBagConstraints();
        gbc_btnAuto.gridwidth = 1;
        gbc_btnAuto.insets = new Insets(0, 0, 5, 5);
        gbc_btnAuto.gridx = 2;
        gbc_btnAuto.gridy = 2;
        mContentPane.add(mBtnAutoTest, gbc_btnAuto);

        GridBagConstraints gbc_listAuto = new GridBagConstraints();
        gbc_listAuto.gridwidth = 1;
        gbc_listAuto.insets = new Insets(0, 0, 5, 5);
        gbc_listAuto.gridx = 3;
        gbc_listAuto.gridy = 2;
        mContentPane.add(groundTruthList, gbc_listAuto);

        GridBagConstraints gbc_check1 = new GridBagConstraints();
        gbc_check1.gridwidth = 1;
        gbc_check1.insets = new Insets(0, 0, 5, 5);
        gbc_check1.gridx = 7;
        gbc_check1.gridy = 1;
        mContentPane.add(logFilterCheckBox, gbc_check1);

        GridBagConstraints gbc_check2 = new GridBagConstraints();
        gbc_check2.gridwidth = 1;
        gbc_check2.insets = new Insets(0, 0, 5, 5);
        gbc_check2.gridx = 8;
        gbc_check2.gridy = 1;
        mContentPane.add(logAfterTiltCheckBox, gbc_check2);

        GridBagConstraints gbc_check3 = new GridBagConstraints();
        gbc_check3.gridwidth = 1;
        gbc_check3.insets = new Insets(0, 0, 5, 5);
        gbc_check3.gridx = 9;
        gbc_check3.gridy = 1;
        mContentPane.add(useNewTiltCheckBox, gbc_check3);

        JLabel lblOriginalBigResult = new JLabel("Original Big Result");
        GridBagConstraints gbc_lblOriginalBigResult = new GridBagConstraints();
        gbc_lblOriginalBigResult.insets = new Insets(0, 0, 5, 0);
        gbc_lblOriginalBigResult.gridx = 18;
        gbc_lblOriginalBigResult.gridy = 3;
        mContentPane.add(lblOriginalBigResult, gbc_lblOriginalBigResult);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridheight = 4;
        gbc_scrollPane.weightx = 20.0;
        gbc_scrollPane.gridwidth = 18;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 3;
        mContentPane.add(scrollPane, gbc_scrollPane);

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] {
                0
        };
        gbl_panel.rowHeights = new int[] {
                0
        };
        gbl_panel.columnWeights = new double[] {
                Double.MIN_VALUE
        };
        gbl_panel.rowWeights = new double[] {
                Double.MIN_VALUE
        };
        panel.setLayout(gbl_panel);
        scrollPane.setViewportView(panel);

        JScrollPane scrollPane_1 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.weighty = 1.0;
        gbc_scrollPane_1.weightx = 1.0;
        gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.gridx = 18;
        gbc_scrollPane_1.gridy = 4;
        mContentPane.add(scrollPane_1, gbc_scrollPane_1);

        mTbStateTime = new JTable();
        scrollPane_1.setViewportView(mTbStateTime);

        JLabel lblSecondtierResult = new JLabel("Second-tier Result");
        GridBagConstraints gbc_lblSecondtierResult = new GridBagConstraints();
        gbc_lblSecondtierResult.insets = new Insets(0, 0, 5, 0);
        gbc_lblSecondtierResult.gridx = 18;
        gbc_lblSecondtierResult.gridy = 5;
        mContentPane.add(lblSecondtierResult, gbc_lblSecondtierResult);

        JScrollPane scrollPane_2 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
        gbc_scrollPane_2.weighty = 1.0;
        gbc_scrollPane_2.weightx = 1.0;
        gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_2.gridx = 18;
        gbc_scrollPane_2.gridy = 6;
        mContentPane.add(scrollPane_2, gbc_scrollPane_2);

        mTbSecondTierTime = new JTable();
        scrollPane_2.setViewportView(mTbSecondTierTime);

        JLabel lblInfo = new JLabel("Info");
        GridBagConstraints gbc_lblInfo = new GridBagConstraints();
        gbc_lblInfo.gridwidth = 18;
        gbc_lblInfo.insets = new Insets(0, 0, 0, 5);
        gbc_lblInfo.gridx = 0;
        gbc_lblInfo.gridy = 7;

        mContentPane.add(lblInfo, gbc_lblInfo);
        mBtnOpen = btnOpen;
        mLblInfo = lblInfo;
        mResultPanel = panel;

        refreshUI();
    }

    /**
     * Set the text of Info(Below the result panel).
     */
    public void setInfo(String str) {
        mLblInfo.setText(str);
        mLblInfo.repaint();
        mContentPane.repaint();
    }

    /**
     * Refresh the ui of main frame. Only use for Open button.
     */
    public void refreshUI() {
        LOG.info("refreshUI()..");
        mResultPanel.removeAll();

        if (Main.PARSER != null) {

        } else {
            mLblInfo.setText("You can choose a kmsg to start the application.");
        }
    }

    /**
     * Transform msec to time string.
     */
    private String msec2TimeFormat(long msec) {
        String str = new String();
        long sec = msec / 1000;
        boolean flag = false;

        // Time format: ddDhh:mm:ss
        if (sec / (60 * 60 * 24) > 0) {
            str += sec / (60 * 60 * 24) + "D ";
            sec = sec - (sec / (60 * 60 * 24)) * (60 * 60 * 24);
            flag = true;
        }
        if (sec / (60 * 60) > 0 || flag) {
            str += sec / (60 * 60) + ":";
            sec = sec - (sec / (60 * 60)) * (60 * 60);
            flag = true;
        }
        if (sec / (60) > 0 || flag) {
            str += sec / (60) + ":";
            sec = sec - (sec / (60)) * (60);
            flag = true;
        }
        str += sec;

        return str;
    }

    /**
     * Resize image.
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

    /**
     * The action while clicking the button and ComboBox
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        // get using tilt function
        if (useNewTiltCheckBox.isSelected()) {

        } else {

        }
        // While clicking the "open" button
        if (event.getSource() == mBtnOpen) {
            LOG.info("Click open button.");
            int returnVal = FILE_CHOOSER.showOpenDialog(MainFrame.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
                // This is where a real application would open the file.
                File file = FILE_CHOOSER.getSelectedFile();
                LOG.info("Open " + file.getName() + ".");

                SwingWorker<Void, Void> worker = new SwingWorker<Void,
                        Void>() {
                            @Override
                            protected Void doInBackground() throws Exception {
                                Main.PARSER = new ParseKmsgLog(FILE_CHOOSER.getSelectedFile());
                                System.out.println("!start parse");
                                Main.PARSER.parse();
                                Main.STATE_TIME_CALCULATOR.reset();
                                refreshUI();
                                Main.MAIN_FRAME.setInfo("Complete. "
                                        + FILE_CHOOSER.getSelectedFile().getName());

                                ArrayList<Record> records = Main.PARSER.getRecords();

                                for (Record record : records) {
                                    System.out.println(record.toString());
                                }

                                try {
                                    statistics();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        };
                worker.execute();
            } else {
                LOG.info("Open command cancelled by user.");
            }
        }
        // while click auto test button
        else if (event.getSource() == mBtnAutoTest) {
            FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = FILE_CHOOSER.showOpenDialog(MainFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {

            } else {
                LOG.info("Open command cancelled by user.");
            }
        }
    }

    protected void statistics() throws IOException {
        ArrayList<Record> records = Main.PARSER.getRecords();
        Statistics stat = new Statistics(Main.PARSER.getRecords());
        File statFile;
        BufferedWriter statBufferedWriter;
        int alarmCount = 0, packetCount = 0, wakeUpCount = 0;
        ArrayList<String> uidList = new ArrayList<String>();

        double totalLogTime = records.get(0).timeDiff(records.get(records.size() - 1));

        for (Record record : records) {
            if (record.mState == State.WAKE_UP) {
                wakeUpCount++;
                for (int reason : record.mWakeUpReason) {
                    if (reason == WakeUpReason.ALARM_MANAGER) {
                        alarmCount++;
                    }
                    else if (reason == WakeUpReason.WIFI_PACKET) {
                        packetCount++;
                    }
                }

                for (Alarm alarm : record.mAlarm) {
                    if (!uidList.contains(alarm.getId())) {
                        uidList.add(alarm.getId());
                    }
                }
            }
        }

        if (Main.ENABLE_STATISTICS_OUTPUT) {
            // Output the statistics file.
            statFile = new File("/home/howard/Documents/stat.csv");
            statFile.createNewFile();
            statBufferedWriter = new BufferedWriter(new FileWriter(statFile, true));

            statBufferedWriter.write(Main.PARSER.getFilename() + "," + totalLogTime + ","
                    + (wakeUpCount / (totalLogTime / 3600)) + ","
                    + (alarmCount / (totalLogTime / 3600)) + ","
                    + (packetCount / (totalLogTime / 3600)) + ","
                    + stat.getMean(State.WAKE_UP, WakeUpReason.DEFAULT) + ","
                    + stat.getStdDev(State.WAKE_UP, WakeUpReason.DEFAULT) + ","
                    + stat.getMean(State.WAKE_UP, WakeUpReason.ALARM_MANAGER) + ","
                    + stat.getStdDev(State.WAKE_UP, WakeUpReason.ALARM_MANAGER) + ","
                    + stat.getMean(State.WAKE_UP, WakeUpReason.WIFI_PACKET) + ","
                    + stat.getStdDev(State.WAKE_UP, WakeUpReason.WIFI_PACKET) + ","
                    + stat.getMean(State.SLEEP, WakeUpReason.DEFAULT) + ","
                    + stat.getStdDev(State.SLEEP, WakeUpReason.DEFAULT) + ",\n");

            statBufferedWriter.flush();
            statBufferedWriter.close();

            String sepFilename = Main.PARSER.getFilename();
            sepFilename += "_statistics.csv";

            statFile = new File(sepFilename);
            statFile.createNewFile();
            statBufferedWriter = new BufferedWriter(new FileWriter(statFile, false));

            for (String id : uidList) {
                Record lastRecord = null;
                statBufferedWriter.write(id + ",");
                for (Record record : records) {
                    if (record.mState == State.WAKE_UP) {
                        for (Alarm alarm : record.mAlarm) {
                            if (alarm.getId().equals(id)) {
                                if (lastRecord != null) {
                                    statBufferedWriter.write(lastRecord.timeDiff(record) + ",");
                                }
                                else {
                                    statBufferedWriter.write("0,");
                                }
                                lastRecord = record;
                                break;
                            }
                        }
                    }
                }
                statBufferedWriter.write("\n");

                statBufferedWriter.write(id + ",");
                for (Record record : records) {
                    if (record.mState == State.WAKE_UP) {
                        for (Alarm alarm : record.mAlarm) {
                            if (alarm.getId().equals(id)) {
                                statBufferedWriter.write(record.getDuration() + ",");
                                break;
                            }
                        }
                    }
                }
                statBufferedWriter.write("\n");
            }

            statBufferedWriter.write("\n");
            statBufferedWriter.write("\n");

            for (String id : uidList) {
                Record lastRecord = null;
                statBufferedWriter.write(id + ",");
                for (Record record : records) {
                    if (record.mState == State.WAKE_UP) {
                        for (Alarm alarm : record.mAlarm) {
                            if (alarm.getId().equals(id) && (alarm.type == 0 || alarm.type == 2)) {
                                if (lastRecord != null) {
                                    statBufferedWriter.write(lastRecord.timeDiff(record) + ",");
                                }
                                else {
                                    statBufferedWriter.write("0,");
                                }
                                lastRecord = record;
                                break;
                            }
                        }
                    }
                }
                statBufferedWriter.write("\n");

                statBufferedWriter.write(id + ",");
                for (Record record : records) {
                    if (record.mState == State.WAKE_UP) {
                        for (Alarm alarm : record.mAlarm) {
                            if (alarm.getId().equals(id) && (alarm.type == 0 || alarm.type == 2)) {
                                statBufferedWriter.write(record.getDuration() + ",");
                                break;
                            }
                        }
                    }
                }
                statBufferedWriter.write("\n");
            }

            statBufferedWriter.flush();
            statBufferedWriter.close();
        }
    }
}
