package com.rozetka.util;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class Action {
    public WebDriver driver;

    public Action(WebDriver driver) {
        this.driver = driver;
    }

    public boolean click(String xpath) {
        try {
            WebElement webElement = driver.findElement(By.xpath(xpath));
            if (webElement == null) {
                return false;
            }
            webElement.click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean doubleClick(String xpath) {
        try {
            Actions builder = new Actions(driver);
            WebElement webElement = driver.findElement(By.xpath(xpath));
            if (webElement == null) {
                return false;
            }
            builder.moveToElement(webElement).doubleClick().build().perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean input(String inputFieldXpath, String data) {
        try {
            WebElement webElement = driver.findElement(By.xpath(inputFieldXpath));
            if (webElement == null) {
                return false;
            }
            webElement.sendKeys(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementPresent(String elementXpath) {
        try {
            return driver.findElement(By.xpath(elementXpath)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void loadPage(String urlXpath) {
        driver.get(urlXpath);
        Delay.longDelay();
    }

    public boolean elementIsNotPresent(String xpath) {
        try {
            driver.findElements(By.xpath(xpath)).isEmpty();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement getElementFromList(String elementsXpath, int elementNumber) {
        try {
            List<WebElement> elements = driver.findElements(By.xpath(elementsXpath));
            if (elements == null) {
                return null;
            }
            return elements.get(elementNumber - 1);
        } catch (Exception e) {
            return null;
        }
    }

    public int getQuantityOfElementsInList(String elementsXpath) {
        try {
            List<WebElement> elements = driver.findElements(By.xpath(elementsXpath));
            if (elements == null) {
                return 0;
            }

            return elements.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void refreshPage() {
        driver.navigate().refresh();
        Delay.delay(10);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}