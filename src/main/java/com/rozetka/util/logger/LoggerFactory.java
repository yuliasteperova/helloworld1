package com.rozetka.util.logger;

import org.openqa.selenium.WebDriver;

import java.util.LinkedList;
import java.util.List;

public class LoggerFactory implements ILogger {

    private List<ILogger> loggers;
    String testCaseName;
    String testCaseUrl;
    String hosterUrl;

    public LoggerFactory(WebDriver driver, String testCaseName, String hosterUrl) {
        this.testCaseName = testCaseName;
        this.hosterUrl = hosterUrl;

        loggers = new LinkedList<>();
        ILogger rl = new RemoteLogger(driver, testCaseName, testCaseUrl, hosterUrl);
        loggers.add(rl);
    }

    @Override
    public void stop(String status) {
        for (ILogger logger : loggers) {
            logger.stop(status);
        }
    }

    @Override
    public void stop(String message, String error, String status) {
        for (ILogger logger : loggers) {
            logger.stop(message, error, status);
        }
    }

    @Override
    public void step(String name, String message, String error, String status, long duration) {
        for (ILogger logger : loggers) {
            logger.step(name, message, error, status, duration);
        }
    }

    @Override
    public void stepOk(String name) {
        for (ILogger logger : loggers) {
            logger.stepOk(name);
        }
    }

    @Override
    public void stepOk(String name, String message) {
        for (ILogger logger : loggers) {
            logger.stepOk(name, message);
        }
    }

    @Override
    public void stepOk(String name, String message, long duration) {
        for (ILogger logger : loggers) {
            logger.stepOk(name, message, duration);
        }
    }

    @Override
    public void stepFail(String name, String error) {
        for (ILogger logger : loggers) {
            logger.stepFail(name, error);
        }
    }

    @Override
    public void stepFail(String name, String error, long duration) {
        for (ILogger logger : loggers) {
            logger.stepFail(name, error, duration);
        }
    }

    @Override
    public void stepWarn(String name, String message) {
        for (ILogger logger : loggers) {
            logger.stepWarn(name, message);
        }
    }

    @Override
    public void stepWarn(String name, String message, Long duration) {
        for (ILogger logger : loggers) {
            logger.stepWarn(name, message, duration);
        }
    }

    @Override
    public void stepInfo(String message) {
        for (ILogger logger : loggers) {
            logger.stepInfo(message);
        }
    }

    @Override
    public void stepInfo(String name, String message) {
        for (ILogger logger : loggers) {
            logger.stepInfo(name, message);
        }
    }
}