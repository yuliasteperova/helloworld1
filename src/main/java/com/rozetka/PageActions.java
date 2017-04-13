package com.rozetka;

import com.rozetka.util.Action;
import com.rozetka.util.Delay;
import com.rozetka.util.logger.ILogger;

/**
 * Created by julia on 12.04.17.
 */
public class PageActions {
    private ILogger logger;
    private Action action;
    private String currentUrl;

    public PageActions(Action action, ILogger logger) {
        this.action = action;
        this.logger = logger;
    }

    public void navigateToSubCategory(String categoryXpath, String subCategoryXpath, String subCategoryName) {
        action.click(categoryXpath);
        action.click(subCategoryXpath);
        Delay.midDelay();

        logger.stepInfo("navigateTo", "Navigate to " + subCategoryName);
    }

    public boolean checkIfCategoryIsLoaded(String categoryUrl, String categoryName) {
        currentUrl = action.getCurrentUrl();

        if (!currentUrl.contains(categoryUrl)) {
            logger.stepFail("pageLoad", "It's not a Smartphone category");
            return false;
        } else {
            logger.stepInfo("pageLoad", categoryName + " category is loaded.");
            return true;
        }
    }

    public boolean checkIfElementIsPresent(String elementXpath, String elementName) {
        if (!action.isElementPresent(elementXpath)){
            logger.stepFail("checkElement", elementName + " is not present.");
            return false;
        } else {
            logger.stepOk("checkElement", elementName + " is present.");
            return true;
        }
    }

    public void clickOnButton(String buttonXpath, String buttonName) {
        if (!action.click(buttonXpath)) {
            logger.stepFail("clickOnButton", "Can't click on <b>" + buttonName + "</b> button.");
        } else {
            logger.stepOk("clickOnButton", "Click on <b>" + buttonName + "</b> button.");
        }
    }

    public String getProductName(String productNameXpath, int productCountInList) {
        return  action.getElementFromList(productNameXpath, productCountInList).getText();
    }

    public String getProductLink(String productLinkXpath, int productCountInList) {
        return action.getElementFromList(productLinkXpath, productCountInList).getAttribute("href");
    }

    public String getProductPrice(String productPriceXpath, int productCountInList) {
        return action.getElementFromList(productPriceXpath, productCountInList).getText();
    }

    public Integer getProductsQty(String productsListXpath) {
        Integer productsQty = action.getQuantityOfElementsInList(productsListXpath);

        logger.stepOk("checkQty", "There are " + productsQty.toString() + " products on page ");
        return productsQty;
    }

    public void navigateThroughPages(int pageNumber) {
        action.click("//li[@id='page" + pageNumber + "']");
    }
}