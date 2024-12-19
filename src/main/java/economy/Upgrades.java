package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.Numbers;

import java.awt.*;

public class Upgrades {
	private static JSONObject data;
	private static GenericDiscordEvent e;
	private static EmbedBuilder builder;
	
	public static void incomeUpgrades() {
		builder.setTitle("__**Income Upgrades**__")
				.addField("Violin Quality " + Numbers.formatNumber(data.get("violinQuality")),
						"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("violinQuality"), 2.8, 1000)) + Emoji.VIOLINS +
						"\nEffect: `+600`" + Emoji.VIOLINS + "/hour\nID: `quality` `violinquality` `q`", true)
				.addField("Skill Level " + Numbers.formatNumber(data.get("skills")),
						"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("skills"), 1.9, 500)) + Emoji.VIOLINS +
						"\nEffect: `+250`" + Emoji.VIOLINS + "/hour\nID: `skills` `s`", true)
				.addField("Lesson Quality " + Numbers.formatNumber(data.get("lessonQuality")),
						"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("lessonQuality"), 1.9, 400)) + Emoji.VIOLINS +
						"\nEffect: `+200`" + Emoji.VIOLINS + "/hour\nID: `lesson` `l` `lessonquality`", true)
				.addField("String Quality " + Numbers.formatNumber(data.get("stringQuality")),
						"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("stringQuality"), 1.45, 300)) + Emoji.VIOLINS +
						"\nEffect: `+150`" + Emoji.VIOLINS + "/hour\nID: `string` `str` `stringquality`", true)
				.addField("Bow Quality " + Numbers.formatNumber(data.get("bowQuality")),
						"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("bowQuality"), 2.3, 600)) + Emoji.VIOLINS +
								"\nEffect: `+250`" + Emoji.VIOLINS + "/hour\nID: `bow` `b` `bowquality`", true);
		if((boolean) data.get("math")) {
			builder.addField("Math Tutoring :white_check_mark:", "Effect: `+6 500`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Math Tutoring :x:", "Price: `10 000 000`" + Emoji.VIOLINS + "\nEffect: `+6 500`" + Emoji.VIOLINS + "/hour\nID: `math`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void orchMiscUpgrades() {
		builder.setTitle("__**Miscellaneous Orchestra Items**__");
		if(!(boolean) data.get("orchestra")) {
			if((long) data.get("hall") == 3) {
				builder.addField("Concert Hall Quality :white_check_mark:", "Effect: `+300`" + Emoji.VIOLINS + "/hour, `x1.125` command income", true);
			} else {
				builder.addField("Concert Hall Quality " + Numbers.formatNumber(data.get("hall")) + "/`3`",
						"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("hall"), 3.5, 100000)) + Emoji.VIOLINS +
								"\nEffect: `+300`" + Emoji.VIOLINS + "/hour, `x1.125` command income\nID: `hall`", true);
			}
			builder.addField("Orchestra",
					"Price: `25 000 000`" + Emoji.VIOLINS + "\nIncome Requirement: `7 500`" + Emoji.VIOLINS + "/hour" +
							"\nEffect: `+3 100`" + Emoji.VIOLINS + "/hour, access to `/rehearse` command\nID:`orchestra`", true);
		} else {
			builder.addField("Concert Hall Quality " + Numbers.formatNumber(data.get("hall")),
							"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("hall"), 3.5, 100000)) + Emoji.VIOLINS +
									"\nEffect: `+300`" + Emoji.VIOLINS + "/hour, x1.125 command income\nID: `hall`", true)
					.addBlankField(true)
					.addField("Conductor Musicality " + Numbers.formatNumber(data.get("conductor")),
							"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("conductor"), 2.8, 100000)) + Emoji.VIOLINS +
									"\nEffect: `+200`" + Emoji.VIOLINS + "/hour\nID: `conductor`", true)
					.addField("Ticket Price " + Numbers.formatNumber(data.get("tickets")),
							"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("tickets"), 1.9, 1000000)) + Emoji.VIOLINS +
									"\nEffect: `+1000`" + Emoji.VIOLINS + "/hour\nID: `tickets`", true)
					.addBlankField(true);
			if((long) data.get("advertising") == 20) {
				builder.addField("Advertising :white_check_mark:", "Effect: `+100`" + Emoji.VIOLINS + "/hour", true);
			} else {
				builder.addField("Advertising " + Numbers.formatNumber(data.get("advertising")) + "/`20`",
						"Price: " + Numbers.formatNumber(100000 * ((long) data.get("advertising") + 1)) + Emoji.VIOLINS +
								"\nEffect: `+100`" + Emoji.VIOLINS + "/hour\nID: `advertising`", true);
			}
			if(!(boolean) data.get("certificate")) {
				builder.addField("Teaching Certificate",
						"Price: `200 000 000`" + Emoji.VIOLINS + "\nIncome Requirement: `40 000`" + Emoji.VIOLINS + "/hr" +
								"\nEffect: `+5 000`" + Emoji.VIOLINS + "/hr, access to `/teach` command\nID: `certificate`", true);
			}
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void otherMiscUpgrades() {
		builder.setTitle("__**Other Miscellaneous Upgrades**__");
		if((long) data.get("efficiency") < 10) {
			builder.addField("Efficient Practising " + Numbers.formatNumber(data.get("efficiency")),
					"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("efficiency"), 1.09, 400)) + Emoji.VIOLINS +
							"\nEffect: Multiplies your command income by `x1.1`\nID: `ep` `efficiency`", true);
		} else if((long) data.get("efficiency") < 100) {
			builder.addField("Efficient Practising " + Numbers.formatNumber(data.get("efficiency")),
					"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("efficiency"), 1.09, 400)) + Emoji.VIOLINS +
							"\nEffect: Multiplies your command income by `x1.05`\nID: `ep` `efficiency`", true);
		} else {
			builder.addField("Efficient Practising " + Numbers.formatNumber(data.get("efficiency")),
					"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("efficiency"), 1.09, 400)) + Emoji.VIOLINS +
							"\nEffect: Multiplies your command income by `x1.025`\nID: `ep` `efficiency`", true);
		}
		if((long) data.get("luck") == 50) {
			builder.addField("Lucky Musician :white_check_mark:", "Effect: Increases your gambling multiplier by 0.5%", true);
		} else {
			builder.addField("Lucky Musician " + Numbers.formatNumber(data.get("luck")) + "/`50`",
					"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("luck"), 1.22, 1000)) + Emoji.VIOLINS +
							"\nEffect: Increases your gambling multiplier by `0.5%`\nID: `lm` `luck`", true);
		}
		if((long) data.get("sophistication") == 30) {
			builder.addField("Sophisticated Robbing :white_check_mark:", "Effect: Increases your chance of a successful `/rob` by `0.25%`", true);
		} else {
			builder.addField("Sophisticated Robbing " + Numbers.formatNumber(data.get("sophistication")) + "/`30`",
					"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("sophistication"), 1.35, 5000)) + Emoji.VIOLINS +
							"\nEffect: Increases your chance of a successful `/rob` by `0.25%`\nID: `rob` `sr` `sophistication`", true);
		}
		builder.addField("Magic Find " + Numbers.formatNumber(data.get("magicFindViolins")),
				"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("magicFindViolins"), 3, 1000000)) + Emoji.VIOLINS +
						"\nEffect: `+1` Magic Find\nID: `magicfindviolins` `magicfindv` `mfv`", true);
		if((boolean) data.get("insurance")) {
			builder.addField("Ling Ling Insurance :white_check_mark:", "Effect: When robbed, this will protect `50%` of violins from being stolen", true);
		} else {
			builder.addField("Ling Ling Insurance :x:",
					"Price: `2 500 000`" + Emoji.VIOLINS +
							"\nEffect: When robbed, this will protect `50%` of violins from being stolen\nID: `insurance`", true);
		}
		if((boolean) data.get("timeCrunch")) {
			builder.addField("Time Crunch :white_check_mark:", "Effect: Decreases cooldowns of income commands by `33%`", true);
		} else {
			builder.addField("Time Crunch :x:",
					"Price: `120 000 000`" + Emoji.VIOLINS +
							"\nEffect: Decreases cooldowns of income commands by `33%`\nID: `timecrunch` `tc`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void medalUpgrades() {
		builder.setTitle("**__Medal Upgrades__**")
				.addField("Extra Income " + Numbers.formatNumber(data.get("moreIncome")),
						"Price: " + Numbers.formatNumber(((long) data.get("moreIncome") + 1)) + Emoji.MEDALS +
								"\nEffect: `+2 000`" + Emoji.VIOLINS + "/hour\nID: `moreincome` `income`", true)
				.addField("Extra Command Income " + Numbers.formatNumber(data.get("moreCommandIncome")),
						"Price: " + Numbers.formatNumber((long) Math.pow(2, (long) data.get("moreCommandIncome"))) + Emoji.MEDALS +
								"\nEffect: `x1.3` command income\nID: `morecommandincome` `commandincome` `cmdincome`", true)
				.addField("Higher Gamble Limit " + Numbers.formatNumber(data.get("moreMulti")),
						"Price: " + Numbers.formatNumber(((long) data.get("moreMulti") + 1)) + Emoji.MEDALS +
								"\nEffect: `+1x` Gamble Cap\nID: `moremulti` `multi`", true)
				.addField("Higher Rob Success Rate " + Numbers.formatNumber(data.get("moreRob")),
						"Price: " + Numbers.formatNumber((long) Math.pow(2, (long) data.get("moreRob"))) + Emoji.MEDALS +
								"\nEffect: Increases the chance of successfully robbing someone by `0.3%`\nID: `morerob` `robbing`", true)
				.addField("Magic Find " + Numbers.formatNumber(data.get("magicFindMedals")),
						"Price: " + Numbers.formatNumber((long) Math.pow(2, (long) data.get("magicFindMedals"))) + Emoji.MEDALS +
								"\nEffect: `+1` Magic Find\nID: `magicfindmedals` `magicfindm` `mfm`", true);
		if((boolean) data.get("shield")) {
			builder.addField("Steal Shield :white_check_mark:", "Effect: Advanced technology takes back `50%` of violins when you get robbed", true);
		} else {
			builder.addField("Steal Shield :x:",
					"Price: `10`" + Emoji.MEDALS +
							"\nEffect: Advanced technology takes back `50%` of violins when you get robbed\nID: `shield`", true);
		}
		if((boolean) data.get("duplicator")) {
			builder.addField("Violin Duplicator :white_check_mark:", "Effect: The Vengeful God of Ben Lee doubles all violins you steal", true);
		} else {
			builder.addField("Violin Duplicator :x:",
					"Price: `15`" + Emoji.MEDALS +
							"\nEffect: The Vengeful God of Ben Lee doubles all violins you steal\nID: `duplicator`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void woodwinds() {
		if(!(boolean) data.get("orchestra")) {
			e.reply("You need to have an orchestra to access these upgrades!  Check out Page 6 for more information.");
			return;
		}
		builder.setTitle("__**Woodwinds**__");
		if((boolean) data.get("piccolo")) {
			builder.addField("Piccolo :white_check_mark:", "Effect: `+30`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Piccolo :x:",
					"Price: `200 000`" + Emoji.VIOLINS +
							"\nEffect: `+30`" + Emoji.VIOLINS + "/hour\nID: `piccolo`", true);
		}
		if((long) data.get("flute") == 4) {
			builder.addField("Flutes :white_check_mark:", "Effect: `+60`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Flutes " + Numbers.formatNumber(data.get("flute")) + "/`4`",
					"Price: " + Numbers.formatNumber(250000 * ((long) data.get("flute") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+60`" + Emoji.VIOLINS + "/hour\nID: `flute`", true);
		}
		if((long) data.get("oboe") == 4) {
			builder.addField("Oboes :white_check_mark:", "Effect: `+50`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Oboes " + Numbers.formatNumber(data.get("oboe")) + "/`4`",
					"Price: " + Numbers.formatNumber(250000 * ((long) data.get("oboe") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+50`" + Emoji.VIOLINS + "/hour\nID: `oboe`", true);
		}
		if((long) data.get("clarinet") == 4) {
			builder.addField("Clarinets :white_check_mark:", "Effect: `+40`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Clarinets " + Numbers.formatNumber(data.get("clarinet")) + "/`4`",
					"Price: " + Numbers.formatNumber(200000 * ((long) data.get("clarinet") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+40`" + Emoji.VIOLINS + "/hour\nID: `clarinet`", true);
		}
		if((long) data.get("bassoon") == 4) {
			builder.addField("Bassoons :white_check_mark:", "Effect: `+40`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Bassoons " + Numbers.formatNumber(data.get("bassoon")) + "/`4`",
					"Price: " + Numbers.formatNumber(200000 * ((long) data.get("bassoon") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+40`" + Emoji.VIOLINS + "/hour\nID: `bassoon`", true);
		}
		if((boolean) data.get("contraBassoon")) {
			builder.addField("Contrabassoon :white_check_mark:", "Effect: `+30`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Contrabassoon :x:",
					"Price: `250 000`" + Emoji.VIOLINS +
							"\nEffect: `+30`" + Emoji.VIOLINS + "/hour\nID: `contrabassoon` `cb`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void brass() {
		if(!(boolean) data.get("orchestra")) {
			e.reply("You need to have an orchestra to access these upgrades!  Check out Page 6 for more information.");
			return;
		}
		builder.setTitle("__**Brass and Percussion**__");
		if((long) data.get("horn") == 8) {
			builder.addField("French Horns :white_check_mark:", "Effect: `+40`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("French Horns " + Numbers.formatNumber(data.get("horn")) + "/`8`",
					"Price: " + Numbers.formatNumber(200000 * ((long) data.get("horn") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+40`" + Emoji.VIOLINS + "/hour\nID: `horn`", true);
		}
		if((long) data.get("trumpet") == 4) {
			builder.addField("Trumpet :white_check_mark:", "Effect: `+30`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Trumpet " + Numbers.formatNumber(data.get("trumpet")) + "/`4`",
					"Price: " + Numbers.formatNumber(200000 * ((long) data.get("trumpet") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+30`" + Emoji.VIOLINS + "/hour\nID: `trumpet`", true);
		}
		if((long) data.get("trombone") == 6) {
			builder.addField("Trombone :white_check_mark:", "Effect: `+20`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Trombone " + Numbers.formatNumber(data.get("trombone")) + "/`6`",
					"Price: " + Numbers.formatNumber(200000 * ((long) data.get("trombone") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+20`" + Emoji.VIOLINS + "/hour\nID: `trombone`", true);
		}
		if((long) data.get("tuba") == 2) {
			builder.addField("Tuba :white_check_mark:", "Effect: `+20`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Tuba " + Numbers.formatNumber(data.get("tuba")) + "/`2`",
					"Price: " + Numbers.formatNumber(200000 * ((long) data.get("tuba") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+20`" + Emoji.VIOLINS + "/hour\nID: `tuba`", true);
		}
		if((long) data.get("timpani") == 2) {
			builder.addField("Timpani :white_check_mark:", "Effect: `+60`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Timpani " + Numbers.formatNumber(data.get("timpani")) + "/`2`",
					"Price: " + Numbers.formatNumber(250000 * ((long) data.get("timpani") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+60`" + Emoji.VIOLINS + "/hour\nID: `timpani`", true);
		}
		if((long) data.get("percussion") == 2) {
			builder.addField("Percussionists :white_check_mark:", "Effect: `+10`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Percussionists " + Numbers.formatNumber(data.get("percussion")) + "/`2`",
					"Price: " + Numbers.formatNumber(100000 * ((long) data.get("percussion") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+10`" + Emoji.VIOLINS + "/hour\nID: `percussion`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void strings() {
		if(!(boolean) data.get("orchestra")) {
			e.reply("You need to have an orchestra to access these upgrades!  Check out Page 6 for more information.");
			return;
		}
		builder.setTitle("__**Strings**__");
		if((long) data.get("violin1") == 20) {
			builder.addField("Violin I :white_check_mark:", "Effect: `+70`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Violin I " + Numbers.formatNumber(data.get("violin1")) + "/`20`",
					"Price: " + Numbers.formatNumber(450000 * (long) data.get("violin1")) + Emoji.VIOLINS +
							"\nEffect: `+70`" + Emoji.VIOLINS + "/hour\nID: `first` `violin1`", true);
		}
		if((long) data.get("violin2") == 20) {
			builder.addField("Violin II :white_check_mark:", "Effect: `+60`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Violin II " + Numbers.formatNumber(data.get("violin2")) + "/`20`",
					"Price: " + Numbers.formatNumber(350000 * (long) data.get("violin2")) + Emoji.VIOLINS +
							"\nEffect: `+60`" + Emoji.VIOLINS + "/hour\nID: `second` `violin2`", true);
		}
		if((long) data.get("cello") == 15) {
			builder.addField("Cellos :white_check_mark:", "Effect: `+50`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Cellos " + Numbers.formatNumber(data.get("cello")) + "/`15`",
					"Price: " + Numbers.formatNumber(300000 * ((long) data.get("cello") + 1)) + Emoji.VIOLINS +
					"\nEffect: `+50`" + Emoji.VIOLINS + "/hour\nID: `cello`", true);
		}
		if((long) data.get("doubleBass") == 5) {
			builder.addField("Double Basses :white_check_mark:", "Effect: `+50`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Double Basses " + Numbers.formatNumber(data.get("doubleBass")) + "/`5`",
					"Price: " + Numbers.formatNumber(300000 * ((long) data.get("doubleBass") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+50`" + Emoji.VIOLINS + "/hour\nID: `doublebass` `db`", true);
		}
		if((long) data.get("piano") == 2) {
			builder.addField("Pianists :white_check_mark:", "Effect: `+110`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Pianists " + Numbers.formatNumber(data.get("piano")) + "/`2`",
					"Price: " + Numbers.formatNumber(750000 * ((long) data.get("piano") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+110`" + Emoji.VIOLINS + "/hour\nID: `piano`", true);
		}
		if((boolean) data.get("harp")) {
			builder.addField("Harp :white_check_mark:", "Effect: `+80`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Harp :x:",
					"Price: `350 000`" + Emoji.VIOLINS +
							"\nEffect: `+80`" + Emoji.VIOLINS + "/hour\nID: `harp`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void choir() {
		if(!(boolean) data.get("orchestra")) {
			e.reply("You need to have an orchestra to access these upgrades!  Check out Page 6 for more information.");
			return;
		}
		builder.setTitle("__**Choir**__");
		if((long) data.get("soprano") == 20) {
			builder.addField("Sopranos :white_check_mark:", "Effect: `+30`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Sopranos " + Numbers.formatNumber(data.get("soprano")) + "/`20`",
					"Price: " + Numbers.formatNumber(80000 * ((long) data.get("soprano") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+30`" + Emoji.VIOLINS + "/hour\nID: `soprano`", true);
		}
		if((long) data.get("alto") == 20) {
			builder.addField("Altos :white_check_mark:", "Effect: `+20`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Altos " + Numbers.formatNumber(data.get("alto")) + "/`20`",
					"Price: " + Numbers.formatNumber(60000 * ((long) data.get("alto") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+20`" + Emoji.VIOLINS + "/hour\nID: `alto`", true);
		}
		if((long) data.get("tenor") == 20) {
			builder.addField("Tenors :white_check_mark:", "Effect: `+20`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Tenors " + Numbers.formatNumber(data.get("tenor")) + "/`20`",
					"Price: " + Numbers.formatNumber(60000 * ((long) data.get("tenor") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+20`" + Emoji.VIOLINS + "/hour\nID: `tenor`", true);
		}
		if((long) data.get("bass") == 20) {
			builder.addField("Basses :white_check_mark:", "Effect: `+20`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Basses " + Numbers.formatNumber(data.get("bass")) + "/`20`",
					"Price: " + Numbers.formatNumber(60000 * ((long) data.get("bass") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+20`" + Emoji.VIOLINS + "/hour\nID: `bass`", true);
		}
		if((long) data.get("soloist") == 4) {
			builder.addField("Vocal Soloists :white_check_mark:", "Effect: `+60`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Vocal Soloists " + Numbers.formatNumber(data.get("soloist")) + "/`4`",
					"Price: " + Numbers.formatNumber(250000 * ((long) data.get("soloist") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+60`" + Emoji.VIOLINS + "/hour\nID: `soloist`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void teacherUpgrades() {
		if(!(boolean) data.get("certificate")) {
			e.reply("You need to have a teaching certificate to access these upgrades!  Check out Page 6 for more information.");
			return;
		}
		builder.setTitle("__**Teacher Upgrades**__")
				.addField("More Students " + Numbers.formatNumber(data.get("students")),
						"Price: " + Numbers.formatNumber(Numbers.itemCost((long) data.get("students"), 1.9, 1000000)) + Emoji.VIOLINS +
								"\nEffect: `+2 000`" + Emoji.VIOLINS + "/hour, `x1.15` violins from `/teach`\nID: `students`", true);
		if((long) data.get("lessonCharge") == 5) {
			builder.addField("Higher Lesson Rates :white_check_mark:", "Effect: `+3 000`" + Emoji.VIOLINS + "/hour, x1.1 violins from `/teach`", true);
		} else {
			builder.addField("Higher Lesson Rates " + Numbers.formatNumber(data.get("lessonCharge")) + "/`5`",
					"Price: " + Numbers.formatNumber(3000000 * ((long) data.get("lessonCharge") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+3 000`" + Emoji.VIOLINS + "/hour, x1.1 violins from `/teach`\nID: `lessoncharge` `pricing`", true);
		}
		if((long) data.get("training") == 10) {
			builder.addField("Teacher Training :white_check_mark:", "Effect: `+1 000`" + Emoji.VIOLINS + "/hour, `x1.05` violins from `/teach`", true);
		} else {
			builder.addField("Teacher Training " + Numbers.formatNumber(data.get("training")) + "/`10`",
					"Price: " + Numbers.formatNumber(2000000 * ((long) data.get("training") + 1)) + Emoji.VIOLINS +
							"\nEffect: `+1 000`" + Emoji.VIOLINS + "/hour, `x1.05` violins from `/teach`\nID: `training`", true);
		}
		if((boolean) data.get("studio")) {
			builder.addField("Teaching Studio :white_check_mark:", "Effect: `+5 000`" + Emoji.VIOLINS + "/hour", true);
		} else {
			builder.addField("Teaching Studio :x:",
					"Price: `20 000 000`" + Emoji.VIOLINS +
							"\nEffect: `+5 000`" + Emoji.VIOLINS + "/hour\nID: `studio`", true);
		}
		if((boolean) data.get("longerLessons")) {
			builder.addField("Longer Lessons :white_check_mark:", "Effect: `x2` violins from `/teach`", true);
		} else {
			builder.addField("Longer Lessons :x:",
					"Price: `30 000 000`" + Emoji.VIOLINS +
							"\nEffect: `x2` violins from `/teach`\nID: `longerlessons` `longer`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void bankUpgrades() {
		if(!(boolean) data.get("orchestra")) {
			e.reply("You need to have an orchestra to access these upgrades!  Check out Page 6 for more information.");
			return;
		}
		builder.setTitle("__**Bank Upgrades**__")
				.addField("Storage Space " + Numbers.formatNumber(data.get("storage")),
						"Price: " + Numbers.formatNumber(3 * (long) data.get("storage")) + Emoji.MEDALS +
								"\nEffect: +" + Numbers.formatNumber(Numbers.maxBank((long) data.get("storage") + 1, (long) data.get("benevolentBankers"))
								- Numbers.maxBank((long) data.get("storage"), (long) data.get("benevolentBankers"))) + " bankspace\nID: `storage`", true);
		if((boolean) data.get("moreInterest")) {
			builder.addField("Higher Interest :white_check_mark:", "Effect: Gain `2%` interest instead of `1%`", true);
		} else {
			builder.addField("Higher Interest :x:",
					"Price: `15`" + Emoji.MEDALS +
							"\nEffect: Gain `2%` interest instead of `1%`\nID: `moreinterest`", true);
		}
		if((boolean) data.get("lessPenalty")) {
			builder.addField("Lower Loan Interest :white_check_mark:", "Effect: Decrease loan penalty rate to `9%`", true);
		} else {
			builder.addField("Lower Loan Interest :x:",
					"Price: `15`" + Emoji.MEDALS +
							"\nEffect: Decrease loan penalty rate to `9%`\nID: `lesspenalty`", true);
		}
		e.replyEmbeds(builder.build());
	}
	
	public static void upgrades(GenericDiscordEvent event, int page) {
		e = event;
		data = LoadData.loadData(event);
		builder = new EmbedBuilder()
				.setColor(Color.decode((String) data.get("color")))
				.setFooter("Violins: " + Numbers.formatNumber(data.get("violins")) +
						"\nMedals: " + Numbers.formatNumber(data.get("medals")) +
						"\nUse `/buy [ID]` to buy an upgrade!", event.getJDA().getSelfUser().getAvatarUrl());
		switch(page) {
			case 1 -> incomeUpgrades();
			case 2 -> woodwinds();
			case 3 -> brass();
			case 4 -> strings();
			case 5 -> choir();
			case 6 -> orchMiscUpgrades();
			case 7 -> teacherUpgrades();
			case 8 -> otherMiscUpgrades();
			case 9 -> medalUpgrades();
			case 10 -> bankUpgrades();
			default -> e.reply("""
					You did not provide a valid page number!  Current Pages
					`1` for Income Upgrades
					`2` for Woodwinds
					`3` for Brass and Percussion
					`4` for Strings
					`5` for Choir
					`6` for Miscellaneous Orchestra Items
					`7` for Teacher Upgrades
					`8` for Other Miscellaneous Upgrades
					`9` for Medal Upgrades
					`10` for Bank Upgrades""");
		}
	}
}