package file.db;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PartsDBTest {
	PartsDB pdb = null;

	@Before
	public void startDB() {
		try {
			pdb = new PartsDB("parts.db");

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.fail("Unexpected Error");
		}
	}

	@After
	public void stopDB() {
		try {
			pdb.close();

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	@Test
	public void testDB() {

		try {
			if (pdb.numRecs() == 0) {
				// Populate the database with records.
				pdb.append("1-9009-3323-4x", "Wiper Blade Micro Edge", 30, 2468);
				pdb.append("1-3233-44923-7j", "Parking Brake Cable", 5, 1439);
				pdb.append("2-3399-6693-2m", "Halogen Bulb H4 55/60W", 22, 813);
				pdb.append("2-599-2029-6k", "Turbo Oil Line O-Ring ", 26, 155);
				pdb.append("3-1299-3299-9u", "Air Pump Electric", 9, 20200);
			}
			dumpRecords(pdb);
			pdb.update(1, "1-3233-44923-7j", "Parking Brake Cable", 5, 1995);
			dumpRecords(pdb);
			
			Assert.assertEquals(   "Parking Brake Cable",pdb.select(1).getDesc());
			Assert.assertEquals(   9,pdb.select(4).getQty());
			Assert.assertEquals(   "Air Pump Electric",pdb.select(4).getDesc());
			 
			
			
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.fail("Unexpected Error");
		}

	}

	static void dumpRecords(PartsDB pdb) throws IOException {
		for (int i = 0; i < pdb.numRecs(); i++) {
			Part part = pdb.select(i);
			System.out.print(format(part.getPartnum(), PartsDB.PNUMLEN, true));
			System.out.print(" | ");
			System.out.print(format(part.getDesc(), PartsDB.DESCLEN, true));
			System.out.print(" | ");
			System.out.print(format("" + part.getQty(), 10, false));
			System.out.print(" | ");
			String s = part.getUnitCost() / 100 + "." + part.getUnitCost() % 100;
			if (s.charAt(s.length() - 2) == '.')
				s += "0";
			System.out.println(format(s, 10, false));
		}
		System.out.println("Number of records = " + pdb.numRecs());
		System.out.println();
	}

	static String format(String value, int maxWidth, boolean leftAlign) {
		StringBuffer sb = new StringBuffer();
		int len = value.length();
		if (len > maxWidth) {
			len = maxWidth;
			value = value.substring(0, len);
		}
		if (leftAlign) {
			sb.append(value);
			for (int i = 0; i < maxWidth - len; i++)
				sb.append(" ");
		} else {
			for (int i = 0; i < maxWidth - len; i++)
				sb.append(" ");
			sb.append(value);
		}
		return sb.toString();
	}
}
