package correlation;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads MySQL DB, output csv file containing boolean value for certain charges
 * (burglary, drugs, weapon, etc.) which will be useful for analysis
 * Namely, contingency tables in R, followed by a fisher test
 * 
 * @author mike
 *
 */
public class BooleanChecker {
	private Map<String, List<String>> hM = new HashMap<String, List<String>>();
	private List<Charges> chargesList = new ArrayList<Charges>();

	public BooleanChecker() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ct?serverTimezone=UTC", "root",
					ASDFASDF);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select docketno, description from charges");
			while (rs.next())
				addMap(rs.getString(1), rs.getString(2));
			con.close();
			System.out.println("Analysing..");
			analyseMap();
			System.out.println("Printing out...");
			printFindings();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void printFindings() throws IOException {
		FileWriter fW = new FileWriter("/home/mike/charges.csv");
		fW.write("docketno,drugCharge,weaponCharge,burglaryCharge" + System.lineSeparator());
		int count = 0;
		for (Charges c : chargesList) {
			fW.write(c.toString() + System.lineSeparator());
			// System.out.println(c);
			if (c.hasDrugCharge() && !c.hasWeaponCharge())
				count++;
		}
		System.out.println(count + " occs");
		fW.close();
	}

	private void analyseMap() {
		for (String d : hM.keySet()) {
			Charges c = new Charges(d);
			for (String s : hM.get(d)) {
				if (containsDrugs(s))
					c.setDrugCharge(true);
				if (containsWeapons(s))
					c.setWeaponCharge(true);
				if (containsBurglary(s))
					c.setBurglaryCharge(true);
			}
			chargesList.add(c);
		}
	}

	private boolean containsWeapons(String s) {
		if (s.contains("WEAPON") || s.contains("WPN") || s.contains("GUN") || s.contains("FIREARM")
				|| s.contains("PISTOL"))
			return true;
		return false;
	}

	private boolean containsBurglary(String s) {
		if (s.contains("ROBBERY") || s.contains("LARCENY") || s.contains("BURGLAR")) {
			// System.out.println("burglary");
			return true;
		}
		return false;
	}

	private boolean containsDrugs(String s) {
		if (s.contains("NARC") || s.contains("HALLU") || s.contains("ILLEGAL DRUG") || s.contains("DRG"))
			return true;
		// if (s.contains("MJ") || s.contains("MR") || s.contains("NARC") ||
		// s.contains("HALLU")
		// || s.contains("ILLEGAL DRUG") || s.contains("DRG"))
		// return true;
		return false;
	}

	private void addMap(String docket, String desc) {
		if (hM.containsKey(docket)) {
			hM.get(docket).add(desc.toUpperCase());
		} else {
			List<String> l = new ArrayList<String>();
			l.add(desc.toUpperCase());
			hM.put(docket, l);
		}
	}

	public static void main(String[] args) {
		new BooleanChecker();
	}

	class Charges {
		private boolean drugCharge = false;
		private boolean weaponCharge = false;
		private boolean burglaryCharge = false;
		private String docketNo;

		private Charges() {
			System.out.println("Invalid instantiation of charges - exiting");
			System.exit(0);
		}

		public Charges(String docketNo) {
			this.docketNo = docketNo;
		}

		public String getDocketNo() {
			return docketNo;
		}

		public boolean hasBurglaryCharge() {
			return burglaryCharge;
		}

		public void setBurglaryCharge(boolean burgCharge) {
			this.burglaryCharge = burgCharge;
		}

		public boolean hasDrugCharge() {
			return drugCharge;
		}

		public void setDrugCharge(boolean drugCharge) {
			this.drugCharge = drugCharge;
		}

		public boolean hasWeaponCharge() {
			return weaponCharge;
		}

		public void setWeaponCharge(boolean weaponCharge) {
			this.weaponCharge = weaponCharge;
		}

		public String toString() {
			return docketNo + ",drug" + drugCharge + ",weapon" + weaponCharge + ",burglary" + burglaryCharge;
		}
	}

	private final String ASDFASDF = "idk";

}
