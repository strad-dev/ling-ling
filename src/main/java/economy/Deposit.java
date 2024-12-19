package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Numbers;

public class Deposit {
	public static void deposit(GenericDiscordEvent e, String temp) {
		JSONObject data = LoadData.loadData(e);
		long amount;
		long wallet = (long) data.get("violins");
		if(temp.isEmpty()) {
			e.reply("You have to deposit something.");
			return;
		}
		if(temp.equals("max")) {
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
			long max = Numbers.maxBank((long) data.get("storage"), (long) data.get("benevolentBankers"));
			long balance = (long) data.get("bank");
			if(balance + amount > max) {
				amount = max - balance;
				balance = max;
				e.reply("**MAX VIOLINS**\nYou deposited " + Numbers.formatNumber(amount) + Emoji.VIOLINS + " into your bank.  You now have " +
						Numbers.formatNumber(balance) + Emoji.VIOLINS + " in your bank.");
			} else {
				balance += amount;
				e.reply("You deposited " + Numbers.formatNumber(amount) + Emoji.VIOLINS + " into your bank.  You now have " +
						Numbers.formatNumber(balance) + Emoji.VIOLINS + " in your bank.");
			}
			data.replace("violins", wallet - amount);
			data.replace("bank", balance);
			SaveData.saveData(e, data);
		}
	}
}