package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

public class PayLoan {
	public static void payLoan(GenericDiscordEvent e, String temp) {
		JSONObject data = LoadData.loadData(e);
		long violins = (long) data.get("violins");
		long owed = (long) data.get("loan");
		if(owed == 0) {
			e.reply("You don't owe anything!");
		} else {
			long amount;
			if(temp.isEmpty()) {
				e.reply("You have to either input `max` or an integer.");
				return;
			}
			if(temp.equals("max")) {
				amount = Math.min(violins, owed);
			} else {
				try {
					amount = Long.parseLong(temp);
				} catch(Exception exception) {
					e.reply("You have to put in a real number or `max` dummy");
					return;
				}
			}
			if(amount > violins) {
				e.reply("You can't pay more than what you have, don't think I'm stupid, I have a quadragintuple PhD in Mathematics");
			} else {
				amount = Math.min(amount, owed);
				data.replace("loan", owed - amount);
				data.replace("violins", violins - amount);
				e.reply("You paid back " + Utils.formatNumber(amount) + Emoji.VIOLINS + ".  You now owe " + Utils.formatNumber(owed - amount) + Emoji.VIOLINS + ".");
				SaveData.saveData(e, data);
			}
		}
	}
}