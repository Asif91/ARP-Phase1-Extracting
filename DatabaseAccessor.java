import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseAccessor {
	private final String dbAddress = "localhost/personality";
	private final String account = "root";
	private final String password = "";
	private final String dbName = "PersonalityAnalyserDemo";
	private Connection connection;
	private Statement statement;

	public DatabaseAccessor() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = null;
			statement = null;
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to locate jdbc driver");
		}
	}

	public void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + dbAddress + "/" + dbName, account, password);
		} catch (SQLException e) {
			System.out.println("Unable to connect to database");
		}
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Unable to close database connection");
		}
	}

	public void insertJob(Job job) {
		int count = 0;
		String script = "SELECT COUNT(*) FROM jobs WHERE job_key = " + job.getJobKey();
		try {
			statement = connection.createStatement();
			ResultSet keys = statement.executeQuery(script);
			keys.next();
			count = keys.getInt("COUNT(*)");

			if (count == 0) {
				script = "INSERT INTO jobs (job_id, job_key, job_url, job_title) VALUES (default, '" + job.getJobKey()
						+ "', '" + job.getJobUrl() + "','" + job.getJobTitle() + "')";
				statement = connection.createStatement();
				statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS);
				keys = statement.getGeneratedKeys();
				if (keys.next()) {
					job.setJobId(keys.getInt(1));
				}
			} else {
				script = "SELECT * FROM jobs WHERE job_key =" + job.getJobKey();
				statement = connection.createStatement();
				keys = statement.executeQuery(script);
				while (keys.next()) {
					int jobId = keys.getInt("job_id");
					job.setJobId(jobId);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occurred while retrieving unvisted jobs !");
			System.out.println("Statement: " + script);
		} finally {
			statement = null;
		}

	}

	public void insertParagraph(Paragraph paragraph) {

		int count = 0;
		String script = "SELECT COUNT(*) FROM jobs WHERE no_of_paras is NULL AND job_id = " + paragraph.getJobId();
		try {

			statement = connection.createStatement();
			ResultSet keys = statement.executeQuery(script);
			while (keys.next())
				count = keys.getInt("COUNT(*)");
			if (count == 1) {
				script = "INSERT INTO paragraphs (paragraph_id, job_id, paragraph_no, descriptions) VALUES (default, '"
						+ paragraph.getJobId() + "','" + paragraph.getParagraphNumber() + "','"
						+ paragraph.getDescription() + "')";
				statement = connection.createStatement();
				statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS);
				keys = statement.getGeneratedKeys();
				if (keys.next()) {
					paragraph.setJobId(keys.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occurred while adding paragraph !");
			System.out.println("Statement:" + script);
		} finally {
			statement = null;
		}

	}

	public void insertKeyword(Keyword keyword) {
		String script = "INSERT INTO keywords (key_id, paragraph_id, keyword) VALUES (default, '"
				+ keyword.getPararaphId() + "', '" + keyword.getKeyword() + "')";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS);
			ResultSet keys = statement.getGeneratedKeys();
			if (keys.next()) {
				keyword.setPararaphId(keys.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occurred while adding keyword !");
			System.out.println("Statement: " + script);
		} finally {
			statement = null;
		}
	}

	public void deleteJob(Job job) {
		String script = "DELETE FROM jobs WHERE job_id = " + job.getJobId();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(script);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occurred while adding keyword !");
			System.out.println("Statement: " + script);
		} finally {
			statement = null;
		}
	}

	public void updateNumberOfParagraph(int numberOfParagraph, int jobId) {
		String script = "UPDATE jobs SET no_of_paras = " + numberOfParagraph + " WHERE job_id = " + jobId;

		try {
			statement = connection.createStatement();
			statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occurred while update number of paragraphs to job table !");
			System.out.println("Statement: " + script);
		} finally {
			statement = null;
		}
	}

	public ArrayList<Job> getUnvistedJobs() {
		ArrayList<Job> jobs = new ArrayList<Job>();
		String script = "SELECT * FROM jobs WHERE no_of_paras is NULL;";
		try {
			statement = connection.createStatement();
			ResultSet keys = statement.executeQuery(script);
			while (keys.next()) {
				int jobId = keys.getInt("job_id");
				String jobKey = keys.getString("job_key");
				String jobUrl = keys.getString("job_url");
				String jobTitle = keys.getString("job_title");
				Job job = new Job(jobId, jobTitle, jobUrl, jobKey);
				jobs.add(job);
			}
			return jobs;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occurred while retrieving unvisted jobs !");
			System.out.println("Statement: " + script);
			return null;
		} finally {
			statement = null;
		}
	}
}
