package file.db;

public class Part {
	public Part(String partnum, String desc, int qty, int ucost) {
		this.partnum = partnum;
		this.desc = desc;
		this.qty = qty;
		this.ucost = ucost;
	}

	String getDesc() {
		return desc;
	}

	String getPartnum() {
		return partnum;
	}

	int getQty() {
		return qty;
	}

	int getUnitCost() {
		return ucost;
	}

	private String partnum;
	private String desc;
	private int qty;
	private int ucost;
}
