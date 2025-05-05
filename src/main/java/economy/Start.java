package economy;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import eventListeners.GenericDiscordEvent;
import org.bson.Document;
import org.json.simple.JSONArray;
import processes.DatabaseManager;

import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class Start {
	public static void start(GenericDiscordEvent e, String id, boolean ban) {
		try {
			MongoDatabase database = DatabaseManager.getDatabase();
			MongoCollection<Document> collection = database.getCollection("Economy Data");
			Document document = collection.find(eq("discordID", id)).first();
			if(document != null) {
				e.reply("You already have a save, don't try to outsmart me!");
				return;
			}
			String discordName;
			if(!ban) {
				discordName = e.getAuthor().getEffectiveName();
			} else {
				try {
					discordName = Objects.requireNonNull(e.getJDA().getUserById(id)).getEffectiveName();
				} catch(Exception exception) {
					discordName = "";
				}
			}
			try {
				InsertOneResult result = collection.insertOne(new Document()
						.append("discordID", id)

						// Currencies
						.append("violins", 0L)
						.append("bank", 0L)
						.append("loan", 0L)
						.append("medals", 0L)
						.append("magicFind", 0L)
						.append("thirdPlace", 0L)
						.append("secondPlace", 0L)
						.append("firstPlace", 0L)

						// Crafting Items
						.append("grains", 0L)
						.append("plastic", 0L)
						.append("water", 0L)
						.append("teaBase", 0L)
						.append("wood", 0L)
						.append("pineSap", 0L)
						.append("steel", 0L)
						.append("horseHair", 0L)
						.append("essence", 0L)

						// AFK Income Items
						.append("rosin", 0L)
						.append("string", 0L)
						.append("bowHair", 0L)
						.append("violinService", 0L)

						// Consumable Items
						.append("rice", 0L)
						.append("tea", 0L)
						.append("blessings", 0L)

						// Lootboxes
						.append("voteBox", 0L)
						.append("giftBox", 0L)
						.append("kits", 1L)
						.append("linglingBox", 0L)
						.append("crazyBox", 0L)
						.append("RNGesusBox", 0L)

						// Lootbox Rewards
						.append("freeIncome", 0L)
						.append("benevolentBankers", 0L)
						.append("qualityItems", 0L)
						.append("bonusMedals", 0L)
						.append("bonusInterest", 0L)
						.append("RNGesusItemThatDoesAbsolutelyNothingLMAO", 0L)

						// AFK Income Penalty
						.append("rosinExpire", System.currentTimeMillis() + 1224000000)
						.append("stringsExpire", System.currentTimeMillis() + 1224000000)
						.append("bowHairExpire", System.currentTimeMillis() + 1224000000)
						.append("serviceExpire", System.currentTimeMillis() + 1224000000)

						// Statistics
						.append("income", 0L)
						.append("streak", 0L)
						.append("earnings", 0L)
						.append("winnings", 0L)
						.append("millions", 0L)
						.append("robbed", 0L)
						.append("lostToRob", 0L)
						.append("scalesPlayed", 0L)
						.append("hoursPractised", 0.0)
						.append("rehearsals", 0L)
						.append("performances", 0L)
						.append("hoursTaught", 0.0)
						.append("maxStreak", 0L)
						.append("votes", 0L)
						.append("luthiers", 0L)
						.append("giftsGiven", 0L)
						.append("giftsReceived", 0L)
						.append("interestEarned", 0L)
						.append("penaltiesIncurred", 0L)
						.append("RNGesusWeight", 0L)
						.append("itemsSold", 0L)
						.append("itemsBought", 0L)
						.append("moneyEarned", 0L)
						.append("moneySpent", 0L)
						.append("taxPaid", 0L)

						// Cooldowns
						.append("betCD", 0L)
						.append("scaleCD", 0L)
						.append("practiceCD", 0L)
						.append("teachCD", 0L)
						.append("rehearseCD", 0L)
						.append("performCD", 0L)
						.append("robCD", 0L)
						.append("voteCD", 0L)
						.append("dailyCD", 0L)
						.append("giftCD", 0L)
						.append("incomeCD", ((System.currentTimeMillis() / 3600000L) + 1L) * 3600000L)
						.append("interestCD", ((System.currentTimeMillis() / 259200000L) + 1L) * 259200000L)

						// Misc Upgrades
						.append("efficiency", 0L)
						.append("luck", 0L)
						.append("sophistication", 0L)
						.append("insurance", false)
						.append("timeCrunch", false)
						.append("magicFindViolins", 0L)

						// Hourly Upgrades
						.append("violinQuality", 0L)
						.append("skills", 0L)
						.append("lessonQuality", 0L)
						.append("stringQuality", 0L)
						.append("bowQuality", 0L)
						.append("math", false)

						// Orchestra
						.append("orchestra", false)

						// Winds
						.append("piccolo", false)
						.append("flute", 0L)
						.append("oboe", 0L)
						.append("clarinet", 0L)
						.append("bassoon", 0L)
						.append("contraBassoon", false)

						// Brass
						.append("horn", 0L)
						.append("trumpet", 0L)
						.append("trombone", 0L)
						.append("tuba", 0L)
						.append("timpani", 0L)
						.append("percussion", 0L)

						// Strings
						.append("violin1", 1L)
						.append("violin2", 1L)
						.append("cello", 0L)
						.append("doubleBass", 0L)
						.append("piano", 0L)
						.append("harp", false)

						// Chorus
						.append("soprano", 0L)
						.append("alto", 0L)
						.append("tenor", 0L)
						.append("bass", 0L)
						.append("soloist", 0L)

						// Misc Orchestra
						.append("hall", 0L)
						.append("conductor", 0L)
						.append("tickets", 0L)
						.append("advertising", 0L)

						// Teaching Upgrades
						.append("certificate", false)
						.append("students", 0L)
						.append("lessonCharge", 0L)
						.append("training", 0L)
						.append("studio", false)
						.append("longerLessons", false)

						// Medal Upgrades
						.append("moreIncome", 0L)
						.append("moreCommandIncome", 0L)
						.append("moreMulti", 0L)
						.append("moreRob", 0L)
						.append("shield", false)
						.append("duplicator", false)
						.append("magicFindMedals", 0L)

						// Bank Upgrades
						.append("storage", 0L)
						.append("moreInterest", false)
						.append("lessPenalty", false)

						// Settings
						.append("color", "#0000FF")
						.append("DMs", true)
						.append("extraInfo", false)

						// Luthier
						.append("luthierBalance", 0L)
						.append("luthierServers", new JSONArray())
						.append("cheater", 0L)

						// Misc Data
						.append("banned", ban)
						.append("perms", 0L)
						.append("medalToday", false)
						.append("isBooster", false)
						.append("serverLevel", 1.0)
						.append("mcIGN", "")
						.append("discordName", discordName)
						.append("achievements", "")
						.append("scaleStreak", 0L)
						.append("scaleStreakRecord", 0L)
						.append("scaleStreakExpire", 0L)
						.append("goOutside", false));

				if(!ban) {
					e.reply("Your profile has been created!  Run `/help 3` for a list of economy commands!\nSupport server: https://discord.gg/gNfPwa8\nBeginner's Guide: <https://docs.google.com/document/d/1Oo8m8XuGsIOyMzJhllUN9SpOJI8hSUeQt5RbyPY9qMI/edit?usp=sharing>");
				}
			} catch(MongoException exception) {
				e.reply("Something went wrong creating the file.  Run `/support` for a link to the support server to get in contact with the developer.");
				exception.printStackTrace();
			}
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
}