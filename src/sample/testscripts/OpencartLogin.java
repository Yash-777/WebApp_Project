package sample.testscripts;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.gridlauncher.GRIDINFO;

/**
 * https://demo.opencart.com/index.php?route=product/product&path=25_28&product_id=42
 * 
 * https://demo.opencart.com/index.php?route=product/product&product_id=41
 * 
 * @author yashwanth.m
 *
 */
public class OpencartLogin {
	public RemoteWebDriver driver;
	//public WebDriver driver;

	String appURL = "http://demo.opencart.com/index.php?route=account/login";
	
	String userName = "//*[@id='input-email']";
	String userKey = "yashwanth.merugu@gmail.com";
	
	String password = "//*[@id='input-password']";
	String secretKey = "MySecretPassword";
	
	String login = "//*[@id='content']/div/div[2]/div/form/input";
	
	String verify = "/html/body/div[2]/div[1]";
	String verifyText = 
			//"Warning: No match for E-Mail Address and/or Password.";
			"Warning: Your account has exceeded allowed number of login attempts. Please try again in 1 hour.";
	
	public static void main(String[] args) {
		try {
			OpencartLogin test = new OpencartLogin();

			URL url = 
					new URL( String.format("http://%s:4444/wd/hub", GRIDINFO.HOSTIP.toString() ));
			DesiredCapabilities caps_IE = DesiredCapabilities.internetExplorer();
			caps_IE.setVersion("11");
			caps_IE.setPlatform(Platform.WINDOWS);
			
			DesiredCapabilities caps_CH = DesiredCapabilities.chrome();
			caps_CH.setVersion("54.0");
			caps_CH.setPlatform(Platform.WINDOWS);
			
			DesiredCapabilities caps_FF = DesiredCapabilities.firefox();
			caps_FF.setVersion("39.0");
			caps_FF.setPlatform(Platform.WINDOWS);
			caps_FF.setCapability("name","YashTest2");
			
			DesiredCapabilities caps = DesiredCapabilities.firefox();
			caps.setCapability("platform","win7");
			caps.setCapability("version","39");
			caps.setCapability("secnarioname","Yash_W7_FF39_1");
			
			RemoteWebDriver driver = 
							//new FirefoxDriver();
							//new RemoteWebDriver(url, caps);
					new RemoteWebDriver(new URL("http://testuser:f5d4d6a5-dc12-4c96-8563-e7241f9f732a@predev.clictest.com:9091/ClicHub/wd/hub"),caps);
			
			test.loginTest( driver );
			test.quitDriver();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (org.openqa.selenium.UnsupportedCommandException e) {
			System.err.println("HTTP Status 403 - Access to the requested resource has been denied | forbidden");
			System.err.println("Wrong Credentials. "+e.getMessage());
		}
	}
	
	public void loginTest( RemoteWebDriver driver ) {
		try {
			this.driver = driver;
			
			driver.get( appURL );
			
			/* Put an Implicit wait, this means that any search for elements on the page could take 
			the time the implicit wait is set for before throwing exception.
			http://toolsqa.com/selenium-webdriver/switch-commands/ */
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			
			// wait up to 10 seconds for the Codes detail page to load
			(new WebDriverWait(driver, 10)).until( new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					String title = driver.getTitle();
					System.out.println("Application Title : "+title);
					return title.equals("Account Login");
				}
			});
			
			WebElement user = driver.findElement(By.xpath( userName ));
			user.sendKeys(userKey);
			
			WebElement secret = driver.findElement(By.xpath( password ));
			secret.sendKeys(secretKey);
			
			WebElement loginbutton = driver.findElement(By.xpath( login ));
			loginbutton.click();
			
			WebElement data = driver.findElement(By.xpath( verify ));
			String text = data.getText();
			System.out.println("Text My Order ["+text+"]");
			
			if( text.equalsIgnoreCase( verifyText ) ) {
				drawBorder(verify, "green");
			} else {
				drawBorder(verify, "red");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void quitDriver() {
		driver.close();
		driver.quit();
	}
	/** 
	 * To highlight an element, it takes XPath and draw border around it.
	 * 
	 * @param xpath
	 * @param color the color around 
	 */
	public void drawBorder(String xpath, String color){
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		WebElement element_node = driver.findElement(By.xpath(xpath));
		String elementStyle = "arguments[0].style.border='3px solid "+color+"'";
		if (driver.getClass().getName().contains("ie")) {
			jse.executeScript(elementStyle, element_node);
		}else {
			try {
				jse.executeScript(
				"if (document.evaluate){"
					+ "var element_node = document.evaluate('"+xpath+"', window.document, null, 9, null ).singleNodeValue;"
					+ "element_node.style.setProperty ('border', '3px solid "+color+"', 'important');"
				+"}"
					);
			} catch (Exception draw) {
				jse.executeScript(elementStyle, element_node);
			}
		}
	}
}