package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

public class Withdraw {
	public static void withdraw(GenericDiscordEvent e, String temp) {
		JSONObject data = LoadData.loadData(e);
		long amount;
		long balance = (long) data.get("bank");
		if(temp.isEmpty()) {
			e.reply("You have to withdraw something.");
			return;
		}
		if(temp.equals("max") || temp.equals("all")) {
			amount = balance;
		} else {
			try {
				amount = Long.parseLong(temp);
			} catch(Exception exception) {
				e.reply("You have to either input `max` or an integer.");
				return;
			}
		}
		if(amount > balance) {
			amount = balance;
		}
		if(amount < 1) {
			e.reply("Stop wasting my time trying to withdraw a negative amount, shame on you");
		} else {
			balance -= amount;
			data.replace("violins", (long) data.get("violins") + amount);
			data.replace("bank", balance);
			e.reply("You withdrew " + Utils.formatNumber(amount) + Emoji.VIOLINS + " from your bank.  You now have " +
					Utils.formatNumber(balance) + Emoji.VIOLINS + " in your bank.");
			SaveData.saveData(e, data);
		}
	}
}