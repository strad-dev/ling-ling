package processes;

import eventListeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.simple.JSONObject;

public class StartBot {
	private static final boolean BETA = true; // TODO UPDATE THIS BEFORE DOING BETAS OR FULL RELEASES

	public static boolean isBeta() {
		return BETA;
	}

	public static void startBot() {
		/* File file = new File("Ling Ling Bot Data\\loadedservers.txt");
		try {
			file.delete();
			file.createNewFile();
		} catch(Exception exception1) {
			//nothing here lol
		} */
		DatabaseManager.connectToDatabase(BETA);
		HypixelManager.connectToHypixel(BETA);
		JSONObject data = DatabaseManager.getMiscData();

		String token;
		String beethoventoken;
		if(BETA) {
			token = "betatoken";
			beethoventoken = "betatoken";
		} else {
			token = "token";
			beethoventoken = "beethoventoken";
		}

		// Start BeethovenBot
		JDA jda;
		assert data != null;
		jda = JDABuilder.create(data.get(beethoventoken).toString(), GatewayIntent.GUILD_INVITES,
						GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.MESSAGE_CONTENT,
						GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS)
				.disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS, CacheFlag.SCHEDULED_EVENTS)
				.addEventListeners(new Autoroles())
				.addEventListeners(new Autounrole())
				.addEventListeners(new Disconnect())
				.addEventListeners(new Join())
				.addEventListeners(new Leave())
				.addEventListeners(new Receiver())
				.addEventListeners(new RoleAdded())
				.addEventListeners(new RoleRemoved())
				// .addEventListeners(new ILoveJava())
				.addEventListeners(new NoInviteLinks())
				.build();
		jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
		jda.getPresence().setActivity(Activity.customStatus("Actively (de)composing."));

