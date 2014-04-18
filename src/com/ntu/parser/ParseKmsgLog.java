
package com.ntu.parser;

import com.ntu.alarm.Alarm;
import com.ntu.common.Record;
import com.ntu.common.State;
import com.ntu.common.WakeUpReason;
import com.ntu.main.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

public class ParseKmsgLog {
    private static final String TAG = "ParseKmsgLog";

    private static final Logger LOG = Logger.getLogger(TAG);

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
            "HH:mm:ss.SSS");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy/MM/dd");
    // An example: 2014-04-01 18:27:22.064209428
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    private final ArrayList<Record> mRecords = new ArrayList<Record>();
    private String mFilename = null;
    private int mState = State.SLEEP;
    private boolean mIsStart = false;
    private double startSystemTime = 0;
    private double stopSystemTime = 0;
    private Calendar startRealTime = null;
    private Calendar stopRealTime = null;
    private Record mWakeUpRecord = null;
    private Record mSleepRecord = null;

    /**
     * Parse filename.
     */
    public ParseKmsgLog(String filename) {
        mFilename = filename;
    }

    /**
     * Parse file.
     */
    public ParseKmsgLog(File file) {
        mFilename = file.getAbsolutePath();
    }

    /**
     * Read the file.
     */
    public void parse() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(mFilename)));
            String str;
            int count = 1;

            while ((str = br.readLine()) != null) {
                Main.MAIN_FRAME.setInfo(String.format("Parsing the %d's line.", count));
                LOG.info(String.format("Parsing the %d's line.", count++));
                Record tempRecord = parseLine(str);
                if (tempRecord != null) {
                    mRecords.add(tempRecord);
                }
            }
            br.close();
            return;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            LOG.info("The input file isn't not found.");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOG.info("The input file isn't not found.");
            e.printStackTrace();
            return;
        } catch (ParseException e) {
            LOG.info("The input file in wrong format.");
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
    }

    /**
     * Parse a line of the file. The format of a line should be:
     * "<INTEGER>[  SYSTEM_TIME] INFO"
     * 
     * @throws Exception
     */
    private Record parseLine(String str) throws Exception {
        // The system time will be in the split[0] and the info string will be
        // in the split[1]
        String[] split = str.split("] ");
        boolean addSleepRecord = false, addWakeUpRecord = false;

        if (split.length == 2) {

            if (mIsStart) {
                double curSystemTime = parseSystemTime(split[0]);

                if (split[1].contains("Resume caused by")
                        || split[1].contains("aborted suspend due to")) {
                    if (mState == State.SLEEP) {
                        mState = State.WAKE_UP;
                        startSystemTime = parseSystemTime(split[0]);
                        mWakeUpRecord = new Record(State.WAKE_UP);
                        mWakeUpRecord.addWakeUpReason(WakeUpReason.String2Number(split[1]));
                    }
                    else if (mState == State.WAKE_UP) {
                        mWakeUpRecord.addWakeUpReason(WakeUpReason.String2Number(split[1]));
                    }
                }
                else if (split[1].contains("suspend: exit suspend, ret = 0")) {
                    // suspend: exit suspend, ret = 0 (2014-04-01
                    // 18:27:22.064209428 UTC)

                    // Add a sleep record.
                    if (stopRealTime == null && startRealTime == null) {
                        // First Time in the log
                        String timeString = split[1].substring(split[1].indexOf("(") + 1,
                                split[1].lastIndexOf(".") + 4);
                        Date date = DATETIME_FORMAT.parse(timeString);

                        startRealTime = Calendar.getInstance();
                        startRealTime.setTime(date);
                        startRealTime.add(Calendar.MILLISECOND,
                                (int) (-1 * (curSystemTime * 1000. - startSystemTime * 1000.)));
                    }
                    else if (stopRealTime != null && startRealTime == null) {
                        // First Time in a wakeup request
                        String timeString = split[1].substring(split[1].indexOf("(") + 1,
                                split[1].lastIndexOf(".") + 4);
                        Date date = DATETIME_FORMAT.parse(timeString);

                        startRealTime = Calendar.getInstance();
                        startRealTime.setTime(date);
                        startRealTime.add(Calendar.MILLISECOND,
                                (int) (-1 * (curSystemTime * 1000. - startSystemTime * 1000.)));

                        LOG.info("timeString " + timeString);

                        LOG.info("startRealTime " + DATETIME_FORMAT.format(startRealTime.getTime())
                                + " curSystemTime " + curSystemTime + " startSystemTime "
                                + startSystemTime);

                        mSleepRecord = new Record(State.SLEEP);

                        mSleepRecord.mStartTime = (Calendar) stopRealTime.clone();
                        mSleepRecord.mStopTime = (Calendar) startRealTime.clone();

                        stopRealTime = null;

                        addSleepRecord = true;
                    }
                }
                else if (split[1].contains("MyLogcat") && split[1].contains("MyAlarmManager")) {
                    Alarm alarm = new Alarm();

                    alarm.parse(split[1]);

                    mWakeUpRecord.addAlarm(alarm);
                }
                else if (split[1].contains("CPU1: shutdown")) {
                    mState = State.SLEEP;
                    stopSystemTime = curSystemTime;

                    mWakeUpRecord.mStartTime = (Calendar) startRealTime.clone();

                    stopRealTime = (Calendar) startRealTime.clone();
                    stopRealTime.add(Calendar.MILLISECOND,
                            (int) (stopSystemTime * 1000 - startSystemTime * 1000));

                    mWakeUpRecord.mStopTime = (Calendar) stopRealTime.clone();

                    startRealTime = null;

                    addWakeUpRecord = true;
                }
            }
            else {
                // Start measuring when a wakeup request happens
                // "<6>[   31.371215] Resume caused by IRQ 162, bcmsdh_sdmmc"
                if (split[1].contains("Resume caused by")
                        || split[1].contains("aborted suspend due to")) {
                    mIsStart = true;
                    mState = State.WAKE_UP;
                    startSystemTime = parseSystemTime(split[0]);
                    mWakeUpRecord = new Record(State.WAKE_UP);
                    mWakeUpRecord.addWakeUpReason(WakeUpReason.String2Number(split[1]));
                }
            }
        }
        else {
            LOG.info(TAG + ". Unknown format of line: " + str);
        }

        if (addSleepRecord) {
            return mSleepRecord;
        }
        else if (addWakeUpRecord) {
            return mWakeUpRecord;
        }
        else {
            return null;
        }
    }

    private double parseSystemTime(String str) {
        // An example: <5>[ 31.370300
        double systemTime = 0;

        str = str.replace("<", " ");
        str = str.replace(">", " ");
        str = str.replace("[", " ");

        Scanner scanner = new Scanner(str);
        scanner.nextInt();

        systemTime = scanner.nextDouble();

        return systemTime;
    }

    public String getFilename() {
        return mFilename;
    }

    public ArrayList<Record> getRecords() {
        return mRecords;
    }
}
