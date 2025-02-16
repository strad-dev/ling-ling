package processes;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {
	@Test
	public void luthierChanceTest() {
		Assertions.assertEquals(0.1, Utils.luthierChance(1), 0.000001);
		Assertions.assertEquals(0.1, Utils.luthierChance(10), 0.000001);
		Assertions.assertEquals(0.06, Utils.luthierChance(50), 0.000001);
		Assertions.assertEquals(0.01, Utils.luthierChance(100), 0.000001);
		Assertions.assertEquals(0.01, Utils.luthierChance(1000), 0.000001);
		Assertions.assertEquals(0.006, Utils.luthierChance(5000), 0.000001);
		Assertions.assertEquals(0.001, Utils.luthierChance(10000), 0.000001);
	}

	@Test
	public void formatNumberTest() {
		Assertions.assertEquals("`123`", Utils.formatNumber(123L));
		Assertions.assertEquals("`1 234`", Utils.formatNumber(1234L));
		Assertions.assertEquals("`12 345`", Utils.formatNumber(12345L));
		Assertions.assertEquals("`123 456`", Utils.formatNumber(123456L));
		Assertions.assertEquals("`1 234 567`", Utils.formatNumber(1234567L));
		Assertions.assertEquals("`-123`", Utils.formatNumber(-123L));
		Assertions.assertEquals("`-1 234`", Utils.formatNumber(-1234L));
		Assertions.assertEquals("`-12 345`", Utils.formatNumber(-12345L));
		Assertions.assertEquals("`-123 456`", Utils.formatNumber(-123456L));
		Assertions.assertEquals("`-1 234 567`", Utils.formatNumber(-1234567L));
	}

	@Test
	public void calculateAmountTest() {
		JSONObject testData = new JSONObject();
		testData.put("efficiency", 0L);
		testData.put("hall", 0L);
		testData.put("moreCommandIncome", 0L);
		testData.put("isBooster", false);
		testData.put("serverLevel", 1.0);

		// test default profile
		Assertions.assertEquals(100, Utils.calculateAmount(testData, 100L));

		// test varying levels of efficiency
		testData.replace("efficiency", 10L);
		Assertions.assertEquals(259, Utils.calculateAmount(testData, 100L));

		testData.replace("efficiency", 50L);
		Assertions.assertEquals(1825, Utils.calculateAmount(testData, 100L));

		testData.replace("efficiency", 100L);
		Assertions.assertEquals(20939, Utils.calculateAmount(testData, 100L));

		testData.replace("efficiency", 150L);
		Assertions.assertEquals(71970, Utils.calculateAmount(testData, 100L));

		testData.replace("efficiency", 200L);
		Assertions.assertEquals(247371, Utils.calculateAmount(testData, 100L));

		testData.replace("efficiency", 0L);

		// test varying levels of hall
		testData.replace("hall", 3L);
		Assertions.assertEquals(142, Utils.calculateAmount(testData, 100L));

		testData.replace("hall", 6L);
		Assertions.assertEquals(202, Utils.calculateAmount(testData, 100L));

		testData.replace("hall", 10L);
		Assertions.assertEquals(324, Utils.calculateAmount(testData, 100L));

		testData.replace("hall", 0L);

		// test varying levels of medal upgrade
		testData.replace("moreCommandIncome", 5L);
		Assertions.assertEquals(371, Utils.calculateAmount(testData, 100L));

		testData.replace("moreCommandIncome", 10L);
		Assertions.assertEquals(1378, Utils.calculateAmount(testData, 100L));

		testData.replace("moreCommandIncome", 15L);
		Assertions.assertEquals(5118, Utils.calculateAmount(testData, 100L));

		testData.replace("moreCommandIncome", 0L);

		// test server level + boosters
		testData.replace("serverLevel", 1.02);
		Assertions.assertEquals(102, Utils.calculateAmount(testData, 100L));
		testData.replace("serverLevel", 1.075);
		Assertions.assertEquals(107, Utils.calculateAmount(testData, 100L));
		testData.replace("serverLevel", 1.11);
		Assertions.assertEquals(111, Utils.calculateAmount(testData, 100L));
		testData.replace("serverLevel", 1.25);
		Assertions.assertEquals(125, Utils.calculateAmount(testData, 100L));

		testData.replace("isBooster", true);
		testData.replace("serverLevel", 1.0);
		Assertions.assertEquals(130, Utils.calculateAmount(testData, 100L));
		testData.replace("serverLevel", 1.075);
		Assertions.assertEquals(137, Utils.calculateAmount(testData, 100L));
		testData.replace("serverLevel", 1.25);
		Assertions.assertEquals(155, Utils.calculateAmount(testData, 100L));

		// test various combinations
		testData.replace("efficiency", 150L);
		testData.replace("hall", 9L);
		testData.replace("moreCommandIncome", 8L);
		testData.replace("serverLevel", 1.15);
		testData.replace("isBooster", true);
		Assertions.assertEquals(2457180, Utils.calculateAmount(testData, 100L));
	}

	@Test
	public void itemCostTest() {
		Assertions.assertEquals(5904900, Utils.itemCost(10, 3, 100));
	}

	@Test
	public void maxLoanTest() {
		JSONObject testData = new JSONObject();

		testData.put("income", 0L);
		Assertions.assertEquals(0, Utils.maxLoan(testData));

		testData.replace("income", 10L);
		Assertions.assertEquals(1, Utils.maxLoan(testData));

		testData.replace("income", 100L);
		Assertions.assertEquals(100, Utils.maxLoan(testData));

		testData.replace("income", 1000L);
		Assertions.assertEquals(10000, Utils.maxLoan(testData));

		testData.replace("income", 10000L);
		Assertions.assertEquals(1000000, Utils.maxLoan(testData));

		testData.replace("income", 50000L);
		Assertions.assertEquals(25000000, Utils.maxLoan(testData), 1);

		testData.replace("income", 100000L);
		Assertions.assertEquals(100000000, Utils.maxLoan(testData), 1);

		testData.replace("income", 150000L);
		Assertions.assertEquals(225000000, Utils.maxLoan(testData), 1);

		testData.replace("income", 200000L);
		Assertions.assertEquals(400000000, Utils.maxLoan(testData), 1);
	}

	@Test
	public void calculateLoanTest() {
		JSONObject testData = new JSONObject();

		// test 0.5-1.25 (default)
		testData.put("violins", 0L);
		testData.put("income", 10000L);
		testData.put("loan", 1000000L);
		Utils.calculateLoan(testData, 10000);
		Assertions.assertEquals(5000, (long) testData.get("violins"));
		Assertions.assertEquals(995000, (long) testData.get("loan"));

		// test 1.25-2.0
		testData.replace("violins", 0L);
		testData.replace("income", 10000L);
		testData.replace("loan", 1500000L);
		Utils.calculateLoan(testData, 10000);
		Assertions.assertEquals(2000, (long) testData.get("violins"));
		Assertions.assertEquals(1492000, (long) testData.get("loan"));

		// test 2.0+
		testData.replace("violins", 0L);
		testData.replace("income", 10000L);
		testData.replace("loan", 2500000L);
		Utils.calculateLoan(testData, 10000);
		Assertions.assertEquals(-1000, (long) testData.get("violins"));
		Assertions.assertEquals(2489000, (long) testData.get("loan"));

		// test 0.0-0.5
		testData.replace("violins", 0L);
		testData.replace("income", 10000L);
		testData.replace("loan", 250000L);
		Utils.calculateLoan(testData, 10000);
		Assertions.assertEquals(8000, (long) testData.get("violins"));
		Assertions.assertEquals(248000, (long) testData.get("loan"));

		// test 0.0
		testData.replace("violins", 0L);
		testData.replace("income", 10000L);
		testData.replace("loan", 0L);
		Utils.calculateLoan(testData, 10000);
		Assertions.assertEquals(10000, (long) testData.get("violins"));
		Assertions.assertEquals(0, (long) testData.get("loan"));

		// test error handling
		testData.replace("violins", 0L);
		testData.replace("income", 10000L);
		testData.replace("loan", -2000L);
		Utils.calculateLoan(testData, 10000);
		Assertions.assertEquals(12000, (long) testData.get("violins"));
		Assertions.assertEquals(0, (long) testData.get("loan"));
	}
}