		// Start LingLing Bot
		Message.suppressContentIntentWarning();
		jda = JDABuilder.createDefault(data.get(token).toString(), GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS,
						GatewayIntent.GUILD_EMOJIS_AND_STICKERS)
				.disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS, CacheFlag.SCHEDULED_EVENTS)
				.addEventListeners(new Disconnect())
				.addEventListeners(new NewReceiver())
				.addEventListeners(new OldReceiver())
				.useSharding(0, 1)
				.build();
		jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
		jda.getPresence().setActivity(Activity.customStatus("Practising violin 40 hours a day.  Sometimes practises 72 hours a day!"));
		jda.upsertCommand(Commands.slash("h", "Collect all pending hourly income!")).queue();

		OptionData optionData;
			/*
			// ONE TIEM UPSERT OF SLASH COMMANDS


			// help page 1
			jda.upsertCommand(
					Commands.slash("joke", "Gives you a fun music-related joke!")
			).queue();
			jda.upsertCommand(
					Commands.slash("kill", "Totally kills your friends.")
							.addOption(OptionType.STRING, "target", "Who do you want to kill?")
			).queue();
			jda.upsertCommand(
					Commands.slash("emojify", "Turns your message into Emojis.")
							.addOption(OptionType.STRING, "message", "Put your message here.")
			).queue();
			
			// help page 2
			jda.upsertCommand(
					Commands.slash("rules", "Rules of the bot.  VERY IMPORTANT")
			).queue();
			jda.upsertCommand(
					Commands.slash("staff", "Lists the staff of the bot.")
			).queue();
			jda.upsertCommand(
					Commands.slash("poll", "Creates a poll where reactions are used to vote.")
							.addOption(OptionType.STRING, "title", "Name your poll!")
							.addOption(OptionType.STRING, "choices", "List your choices, separated by semicolons.")
			).queue();
			jda.upsertCommand(
					Commands.slash("invite", "Gives you the invite link for the bot.")
			).queue();
			
			data = new OptionData(OptionType.STRING, "page", "Select which question you want answered.")
					.addChoice("No Rob Disabling", "rob")
					.addChoice("No Gifting Violins", "give")
					.addChoice("Getting Luthier Multipliers", "luthier")
					.addChoice("Increase Hourly Income", "hourly")
					.addChoice("Hire an Orchestra", "orchestra")
					.addChoice("hElP mY sAvE wAs dElEtEd", "delete")
					.addChoice("Rob Succeed Chance", "robchance")
					.addChoice("Where Leveling ??!?!?1!?1??!?!1/", "leveling")
					.addChoice("WHY CAN'T I PING AS;DKFJ S;ALFJS;AKL", "ping")
					.addChoice("pls rngesus bless me", "rngesus")
					.addChoice("Luthier Spawn Chance", "luthierchance")
					.addChoice("Magic Find", "magicfind")
					.addChoice("Command Income Multiplier", "commandmulti")
					.addChoice("Market", "market");
			jda.upsertCommand(
					Commands.slash("faq", "The FAQ pages for this bot.")
							.addOptions(data)
			).queue();
			jda.upsertCommand(
					Commands.slash("support", "Gives you the support server for the bot.")
			).queue();
			jda.upsertCommand(
					Commands.slash("website", "Gives you the link to the official website for the bot.")
			).queue();
			jda.upsertCommand(
					Commands.slash("botstats", "Gives stats for the bot.")
			).queue();
			data = new OptionData(OptionType.STRING, "option", "Select which option you want edited.")
					.addChoice("Color", "color")
					.addChoice("DMs", "dms")
					.addChoice("Extra Info", "info");
			jda.upsertCommand(
					Commands.slash("settings", "Edit your personal settings!")
							.addOptions(data)
							.addOption(OptionType.STRING, "newvalue", "Choose the new value you want for the settings.")
			).queue();
			jda.upsertCommand(
					Commands.slash("help", "Main Help page for the bot.")
							.addOption(OptionType.STRING, "page", "Select which help page you want to view.")
			).queue();
			jda.upsertCommand(
					Commands.slash("guide", "Beginnner's Guide for the bot.")
			).queue();
			
			// help page 3
			jda.upsertCommand(
					Commands.slash("start", "Begin your journey!")
			).queue();
			jda.upsertCommand(
					Commands.slash("b", "View your balance.")
							.addOption(OptionType.STRING, "otheruser", "Use this to view another user's profile.")
			).queue();
			jda.upsertCommand(
					Commands.slash("stats", "View your stats.")
							.addOption(OptionType.STRING, "otheruser", "Use this to view another user's stats.")
			).queue();
		optionData = new OptionData(OptionType.STRING, "page", "Select the page to view.")
				.addChoice("Raw Materials", "1")
				.addChoice("Consumables", "2")
				.addChoice("Lootboxes", "3")
				.addChoice("RNG Drops", "4");
		jda.upsertCommand(
				Commands.slash("inv", "View your inventory.")
						.addOptions(optionData)
						.addOption(OptionType.STRING, "otheruser", "Use this to view another user's inventory.")
		).queue();*/
		OptionData data1 = new OptionData(OptionType.STRING, "action", "Choose whether to Buy, Sell, or view an item or your offers.")
				.addChoice("Buy Items", "buy")
				.addChoice("Sell Items", "sell")
				.addChoice("View Item Prices", "view")
				.addChoice("View Your Offers", "offers")
				.addChoice("Cancel Your Offers", "cancel");
		OptionData data2 = new OptionData(OptionType.STRING, "item", "Pick the item you want to Buy, Sell, or View.")
				.addChoice("Grains", "grains")
				.addChoice("Plastic", "plastic")
				.addChoice("Water", "water")
				.addChoice("Tea Base", "teaBase")
				.addChoice("Wood", "wood")
				.addChoice("Pine Sap", "pineSap")
				.addChoice("Steel", "steel")
				.addChoice("Horse Hair", "horseHair");
		jda.upsertCommand(
				Commands.slash("market", "View and interact with the Public Market!")
						.addOptions(data1, data2)
						.addOption(OptionType.STRING, "amount", "Pick the amount you want to Buy or Sell.")
						.addOption(OptionType.INTEGER, "price", "Pick the price you want to Sell your items at.")
		).queue();
			/*optionData = new OptionData(OptionType.STRING, "item", "Choose the item you want to craft.")
					.addChoice("Rice", "rice")
					.addChoice("Bubble Tea", "tea")
					.addChoice("Rosin", "rosin")
					.addChoice("New Strings", "string")
					.addChoice("Bow Hair", "bowHair")
					.addChoice("Violin Service", "violinService")
					.addChoice("1x Luthier", "luthier");
			jda.upsertCommand(
					Commands.slash("craft", "Craft items!")
							.addOptions(optionData)
							.addOption(OptionType.STRING, "amount", "How much do you want to craft?")
			).queue();
			/*jda.upsertCommand(
					Commands.slash("link", "Link your Minecraft acccount!  SEE /rules FOR POLICIES REGARDING MINECRAFT LINKING.")
							.addOption(OptionType.STRING, "ign", "Put in your Minecraft IGN!")
			).queue();
			
			jda.upsertCommand(
					Commands.slash("c", "View your cooldowns.")
			).queue();
			data = new OptionData(OptionType.INTEGER, "page", "Select the page to view.")
					.addChoice("Income Upgrades", 1)
					.addChoice("Woodwinds (Orchestra)", 2)
					.addChoice("Brass (Orchestra)", 3)
					.addChoice("Strings (Orchestra)", 4)
					.addChoice("Choir (Orchestra)", 5)
					.addChoice("Misc Orchestra Upgrades", 6)
					.addChoice("Teaching Upgrades (Certificate)", 7)
					.addChoice("Other Misc Upgrades", 8)
					.addChoice("Medal Upgrades", 9)
					.addChoice("Bank Upgrades (Orchestra)", 10);
			jda.upsertCommand(
					Commands.slash("up", "View your upgrades.")
							.addOptions(data)
			).queue();
			jda.upsertCommand(
					Commands.slash("buy", "Buy something.  CASE SENSITIVE!")
							.addOption(OptionType.STRING, "item", "Pick the item to buy.")
			).queue();
			data = new OptionData(OptionType.STRING, "item", "Choose the item you want to craft.")
					.addChoice("Rice", "rice")
					.addChoice("Bubble Tea", "tea")
					.addChoice("Blessing", "blessings")
					.addChoice("Rosin", "rosin")
					.addChoice("New Strings", "string")
					.addChoice("Bow Hair", "bowHair")
					.addChoice("Violin Service", "violinService")
					.addChoice("Free Box", "voteBox")
					.addChoice("Gift Box", "giftBox")
					.addChoice("Musician Kit", "kits")
					.addChoice("Ling Ling Box", "linglingBox")
					.addChoice("Crazy Person Box", "crazyBox")
					.addChoice("RNGesus Box", "RNGesusBox");
			jda.upsertCommand(
					Commands.slash("use", "Use something.")
							.addOptions(data)
							.addOption(OptionType.STRING, "amount", "How much to use.")
			).queue();
			jda.upsertCommand(
					Commands.slash("claim", "Claim a Free Box, every 12 hours!")
			).queue();
			jda.upsertCommand(
					Commands.slash("gift", "Gift a Friend!")
							.addOption(OptionType.STRING, "otheruser", "Put your friend's ID here.")
			).queue();
		jda.upsertCommand(
				Commands.slash("lb", "View a leaderboard!")
						.addOption(OptionType.STRING, "leaderboard", "Select the leaderboard to view.")
		).queue();
		/*jda.upsertCommand(
				Commands.slash("a", "View your achievements!")
						.addOption(OptionType.STRING, "page", "Select the page to view.")
		).queue();
		jda.upsertCommand(
				Commands.slash("resetscales", "Manually reset your Scale Streak.")
		).queue();
		jda.deleteCommandById("bet").queue();
		/*
		jda.upsertCommand(
				Commands.slash("dep", "Deposit Violins into your bank account.")
						.addOption(OptionType.STRING, "amount", "How much you want to deposit.")
		).queue();
		jda.upsertCommand(
				Commands.slash("with", "Withdraws Violins into your bank account.")
						.addOption(OptionType.STRING, "amount", "How much you want to withdraw.")
		).queue();
		jda.upsertCommand(
				Commands.slash("loan", "Loans Violins from The Bank of TwoSet.")
						.addOption(OptionType.STRING, "amount", "How much you want to loan.")
		).queue();
		jda.upsertCommand(
				Commands.slash("payloan", "Pays back your loan.")
						.addOption(OptionType.STRING, "amount", "How much you want to pay back.")
		).queue();

		// help page 4
		jda.upsertCommand(
				Commands.slash("s", "Plays some scales.")
		).queue();
		jda.upsertCommand(
				Commands.slash("p", "Practises for 45 minutes.")
		).queue();
		jda.upsertCommand(
				Commands.slash("r", "Goes to Rehearsal.")
		).queue();
		jda.upsertCommand(
				Commands.slash("pf", "Gives a performance.")
		).queue();
		jda.upsertCommand(
				Commands.slash("t", "Teach a lesson.")
		).queue();
		jda.upsertCommand(
				Commands.slash("d", "Get your Daily Dose of Violins!")
		).queue();
		jda.upsertCommand(
				Commands.slash("rob", "Rob your friends!")
						.addOption(OptionType.STRING, "target", "Put the ID of your unfortunate target.")
		).queue();

		// help page 5
		jda.upsertCommand(
				Commands.slash("give", "Give a user some items.")
						.addOption(OptionType.STRING, "user", "Person")
						.addOption(OptionType.STRING, "item", "The item")
						.addOption(OptionType.INTEGER, "amount", "The amount")
		).queue();
		jda.upsertCommand(
				Commands.slash("warn", "Warns a user for misbehaviour.")
						.addOption(OptionType.STRING, "user", "The misbehaver")
						.addOption(OptionType.STRING, "reason", "Why are you warning them?????")
		).queue();
		jda.upsertCommand(
				Commands.slash("resetsave", "Resets the save of a user.")
						.addOption(OptionType.STRING, "user", "The misbehaver")
						.addOption(OptionType.STRING, "reason", "Why are you resetting their save?????")
		).queue();
		data = new OptionData(OptionType.STRING, "actiontype", "Setup or Edit")
				.addChoice("Setup", "setup")
				.addChoice("Edit", "edit");
		data2 = new OptionData(OptionType.STRING, "editoption", "Which Edit option")
				.addChoice("Channel", "channel")
				.addChoice("Multiplier", "multiplier")
				.addChoice("Word", "word");
		jda.upsertCommand(
				Commands.slash("luthier", "Changes the Luthier configuration for a server.")
						.addOptions(data, data2)
						.addOption(OptionType.STRING, "newvalue", "See Name")
		).queue();
		jda.upsertCommand(
				Commands.slash("resetincomes", "Adjusts all users' incomes to correct values.")
		).queue();
		jda.upsertCommand(
				Commands.slash("ban", "BANs a user.")
						.addOption(OptionType.STRING, "user", "The misbehaver")
						.addOption(OptionType.STRING, "reason", "Why are you banning them?????")
		).queue();
		jda.upsertCommand(
				Commands.slash("unban", "unbans a user.")
						.addOption(OptionType.STRING, "user", "The pardoned")
						.addOption(OptionType.BOOLEAN, "resetsave", "Do you want to reset their save?")
						.addOption(OptionType.STRING, "reason", "Why are you unbanning them?????")
		).queue();
		jda.upsertCommand(
				Commands.slash("updateluthierchance", "Forcibly updates all luthier spawn rates.")
		).queue();

		//help page 6
		jda.upsertCommand(
				Commands.slash("updateusers", "Updates all saves.")
						.addOption(OptionType.STRING, "newkey", "See name")
						.addOption(OptionType.STRING, "datatype", "See name")
						.addOption(OptionType.STRING, "setvalue", "Value of the new key")
		).queue();
		jda.upsertCommand(
				Commands.slash("updateroles", "Forcibly updates all roles for users.")
		).queue();
		data = new OptionData(OptionType.INTEGER, "rank", "The new permission level")
				.addChoice("Normie", 0)
				.addChoice("Moderator", 1)
				.addChoice("Admin", 2)
				.addChoice("Developer Perms - DANGEROUS", 3);
		jda.upsertCommand(
				Commands.slash("setpermlevel", "Sets the permission level for a user.")
						.addOption(OptionType.STRING, "user", "The user to change the Permissions for.")
						.addOptions(data)
		).queue();
		jda.upsertCommand(
				Commands.slash("globalstats", "View the combined stats of EVERYONE.")
		).queue();
		jda.upsertCommand(
				Commands.slash("forcestop", "Forcibly stops the bot, and prevents its restart.")
						.addOption(OptionType.STRING, "password", "To prevent accidental usage.")
		).queue();
		jda.upsertCommand(
				Commands.slash("resetdaily", "Gives everyone more time to run /d")
		).queue();
		jda.upsertCommand(
				Commands.slash("custom", "Custom Command - Usually Disabled")
		).queue();

		// LUTHIER
		jda.upsertCommand(
				Commands.slash("answer", "Answers a Luthier unscramble.")
						.addOption(OptionType.STRING, "guess", "See name")
		).queue();
		System.out.println("All commands upserted!"); */
	}
}