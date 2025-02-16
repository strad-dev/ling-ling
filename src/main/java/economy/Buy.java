package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

public class Buy {
	private static JSONObject data;
	private static String currencyEmoji;
	private static String item;
	private static GenericDiscordEvent e;
	
	public static void setEmoji(String currency) {
		switch(currency) {
			case "violins" -> currencyEmoji = Emoji.VIOLINS;
			case "medals" -> currencyEmoji = Emoji.MEDALS;
		}
	}

	public static void setItem(String original) {
		switch(original) {
			case "quality", "violinquality", "q" -> item = "violinQuality";
			case "skills", "s" -> item = "skills";
			case "lesson", "l", "lessonquality" -> item = "lessonQuality";
			case "string", "str", "stringquality" -> item = "stringQuality";
			case "bow", "b", "bowquality" -> item = "bowQuality";
			case "math" -> item = "math";

			case "piccolo" -> item = "piccolo";
			case "flute" -> item = "flute";
			case "oboe" -> item = "oboe";
			case "clarinet" -> item = "clarinet";
			case "bassoon" -> item = "bassoon";
			case "contrabassoon", "cb" -> item = "contraBassoon";

			case "horn" -> item = "horn";
			case "trumpet" -> item = "trumpet";
			case "trombone" -> item = "trombone";
			case "tuba" -> item = "tuba";
			case "timpani" -> item = "timpani";
			case "percussion" -> item = "percussion";

			case "first", "violin1" -> item = "violin1";
			case "second", "violin2" -> item = "violin2";
			case "cello" -> item = "cello";
			case "doublebass", "db" -> item = "doubleBass";
			case "piano" -> item = "piano";
			case "harp" -> item = "harp";

			case "soprano" -> item = "soprano";
			case "alto" -> item = "alto";
			case "tenor" -> item = "tenor";
			case "bass" -> item = "bass";
			case "soloist" -> item = "soloist";

			case "hall" -> item = "hall";
			case "conductor" -> item = "conductor";
			case "tickets" -> item = "tickets";
			case "advertising" -> item = "advertising";

			case "students" -> item = "students";
			case "lessoncharge", "pricing" -> item = "lessonCharge";
			case "training" -> item = "training";
			case "studio" -> item = "studio";
			case "longerlessons", "longer" -> item = "longerLessons";

			case "efficiency", "ep" -> item = "efficiency";
			case "luck", "lm" -> item = "luck";
			case "sophistication", "rob", "sr" -> item = "sophistication";
			case "magicfindviolins", "magicfindv", "mfv" -> item = "magicFindViolins";
			case "insurance" -> item = "insurance";
			case "timecrunch", "tc" -> item = "timeCrunch";

			case "moreincome", "income" -> item = "moreIncome";
			case "morecommandincome", "commandincome", "cmdincome" -> item = "moreCommandIncome";
			case "moremulti", "multi" -> item = "moreMulti";
			case "morerob", "robbing" -> item = "moreRob";
			case "magicfindmedals", "magicfindm", "mfm" -> item = "magicFindMedals";
			case "shield" -> item = "shield";
			case "duplicator" -> item = "duplicator";

			case "storage" -> item = "storage";
			case "moreinterest" -> item = "moreInterest";
			case "lesspenalty" -> item = "lessPenalty";

			case "orchestra" -> item = "orchestra";
			case "certificate" -> item = "certificate";

			default -> item = original;
		}
	}
	
	public static void processBooleanUpgrade(long cost, int add, String currency) {
		setEmoji(currency);
		long amount = (long) data.get(currency);
		if((boolean) data.get(item)) {
			e.reply("You already purchased `" + item + "`!");
		} else if(amount < cost) {
			e.reply("You do not have enough " + currency + " to purchase `" + item + "`!\nYou need " + Utils.formatNumber(cost - amount) + " more " + currency + ".");
		} else {
			data.replace(currency, amount - cost);
			data.replace("income", (long) data.get("income") + add);
			data.replace(item, true);
			e.reply("Successfully purchased `" + item + "` for " + Utils.formatNumber(cost) + currencyEmoji +
					"\nYou have " + Utils.formatNumber(amount - cost) +  currencyEmoji + " left.");
			if(item.equals("orchestra")) {
				data.replace("storage", 1);
				e.sendMessage("The Bank of TwoSet has noticed you, and has given you space to store violins!  " +
						"You have 20 million storage for free; this can increased by buying more space using medals.");
			}
			RNGesus.lootbox(e, data);
			Achievement.calculateAchievement(e, data, "income", "AFKer");
			SaveData.saveData(e, data);

		}
	}
	
