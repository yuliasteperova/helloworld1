package com.rozetka.util.logger;

public interface ILogger {

    public void stop(String status);

    public void stop(String message, String error, String status);

    public void step(String name, String message, String error, String status, long duration);

    public void stepOk(String name);

    public void stepOk(String name, String message);

    public void stepOk(String name, String message, long duration);

    public void stepFail(String name, String error);

    public void stepFail(String name, String error, long duration);

    public void stepWarn(String name, String message);

    public void stepWarn(String name, String message, Long duration);

    public void stepInfo(String message);

    public void stepInfo(String name, String message);
}
