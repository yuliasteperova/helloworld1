package com.rozetka.util;

import org.slf4j.*;

import java.util.concurrent.TimeUnit;

public class Delay {
    private final static org.slf4j.Logger syslog = LoggerFactory.getLogger(Config.class);

	public static void shortDelay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            syslog.error("Error during shortDelay: ", e);
        }
	}

	public static void longDelay() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            syslog.error("Error during longDelay: ", e);
        }
	}

	public static void midDelay() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            syslog.error("Error during midDelay: ", e);
        }
	}

    public static void delay(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            syslog.error("Error during delay", e);
        }
    }
}