	public static void processUpgrade(long cost, int add, int maxAmount, String currency) {
		setEmoji(currency);
		long amount = (long) data.get(currency);
		if((long) data.get(item) == maxAmount) {
			e.reply("You already purchased the maximum amount of `" + item + "`!");
		} else if(amount < cost) {
			e.reply("You do not have enough " + currency + " to purchase `" + item + "`!\nYou need " + Utils.formatNumber(cost - amount) + " more " + currency + ".");
		} else {
			data.replace(currency, amount - cost);
			data.replace("income", (long) data.get("income") + add);
			long itemCount = (long) data.get(item) + 1;
			data.replace(item, itemCount);
			if(item.contains("magicFind")) {
				data.replace("magicFind", (long) data.get("magicFind") + 1);
			}
			if(maxAmount == 2147483647) {
				e.reply("Successfully purchased `" + item + " #" + itemCount + "` for " + Utils.formatNumber(cost) + currencyEmoji +
						"\nYou have " + Utils.formatNumber(amount - cost) + currencyEmoji + " left.");
			} else {
				if(amount == maxAmount) {
					e.reply("**MAX LEVEL**\nSuccessfully purchased `" + item + " #" + itemCount + "/" + maxAmount + "` for `" + cost + "`" + currencyEmoji +
							"\nYou have " + Utils.formatNumber(amount - cost) + currencyEmoji + " left.");
				} else {
					e.reply("Successfully purchased `" + item + " #" + itemCount + "/" + maxAmount + "` for " + Utils.formatNumber(cost) + currencyEmoji +
							"\nYou have " + Utils.formatNumber(amount - cost) + currencyEmoji + " left.");
				}
			}
			RNGesus.lootbox(e, data);
			Achievement.calculateAchievement(e, data, "income", "AFKer");
			SaveData.saveData(e, data);
		}
	}
	
