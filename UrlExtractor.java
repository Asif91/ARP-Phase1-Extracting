import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlExtractor {
	private int numberOfJobs;
	private int numberOfPages;
	private int numberOfJobsOnLastPage;
	private int numberOfJobsPerPage;
	private String baseUrl;
	private String url;
	private String pageContent;

	private ArrayList<Job> jobs;
	private List<String> urls;
	private URL seek;
	private BufferedReader reader;
	// private String jobUrl = '"' + "url" + '"' + ":" + '"' +
	// "https://www.seek.com.au/job/";
	private String jobUrl = "jobIds";
	private String jobTitle = '"' + "title" + '"' + ":";

	private Job job;
	private DatabaseAccessor db;

	public UrlExtractor(String baseUrl) {
		jobs = new ArrayList<Job>();
		numberOfJobsPerPage = 20; // number of jobs on Seek by default
		numberOfPages = 1;
		this.baseUrl = baseUrl;
		db = new DatabaseAccessor();
		db.connect();
	}

	public void extractNewIndex() {
		for (int i = 1; i <= numberOfPages; i++) {
			url = baseUrl + "?page=" + i;

			try {
				seek = new URL(url);
				reader = new BufferedReader(new InputStreamReader(seek.openStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					pageContent += line;
				}
				reader.close();
			} catch (IOException e) {
				System.out.println("An error occured while accessing Seek website");
			}

			if (i == 1)
				pageNumberExtract(pageContent, numberOfJobsPerPage);

			System.out.println("Page:" + i);

			if (i != numberOfPages)
				jobExtract(pageContent);
			else
				jobExtract(pageContent, numberOfJobsOnLastPage);
			pageContent = "";
		}
		System.out.println("Extraction finished");

		for (int k = 0; k < jobs.size(); k++)
			System.out.println("Job " + (k + 1) + ": " + jobs.get(k).getJobTitle() + " : " + jobs.get(k).getJobUrl());
	}

	public void extractJobs(int numberOfJobs) {
		System.out.println("Extracting job descriptions");
		if (numberOfJobs < jobs.size()) {

			if (numberOfJobs == 0) {
				for (Job job : jobs)
					descriptionExtract(job);
				System.out.println("Extraction complete");
			} else {
				for (int i = 0; i < numberOfJobs; i++)
					descriptionExtract(jobs.get(i));
			}
		} else {
			System.out.println("The Number of Jobs You Enter Is Greater Than the Number of Jobs in Database");
		}
	}

	public void pageNumberExtract(String pageContent, int numberOfJobsPerPage) {
		String totalJobs = '"' + "searchResultTotalCount" + '"' + ":";
		String jobCount = pageContent.substring(pageContent.indexOf(totalJobs) + 25,
				pageContent.indexOf(totalJobs) + 31);
		jobCount = jobCount.substring(0, jobCount.indexOf(','));
		numberOfJobs = new Integer(jobCount);
		numberOfPages = numberOfJobs / numberOfJobsPerPage;
		numberOfJobsOnLastPage = numberOfJobs % numberOfJobsPerPage;
		if ((double) new Integer(jobCount) / numberOfJobsPerPage > numberOfPages) {
			numberOfPages++;
		}

		System.out.println("Number of jobs: " + jobCount);
		System.out.println("Number of pages: " + numberOfPages);
		System.out.println("Number of jobs on the last page: " + numberOfJobsOnLastPage);

	}

	public void jobExtract(String pageContent) {
		String jobTeaser = "," + '"' + "teaser" + '"';
		int nextTeaser = pageContent.indexOf(jobTeaser);
		int nextUrl = pageContent.indexOf(jobUrl);
		// System.out.println("Urls:" + nextUrl);
		// System.out.println(pageContent.substring(nextUrl + 9, nextUrl +
		// 188));
		urls = Arrays.asList(pageContent.substring(nextUrl + 9, nextUrl + 188).split(","));
		int nextTitle = pageContent.indexOf(jobTitle);

		String url;
		String title;
		String key;

		System.out.println("Updating jobs to databse");

		for (int i = 0; i < 20; i++) {
			title = pageContent.substring(pageContent.indexOf(jobTitle, nextTitle) + 9,
					pageContent.indexOf(jobTeaser, nextTeaser) - 1);

			if (title.contains("locationMatch")) {
				title = title.substring(0, title.indexOf("locationMatch") - 3);
			}
			// System.out.println(title);

			nextTitle = pageContent.indexOf(jobTitle, nextTitle + 1);
			nextTeaser = pageContent.indexOf(jobTeaser, +nextTeaser + 1);

			// url = pageContent.substring(pageContent.indexOf(jobUrl, nextUrl)
			// + 7,
			// pageContent.indexOf(jobUrl, nextUrl) + 43);
			// nextUrl = pageContent.indexOf(jobUrl, nextUrl + 1);
			//
			// key = url.substring(28);

			job = new Job(title, "https://www.seek.com.au/job/" + urls.get(i), urls.get(i));
			db.insertJob(job);
			jobs.add(job);
		}
		System.out.println("Update complete");
	}

	public void jobExtract(String pageContent, int numberOfJobs) {
		String jobTeaser = "," + '"' + "teaser" + '"';
		int nextTeaser = pageContent.indexOf(jobTeaser);
		int nextUrl = pageContent.indexOf(jobUrl);
		String urlList = pageContent.substring(nextUrl + 9, nextUrl + (numberOfJobs * 9) + 8);
		// System.out.println("Urls:" + urlList);
		urls = Arrays.asList(urlList.split(","));
		int nextTitle = pageContent.indexOf(jobTitle);

		String url;
		String title;
		String key;

		System.out.println("Updating jobs to databse");

		for (int i = 0; i < numberOfJobs; i++) {
			title = pageContent.substring(pageContent.indexOf(jobTitle, nextTitle) + 9,
					pageContent.indexOf(jobTeaser, nextTeaser) - 1);

			nextTitle = pageContent.indexOf(jobTitle, nextTitle + 1);
			nextTeaser = pageContent.indexOf(jobTeaser, +nextTeaser + 1);

			if (title.contains("locationMatch")) {
				title = title.substring(0, title.indexOf("locationMatch") - 3);
			}
			// System.out.println(title);
			// url = pageContent.substring(pageContent.indexOf(jobUrl, nextUrl)
			// + 7,
			// pageContent.indexOf(jobUrl, nextUrl) + 43);
			// nextUrl = pageContent.indexOf(jobUrl, nextUrl + 1);
			//
			// key = url.substring(28);
			// =========================
			job = new Job(title, "https://www.seek.com.au/job/" + urls.get(i), urls.get(i));

			db.insertJob(job);
			System.out.println("Job ID: " + job.getJobId());
			jobs.add(job);
		}
		System.out.println("Update complete");
	}

	public void descriptionExtract(Job job) {
		Paragraph newParagraph;

		try {
			String description;
			Document doc = Jsoup.connect(job.getJobUrl()).get();

			Elements paragraphs = doc.select("[data-automation=jobDescription]").select("div.templatetext").select("p");
			Elements lists = doc.select("[data-automation=jobDescription]").select("div.templatetext").select("ul");

			// System.out.println("No of Paras: " + paragraphs.size());

			if (paragraphs.size() == 0) {
				paragraphs = doc.select("[data-automation=jobDescription]").select("div.templatetext");
				if (paragraphs.size() == 0) {
					paragraphs = doc.select("[data-automation=jobDescription]").select("blockquote.templatetext")
							.select("p");
					lists = doc.select("[data-automation=jobDescription]").select("blockquote.templatetext")
							.select("ul");
				} else {
					paragraphs = doc.select("[data-automation=jobDescription]");
					if(paragraphs.size() != 0) {
					lists.clear();
					}
				}
			}

			for (Element paragraph : paragraphs) {
				description = paragraph.text();
				description = description.replace("'", "\\\\").replace("-", " ").replace(" OR ", "\\").replace(" / ",
						"\\");

				newParagraph = new Paragraph(job.getJobId(), job.getParagraphs().size() + 1, description);
				if (newParagraph.getDescription().length() > 3) {
					db.insertParagraph(newParagraph);
					job.getParagraphs().add(newParagraph);
				}
			}

			for (Element list : lists) {
				description = list.text();
				description = description.replace("'", "\\").replace("-", " ").replace(" OR ", "\\").replace(" / ",
						"\\");
				newParagraph = new Paragraph(job.getJobId(), job.getParagraphs().size() + 1, description);

				if (newParagraph.getDescription().length() > 3) {
					db.insertParagraph(newParagraph);
					job.getParagraphs().add(newParagraph);
				}
			}
			db.updateNumberOfParagraph(job.getParagraphs().size(), job.getJobId());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadJobsFromDb() {
		jobs = db.getUnvistedJobs();
		for (int i = 0; i < jobs.size(); i++)
			System.out.println("Job " + (i + 1) + ": " + jobs.get(i).getJobTitle() + " : " + jobs.get(i).getJobUrl()
					+ " - Loaded");
	}

}
