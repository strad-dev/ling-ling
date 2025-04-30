package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processes.DatabaseManager;
import processes.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Market {
	private static EmbedBuilder builder;
	private static JSONObject data;
	private static GenericDiscordEvent e;
	private static String emoji;
	private static long amount;
	private static long price;
	private static String item;

	public static void itemSwitch(String input) {
		switch(input) {
			case "grains" -> {
				emoji = Emoji.GRAINS;
				item = "grains";
			}
			case "horseHair", "horsehair", "hair" -> {
				emoji = Emoji.HORSE_HAIR;
				item = "horseHair";
			}
			case "pineSap", "pinesap", "sap" -> {
				emoji = Emoji.SAP;
				item = "pineSap";
			}
			case "plastic" -> {
				emoji = Emoji.PLASTIC;
				item = "plastic";
			}
			case "steel" -> {
				emoji = Emoji.STEEL;
				item = "steel";
			}
			case "teaBase", "teabase", "tea" -> {
				emoji = Emoji.TEABAG;
				item = "teaBase";
			}
			case "water" -> {
				emoji = Emoji.WATER;
				item = "water";
			}
			case "wood" -> {
				emoji = Emoji.WOOD;
				item = "wood";
			}
			case "none" -> {
				emoji = "";
				item = "none";
			}
			default -> {
				emoji = "";
				item = "invalid";
			}
		}
	}

	public static void showItemInfo() {
		ArrayList<String> files = DatabaseManager.getItemData(item);
		builder.setTitle("Information for " + item + " " + emoji);
		if(files == null || files.getFirst().isEmpty()) {
			builder.addField("No items found!", "Unfortunately, nobody is selling this item.  Check back later!", false);
		} else {
			StringBuilder line = new StringBuilder();
			for(int i = 0; i < Math.min(10, files.size()); i++) {
				String order = files.get(i);
				// price count ID
				String[] nameSplit = order.split(" ");
				if(nameSplit[2].equals(e.getAuthor().getId())) {
					line.append(Utils.formatNumber(Long.parseLong(nameSplit[1]))).append("x for ").append(Utils.formatNumber(Long.parseLong(nameSplit[0]))).append(Emoji.VIOLINS + " **Your Order**\n");
					continue;
				}
				line.append(Utils.formatNumber(Long.parseLong(nameSplit[1]))).append("x for ").append(Utils.formatNumber(Long.parseLong(nameSplit[0]))).append(Emoji.VIOLINS + "\n");
			}
			if(files.size() > 10) {
				line.append("+").append(Utils.formatNumber((long) files.size() - 10)).append(" more");
			}
			builder.addField("Best Sell Offers", line.toString(), false);
		}
		e.replyEmbeds(builder.build());
	}

	public static void showAllInfo() {
		ArrayList<Document> allItems = DatabaseManager.getAllData("Market");
		builder.setTitle("Information for All Items");
		for(Document document : allItems) {
			JSONParser parser = new JSONParser();
			JSONObject file;
			try {
				file = (JSONObject) parser.parse(document.toJson());
			} catch(Exception exception) {
				continue;
			}
			String currentItem = (String) file.get("item");
			itemSwitch(currentItem);
			ArrayList<String> itemData = new ArrayList<>(Arrays.asList(((String) file.get("data")).split("\n")));
			itemData.sort(new MarketComparator());
			if(itemData.size() == 1 && itemData.getFirst().isEmpty()) {
				builder.addField(currentItem, "No Sell Offers!", true);
			} else {
				String[] fileName = itemData.getFirst().split(" ");
				builder.addField(currentItem + " " + emoji, "Price: " + Utils.formatNumber(Long.parseLong(fileName[0])) + Emoji.VIOLINS, true);
			}
		}
		e.replyEmbeds(builder.build());
	}

	public static void buy() {
		JSONParser parser = new JSONParser();
		ArrayList<String> orders = DatabaseManager.getItemData(item);
		if(orders == null || orders.getFirst().isEmpty()) {
			e.reply("Nobody is selling this item!");
			return;
		}
		long paid = 0;
		long gained = 0;
		long violins = (long) data.get("violins");
		boolean ranOutOfOrders = false;
		boolean ranOutOfViolins = false;
		for(int i = 0; amount > 0; i++) {
			if(i == orders.size()) {
				ranOutOfOrders = true;
				break;
			}
			String[] orderData = orders.get(i).split(" ");
			long offerAmount = Long.parseLong(orderData[1]);
			long offerPrice = Long.parseLong(orderData[0]);
			String offererID = orderData[2];
			if(offererID.equals(e.getAuthor().getId())) {
				continue;
			}
			JSONObject tempData = DatabaseManager.getDataById("Economy Data", offererID);
			if(tempData == null) {
				continue;
			}
			long price;
			long purchased;
			if(amount < offerAmount) {
				price = amount * offerPrice;
				purchased = amount;
				if(price > violins) {
					ranOutOfViolins = true;
					break;
				}
				offerAmount -= amount;
				gained += amount;
				paid += price;
				orders.set(i, offerPrice + " " + offerAmount + " " + offererID);
				try {
					if((boolean) tempData.get("DMs")) {
						long finalAmount = amount;
						Objects.requireNonNull(e.getJDA().getUserById(offererID)).openPrivateChannel().queue((channel) -> channel.sendMessage("Someone just purchased " + Utils.formatNumber(finalAmount) + emoji + " at " + Utils.formatNumber(offerPrice) + Emoji.VIOLINS + " each.  You made " + Utils.formatNumber((long) (price * 0.99)) + Emoji.VIOLINS + "!\n*Ling Ling taxed you " + Utils.formatNumber((long) (price * 0.01)) + Emoji.VIOLINS + "*").queue());
					}
				} catch(Exception exception) {
					// nothing here
				}
				tempData.replace("violins", (long) tempData.get("violins") + (long) (price * 0.99));
				tempData.replace("itemsSold", (long) tempData.get("itemsSold") + gained);
				tempData.replace("moneyEarned", (long) tempData.get("moneyEarned") + (long) (price * 0.99));
				tempData.replace("taxPaid", (long) tempData.get("taxPaid") + (long) (price * 0.01));
				amount = 0;
				builder = new EmbedBuilder()
						.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
						.setColor(Color.GREEN)
						.setTitle("**__Sell Offer Partially Filled__**")
						.addField("Buyer: " + data.get("discordName") + " " + e.getAuthor().getId(), "Seller: " + tempData.get("discordName") + " `" + tempData.get("discordID") + "`\nItem: " + item + "\nPurchased: " + purchased + "\nPrice: " + price, false);
				Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("1028934753270894592")).sendMessageEmbeds(builder.build()).queue();
			} else {
				price = offerAmount * offerPrice;
				purchased = offerAmount;
				if(price > violins) {
					ranOutOfViolins = true;
					break;
				}
				amount -= offerAmount;
				gained += offerAmount;
				paid += price;
				orders.set(i, "");
				try {
					if((boolean) tempData.get("DMs")) {
						long finalOfferAmount = offerAmount;
						Objects.requireNonNull(e.getJDA().getUserById(offererID)).openPrivateChannel().queue((channel) -> channel.sendMessage("Someone just purchased " + Utils.formatNumber(finalOfferAmount) + emoji + " at " + Utils.formatNumber(offerPrice) + Emoji.VIOLINS + " each.  You made " + Utils.formatNumber((long) (price * 0.99)) + Emoji.VIOLINS + "!\n*Ling Ling taxed you " + Utils.formatNumber((long) (price * 0.01)) + Emoji.VIOLINS + "*").queue());
					}
				} catch(Exception exception) {
					// nothing here
				}
				tempData.replace("violins", (long) tempData.get("violins") + (long) (price * 0.99));
				tempData.replace("itemsSold", (long) tempData.get("itemsSold") + gained);
				tempData.replace("moneyEarned", (long) tempData.get("moneyEarned") + (long) (price * 0.99));
				tempData.replace("taxPaid", (long) tempData.get("taxPaid") + (long) (price * 0.01));
				builder = new EmbedBuilder()
						.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
						.setColor(Color.GREEN)
						.setTitle("**__Sell Offer Filled__**")
						.addField("Buyer: " + data.get("discordName") + " " + e.getAuthor().getId(), "Seller: " + tempData.get("discordName") + " `" + tempData.get("discordID") + "`\nItem: " + item + "\n# Purchased: " + purchased + "\nPrice: " + price, false);
				Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("1028934753270894592")).sendMessageEmbeds(builder.build()).queue();
			}
			DatabaseManager.saveDataForUser("Economy Data", offererID, tempData);
		}
		data.replace("violins", (long) data.get("violins") - paid);
		data.replace("itemsBought", (long) data.get("itemsBought") + gained);
		data.replace("moneySpent", (long) data.get("moneySpent") + paid);
		data.replace(item, (long) data.get(item) + gained);
		if(!ranOutOfViolins && gained == 0) {
			e.reply("There was none of this item for sale!");
		} else if(ranOutOfViolins && gained == 0) {
			e.reply("You don't have enough violins to purchase anything!  Try lowering the amount of items you're buying.");
		} else if(ranOutOfOrders) {
			e.reply("You purchased " + Utils.formatNumber(gained) + emoji + " for " + Utils.formatNumber(paid) + Emoji.VIOLINS + "\nAverage price paid: " + Utils.formatNumber(paid / gained) + Emoji.VIOLINS + "\n*you kind of bought out everything...*");
		} else if(ranOutOfViolins && gained > 0) {
			e.reply("You purchased " + Utils.formatNumber(gained) + emoji + " for " + Utils.formatNumber(paid) + Emoji.VIOLINS + "\nAverage price paid: " + Utils.formatNumber(paid / gained) + Emoji.VIOLINS + "\n*you ran out of violins though...*");
		} else {
			e.reply("You purchased " + Utils.formatNumber(gained) + emoji + " for " + Utils.formatNumber(paid) + Emoji.VIOLINS + "\nAverage price paid: " + Utils.formatNumber(paid / gained) + Emoji.VIOLINS);
		}
		Achievement.calculateAchievement(e, data, "moneySpent", "Big Spender");
		SaveData.saveData(e, data);
		DatabaseManager.saveMarketData(item, orders);
	}

	public static void sell() {
		long userAmount = (long) data.get(item);
		if(userAmount == 0) {
			e.reply("You don't have any of this item!");
			return;
		}
		ArrayList<String> orders = DatabaseManager.getItemData(item);
		if(price == -1 && (orders == null || orders.getFirst().isEmpty())) {
			e.reply("Nobody is selling this item!  Be the one to set the price!");
			return;
		}
		if(amount > userAmount) {
			amount = userAmount;
		}
		if(amount <= 0) {
			e.reply("You cannot sell a nonpositive amount of items, don't try to fool me.");
			return;
		}
		if(price == -1) {
			price = Long.parseLong(orders.getFirst().split(" ")[0]) - 1;
		}
		if(price <= 0) {
			e.reply("You cannot sell items for a negative amount of violins, I know that scamming trick.");
			return;
		}
		String newString = price + " " + amount + " " + e.getAuthor().getId();
		assert orders != null;
		if(orders.contains(newString)) {
			int index = orders.indexOf(newString);
			orders.set(index, price + " " + (amount + Long.parseLong(orders.get(index).split(" ")[2])) + " " + e.getAuthor().getId());
		} else {
			orders.add(newString);
		}
		data.replace(item, (long) data.get(item) - amount);
		e.reply("You set up a Sell Offer for " + Utils.formatNumber(amount) + emoji + " at " + Utils.formatNumber(price) + Emoji.VIOLINS + " per!");
		SaveData.saveData(e, data);
		DatabaseManager.saveMarketData(item, orders);
		builder = new EmbedBuilder()
				.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
				.setColor(Color.BLUE)
				.setTitle("**__Sell Offer Setup__**")
				.addField("User: " + e.getAuthor().getGlobalName() + " " + e.getAuthor().getId(), "Item: " + item + "\nAmount: " + Utils.formatNumber(amount) + "\nPrice: " + Utils.formatNumber(price), false);
		Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("1028934753270894592")).sendMessageEmbeds(builder.build()).queue();
	}

	public static void viewOffers() {
		ArrayList<Document> allItems = DatabaseManager.getAllData("Market");
		for(Document file : allItems) {
			JSONParser parser = new JSONParser();
			JSONObject data;
			try {
				data = (JSONObject) parser.parse(file.toJson());
			} catch(Exception exception) {
				continue;
			}
			itemSwitch((String) data.get("item"));
			String[] orders = ((String) data.get("data")).split("\n");
			if(orders.length == 1 && orders[0].isEmpty()) {
				builder.addField(item + " " + emoji, "No offers!", true);
				continue;
			}
			StringBuilder stringBuilder = new StringBuilder();
			long count = 0;
			for(String order : orders) {
				String[] orderArray = order.split(" ");
				if(orderArray[2].equals(e.getAuthor().getId())) {
					String toAppend = Utils.formatNumber(Long.parseLong(orderArray[1])) + emoji + " for " + Utils.formatNumber(Long.parseLong(orderArray[0])) + Emoji.VIOLINS + " per" + "\n";
					if(stringBuilder.length() + toAppend.length() > 1012) {
						count++;
					} else {
						stringBuilder.append(Utils.formatNumber(Long.parseLong(orderArray[1]))).append(emoji).append(" for ").append(Utils.formatNumber(Long.parseLong(orderArray[0]))).append(Emoji.VIOLINS + " per").append("\n");
					}
				}
			}
			if(stringBuilder.isEmpty()) {
				stringBuilder.append("No offers!");
			}
			if(count > 0) {
				stringBuilder.append("+").append(Utils.formatNumber(count)).append(" more ");
			}
			builder.addField(item + emoji, stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString(), true);
		}
		builder.setTitle("Your Sell Offers");
		e.replyEmbeds(builder.build());
	}

	public static void cancelOffers() {
		ArrayList<Document> allItems = DatabaseManager.getAllData("Market");
		StringBuilder stringBuilder = new StringBuilder();
		for(Document file : allItems) {
			JSONParser parser = new JSONParser();
			JSONObject itemData;
			try {
				itemData = (JSONObject) parser.parse(file.toJson());
			} catch(Exception exception) {
				continue;
			}
			itemSwitch((String) itemData.get("item"));
			String[] orders = ((String) itemData.get("data")).split("\n");
			if(orders.length == 1 && orders[0].isEmpty()) {
				continue;
			}
			for(int i = 0; i < orders.length; i++) {
				String order = orders[i];
				String[] orderArray = order.split(" ");
				if(orderArray[2].equals(e.getAuthor().getId())) {
					stringBuilder.append(Utils.formatNumber(Long.parseLong(orderArray[1]))).append(emoji).append("\n");
					orders[i] = "";
					long num;
					if(data.get(item) == null) {
						num = 0;
					} else {
						num = (long) data.get(item);
					}
					data.replace(item, num + Long.parseLong(orderArray[1]));
				}
			}
			DatabaseManager.saveMarketData(item, new ArrayList<>(Arrays.asList(orders)));
		}
		builder.addField("Items Returned", stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString(), true);
		builder.setTitle("__All Sell Offers Canceled!__");
		e.replyEmbeds(builder.build());
		SaveData.saveData(e, data);
		builder = new EmbedBuilder()
				.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
				.setColor(Color.RED)
				.setTitle("**__Sell Offers Cancelled__**")
				.addField("User: " + e.getAuthor().getGlobalName() + " " + e.getAuthor().getId(), stringBuilder.toString(), false);
		Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("1028934753270894592")).sendMessageEmbeds(builder.build()).queue();
	}

	public static void market(GenericDiscordEvent event, String item1, String action, String amount1, long price1) {
		e = event;
		data = LoadData.loadData(e);
		try {
			amount = Long.parseLong(amount1);
		} catch(Exception exception) {
			if(Objects.equals(amount1, "max")) {
				amount = 2147483647;
			} else {
				amount = 1;
			}
		}
		price = price1;
		itemSwitch(item1);
		if(item.equals("invalid")) {
			e.reply("You did not give a valid item!");
			return;
		}
		builder = new EmbedBuilder()
				.setColor(Color.decode((String) data.get("color")))
				.setFooter("Ling Ling\nReminder: You cannot purchase from your own orders.", e.getJDA().getSelfUser().getAvatarUrl());
		switch(action) {
			// Viewing an item will return the lowest price for that specific item, how many items are in stock, and the 5 best offers.
			// In the future, more statistics may be included in the detailed view.
			// Viewing in general will only return the lowest price for items.
			case "view" -> {
				if(item.equals("none")) {
					showAllInfo();
				} else {
					showItemInfo();
				}
			}

			// Buying requires the Item field.  Not providing an Amount defaults to 1 item.
			// Buy will automatically take the cheapest offers.
			case "buy" -> {
				if(emoji.isEmpty()) {
					e.reply("You have to tell me what item you want to buy.");
				} else {
					buy();
				}
			}

			// Sell requires the Item field.  Not providing an Amount defaults to 1 item.
			// Not providing a Price will automatically put up your offer for 1 violin lower than the lowest offer.
			case "sell" -> {
				if(emoji.isEmpty()) {
					e.reply("You have to tell me what item you want to sell.");
				} else {
					sell();
				}
			}
			case "offers" -> viewOffers();
			case "cancel" -> cancelOffers();
			default -> showAllInfo();
		}
	}
}