	public static void buy(GenericDiscordEvent event, String item1) {
		e = event;
		setItem(item1);
		data = LoadData.loadData(e);
		boolean boughtItem = false;
		boolean hasOrchestra = (boolean) data.get("orchestra");
		boolean hasCertificate = (boolean) data.get("certificate");
		if(item.isEmpty()) {
			e.reply("You have to specify an upgrade to buy, can't give you nothing.");
			return;
		}
		if(hasCertificate) {
			boughtItem = true;
			switch(item) {
				case "longerLessons" -> processBooleanUpgrade(30000000, 0, "violins");
				case "studio" -> processBooleanUpgrade(20000000, 5000, "violins");
				case "training" ->
						processUpgrade(2000000 * ((long) data.get("training") + 1), 1000, 10, "violins");
				case "lessonCharge" ->
						processUpgrade(3000000 * ((long) data.get("lessonCharge") + 1), 3000, 5, "violins");
				case "students" ->
						processUpgrade(Utils.itemCost((long) data.get("students"), 1.9, 1000000), 2000, 2147483647, "violins");
				default -> boughtItem = false;
			}
		}
		if(hasOrchestra && !boughtItem) {
			boughtItem = true;
			switch(item) {
				case "piccolo" -> processBooleanUpgrade(200000, 30, "violins");
				case "contraBassoon" -> processBooleanUpgrade(250000, 30, "violins");
				case "harp" -> processBooleanUpgrade(350000, 80, "violins");
				case "flute" -> processUpgrade(250000 * ((long) data.get("flute") + 1), 60, 4, "violins");
				case "oboe" -> processUpgrade(250000 * ((long) data.get("oboe") + 1), 60, 4, "violins");
				case "clarinet" ->
						processUpgrade(200000 * ((long) data.get("clarinet") + 1), 50, 4, "violins");
				case "bassoon" ->
						processUpgrade(200000 * ((long) data.get("bassoon") + 1), 40, 4, "violins");
				case "horn" -> processUpgrade(200000 * ((long) data.get("horn") + 1), 40, 8, "violins");
				case "trumpet" ->
						processUpgrade(200000 * ((long) data.get("trumpet") + 1), 30, 4, "violins");
				case "trombone" ->
						processUpgrade(200000 * ((long) data.get("trombone") + 1), 20, 6, "violins");
				case "tuba" -> processUpgrade(200000 * ((long) data.get("tuba") + 1), 20, 2, "violins");
				case "timpani" ->
						processUpgrade(250000 * ((long) data.get("timpani") + 1), 60, 2, "violins");
				case "percussion" ->
						processUpgrade(100000 * ((long) data.get("percussion") + 1), 10, 2, "violins");
				case "violin1" -> processUpgrade(450000 * (long) data.get("violin1"), 70, 20, "violins");
				case "violin2" -> processUpgrade(350000 * (long) data.get("violin2"), 60, 20, "violins");
				case "cello" -> processUpgrade(300000 * ((long) data.get("cello") + 1), 50, 15, "violins");
				case "doubleBass" ->
						processUpgrade(300000 * ((long) data.get("doubleBass") + 1), 50, 5, "violins");
				case "piano" -> processUpgrade(750000 * ((long) data.get("piano") + 1), 110, 2, "violins");
				case "soprano" ->
						processUpgrade(80000 * ((long) data.get("soprano") + 1), 30, 20, "violins");
				case "alto" -> processUpgrade(60000 * ((long) data.get("alto") + 1), 20, 20, "violins");
				case "tenor" -> processUpgrade(60000 * ((long) data.get("tenor") + 1), 20, 20, "violins");
				case "bass" -> processUpgrade(60000 * ((long) data.get("bass") + 1), 20, 20, "violins");
				case "soloist" ->
						processUpgrade(250000 * ((long) data.get("soloist") + 1), 60, 4, "violins");
				case "conductor" ->
						processUpgrade(Utils.itemCost((long) data.get("conductor"), 2.8, 100000), 500, 2147483647, "violins");
				case "advertising" ->
						processUpgrade(100000 * ((long) data.get("advertising") + 1), 100, 20, "violins");
				case "tickets" ->
						processUpgrade(Utils.itemCost((long) data.get("tickets"), 1.9, 1000000), 1000, 2147483647, "violins");
				case "moreInterest", "lessPenalty" -> processBooleanUpgrade(15, 0, "medals");
				case "storage" -> processUpgrade(3 * (long) data.get("storage"), 0, 2147483647, "medals");
				case "certificate" -> {
					if((long) data.get("income") < 40000) {
						e.reply("You do not have neough hourly income to become a teacher!");
					} else {
						processBooleanUpgrade(200000000, 5000, "violins");
					}
				}
				default -> boughtItem = false;
			}
		}
		if(!boughtItem) {
			switch(item) {
				case "insurance" -> processBooleanUpgrade(2500000, 0, "violins");
				case "timeCrunch" -> processBooleanUpgrade(120000000, 0, "violins");
				case "efficiency" ->
						processUpgrade(Utils.itemCost((long) data.get("efficiency"), 1.09, 400), 0, 2147483647, "violins");
				case "luck" ->
						processUpgrade(Utils.itemCost((long) data.get("luck"), 1.22, 1000), 0, 50, "violins");
				case "sophistication" ->
						processUpgrade(Utils.itemCost((long) data.get("sophistication"), 1.35, 5000), 0, 30, "violins");
				case "violinQuality" ->
						processUpgrade(Utils.itemCost((long) data.get("violinQuality"), 2.8, 1000), 600, 2147483647, "violins");
				case "skills" ->
						processUpgrade(Utils.itemCost((long) data.get("skills"), 1.9, 500), 250, 2147483647, "violins");
				case "lessonQuality" ->
						processUpgrade(Utils.itemCost((long) data.get("lessonQuality"), 1.9, 400), 200, 2147483647, "violins");
				case "stringQuality" ->
						processUpgrade(Utils.itemCost((long) data.get("stringQuality"), 1.45, 300), 150, 2147483647, "violins");
				case "bowQuality" ->
						processUpgrade(Utils.itemCost((long) data.get("bowQuality"), 2.3, 600), 250, 2147483647, "violins");
				case "math" -> processBooleanUpgrade(10000000, 6500, "violins");
				case "moreIncome" ->
						processUpgrade((long) data.get("moreIncome") + 1, 2000, 2147483647, "medals");
				case "moreCommandIncome" ->
						processUpgrade((long) Math.pow(2, (long) data.get("moreCommandIncome")), 0, 2147483647, "medals");
				case "moreMulti" ->
						processUpgrade((long) data.get("moreMulti") + 1, 0, 2147483647, "medals");
				case "moreRob" ->
						processUpgrade((long) Math.pow(2, (long) data.get("moreRob")), 0, 2147483647, "medals");
				case "shield" -> processBooleanUpgrade(10, 0, "medals");
				case "duplicator" -> processBooleanUpgrade(15, 0, "medals");
				case "magicFindViolins" ->
						processUpgrade(Utils.itemCost((long) data.get("magicFindViolins"), 3, 1000000), 0, 2147483647, "violins");
				case "magicFindMedals" ->
						processUpgrade((long) Math.pow(2, (long) data.get("magicFindMedals")), 0, 2147483647, "medals");
				case "orchestra" -> {
					if((long) data.get("income") < 7500) {
						e.reply("You do not have enough hourly income to hire an orchestra!");
					} else {
						processBooleanUpgrade(25000000, 3100, "violins");
					}
				}
				case "hall" -> {
					if(!hasOrchestra) {
						processUpgrade(Utils.itemCost((long) data.get("hall"), 3.5, 100000), 300, 3, "violins");
					} else {
						processUpgrade(Utils.itemCost((long) data.get("hall"), 3.5, 100000), 300, 2147483647, "violins");
					}
				}
				default -> e.reply("You can't buy something that's not for sale, that would be quite a waste of time and violins.");
			}
		}
	}
}