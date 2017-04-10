
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Find offenders that are likely to move around the state for their crimes; a
 * way to evade recognition?
 * 
 * @author mike
 *
 */
public class MovingOffenders {

	private Map<String, List<String>> hM = new HashMap<String, List<String>>();

	public MovingOffenders() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ct?serverTimezone=UTC", "root",
					ASDFASDF);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct * from person_arresting_agency");
			while (rs.next())
				addMap(rs.getString(1), rs.getString(2));
			con.close();
			System.out.println("Printing out...");
			printMap();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void printMap() throws IOException {
		FileWriter fW = new FileWriter("/home/mike/offender_arrests.csv");
		// fW.write("docketno,drugCharge,weaponCharge,burglaryCharge" +
		// System.lineSeparator());

		for (String s : hM.keySet()) {
			fW.write(s + "," + hM.get(s).size() + System.lineSeparator());
		}

		fW.close();
	}

	private void addMap(String pid, String arresting_agency) {
		if (hM.containsKey(pid)) {
			hM.get(pid).add(arresting_agency.toUpperCase());
		} else {
			List<String> l = new ArrayList<String>();
			l.add(arresting_agency.toUpperCase());
			hM.put(pid, l);
		}
	}

	public static void main(String[] args) {
		new MovingOffenders();
	}

	private final String ASDFASDF = "idk";

}
