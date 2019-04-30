package file.db;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PartsDB  {
	public final static int PNUMLEN = 20;
	public final static int DESCLEN = 30;
	public final static int QUANLEN = 4;
	public final static int COSTLEN = 4;

	private final static int RECLEN = 2 * PNUMLEN + 2 * DESCLEN + QUANLEN + COSTLEN;

	private RandomAccessFile raf;

	public PartsDB(String path) throws IOException {
		raf = new RandomAccessFile(path, "rw");
	}

	public void append(String partnum, String partdesc, int qty, int ucost) throws IOException {
		// sets to the next line
		raf.seek(raf.length());
		write(partnum, partdesc, qty, ucost);
	}

	private void write(String partnum, String partdesc, int qty, int ucost) throws IOException {
		StringBuffer sb = new StringBuffer(partnum);
		if (sb.length() > PNUMLEN)
			sb.setLength(PNUMLEN);
		if (sb.length() < PNUMLEN) {
			// padding
			int len = PNUMLEN - sb.length();
			for (int i = 0; i < len; i++)
				sb.append(" ");
		}
		raf.writeChars(sb.toString());
		sb = new StringBuffer(partdesc);
		if (sb.length() > DESCLEN)
			sb.setLength(DESCLEN);
		if (sb.length() < DESCLEN) {
			// padding
			int len = DESCLEN - sb.length();
			for (int i = 0; i < len; i++)
				sb.append(" ");
		}
		raf.writeChars(sb.toString());
		raf.writeInt(qty);
		raf.writeInt(ucost);
	}

	// ---------Index based selection
	public Part select(int recno) throws IOException {
		if (recno < 0 || recno >= numRecs())
			throw new IllegalArgumentException(recno + " out of range");
		raf.seek(recno * RECLEN);
		return read();
	}

	public void update(int recno, String partnum, String partdesc, int qty, int ucost) throws IOException {
		if (recno < 0 || recno >= numRecs())
			throw new IllegalArgumentException(recno + " out of range");
		raf.seek(recno * RECLEN);
		write(partnum, partdesc, qty, ucost);
	}

	// Number of records
	public int numRecs() throws IOException {
		return (int) raf.length() / RECLEN;
	}

	private Part read() throws IOException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < PNUMLEN; i++)
			sb.append(raf.readChar());
		String partnum = sb.toString().trim();
		sb.setLength(0);
		for (int i = 0; i < DESCLEN; i++)
			sb.append(raf.readChar());
		String partdesc = sb.toString().trim();
		int qty = raf.readInt();
		int ucost = raf.readInt();
		return new Part(partnum, partdesc, qty, ucost);
	}

	public void close() {
		try {
			raf.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
