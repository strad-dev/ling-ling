package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Utils;

import java.awt.*;

public class Inventory {
	public static void inventory(GenericDiscordEvent e, String user, int page) {
		if(user.isEmpty()) {
			user = e.getAuthor().getId();
		}

		JSONObject data = DatabaseManager.getDataForUser("Economy Data", user);
		if(data == null) {
			e.reply("This save file does not exist!");
			return;
		}

		try {
			user = data.get("discordName").toString();
		} catch(Exception exception) {
			user = "Someone";
		}

		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Color.decode((String) data.get("color")))
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl());
		if(page == -1) {
			builder.setTitle(user + "'s Inventory")
					.addField("Inventory Pages", "\n`1` - Raw Materials\n`2` - Consumables\n`3` - Lootboxes\n`4` - RNG Drops", true);
		} else {
			switch(page) {
				case 1 -> builder.setTitle(user + "'s Inventory - Page 1\nRaw Materials")
						.addField("Rice Grains " + Emoji.GRAINS, "Count: " + Utils.formatNumber(data.get("grains")) + "\nCrafting Ingredient", true)
						.addField("Plastic " + Emoji.PLASTIC, "Count: " + Utils.formatNumber(data.get("plastic")) + "\nCrafting Ingredient", true)
						.addField("Water " + Emoji.WATER, "Count: " + Utils.formatNumber(data.get("water")) + "\nCrafting Ingredient", true)
						.addField("Tea Base " + Emoji.TEABAG, "Count: " + Utils.formatNumber(data.get("teaBase")) + "\nCrafting Ingredient", true)
						.addBlankField(true)
						.addField("Wood " + Emoji.WOOD, "Count: " + Utils.formatNumber(data.get("wood")) + "\nCrafting Ingredient", true)
						.addField("Pine Sap " + Emoji.SAP, "Count: " + Utils.formatNumber(data.get("pineSap")) + "\nCrafting Ingredient", true)
						.addField("Steel " + Emoji.STEEL, "Count: " + Utils.formatNumber(data.get("steel")) + "\nCrafting Ingredient", true)
						.addField("Horse Hair " + Emoji.HORSE_HAIR, "Count: " + Utils.formatNumber(data.get("horseHair")) + "\nCrafting Ingredient", true);
				case 2 -> builder.setTitle(user + "'s Inventory - Page 2\nConsumables")
						.addField("Rice " + Emoji.RICE, "Count: " + Utils.formatNumber(data.get("rice")) +
								"\nUsage: Gives you 2 hours of income\nID: `rice`", true)
						.addField("Bubble Tea " + Emoji.TEA, "Count: " + Utils.formatNumber(data.get("tea")) +
								"\nUsage: Gives you 6 hours of income\nID: `tea`", true)
						.addField("Ling Ling Blessing " + Emoji.BLESSING, "Count: " + Utils.formatNumber(data.get("blessings")) +
								"\nUsage: Gives you 24 hours of income and 1-3 Ling Ling Medals\n`blessings` `blessing`", true)
						.addField("Rosin " + Emoji.ROSIN, "Count: " + Utils.formatNumber(data.get("rosin")) +
								"\nUsage: Allows you to make 25% of your income for 50 hours.  Stackable\nID: `rosin`", true)
						.addBlankField(true)
						.addField("New Strings " + Emoji.STRING, "Count: " + Utils.formatNumber(data.get("string")) +
								"\nUsage: Allows you to make 25% of your income for 100 hours.  Stackable\nID: `string`", true)
						.addField("Bow Hair " + Emoji.BOW_HAIR, "Count: " + Utils.formatNumber(data.get("bowHair")) +
								"\nUsage: Allows you to make 25% of your income for 150 hours.  Stackable\nID: `bowhair` `hair`", true)
						.addBlankField(true)
						.addField("Violin Service " + Emoji.SERVICE, "Count: " + Utils.formatNumber(data.get("violinService")) +
								"\nUsage: Allows you to make 25% of your income for 250 hours.  Stackable\n`violinservice` `service`", true);
				case 3 -> builder.setTitle(user + "'s Inventory - Page 3\nLootboxes")
						.addField("Free Box " + Emoji.FREE_BOX, "Count: " + Utils.formatNumber(data.get("voteBox")) +
								"\nUsage: Gives you bad random items, as decided by RNGesus\nID: `freebox` `votebox` `free` `vote`", true)
						.addField("Gift Box " + Emoji.GIFT_BOX, "Count: " + Utils.formatNumber(data.get("giftBox")) +
								"\nUsage: Gives you miniscully valuable random items, as decided by RNGesus\nID: `giftbox` `gift`", true)
						.addField("Musician Kit " + Emoji.MUSICIAN_KIT, "Count: " + Utils.formatNumber(data.get("kits")) +
								"\nUsage: Gives you somewhat valuable random items, as decided by RNGesus\nID: `kits` `kit`", true)
						.addField("Ling Ling Box " + Emoji.LING_LING_BOX, "Count: " + Utils.formatNumber(data.get("linglingBox")) +
								"\nUsage: Gives you valuable random items, as decided by RNGesus\nID: `linglingbox` `lingling` `llbox`", true)
						.addField("Crazy Person Box " + Emoji.CRAZY_BOX, "Count: " + Utils.formatNumber(data.get("crazyBox")) +
								"\nUsage: Gives you very valuable random items, as decided by RNGesus\nID: `crazybox` `crazy`", true)
						.addField("RNGesus Box " + Emoji.RNGESUS_BOX, "Count: " + Utils.formatNumber(data.get("RNGesusBox")) +
								"\nUsage: Gives you EXTREMELY valuable random items, as decided by RNGesus\nID: `rngesusbox` `rngesus`", true);
				case 4 -> {
					builder.setTitle(user + "'s Inventory - Page 4\nRNG Drops")
							.addField("Extra Income Voucher", "Count: " + Utils.formatNumber(data.get("freeIncome")) +
									"\nEffect: Grants +" + Utils.formatNumber((long) data.get("freeIncome") * 100) + Emoji.VIOLINS + "/hour income", true)
							.addField("Benevolent Banker", "Count: " + Utils.formatNumber(data.get("benevolentBankers")) +
									"\nEffect: Grants +" + Utils.formatNumber(data.get("benevolentBankers")) + "% bank space", true)
							.addField("Item Quality Boost Potion", "Count: " + Utils.formatNumber(data.get("qualityItems")) +
									"\nEffect: Grants +" + Utils.formatNumber((long) data.get("qualityItems") * 5) + "% item duration (where applicable)", true)
							.addField("Medal Generator", "Count: " + Utils.formatNumber(data.get("bonusMedals")) +
									"\nEffect: Grants +" + Utils.formatNumber(data.get("bonusMedals")) + Emoji.MEDALS + " each time `!daily` is run", true)
							.addField("Bonus Interest", "Count: " + Utils.formatNumber(data.get("bonusInterest")) +
									"\nEffect: Grants +`" + ((long) data.get("bonusInterest")) * 0.1 + "`% extra interest", true);
					if((long) data.get("RNGesusItemThatDoesAbsolutelyNothingLMAO") == 0) {
						builder.addField("???", "Count: `0`\nEffect: ???", true);
					} else {
						builder.addField("RNGesus Item That Does Absolutely Nothing LMAO", "Count: " + Utils.formatNumber(data.get("RNGesusItemThatDoesAbsolutelyNothingLMAO")) +
								"\nA completely useless item!  At least you can flex this in your inventory.", true);
					}
				}
				default -> {
					builder.setTitle(user + "'s Inventory - Page ???")
							.addField("404 Not Found", "The page you are looking for doesn't exist!  Run `/inv` to see a list of pages.", true);
				}
			}
		}
		e.replyEmbeds(builder.build());
	}
}