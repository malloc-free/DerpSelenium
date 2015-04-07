/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DerpSelenium;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author michael
 */
public class test {
    public static void main(String[] args) throws InterruptedException {
        if(args.length != 2) {
            print("Required args: username password");
            System.exit(-1);
        }
        
        loginMega(args[0], args[1]);
    }
    
    public static void loginMega(String username, String passwd) throws InterruptedException {
        FirefoxProfile profile = new FirefoxProfile();
        WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("/home/michael/bin/firefox")), profile);
        driver.get("http://www.mega.nz");
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>(){
            @Override
            public Boolean apply(WebDriver d) {
                try {
                    WebElement loginButton = 
                            d.findElement(By.className("top-login-button"));
                    System.out.println(loginButton.getText());
                }
                catch(NoSuchElementException e) {
                    return false;
                }
                    
                return true;
            }
        });
        
        WebElement loginButton = 
                driver.findElement(By.xpath("//a[@class='top-login-button hidden']"));
        loginButton.click();
        WebElement nameElement = driver.findElement(By.name("login-name"));
        WebElement pwElement = driver.findElement(By.name("login-password"));
        nameElement.sendKeys(username);
        pwElement.sendKeys(passwd);
        WebElement submitButton = driver.findElement(By.className("top-dialog-login-button"));     
        submitButton.click();
        
        Thread.sleep(15000);
  
        String url = driver.getCurrentUrl();
        List<WebElement> elements = driver.findElements(By.tagName("div"));
        Map<String, WebElement> elementMap = new HashMap<>();
        for(Iterator<WebElement> it = elements.iterator(); it.hasNext();) {
            WebElement e = it.next();
            try {
                if(!e.isDisplayed() || !e.isEnabled() || e.getAttribute("class").equals("")) {
                    it.remove();
                }
                else {
                    elementMap.put(e.getAttribute("class"), e);
                }
            }
            catch(StaleElementReferenceException ex) {
                it.remove();
            }
        
        }
        
        print("Number of added elements = " + elements.size());
         
        List<WebElement> contacts = 
                driver.findElements(By.xpath("//div[@class='nw-fm-left-icon contacts ui-droppable']"));
        
        contacts.get(1).click();
        Thread.sleep(2000);
        //driver.navigate().back();
        //Thread.sleep(2000);
        WebElement el = elementMap.get("nw-fm-left-icon shared-with-me ui-droppable");
        if(el == null) {
            print("Element not found");
        }
        else {
            el.click();
        }
        
    }
    
    public static void print(String toPrint) {
        System.out.println(toPrint);
    }
    
    public static void info(String name, WebElement element) {
        print("Element name = " + name);
        print("Element.isDisplayed = " + element.isDisplayed());
        print("Element.isEnabled = " + element.isEnabled());
        print("Element.isSeleceted = " + element.isSelected());

    }
}
