import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.management.OperatingSystemMXBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	private static Document mainDoc = null;
	private static HashSet<String> emails = new HashSet<String>();
	private static HashSet<String> coloredURL = new HashSet<String>();
	private static Queue<String> URL = new LinkedList<String>();
	private static HashSet<String> Classes = new HashSet<String>();
	private static LinkedList<String> deadLinks = new LinkedList<String>();
	private static java.net.URL url;
	private static Elements links;
	private static String urlString = "";
	private static int counter;
	private static PrimsMST graph = new PrimsMST();
	// private static String Class = "([a-zA-Z]{3,4})\\s(\\d{3})(?:\\s-)?(\\s\\D*)";
	private static Pattern CLASS = Pattern.compile("([a-zA-Z]{3,4})\\s(\\d{3})(?:\\s-)?(\\s\\D*)");
	private static int graphSize;

	public Scraper(String url, int amt) {
		URL.add(url);
		counter = amt;

		// for(String url : URL)
		// System.out.println(url);

		// for(string url:URL)
	}

	public void executeMST(int graphAmt) {
		while (coloredURL.size() < graphAmt) {
			addGraphVertex();
		}
	}

	public boolean execute() {
		System.out.println("in execute");
		Document doc = null;
		synchronized (URL) {
			while (coloredURL.contains(URL.peek()))
				URL.remove();

			try {
				counter--;

				urlString = URL.remove();
				// getJavaScript(urlString);
				// System.out.println(urlString);

				coloredURL.add(urlString);
				url = new java.net.URL(urlString);
				doc = Jsoup.parse(url, 20000);
				getURL(doc);

			} catch (IOException e) {
				System.out.println("hu");
				if (!URL.isEmpty())
					deadLinks.add(URL.remove());

			}

		}
		if (doc != null) {
			String title = doc.title();

			// links = doc.select("a[href]");

			// System.out.println(doc.text());
			// System.out.println(doc.toString());

			// HashSet<Element> visited = new HashSet<Element>();
			// for (Element link : links) {
			// visited.add(link);
			// System.out.println(link.text());
			System.out.println("Getting " + counter + ": " + title);

			lookForClasses(doc);
			// getInternalLinks();

			getEmails(doc);

			// getClasses();
			coloredURL.add(urlString);
		}
		if (counter == 1)
			return true;
		else
			return false;
	}

	public static void getURL(Document doc2) {

		Pattern p = Pattern.compile(
				"https?:\\/\\/(www\\.)?([-a-zA-Z0-9@:%._\\+~#=]{2,256})?touro([-a-zA-Z0-9@:%._\\\\+~#=]{2,256})?\\.edu\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		Matcher matcher = p.matcher(doc2.toString());

		while (matcher.find()) {
			if (!matcher.group().contains(".png") && !coloredURL.contains(matcher.group()))
				URL.add(matcher.group());
		}

		getInternalLinks(doc2);
	}

	public static boolean checkWebsite(Document doc) {
		boolean check = false;
		Pattern p = Pattern.compile("[a-zA-Z]{3,4}\\|\\d{3}");
		Matcher matcher = p.matcher(doc.toString());

		if (matcher.find()) {
			check = true;
		}
		return check;
	}

	public static void lookForClasses(Document doc) {
		checkHTML(doc);
		if (checkWebsite(doc))
			getJavaScript(urlString);
	}

	public static void checkHTML(Document doc) {

		Pattern p = Pattern.compile("([a-zA-Z]{3,4})\\s(\\d{3})\\s-(\\s\\D*)");
		Matcher matcher = p.matcher(doc.toString());

		while (matcher.find()) {
			Classes.add(matcher.group());
		}

	}

	public static void getEmails(Document doc2) {
		Pattern p = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}");
		Matcher matcher = p.matcher(doc2.toString());

		while (matcher.find()) {
			emails.add(matcher.group());

		}
	}

	public static void getJavaScript(String webAddress) {

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\sam\\Desktop\\chromedriver_win32\\chromedriver74.exe");
		// Open a new browser window
		WebDriver driver = new ChromeDriver();
		ArrayList<WebElement> languagesNamesList = new ArrayList<WebElement>();

		driver.navigate().to(webAddress);

		// Click the "ABOUT" button (after it's loaded)

		waitForLoad(driver);
		// Copy a list of all the languages (after they're loaded)
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(languagesParagraphXpath)));
		languagesNamesList.addAll(driver.findElements(By.tagName("td")));
		String languageName = "";
		// Print the languages
		for (WebElement languageElement : languagesNamesList) {

			languageName = languageElement.getText();
			if (languageName.matches("([a-zA-Z]{3,4})\\s(\\d{3})\\s-(\\s\\D*)")) {
				Classes.add(languageName);
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

	public static void getInternalLinks(Document doc2) {
		Elements links = doc2.select("a");

		for (Element link : links) {
			String absHref = link.attr("abs:href"); // "http://jsoup.org/"
			if (!absHref.contains(".png") && !coloredURL.contains(absHref) && absHref.matches(
					"https?:\\/\\/(www\\.)?([-a-zA-Z0-9@:%._\\+~#=]{2,256})?touro([-a-zA-Z0-9@:%._\\\\+~#=]{2,256})?\\.edu\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)"))
				URL.add(absHref);
		}
	}

	public static void addGraphVertex() {
		List<Edge> edges = new LinkedList<Edge>();
		Elements links = mainDoc.select("a");

		for (Element link : links) {
			String href = link.attr("href"); // "http://jsoup.org/"
			if (!href.contains(".png") && !coloredURL.contains(href) && href.matches(
					"https?:\\/\\/(www\\.)?([-a-zA-Z0-9@:%._\\+~#=]{2,256})?touro([-a-zA-Z0-9@:%._\\\\+~#=]{2,256})?\\.edu\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")) {
				int weight = (Math.abs(href.length() - urlString.length()) + 1);
				edges.add(new Edge(new VertexMETA(href), weight));
			}
		}
		graph.addVertex(new VertexMETA(urlString, edges));
	}

	public HashSet<String> getEmailList() {
		return emails;
	}

	public HashSet<String> getColored() {
		return coloredURL;
	}

}
