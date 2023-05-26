package com.rozetka.util.logger;

import com.rozetka.util.Config;
import com.rozetka.util.net.HttpHelper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RemoteLogger implements ILogger {

    private final static org.slf4j.Logger syslog = org.slf4j.LoggerFactory.getLogger(RemoteLogger.class);

    public static String SUITE_ID;
    WebDriver driver;
    String testCaseName;
    String testCaseUrl;
    String hosterUrl;
    String remoteLoggerUrl;
    int testId = -1;

    static {
        SUITE_ID = UUID.randomUUID().toString();
        syslog.debug("----------------------------------------");
        syslog.debug("Run tests. SUITE_ID : " + SUITE_ID);
    }

    public RemoteLogger(WebDriver driver, String testCaseName, String testCaseUrl, String hosterUrl) {
        this.driver = driver;
        this.testCaseName = testCaseName;
        this.testCaseUrl = testCaseUrl;
        this.hosterUrl = hosterUrl;
        remoteLoggerUrl = Config.get("remoteLoggerUrl");
    }

    private boolean startIfNeed() {
        if (testId > 0) {
            return true;
        }

        syslog.debug("Try to start test [" + testCaseName + "].");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", testCaseName));
        params.add(new BasicNameValuePair("suiteId", SUITE_ID));
        params.add(new BasicNameValuePair("hoster", hosterUrl));

        String url = remoteLoggerUrl + "/test/start";
        String response = HttpHelper.sendGet(url, params);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.getInt("result") == 0) {
                JSONObject respObject = new JSONObject(jsonObject.getString("response"));
                testId = respObject.getInt("id");
                syslog.debug("Start test [" + testCaseName + "] - SUCCESS.");
            }
        } catch (JSONException e) {
            syslog.error("Start test [" + testCaseName + "] - FAILED.");
        }

        return testId > 0;
    }

    @Override
    public void stop(String status) {
        stop("", "", status);
    }

    @Override
    public void stop(String message, String error, String status) {
        if (!startIfNeed()) {
            syslog.error("Can't start test");
            return;
        }

        syslog.debug("Stop test [" + testCaseName + "].");
        String url = remoteLoggerUrl + "/test/finish";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("testId", String.valueOf(testId)));
        params.add(new BasicNameValuePair("message", message));
        params.add(new BasicNameValuePair("error", error));
        params.add(new BasicNameValuePair("status", status));
        HttpHelper.sendGet(url, params);
    }

    @Override
    public void step(String name, String message, String error, String status, long duration) {
        if (!startIfNeed()) {
            syslog.error("Can't start test");
            return;
        }

        String url = remoteLoggerUrl + "/step/add";
        File screenshot = null;
        if (status.equals(Status.FAIL) || status.equals(Status.INFO)) {
            screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        }

        MultipartEntityBuilder stepEntityBuilder = MultipartEntityBuilder.create();
        stepEntityBuilder.addPart("testId", new StringBody(String.valueOf(testId), ContentType.TEXT_PLAIN))
                .addPart("name", new StringBody(name, ContentType.TEXT_PLAIN))
                .addPart("message", new StringBody(message, ContentType.TEXT_PLAIN))
                .addPart("error", new StringBody(error, ContentType.TEXT_PLAIN))
                .addPart("status", new StringBody(status, ContentType.TEXT_PLAIN))
                .addPart("duration", new StringBody(String.valueOf(duration), ContentType.TEXT_PLAIN));

        if (screenshot != null) {
            stepEntityBuilder.addPart("file", new FileBody(screenshot));
        }
        syslog.debug("Adding steps.");
        HttpEntity stepEntity = stepEntityBuilder.build();
        HttpHelper.sendMultipartFormPost(url, stepEntity);
    }

    @Override
    public void stepOk(String name) {
        step(name, "", "", Status.OK, 0l);
    }

    @Override
    public void stepOk(String name, String message) {
        step(name, message, "", Status.OK, 0l);
    }

    @Override
    public void stepOk(String name, String message, long duration) {
        step(name, "", "", Status.OK, duration);
    }

    @Override
    public void stepFail(String name, String error) {
        step(name, "", error, Status.FAIL, 0l);
    }

    @Override
    public void stepFail(String name, String error, long duration) {
        step(name, "", error, Status.FAIL, duration);
    }

    @Override
    public void stepWarn(String name, String message) {
        step(name, message, "", Status.WARN, 0l);
    }

    @Override
    public void stepWarn(String name, String message, Long duration) {
        step(name, message, "", Status.WARN, duration);
    }

    @Override
    public void stepInfo(String message) {
        step("INFO_STEP", message, "", Status.INFO, 0l);
    }

    @Override
    public void stepInfo(String name, String message) {
        step(name, message, "", Status.INFO, 0l);
    }
}