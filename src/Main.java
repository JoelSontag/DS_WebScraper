import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private static int counter = 100000;
	private static Scraper scraper = new Scraper("https://dental.touro.edu", counter);
	private static Scanner kybd = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("1.MST\n2.Regular MultiThreaded");
		if (kybd.nextInt() == 1) {
			System.out.println("Enter amount");
			MST(kybd.nextInt());
		} else
			thread();
	}

	public static void MST(int amt) {
		scraper.executeMST(amt);
	}

	public static void thread() {

		ExecutorService threadPool = Executors.newCachedThreadPool();

		for (int i = 0; i < counter; i++)
			threadPool.submit(() -> {
				scraper.execute();
			});

		threadPool.shutdown();
		while (!threadPool.isTerminated())
			;

		System.out.println(scraper.getColored().size() + " pages scraped");
		for (String s : scraper.getColored())
			System.out.println(s);

		for (String s : scraper.getEmailList())
			System.out.println(s);
	}
}
