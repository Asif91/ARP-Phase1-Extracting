import java.util.ArrayList;

public class Paragraph {
	private int paragraphId;
	private int jobId;
	private int paragraphNumber;
	private String description;
	private ArrayList<Keyword> keywords;

	public Paragraph(int jobId, int paragraphNumber, String description) {
		this.jobId = jobId;
		this.paragraphNumber = paragraphNumber;
		this.description = description;
		keywords = new ArrayList<Keyword>();
	}

	public int getParagraphId() {
		return paragraphId;
	}

	public void setParagraphId(int paragraphId) {
		this.paragraphId = paragraphId;
	}


	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getParagraphNumber() {
		return paragraphNumber;
	}

	public void setParagraphNumber(int paragraphNumber) {
		this.paragraphNumber = paragraphNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<Keyword> keywords) {
		this.keywords = keywords;
	}
}
