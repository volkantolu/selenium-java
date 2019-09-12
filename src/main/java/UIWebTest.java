import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class UIWebTest {

    public WebDriver getBrowser(String browserType) {

        WebDriver driver = null;
        try {
            if (browserType.equals("chrome")) {
                // https://sites.google.com/a/chromium.org/chromedriver/downloads
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver_win32/chromedriver.exe");
                driver = new ChromeDriver();
            } else {
                // generic driver
                // https://sites.google.com/a/chromium.org/chromedriver/downloads
                // "/development/test/automation/selenium/webdrivers/chromeDriver/chromedriver.exe"
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver_win32/chromedriver.exe");
                driver = new ChromeDriver();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return driver;
    }

    private void navigateToUrl(WebDriver driver, String url) {
        try {
            driver.get(url);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public void searchBarSuggestionListControl(WebDriver driver, String searchKeyword) {
        System.out.println("Test Case: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int timeOutLimit = 5;
        WebDriverWait wait = new WebDriverWait(driver, timeOutLimit);
        try {
            WebElement productSearch = driver.findElement(By.id("search-term"));

            //productSearch.clear();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementById('search-term').value=''");

            List<WebElement> autocompleteSuggestions = driver
                    .findElements(By.cssSelector(".FullscreenSearch__results"));
            int before = autocompleteSuggestions.size();
            System.out.println("Before Search: " + before);

            productSearch.sendKeys(searchKeyword);

            Thread.sleep(2000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"FullscreenSearch__results\"]")));

            autocompleteSuggestions = driver.findElements(By.xpath("//div[@class=\"FullscreenSearch__results\"]"));
            int after = autocompleteSuggestions.size();
            System.out.println("After Search: " + after);

            if (before != after) {
                System.out.println("SUCCESS");
            } else if (after == 1){
                if ("No results found".equals(autocompleteSuggestions.get(0).getText())) {
                    System.out.println("No results found");
                } else {
                    System.out.println("Message at suggestion area: " + autocompleteSuggestions.get(0).getText());
                }
            }
            else{
                System.out.println("No result found for search keyword");
            }
        } catch (Exception e) {
            System.out.println("TEST CASE FAILED");
            // e.printStackTrace();
        }

    }

    private void singleClickShoppingLinkControl(WebDriver driver) {
        try {
            System.out.println("Test Case: " + Thread.currentThread().getStackTrace()[1].getMethodName());

            List<WebElement> hrefElements = driver.findElements(By.tagName("a"));
            String navigationUrl = "";

            for (WebElement we : hrefElements) {
                if (we.getText().equals("Home")) {
                    // wait.until(ExpectedConditions.elementToBeClickable(we));
                    navigationUrl = we.getAttribute("href");
                    we.click();
                    break;
                }
            }

            String currentUrl = driver.getCurrentUrl();

            if (currentUrl.equals(navigationUrl)) {
                System.out.println("SUCCESS");
            } else {
                System.out.println("FAIL");
            }

        } catch (Exception e) {
            System.out.println("TEST CASE FAILED");
            // e.printStackTrace();

        }

    }

    public static void main(String[] args) {

        UIWebTest uiWebTest = new UIWebTest();
        String urlPrefix = "https://www.rottentomatoes.com/";
        String pageURL = "guide/best-tv-shows-and-movies-original-to-amazon-prime-video/";
        String browserType = "chrome";
        String searchKeyword = "";
        WebDriver browser = null;

        try {
            browser = uiWebTest.getBrowser(browserType);
            uiWebTest.navigateToUrl(browser, urlPrefix);

            WebElement bestOfAmazonPrimeLink = browser.findElement(By.xpath("//a[text()=\" Best of Amazon Prime \"]"));
            bestOfAmazonPrimeLink.click();
            Thread.sleep(2000);

            searchKeyword = "xyzxyz";
            uiWebTest.searchBarSuggestionListControl(browser, searchKeyword);

            searchKeyword = "fleabag";
            Thread.sleep(2000);
            uiWebTest.searchBarSuggestionListControl(browser, searchKeyword);

            searchKeyword = "#%&";
            Thread.sleep(2000);
            uiWebTest.searchBarSuggestionListControl(browser, searchKeyword);

            uiWebTest.singleClickShoppingLinkControl(browser);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            browser.close();
            System.exit(0);
        }

        System.out.println("Hello World");

    }
}
