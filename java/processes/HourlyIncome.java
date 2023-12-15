package processes;

import com.mongodb.client.MongoCollection;
import dev.ResetDaily;
import eventListeners.GenericDiscordEvent;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

class CreateThread implements Runnable {

	private static GenericDiscordEvent e;
	private static long time;
	private static JSONObject data;

	public static void setGenericDiscordEvent(GenericDiscordEvent e1, long time1, JSONObject data1) {
		e = e1;
		time = time1;
		data = data1;
	}

	@Override
	public void run() {
		long begin = System.currentTimeMillis();
		System.out.println("[DEBUG] New Thread: " + Thread.currentThread().getId() + "\n        Hourly Income Thread");
		long howManyHours = 0;
		while(System.currentTimeMillis() > time) {
			System.out.println("        Sending Income for Time " + Instant.ofEpochMilli(time).atZone(ZoneId.of("UTC")).toLocalDateTime());
			time += 3600000;
			howManyHours++;
			data.replace("hourly", time);
			DatabaseManager.saveMiscData(data);

			//RESET COOLDOWNS BOUND TO 12AM UTC
			if(time % 86400000 == 0) {
				ResetDaily.resetDaily(e);
			}

			//BANK SHIT
			if(time % 259200000 == 0) {
				InterestPenalty.interestPenalty();
				Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("863135059712409632")).sendMessage("Loan penalties applied; Interest awarded!").queue();
			}

		}
		HourlyIncome.hourlyIncome(howManyHours);
		data.replace("sendingHourly", false);
		DatabaseManager.saveMiscData(data);
		long end = System.currentTimeMillis();
		Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("863135059712409632")).sendMessage("Hourly Incomes Sent!  Hours sent: " + howManyHours + "\nTotal Time: " + (end - begin)).queue();
		System.out.println("        Thread " + Thread.currentThread().getId() + " Finished.   Time taken: " + (end - begin));
	}
}

public class HourlyIncome {
	public static void checkHourly(GenericDiscordEvent e1) {
		//HOURLY
		JSONObject data = DatabaseManager.getMiscData();
		assert data != null;
		long time = (long) data.get("hourly");
		if(System.currentTimeMillis() > time && !Boolean.parseBoolean(data.get("sendingHourly").toString())) {
			data.replace("sendingHourly", true);
			DatabaseManager.saveMiscData(data);
			CreateThread.setGenericDiscordEvent(e1, time, data);
			Thread object = new Thread(new CreateThread());
			object.start();
		}
	}

	public static void hourlyIncome(long howManyHours) {
		ArrayList<Document> documents = DatabaseManager.getAllEconomyData();
		MongoCollection<Document> collection = DatabaseManager.prepareStoreAllEconomyData();
		JSONParser parser = new JSONParser();
		JSONObject data;
		for(Document file : documents) {
			try {
				data = (JSONObject) parser.parse(file.toJson());
				if((boolean) data.get("banned")) {
					continue;
				}
			} catch(Exception exception) {
				continue;
			}
			long income = (long) data.get("income") * howManyHours;
			long time = System.currentTimeMillis();
			if(time > (long) data.get("rosinExpire")) {
				income = (long) (income - (income * 0.25));
			}
			if(time > (long) data.get("stringsExpire")) {
				income = (long) (income - (income * 0.25));
			}
			if(time > (long) data.get("bowHairExpire")) {
				income = (long) (income - (income * 0.25));
			}
			if(time > (long) data.get("serviceExpire")) {
				income = (long) (income - (income * 0.25));
			}
			long violins = (long) data.get("violins");
			long loan = (long) data.get("loan");
			if(loan > income * 400) {
				violins = (long) (violins - (income * 0.3));
				loan = (long) (loan - (income * 1.3));
			} else if(loan > income * 250) {
				loan -= income;
			} else if(loan > income * 100) {
				violins = (long) (violins + income * 0.3);
				loan = (long) (loan - (income * 0.7));
			} else if(loan > 0) {
				violins = (long) (violins + income * 0.6);
				loan = (long) (loan - (income * 0.4));
			} else {
				violins += income;
			}
			if(loan < 0) {
				violins -= loan;
				loan = 0;
			}
			data.replace("violins", violins);
			data.replace("loan", loan);
			collection.replaceOne(eq("discordID", data.get("discordID")), Document.parse(data.toJSONString()));
		}
	}
}