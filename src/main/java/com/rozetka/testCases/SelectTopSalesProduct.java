package com.rozetka.testCases;

import com.rozetka.AbstractTestCase;
import com.rozetka.PageActions;
import com.rozetka.util.Action;
import com.rozetka.util.Config;
import com.rozetka.util.logger.LoggerFactory;
import com.rozetka.util.logger.Status;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by julia on 12.04.17.
 */

public class SelectTopSalesProduct extends AbstractTestCase {
    public static final String HOSTER_URL = Config.get("hosterUrl");
    public static String status = Status.OK;

    @Test(groups = {"selectTopSaleProduct"})
    public void testSelectTopSalesProduct() throws IOException {
        final String testCaseName = "Select top sales product";
        final String categoryName = "Phones, TV and Electronics";
        final String subCategoryFirstLevelName = "Phones";
        final String subCategorySecondLevelName = "SmartPhones";
        final int pagesQtyForCheckingProduct = 3;

        status = Status.OK;
        LoggerFactory logger = new LoggerFactory(driver, testCaseName, HOSTER_URL);

        action = new Action(driver);
        pageActions = new PageActions(action, logger);

        try {
            action.loadPage(HOSTER_URL);
            logger.stepInfo("Load " + HOSTER_URL);

            pageActions.navigateToCategory(resource.get(CATEGORIES, "smartponesTvAndElectronics"), categoryName);
            pageActions.navigateToCategory(resource.get(CATEGORIES, "firstLevelSubCategoryPhones"), subCategoryFirstLevelName);
            pageActions.navigateToCategory(resource.get(CATEGORIES, "subCategorySmartphones"), subCategorySecondLevelName);

            // -- check that Smartphones category is loaded (check current url), if no - break test;
            if (!pageActions.checkIfCategoryIsLoaded(resource.get(URL, "smartPhoneCategory"), subCategorySecondLevelName)) {
                throw new Exception();
            } else {
                //else check if title of subcategory is present
                pageActions.checkIfElementIsPresent(resource.get(SMARTPHONE_CATEGORY, "smartPhoneCategoryTitle"), "\'All smartphones\' title");
            }

            for (int i=1; i<=pagesQtyForCheckingProduct;) {
                logger.stepOk("getProducts", "Get top sales products from page " + i);
                Integer salesQty = pageActions.getProductsQty(resource.get(SMARTPHONE_CATEGORY, "productsWithTopSalesLabel"));

                int j = 1;
                for (;j <= salesQty; j++) {
                    String link = pageActions.getProductLink(resource.get(SMARTPHONE_CATEGORY, "topSalesProductLink"), j);
                    String name = pageActions.getProductName(resource.get(SMARTPHONE_CATEGORY, "topSalesProductName"), j);
                    String price = pageActions.getProductPrice(resource.get(SMARTPHONE_CATEGORY, "topSalesProductPrice"), j);
                    logger.stepOk("info","Link: " + link + "<br> Name: " + name + "<br> Price: " + price);
                }

                i++;
                pageActions.navigateThroughPages(i);
            }
        } catch (Exception e) {
            status = Status.FAIL;
        } finally {
            logger.stepInfo("RESULT", "Test " + testCaseName + " " + status);
            logger.stop(status);
            WRITER.append("<tr><td>" + testCaseName + "</td><td>" + status + "</td></tr>");
        }
    }
}