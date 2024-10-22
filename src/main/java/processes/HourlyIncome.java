package processes;

import economy.Emoji;
import economy.LoadData;
import economy.RNGesus;
import economy.SaveData;
import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;

public class HourlyIncome {
	public static void hourlyIncome(GenericDiscordEvent e) {
		JSONObject data = LoadData.loadData(e);
		long time = System.currentTimeMillis();
		long lastIncome = (long) data.get("incomeCD");
		long interestCD = (long) data.get("interestCD");
		double howManyHours = 0;
		long originalHours = 0;
		long interest = 0;
		String message = "";
		while(lastIncome < time) {
			if(time <= (long) data.get("rosinExpire")) {
				howManyHours += 0.25;
			}
			if(time <= (long) data.get("stringsExpire")) {
				howManyHours += 0.25;
			}
			if(time <= (long) data.get("bowHairExpire")) {
				howManyHours += 0.25;
			}
			if(time <= (long) data.get("serviceExpire")) {
				howManyHours += 0.25;
			}
			if(time > interestCD) {
				interest += InterestPenalty.interestPenalty(data);
				interestCD += 259200000;
			}
			originalHours++;
			lastIncome += 3600000;
		}

		long income = (long) data.get("income");
		long gross = (long) (income * howManyHours);
		long net;
		long loanPaid = 0;
		long loan = (long) data.get("loan");
		long maxLoan = Numbers.maxLoan(data);

		if(loan > maxLoan * 2) {
			net = (long) (gross * -0.3);
			loanPaid += (long) (gross * 1.3);
		} else if(loan > maxLoan * 1.25) {
			net = 0;
			loanPaid += gross;
		} else if(loan > maxLoan / 2) {
			net = (long) (gross * 0.3);
			loanPaid += (long) (gross * 0.7);
		} else if(loan > 0) {
			net = (long) (gross * 0.6);
			loanPaid += (long) (gross * 0.4);
		} else {
			net = gross;
		}
		if(loanPaid > loan) {
			net += loanPaid - loan;
			loanPaid = loan;
		}
		data.replace("violins", (long) data.get("violins") + net);
		data.replace("loan", loan - loanPaid);
		data.replace("incomeCD", lastIncome);
		data.replace("interestCD", interestCD);
		long originalIncome = originalHours * income;
		String reply = "You collected " + Numbers.formatNumber(originalHours) + " hours of income!" +
				"\n\nOriginal Income: " + Numbers.formatNumber(originalIncome) + Emoji.VIOLINS +
				"\nGross Income: " + Numbers.formatNumber(gross) + Emoji.VIOLINS +
				"\nIncome Lost to Inactive Items: " + Numbers.formatNumber(originalIncome - gross) + Emoji.VIOLINS +
				"\nLoans Paid: " + Numbers.formatNumber(loanPaid) + Emoji.VIOLINS +
				"\n\nNet Income: " + Numbers.formatNumber(net) + Emoji.VIOLINS;
		if(interest != 0) {
			reply += "\n\nInterest Earned: " + Numbers.formatNumber(interest) + Emoji.VIOLINS;
		}
		e.reply(reply);
		SaveData.saveData(e, data);
	}
}