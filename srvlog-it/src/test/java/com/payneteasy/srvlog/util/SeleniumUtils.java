package com.payneteasy.srvlog.util;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

/**
 * Date: 09.01.13
 */
public class SeleniumUtils {
    private RemoteWebDriver webDriver;
    public static final int MAX_WAIT_TIMEOUT = 20 * 1000; // twenty seconds

    public void initWebDriver() {
        webDriver = new FirefoxDriver(new FirefoxBinary(), null);
        webDriver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
    }

    public void releaseWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    public void initWithLogin() throws InterruptedException {
        initWebDriver();
        doLogin();
    }

    public RemoteWebDriver getWebDriver() {
        return webDriver;
    }

    private void doLogin() throws InterruptedException {
        webDriver.get(url(""));

        WebElement userNameElement = waitTillElementExists(By.name("j_username"));
        userNameElement.sendKeys("admin");

        WebElement passwordElement = waitTillElementExists(By.name("j_password"));
        passwordElement.sendKeys("admin");

        clickElementWhenVisible(By.xpath("//input[@type='submit']"));

    }

    public String url(String srvlogUiUri) {
        StringBuffer buf = new StringBuffer();
        buf.append("http://").append(WebContainerUtils.DOMAIN).append(":").append(WebContainerUtils.PORT)
                .append(WebContainerUtils.CONTEXT).append(srvlogUiUri);
        return buf.toString();
    }

    private boolean elementExists(By by) {
        try {
            webDriver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private WebElement waitTillElementExists(final By by) throws InterruptedException {
        waitTillConditionIsTrue(new Condition() {
            @Override
            public boolean isTrue() {
                return elementExists(by);
            }
        });
        return webDriver.findElement(by);
    }

    private void waitTillConditionIsTrue(Condition condition) throws InterruptedException {
        long started = System.currentTimeMillis();
        boolean stillRun;
        do {
            stillRun = !condition.isTrue();
            if (stillRun) {
                Thread.sleep(100);
            }
            if (System.currentTimeMillis() - started > MAX_WAIT_TIMEOUT) {
                throw new IllegalStateException("Waited too long for condition");
            }
        } while (stillRun);
    }

    private void clickElementWhenVisible(final By by) throws InterruptedException {
        waitTillConditionIsTrue(new Condition() {
            @Override
            public boolean isTrue() {
                try {
                    webDriver.findElement(by).click();
                    return true;
                } catch (ElementNotVisibleException e) {
                    return false;
                }
            }
        });
    }

    private static interface Condition {
        boolean isTrue();
    }
}
