
package com.ntu.utils;

import com.ntu.common.Record;
import com.ntu.common.State;

import java.util.ArrayList;

public class Statistics {
    ArrayList<Record> mRecords = null;

    public Statistics(ArrayList<Record> records) {
        mRecords = records;
    }

    public static double getMean(ArrayList<Double> data) {
        double sum = 0.;

        for (Double d : data) {
            sum += d;
        }

        if (data == null || data.size() == 0) {
            return 0;
        }
        else {
            return sum / data.size();
        }
    }

    public double getMean(int state, int wakeUpReason) {
        ArrayList<Double> data = new ArrayList<Double>();

        for (Record record : mRecords) {
            if (record.mState == state && state == State.WAKE_UP) {
                if (wakeUpReason == State.DEFAULT) {
                    data.add(record.getDuration());
                }
                else {
                    for (Integer reason : record.mWakeUpReason) {
                        if (reason == wakeUpReason) {
                            data.add(record.getDuration());
                            break;
                        }
                    }
                }
            }
            else if (record.mState == state && state == State.SLEEP) {
                data.add(record.getDuration());
            }
        }

        return Statistics.getMean(data);
    }

    public static double getVariance(ArrayList<Double> data)
    {
        double mean = getMean(data);
        double temp = 0;

        for (Double a : data) {
            temp += (mean - a) * (mean - a);
        }
        if (data == null || data.size() == 0) {
            return 0;
        }
        else {
            return temp / data.size();
        }
    }

    public static double getStdDev(ArrayList<Double> data)
    {
        return Math.sqrt(getVariance(data));
    }

    public double getStdDev(int state, int wakeUpReason) {
        ArrayList<Double> data = new ArrayList<Double>();

        for (Record record : mRecords) {
            if (record.mState == state && state == State.WAKE_UP) {
                if (wakeUpReason == State.DEFAULT) {
                    data.add(record.getDuration());
                }
                else {
                    for (Integer reason : record.mWakeUpReason) {
                        if (reason == wakeUpReason) {
                            data.add(record.getDuration());
                            break;
                        }
                    }
                }
            }
            else if (record.mState == state && state == State.SLEEP) {
                data.add(record.getDuration());
            }
        }

        return Statistics.getStdDev(data);
    }
}
