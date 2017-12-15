import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int option = 0;
		String url;
		int numberOfJobs = 0;
		UrlExtractor myExtractor;
		do {
			System.out.println("Personality Analyser");
			System.out.println("1. Extract New Data");
			System.out.println("2. Continue to Extract Data");
			System.out.println("0. Quit");
			option = scan.nextInt();
			// https://www.seek.com.au/tester-jobs-in-information-communication-technology/in-Hawthorn-VIC-3122

			if (option == 1) {
				System.out.println("URL: ");
				url = scan.next();
				System.out.println("How Many Jobs Do You Want to Extract ?( 0:Extract All)");
				numberOfJobs = scan.nextInt();
				System.out.println("Job url extraction in progress");
				myExtractor = new UrlExtractor(url);
				myExtractor.extractNewIndex();
				myExtractor.extractJobs(numberOfJobs);
			} else if (option == 2) {
				System.out.println("How Many Jobs Do You Want to Extract ?( 0:Extract All)");
				numberOfJobs = scan.nextInt();
				myExtractor = new UrlExtractor("local");
				myExtractor.loadJobsFromDb();
				myExtractor.extractJobs(numberOfJobs);

			}  else if (option == 0) {
				System.out.println("Bye Bye !");
			}

		} while (option != 0);

	}

}
