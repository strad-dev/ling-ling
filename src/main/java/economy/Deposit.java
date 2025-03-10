package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

public class Deposit {
	public static void deposit(GenericDiscordEvent e, String temp) {
		JSONObject data = LoadData.loadData(e);
		long amount;
		long wallet = (long) data.get("violins");
		if(temp.isEmpty()) {
			e.reply("You have to deposit something.");
			return;
		}
		if(temp.equals("max") || temp.equals("all")) {
			amount = wallet;
		} else {
			try {
				amount = Long.parseLong(temp);
			} catch(Exception exception) {
				e.reply("You have to either input `max` or an integer.");
				return;
			}
		}
		if(amount > wallet) {
			amount = wallet;
		}
		if(amount < 1) {
			e.reply("Stop wasting my time trying to deposit a negative amount.");
		} else {
			long max = Utils.maxBank((long) data.get("storage"), (long) data.get("benevolentBankers"));
			long balance = (long) data.get("bank");
			if(balance + amount > max) {
				amount = max - balance;
				balance = max;
				e.reply("**MAX VIOLINS**\nYou deposited " + Utils.formatNumber(amount) + Emoji.VIOLINS + " into your bank.  You now have " +
						Utils.formatNumber(balance) + Emoji.VIOLINS + " in your bank.");
			} else {
				balance += amount;
				e.reply("You deposited " + Utils.formatNumber(amount) + Emoji.VIOLINS + " into your bank.  You now have " +
						Utils.formatNumber(balance) + Emoji.VIOLINS + " in your bank.");
			}
			data.replace("violins", wallet - amount);
			data.replace("bank", balance);
			SaveData.saveData(e, data);
		}
	}
}