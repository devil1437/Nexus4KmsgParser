
package com.ntu.common;

import com.ntu.alarm.Alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * After parsing, a record represent a wake up time.
 */
public class Record {
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    public Calendar mStartTime = null, mStopTime = null;
    public int mState = State.DEFAULT;
    public ArrayList<Integer> mWakeUpReason = new ArrayList<Integer>();
    public ArrayList<Alarm> mAlarm = new ArrayList<Alarm>();

    public Record(int state) {
        mState = state;
    }

    /**
     * Add a wakeUp number to list.
     * 
     * @param wakeUpNumber
     */
    public void addWakeUpReason(int wakeUpNumber) {
        if (!mWakeUpReason.contains(wakeUpNumber)) {
            mWakeUpReason.add(wakeUpNumber);
        }
    }

    public void addAlarm(Alarm alarm) {
        mAlarm.add(alarm);
    }

    /**
     * Return the time between the two record. The return value is in second.
     * 
     * @param r2
     */
    public double timeDiff(Record r2) {
        if (r2.mStartTime.getTimeInMillis() <= this.mStartTime.getTimeInMillis()) {
            return (this.mStartTime.getTimeInMillis() - r2.mStartTime.getTimeInMillis()) / 1000.;
        }
        else {
            return (r2.mStartTime.getTimeInMillis() - this.mStartTime.getTimeInMillis()) / 1000.;
        }
    }

    public double getDuration() {
        return (mStopTime.getTimeInMillis() - mStartTime.getTimeInMillis()) / 1000.;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("mStartTime ");
        sb.append(DATETIME_FORMAT.format(mStartTime.getTime()));
        sb.append(" mStopTime ");
        sb.append(DATETIME_FORMAT.format(mStopTime.getTime()));
        sb.append(" Duration ");
        sb.append(getDuration());
        sb.append(" State ");
        sb.append(State.NAME[mState]);
        sb.append(" mWakeUpReason");
        for (int i = 0; i < mWakeUpReason.size(); i++) {
            sb.append(" ");
            sb.append(WakeUpReason.NAME[mWakeUpReason.get(i)]);
        }

        for (int i = 0; i < mAlarm.size(); i++) {
            sb.append(" mAlarm" + i + " ");
            sb.append(mAlarm.get(i));
        }
        sb.append("\n");

        return sb.toString();
    }
}
