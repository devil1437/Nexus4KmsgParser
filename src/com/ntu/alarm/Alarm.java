
package com.ntu.alarm;

import java.util.Scanner;
import java.util.logging.Logger;

public class Alarm {
    private static final String TAG = "Alarm";

    private static final Logger LOG = Logger.getLogger(TAG);

    public int type;
    public long when;
    public String creatorPackageName;
    public int creatorUid;

    public Alarm() {
        when = 0;
        creatorPackageName = null;
        creatorUid = 0;
    }

    public void parse(String str) {
        // An alarm log example:
        // "V/AlarmManager(  399): Time 1397058744 MyLogcat MyAlarmManager Alarm Alarm{41906b20 type 0 jp.naver.line.android 10071}"
        Scanner scanner = new Scanner(str);
        int state = 0;

        while (scanner.hasNext()) {
            String data = scanner.next();

            if (data.equals("Time") && state == 0) {
                when = scanner.nextLong();
                state = 1;
                LOG.info("When " + when);
            }
            else if (data.equals("type") && state == 1) {
                type = scanner.nextInt();
                LOG.info("Type " + type);

                creatorPackageName = scanner.next();
                LOG.info("CreatorPackageName " + creatorPackageName);

                String str2 = scanner.next();
                // Extract digit
                str2 = str2.replaceAll("\\D+", "");
                creatorUid = Integer.parseInt(str2);
                LOG.info("CreatorUid " + creatorUid);
                state = 2;
            }

            if (state == 2) {
                break;
            }
        }
    }

    public String getId() {
        return creatorPackageName + creatorUid;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(128);
        sb.append("type ");
        sb.append(type);
        sb.append(" creatorPackageName ");
        sb.append(creatorPackageName);
        sb.append(" creatorUid ");
        sb.append(creatorUid);
        return sb.toString();
    }
}
