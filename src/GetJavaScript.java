import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetJavaScript {
private static ArrayList<String> classes= new ArrayList<String>();
	public static void main(String[] args) {

		System.setProperty("webdriver.chrome.driver", "C:\\Users\\sam\\Desktop\\chromedriver_win32\\chromedriver.exe");
		// Open a new browser window
		WebDriver driver = new ChromeDriver();
		ArrayList<WebElement> languagesNamesList = new ArrayList<WebElement>();

		driver.navigate().to("http://shs.touro.edu/programs/nursing/bs-course-sequence/");

		// Click the "ABOUT" button (after it's loaded)

		waitForLoad(driver);
		// Copy a list of all the languages (after they're loaded)
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(languagesParagraphXpath)));
		languagesNamesList.addAll(driver.findElements(By.tagName("td")));
		String languageName = "";
		// Print the languages
		for (WebElement languageElement : languagesNamesList) {
			
				languageName = languageElement.getText();
				 if(languageName.matches("(\\D{4})\\s(\\d{3})\\s-(\\s\\D*)")) {
				classes.add(languageName);
			}
		}

		// Close the browser window
		driver.close();

	}

	public static void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(pageLoadCondition);
	}
	public ArrayList<String> getClasses(){
		return classes;
	}
}
