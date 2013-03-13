package REALDrummer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class myScribe extends JavaPlugin implements Listener {
	public static Server server;
	public static ConsoleCommandSender console;
	private String[] parameters;
	private static final String[] enable_messages = {
			"The pen is mightier than the pixelated diamond sword.",
			"I shall demonstrate unto all of you the proper way to use this thrillingly versatile language that we refer to as 'English.'",
			"I advise against the use of my AutoCorrection features if many persons that choose to enter your server do not speak English.",
			"William Shakespeare? I once knew him as \"Bill.\"",
			"I am rapping. ...rapping at your chamber door." },
			disable_messages = {
					"Though I am now disabled, I shall continue to spread proper literacy across the globe in the hope that some day soon, we will see people speaking proper English once again.",
					"If you believe plugins can't dream,...\n...you're wrong.",
					"Farewell, good literate sir.",
					"Good evening, Sir Operator.",
					"I shall return with the gifts of proper language upon the arrival of the upcoming morn." },
			color_color_code_chars = { "0", "1", "2", "3", "4", "5", "6", "7",
					"8", "9", "a", "b", "c", "d", "e", "f" },
			formatting_color_code_chars = { "k", "l", "m", "n", "o", "r" },
			profanities = { "fuck", "fck", "fuk", "Goddamn", "Goddam", "damn",
					"shit", "dammit", "bastard", "bitch", "btch", "damnit",
					"cunt", "asshole", "bigass", "dumbass", "badass", "dick" },
			borders = { "[]", "\\/", "\"*", "_^", "-=", ":;", "&%", "#@", ",.",
					"<>", "~$", ")(" },
			yeses = { "yes", "yeah", "yep", "ja", "sure", "why not", "okay",
					"do it", "fine", "whatever", "very well", "accept", "tpa",
					"cool", "hell yeah", "hells yeah", "hells yes", "come" },
			nos = { "no", "nah", "nope", "no thanks", "no don't", "shut up",
					"ignore", "it's not", "its not", "creeper", "unsafe",
					"wait", "one ", "1 " },
			recipes = {
					null,
					null,
					null,
					null,
					"Just craft any kind of wood (logs).",
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					"Melt sand in a furnace.",
					null,
					"&1lapis lazuli\nLLL\nLLL\nLLL",
					"&8cobblestone&f, bow, &4redstone dust\n&8CCC\nC&fB&8C\nC&4R&8C",
					"&esand\n&0---\n-&eSS\n&0-&eSS",
					"&4redstone dust\n&e&m&nWWW\nW&4R&e&m&nW\nWWW",
					"wool, &e&m&nwooden planks\n&0---\n&fWWW\n&e&m&nWWW",
					"&6gold&f, &e&msticks&f, &4redstone dust\n&6G&0-&6G\nG&f&e&mS&6G\nG&4R&6G",
					"&7iron ingots&f, &8&nstone pressure plate&f, &4redstone dust\n&7I&0-&7I\nI&8&nS&7I\nI&4R&7I",
					"&aslimeballs&f, &e&npistons\n&0---\n-&aS&0-\n-&e&nP&0-",
					null,
					null,
					null,
					"&e&m&nwooden planks&f, &8cobblestone&f, &7iron ingots&f, &4redstone dust\n&e&m&nWWW\n&8C&7I&8C\nC&4R&8C",
					null,
					"string\n&0---\n-&fSS\n&0-&fSS",
					null,
					null,
					null,
					null,
					null,
					"&6gold ingots\nGGG\nGGG\nGGG",
					"&7iron ingots\nIII\nIII\nIII",
					null,
					"&5&kM &fcan be cobblestone, stone, stone bricks, wooden planks, bricks, or sandstone\n&0---\n---&bx6\n&5&kMMM",
					"&cBricks &f(which can be made by cooking clay)\n&0---\n-&cBB\n&0-&cBB",
					"&esand&f, &8gunpowder\n&8G&eS&8G\n&eS&8G&eS\n&8G&eS&8G",
					"&e&m&nwooden planks&f, &cbooks\n&e&m&nWWW\n&cBBB\n&e&m&nWWW",
					null,
					null,
					"&fcoal or charcoal, &e&msticks\n&0---\n-&fC&0-\n-&e&mS&0-",
					null,
					null,
					"&5&kM &fcan be any wooden planks, cobblestone, bricks, any stone bricks, Nether brick, or sanstone\n&0--&5&kM\n&0-&5&kMM\nMMM" };
	private AutoCorrection[] default_AutoCorrections = {
			new AutoCorrection(" i ", " I ", null, true, null),
			new AutoCorrection(" ik ", " I know ", null, true, null),
			new AutoCorrection(" ib ", " I'm back ", null, true, null),
			new AutoCorrection(" ic ", " I see ", null, true, null),
			new AutoCorrection(" tp ", " teleport ", null, false, null),
			new AutoCorrection(" idk ", " I don't know ", null, true, null),
			new AutoCorrection(" idc ", " I don't care ", null, true, null),
			new AutoCorrection(" ikr ", " I know, right? ", "?", false, null),
			new AutoCorrection(" ikr ", " I know, right ", null, true, null),
			new AutoCorrection(" irl ", " in real life ", null, true, null),
			new AutoCorrection(" wtf ", " what the fuck? ", "?", false, null),
			new AutoCorrection(" wtf ", " what the fuck ", null, true, null),
			new AutoCorrection(" wth ", " what the hell? ", "?", false, null),
			new AutoCorrection(" wth ", " what the hell ", null, true, null),
			new AutoCorrection(" ftw ", " for the win ", null, true, null),
			new AutoCorrection(" y ", " why ", "=", false, null),
			new AutoCorrection(" u ", " you ", null, true, null),
			new AutoCorrection(" ur ", " your ", null, true, null),
			new AutoCorrection(" r ", " are ", null, true, null),
			new AutoCorrection(" o . o ", " \\o.\\o\\ ", null, true, null),
			new AutoCorrection(" o ", " oh ", null, true, null),
			new AutoCorrection(" c ", " see ", null, true, null),
			new AutoCorrection(" k ", " okay ", null, true, null),
			new AutoCorrection(" kk ", " okay ", null, true, null),
			new AutoCorrection(" ic ", " I see ", null, true, null),
			new AutoCorrection(" cya ", " see ya ", null, true, null),
			new AutoCorrection(" sum1", " someone ", null, true, null),
			new AutoCorrection(" some1", " someone ", null, true, null),
			new AutoCorrection("every1", "everyone", null, true, null),
			new AutoCorrection("any1", "anyone", null, true, null),
			new AutoCorrection(" ttyl ", " I'll talk to you later ", null,
					true, null),
			new AutoCorrection(" wb ", " welcome back ", null, true, null),
			new AutoCorrection(" ty ", " thank you ", null, true, null),
			new AutoCorrection(" yw ", " you're welcome ", null, true, null),
			new AutoCorrection(" gb ", " goodbye ", null, true, null),
			new AutoCorrection(" hb ", " happy birthday ", null, true, null),
			new AutoCorrection(" gl ", " good luck ", null, true, null),
			new AutoCorrection(" jk ", " just kidding ", null, true, null),
			new AutoCorrection(" jking ", " just kidding ", null, true, null),
			new AutoCorrection(" jkjk ", " just kidding ", null, true, null),
			new AutoCorrection(" np ", " no problem ", null, true, null),
			new AutoCorrection(" tmi ", " too much information ", null, true,
					null),
			new AutoCorrection(" afk ", " \\a.\\f.\\k. ", "/", true, null),
			new AutoCorrection(" \\a.\\f.\\k. .", " \\a.\\f.\\k.", null, true,
					null),
			new AutoCorrection(" omg ", " oh my God ", null, true, null),
			new AutoCorrection(" omfg ", " oh my fucking God ", null, true,
					null),
			new AutoCorrection(" stfu ", " shut the fuck up ", null, true, null),
			new AutoCorrection(" btw ", " by the way ", null, true, null),
			new AutoCorrection(" i gtg ", " I have to go ", null, true, null),
			new AutoCorrection(" i g2g ", " I have to go ", null, true, null),
			new AutoCorrection(" igtg ", " I have to go ", null, true, null),
			new AutoCorrection(" ig2g ", " I have to go ", null, true, null),
			new AutoCorrection(" gtg ", " I have to go ", null, true, null),
			new AutoCorrection(" g2g ", " I have to go ", null, true, null),
			new AutoCorrection(" 2nite ", " tonight ", null, true, null),
			new AutoCorrection(" l8", "late", null, true, null),
			new AutoCorrection(" w8", " wait ", null, true, null),
			new AutoCorrection(" m8", " mate", null, true, null),
			new AutoCorrection(" 4got ", " forgot ", null, true, null),
			new AutoCorrection(" 4get ", " forget ", null, true, null),
			new AutoCorrection(" i brb ", " I'll be right back ", null, true,
					null),
			new AutoCorrection(" ibrb ", " I'll be right back ", null, true,
					null),
			new AutoCorrection(" brb ", " I'll be right back ", null, true,
					null),
			new AutoCorrection(" nvm ", " never mind ", null, true, null),
			new AutoCorrection(" ppl ", " people ", null, true, null),
			new AutoCorrection(" nm ", " never mind ", null, true, null),
			new AutoCorrection(" tp ", " teleport ", null, true, null),
			new AutoCorrection(" tpa ", " teleport ", null, true, null),
			new AutoCorrection(" cuz ", " because ", null, true, null),
			new AutoCorrection(" plz ", " please ", null, true, null),
			new AutoCorrection(" ppl ", " people ", null, true, null),
			new AutoCorrection(" thx ", " thanks ", null, true, null),
			new AutoCorrection(" thnx ", " thanks ", null, true, null),
			new AutoCorrection(" xmas ", " Christmas ", null, true, null),
			new AutoCorrection(" becuz ", " because ", null, true, null),
			new AutoCorrection(" sry ", " sorry ", null, true, null),
			new AutoCorrection(" cm ", " Creative Mode ", null, true, null),
			new AutoCorrection(" cmp ", " Creative multiplayer ", null, true,
					null),
			new AutoCorrection(" sm ", " Survival Mode ", null, true, null),
			new AutoCorrection(" smp ", " Survival multiplayer ", null, true,
					null),
			new AutoCorrection(" im ", " I'm ", null, true, null),
			new AutoCorrection(" wont ", " won't ", null, true, null),
			new AutoCorrection(" didnt ", " didn't ", null, true, null),
			new AutoCorrection(" dont ", " don't ", null, true, null),
			new AutoCorrection(" cant ", " can't ", null, true, null),
			new AutoCorrection(" wouldnt ", " wouldn't ", null, true, null),
			new AutoCorrection(" shouldnt ", " shouldn't ", null, true, null),
			new AutoCorrection(" couldnt ", " couldn't ", null, true, null),
			new AutoCorrection(" isnt ", " isn't ", null, true, null),
			new AutoCorrection(" aint ", " ain't ", null, true, null),
			new AutoCorrection(" doesnt ", " doesn't ", null, true, null),
			new AutoCorrection(" youre ", " you're ", null, true, null),
			new AutoCorrection(" hes ", " he's ", null, true, null),
			new AutoCorrection(" shes ", " she's ", null, true, null),
			new AutoCorrection(" hed ", " he'd ", null, true, null),
			new AutoCorrection(" could of ", " could have ", null, true, null),
			new AutoCorrection(" should of ", "should have ", null, true, null),
			new AutoCorrection(" would of ", " would have ", null, true, null),
			new AutoCorrection(" itz ", " it's ", null, true, null),
			new AutoCorrection("wierd", "weird", null, true, null),
			new AutoCorrection("recieve", "receive", null, true, null),
			new AutoCorrection(" blowed up ", " blew up ", null, true, null),
			new AutoCorrection(" blowed it up ", " blew it up ", null, true,
					null),
			new AutoCorrection(" RAM ", " \\RAM ", null, true, null),
			new AutoCorrection(" NASA ", " \\NASA ", null, true, null),
			new AutoCorrection(" Xbox LIVE ", " Xbox \\LIVE ", null, true, null),
			new AutoCorrection(" AIDS ", " \\AIDS ", null, true, null),
			new AutoCorrection("!1", "!!", null, true, null),
			new AutoCorrection("\".", ".\"", "\"", false, null) };
	// [0] is for important announcements, [1] is for normal announcements, and
	// [2] is for unimportant announcements
	private long[] expiration_times_for_announcements = new long[3];
	// muted players = new HashMap<muted player's name, name of player who muted
	// them or "someone on the console">
	// mail = new HashMap<recipient, ArrayList<messages>>
	private ArrayList<Announcement> announcements = new ArrayList<Announcement>();
	private ArrayList<AutoCorrection> AutoCorrections = new ArrayList<AutoCorrection>();
	private HashMap<String, String> epithets_by_user = new HashMap<String, String>(),
			muted_players = new HashMap<String, String>();
	private HashMap<String, ArrayList<String>> death_messages_by_cause = new HashMap<String, ArrayList<String>>(),
			default_death_messages = new HashMap<String, ArrayList<String>>(),
			mail = new HashMap<String, ArrayList<String>>(),
			login_messages = new HashMap<String, ArrayList<String>>(),
			logout_messages = new HashMap<String, ArrayList<String>>();
	private HashMap<Player, String> message_beginnings = new HashMap<Player, String>(),
			command_beginnings = new HashMap<Player, String>();
	private static ArrayList<String> AFK_players = new ArrayList<String>(),
			players_who_have_accepted_the_rules = new ArrayList<String>(),
			players_who_have_read_the_rules = new ArrayList<String>();
	private static String rules = "", default_epithet = "",
			default_message_format = "", say_format = "";
	private static boolean players_must_accept_rules = true,
			AutoCorrect_on = true, capitalize_first_letter = true,
			end_with_period = true, change_all_caps_to_italics = true,
			cover_up_profanities = true, insert_command_usages = true,
			true_username_required = true, display_death_messages = true;
	private static Plugin Vault = null;
	private static Permission permissions = null;
	private static Economy economy = null;

	// TODO: make it so that once players accept the rules, myScribe shows them
	// the announcements
	// TODO: the AutoCorrections go to default no matter what. Fix it.
	// TODO: if there is an asterisk at the end, only cancel first letter
	// capitalizations and ending with periods
	// TODO: set up config questions for true_username_required and if the true
	// username IS required, should we just not allow them to make the epithet
	// or allow
	// them to make it, but put their true username at the end in parentheses?
	// TODO: fix abbreviation screwups
	// TODO: finish information parts: recipes, and potion recipes (/potion)
	// /potion with no parameters will tell you basics (splash potions need
	// gunpowder, redstone extends time, etc.)
	// TODO: make all-caps-to-italics changes capitalize the first letter if the
	// first letter is lowercase
	// TODO: make a HashMap for Bukkit commands and their usages for inserting
	// command usages

	// plugin enable/disable and the command operator
	public void onEnable() {
		server = getServer();
		console = server.getConsoleSender();
		server.getPluginManager().registerEvents(this, this);
		loadTheAnnouncements(console);
		loadTheAutoCorrections(console);
		loadTheDeathMessages(console);
		loadTheEpithets(console);
		loadTheLoginMessages(console);
		loadTheLogoutMessages(console);
		loadTheRules(console);
		loadTheTemporaryData();
		// done enabling
		String enable_message = enable_messages[(int) (Math.random() * enable_messages.length)];
		console.sendMessage(ChatColor.BLUE + enable_message);
		for (Player player : server.getOnlinePlayers())
			if (player.isOp())
				player.sendMessage(ChatColor.BLUE + enable_message);
	}

	public void onDisable() {
		saveTheAnnouncements(console, true);
		saveTheAutoCorrectSettings(console, true);
		saveTheDeathMessages(console, true);
		saveTheEpithets(console, true);
		saveTheLoginMessages(console, true);
		saveTheLogoutMessages(console, true);
		saveTheRules(console, true);
		saveTheTemporaryData();
		// done disabling
		String disable_message = disable_messages[(int) (Math.random() * disable_messages.length)];
		console.sendMessage(ChatColor.BLUE + disable_message);
		for (Player player : server.getOnlinePlayers())
			if (player.isOp())
				player.sendMessage(ChatColor.BLUE + disable_message);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String command,
			String[] my_parameters) {
		parameters = my_parameters;
		if ((command.equalsIgnoreCase("myScribe") || command
				.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("e") || (parameters.length > 2
						&& parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("e")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheEpithets(sender);
			else
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you're not allowed to use "
						+ ChatColor.BLUE + "/myScribe load" + ChatColor.RED
						+ ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command
				.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("a") || (parameters.length > 2
						&& parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("a")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheAutoCorrections(sender);
			else
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you're not allowed to use "
						+ ChatColor.BLUE + "/myScribe load" + ChatColor.RED
						+ ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command
				.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("d") || (parameters.length > 2
						&& parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("d")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheDeathMessages(sender);
			else
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you're not allowed to use "
						+ ChatColor.BLUE + "/myScribe load" + ChatColor.RED
						+ ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command
				.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("login")
						|| (parameters.length > 2
								&& parameters[1].equalsIgnoreCase("the") && parameters[2]
								.toLowerCase().startsWith("login"))
						|| (parameters.length > 2
								&& parameters[1].equalsIgnoreCase("log") && parameters[2]
									.equalsIgnoreCase("in")) || (parameters.length > 3
						&& parameters[1].equalsIgnoreCase("the")
						&& parameters[2].equalsIgnoreCase("log") && parameters[3]
							.equalsIgnoreCase("in")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheLoginMessages(sender);
			else
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you're not allowed to use "
						+ ChatColor.BLUE + "/myScribe load" + ChatColor.RED
						+ ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command
				.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("logout")
						|| (parameters.length > 2
								&& parameters[1].equalsIgnoreCase("the") && parameters[2]
								.toLowerCase().startsWith("logout"))
						|| (parameters.length > 2
								&& parameters[1].equalsIgnoreCase("log") && parameters[2]
									.equalsIgnoreCase("out")) || (parameters.length > 3
						&& parameters[1].equalsIgnoreCase("the")
						&& parameters[2].equalsIgnoreCase("log") && parameters[3]
							.equalsIgnoreCase("out")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheLogoutMessages(sender);
			else
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you're not allowed to use "
						+ ChatColor.BLUE + "/myScribe load" + ChatColor.RED
						+ ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command
				.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("r") || (parameters.length > 2
						&& parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("r")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheRules(sender);
			else
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you're not allowed to use "
						+ ChatColor.BLUE + "/myScribe load" + ChatColor.RED
						+ ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command
				.equalsIgnoreCase("mS"))
				&& parameters.length > 0
				&& parameters[0].equalsIgnoreCase("load")) {
			if (!(sender instanceof Player) || sender.isOp()) {
				loadTheEpithets(sender);
				loadTheAutoCorrections(sender);
				loadTheDeathMessages(sender);
				loadTheLoginMessages(sender);
				loadTheLogoutMessages(sender);
				loadTheRules(sender);
			} else
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you're not allowed to use "
						+ ChatColor.BLUE + "/myScribe load" + ChatColor.RED
						+ ".");
			return true;
		}
		// TODO add partial saves
		else if ((command.equalsIgnoreCase("myScribe") || command
				.equalsIgnoreCase("mS"))
				&& parameters.length > 0
				&& parameters[0].equalsIgnoreCase("save")) {
			if (!(sender instanceof Player)
					|| sender.hasPermission("myscribe.admin")) {
				saveTheEpithets(sender, true);
				saveTheAutoCorrectSettings(sender, true);
				saveTheDeathMessages(sender, true);
				saveTheLoginMessages(sender, true);
				saveTheLogoutMessages(sender, true);
				saveTheRules(sender, true);
			} else
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you don't have permission to use "
						+ ChatColor.BLUE + "/myScribe save" + ChatColor.RED
						+ ".");
			return true;
		} else if (command.equalsIgnoreCase("epithet")
				|| command.equalsIgnoreCase("nick")) {
			if (parameters.length > 0
					&& (!(sender instanceof Player) || sender
							.hasPermission("myscribe.epithet")))
				changeEpithet(sender);
			else if (parameters.length > 0)
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you don't have permission to change your epithet.");
			else
				sender.sendMessage(ChatColor.RED
						+ "You forgot to tell me what I should change your epithet to!");
			return true;
		} else if (command.equalsIgnoreCase("correct")) {
			if (parameters.length >= 2
					&& (!(sender instanceof Player)
							|| sender.hasPermission("myscribe.correct") || sender
								.hasPermission("myscribe.admin")))
				addCorrection(sender);
			else if (sender instanceof Player
					&& !sender.hasPermission("myscribe.correct"))
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you don't have permission to create your own AutoCorrections.");
			else
				sender.sendMessage(ChatColor.RED
						+ "You forgot to tell me what correction you want to make!");
			return true;
		} else if (command.equalsIgnoreCase("afklist")
				|| (command.equalsIgnoreCase("afk") && parameters.length > 0 && parameters[0]
						.equalsIgnoreCase("list"))) {
			AFKList(sender, false);
			return true;
		} else if (command.equalsIgnoreCase("afk") && parameters.length > 0) {
			AFKCheck(sender);
			return true;
		} else if (command.equalsIgnoreCase("afk")) {
			if (sender instanceof Player)
				AFKToggle(sender);
			else
				sender.sendMessage(ChatColor.RED
						+ "You're a console! You can't be away from the keyboard! You're IN the computer!");
			return true;
		} else if (command.equalsIgnoreCase("rules")) {
			if (!rules.equals("")) {
				if (sender instanceof Player)
					players_who_have_read_the_rules.add(sender.getName());
				sender.sendMessage(colorCode(rules));
			} else
				sender.sendMessage(ChatColor.RED
						+ "As I said, the rules haven't been written down yet, so you can't read them right now. Sorry.");
			return true;
		} else if (command.equalsIgnoreCase("accept")
				|| command.equalsIgnoreCase("acceptrules")
				|| command.equalsIgnoreCase("accepttherules")) {
			if (!(sender instanceof Player) || sender.isOp())
				sender.sendMessage(ChatColor.RED
						+ "You don't need to accept the rules! "
						+ ChatColor.ITALIC + "You made the rules!");
			else if (players_who_have_read_the_rules.contains(sender.getName())
					&& !players_who_have_accepted_the_rules.contains(sender
							.getName())) {
				players_who_have_accepted_the_rules.add(sender.getName());
				sender.sendMessage(ChatColor.BLUE
						+ "Remember the rules and have fun! You can read the rules again any time with /rules.");
			} else if (players_who_have_accepted_the_rules.contains(sender
					.getName()))
				sender.sendMessage(ChatColor.BLUE
						+ "You've already accepted the rules. You're good to go. Have fun.");
			else if (!rules.equals(""))
				sender.sendMessage(colorCode("&cYou haven't even &oread%o the rules! I know you didn't! Read 'em!"));
			else
				sender.sendMessage(ChatColor.RED
						+ "I already told you: you don't have to accept the rules right now. They haven't been written down yet.");
			return true;
		} else if (command.toLowerCase().startsWith("color")
				|| command.equalsIgnoreCase("codes")) {
			sender.sendMessage(colorCode("&00 &11 &22 &33 &44 &55 &66 &77 &88 &99 &aa &bb &cc &dd &ee &ff &kk&f(k) &f&ll&f &mm&f &nn&f &oo"));
			return true;
		} else if (command.equalsIgnoreCase("say")) {
			if (!(sender instanceof Player) || sender.isOp()) {
				String message = "";
				for (String parameter : parameters)
					message = message + " " + parameter;
				server.broadcastMessage(colorCode(replace(say_format,
						"[message]", AutoCorrect(sender, message), null, true,
						false)));
			} else
				sender.sendMessage(ChatColor.RED
						+ "Only ops have permission to broadcast messages with "
						+ ChatColor.BLUE + "/say" + ChatColor.RED + ".");
			return true;
		} else if (command.equalsIgnoreCase("announce")
				|| command.equalsIgnoreCase("declare")
				|| command.equalsIgnoreCase("decree")) {
			if ((!(sender instanceof Player)
					|| sender.hasPermission("myscribe.announce.unimportant")
					|| sender.hasPermission("myscribe.announce") || sender
						.hasPermission("myscribe.announce.important"))
					&& parameters.length > 0)
				announce(sender);
			else if (parameters.length > 0)
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you don't have permission to use "
						+ ChatColor.BLUE + "/" + command.toLowerCase()
						+ ChatColor.RED + ".");
			else
				sender.sendMessage(ChatColor.RED
						+ "You forgot to tell me what you want to announce!");
		} else if (command.equalsIgnoreCase("recipe")
				|| command.equalsIgnoreCase("craft")) {
			if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED
						+ "You forgot to tell me what item you want the recipe for!");
			else
				getRecipe(sender);
			return true;
		} else if (command.equalsIgnoreCase("ids")
				|| command.equalsIgnoreCase("id")) {
			id(sender);
			return true;
		} else if (command.equals("trade") || command.equals("exchange")) {
			// TODO
		} else if (command.equalsIgnoreCase("login")
				|| command.equalsIgnoreCase("loginmessage")) {
			if (sender.hasPermission("myscribe.loginmessage")) {
				String message = "", owner;
				ArrayList<String> messages = login_messages.get(sender
						.getName());
				int extra_param = 0;
				if (parameters[0].toLowerCase().startsWith("for:")) {
					owner = parameters[0].substring(4);
					extra_param++;
				} else
					owner = "[server]";
				for (int i = extra_param; i < parameters.length; i++) {
					message += (parameters[i] + " ");
				}
				login_messages.put(owner, messages);
			}
			return true;
		}
		return false;
	}

	// intra-command methods
	private String AutoCorrect(CommandSender sender, String message) {
		console.sendMessage("\"" + message + "\"");
		if (AutoCorrect_on && !message.startsWith("http")
				&& !message.startsWith("www.") && !message.startsWith("*")
				&& !message.endsWith("*")) {
			while ((message.endsWith("/") && !message.endsWith(":/"))
					|| (message.endsWith("\\") && !message.endsWith(":\\"))
					|| message.endsWith(",") || message.endsWith(">"))
				message = message.substring(0, message.length() - 1);
			// use spaces to make punctuation separate words
			if (message.length() > 1)
				for (int i = 1; i < message.length(); i++)
					if ((!message.substring(i, i + 1).toLowerCase()
							.equals(message.substring(i, i + 1).toUpperCase())
							|| message.substring(i, i + 1).equals("0")
							|| message.substring(i, i + 1).equals("1")
							|| message.substring(i, i + 1).equals("2")
							|| message.substring(i, i + 1).equals("3")
							|| message.substring(i, i + 1).equals("4")
							|| message.substring(i, i + 1).equals("5")
							|| message.substring(i, i + 1).equals("6")
							|| message.substring(i, i + 1).equals("7")
							|| message.substring(i, i + 1).equals("8") || message
							.substring(i, i + 1).equals("9"))
							&& message
									.substring(i - 1, i)
									.toLowerCase()
									.equals(message.substring(i - 1, i)
											.toUpperCase())
							&& !message.substring(i - 1, i).equals(" ")
							&& !message.substring(i - 1, i).equals("'")
							&& !message.substring(i - 1, i).equals("\\")
							&& !message.substring(i - 1, i).equals("-")
							&& !message.substring(i - 1, i).equals("_")
							&& !((message.substring(i - 1, i).equals(":")
									|| message.substring(i - 1, i).equals(";") || message
									.substring(i - 1, i).equals("=")) && (i + 1 >= message
									.length() || message
									.substring(i + 1, i + 2).equals(" ")))
							&& !isColorCode(message.substring(i - 1, i + 1),
									null, null)
							&& !message.substring(i - 1, i).equals("0")
							&& !message.substring(i - 1, i).equals("1")
							&& !message.substring(i - 1, i).equals("2")
							&& !message.substring(i - 1, i).equals("3")
							&& !message.substring(i - 1, i).equals("4")
							&& !message.substring(i - 1, i).equals("5")
							&& !message.substring(i - 1, i).equals("6")
							&& !message.substring(i - 1, i).equals("7")
							&& !message.substring(i - 1, i).equals("8")
							&& !message.substring(i - 1, i).equals("9"))
						message = message.substring(0, i) + " "
								+ message.substring(i);
			message = " " + message + " ";
			// perform corrections
			for (AutoCorrection correction : AutoCorrections)
				if (!(sender instanceof Player)
						|| correction.target == null
						|| (correction.target.startsWith("\\[")
								&& correction.target.endsWith("\\]") && (permissions != null && correction.target
								.substring(1, correction.target.length() - 1)
								.equals(permissions
										.getPrimaryGroup((Player) sender))))
						|| ((!correction.target.startsWith("\\[") || !correction.target
								.endsWith("\\]")) && sender.getName().equals(
								correction.target)))
					message = replace(message, correction.to_correct,
							correction.to_correct_to, correction.unless,
							correction.before, true);
			String[] words = message.split(" ");
			// change all capital words to italics
			if (change_all_caps_to_italics) {
				for (int i = 0; i < words.length; i++) {
					// temporarily eliminate color codes
					String temp = words[i];
					if (temp.contains("&") || temp.contains("%"))
						for (int j = 0; j < temp.length() - 1; j++)
							if (isColorCode(temp.substring(j, j + 2), null,
									null)) {
								temp = temp.substring(0, j)
										+ temp.substring(j + 2);
								if (temp.contains("&") || temp.contains("%"))
									j--;
								else
									break;
							}
					// make the changes if, respectively, the word is longer
					// than one letter (or the word before or after it is
					// italicized); there are no
					// underscores (found in usernames or emoticons); it doesn't
					// have a length of two and start with or and with an X, a
					// colon, a semicolon, or
					// an equals sign (emoticons); the words is all caps (of
					// course); and the word is not in an abbreviation
					if ((temp.length() > 1
							|| (i > 0 && words[i - 1].toLowerCase().contains(
									"&o")) || (i < words.length - 1 && words[i + 1]
							.toLowerCase().contains("&o")))
							&& !temp.contains("_")
							&& !temp.contains("\\")
							&& temp.equals(temp.toUpperCase())
							&& !temp.toLowerCase().equals(temp)
							&& (temp.length() != 2 || !(temp.startsWith("X")
									|| temp.endsWith("X")
									|| temp.startsWith(":")
									|| temp.endsWith(":")
									|| temp.startsWith(";")
									|| temp.endsWith(";")
									|| temp.startsWith("=") || temp
										.endsWith("=")))
							&& (temp.length() != 3 || !(temp.startsWith(">X")
									|| temp.startsWith(">:")
									|| temp.startsWith(">;") || temp
										.startsWith(">=")))
							&& (words[i].length() > 1 || i == 0
									|| i == words.length || !(words[i - 1]
									.endsWith(".") && words[i + 1]
									.startsWith(".")))) {
						if (i == words.length - 1 && !temp.endsWith(".")
								&& !temp.endsWith("!") && !temp.endsWith("?")
								&& !temp.endsWith(".\"")
								&& !temp.endsWith("!\"")
								&& !temp.endsWith("?\""))
							if (words[i].endsWith("\""))
								words[i] = words[i].substring(0,
										words[i].length() - 1)
										+ "!\"";
							else
								words[i] = words[i] + "!";
						words[i] = "&o" + words[i].toLowerCase() + "%o";
						if (i > 0
								&& words[i - 1].length() == 1
								&& words[i - 1].toUpperCase().equals(
										words[i - 1])
								&& !words[i - 1].toLowerCase().equals(
										words[i - 1])) {
							words[i - 1] = "&o" + words[i - 1].toLowerCase()
									+ "%o";
							if (words[i - 1].equals("&oi%o"))
								words[i - 1] = "&oI%o";
						}
					}
				}
			}
			// reconstruct the message from the words
			message = "";
			for (String word : words)
				if (message.equals(""))
					message = word;
				else
					message = message + " " + word;
			if (cover_up_profanities) {
				message = replace(message, " ass ", " a&kss%k ", null, true,
						true);
				for (String profanity : profanities)
					message = replace(
							message,
							profanity,
							profanity.substring(0, 1) + "&k"
									+ profanity.substring(1) + "%k", null,
							true, true);
			}
			message = replace(message, "./ ", "./", null, true, true);
			if (insert_command_usages)
				for (int i = 0; i < message.length() - 1; i++) {
					if (!message.contains("./"))
						break;
					if (message.substring(i, i + 2).equals("./")) {
						int end_index = i + 2;
						while (end_index < message.length())
							if (!message
									.substring(end_index, end_index + 1)
									.toLowerCase()
									.equals(message.substring(end_index,
											end_index + 1).toUpperCase()))
								end_index++;
							else
								try {
									Integer.parseInt(message.substring(
											end_index, end_index + 1));
									end_index++;
								} catch (NumberFormatException exception) {
									break;
								}
						console.sendMessage("\""
								+ message.substring(i + 2, end_index) + "\"");
						PluginCommand command = server.getPluginCommand(message
								.substring(i + 2, end_index).toLowerCase());
						if (command != null) {
							String color_code = "f";
							if (command.getPlugin().getName()
									.equals("myScribe"))
								color_code = "9";
							else if (command.getPlugin().getName()
									.equals("myUltraWarps"))
								color_code = "a";
							else if (command.getPlugin().getName()
									.equals("myZeus"))
								color_code = "b";
							else if (command.getPlugin().getName()
									.equals("myGuardDog"))
								color_code = "e";
							else if (command.getPlugin().getName()
									.equals("myCarpet"))
								color_code = "6";
							else if (command.getPlugin().getName()
									.equals("myOpAids"))
								color_code = "7";
							else if (command.getPlugin().getName()
									.equals("myGroundsKeeper"))
								color_code = "2";
							String usage = "&"
									+ color_code
									+ replace(command.getUsage(), "<command>",
											command.getName(), null, true, true)
									+ "%" + color_code;
							if (command.getAliases().size() > 0) {
								usage = usage + " (";
								for (String alias : command.getAliases()) {
									if (!usage.endsWith("("))
										usage = usage + " ";
									usage = usage + "or &" + color_code + "/"
											+ alias + "%" + color_code;
								}
								usage = usage + ")";
							}
							console.sendMessage("message: \"" + message + "\"");
							message = replace(message,
									"./" + message.substring(i + 2, end_index),
									usage, null, true, true);
						}
					}
				}
			// eliminate extra spaces between letters and punctuation
			message = replace(
					replace(message, "... ", "...", null, true, true), "/ ",
					"/", null, true, true);
			int quote_counter = 0;
			for (int i = 1; i < message.length(); i++) {
				if (message.substring(i, i + 1).toLowerCase()
						.equals(message.substring(i, i + 1).toUpperCase())
						&& (i + 2 > message.length() || !isColorCode(
								message.substring(i, i + 2), null, null))) {
					try {
						Integer.parseInt(message.substring(i, i + 1));
					} catch (NumberFormatException exception) {
						if (message.substring(i, i + 1).equals("\""))
							quote_counter++;
						if (!message.substring(i, i + 1).equals("(")
								&& !message.substring(i, i + 1).equals("[")
								&& !message.substring(i, i + 1).equals("{")
								&& !message.substring(i, i + 1).equals("\\")
								&& !message.substring(i, i + 1).equals("+")
								&& !message.substring(i, i + 1).equals("/")
								&& !(message.substring(i, i + 1).equals("\"") && quote_counter % 2 == 1))
							while (i > 0
									&& message.substring(i - 1, i).equals(" "))
								message = message.substring(0, i - 1)
										+ message.substring(i);
						else if (message.substring(i, i + 1).equals("/")) {
							int end_index = i + 1;
							while (end_index < message.length()
									&& (!message
											.substring(end_index, end_index + 1)
											.toUpperCase()
											.equals(message.substring(
													end_index, end_index + 1)
													.toLowerCase())
											|| message.substring(end_index,
													end_index + 1).equals("0")
											|| message.substring(end_index,
													end_index + 1).equals("1")
											|| message.substring(end_index,
													end_index + 1).equals("2")
											|| message.substring(end_index,
													end_index + 1).equals("3")
											|| message.substring(end_index,
													end_index + 1).equals("4")
											|| message.substring(end_index,
													end_index + 1).equals("5")
											|| message.substring(end_index,
													end_index + 1).equals("6")
											|| message.substring(end_index,
													end_index + 1).equals("7")
											|| message.substring(end_index,
													end_index + 1).equals("8") || message
											.substring(end_index, end_index + 1)
											.equals("9")))
								end_index++;
							// TODO: change the list of commands here to
							// Bukkit_command_usages.keySet().contains(message.substring(i+1,
							// end_index)
							if (server.getPluginCommand(message.substring(
									i + 1, end_index)) == null
									&& !(message.substring(i + 1, end_index)
											.equalsIgnoreCase("version")
											|| message
													.substring(i + 1, end_index)
													.equalsIgnoreCase("plugins")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("reload")
											|| message
													.substring(i + 1, end_index)
													.equalsIgnoreCase("timings")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("tell")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("kill")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("me")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("help")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("?")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("kick")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("ban")
											|| message
													.substring(i + 1, end_index)
													.equalsIgnoreCase("banlist")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("pardon")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("ban-ip")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase(
															"pardon-ip")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("op")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("deop")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("tp")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("give")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("stop")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase(
															"save-all")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase(
															"save-off")
											|| message
													.substring(i + 1, end_index)
													.equalsIgnoreCase("save-on")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("list")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("say")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase(
															"whitelist")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("time")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase(
															"gamemode")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase("xp")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase(
															"toggledownfall")
											|| message.substring(i + 1,
													end_index)
													.equalsIgnoreCase(
															"defaultgamemode") || message
											.substring(i + 1, end_index)
											.equalsIgnoreCase("seed")))
								while (i > 0
										&& message.substring(i - 1, i).equals(
												" "))
									message = message.substring(0, i - 1)
											+ message.substring(i);
						} else if (message.substring(i, i + 1).equals("\"")
								|| message.substring(i, i + 1).equals("(")
								|| message.substring(i, i + 1).equals("[")
								|| message.substring(i, i + 1).equals("{"))
							while (i < message.length() - 1
									&& message.substring(i + 1, i + 2).equals(
											" "))
								if (i == message.length() - 2)
									message = message.substring(0, i + 1);
								else
									message = message.substring(0, i + 1)
											+ message.substring(i + 2);
					}
				}
			}
			message = replace(message, "  ", " ", null, true, true);
			message = replace(
					replace(replace(message, "%o.", ".%o", null, true, true),
							"%o!", "!%o", null, true, true), "%o?", "?%o",
					null, true, true);
			while (message.length() >= 2
					&& isColorCode(message.substring(message.length() - 2),
							null, null))
				message = message.substring(0, message.length() - 2);
			// capitalize the first letter of every sentence if it is not a
			// correction
			if (capitalize_first_letter && !message.startsWith("*")
					&& !message.endsWith("*")) {
				message = "." + message;
				for (int i = 0; i < message.length(); i++) {
					String check_message = message.substring(i);
					while (check_message.startsWith(" "))
						check_message = check_message.substring(1);
					// locate terminal punctuation and make sure that the thing
					// after them isn't an emoticon
					if ((message.substring(i, i + 1).equals(".")
							|| message.substring(i, i + 1).equals("!") || message
							.substring(i, i + 1).equals("?"))
							&& !check_message.startsWith(":")
							&& !check_message.startsWith(";")
							&& !check_message.startsWith("=")
							&& !(check_message.length() >= 3
									&& check_message.substring(0, 1)
											.equalsIgnoreCase(
													check_message.substring(2,
															3)) && check_message
									.substring(0, 1).equalsIgnoreCase("o"))) {
						while (i < message.length() - 1)
							// if it's not a letter or number, skip it
							if (message
									.substring(i, i + 1)
									.toUpperCase()
									.equals(message.substring(i, i + 1)
											.toLowerCase())
									&& !message.substring(i, i + 1).equals("0")
									&& !message.substring(i, i + 1).equals("1")
									&& !message.substring(i, i + 1).equals("2")
									&& !message.substring(i, i + 1).equals("3")
									&& !message.substring(i, i + 1).equals("4")
									&& !message.substring(i, i + 1).equals("5")
									&& !message.substring(i, i + 1).equals("6")
									&& !message.substring(i, i + 1).equals("7")
									&& !message.substring(i, i + 1).equals("8")
									&& !message.substring(i, i + 1).equals("9")) {
								// skip two characters if it's a color code
								if (i < message.length() - 2
										&& isColorCode(
												message.substring(i, i + 2),
												null, null))
									i++;
								i++;
							}
							// both of these stop the loop, but if
							// i=message.length+1, it means it found a "\"
							// before the letter, so it should cancel
							// capitalization
							else if (message.substring(i, i + 1).equals("\\"))
								i = message.length() + 1;
							else
								break;
						// don't capitalize after an ellipsis or a
						// "\" (with the presence of a "\" indicated with
						// i==message.length()+1
						if (i < message.length() + 1
								&& (i < 3 || !message.substring(i - 3, i)
										.equals("...")))
							if (i + 1 == message.length())
								message = message.substring(0, i)
										+ message.substring(i, i + 1)
												.toUpperCase();
							else
								message = message.substring(0, i)
										+ message.substring(i, i + 1)
												.toUpperCase()
										+ message.substring(i + 1);
					}
				}
				message = message.substring(1);
			}
			// end lines with a period if no terminal punctuation exists and the
			// message doesn't start with or end with a * (correction) and the
			// message's last or second to last characters are not colons
			// (emoticons)
			if (end_with_period
					&& !message.endsWith(".")
					&& !message.endsWith("!")
					&& !message.endsWith("?")
					&& !message.endsWith(".\"")
					&& !message.endsWith("!\"")
					&& !message.endsWith("?\"")
					&& message.length() > 0
					&& !(message.endsWith(":")
							|| message.endsWith("=")
							|| message.endsWith(";")
							|| (message.length() >= 2 && (message.substring(
									message.length() - 2, message.length() - 1)
									.equals(":")
									|| message.substring(message.length() - 2,
											message.length() - 1).equals("=") || message
									.substring(message.length() - 2,
											message.length() - 1).equals(";")))
							|| message.toUpperCase().endsWith("XD")
							|| message.endsWith("<3") || (message.length() >= 3
							&& message.substring(message.length() - 1)
									.equalsIgnoreCase(
											message.substring(
													message.length() - 3,
													message.length() - 2)) && (message
							.toLowerCase().endsWith("o")
							|| message.toLowerCase().endsWith("t") || message
								.endsWith("-")))) && !message.endsWith("\\"))
				if (!message.endsWith("\""))
					message = message + "%k.";
				else
					message = message.substring(0, message.length() - 1)
							+ "%k.\"";
		}
		// get rid of all color codes immediately followed by an anti color code
		// of the same kind
		for (String color_code_char : color_color_code_chars) {
			message = replace(message, "%" + color_code_char + "&"
					+ color_code_char, "", null, true, true);
			message = replace(message, "&" + color_code_char + "%"
					+ color_code_char, "", null, true, true);
			message = replace(message, "%" + color_code_char + " &"
					+ color_code_char, " ", null, true, true);
			message = replace(message, "&" + color_code_char + " %"
					+ color_code_char, " ", null, true, true);
		}
		for (String color_code_char : formatting_color_code_chars) {
			message = replace(message, "%" + color_code_char + "&"
					+ color_code_char, "", null, true, true);
			message = replace(message, "&" + color_code_char + "%"
					+ color_code_char, "", null, true, true);
			message = replace(message, "%" + color_code_char + " &"
					+ color_code_char, " ", null, true, true);
			message = replace(message, "&" + color_code_char + " %"
					+ color_code_char, " ", null, true, true);
		}
		return replace(message, "\\", "", "\\", false, true);
	}

	private static String colorCode(String text) {
		text = "&f" + text;
		// put color codes in the right order if they're next to each other
		for (int i = 0; i < text.length() - 3; i++)
			if (isColorCode(text.substring(i, i + 2), false, true)
					&& isColorCode(text.substring(i + 2, i + 4), true, true))
				text = text.substring(0, i) + text.substring(i + 2, i + 4)
						+ text.substring(i, i + 2) + text.substring(i + 4);
		// replace all anti color codes with non antis
		String current_color_code = "";
		for (int i = 0; i < text.length() - 1; i++) {
			if (isColorCode(text.substring(i, i + 2), null, true))
				current_color_code = current_color_code
						+ text.substring(i, i + 2);
			else if (isColorCode(text.substring(i, i + 2), null, false)) {
				while (text.length() > i + 2
						&& isColorCode(text.substring(i, i + 2), null, false)) {
					current_color_code = replace(current_color_code,
							"&" + text.substring(i + 1, i + 2), "", null, true,
							true);
					if (current_color_code.equals(""))
						current_color_code = "&f";
					text = text.substring(0, i) + text.substring(i + 2);
				}
				text = text.substring(0, i) + current_color_code
						+ text.substring(i);
			}
		}
		String colored_text = ChatColor.translateAlternateColorCodes('&', text);
		return colored_text;
	}

	private static Boolean isColorCode(String text,
			Boolean true_non_formatting_null_either,
			Boolean true_non_anti_null_either) {
		if (!text.startsWith("&") && !text.startsWith("%"))
			return false;
		if (true_non_anti_null_either != null)
			if (true_non_anti_null_either && text.startsWith("%"))
				return false;
			else if (!true_non_anti_null_either && text.startsWith("&"))
				return false;
		if (true_non_formatting_null_either == null
				|| true_non_formatting_null_either)
			for (String color_color_code_char : color_color_code_chars)
				if (text.substring(1, 2)
						.equalsIgnoreCase(color_color_code_char))
					return true;
		if (true_non_formatting_null_either == null
				|| !true_non_formatting_null_either)
			for (String formatting_color_code_char : formatting_color_code_chars)
				if (text.substring(1, 2).equalsIgnoreCase(
						formatting_color_code_char))
					return true;
		return false;
	}

	private boolean isBorder(String test) {
		if (test.length() == 40) {
			for (String border : borders)
				if (test.contains(border)) {
					test = replace(test, border, "", null, true, true);
					break;
				}
			if (test.equals(""))
				return true;
		}
		return false;
	}

	private static String replace(String to_return, String to_change,
			String to_change_to, String unless, boolean true_means_before,
			boolean replace_all) {
		if (!to_return.toLowerCase().contains(to_change.toLowerCase()))
			return to_return;
		for (int i = 0; to_return.length() >= i + to_change.length(); i++) {
			if (to_return.substring(i, i + to_change.length())
					.equalsIgnoreCase(to_change)
					&& (unless == null
							|| (true_means_before && (i < unless.length() || !to_return
									.substring(i - unless.length(), i).equals(
											unless))) || (!true_means_before && (to_return
							.length() < i + to_change.length()
							+ unless.length() || !to_return.substring(
							i + to_change.length(),
							i + to_change.length() + unless.length())
							.equalsIgnoreCase(unless))))) {
				to_return = to_return.substring(0, i) + to_change_to
						+ to_return.substring(i + to_change.length());
				if (!replace_all)
					break;
				else
					i = i + to_change_to.length() - 1;
			}
			if (!to_return.toLowerCase().contains(to_change.toLowerCase()))
				break;
		}
		return to_return;
	}

	private static Boolean getResponse(CommandSender sender,
			String unformatted_response, String current_status_line,
			String current_status_is_true_message) {
		boolean said_yes = false, said_no = false;
		String formatted_response = unformatted_response;
		// elimiate unnecessary spaces and punctuation
		while (formatted_response.startsWith(" "))
			formatted_response = formatted_response.substring(1);
		while (formatted_response.endsWith(" "))
			formatted_response = formatted_response.substring(0,
					formatted_response.length() - 1);
		formatted_response = formatted_response.toLowerCase();
		// check their response
		for (String yes : yeses)
			if (formatted_response.startsWith(yes))
				said_yes = true;
		if (said_yes)
			return true;
		else {
			for (String no : nos)
				if (formatted_response.startsWith(no))
					said_no = true;
			if (said_no)
				return false;
			else if (current_status_line != null) {
				if (!formatted_response.equals("")) {
					if (unformatted_response.substring(0, 1).equals(" "))
						unformatted_response = unformatted_response
								.substring(1);
					sender.sendMessage(ChatColor.RED + "I don't know what \""
							+ unformatted_response + "\" means.");
				}
				while (current_status_line.startsWith(" "))
					current_status_line = current_status_line.substring(1);
				if (current_status_line
						.startsWith(current_status_is_true_message))
					return true;
				else
					return false;
			} else
				return null;
		}
	}

	public static int translateStringtoTimeInms(String written) {
		int time = 0;
		String[] temp = written.split(" ");
		ArrayList<String> words = new ArrayList<String>();
		for (String word : temp)
			if (!word.equalsIgnoreCase("and") && !word.equalsIgnoreCase("&"))
				words.add(replace(word.toLowerCase(), ",", "", null, true, true));
		while (words.size() > 0) {
			// for formats like "2 days 3 minutes 5.57 seconds" or
			// "3 d 5 m 12 s"
			try {
				double amount = Double.parseDouble(words.get(0));
				if (words.get(0).contains("d") || words.get(0).contains("h")
						|| words.get(0).contains("m")
						|| words.get(0).contains("s"))
					throw new NumberFormatException();
				int factor = 0;
				if (words.size() > 1) {
					if (words.get(1).startsWith("d"))
						factor = 86400000;
					else if (words.get(1).startsWith("h"))
						factor = 3600000;
					else if (words.get(1).startsWith("m"))
						factor = 60000;
					else if (words.get(1).startsWith("s"))
						factor = 1000;
					if (factor > 0)
						// since a double of, say, 1.0 is actually 0.99999...,
						// (int)ing it will reduce exact numbers by one, so I
						// added 0.1 to it to avoid that.
						time = time + (int) (amount * factor + 0.1);
					words.remove(0);
					words.remove(0);
				} else
					words.remove(0);
			} catch (NumberFormatException exception) {
				// if there's no space between the time and units, e.g.
				// "2h, 5m, 25s" or "4hours, 3min, 2.265secs"
				double amount = 0;
				int factor = 0;
				try {
					if (words.get(0).contains("d")
							&& (!words.get(0).contains("s") || words.get(0)
									.indexOf("s") > words.get(0).indexOf("d"))) {
						amount = Double.parseDouble(words.get(0).split("d")[0]);
						console.sendMessage("amount should="
								+ words.get(0).split("d")[0]);
						factor = 86400000;
					} else if (words.get(0).contains("h")) {
						amount = Double.parseDouble(words.get(0).split("h")[0]);
						factor = 3600000;
					} else if (words.get(0).contains("m")) {
						amount = Double.parseDouble(words.get(0).split("m")[0]);
						factor = 60000;
					} else if (words.get(0).contains("s")) {
						amount = Double.parseDouble(words.get(0).split("s")[0]);
						factor = 1000;
					}
					if (factor > 0)
						// since a double of, say, 1.0 is actually 0.99999...,
						// (int)ing it will reduce exact numbers by one, so I
						// added 0.1 to it to avoid that.
						time = time + (int) (amount * factor + 0.1);
				} catch (NumberFormatException exception2) {
				}
				words.remove(0);
			}
		}
		return time;
	}

	public static String translateTimeInmsToString(long time,
			boolean round_seconds) {
		// get the values (e.g. "2 days" or "55.7 seconds")
		ArrayList<String> values = new ArrayList<String>();
		if (time > 86400000) {
			values.add((int) (time / 86400000) + " days");
			time = time % 86400000;
		}
		if (time > 3600000) {
			values.add((int) (time / 3600000) + " hours");
			time = time % 3600000;
		}
		if (time > 60000) {
			values.add((int) (time / 60000) + " minutes");
			time = time % 60000;
		}
		// add a seconds value if there is still time remaining or if there are
		// no other values
		if (time > 0 || values.size() == 0)
			// if you have partial seconds and !round_seconds, it's written as a
			// double so it doesn't truncate the decimals
			if ((time / 1000.0) != (time / 1000) && !round_seconds)
				values.add((time / 1000.0) + " seconds");
			// if seconds are a whole number, just write it as a whole number
			// (integer)
			else
				values.add(Math.round(time / 1000) + " seconds");
		// if there are two or more values, add an "and"
		if (values.size() >= 2)
			values.add(values.size() - 1, "and");
		// assemble the final String
		String written = "";
		for (int i = 0; i < values.size(); i++) {
			// add spaces as needed
			if (i > 0)
				written = written + " ";
			written = written + values.get(i);
			// add commas as needed
			if (values.size() >= 4 && i < values.size() - 1
					&& !values.get(i).equals("and"))
				written = written + ",";
		}
		if (!written.equals(""))
			return written;
		else
			return null;
	}

	// listeners
	@EventHandler(priority = EventPriority.HIGHEST)
	public void cancelMutedPlayersChatOrFormatMessage(AsyncPlayerChatEvent event) {
		// cancel messages accidentally typed while walking
		if (event.getMessage().equals("w") || event.getMessage().equals("t")) {
			event.setCancelled(true);
			return;
		}
		// if the player hasn't accepted the rules, cancel the event
		if (!players_who_have_accepted_the_rules.contains(event.getPlayer()
				.getName()) && !event.getPlayer().isOp() && !rules.equals(""))
			if (event.getMessage().toLowerCase().contains("shut up")
					|| event.getMessage().toLowerCase().contains("stfu"))
				event.getPlayer().sendMessage(
						colorCode("&4&oNo, &lyou %lshut up, b&kitch%k!"));
			else
				event.getPlayer().sendMessage(
						ChatColor.RED + "Read and accept the rules first.");
		// if the player is muted, cancel the event
		else if (muted_players.containsKey(event.getPlayer().getName()))
			if (event.getMessage().toLowerCase().contains("shut up")
					|| event.getMessage().toLowerCase().contains("stfu"))
				event.getPlayer().sendMessage(
						colorCode("&4&oNo, &lyou %lshut up, b&kitch%k!"));
			else
				event.getPlayer().sendMessage(
						ChatColor.RED
								+ muted_players
										.get(event.getPlayer().getName())
								+ " muted you. You're not allowed to talk.");
		// if it's the continuation of a command...
		else if (command_beginnings.containsKey(event.getPlayer())) {
			String command_beginning = command_beginnings
					.get(event.getPlayer());
			if (command_beginnings.get(event.getPlayer()) != null) {
				command_beginning = command_beginnings.get(event.getPlayer());
				command_beginnings.remove(event.getPlayer());
			}
			if (event.getMessage().endsWith("[...]")) {
				command_beginnings.put(
						event.getPlayer(),
						command_beginning
								+ event.getMessage().substring(0,
										event.getMessage().length() - 5));
				event.getPlayer().sendMessage(
						ChatColor.BLUE + "You may continue typing.");
			} else
				event.getPlayer().performCommand(
						command_beginning + event.getMessage());
		}
		// if it is a chat
		else if (!event.getMessage().endsWith("[...]")) {
			String epithet = epithets_by_user.get(event.getPlayer().getName());
			// if the user has no epithet, use the default
			if (epithet == null)
				epithet = replace(default_epithet, "[player]", event
						.getPlayer().getName(), null, true, true);
			// fit the epithet and message into the message format
			String full_chat_message = event.getMessage();
			if (message_beginnings.get(event.getPlayer()) != null) {
				full_chat_message = message_beginnings.get(event.getPlayer())
						+ event.getMessage();
				message_beginnings.remove(event.getPlayer());
			}
			event.setMessage(colorCode(replace(
					replace(replace(default_message_format, "[player]", event
							.getPlayer().getName(), null, true, true),
							"[epithet]", epithet, null, true, false),
					"[message]",
					AutoCorrect(event.getPlayer(), full_chat_message), null,
					true, false)));
		} else {
			String message_beginning = message_beginnings
					.get(event.getPlayer());
			if (message_beginning == null)
				message_beginning = "";
			message_beginnings.put(
					event.getPlayer(),
					message_beginning
							+ event.getMessage().substring(0,
									event.getMessage().length() - 5));
			event.getPlayer().sendMessage(
					ChatColor.BLUE + "You may continue typing.");
		}
	}

	@EventHandler
	public void commandProcessing(PlayerCommandPreprocessEvent event) {
		if (!players_who_have_accepted_the_rules.contains(event.getPlayer()
				.getName())
				&& !event.getPlayer().isOp()
				&& !event.getMessage().toLowerCase().startsWith("/rules")
				&& !event.getMessage().toLowerCase().startsWith("/accept")
				&& !rules.equals("")) {
			event.getPlayer().sendMessage(
					ChatColor.RED
							+ "You have to read and accept the rules first.");
			event.setCancelled(true);
		} else if (event.getMessage().endsWith("[...]")) {
			String command_beginning = command_beginnings
					.get(event.getPlayer());
			if (command_beginning == null)
				command_beginning = "";
			command_beginnings.put(
					event.getPlayer(),
					command_beginning
							+ event.getMessage().substring(1,
									event.getMessage().length() - 5));
			event.getPlayer().sendMessage(
					ChatColor.BLUE + "You may continue typing.");
			event.setCancelled(true);
		} else {
			String command = event.getMessage().substring(1);
			if (command_beginnings.get(event.getPlayer()) != null) {
				command = command_beginnings.get(event.getPlayer()) + command;
				command_beginnings.remove(event.getPlayer());
			}
			event.setMessage("/" + command);
		}
		if (AFK_players.contains(event.getPlayer().getName())
				&& !replace(event.getMessage(), " ", "", null, true, true)
						.equalsIgnoreCase("afk")) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPlayedBefore()) {
			event.setJoinMessage(ChatColor.BLUE + "Please welcome "
					+ event.getPlayer().getName()
					+ " to the server. It is their first time playing.");
			event.getPlayer()
					.sendMessage(
							ChatColor.BLUE
									+ "Welcome to the server. We hope you have a great time. Don't forget to read the rules and accept them so you can place blocks and access commands!");
		} else {
			ArrayList<String> possible_messages = new ArrayList<String>();
			if (login_messages.get("[server]") != null) {
				possible_messages = login_messages.get("[server]");
			}
			if (server.getPluginManager().getPlugin("Vault") != null
					&& server.getPluginManager().getPlugin("Vault").isEnabled()
					&& permissions.getPrimaryGroup(event.getPlayer()) != null
					&& login_messages.get("["
							+ permissions.getPrimaryGroup(event.getPlayer())
							+ "]") != null) {
				possible_messages = login_messages.get("["
						+ permissions.getPrimaryGroup(event.getPlayer()) + "]");
			}
			if (login_messages.get(event.getPlayer().getName()) != null) {
				possible_messages = login_messages.get(event.getPlayer()
						.getName());
			}
			String epithet = epithets_by_user.get(event.getPlayer().getName());
			if (epithet == null) {
				epithet = default_epithet.replaceAll("\\[player\\]", event
						.getPlayer().getName());
			}
			event.setJoinMessage(possible_messages
					.get((int) (Math.random() * possible_messages.size()))
					.replaceAll("\\[player\\]", event.getPlayer().getName())
					.replaceAll("\\[epithet\\]", epithet));
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnMove(PlayerMoveEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnChat(AsyncPlayerChatEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnBedEnter(PlayerBedEnterEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnBedLeave(PlayerBedLeaveEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnBucketFill(PlayerBucketFillEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnBucketEmpty(PlayerBucketEmptyEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnDropItem(PlayerDropItemEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnEggThrow(PlayerEggThrowEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnFish(PlayerFishEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnInteract(PlayerInteractEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnRespawn(PlayerRespawnEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnShear(PlayerShearEntityEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnSneak(PlayerToggleSneakEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE
					+ event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		ArrayList<String> possible_messages = new ArrayList<String>();
		if (logout_messages.get("[server]") != null) {
			possible_messages = logout_messages.get("[server]");
		}
		if (server.getPluginManager().getPlugin("Vault") != null
				&& server.getPluginManager().getPlugin("Vault").isEnabled()
				&& permissions.getPrimaryGroup(event.getPlayer()) != null
				&& logout_messages.get("["
						+ permissions.getPrimaryGroup(event.getPlayer()) + "]") != null) {
			possible_messages = logout_messages.get("["
					+ permissions.getPrimaryGroup(event.getPlayer()) + "]");
		}
		if (login_messages.get(event.getPlayer().getName()) != null) {
			possible_messages = login_messages.get(event.getPlayer().getName());
		}
		String epithet = epithets_by_user.get(event.getPlayer().getName());
		if (epithet == null) {
			epithet = default_epithet.replaceAll("\\[player\\]", event
					.getPlayer().getName());
		}
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
		}
		if (message_beginnings.containsKey(event.getPlayer().getName())) {
			message_beginnings.remove(event.getPlayer().getName());
		}
		if (command_beginnings.containsKey(event.getPlayer().getName())) {
			command_beginnings.remove(event.getPlayer().getName());
		}
		event.setQuitMessage(possible_messages
				.get((int) (Math.random() * possible_messages.size()))
				.replaceAll("\\[player\\]", event.getPlayer().getName())
				.replaceAll("\\[epithet\\]", epithet));
	}

	// loading
	private void loadTheAnnouncements(CommandSender sender) {
		announcements = new ArrayList<Announcement>();
		// default expiration times: 4 weeks (2,419,200,000ms) for important
		// ones, 2 weeks (1,209,600,000ms) for normal ones, and 1 week
		// (604,800,000ms) for
		// unimportant ones
		expiration_times_for_announcements = new long[] { 2419200000L,
				1209600000L, 604800000L };
		// check the announcements file
		File corrections_file = new File(getDataFolder(), "announcements.txt");
		// read the announcements.txt file
		try {
			if (!corrections_file.exists()) {
				getDataFolder().mkdir();
				sender.sendMessage(ChatColor.YELLOW
						+ "I couldn't find an announcements.txt file. I'll make a new one.");
				corrections_file.createNewFile();
			}
			BufferedReader in = new BufferedReader(new FileReader(
					corrections_file));
			String save_line = in.readLine();
			while (save_line != null) {
				if (save_line
						.startsWith("expiration time for important announcements:"))
					expiration_times_for_announcements[0] = translateStringtoTimeInms(save_line
							.substring(45));
				else if (save_line
						.startsWith("expiration time for normal announcements:"))
					expiration_times_for_announcements[1] = translateStringtoTimeInms(save_line
							.substring(42));
				else if (save_line
						.startsWith("expiration time for unimportant announcements:"))
					expiration_times_for_announcements[2] = translateStringtoTimeInms(save_line
							.substring(47));
				else if (save_line.startsWith("At time = ")) {
					final Announcement read_announcement = new Announcement(
							save_line);
					// check to see if the announcement has expired already and
					// if not, and if it hasn't, add it and schedule an event to
					// cancel it later
					if (read_announcement.time_created
							+ expiration_times_for_announcements[read_announcement.is_important_index] > Calendar
							.getInstance().getTimeInMillis()) {
						announcements.add(read_announcement);
						server.getScheduler().scheduleSyncDelayedTask(
								this,
								new Runnable() {

									@Override
									public void run() {
										announcements.remove(read_announcement);
									}

								}, // divide by 50 because the scheduler
									// schedules 1 tick/20 seconds;
									// ms*1s/1000ms*20ticks/s = ms*20/1000 =
									// ms/50
								(read_announcement.time_created
										+ expiration_times_for_announcements[read_announcement.is_important_index] - Calendar
										.getInstance().getTimeInMillis()) / 50);
					}
				}
				save_line = in.readLine();
			}
			in.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your announcements.");
			exception.printStackTrace();
			return;
		}
		saveTheAnnouncements(sender, false);
		if (announcements.size() == 0)
			sender.sendMessage(ChatColor.BLUE
					+ "Your announcement settings have been loaded.");
		else if (announcements.size() == 1)
			sender.sendMessage(ChatColor.BLUE
					+ "Your announcement settings and your server's only current announcement have been loaded.");
		else
			sender.sendMessage(ChatColor.BLUE
					+ "Your announcement settings and your server's "
					+ announcements.size()
					+ " current announcements have been loaded.");
		if (sender instanceof Player)
			if (announcements.size() == 0)
				console.sendMessage(ChatColor.BLUE + sender.getName()
						+ " loaded your announcement settings from file.");
			else if (announcements.size() == 1)
				console.sendMessage(ChatColor.BLUE
						+ sender.getName()
						+ " loaded your announcement settings and your one current announcement from file.");
			else
				console.sendMessage(ChatColor.BLUE + sender.getName()
						+ " loaded your announcement settings and your "
						+ announcements.size()
						+ " current announcements from file.");
	}

	private void loadTheAutoCorrections(CommandSender sender) {
		AutoCorrections = new ArrayList<AutoCorrection>();
		// check the AutoCorrections file
		File corrections_file = new File(getDataFolder(), "AutoCorrections.txt");
		try {
			if (!corrections_file.exists()) {
				getDataFolder().mkdir();
				sender.sendMessage(ChatColor.YELLOW
						+ "I couldn't find an AutoCorrections.txt file. I'll make a new one.");
				corrections_file.createNewFile();
				for (int i = 0; i < default_AutoCorrections.length; i++)
					AutoCorrections.add(default_AutoCorrections[i]);
			} else {
				// read the AutoCorrections.txt file
				BufferedReader in = new BufferedReader(new FileReader(
						corrections_file));
				String save_line = in.readLine();
				while (save_line != null) {
					while (save_line.startsWith(" "))
						save_line = save_line.substring(1);
					if (save_line
							.startsWith("Would you like to use AutoCorrections in your server's chat?"))
						AutoCorrect_on = getResponse(sender,
								save_line.substring(60), in.readLine(),
								"Right now, AutoCorrections are enabled.");
					else if (save_line
							.startsWith("Would you like me to capitalize the first letter of every sentence?"))
						capitalize_first_letter = getResponse(sender,
								save_line.substring(67), in.readLine(),
								"Right now, first letter capitalization is enabled.");
					else if (save_line
							.equals("Would you like me to put periods at the end of any sentences that have no terminal punctuation?"))
						end_with_period = getResponse(sender,
								save_line.substring(95), in.readLine(),
								"Right now, period addition is enabled.");
					else if (save_line
							.equals("Would you like me to change any all-caps words or sentences to lowercase italics?"))
						change_all_caps_to_italics = getResponse(sender,
								save_line.substring(81), in.readLine(),
								"Right now, caps to italics conversion is enabled.");
					else if (save_line
							.equals("Would you like me to cover up profanities using magic (meaning the &k color code, not fairy dust)?"))
						cover_up_profanities = getResponse(sender,
								save_line.substring(98), in.readLine(),
								"Right now, profanity coverup is enabled.");
					else if (save_line
							.equals("Would you like me to replace all in-text commands with their usages when they start with a \"./\"?"))
						insert_command_usages = getResponse(sender,
								save_line.substring(96), in.readLine(),
								"Right now, command usage insertion is enabled.");
					else if (save_line.startsWith("Change \""))
						AutoCorrections.add(new AutoCorrection(save_line));
					save_line = in.readLine();
				}
				in.close();
			}
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your AutoCorrections.");
			exception.printStackTrace();
			return;
		}
		if (AutoCorrections.size() == 0)
			for (int i = 0; i < default_AutoCorrections.length; i++)
				AutoCorrections.add(default_AutoCorrections[i]);
		saveTheAutoCorrectSettings(sender, false);
		if (AutoCorrections.size() == 0)
			sender.sendMessage(ChatColor.BLUE
					+ "Your AutoCorrect settings have been loaded.");
		else if (AutoCorrections.size() == 1)
			sender.sendMessage(ChatColor.BLUE
					+ "Your AutoCorrect settings and your server's only AutoCorrection have been loaded.");
		else
			sender.sendMessage(ChatColor.BLUE
					+ "Your AutoCorrect settings and your server's "
					+ AutoCorrections.size()
					+ " AutoCorrections have been loaded.");
		if (sender instanceof Player)
			if (AutoCorrections.size() == 0)
				console.sendMessage(ChatColor.BLUE + sender.getName()
						+ " loaded your AutoCorrect settings from file.");
			else if (AutoCorrections.size() == 1)
				console.sendMessage(ChatColor.BLUE
						+ sender.getName()
						+ " loaded your AutoCorrect settings and your one AutoCorrection from file.");
			else
				console.sendMessage(ChatColor.BLUE + sender.getName()
						+ " loaded your AutoCorrect settings and your "
						+ AutoCorrections.size()
						+ " AutoCorrections from file.");
	}

	private void loadTheDeathMessages(CommandSender sender) {
		death_messages_by_cause = new HashMap<String, ArrayList<String>>();
		// check the death messages file
		File death_messages_file = new File(getDataFolder(),
				"death messages.txt");
		// read the death messages.txt file
		try {
			if (!death_messages_file.exists()) {
				getDataFolder().mkdir();
				sender.sendMessage(ChatColor.YELLOW
						+ "I couldn't find an death messages.txt file. I'll make a new one.");
				death_messages_file.createNewFile();
				death_messages_by_cause = default_death_messages;
			}
			BufferedReader in = new BufferedReader(new FileReader(
					death_messages_file));
			String save_line = in.readLine();
			while (save_line != null) {
				while (save_line.startsWith(" "))
					save_line = save_line.substring(1);
				if (save_line
						.startsWith("Do you want hilarious death messages to appear when people die?"))
					display_death_messages = getResponse(sender,
							save_line.substring(63), in.readLine(),
							"Right now, hilarious death messages will appear when someone dies.");
				save_line = in.readLine();
			}
			in.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your death messages.");
			exception.printStackTrace();
			return;
		}
		saveTheDeathMessages(sender, false);
		if (death_messages_by_cause.size() == 0)
			sender.sendMessage(ChatColor.BLUE
					+ "You don't have any death messages to load.");
		else if (death_messages_by_cause.size() == 1)
			sender.sendMessage(ChatColor.BLUE
					+ "Your only death message has been loaded.");
		else
			sender.sendMessage(ChatColor.BLUE + "Your "
					+ death_messages_by_cause.size()
					+ " death messages have been loaded.");
		if (sender instanceof Player)
			if (death_messages_by_cause.size() == 0)
				console.sendMessage(ChatColor.BLUE
						+ sender.getName()
						+ " tried to laod your death messages from file, but there were none.");
			else if (death_messages_by_cause.size() == 1)
				console.sendMessage(ChatColor.BLUE + sender.getName()
						+ " loaded your only death message from file.");
			else
				console.sendMessage(ChatColor.BLUE + sender.getName()
						+ " loaded your " + death_messages_by_cause.size()
						+ " death messages from file.");
	}

	private void loadTheEpithets(CommandSender sender) {
		epithets_by_user = new HashMap<String, String>();
		default_epithet = "<[player]>";
		default_message_format = "[epithet]&f: [message]";
		// check the epithets file
		File epithets_file = new File(getDataFolder(), "epithets.txt");
		// read the epithets.txt file
		try {
			if (!epithets_file.exists()) {
				getDataFolder().mkdir();
				sender.sendMessage(ChatColor.YELLOW
						+ "I couldn't find an epithets.txt file. I'll make a new one.");
				epithets_file.createNewFile();
			}
			BufferedReader in = new BufferedReader(
					new FileReader(epithets_file));
			String save_line = in.readLine();
			while (save_line != null) {
				// eliminate preceding spaces
				while (save_line.length() > 0
						&& save_line.substring(0, 1).equals(" "))
					save_line = save_line.substring(1);
				if (save_line.toLowerCase().startsWith("default epithet: "))
					default_epithet = save_line.substring(17);
				else if (save_line.toLowerCase().startsWith(
						"default message format: "))
					default_message_format = save_line.substring(24);
				else if (save_line.contains(": ") && !isBorder(save_line))
					epithets_by_user.put(save_line.split(": ")[0],
							save_line.split(": ")[1]);
				save_line = in.readLine();
			}
			in.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your epithets.");
			exception.printStackTrace();
			return;
		}
		saveTheEpithets(sender, false);
		if (epithets_by_user.size() == 0)
			sender.sendMessage(ChatColor.BLUE
					+ "Your epithet settings have been loaded.");
		else if (epithets_by_user.size() == 1)
			sender.sendMessage(ChatColor.BLUE
					+ "Your epithet settings and your server's only epithet have been loaded.");
		else
			sender.sendMessage(ChatColor.BLUE
					+ "Your epithet settings and your server's "
					+ epithets_by_user.size() + " epithets have been loaded.");
	}

	private void loadTheLoginMessages(CommandSender sender) {
		File login_messages = new File(getDataFolder(), "login messages.txt");
		try {
			if (!login_messages.exists()) {
				sender.sendMessage(ChatColor.YELLOW
						+ "I can see you don't have a login messages file yet. I'll go ahead and make one for you.");
				login_messages.createNewFile();
				// TODO default login messages
				return;
			}
			BufferedReader read = new BufferedReader(new FileReader(
					login_messages));
			String save_line = read.readLine();
			String person = null;
			ArrayList<String> curr_messages = new ArrayList<String>();
			while (save_line != null) {
				if (save_line.contains("====")) {
					this.login_messages.put(person, curr_messages);
					person = save_line.substring(5, save_line.length() - 5);
				} else {
					curr_messages.add(save_line);
				}
				save_line = read.readLine();
			}
			this.login_messages.put(person, curr_messages);
		} catch (IOException e) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "Sorry, but I got an IOException while reading the login messages.");
			e.printStackTrace();
			return;
		}
		sender.sendMessage(ChatColor.BLUE + "Your "
				+ this.login_messages.size()
				+ "login groups and users loaded successfully.");
		if (sender instanceof Player) {
			console.sendMessage(ChatColor.BLUE + "Your "
					+ this.login_messages.size()
					+ "login groups and users loaded successfully.");
		}
	}

	private void loadTheLogoutMessages(CommandSender sender) {
		File logout_messages = new File(getDataFolder(), "logout messages.txt");
		try {
			if (!logout_messages.exists()) {
				sender.sendMessage(ChatColor.YELLOW
						+ "I can see you don't have a logout messages file yet. I'll go ahead and make one for you.");
				logout_messages.createNewFile();
				// TODO default logout messages
				return;
			}
			BufferedReader read = new BufferedReader(new FileReader(
					logout_messages));
			String save_line = read.readLine();
			String person = null;
			ArrayList<String> curr_messages = new ArrayList<String>();
			while (save_line != null) {
				if (save_line.contains("====")) {
					this.logout_messages.put(person, curr_messages);
					person = save_line.substring(5, save_line.length() - 5);
				} else {
					curr_messages.add(save_line);
				}
				save_line = read.readLine();
			}
			this.logout_messages.put(person, curr_messages);
		} catch (IOException e) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "Sorry, but I got an IOException while reading the logout messages.");
			e.printStackTrace();
			return;
		}
		sender.sendMessage(ChatColor.BLUE + "Your "
				+ this.login_messages.size()
				+ "logout groups and users loaded successfully.");
		if (sender instanceof Player) {
			console.sendMessage(ChatColor.BLUE + "Your "
					+ this.login_messages.size()
					+ "logout groups and users loaded successfully.");
		}
	}

	private void loadTheRules(CommandSender sender) {
		rules = "";
		players_who_have_accepted_the_rules = new ArrayList<String>();
		// check the rules file
		File rules_file = new File(getDataFolder(), "rules.txt");
		// read the rules.txt file
		players_who_have_accepted_the_rules = new ArrayList<String>();
		try {
			if (!rules_file.exists()) {
				sender.sendMessage(ChatColor.YELLOW
						+ "I couldn't find an rules.txt file. I'll make a new one.");
				getDataFolder().mkdir();
				rules_file.createNewFile();
			}
			BufferedReader in = new BufferedReader(new FileReader(rules_file));
			String save_line = in.readLine();
			rules = "";
			while (save_line != null) {
				if (isBorder(save_line)) {
					save_line = in.readLine();
					if (save_line.contains(", ")) {
						String[] players_listed = save_line.split(", ");
						// elimiate the "and" and sentence ending around the
						// last username
						players_listed[players_listed.length - 1] = players_listed[players_listed.length - 1]
								.substring(
										4,
										players_listed[players_listed.length - 1]
												.length() - 29);
						// convert the array to an ArrayList
						for (String listed_player : players_listed)
							players_who_have_accepted_the_rules
									.add(listed_player);
					} else if (save_line.contains(" and ")) {
						String[] players_listed = save_line.split(" and ");
						players_listed[1] = players_listed[1].substring(0,
								players_listed[1].length() - 25);
						players_who_have_accepted_the_rules
								.add(players_listed[0]);
						players_who_have_accepted_the_rules
								.add(players_listed[1]);
					} else if (!save_line.startsWith("No one"))
						players_who_have_accepted_the_rules.add(save_line
								.split(" ")[0]);
				} else if (rules.equals("")
						&& !save_line
								.equals("Type the rules below this line and above the border line!"))
					rules = save_line;
				else if (!save_line
						.equals("Type the rules below this line and above the border line!"))
					rules = rules + "\n" + save_line;
				save_line = in.readLine();
			}
			in.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your rules.");
			exception.printStackTrace();
			return;
		}
		saveTheRules(sender, false);
		if (!rules.equals(""))
			sender.sendMessage(ChatColor.BLUE + "Your rules have been loaded.");
		else
			sender.sendMessage(ChatColor.RED
					+ "You haven't written down your rules! You need to write down your rules in the rules.txt right now!");
	}

	private void loadTheTemporaryData() {
		// check the temporary file
		File temp_file = new File(getDataFolder(), "temp.txt");
		if (temp_file.exists())
			// read the temp.txt file
			try {
				BufferedReader in = new BufferedReader(
						new FileReader(temp_file));
				String save_line = in.readLine(), data_type = "", commander = "";
				while (save_line != null) {
					if (save_line.startsWith("==== "))
						if (save_line.substring(5).startsWith("muted"))
							data_type = "muted players";
						else
							data_type = "mail";
					else if (save_line.startsWith("== "))
						commander = save_line.split(" ")[1];
					else if (data_type.equals("muted players"))
						muted_players.put(save_line.split(",")[0],
								save_line.split(",")[1]);
					else if (data_type.equals("mail")) {
						// in the case of mail, "commander" actually represents
						// the recipient of the mail
						ArrayList<String> mail_for_this_person = mail
								.get(commander);
						if (mail_for_this_person == null)
							mail_for_this_person = new ArrayList<String>();
						mail_for_this_person.add(save_line);
						mail.put(commander, mail_for_this_person);
					}
					save_line = in.readLine();
				}
				in.close();
			} catch (IOException exception) {
				console.sendMessage(ChatColor.DARK_RED
						+ "I got an IOException while trying to load the temporary data.");
				exception.printStackTrace();
				return;
			}
		temp_file.delete();
	}

	// saving
	private void saveTheAnnouncements(CommandSender sender,
			boolean display_message) {
		// check the announcements file
		File announcements_file = new File(getDataFolder(), "announcements.txt");
		// save the announcements
		try {
			if (!announcements_file.exists()) {
				getDataFolder().mkdir();
				announcements_file.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(
					announcements_file));
			out.write("expiration time for important announcements: "
					+ translateTimeInmsToString(
							expiration_times_for_announcements[0], false));
			out.newLine();
			out.write("expiration time for normal announcements: "
					+ translateTimeInmsToString(
							expiration_times_for_announcements[1], false));
			out.newLine();
			out.write("expiration time for unimportant announcements: "
					+ translateTimeInmsToString(
							expiration_times_for_announcements[2], false));
			out.newLine();
			String border_unit = borders[(int) (Math.random() * borders.length)], border = "";
			for (int i = 0; i < 20; i++)
				border = border + border_unit;
			out.write(border);
			out.newLine();
			for (Announcement announcement : announcements) {
				out.write(announcement.save_line);
				out.newLine();
			}
			out.close();
		} catch (IOException exception) {
			console.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your temporary data.");
			exception.printStackTrace();
			return;
		}
	}

	private void saveTheAutoCorrectSettings(CommandSender sender,
			boolean display_message) {
		// link up with Vault
		Vault = server.getPluginManager().getPlugin("Vault");
		if (Vault != null) {
			// locate the permissions and economy plugins
			try {
				permissions = server.getServicesManager()
						.getRegistration(Permission.class).getProvider();
			} catch (NullPointerException exception) {
				permissions = null;
			}
			try {
				economy = server.getServicesManager()
						.getRegistration(Economy.class).getProvider();
			} catch (NullPointerException exception) {
				economy = null;
			}
			// forcibly enable the permissions plugin
			if (permissions != null) {
				Plugin permissions_plugin = server.getPluginManager()
						.getPlugin(permissions.getName());
				if (permissions_plugin != null
						&& !permissions_plugin.isEnabled())
					server.getPluginManager().enablePlugin(permissions_plugin);
			}
			// send confirmation messages
			console.sendMessage(ChatColor.BLUE + "I see your Vault...");
			if (permissions == null && economy == null)
				console.sendMessage(ChatColor.RED
						+ "...but I can't find any economy or permissions plugins.");
			else if (permissions != null) {
				console.sendMessage(ChatColor.BLUE + "...and raise you a "
						+ permissions.getName() + "...");
				if (economy != null)
					console.sendMessage(ChatColor.BLUE + "...as well as a "
							+ economy.getName() + ".");
				else
					console.sendMessage(ChatColor.RED
							+ "...but I can't find your economy plugin.");
			} else if (permissions == null && economy != null) {
				console.sendMessage(ChatColor.BLUE + "...and raise you a "
						+ economy.getName() + "...");
				console.sendMessage(ChatColor.RED
						+ "...but I can't find your permissions plugin.");
			}
		}
		File corrections_file = new File(getDataFolder(), "AutoCorrections.txt");
		// save the AutoCorrections
		try {
			// check the AutoCorrections file
			if (!corrections_file.exists()) {
				getDataFolder().mkdir();
				corrections_file.createNewFile();
				for (AutoCorrection correction : default_AutoCorrections)
					AutoCorrections.add(correction);
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(
					corrections_file));
			out.write("Would you like to use AutoCorrections in your server's chat? ");
			out.newLine();
			if (AutoCorrect_on)
				out.write("   Right now, AutoCorrections are enabled.");
			else
				out.write("   Right now, AutoCorrections are disabled.");
			out.newLine();
			out.newLine();
			out.write("Would you like me to capitalize the first letter of every sentence? ");
			out.newLine();
			if (capitalize_first_letter)
				out.write("   Right now, first letter capitalization is enabled.");
			else
				out.write("   Right now, first letter capitalization is disabled.");
			out.newLine();
			out.newLine();
			out.write("Would you like me to put periods at the end of any sentences that have no terminal punctuation? ");
			out.newLine();
			if (end_with_period)
				out.write("   Right now, period addition is enabled.");
			else
				out.write("   Right now, period addition is diabled.");
			out.newLine();
			out.newLine();
			out.write("Would you like me to change any all-caps words or sentences to lowercase italics? ");
			out.newLine();
			if (change_all_caps_to_italics)
				out.write("   Right now, caps to italics conversion is enabled.");
			else
				out.write("   Right now, caps to italics conversion is diabled.");
			out.newLine();
			out.newLine();
			out.write("Would you like me to cover up profanities using magic (meaning the &k color code, not fairy dust)? ");
			out.newLine();
			if (cover_up_profanities)
				out.write("   Right now, profanity coverup is enabled.");
			else
				out.write("   Right now, profanity coverup is diabled.");
			out.newLine();
			out.newLine();
			out.write("Would you like me to replace all in-text commands with their usages when they start with a \"./\"? ");
			out.newLine();
			if (insert_command_usages)
				out.write("   Right now, command usage insertion is enabled.");
			else
				out.write("   Right now, command usage insertion is disabled.");
			out.newLine();
			String border_unit = borders[(int) (Math.random() * borders.length)], border = "";
			for (int i = 0; i < 20; i++)
				border = border + border_unit;
			out.write(border);
			out.newLine();
			for (AutoCorrection correction : AutoCorrections) {
				out.write(correction.save_line);
				out.newLine();
			}
			out.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your AutoCorrections.");
			exception.printStackTrace();
			return;
		}
		if (display_message) {
			if (AutoCorrections.size() == 0)
				sender.sendMessage(ChatColor.BLUE
						+ "Your AutoCorrect settings have been saved.");
			else if (AutoCorrections.size() == 1)
				sender.sendMessage(ChatColor.BLUE
						+ "Your AutoCorrect settings and your server's only AutoCorrection have been saved.");
			else
				sender.sendMessage(ChatColor.BLUE
						+ "Your AutoCorrect settings and your server's "
						+ AutoCorrections.size()
						+ " AutoCorrections have been saved.");
			if (sender instanceof Player)
				if (AutoCorrections.size() == 0)
					console.sendMessage(ChatColor.BLUE + sender.getName()
							+ " saved your AutoCorrect settings.");
				else if (AutoCorrections.size() == 1)
					console.sendMessage(ChatColor.BLUE
							+ sender.getName()
							+ " saved your AutoCorrect settings and your server's only AutoCorrection.");
				else
					console.sendMessage(ChatColor.BLUE
							+ sender.getName()
							+ " saved your AutoCorrect settings and your server's "
							+ AutoCorrections.size() + " AutoCorrections.");
		}
	}

	private void saveTheDeathMessages(CommandSender sender,
			boolean display_message) {
		boolean failed = false;
		// check the death messages file
		File death_messages_file = new File(getDataFolder(),
				"death messages.txt");
		if (!death_messages_file.exists()) {
			getDataFolder().mkdir();
			try {
				death_messages_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED
						+ "I couldn't create an AutoCorrections.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// save the AutoCorrections
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					death_messages_file));
			out.write("Do you want hilarious death messages to appear when people die? ");
			out.newLine();
			if (display_death_messages)
				out.write("   Right now, hilarious death messages will appear when someone dies.");
			else
				out.write("   Right now, hilarious death messages will not appear when someone dies. Boring...");
			out.newLine();
			out.newLine();
			out.write("Below is the list of death messages. You can customize them however you like and use as many colors as you want. Put \"[player]\" to show where the name of the player who dies should appear in the message.");
			out.newLine();
			for (int i = 0; i < death_messages_by_cause.size(); i++) {
				String cause = (String) death_messages_by_cause.keySet()
						.toArray()[i];
				ArrayList<String> messages = death_messages_by_cause.get(cause);
				out.write(" == " + cause + " == ");
				out.newLine();
				for (String message : messages) {
					out.write(message);
					out.newLine();
				}
			}
			out.flush();
			out.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your AutoCorrections.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed && display_message)
			if (death_messages_by_cause.size() == 0)
				sender.sendMessage(ChatColor.BLUE
						+ "You don't actually have any death messages to save.");
			else if (death_messages_by_cause.size() == 1)
				sender.sendMessage(ChatColor.BLUE
						+ "Your server's only death message has been saved.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your server's "
						+ death_messages_by_cause.size()
						+ " death messages have been saved.");
	}

	private void saveTheEpithets(CommandSender sender, boolean display_message) {
		// check the epithets file
		File epithets_file = new File(getDataFolder(), "epithets.txt");
		// save the epithets
		try {
			if (!epithets_file.exists()) {
				getDataFolder().mkdir();
				epithets_file.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(
					epithets_file));
			out.write("Make sure that there is a space after the colon for each data entry. You may use color codes everywhere.");
			out.newLine();
			out.write("Designate the default epithet for new players below. Use \"[player]\" to designate where the player's name should go.");
			out.newLine();
			out.write("default epithet: " + default_epithet);
			out.newLine();
			out.newLine();
			out.write("This is the general format for a message. Use \"[epithet]\" to designate where the epithet should go and \"[message]\" for the message.");
			out.newLine();
			out.write("default message format: " + default_message_format);
			out.newLine();
			String border_unit = borders[(int) (Math.random() * borders.length)], border = "";
			for (int i = 0; i < 20; i++)
				border = border + border_unit;
			out.write(border);
			out.newLine();
			for (int i = 0; i < epithets_by_user.size(); i++) {
				String user = (String) epithets_by_user.keySet().toArray()[i];
				out.write("     " + user + ": " + epithets_by_user.get(user));
				out.newLine();
			}
			out.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your epithets.");
			exception.printStackTrace();
			return;
		}
		if (display_message) {
			if (epithets_by_user.size() == 0)
				sender.sendMessage(ChatColor.BLUE
						+ "Your epithet settings have been saved.");
			else if (epithets_by_user.size() == 1)
				sender.sendMessage(ChatColor.BLUE
						+ "Your epithet settings and your server's only epithet have been saved.");
			else
				sender.sendMessage(ChatColor.BLUE
						+ "Your epithet settings and your server's "
						+ epithets_by_user.size()
						+ " epithets have been saved.");
			if (sender instanceof Player)
				if (epithets_by_user.size() == 0)
					sender.sendMessage(ChatColor.BLUE + sender.getName()
							+ " saved your epithet settings.");
				else if (epithets_by_user.size() == 1)
					sender.sendMessage(ChatColor.BLUE
							+ sender.getName()
							+ " saved your epithet settings and your server's only epithet.");
				else
					sender.sendMessage(ChatColor.BLUE + sender.getName()
							+ " saved your epithet settings and your server's "
							+ epithets_by_user.size() + " epithets.");
		}
	}

	private void saveTheLoginMessages(CommandSender sender,
			boolean display_message) {
		File login_messages = new File(getDataFolder(), "login messages.txt");
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(
					login_messages));
			String person = null;
			write.write("==== [server] ====");
			write.newLine();
			if (this.login_messages.get("[server]") != null) {
				for (String message : this.login_messages.get("[server]")) {
					write.write(message);
					write.newLine();
				}

			}
			for (String key : this.login_messages.keySet()) {
				write.write("==== " + key + " ====");
				write.newLine();
				if (key.startsWith("[")) {
					for (String message : this.login_messages.get(key)) {
						write.write(message);
						write.newLine();
					}
				}
			}
			for (String key : this.login_messages.keySet()) {
				write.write("==== " + key + " ====");
				write.newLine();
				if (!key.startsWith("[")) {
					for (String message : this.login_messages.get(key)) {
						write.write(message);
						write.newLine();
					}
				}
			}
		} catch (IOException e) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I'm sorry but I got an IOException while saving the login messaes.");
			if (sender instanceof Player) {
				console.sendMessage(ChatColor.DARK_RED
						+ "I'm sorry but I got an IOException while saving the login messaes.");
			}
			e.printStackTrace();
			return;
		}
		if (display_message) {
			sender.sendMessage(ChatColor.BLUE
					+ "I saved the login messages successfully.");
			if (sender instanceof Player) {
				console.sendMessage(ChatColor.BLUE
						+ "I saved the login messages successfully.");
			}
		}
	}

	private void saveTheLogoutMessages(CommandSender sender,
			boolean display_message) {
		File logout_messages = new File(getDataFolder(), "logout messages.txt");
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(
					logout_messages));
			String person = null;
			write.write("==== [server] ====");
			write.newLine();
			if (this.logout_messages.get("[server]") != null) {
				for (String message : this.logout_messages.get("[server]")) {
					write.write(message);
					write.newLine();
				}

			}
			for (String key : this.logout_messages.keySet()) {
				write.write("==== " + key + " ====");
				write.newLine();
				if (key.startsWith("[")) {
					for (String message : this.logout_messages.get(key)) {
						write.write(message);
						write.newLine();
					}
				}
			}
			for (String key : this.logout_messages.keySet()) {
				write.write("==== " + key + " ====");
				write.newLine();
				if (!key.startsWith("[")) {
					for (String message : this.logout_messages.get(key)) {
						write.write(message);
						write.newLine();
					}
				}
			}
		} catch (IOException e) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I'm sorry but I got an IOException while saving the logout messages.");
			if (sender instanceof Player) {
				console.sendMessage(ChatColor.DARK_RED
						+ "I'm sorry but I got an IOException while saving the logout messages.");
			}
			e.printStackTrace();
			return;
		}
		if (display_message) {
			sender.sendMessage(ChatColor.BLUE
					+ "I saved the logout messages successfully.");
			if (sender instanceof Player) {
				console.sendMessage(ChatColor.BLUE
						+ "I saved the logout messages successfully.");
			}
		}
	}

	private void saveTheRules(CommandSender sender, boolean display_message) {
		// check the rules file
		File rules_file = new File(getDataFolder(), "rules.txt");
		// save the rules
		try {
			if (!rules_file.exists()) {
				getDataFolder().mkdir();
				rules_file.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(rules_file));
			out.write("Type the rules below this line and above the border line!");
			out.newLine();
			String[] rules_lines = rules.split("\n");
			for (String rule_line : rules_lines) {
				out.write(rule_line);
				out.newLine();
			}
			String border_unit = borders[(int) (Math.random() * borders.length)], border = "";
			for (int i = 0; i < 20; i++)
				border = border + border_unit;
			out.write(border);
			out.newLine();
			// save the users who have accepted the rules
			if (players_who_have_accepted_the_rules.size() > 2) {
				for (int i = 0; i < players_who_have_accepted_the_rules.size() - 1; i++)
					out.write(players_who_have_accepted_the_rules.get(i) + ", ");
				out.write("and "
						+ players_who_have_accepted_the_rules
								.get(players_who_have_accepted_the_rules.size() - 1)
						+ " have all accepted the rules.");
			} else if (players_who_have_accepted_the_rules.size() == 2)
				out.write(players_who_have_accepted_the_rules.get(0) + " and "
						+ players_who_have_accepted_the_rules.get(1)
						+ " have accepted the rules.");
			else if (players_who_have_accepted_the_rules.size() == 1)
				out.write(players_who_have_accepted_the_rules.get(0)
						+ " is the only one who has accepted the rules.");
			else
				out.write("No one has accepted the rules yet!");
			out.flush();
			out.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your rules.");
			exception.printStackTrace();
			return;
		}
		if (display_message)
			if (!rules.equals(""))
				sender.sendMessage(ChatColor.BLUE
						+ "Your rules have been saved.");
			else
				sender.sendMessage(ChatColor.RED
						+ "Go write down your rules in the rules.txt!");
	}

	private void saveTheTemporaryData() {
		// ^that warning suppression is for the part were I cast an Object,
		// data[2], as an ArrayList<String> without a check
		// check the temporary file
		File temp_file = new File(getDataFolder(), "temp.txt");
		// save the warp and death histories
		try {
			if (!temp_file.exists()) {
				getDataFolder().mkdir();
				temp_file.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(temp_file));
			out.write("==== muted players ====");
			out.newLine();
			for (String key : muted_players.keySet()) {
				out.write(key + "," + muted_players.get(key));
				out.newLine();
			}
			out.write("==== mail ====");
			out.newLine();
			for (String key : mail.keySet()) {
				out.write("== " + key + " ==");
				out.newLine();
				for (String message : mail.get(key)) {
					out.write(message);
					out.newLine();
				}
			}
			out.close();
		} catch (IOException exception) {
			console.sendMessage(ChatColor.DARK_RED
					+ "I got an IOException while trying to save your temporary data.");
			exception.printStackTrace();
			return;
		}
	}

	// command methods
	private void addCorrection(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "Coming soon to a server near you!");
		// TODO
	}

	private void AFKCheck(CommandSender sender) {
		String target_player = null;
		for (Player player : server.getOnlinePlayers())
			if (player.getName().toLowerCase()
					.startsWith(parameters[0].toLowerCase()))
				target_player = player.getName();
		if (target_player == null)
			sender.sendMessage(ChatColor.RED + "I couldn't find \""
					+ parameters[0] + "\" anywhere.");
		else if (AFK_players.contains(target_player))
			sender.sendMessage(ChatColor.BLUE + target_player
					+ " is a.f.k. right now.");
		else
			sender.sendMessage(ChatColor.BLUE + target_player
					+ " is not a.f.k right now.");
	}

	private void AFKList(CommandSender sender, boolean on_login) {
		if (AFK_players.size() == 0) {
			if (!on_login)
				sender.sendMessage(ChatColor.BLUE
						+ "No one is a.f.k. right now!");
		} else if (AFK_players.size() == 1)
			sender.sendMessage(ChatColor.BLUE + AFK_players.get(0)
					+ " is the only one a.f.k. right now.");
		else if (AFK_players.size() == 2)
			sender.sendMessage(ChatColor.BLUE + AFK_players.get(0) + " and "
					+ AFK_players.get(1)
					+ " are the only ones a.f.k. right now.");
		else {
			String list = ChatColor.BLUE + "";
			for (int i = 0; i < AFK_players.size() - 1; i++)
				list = list + AFK_players.get(i) + ", ";
			sender.sendMessage(list + " and "
					+ AFK_players.get(AFK_players.size() - 1)
					+ " are all a.f.k.");
		}
	}

	private void AFKToggle(CommandSender sender) {
		Player player = (Player) sender;
		if (!AFK_players.contains(player.getName())) {
			AFK_players.add(player.getName());
			server.broadcastMessage(ChatColor.GRAY + player.getName()
					+ " is now a.f.k.");
		} else {
			AFK_players.remove(player.getName());
			server.broadcastMessage(ChatColor.BLUE + player.getName()
					+ " is back.");
		}
	}

	private void announce(CommandSender sender) {
		byte extra_param = 0;
		Boolean is_important = null;
		// figure out if it's important, unimportant, or normal
		if (parameters[0].toLowerCase().startsWith("[i")) {
			extra_param++;
			is_important = true;
		} else if (parameters[0].toLowerCase().startsWith("[u")) {
			extra_param++;
			is_important = false;
		}
		// construct the announcement
		String announcement = "";
		for (int i = extra_param; i < parameters.length; i++) {
			announcement = announcement + parameters[i];
			if (i < parameters.length - 1)
				announcement = announcement + " ";
		}
		announcement = AutoCorrect(sender, announcement);
		if (is_important)
			server.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD
					+ "THIS IS IMPORTANT! LISTEN UP!");
		String creator = "server";
		if (sender instanceof Player)
			creator = sender.getName();
		String epithet = epithets_by_user.get(creator);
		if (epithet == null)
			epithet = sender.getName();
		server.broadcastMessage(ChatColor.BLUE + colorCode(announcement)
				+ "\n-- " + colorCode(epithet));
		if (creator.equals("server") && epithets_by_user.get("server") != null)
			creator = epithets_by_user.get("server");
		ArrayList<String> players_who_were_online = new ArrayList<String>();
		for (Player player : server.getOnlinePlayers())
			players_who_were_online.add(player.getName());
		announcements.add(new Announcement(announcement, creator,
				players_who_were_online, is_important));
	}

	private void changeEpithet(CommandSender sender) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;
		String owner, epithet = "";
		int extra_param = 0;
		if (parameters[0].toLowerCase().startsWith("for:")) {
			owner = parameters[0].substring(4);
			extra_param++;
		} else if (player != null)
			owner = player.getName();
		else
			owner = "server";
		for (int i = extra_param; i < parameters.length; i++)
			if (!epithet.equals(""))
				epithet = epithet + " " + parameters[i];
			else
				epithet = parameters[i];
		if (!owner.equalsIgnoreCase("server") && !epithet.equals("")) {
			// eliminate preceding spaces
			while (epithet.startsWith(" "))
				epithet = epithet.substring(1);
			boolean epithet_is_acceptable = epithet.length() > 0;
			if (true_username_required && player != null
					&& !player.hasPermission("myscribe.admin")) {
				epithet_is_acceptable = epithet.length() > 0
						&& !epithet.contains("&k")
						&& epithet.replaceAll("(&+([a-fA-Fk-oK-OrR0-9]))", "")
								.contains(player.getName());
			}
			if ((epithet_is_acceptable && player.getName().equals(owner))
					|| player.hasPermission("myscribe.admin")) {
				epithets_by_user.put(owner, epithet);
				if (player != null && player.getName().equalsIgnoreCase(owner))
					player.sendMessage(ChatColor.BLUE
							+ "Henceforth, you shall be known as \""
							+ colorCode(epithet) + ChatColor.BLUE + ".\"");
				else
					sender.sendMessage(ChatColor.BLUE + owner
							+ " shall henceforth be known as \""
							+ colorCode(epithet) + ChatColor.BLUE + ".\"");
			} else
				sender.sendMessage(ChatColor.RED
						+ "Your epithet must contain your true username and not use magic.");
		} else if (owner.equalsIgnoreCase("server") && !epithet.equals("")) {
			say_format = epithet;
			sender.sendMessage(ChatColor.BLUE
					+ "You set the \"/say\" epithet to \"" + colorCode(epithet)
					+ ChatColor.BLUE + ".\"");
		} else if (epithet.equals(""))
			sender.sendMessage(ChatColor.RED
					+ "You forgot to tell me the epithet you want!");
	}

	private void getRecipe(CommandSender sender) {
		// assemble the query
		String query = "";
		for (String parameter : parameters)
			query = query + parameter.toLowerCase() + " ";
		query = query.substring(0, query.length() - 1);
		// get the item's I.D. and data
		int id = -1, data = -1;
		try {
			id = Integer.parseInt(query);
		} catch (NumberFormatException exception) {
			if (query.split(":").length == 2) {
				try {
					id = Integer.parseInt(query.split(":")[0]);
					data = Integer.parseInt(query.split(":")[1]);
				} catch (NumberFormatException exception2) {
					id = -1;
					data = -1;
					Integer[] temp = Wiki.getItemIdAndData(query.split(" "));
					if (temp[0] != null) {
						id = temp[0];
						data = temp[1];
					}
				}
			} else {
				Integer[] temp = Wiki.getItemIdAndData(query.split(" "));
				if (temp[0] != null) {
					id = temp[0];
					data = temp[1];
				}
			}
		}
		if (Wiki.getRecipe(id, data) != null)
			sender.sendMessage(colorCode(recipes[id]));
		else if (Wiki.getItemName(id, data, false) != null)
			sender.sendMessage(ChatColor.BLUE + "You can't craft a "
					+ Wiki.getItemName(id, data, false) + "!");
		else if (query.toLowerCase().startsWith("a")
				|| query.toLowerCase().startsWith("e")
				|| query.toLowerCase().startsWith("i")
				|| query.toLowerCase().startsWith("o")
				|| query.toLowerCase().startsWith("u"))
			sender.sendMessage(ChatColor.RED
					+ "Sorry, but I don't know what an \"" + query + "\" is.");
		else
			sender.sendMessage(ChatColor.RED
					+ "Sorry, but I don't know what a \"" + query + "\" is.");
	}

	private void id(CommandSender sender) {
		if (parameters.length == 0 || parameters[0].equalsIgnoreCase("this")
				|| parameters[0].equalsIgnoreCase("that"))
			if (sender instanceof Player) {
				int id = ((Player) sender).getTargetBlock(null, 1024)
						.getTypeId();
				String item_name = Wiki.getItemName(id, ((Player) sender)
						.getTargetBlock(null, 1024).getData(), false);
				if (item_name != null)
					if (item_name.endsWith("s") && !item_name.endsWith("ss"))
						sender.sendMessage(ChatColor.GRAY + "Those "
								+ item_name
								+ " you're pointing at have the I.D. " + id
								+ ".");
					else
						sender.sendMessage(ChatColor.GRAY + "That " + item_name
								+ " you're pointing at has the I.D. " + id
								+ ".");
				else
					sender.sendMessage(ChatColor.RED
							+ "Uh...what in the world " + ChatColor.ITALIC
							+ "is" + ChatColor.RED
							+ " that thing you're pointing at?");
				item_name = Wiki.getItemName(((Player) sender).getItemInHand()
						.getTypeId(), ((Player) sender).getItemInHand()
						.getData().getData(), false);
				if (item_name != null)
					if (item_name.endsWith("s") && !item_name.endsWith("ss"))
						sender.sendMessage(ChatColor.GRAY + "Those "
								+ item_name + " you're holding have the I.D. "
								+ id + ".");
					else
						sender.sendMessage(ChatColor.GRAY + "That " + item_name
								+ " you're holding has the I.D. " + id + ".");
				else
					sender.sendMessage(ChatColor.RED
							+ "Uh...what in the world " + ChatColor.ITALIC
							+ "is" + ChatColor.RED
							+ " that thing you're holding?");
			} else
				sender.sendMessage(ChatColor.RED
						+ "You forgot to tell me what item or I.D. you want identified!");
		else {
			String query = "";
			for (String parameter : parameters)
				if (query.equals(""))
					query = parameter;
				else
					query = query + " " + parameter;
			try {
				int id = Integer.parseInt(query);
				String item_name = Wiki.getItemName(id, -1, true);
				if (item_name != null)
					if (item_name.endsWith("s") && !item_name.endsWith("ss"))
						sender.sendMessage(ChatColor.GRAY
								+ item_name.substring(0, 1).toUpperCase()
								+ item_name.substring(1) + " have the I.D. "
								+ id + ".");
					else
						sender.sendMessage(ChatColor.GRAY
								+ item_name.substring(0, 1).toUpperCase()
								+ item_name.substring(1) + " has the I.D. "
								+ id + ".");
				else
					sender.sendMessage(ChatColor.RED + "No item has the I.D. "
							+ id + ".");
			} catch (NumberFormatException exception) {
				Integer[] id_and_data = Wiki.getItemIdAndData(query.split(" "));
				if (id_and_data == null) {
					if (query.toLowerCase().startsWith("a")
							|| query.toLowerCase().startsWith("e")
							|| query.toLowerCase().startsWith("i")
							|| query.toLowerCase().startsWith("o")
							|| query.toLowerCase().startsWith("u"))
						sender.sendMessage(ChatColor.RED
								+ "Sorry, but I don't know what an \"" + query
								+ "\" is.");
					else
						sender.sendMessage(ChatColor.RED
								+ "Sorry, but I don't know what a \"" + query
								+ "\" is.");
					return;
				}
				// this part seems odd because it seems like it's a long
				// roundabout way to get item_name. You might think: isn't
				// item_name the same as query?
				// Wrong. A query can (and probably is) just a few letters from
				// the name of the item. By finding the id, then using that to
				// get the name, it's
				// an effective autocompletion of the item name
				String item_name = Wiki.getItemName(id_and_data[0],
						id_and_data[1], false);
				// if it found it, send the message
				String id = String.valueOf(id_and_data[0]);
				if (id_and_data[1] > 0)

					if (item_name.endsWith("s") && !item_name.endsWith("ss"))
						sender.sendMessage(ChatColor.GRAY
								+ item_name.substring(0, 1).toUpperCase()
								+ item_name.substring(1) + " have the I.D. "
								+ id + ".");
					else
						sender.sendMessage(ChatColor.GRAY
								+ item_name.substring(0, 1).toUpperCase()
								+ item_name.substring(1) + " has the I.D. "
								+ id + ".");
				return;
			}
		}
	}
}