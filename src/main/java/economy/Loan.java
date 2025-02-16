package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

public class Loan {
	public static void loan(GenericDiscordEvent e, String amount) {
		JSONObject data = LoadData.loadData(e);
		long balance = (long) data.get("loan");
		if(balance > 0) {
			e.reply("You still have an outstanding balance of " + Utils.formatNumber(balance) + Emoji.VIOLINS + "!\n\nIf you would like to repay in full or a portion, use `payloan`");
		} else {
			long loan;
			if(amount.isEmpty()) {
				e.reply("You have to either input `max` or an integer.");
				return;
			}
			long maxLoan = Utils.maxLoan(data);
			if(amount.equals("max")) {
				loan = maxLoan;
			} else {
				try {
					loan = Long.parseLong(amount);
				} catch(Exception exception) {
					e.reply("You have to either input `max` or an integer.");
					return;
				}
				if(loan > maxLoan) {
					loan = maxLoan;
				}
			}
			data.replace("loan", loan);
			data.replace("violins", (long) data.get("violins") + loan);
			e.reply("You have borrowed " + Utils.formatNumber(loan) + Emoji.VIOLINS + " from the Bank.  " +
					"Most actions will result in a portion being used to pay back the loan.  You can also manually contribute by using `payloan`.");
			SaveData.saveData(e, data);
		}
	}
}