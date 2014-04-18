
package com.ntu.common;

public class WakeUpReason {
    public static int DEFAULT = -1;
    public static int WIFI_PACKET = 0;
    public static int ALARM_MANAGER = 1;
    public static int GPIO_KEY = 2;

    public static String[] NAME = {
            "WiFiPacket", "AlarmManager", "GpioKey"
    };

    public static int String2Number(String str) {
        int ret = DEFAULT;

        if (str.contains("bcmsdh_sdmmc") || str.contains("IRQ 162")) {
            ret = WIFI_PACKET;
        }
        else if (str.contains("TWL6030-PIH") || str.contains("IRQ 39")) {
            ret = ALARM_MANAGER;
        }
        else if (str.contains("I/O pad") || str.contains("gpio 2")
                || str.contains("gpio_keys") || str.contains("IRQ 163")) {
            ret = GPIO_KEY;
        }

        return ret;
    }
}
