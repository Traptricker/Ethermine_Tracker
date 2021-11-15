package com.traptricker;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;

public class SeleniumScrapper {

    private static ChromeDriver driver;

    public SeleniumScrapper(String minerAddress) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        setUpWebdriver(driver, minerAddress);
    }

    private void setUpWebdriver(WebDriver driver, String minerAddress) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        String ethermineURL = String.format("https://ethermine.org/miners/%s/dashboard", minerAddress);
        driver.get(ethermineURL);

        // Waits to make sure the web page has loaded
        Thread.sleep(5000);

        // Turns on auto refreshing for the page
        driver.findElement(By.xpath("//div[@class='slider round']")).click();
    }

    public WebElement getEthermineTable() {
        List<WebElement> searchList = driver.findElements(By.xpath("//div[@class='active table-container']//tbody"));
        // Confirms that there are active miners, then gets each of them (isEmpty doesn't work)
        if (searchList.get(0).getText().split(" ").length > 1) {
            return searchList.get(0);
        }
        return null;
    }

    // Gets the values from the ethermine table
    public Map<String, Map<String, String>> getEthermineData(WebElement ethermineTable) {
        // Turns the table into an array
        String[] search = ethermineTable.getText().split("\n");
        Map<String, Map<String, String>> minerDataDict = new HashMap<>();
        for (String miner : search) {
            // Turns each row in the table into an array
            String[] minerData = miner.split(" ");
            // Turns each row in the table into a Hashmap (Basically a Dictionary)
            Map<String, String> minerHashrates = new HashMap<>();
            minerHashrates.put("Reported Hashrate", minerData[1]);
            minerHashrates.put("Current Hashrate", minerData[2]);
            minerDataDict.put(minerData[0], minerHashrates);
        }
        return minerDataDict;
    }
}
