package economy;

import java.util.Comparator;

public class MarketComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		long price1 = Long.parseLong(o1.split(" ")[0]);
		long price2 = Long.parseLong(o2.split(" ")[0]);
		return Long.compare(price1, price2);
	}
}