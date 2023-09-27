package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processes.Numbers;
import processes.WindowsExplorerStringComparator;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
			case "horseHair", "hair" -> {
				emoji = Emoji.HORSE_HAIR;
				item = "horseHair";
			}
			case "pineSap", "sap" -> {
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
			case "teaBase", "tea" -> {
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
		}
	}

	public static void showItemInfo() {
		String[] files = new File("Ling Ling Bot Data\\Market\\" + item).list();
		builder.setTitle("Information for " + item + " " + emoji);
		if(files == null || files.length == 0) {
			builder.addField("No items found!", "Unfortunately, nobody is selling this item.  Check back later!", false);
		} else {
			Arrays.sort(files, new WindowsExplorerStringComparator());
			StringBuilder line = new StringBuilder();
			for(int i = 0; i < Math.min(10, files.length); i++) {
				String name = files[i];
				// price count ID
				String[] nameSplit = name.substring(0, name.lastIndexOf('.')).split(" ");
				line.append('`').append(Numbers.formatNumber(Long.parseLong(nameSplit[1]))).append("x` for `").append(Numbers.formatNumber(Long.parseLong(nameSplit[0]))).append("`" + Emoji.VIOLINS + "\n");
			}
			if(files.length > 10) {
				line.append("+`").append(files.length - 10).append("` more");
			}
			builder.addField("Best Sell Offers", line.toString(), false);
		}
		e.replyEmbeds(builder.build());
	}

	public static void showAllInfo() {
		builder.setTitle("Information for All Items");
		File[] files = new File("Ling Ling Bot Data\\Market").listFiles();
		assert files != null;
		for(File file : files) {
			itemSwitch(file.getName());
			String[] names = file.list();
			if(names == null || names.length == 0) {
				builder.addField(file.getName(), "No Sell Offers!", true);
			} else {
				Arrays.sort(names, new WindowsExplorerStringComparator());
				String[] fileName = names[0].split(" ");
				builder.addField(file.getName() + " " + emoji, "Price: `" + Numbers.formatNumber(Long.parseLong(fileName[0])) + "`" + Emoji.VIOLINS, true);
			}
		}
		e.replyEmbeds(builder.build());
	}

	public static void buy() {
		JSONParser parser = new JSONParser();
		String[] files = new File("Ling Ling Bot Data\\Market\\" + item).list();
		if(files == null || files.length == 0) {
			e.reply("Nobody is selling this item!");
			return;
		}
		Arrays.sort(files, new WindowsExplorerStringComparator());
		long paid = 0;
		long gained = 0;
		long violins = (long) data.get("violins");
		boolean ranOutOfOrders = false;
		boolean ranOutOfViolins = false;
		for(int i = 0; amount > 0; i++) {
			if(i == files.length) {
				ranOutOfOrders = true;
				break;
			}
			String realFileName = files[i];
			File file = new File("Ling Ling Bot Data\\Market\\" + item + "\\" + realFileName);
			String[] fileName = realFileName.split(" ");
			long offerAmount = Long.parseLong(fileName[1]);
			long offerPrice = Long.parseLong(fileName[0]);
			String offererID = fileName[2].substring(0, fileName[2].lastIndexOf('.'));
			JSONObject tempData;
			try(FileReader reader = new FileReader("Ling Ling Bot Data\\Economy Data\\" + offererID + ".json")) {
				tempData = (JSONObject) parser.parse(reader);
				reader.close();
			} catch(Exception exception) {
				exception.printStackTrace();
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
				file.renameTo(new File("Ling Ling Bot Data\\Market\\" + item + "\\" + offerPrice + " " + offerAmount + " " + offererID + ".txt"));
				if((boolean) tempData.get("DMs")) {
					long finalAmount = amount;
					Objects.requireNonNull(e.getJDA().getUserById(offererID)).openPrivateChannel().queue((channel) -> channel.sendMessage("Someone just purchased `" + Numbers.formatNumber(finalAmount) + "`" + emoji + " at `" + Numbers.formatNumber(offerPrice) + "`" + Emoji.VIOLINS + " each.  You made `" + Numbers.formatNumber((long) (price * 0.99)) + "`" + Emoji.VIOLINS + "!\n*Ling Ling taxed you `" + Numbers.formatNumber((long) (price * 0.01)) + "`" + Emoji.VIOLINS + "*"));
				}
				tempData.replace("violins", (long) tempData.get("violins") + (long) (price * 0.99));
				tempData.replace("itemsSold", (long) tempData.get("itemsSold") + gained);
				tempData.replace("moneyEarned", (long) tempData.get("moneyEarned") + (long) (price * 0.99));
				tempData.replace("taxPaid", (long) tempData.get("taxPaid") + (long) (price * 0.01));
				amount = 0;
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
				file.delete();
				if((boolean) tempData.get("DMs")) {
					long finalOfferAmount = offerAmount;
					Objects.requireNonNull(e.getJDA().getUserById(offererID)).openPrivateChannel().queue((channel) -> channel.sendMessage("Someone just purchased `" + Numbers.formatNumber(finalOfferAmount) + "x` " + item + " at `" + Numbers.formatNumber(offerPrice) + "`" + Emoji.VIOLINS + " each.  You made `" + Numbers.formatNumber((long) (price * 0.99)) + "`" + Emoji.VIOLINS + "!\n*Ling Ling taxed you `" + Numbers.formatNumber((long) (price * 0.01)) + "`" + Emoji.VIOLINS + "*").queue());
				}
				tempData.replace("violins", (long) tempData.get("violins") + (long) (price * 0.99));
				tempData.replace("itemsSold", (long) tempData.get("itemsSold") + gained);
				tempData.replace("moneyEarned", (long) tempData.get("moneyEarned") + (long) (price * 0.99));
				tempData.replace("taxPaid", (long) tempData.get("taxPaid") + (long) (price * 0.01));
			}
			try(FileWriter writer = new FileWriter("Ling Ling Bot Data\\Economy Data\\" + offererID + ".json")) {
				writer.write(tempData.toJSONString());
				writer.close();
			} catch(Exception exception) {
				// nothing here lmao
			}
			builder = new EmbedBuilder()
					.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
					.setColor(Color.GREEN)
					.setTitle("**__Sell Offer Filled__**")
					.addField("Buyer: " + e.getAuthor().getGlobalName() + " `" + e.getAuthor().getId() + "`", "Seller: <@" + offererID + ">\nItem: " + item + "\n# Purchased: " + purchased + "\nPrice: " + price, false);
			Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("1028934753270894592")).sendMessageEmbeds(builder.build()).queue();
		}
		data.replace("violins", (long) data.get("violins") - paid);
		data.replace("itemsBought", (long) data.get("itemsBought") + gained);
		data.replace("moneySpent", (long) data.get("moneySpent") + paid);
		data.replace(item, (long) data.get(item) + gained);
		if(ranOutOfOrders) {
			e.reply("You purchased `" + gained + "`" + emoji + " for `" + Numbers.formatNumber(paid) + "`" + Emoji.VIOLINS + "\nAverage price paid: `" + Numbers.formatNumber(paid / gained) + "`" + Emoji.VIOLINS + "\n*you kind of bought out everything...*");
		} else if(ranOutOfViolins && gained > 0) {
			e.reply("You purchased `" + gained + "`" + emoji + " for `" + Numbers.formatNumber(paid) + "`" + Emoji.VIOLINS + "\nAverage price paid: `" + Numbers.formatNumber(paid / gained) + "`" + Emoji.VIOLINS + "\n*you ran out of violins though...*");
		} else if(ranOutOfViolins && gained == 0) {
			e.reply("You don't have enough violins to purchase all that!  Try lowering the amount of items you're buying.");
		} else {
			e.reply("You purchased `" + gained + "`" + emoji + " for `" + Numbers.formatNumber(paid) + "`" + Emoji.VIOLINS + "\nAverage price paid: `" + Numbers.formatNumber(paid / gained) + "`" + Emoji.VIOLINS);
		}
		SaveData.saveData(e, data);
	}

	@SuppressWarnings("DataFlowIssue")
	public static void sell() {
		String[] files = new File("Ling Ling Bot Data\\Market\\" + item).list();
		if(price == -1 && (files == null || files.length == 0)) {
			e.reply("Nobody is selling this item!  Be the one to set the price!");
			return;
		}
		long userAmount = (long) data.get(item);
		if(amount > userAmount) {
			amount = userAmount;
		}
		if(amount <= 0) {
			e.reply("You cannot sell a nonpositive amount of items, don't try to fool me.");
			return;
		}
		if(price == -1) {
			price = Long.parseLong(files[0].split(" ")[0]) - 1;
			return;
		}
		if(price <= 0) {
			e.reply("You cannot sell items for a negative amount of violins, I know that scamming trick.");
			return;
		}
		Arrays.sort(files, new WindowsExplorerStringComparator());
		File file = new File("Ling Ling Bot Data\\Market\\" + item + "\\" + price + " " + amount + " " + e.getAuthor().getId() + ".txt");
		try {
			file.createNewFile();
		} catch(Exception exception) {
			file.renameTo(new File("Ling Ling Bot Data\\Market\\" + item + "\\" + price + " " + (amount + file.getName().split(" ")[1]) + " " + e.getAuthor().getId() + ".txt"));
		}
		data.replace(item, (long) data.get(item) - amount);
		e.reply("You set up a Sell Offer for `" + Numbers.formatNumber(amount) + "`" + emoji + " at `" + Numbers.formatNumber(price) + "`" + Emoji.VIOLINS + " per!");
		SaveData.saveData(e, data);
		builder = new EmbedBuilder()
				.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
				.setColor(Color.BLUE)
				.setTitle("**__Sell Offer Setup__**")
				.addField("User: " + e.getAuthor().getGlobalName() + " `" + e.getAuthor().getId() + "`", "Item: " + item + "\nAmount: " + amount + "\nPrice: " + price, false);
		Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("1028934753270894592")).sendMessageEmbeds(builder.build()).queue();
	}

	public static void viewOffers() {
		File[] directories = new File("Ling Ling Bot Data\\Market").listFiles();
		assert directories != null;
		for(File file : directories) {
			String[] orders = file.list();
			itemSwitch(file.getName());
			if(orders == null || orders.length == 0) {
				builder.addField(file.getName() + " " + emoji, "No offers!", true);
				continue;
			}
			StringBuilder stringBuilder = new StringBuilder();
			for(String name : orders) {
				String[] nameArray = name.substring(0, name.lastIndexOf('.')).split(" ");
				if(nameArray[2].equals(e.getAuthor().getId())) {
					stringBuilder.append("`").append(Numbers.formatNumber(Long.parseLong(nameArray[1]))).append("`").append(emoji).append(" for `").append(Numbers.formatNumber(Long.parseLong(nameArray[0]))).append("`" + Emoji.VIOLINS + " per").append("\n");
				}
			}
			if(stringBuilder.isEmpty()) {
				stringBuilder.append("No offers!");
			}
			builder.addField(file.getName() + emoji, stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString(), true);
		}
		builder.setTitle("Your Sell Offers");
		e.replyEmbeds(builder.build());
	}

	public static void cancelOffers() {
		File[] directories = new File("Ling Ling Bot Data\\Market").listFiles();
		assert directories != null;
		StringBuilder stringBuilder = new StringBuilder();
		for(File file : directories) {
			File[] orders = file.listFiles();
			if(orders == null) {
				continue;
			}
			itemSwitch(file.getName());
			for(File order : orders) {
				String[] nameArray = order.getName().substring(0, order.getName().lastIndexOf('.')).split(" ");
				if(nameArray[2].equals(e.getAuthor().getId())) {
					stringBuilder.append("`").append(Numbers.formatNumber(Long.parseLong(nameArray[1]))).append("`").append(emoji).append("\n");
					order.delete();
					data.replace(file.getName(), (long) data.get(file.getName()) + Long.parseLong(nameArray[1]));
				}
			}
		}
		builder.addField("Items Returned", stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString(), true);
		builder.setTitle("__All Sell Offers Canceled!__");
		e.replyEmbeds(builder.build());
		SaveData.saveData(e, data);
		builder = new EmbedBuilder()
				.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
				.setColor(Color.RED)
				.setTitle("**__Sell Offers Cancelled__**")
				.addField("User: " + e.getAuthor().getGlobalName() + " `" + e.getAuthor().getId() + "`", stringBuilder.toString(), false);
		Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("1028934753270894592")).sendMessageEmbeds(builder.build()).queue();
	}

	public static void market(GenericDiscordEvent event, String item1, String action, long amount1, long price1) {
		event.reply("This command is temporarily diabled while Strad wraps his head around how the hell to migrate this command.  Also not like it saw much use anyway...");
		/*e = event;
		data = LoadData.loadData(e);
		amount = amount1;
		price = price1;
		itemSwitch(item1);
		builder = new EmbedBuilder()
				.setColor(Color.decode((String) data.get("color")))
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl());
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
				if(emoji.equals("")) {
					e.reply("You have to tell me what item you want to buy.");
				} else {
					buy();
				}
			}

			// Sell requires the Item field.  Not providing an Amount defaults to 1 item.
			// Not providing a Price will automatically put up your offer for 1 violin lower than the lowest offer.
			case "sell" -> {
				if(emoji.equals("")) {
					e.reply("You have to tell me what item you want to sell.");
				} else {
					sell();
				}
			}
			case "offers" -> viewOffers();
			case "cancel" -> cancelOffers();
			default -> showAllInfo();
		}*/
	}
}