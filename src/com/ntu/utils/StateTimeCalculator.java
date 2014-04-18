
package com.ntu.utils;

import com.ntu.common.Policy;
import com.ntu.common.State;

import java.util.logging.Logger;

public class StateTimeCalculator {
    private static final String TAG = "StateTimeCalculator";

    private static final Logger LOG = Logger.getLogger(TAG);

    private long[][] mStateTime;

    /*
     * Record the total time of each state for one model.
     */
    public StateTimeCalculator() {
        mStateTime = new long[Policy.NUM_POLICY][State.NUM_STATE];
    }

    /*
     * Add time to specified state.
     */
    public void addTime(int policyNumber, int state, int time) {
        mStateTime[policyNumber][state] += time;
    }

    /*
     * Add time to specified state.
     */
    public void addTime(int policyNumber, int state, long time) {
        mStateTime[policyNumber][state] += time;
    }

    /*
     * Get the time of specified state.
     */
    public long getStateTime(int policyNumber, int state) {
        return mStateTime[policyNumber][state];
    }

    /*
     * Reset time of each state.
     */
    private void resetStateTime() {
        mStateTime = new long[Policy.NUM_POLICY][State.NUM_STATE];
    }

    /*
     * Reset time to current time and time of each state(for phone).
     */
    public void reset() {
        resetStateTime();
    }

    /*
     * Reset time of specified state.
     */
    public void resetStateTime(int policyNumber, int state) {
        mStateTime[policyNumber][state] = 0;
    }

    public String toString(int policyNumber) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < State.NUM_STATE; i++) {
            sb.append(State.NAME[i]);
            sb.append(" ");
            sb.append(mStateTime[policyNumber][i]);
            sb.append(" ");
        }

        return sb.toString();
    }
}
