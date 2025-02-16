package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.Utils;

import java.awt.*;

public class Cooldowns {

	public static void cooldowns(GenericDiscordEvent e) {
		try {
			JSONObject data = LoadData.loadData(e);
			long time = System.currentTimeMillis();
			EmbedBuilder builder = new EmbedBuilder()
					.setColor(Color.decode((String) data.get("color")))
					.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl());
			EmbedBuilder builder2 = new EmbedBuilder()
					.setColor(Color.decode((String) data.get("color")))
					.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl());
			//rob
			long milliseconds = (long) data.get("robCD") - time;
			if(milliseconds < 0) {
				builder.addField("**Rob**", ":white_check_mark:", true);
			} else {
				builder.addField("**Rob**", Utils.makeCooldownTime(milliseconds), true);
			}

			//scales
			milliseconds = (long) data.get("scaleCD") - time;
			if(milliseconds < 0) {
				builder.addField("**Scales**", ":white_check_mark:", true);
			} else {
				builder.addField("**Scales**", Utils.makeCooldownTime(milliseconds), true);
			}

			//practice
			milliseconds = (long) data.get("practiceCD") - time;
			if(milliseconds < 0) {
				builder.addField("**Practice**", ":white_check_mark:", true);
			} else {
				builder.addField("**Practice**", Utils.makeCooldownTime(milliseconds), true);
			}

			//teach
			if((boolean) data.get("certificate")) {
				milliseconds = (long) data.get("teachCD") - time;
				if(milliseconds < 0) {
					builder.addField("**Teach**", ":white_check_mark:", true);
				} else {
					builder.addField("**Teach**", Utils.makeCooldownTime(milliseconds), true);
				}
			}

			//rehearse
			if((boolean) data.get("orchestra")) {
				milliseconds = (long) data.get("rehearseCD") - time;
				if(milliseconds < 0) {
					builder.addField("**Rehearse**", ":white_check_mark:", true);
				} else {
					builder.addField("**Rehearse**", Utils.makeCooldownTime(milliseconds), true);
				}
			}

			//perform
			milliseconds = (long) data.get("performCD") - time;
			if(milliseconds < 0) {
				builder.addField("**Perform**", ":white_check_mark:", true);
			} else {
				builder.addField("**Perform**", Utils.makeCooldownTime(milliseconds), true);
			}

			//daily
			milliseconds = (long) data.get("dailyCD") - time;
			if(milliseconds < 0) {
				builder.addField("**Daily**", ":white_check_mark:", true);
			} else {
				builder.addField("**Daily**", Utils.makeCooldownTime(milliseconds), true);
			}

			//gift
			milliseconds = (long) data.get("giftCD") - time;
			if(milliseconds < 0) {
				builder.addField("**Gift**", ":white_check_mark:", true);
			} else {
				builder.addField("**Gift**", Utils.makeCooldownTime(milliseconds), true);
			}

			//claim
			milliseconds = (long) data.get("voteCD") - time;
			if(milliseconds < 0) {
				builder.addField("**Claim**", ":white_check_mark:", true);
			} else {
				builder.addField("**Claim**", Utils.makeCooldownTime(milliseconds), true);
			}

			//income - rosin
			milliseconds = (long) data.get("rosinExpire") - time;
			if(milliseconds < 0) {
				builder2.addField("**Rosin Income**", ":x:", true);
			} else {
				if(milliseconds < 86400000) {
					builder2.addField("**Rosin Income**", ":warning:\n" + Utils.makeCooldownTime(milliseconds), true);
				} else {
					builder2.addField("**Rosin Income**", ":white_check_mark:\n" + Utils.makeCooldownTime(milliseconds), true);
				}
			}
			builder2.addBlankField(true);

			//income - strings
			milliseconds = (long) data.get("stringsExpire") - time;
			if(milliseconds < 0) {
				builder2.addField("**Strings Income**", ":x:", true);
			} else {
				if(milliseconds < 86400000) {
					builder2.addField("**Strings Income**", ":warning:\n" + Utils.makeCooldownTime(milliseconds), true);
				} else {
					builder2.addField("**Strings Income**", ":white_check_mark:\n" + Utils.makeCooldownTime(milliseconds), true);
				}
			}

			//income - bow hairs
			milliseconds = (long) data.get("bowHairExpire") - time;
			if(milliseconds < 0) {
				builder2.addField("**Bow Hair Income**", ":x:", true);
			} else {
				if(milliseconds < 86400000) {
					builder2.addField("**Bow Hair Income**", ":warning:\n" + Utils.makeCooldownTime(milliseconds), true);
				} else {
					builder2.addField("**Bow Hair Income**", ":white_check_mark:\n" + Utils.makeCooldownTime(milliseconds), true);
				}
			}
			builder2.addBlankField(true);

			//income - service
			milliseconds = (long) data.get("serviceExpire") - time;
			if(milliseconds < 0) {
				builder2.addField("**Violin Service Income**", ":x:", true);
			} else {
				if(milliseconds < 86400000) {
					builder2.addField("**Violin Service Income**", ":warning:\n" + Utils.makeCooldownTime(milliseconds), true);
				} else {
					builder2.addField("**Violin Service Income**", ":white_check_mark:\n" + Utils.makeCooldownTime(milliseconds), true);
				}
			}

			builder2.addField("Hourly", "Reminder to run `h` to claim your hourly income!\nMake sure the above four are checked to avoid losing income.", false);

			builder.setTitle("__**Command Cooldowns**__");
			builder2.setTitle("__**Time Before Hourly Income Penalty**__");
			e.replyTwoEmbeds(builder.build(), builder2.build());
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
}