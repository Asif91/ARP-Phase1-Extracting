import java.util.ArrayList;

public class Job {
	private int jobId;
	private String jobTitle;
	private String jobUrl;
	private String jobKey;
	private ArrayList<Paragraph> paragraphs;
	
	public Job(String jobTitle, String jobUrl, String jobKey){
		this.jobTitle = jobTitle;
		this.jobUrl = jobUrl;
		this.jobKey = jobKey;
		paragraphs = new ArrayList<Paragraph>();
	}
	
	public Job(int jobId, String jobTitle, String jobUrl, String jobKey){
		this.jobId = jobId;
		this.jobTitle = jobTitle;
		this.jobUrl = jobUrl;
		this.jobKey = jobKey;
		paragraphs = new ArrayList<Paragraph>();
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

	public String getJobKey() {
		return jobKey;
	}

	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}

	public ArrayList<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(ArrayList<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}



	
	
	
}
