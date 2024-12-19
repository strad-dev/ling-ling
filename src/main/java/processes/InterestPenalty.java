package processes;

import org.json.simple.JSONObject;

public class InterestPenalty {
	public static long interestPenalty(JSONObject data) {
		//BANK INTEREST
		long balance = (long) data.get("bank");
		double interest = 0.01;
		if((boolean) data.get("moreInterest")) {
			interest += 0.01;
		}
		interest += 0.001 * (long) data.get("bonusInterest");
		long earned = (long) (balance * interest);
		long max = Numbers.maxBank((long) data.get("storage"), (long) data.get("benevolentBankers"));
		if(earned + balance > max) {
			earned -= (earned + balance) - max;
		}

		long loan = (long) data.get("loan");
		long violins = (long) data.get("violins");
		long maxLoan = Numbers.maxLoan(data);
		if(loan > maxLoan * 2) {
			violins -= earned * 4;
			loan -= earned * 5;
		} else if(loan > maxLoan * 1.25) {
			violins -= earned;
			loan -= earned * 2;
		} else if(loan > maxLoan / 2) {
			loan -= earned;
		} else if(loan > 0) {
			balance = (long) (balance + earned * 0.5);
			loan = (long) (loan - (earned * 0.5));
		} else {
			balance += earned;
		}

		if(loan < 0) {
			balance -= loan;
			loan = 0;
		}

		//LOAN PENALTY
		if((boolean) data.get("lessPenalty")) {
			data.replace("loan", (long) (loan * 1.09));
			data.replace("penaltiesIncurred", (long) data.get("penaltiesIncurred") + (long) (loan * 0.09));
		} else {
			data.replace("loan", (long) (loan * 1.1));
			data.replace("penaltiesIncurred", (long) data.get("penaltiesIncurred") + (long) (loan * 0.1));
		}

		data.replace("violins", violins);
		data.replace("loan", loan);
		data.replace("bank", balance);
		data.replace("interestEarned", (long) data.get("interestEarned") + earned);
		return earned;
	}
}