package REALDrummer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
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
	private static Server server;
	private static ConsoleCommandSender console;
	private String[] parameters, profanities = { "fuck", "fck", "fuk", "Goddamn", "Goddam", "damn", "shit", "sht", "dammit", "bastard", "bitch", "btch",
			"damnit", "cunt", "asshole", " ass " }, magic_words = { "Sha-ZAM!", "ALAKAZAM!", "POOF!", "BOOM!", "KA-POW!", "Sha-FWAAAH!", "Kali-kaPOW!",
			"TORTELLINI!", "Kras-TOPHALEMOTZ!", "Wah-SHAM!", "Wa-ZAM!", "Wha-ZOO!", "KERFUFFLE!", "WOOOOWOWOWOWOW!", "CREAMPUFF WADLEEDEE!", "FLUFFENNUGGET!",
			"FALALALALAAAAAA-lala-la-LAAAA!", "SHNITZ-LIEDERHOSEN!", "BWAAAAAAAAAAAAH!", "FEE-FI-FO-FUM!", "ROTISSERIE!", "LALA-BIBIAY!", "Kurlaka-FWAH!" },
			borders = { "[]", "\\/", "/\\", "\"*", "_^", "-=", ":;", "\u00A1!", "&%", "#@", ",.", "\u00BF?", "<>", ")(", "~$" }, uncraftable_items = {};
	private HashMap<String, String> epithets_by_user = new HashMap<String, String>();
	private HashMap<String[], String> item_recipes = new HashMap<String[], String>();
	private HashMap<String, ArrayList<String>> death_messages_by_cause = new HashMap<String, ArrayList<String>>();
	private HashMap<Player, String> message_beginnings = new HashMap<Player, String>(), command_beginnings = new HashMap<Player, String>();
	private HashMap<Integer, String> item_IDs = new HashMap<Integer, String>();
	private static ArrayList<String> strings_to_correct = new ArrayList<String>(), corrected_strings = new ArrayList<String>(),
			unless_strings = new ArrayList<String>(), AFK_players = new ArrayList<String>(), players_who_have_accepted_the_rules = new ArrayList<String>(),
			players_who_have_read_the_rules = new ArrayList<String>(), login_messages = new ArrayList<String>(), logout_messages = new ArrayList<String>();
	private static HashMap<Enchantment, String> enchantment_names = new HashMap<Enchantment, String>();
	private static ArrayList<Boolean> true_means_before = new ArrayList<Boolean>();
	private static String rules = "", default_epithet = "", default_message_format = "", say_epithet = "";
	private static boolean players_must_accept_rules = true, AutoCorrect_on = true, capitalize_first_letter = true, end_with_period = true,
			change_all_caps_to_italics = true, true_username_required = true, cover_up_profanities = true, display_death_messages = true;

	// TODO: set up config questions for true_username_required and
	// cover_up_profanities

	// plugin enable/disable and the command operator
	public void onEnable() {
		server = getServer();
		console = server.getConsoleSender();
		server.getPluginManager().registerEvents(this, this);
		// input enchantment names
		enchantment_names.put(Enchantment.ARROW_DAMAGE, "Power");
		enchantment_names.put(Enchantment.ARROW_FIRE, "Flame");
		enchantment_names.put(Enchantment.ARROW_INFINITE, "Infinite");
		enchantment_names.put(Enchantment.ARROW_KNOCKBACK, "Punch");
		enchantment_names.put(Enchantment.DAMAGE_ALL, "Sharpness");
		enchantment_names.put(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods");
		enchantment_names.put(Enchantment.DAMAGE_UNDEAD, "Smite");
		enchantment_names.put(Enchantment.DIG_SPEED, "Efficiency");
		enchantment_names.put(Enchantment.DURABILITY, "Unbreaking");
		enchantment_names.put(Enchantment.FIRE_ASPECT, "Fire Aspect");
		enchantment_names.put(Enchantment.KNOCKBACK, "Knockback");
		enchantment_names.put(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");
		enchantment_names.put(Enchantment.LOOT_BONUS_MOBS, "Looting");
		enchantment_names.put(Enchantment.OXYGEN, "Respiration");
		enchantment_names.put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
		enchantment_names.put(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection");
		enchantment_names.put(Enchantment.PROTECTION_FALL, "Feather Falling");
		enchantment_names.put(Enchantment.PROTECTION_FIRE, "Fire Protection");
		enchantment_names.put(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection");
		enchantment_names.put(Enchantment.SILK_TOUCH, "Silk Touch");
		enchantment_names.put(Enchantment.WATER_WORKER, "Aqua Affinity");
		// input item I.D.s
		for (Material material : Material.values())
			item_IDs.put(material.getId(), replace(material.toString().toLowerCase(), "_", " ", null, true, true));
		// input item recipes
		item_recipes.put(new String[] { "wooden planks", "wood planks", "planks" }, ChatColor.AQUA + "Just craft any kind of wood (logs).");
		item_recipes.put(new String[] { "glass" }, ChatColor.AQUA + "Cook sand.");
		item_recipes.put(new String[] { "lapis lazuli blocks", "lapis blocks" }, "&1lapis lazuli\nLLL\nLLL\nLLL");
		item_recipes.put(new String[] { "dispenser", "shooter" }, "&8cobblestone&f, bow, &4redstone dust\n&8CCC\nC&fB&8C\nC&4R&8C");
		item_recipes.put(new String[] { "sandstone", "sand brick", "sandbrick", "sand stone" }, "&esand\n&0---\n-&eSS\n&0-&eSS");
		item_recipes.put(new String[] { "note block", "music block", "sound block", "speaker" },
				"&e&m&nwooden planks&f, &4redstone dust\n&e&m&nWWW\nW&4R&e&m&nW\nWWW");
		item_recipes.put(new String[] { "bed" }, "wool, &e&m&nwooden planks\n&0---\n&fWWW\n&e&m&nWWW");
		item_recipes.put(new String[] { "powered rails", "redstone rails", "powered railroad tracks", "redstone railroad tracks" },
				"&6gold&f, &e&msticks&f, &4redstone dust\n&6G&0-&6G\nG&f&e&mS&6G\nG&4R&6G");
		item_recipes.put(new String[] { "detector rails", "detection rails", "sensor rails", "sensory rails", "pressure rails", "pressure plate rails",
				"detector railroad tracks", "detection railroad tracks", "sensor railroad tracks", "sensory railroad tracks", "pressure railroad tracks",
				"pressure plate railroad tracks" }, "&7iron ingots&f, &8&nstone pressure plate&f, &4redstone dust\n&7I&0-&7I\nI&8&nS&7I\nI&4R&7I");
		item_recipes.put(new String[] { "sticky pistons", "slimy pistons" }, "&aslimeballs&f, &e&npistons\n&0---\n-&aS&0-\n-&e&nP&0-");
		item_recipes.put(new String[] { "pistons" }, "&e&m&nwooden planks&f, &8cobblestone&f, &7iron ingots&f, &4redstone dust\n&e&m&nWWW\n&8C&7I&8C\nC&4R&8C");
		item_recipes.put(new String[] { "wool blocks" }, "string\n&0---\n-&fSS\n&0-&fSS");
		item_recipes.put(new String[] { "gold", "golden", "blocks" }, "&6gold ingots\nGGG\nGGG\nGGG");
		item_recipes.put(new String[] { "iron blocks", "blocks of iron", "block of iron" }, "&7iron ingots\nIII\nIII\nIII");
		item_recipes.put(new String[] { "slabs", "half blocks" },
				"&5&kM &fcan be cobblestone, stone, stone bricks, wooden planks, bricks, or sandstone\n&0---\n---          &bx6\n&5&kMMM");
		item_recipes.put(new String[] { "bricks", "brick blocks" }, "&cBricks &fcan be made by cooking clay.\n&0---\n-&cBB\n&0-&cBB");
		item_recipes.put(new String[] { "TNT", "T.N.T.", "dynamite", "trinitrotoluene" }, "&esand&f, gunpowder\nG&eS&fG\n&eS&fG&eS\n&fG&eS&fG");
		item_recipes.put(new String[] { "bookshelf", "bookshelves", "bookcases" }, "&e&m&nwooden planks&f, &cbooks\n&e&m&nWWW\n&cBBB\n&e&m&nWWW");
		item_recipes.put(new String[] { "torches", "fire sticks" }, "coal or charcoal, &e&msticks\n&0---\n-&fC&0-\n-&e&mS&0-");
		loadTheEpithets(console);
		loadTheAutoCorrectSettings(console);
		loadTheDeathMessages(console);
		loadTheLoginMessages(console);
		loadTheLogoutMessages(console);
		loadTheRules(console);
	}

	public void onDisable() {
		saveTheEpithets(console, true);
		saveTheAutoCorrectSettings(console, true);
		saveTheDeathMessages(console, true);
		saveTheLoginMessages(console, true);
		saveTheLogoutMessages(console, true);
		saveTheRules(console, true);
	}

	public boolean onCommand(CommandSender sender, Command command, String command_label, String[] my_parameters) {
		parameters = my_parameters;
		if (command_label.equalsIgnoreCase("myChat")
				&& parameters[0].equalsIgnoreCase("load")
				&& parameters.length > 1
				&& (parameters[1].equalsIgnoreCase("chat") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.equalsIgnoreCase("chat")))) {
			if (!(sender instanceof Player) || sender.hasPermission("mychat.admin")) {
				loadTheEpithets(sender);
				loadTheAutoCorrectSettings(sender);
				loadTheDeathMessages(sender);
				loadTheLoginMessages(sender);
				loadTheLogoutMessages(sender);
				loadTheRules(sender);
			} else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myChat load" + ChatColor.RED + ".");
			return true;
		} else if (command_label.equalsIgnoreCase("myChat")
				&& parameters[0].equalsIgnoreCase("save")
				&& parameters.length > 1
				&& (parameters[1].equalsIgnoreCase("chat") || parameters[1].equalsIgnoreCase("myChat") || (parameters.length > 2
						&& parameters[1].equalsIgnoreCase("the") && parameters[2].equalsIgnoreCase("chat")))) {
			if (!(sender instanceof Player) || sender.hasPermission("mychat.admin")) {
				saveTheEpithets(sender, true);
				saveTheAutoCorrectSettings(sender, true);
				saveTheDeathMessages(sender, true);
				saveTheLoginMessages(sender, true);
				saveTheLogoutMessages(sender, true);
				saveTheRules(sender, true);
			} else
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to use " + ChatColor.BLUE + "/myChat save" + ChatColor.RED + ".");
			return true;
		} else if (command_label.equalsIgnoreCase("epithet")) {
			if (parameters.length > 0 && (!(sender instanceof Player) || sender.hasPermission("mychat.epithet")))
				changeEpithet(sender);
			else if (parameters.length > 0)
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to change your epithet.");
			else
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what I should change your epithet to!");
			return true;
		} else if (command_label.equalsIgnoreCase("correct")) {
			if (parameters.length >= 2 && (!(sender instanceof Player) || sender.hasPermission("mychat.correct") || sender.hasPermission("mychat.admin")))
				addCorrection(sender);
			else if (sender instanceof Player && !sender.hasPermission("mychat.correct"))
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to create your own AutoCorrections.");
			else
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what correction you want to make!");
			return true;
		} else if (command_label.equalsIgnoreCase("afklist")
				|| (command_label.equalsIgnoreCase("afk") && parameters.length > 0 && parameters[0].equalsIgnoreCase("list"))) {
			AFKList(sender, false);
			return true;
		} else if (command_label.equalsIgnoreCase("afk") && parameters.length > 0) {
			AFKCheck(sender);
			return true;
		} else if (command_label.equalsIgnoreCase("afk")) {
			if (sender instanceof Player)
				AFKToggle(sender);
			else
				sender.sendMessage(ChatColor.RED + "You're a console! You can't be away from the keyboard! You're IN the computer!");
			return true;
		} else if (command_label.equalsIgnoreCase("rules")) {
			if (!rules.equals("")) {
				if (sender instanceof Player)
					players_who_have_read_the_rules.add(sender.getName());
				sender.sendMessage(colorCode(rules));
			} else
				sender.sendMessage(ChatColor.RED + "As I said, the rules haven't been written down yet, so you can't read them right now. Sorry.");
			return true;
		} else if (command_label.equalsIgnoreCase("accept")) {
			if (!(sender instanceof Player))
				sender.sendMessage(ChatColor.RED + "You don't need to accept the rules! YOU MADE THE RULES!");
			else if (players_who_have_read_the_rules.contains(sender.getName()) && !players_who_have_accepted_the_rules.contains(sender.getName())) {
				players_who_have_accepted_the_rules.add(sender.getName());
				sender.sendMessage(ChatColor.BLUE + "Remember the rules and have fun! You can read the rules again any time with /rules.");
			} else if (players_who_have_accepted_the_rules.contains(sender.getName()))
				sender.sendMessage(ChatColor.BLUE + "You've already accepted the rules. You're good to go. Have fun.");
			else if (!rules.equals(""))
				sender.sendMessage(colorCode("&cYou haven't even &oread%o the rules! I know you didn't! Read 'em!"));
			else
				sender.sendMessage(ChatColor.RED + "I already told you: you don't have to accept the rules right now. They haven't been written down yet.");
			return true;
		} else if (command_label.toLowerCase().startsWith("color") || command_label.equalsIgnoreCase("codes")) {
			sender.sendMessage(colorCode("&00 &11 &22 &33 &44 &55 &66 &77 &88 &99 &aa &bb &cc &dd &ee &ff &kk&f(k) &f&ll &f&mm &f&nn &f&oo"));
			return true;
		} else if (command_label.equalsIgnoreCase("enchant") || command_label.equalsIgnoreCase("ench")) {
			if (!(sender instanceof Player))
				sender.sendMessage(ChatColor.RED
						+ "Okay. Sure. Let me just...-_- You're a console. You know you're not holding any enchantable items right? ...'cause you have no hands? ...'cause YOU'RE A CONSOLE!?");
			else if (!sender.isOp())
				sender.sendMessage(ChatColor.RED
						+ "Sorry, but you don't have permission to enchant items at your whim. You have to kill monsters and mine and earn those levels just like everyone else.");
			else if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what enchantment you want to put on it!");
			else
				enchantItem(sender);
			return true;
		} else if (command_label.equalsIgnoreCase("say")) {
			if (!(sender instanceof Player) || sender.isOp()) {
				String message = "";
				for (String parameter : parameters)
					message = message + " " + parameter;
				server.broadcastMessage(colorCode(replace(replace(default_message_format, "[epithet]", say_epithet, null, true, false), "[message]", message,
						null, true, false)));
			} else
				sender.sendMessage(ChatColor.RED + "Only ops have permission to broadcast messages with " + ChatColor.BLUE + "/say" + ChatColor.RED + ".");
		} else if (command_label.equalsIgnoreCase("announce") || command_label.equalsIgnoreCase("declare") || command_label.equalsIgnoreCase("decree")) {
			// TODO
		} else if (command_label.equalsIgnoreCase("enable") || command_label.equalsIgnoreCase("en")) {
			if (sender instanceof Player && !sender.isOp())
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to enable and disable plugins.");
			else if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "You forgot to tell me which plugin to enable!");
			else {
				for (Plugin target : server.getPluginManager().getPlugins())
					if (target.getName().toLowerCase().startsWith(parameters[0].toLowerCase())) {
						if (!target.isEnabled()) {
							server.getPluginManager().enablePlugin(target);
							sender.sendMessage(ChatColor.BLUE + target.getName() + " has been enabled.");
						} else
							sender.sendMessage(ChatColor.RED + target.getName() + " is already enabled.");
						return true;
					}
				sender.sendMessage(ChatColor.RED + "Sorry, but I couldn't find a plugin called \"" + parameters[0] + ".\"");
				return true;
			}
		} else if (command_label.equalsIgnoreCase("disable") || command_label.equalsIgnoreCase("dis")) {
			if (sender instanceof Player && !sender.isOp())
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to enable and disable plugins.");
			else if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "You forgot to tell me which plugin to disable!");
			else {
				for (Plugin target : server.getPluginManager().getPlugins())
					if (target.getName().toLowerCase().startsWith(parameters[0].toLowerCase())) {
						if (target.isEnabled()) {
							server.getPluginManager().disablePlugin(target);
							sender.sendMessage(ChatColor.BLUE + target.getName() + " has been disbabled.");
						} else
							sender.sendMessage(ChatColor.RED + target.getName() + " is already disabled.");
						return true;
					}
				sender.sendMessage(ChatColor.RED + "Sorry, but I couldn't find a plugin called \"" + parameters[0] + ".\"");
				return true;
			}
		} else if (command_label.equalsIgnoreCase("recipe") || command_label.equalsIgnoreCase("craft")) {
			if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what item you want the recipe for!");
			else
				getRecipe(sender);
		} else if (command_label.equalsIgnoreCase("ids") || command_label.equalsIgnoreCase("id")) {
			if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "You forgot to give me an I.D. or an item name!");
			else {
				String query = parameters[0];
				if (parameters.length > 1)
					query = "";
				for (String parameter : parameters)
					if (query.equals(""))
						query = parameter;
					else
						query = query + " " + parameter;
				try {
					int id = Integer.parseInt(query);
					String item = item_IDs.get(id);
					if (item != null)
						sender.sendMessage(ChatColor.BLUE + item + " has the I.D. " + id);
					else
						sender.sendMessage(ChatColor.RED + "No item has the I.D. " + id);
				} catch (NumberFormatException exception) {
					for (Object id : item_IDs.keySet().toArray())
						if (item_IDs.get(id).toLowerCase().startsWith(query.toLowerCase())) {
							sender.sendMessage(ChatColor.BLUE + item_IDs.get(id) + " has the I.D. " + id + ".");
							return true;
						}
					if (query.toLowerCase().startsWith("a") || query.toLowerCase().startsWith("e") || query.toLowerCase().startsWith("i")
							|| query.toLowerCase().startsWith("o") || query.toLowerCase().startsWith("u"))
						sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what an \"" + query + "\" is.");
					else
						sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what a \"" + query + "\" is.");
				}
				return true;
			}
		}
		return false;
	}

	// intra-command methods
	private String AutoCorrect(String message) {
		String special_message = message;
		if (AutoCorrect_on && !special_message.startsWith("http") && !special_message.startsWith("www.")) {
			while (special_message.endsWith("/") || special_message.endsWith(",") || special_message.endsWith(">"))
				special_message = special_message.substring(0, special_message.length() - 1);
			// use spaces to make punctuation separate words
			if (special_message.length() > 1)
				for (int i = 1; i < special_message.length(); i++)
					if (!special_message.substring(i - 1, i).toLowerCase().equals(special_message.substring(i - 1, i).toUpperCase())
							&& special_message.substring(i, i + 1).toLowerCase().equals(special_message.substring(i, i + 1).toUpperCase())
							&& !special_message.substring(i, i + 1).equals(" ") && !special_message.substring(i, i + 1).equals("'")
							&& (i + 2 > special_message.length() || !isColorCode(special_message.substring(i, i + 2), null, null))) {
						try {
							Integer.parseInt(special_message.substring(i, i + 1));
						} catch (NumberFormatException exception) {
							special_message = special_message.substring(0, i) + " " + special_message.substring(i);
						}
					}
			special_message = " " + special_message + " ";
			// perform corrections
			for (int i = 0; i < strings_to_correct.size(); i++)
				special_message = replace(special_message, strings_to_correct.get(i), corrected_strings.get(i), unless_strings.get(i),
						true_means_before.get(i), true);
			String[] words = special_message.split(" ");
			// change all capital words to italics
			if (change_all_caps_to_italics) {
				for (int i = 0; i < words.length; i++) {
					if ((words[i].length() > 1 || (i > 0 && words[i - 1].contains("&o")) || (i < words.length - 1 && words[i + 1].contains("&o")))
							&& !words[i].contains(":") && !words[i].contains("=") && !words[i].contains("\\") && words[i].equals(words[i].toUpperCase())
							&& !words[i].toLowerCase().equals(words[i]) && !replace(words[i], " ", "", null, true, true).equalsIgnoreCase("XD")) {
						if (i == words.length - 1 && !words[i].endsWith(".") && !words[i].endsWith("!") && !words[i].endsWith("?") && !words[i].endsWith(".\"")
								&& !words[i].endsWith("!\"") && !words[i].endsWith("?\""))
							if (words[i].endsWith("\""))
								words[i] = words[i].substring(0, words[i].length() - 1) + "!\"";
							else
								words[i] = words[i] + "!";
						words[i] = "&o" + words[i].toLowerCase() + "%o";
					}
				}
			}
			// reconstruct the message from the words
			special_message = "";
			for (String word : words)
				if (special_message.equals(""))
					special_message = word;
				else
					special_message = special_message + " " + word;
			if (cover_up_profanities) {
				special_message = replace(special_message, " ass ", " a&kss%k ", null, true, true);
				special_message = replace(special_message, " dumbass ", " dumba&kss%k ", null, true, true);
				special_message = replace(special_message, " badass ", " bada&kss%k ", null, true, true);
				for (String profanity : profanities)
					special_message = replace(special_message, profanity, profanity.substring(0, 1) + "&k" + profanity.substring(1) + "%k", null, true, true);
			}
			words = special_message.split(" ");
			// capitalize the first letter of every sentence if it is not a
			// correction
			if (capitalize_first_letter)
				for (int i = 0; i < words.length; i++)
					if (words[i].length() > 0 && !special_message.startsWith("*") && !special_message.endsWith("*")
							&& (i == 0 || (words[i - 1].endsWith(".") || words[i - 1].endsWith("!") || words[i - 1].endsWith("?")))) {
						int first_letter_index = 0;
						while (first_letter_index + 1 <= words[i].length() && words[i].substring(first_letter_index, first_letter_index + 1).equals(" "))
							first_letter_index++;
						while (first_letter_index + 2 <= words[i].length()
								&& isColorCode(words[i].substring(first_letter_index, first_letter_index + 2), null, null))
							first_letter_index = first_letter_index + 2;
						words[i] = words[i].substring(0, first_letter_index) + words[i].substring(first_letter_index, first_letter_index + 1).toUpperCase()
								+ words[i].substring(first_letter_index + 1);
					}
			special_message = "";
			for (int i = 0; i < words.length; i++)
				special_message = special_message + words[i] + " ";
			special_message = special_message.substring(0, special_message.length() - 1);
			// eliminate extra spaces between letters and punctuation
			for (int i = 1; i < special_message.length(); i++) {
				// make sure it's not a letter
				if (special_message.substring(i, i + 1).equals(".") || special_message.substring(i, i + 1).equals("!")
						|| special_message.substring(i, i + 1).equals("?")) {
					while (i > 0 && special_message.substring(i - 1, i).equals(" "))
						special_message = special_message.substring(0, i - 1) + special_message.substring(i);
				}
			}
			special_message = replace(special_message, "  ", " ", null, true, true);
			while (special_message.length() >= 2 && isColorCode(special_message.substring(special_message.length() - 2), null, null))
				special_message = special_message.substring(0, special_message.length() - 2);
			// end lines with a period if no terminal punctuation exists and the
			// message doesn't start with or end with a * (correction) and the
			// message's last or second to last characters are not colons
			// (emoticons)
			if (special_message.endsWith("%o"))
				special_message = special_message.substring(0, special_message.length() - 2);
			if (end_with_period
					&& special_message.length() > 0
					&& !(special_message.endsWith(".")
							|| special_message.endsWith("?")
							|| special_message.endsWith("!")
							|| special_message.endsWith(".\"")
							|| special_message.endsWith("?\"")
							|| special_message.endsWith("!\"")
							|| special_message.startsWith("*")
							|| special_message.endsWith("*")
							|| special_message.endsWith(":")
							|| special_message.endsWith("=")
							|| special_message.endsWith(";")
							|| (special_message.length() >= 2 && (special_message.substring(special_message.length() - 2, special_message.length() - 1).equals(
									":")
									|| special_message.substring(special_message.length() - 2, special_message.length() - 1).equals("=") || special_message
									.substring(special_message.length() - 2, special_message.length() - 1).equals(";")))
							|| special_message.toUpperCase().endsWith("XD") || special_message.endsWith("<3") || (special_message.length() >= 3
							&& special_message.substring(special_message.length() - 1).equalsIgnoreCase(
									special_message.substring(special_message.length() - 3, special_message.length() - 2)) && (special_message.toLowerCase()
							.endsWith("o") || special_message.toLowerCase().endsWith("t") || special_message.endsWith("-"))))
					&& !special_message.endsWith("\\"))
				if (!special_message.endsWith("\""))
					special_message = special_message + ".";
				else
					special_message = special_message.substring(0, special_message.length() - 1) + ".\"";
		}
		return replace(special_message, "\\", "", "\\", false, true);
	}

	private static String colorCode(String text) {
		text = "&f" + text;
		// put color codes in the right order if they're next to each other
		for (int i = 0; i < text.length() - 3; i++)
			if (isColorCode(text.substring(i, i + 2), false, true) && isColorCode(text.substring(i + 2, i + 4), true, true))
				text = text.substring(0, i) + text.substring(i + 2, i + 4) + text.substring(i, i + 2) + text.substring(i + 4);
		String current_color_code = "";
		boolean anti_codes_found = false;
		for (int i = 0; i < text.length() - 1; i++) {
			if (isColorCode(text.substring(i, i + 2), null, true))
				current_color_code = current_color_code + text.substring(i, i + 2);
			else
				while (text.length() >= i + 2 && isColorCode(text.substring(i, i + 2), null, false)) {
					current_color_code = replace(current_color_code, "&" + text.substring(i + 1, i + 2), "", null, true, true);
					anti_codes_found = true;
					text = text.substring(0, i) + text.substring(i + 2);
				}
			if (anti_codes_found) {
				anti_codes_found = false;
				text = text.substring(0, i) + current_color_code + text.substring(i);
			}
		}
		String colored_text = ChatColor.translateAlternateColorCodes('&', text);
		return colored_text;
	}

	private static Boolean isColorCode(String text, Boolean true_non_formatting_null_either, Boolean true_non_anti_null_either) {
		if (((true_non_anti_null_either == null || true_non_anti_null_either == true) && text.substring(0, 1).equals("&"))
				|| ((true_non_anti_null_either == null || true_non_anti_null_either == false) && text.substring(0, 1).equals("%")))
			if (((true_non_formatting_null_either == null || true_non_formatting_null_either) && (text.substring(1, 2).equalsIgnoreCase("0")
					|| text.substring(1, 2).equalsIgnoreCase("1") || text.substring(1, 2).equalsIgnoreCase("2") || text.substring(1, 2).equalsIgnoreCase("3")
					|| text.substring(1, 2).equalsIgnoreCase("4") || text.substring(1, 2).equalsIgnoreCase("5") || text.substring(1, 2).equalsIgnoreCase("6")
					|| text.substring(1, 2).equalsIgnoreCase("7") || text.substring(1, 2).equalsIgnoreCase("8") || text.substring(1, 2).equalsIgnoreCase("9")
					|| text.substring(1, 2).equalsIgnoreCase("a") || text.substring(1, 2).equalsIgnoreCase("b") || text.substring(1, 2).equalsIgnoreCase("c")
					|| text.substring(1, 2).equalsIgnoreCase("d") || text.substring(1, 2).equalsIgnoreCase("e") || text.substring(1, 2).equalsIgnoreCase("f")))
					|| ((true_non_formatting_null_either == null || !true_non_formatting_null_either) && (text.substring(1, 2).equalsIgnoreCase("k")
							|| text.substring(1, 2).equalsIgnoreCase("l") || text.substring(1, 2).equalsIgnoreCase("m")
							|| text.substring(1, 2).equalsIgnoreCase("n") || text.substring(1, 2).equalsIgnoreCase("o"))))
				return true;
		return false;
	}

	private static String replace(String to_return, String to_change, String to_change_to, String unless, boolean true_means_before, boolean replace_all) {
		if (!to_return.contains(to_change))
			return to_return;
		for (int i = 0; to_return.length() >= i + to_change.length(); i++) {
			if (to_return.substring(i, i + to_change.length()).equalsIgnoreCase(to_change)
					&& (unless == null || (true_means_before && (i < unless.length() || !to_return.substring(i - unless.length(), i).equals(unless))) || (!true_means_before && (to_return
							.length() < i + to_change.length() + unless.length() || !to_return.substring(i + to_change.length(),
							i + to_change.length() + unless.length()).equals(unless))))) {
				to_return = to_return.substring(0, i) + to_change_to + to_return.substring(i + to_change.length());
				if (!replace_all)
					break;
				else
					i--;
			}
			if (!to_return.contains(to_change))
				break;
		}
		return to_return;
	}

	// listeners
	@EventHandler(priority = EventPriority.HIGHEST)
	public void formatMessage(PlayerChatEvent event) {
		// cancel messages accidentally typed while walking
		if (event.getMessage().equals("w") || event.getMessage().equals("t"))
			event.setCancelled(true);
		// if the player hasn't accepted the rules, cancel the event
		if (!players_who_have_accepted_the_rules.contains(event.getPlayer().getName()) && !event.getPlayer().isOp()) {
			if (event.getMessage().toLowerCase().contains("shut up"))
				event.getPlayer().sendMessage(colorCode("&4&oNo, &lyou %lshut up, b&kitch%k!"));
			else
				event.getPlayer().sendMessage(ChatColor.RED + "Read and accept the rules first.");
			event.setCancelled(true);
		}
		// if it's the continuation of a command...
		if (command_beginnings.containsKey(event.getPlayer())) {
			event.setCancelled(true);
			String command_beginning = command_beginnings.get(event.getPlayer());
			if (command_beginning == null)
				command_beginning = "";
			if (event.getMessage().endsWith("[...]"))
				command_beginnings.put(event.getPlayer(), command_beginning + event.getMessage().substring(0, event.getMessage().length() - 5));
			else
				event.getPlayer().performCommand(command_beginning + event.getMessage());
		}
		// if it is a chat
		if (!event.isCancelled()) {
			event.setCancelled(true);
			if (!event.getMessage().endsWith("[...]")) {
				String epithet = epithets_by_user.get(event.getPlayer().getName());
				// if the user has no epithet, use the default
				if (epithet == null)
					epithet = replace(default_epithet, "[player]", event.getPlayer().getName(), null, true, true);
				// fit the epithet and message into the message format
				String formatted_message = default_message_format;
				String full_chat_message = event.getMessage();
				if (message_beginnings.get(event.getPlayer()) != null) {
					full_chat_message = message_beginnings.get(event.getPlayer()) + event.getMessage();
					message_beginnings.remove(event.getPlayer());
				}
				formatted_message = replace(replace(formatted_message, "[epithet]", epithet, null, true, false), "[message]", full_chat_message, null, true,
						false);
				formatted_message = replace(formatted_message, full_chat_message, AutoCorrect(full_chat_message), null, true, false);
				server.broadcastMessage(colorCode(formatted_message));
			} else {
				String message_beginning = message_beginnings.get(event.getPlayer());
				if (message_beginning == null)
					message_beginning = "";
				message_beginnings.put(event.getPlayer(), message_beginning + event.getMessage().substring(0, event.getMessage().length() - 5));
				event.getPlayer().sendMessage(ChatColor.BLUE + "You may continue typing.");
			}
		}
	}

	public void commandProcessing(PlayerCommandPreprocessEvent event) {
		if (!players_who_have_accepted_the_rules.contains(event.getPlayer().getName()) && !event.getPlayer().isOp()
				&& !event.getMessage().toLowerCase().startsWith("rules") && !event.getMessage().toLowerCase().startsWith("accept")) {
			event.getPlayer().sendMessage(ChatColor.RED + "You have to read and accept the rules first.");
			event.setCancelled(true);
		} else if (event.getMessage().endsWith("[...]")) {
			event.setCancelled(true);
			String command_beginning = command_beginnings.get(event.getPlayer());
			if (command_beginning == null)
				command_beginning = "";
			command_beginnings.put(event.getPlayer(), command_beginning + event.getMessage().substring(0, event.getMessage().length() - 5));
			event.getPlayer().sendMessage(ChatColor.BLUE + "You may continue typing.");
		} else {
			String command = event.getMessage();
			if (command_beginnings.get(event.getPlayer()) != null)
				command = command_beginnings.get(event.getPlayer()) + command;
			event.setMessage(command);
		}
		if (AFK_players.contains(event.getPlayer().getName()) && !replace(event.getMessage(), " ", "", null, true, true).equalsIgnoreCase("afk")) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		if (login_messages.size() > 0) {
			int random = (int) (Math.random() * login_messages.size()) + 1;
			event.setJoinMessage(login_messages.get(random));
		}
		if (!event.getPlayer().isOp() && players_must_accept_rules && !players_who_have_accepted_the_rules.contains(event.getPlayer().getName()))
			if (!rules.equals(""))
				if (event.getPlayer().hasPlayedBefore())
					event.getPlayer().sendMessage(
							ChatColor.RED + "You still haven't accepted our rules! Read the rules with " + ChatColor.BLUE + "/rules" + ChatColor.RED
									+ ". Know them, love them, embrace them, then accept them with " + ChatColor.BLUE + "/accept" + ChatColor.RED
									+ ".\nYou can't build, use any commands, or even speak until you do!");
				else
					event.getPlayer().sendMessage(
							ChatColor.RED + "Welcome to " + server.getName() + "! Read the rules with " + ChatColor.BLUE + "/rules" + ChatColor.RED
									+ ". Know them, love them, embrace them, then accept them with " + ChatColor.BLUE + "/accept" + ChatColor.RED
									+ ".\nYou can't build, use any commands, or even speak until you do!");
			else if (!event.getPlayer().isOp())
				event.getPlayer()
						.sendMessage(
								ChatColor.YELLOW
										+ "I should be telling you to read and accept the rules right now, but your server admin hasn't even written them down yet, so you're off the hook for now.\nOnce the rules are written down, you will have to read and accept them.");
			else
				event.getPlayer().sendMessage(
						ChatColor.RED + "Your rules aren't written down! Someone needs to write your server's rules down in the rules.txt file right now!");
		else
			AFKList(event.getPlayer(), true);
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnMove(PlayerMoveEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnChat(PlayerChatEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnBedEnter(PlayerBedEnterEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnBedLeave(PlayerBedLeaveEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnBucketFill(PlayerBucketFillEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnBucketEmpty(PlayerBucketEmptyEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnDropItem(PlayerDropItemEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnEggThrow(PlayerEggThrowEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnFish(PlayerFishEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnInteract(PlayerInteractEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnRespawn(PlayerRespawnEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnShear(PlayerShearEntityEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void cancelAFKOnSneak(PlayerToggleSneakEvent event) {
		if (AFK_players.contains(event.getPlayer().getName())) {
			AFK_players.remove(event.getPlayer().getName());
			server.broadcastMessage(ChatColor.BLUE + event.getPlayer().getName() + " is back.");
		}
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		if (logout_messages.size() > 0) {
			int random = (int) (Math.random() * logout_messages.size()) + 1;
			event.setQuitMessage(logout_messages.get(random));
		}
		if (AFK_players.contains(event.getPlayer().getName()))
			AFK_players.remove(event.getPlayer().getName());
		if (message_beginnings.containsKey(event.getPlayer().getName()))
			message_beginnings.remove(event.getPlayer().getName());
		if (command_beginnings.containsKey(event.getPlayer().getName()))
			command_beginnings.remove(event.getPlayer().getName());
	}

	// loading
	private void loadTheAutoCorrectSettings(CommandSender sender) {
		boolean failed = false;
		strings_to_correct = new ArrayList<String>();
		corrected_strings = new ArrayList<String>();
		unless_strings = new ArrayList<String>();
		true_means_before = new ArrayList<Boolean>();
		// check the AutoCorrections file
		File corrections_file = new File(this.getDataFolder(), "AutoCorrections.txt");
		if (!corrections_file.exists()) {
			this.getDataFolder().mkdir();
			try {
				sender.sendMessage(ChatColor.YELLOW + "I couldn't find an AutoCorrections.txt file. I'll make a new one.");
				corrections_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED + "I couldn't create an AutoCorrections.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// read the AutoCorrections.txt file
		try {
			BufferedReader in = new BufferedReader(new FileReader(corrections_file));
			String save_line = in.readLine();
			while (save_line != null) {
				boolean already_progressed = false;
				if (save_line.equals("Would you like to use AutoCorrections in your server's chat? No."))
					AutoCorrect_on = false;
				else if (save_line.equals("Would you like to use AutoCorrections in your server's chat? ")
						|| save_line.equals("Would you like to use AutoCorrections in your server's chat? Yes."))
					AutoCorrect_on = true;
				else if (save_line.equals("Would you like me to capitalize the first letter of every sentence? Yes.")
						|| save_line.equals("Would you like me to capitalize the first letter of every sentence? "))
					capitalize_first_letter = true;
				else if (save_line.equals("Would you like me to capitalize the first letter of every sentence? No."))
					capitalize_first_letter = false;
				else if (save_line.equals("Would you like me to put periods at the end of any sentences that have no terminal punctuation? Yes.")
						|| save_line.equals("Would you like me to put periods at the end of any sentences that have no terminal punctuation? "))
					end_with_period = true;
				else if (save_line.equals("Would you like me to put periods at the end of any sentences that have no terminal punctuation? No."))
					end_with_period = false;
				else if (save_line.equals("Would you like me to change any all-caps words or sentences to lowercase italics? ")
						|| save_line.equals("Would you like me to change any all-caps words or sentences to lowercase italics? Yes."))
					change_all_caps_to_italics = true;
				else if (save_line.equals("Would you like me to change any all-caps words or sentences to lowercase italics? No."))
					change_all_caps_to_italics = false;
				else {
					int first_letter_index = 0;
					for (int j = 0; j < save_line.length(); j++) {
						if (save_line.substring(j, j + 1).equals(" "))
							first_letter_index++;
						else
							j = save_line.length();
					}
					if (save_line.length() > 8 + first_letter_index
							&& save_line.substring(first_letter_index, 8 + first_letter_index).equalsIgnoreCase("change: ")) {
						strings_to_correct.add(save_line.substring(8 + first_letter_index));
						save_line = in.readLine();
						already_progressed = true;
						if (save_line != null && save_line.length() >= 4 + first_letter_index
								&& save_line.substring(first_letter_index, 4 + first_letter_index).equals("to: ")) {
							if (save_line.length() > 4 + first_letter_index)
								corrected_strings.add(save_line.substring(4 + first_letter_index));
							else
								corrected_strings.add("");
							save_line = in.readLine();
							if (save_line != null && save_line.length() > 8 + first_letter_index
									&& save_line.substring(first_letter_index, 8 + first_letter_index).equalsIgnoreCase("unless: ")) {
								unless_strings.add(save_line.substring(8 + first_letter_index));
								save_line = in.readLine();
								if (save_line != null && save_line.length() >= 13 + first_letter_index
										&& save_line.substring(first_letter_index, 13 + first_letter_index).equalsIgnoreCase("comes: before")) {
									true_means_before.add(true);
									save_line = in.readLine();
								} else if (save_line != null && save_line.length() >= 12 + first_letter_index
										&& save_line.substring(first_letter_index, 12 + first_letter_index).equalsIgnoreCase("comes: after")) {
									true_means_before.add(false);
									save_line = in.readLine();
								} else
									unless_strings.set(unless_strings.size() - 1, null);
							} else {
								unless_strings.add(null);
								true_means_before.add(true);
							}
						} else
							strings_to_correct.set(strings_to_correct.size() - 1, null);
					}
				}
				if (!already_progressed)
					save_line = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "The AutoCorrections.txt I created a few milliseconds ago doesn't exist. -_-");
			exception.printStackTrace();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your AutoCorrections.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed) {
			saveTheAutoCorrectSettings(sender, false);
			if (strings_to_correct.size() == 0)
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings have been loaded.");
			else if (strings_to_correct.size() == 1)
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings and your server's only AutoCorrection have been loaded.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings and your server's " + strings_to_correct.size()
						+ " AutoCorrections have been loaded.");
		}
	}

	private void loadTheDeathMessages(CommandSender sender) {
		// TODO Auto-generated method stub
	}

	private void loadTheEpithets(CommandSender sender) {
		boolean failed = false;
		epithets_by_user = new HashMap<String, String>();
		// check the epithets file
		File epithets_file = new File(this.getDataFolder(), "epithets.txt");
		if (!epithets_file.exists()) {
			this.getDataFolder().mkdir();
			try {
				sender.sendMessage(ChatColor.YELLOW + "I couldn't find an epithets.txt file. I'll make a new one.");
				epithets_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED + "I couldn't create an epithets.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// read the epithets.txt file
		try {
			BufferedReader in = new BufferedReader(new FileReader(epithets_file));
			boolean parse_individual_users = false;
			String save_line = in.readLine();
			while (save_line != null) {
				// eliminate preceding spaces
				while (save_line.length() > 0 && save_line.substring(0, 1).equals(" "))
					save_line = save_line.substring(1);
				if (save_line.toLowerCase().startsWith("default epithet: "))
					default_epithet = save_line.substring(17);
				else if (save_line.toLowerCase().startsWith("default message format: "))
					default_message_format = save_line.substring(24);
				else {
					if (save_line.toLowerCase().startsWith("individual epithets:")) {
						parse_individual_users = true;
						save_line = save_line.substring(20);
					}
				}
				if (parse_individual_users) {
					for (int i = 0; i < save_line.length() - 2; i++)
						if (save_line.substring(i, i + 2).equals(": ")) {
							String epithet = save_line.substring(i + 2);
							// eliminate preceding spaces
							while (epithet.length() > 0 && epithet.substring(0, 1).equals(" "))
								epithet = epithet.substring(1);
							// make sure the epithet isn't all spaces
							if (epithet.length() > 0)
								epithets_by_user.put(save_line.substring(0, i), epithet);
						}
				}
				save_line = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "The epithets.txt I created a few milliseconds ago doesn't exist. -_-");
			exception.printStackTrace();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your epithets.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed) {
			saveTheEpithets(sender, false);
			if (epithets_by_user.size() == 0)
				sender.sendMessage(ChatColor.BLUE + "Your epithet settings have been loaded.");
			else if (epithets_by_user.size() == 1)
				sender.sendMessage(ChatColor.BLUE + "Your epithet settings and your server's only epithet have been loaded.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your epithet settings and your server's " + epithets_by_user.size() + " epithets have been loaded.");
		}
	}

	private void loadTheLoginMessages(CommandSender sender) {
		// TODO Auto-generated method stub

	}

	private void loadTheLogoutMessages(CommandSender sender) {
		// TODO Auto-generated method stub

	}

	private void loadTheRules(CommandSender sender) {
		boolean failed = false;
		rules = "";
		players_who_have_accepted_the_rules = new ArrayList<String>();
		// check the rules file
		File rules_file = new File(this.getDataFolder(), "rules.txt");
		if (!rules_file.exists()) {
			this.getDataFolder().mkdir();
			try {
				sender.sendMessage(ChatColor.YELLOW + "I couldn't find an rules.txt file. I'll make a new one.");
				rules_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED + "I couldn't create an rules.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// read the rules.txt file
		players_who_have_accepted_the_rules = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(rules_file));
			String save_line = in.readLine();
			rules = "";
			while (save_line != null) {
				boolean is_border = false;
				if (save_line.length() == 40) {
					String temp = save_line;
					for (String border : borders)
						if (temp.contains(border)) {
							temp = replace(temp, border, "", null, true, true);
							break;
						}
					if (temp.equals(""))
						is_border = true;
				}
				if (is_border) {
					save_line = in.readLine();
					if (save_line.contains(", ")) {
						String[] players_listed = save_line.split(", ");
						// elimiate the "and" and sentence ending around the
						// last username
						players_listed[players_listed.length - 1] = players_listed[players_listed.length - 1].substring(4,
								players_listed[players_listed.length - 1].length() - 29);
						// convert the array to an ArrayList
						for (String listed_player : players_listed)
							players_who_have_accepted_the_rules.add(listed_player);
					} else if (save_line.contains(" and ")) {
						String[] players_listed = save_line.split(" and ");
						players_listed[1] = players_listed[1].substring(0, players_listed[1].length() - 25);
						players_who_have_accepted_the_rules.add(players_listed[0]);
						players_who_have_accepted_the_rules.add(players_listed[1]);
					} else if (!save_line.startsWith("No one")) {
						String[] words = save_line.split(" ");
						players_who_have_accepted_the_rules.add(words[0]);
					}
				} else if (rules.equals(""))
					rules = save_line;
				else
					rules = rules + "\n" + save_line;
				save_line = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "The rules.txt I created a few milliseconds ago doesn't exist. -_-");
			exception.printStackTrace();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your rules.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed) {
			saveTheRules(sender, false);
			if (!rules.equals(""))
				sender.sendMessage(ChatColor.BLUE + "Your rules have been loaded.");
			else
				sender.sendMessage(ChatColor.RED + "You haven't written down your rules! You need to write down your rules in the rules.txt right now!");
		}
	}

	// saving
	private void saveTheAutoCorrectSettings(CommandSender sender, boolean display_message) {
		boolean failed = false;
		// check the AutoCorrections file
		File corrections_file = new File(this.getDataFolder(), "AutoCorrections.txt");
		if (!corrections_file.exists()) {
			this.getDataFolder().mkdir();
			try {
				corrections_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED + "I couldn't create an AutoCorrections.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// save the AutoCorrections
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(corrections_file));
			out.write("Would you like to use AutoCorrections in your server's chat? ");
			out.newLine();
			if (AutoCorrect_on)
				out.write("Right now, AutoCorrections are enabled.");
			else
				out.write("Right now, AutoCorrections are disabled.");
			out.newLine();
			out.newLine();
			out.write("Would you like me to capitalize the first letter of every sentence? ");
			out.newLine();
			if (capitalize_first_letter)
				out.write("Right now, first letter capitalization is enabled.");
			else
				out.write("Right now, first letter capitalization is disabled.");
			out.newLine();
			out.newLine();
			out.write("Would you like me to put periods at the end of any sentences that have no terminal punctuation? ");
			out.newLine();
			if (end_with_period)
				out.write("Right now, period addition is enabled.");
			else
				out.write("Right now, period addition is diabled.");
			out.newLine();
			out.newLine();
			out.write("Would you like me to change any all-caps words or sentences to lowercase italics? ");
			out.newLine();
			if (change_all_caps_to_italics)
				out.write("Right now, caps to italics conversion is enabled.");
			else
				out.write("Right now, caps to italics conversion is diabled.");
			out.newLine();
			out.newLine();
			out.write("universal AutoCorrections:");
			out.newLine();
			for (int i = 0; i < strings_to_correct.size(); i++) {
				if (strings_to_correct.get(i) != null && !strings_to_correct.get(i).equals("") && corrected_strings.get(i) != null) {
					out.write("  change: " + strings_to_correct.get(i));
					out.newLine();
					out.write("  to: " + corrected_strings.get(i));
					out.newLine();
					if (unless_strings.get(i) != null) {
						out.write("  unless: " + unless_strings.get(i));
						out.newLine();
						if (true_means_before.get(i))
							out.write("  comes: before");
						else
							out.write("  comes: after");
						out.newLine();
					}
					out.newLine();
				}
			}
			out.flush();
			out.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your AutoCorrections.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed && display_message)
			if (strings_to_correct.size() == 0)
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings have been saved.");
			else if (strings_to_correct.size() == 1)
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings and your server's only AutoCorrection have been saved.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings and your server's " + strings_to_correct.size()
						+ " AutoCorrections have been saved.");
	}

	private void saveTheDeathMessages(CommandSender sender, boolean display_message) {
		boolean failed = false;
		// check the death messages file
		File death_messages_file = new File(this.getDataFolder(), "death messages.txt");
		if (!death_messages_file.exists()) {
			this.getDataFolder().mkdir();
			try {
				death_messages_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED + "I couldn't create an AutoCorrections.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// save the AutoCorrections
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(death_messages_file));
			out.write("Do you want death messages to appear when people die? ");
			out.newLine();
			if (display_death_messages)
				out.write("Right now, death messages will appear when someone dies.");
			else
				out.write("Right now, death messages will not appear when someone dies.");
			out.newLine();
			out.newLine();
			out.write("Below is the list of death messages. You can customize them however you like and use as many colors as you want. Put \"[player]\" to show where the name of the player who dies should appear in the message.");
			out.newLine();
			for (int i = 0; i < death_messages_by_cause.size(); i++) {
				String cause = (String) death_messages_by_cause.keySet().toArray()[i];
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
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your AutoCorrections.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed && display_message)
			if (strings_to_correct.size() == 0)
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings have been saved.");
			else if (strings_to_correct.size() == 1)
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings and your server's only AutoCorrection have been saved.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your AutoCorrect settings and your server's " + strings_to_correct.size()
						+ " AutoCorrections have been saved.");
	}

	private void saveTheEpithets(CommandSender sender, boolean display_message) {
		boolean failed = false;
		// check the epithets file
		File epithets_file = new File(this.getDataFolder(), "epithets.txt");
		if (!epithets_file.exists()) {
			this.getDataFolder().mkdir();
			try {
				epithets_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED + "I couldn't create a epithets.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// save the epithets
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(epithets_file));
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
			out.newLine();
			out.write("individual epithets:");
			out.newLine();
			for (int i = 0; i < epithets_by_user.size(); i++) {
				String user = (String) epithets_by_user.keySet().toArray()[i];
				out.write("     " + user + ": " + epithets_by_user.get(user));
				out.newLine();
			}
			out.flush();
			out.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your epithets.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed && display_message)
			if (epithets_by_user.size() == 0)
				sender.sendMessage(ChatColor.BLUE + "Your epithet settings have been saved.");
			else if (epithets_by_user.size() == 1)
				sender.sendMessage(ChatColor.BLUE + "Your epithet settings and your server's only epithet have been saved.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your epithet settings and your server's " + epithets_by_user.size() + " epithets have been saved.");
	}

	private void saveTheLoginMessages(CommandSender sender, boolean display_message) {
		// TODO Auto-generated method stub

	}

	private void saveTheLogoutMessages(CommandSender sender, boolean display_message) {
		// TODO Auto-generated method stub

	}

	private void saveTheRules(CommandSender sender, boolean display_message) {
		boolean failed = false;
		// check the rules file
		File rules_file = new File(this.getDataFolder(), "rules.txt");
		if (!rules_file.exists()) {
			this.getDataFolder().mkdir();
			try {
				rules_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED + "I couldn't create a rules.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// save the rules
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(rules_file));
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
				out.write("and " + players_who_have_accepted_the_rules.get(players_who_have_accepted_the_rules.size() - 1) + " have all accepted the rules.");
			} else if (players_who_have_accepted_the_rules.size() == 2)
				out.write(players_who_have_accepted_the_rules.get(0) + " and " + players_who_have_accepted_the_rules.get(1) + " have accepted the rules.");
			else if (players_who_have_accepted_the_rules.size() == 1)
				out.write(players_who_have_accepted_the_rules.get(0) + " is the only one who has accepted the rules.");
			else
				out.write("No one has accepted the rules yet!");
			out.flush();
			out.close();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your rules.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed && display_message)
			if (!rules.equals(""))
				sender.sendMessage(ChatColor.BLUE + "Your rules have been saved.");
			else
				sender.sendMessage(ChatColor.RED + "Go write down your rules in the rules.txt!");
	}

	// command methods
	private void addCorrection(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "Coming soon to a server near you!");
	}

	private void AFKCheck(CommandSender sender) {
		String target_player = null;
		for (Player player : server.getOnlinePlayers())
			if (player.getName().toLowerCase().startsWith(parameters[0].toLowerCase()))
				target_player = player.getName();
		if (target_player == null)
			sender.sendMessage(ChatColor.RED + "I couldn't find \"" + parameters[0] + "\" anywhere.");
		else if (AFK_players.contains(target_player))
			sender.sendMessage(ChatColor.BLUE + target_player + " is a.f.k. right now.");
		else
			sender.sendMessage(ChatColor.BLUE + target_player + " is not a.f.k right now.");
	}

	private void AFKList(CommandSender sender, boolean on_login) {
		if (AFK_players.size() == 0) {
			if (!on_login)
				sender.sendMessage(ChatColor.BLUE + "No one is a.f.k. right now!");
		} else if (AFK_players.size() == 1)
			sender.sendMessage(ChatColor.BLUE + AFK_players.get(0) + " is the only one a.f.k. right now.");
		else if (AFK_players.size() == 2)
			sender.sendMessage(ChatColor.BLUE + AFK_players.get(0) + " and " + AFK_players.get(1) + " are the only ones a.f.k. right now.");
		else {
			String list = ChatColor.BLUE + "";
			for (int i = 0; i < AFK_players.size() - 1; i++)
				list = list + AFK_players.get(i) + ", ";
			sender.sendMessage(list + " and " + AFK_players.get(AFK_players.size() - 1) + " are all a.f.k.");
		}
	}

	private void AFKToggle(CommandSender sender) {
		Player player = (Player) sender;
		if (!AFK_players.contains(player.getName())) {
			AFK_players.add(player.getName());
			server.broadcastMessage(ChatColor.GRAY + player.getName() + " is now a.f.k.");
		} else {
			AFK_players.remove(player.getName());
			server.broadcastMessage(ChatColor.BLUE + player.getName() + " is back.");
		}
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
			if (true_username_required && player != null && !player.hasPermission("mychat.admin")) {
				epithet_is_acceptable = epithet.length() > 0 && !epithet.contains("&k")
						&& epithet.replaceAll("(&+([a-fA-Fk-oK-OrR0-9]))", "").contains(player.getName());
			}
			if ((epithet_is_acceptable && player.getName().equals(owner)) || player.hasPermission("mychat.admin")) {
				epithets_by_user.put(owner, epithet);
				if (player != null && player.getName().equalsIgnoreCase(owner))
					player.sendMessage(ChatColor.BLUE + "Henceforth, you shall be known as \"" + colorCode(epithet) + ChatColor.BLUE + ".\"");
				else
					sender.sendMessage(ChatColor.BLUE + owner + " shall henceforth be known as \"" + colorCode(epithet) + ChatColor.BLUE + ".\"");
			} else
				sender.sendMessage(ChatColor.RED + "Your epithet must contain your true username and not use magic.");
		} else if (owner.equalsIgnoreCase("server") && !epithet.equals("")) {
			say_epithet = epithet;
			sender.sendMessage(ChatColor.BLUE + "You set the \"/say\" epithet to \"" + colorCode(epithet) + ChatColor.BLUE + ".\"");
		} else if (epithet.equals(""))
			sender.sendMessage(ChatColor.RED + "You forgot to tell me the epithet you want!");
	}

	private void enchantItem(CommandSender sender) {
		Player player = (Player) sender;
		// check to make sure the item is enchantable at all and get the item
		// name
		String item_name = null;
		int id = player.getItemInHand().getTypeId();
		if (id == 302)
			item_name = "chainmail helmet";
		else if (id == 303)
			item_name = "chainmail chestplate";
		else if (id == 304)
			item_name = "chainmail leggings";
		else if (id == 305)
			item_name = "chainmail boots";
		else if (id == 306)
			item_name = "iron helmet";
		else if (id == 307)
			item_name = "iron chestplate";
		else if (id == 308)
			item_name = "iron leggings";
		else if (id == 309)
			item_name = "iron boots";
		else if (id == 310)
			item_name = "diamond helmet";
		else if (id == 311)
			item_name = "diamond chestplate";
		else if (id == 312)
			item_name = "diamond leggings";
		else if (id == 313)
			item_name = "diamond boots";
		else if (id == 314)
			item_name = "gold helmet";
		else if (id == 315)
			item_name = "gold chestplate";
		else if (id == 316)
			item_name = "gold leggings";
		else if (id == 317)
			item_name = "gold boots";
		else if (id == 267)
			item_name = "iron sword";
		else if (id == 268)
			item_name = "wooden sword";
		else if (id == 272)
			item_name = "stone sword";
		else if (id == 276)
			item_name = "diamond sword";
		else if (id == 283)
			item_name = "golden sword";
		else if (id == 257)
			item_name = "iron pickaxe";
		else if (id == 270)
			item_name = "wooden pickaxe";
		else if (id == 274)
			item_name = "stone pickaxe";
		else if (id == 278)
			item_name = "diamond pickaxe";
		else if (id == 285)
			item_name = "golden pickaxe";
		else if (id == 256)
			item_name = "iron shovel";
		else if (id == 269)
			item_name = "wooden shovel";
		else if (id == 273)
			item_name = "stone shovel";
		else if (id == 277)
			item_name = "diamond shovel";
		else if (id == 284)
			item_name = "golden shovel";
		else if (id == 258)
			item_name = "iron axe";
		else if (id == 271)
			item_name = "wooden axe";
		else if (id == 275)
			item_name = "stone axe";
		else if (id == 279)
			item_name = "diamond axe";
		else if (id == 286)
			item_name = "golden axe";
		else if (id == 261)
			item_name = "bow";
		else {
			player.sendMessage(ChatColor.RED + "Sorry, but that thing isn't even enchantable.");
			return;
		}
		// read the enchantments to add
		String temp = "";
		for (String parameter : parameters)
			temp = temp + parameter;
		String[] enchantments_names = temp.split(",");
		int[] enchantments_levels = new int[enchantments_names.length];
		// get the level of each enchantment
		for (int i = 0; i < enchantments_names.length; i++)
			if (enchantments_names[i].toLowerCase().endsWith("iv")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 2);
				enchantments_levels[i] = 4;
			} else if (enchantments_names[i].toLowerCase().endsWith("v")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 1);
				enchantments_levels[i] = 5;
			} else if (enchantments_names[i].toLowerCase().endsWith("iii")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 3);
				enchantments_levels[i] = 3;
			} else if (enchantments_names[i].toLowerCase().endsWith("ii")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 2);
				enchantments_levels[i] = 2;
			} else if (enchantments_names[i].toLowerCase().endsWith("i")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 1);
				enchantments_levels[i] = 1;
			} else if (enchantments_names[i].toLowerCase().endsWith("5")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 1);
				enchantments_levels[i] = 5;
			} else if (enchantments_names[i].toLowerCase().endsWith("4")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 1);
				enchantments_levels[i] = 4;
			} else if (enchantments_names[i].toLowerCase().endsWith("3")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 1);
				enchantments_levels[i] = 3;
			} else if (enchantments_names[i].toLowerCase().endsWith("2")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 1);
				enchantments_levels[i] = 2;
			} else if (enchantments_names[i].toLowerCase().endsWith("1")) {
				enchantments_names[i] = enchantments_names[i].substring(0, enchantments_names[i].length() - 1);
				enchantments_levels[i] = 1;
			} else
				enchantments_levels[i] = 1;
		// get the enchantments themselves
		Enchantment[] enchantments = new Enchantment[enchantments_names.length];
		for (int i = 0; i < enchantments_names.length; i++)
			for (Object enchantment : enchantment_names.keySet().toArray())
				if (enchantment_names.get(enchantment).toLowerCase().startsWith(enchantments_names[i].toLowerCase())
						&& ((Enchantment) enchantment).canEnchantItem(player.getItemInHand()))
					enchantments[i] = ((Enchantment) enchantment);
		for (int i = 0; i < enchantments_names.length; i++)
			if (enchantments[i] == null)
				for (Object enchantment : enchantment_names.keySet().toArray())
					if (enchantment_names.get(enchantment).toLowerCase().startsWith(enchantments_names[i].toLowerCase()))
						enchantments[i] = ((Enchantment) enchantment);
		// try to enchant the item
		ArrayList<Enchantment> good_enchantments = new ArrayList<Enchantment>(), bad_enchantments = new ArrayList<Enchantment>();
		ArrayList<String> absent_enchantments = new ArrayList<String>(), good_enchantments_levels = new ArrayList<String>();
		for (int i = 0; i < enchantments.length; i++) {
			if (enchantments[i] != null && enchantments[i].canEnchantItem(player.getItemInHand())) {
				player.getItemInHand().addEnchantment(enchantments[i], enchantments_levels[i]);
				good_enchantments.add(enchantments[i]);
				if (enchantments[i].equals(Enchantment.SILK_TOUCH) || enchantments[i].equals(Enchantment.ARROW_INFINITE)
						|| enchantments[i].equals(Enchantment.WATER_WORKER) || enchantments[i].equals(Enchantment.ARROW_FIRE))
					good_enchantments_levels.add("");
				else if (enchantments_levels[i] == 1)
					good_enchantments_levels.add(" I");
				else if (enchantments_levels[i] == 2)
					good_enchantments_levels.add(" II");
				else if (enchantments_levels[i] == 3)
					good_enchantments_levels.add(" III");
				else if (enchantments_levels[i] == 4)
					good_enchantments_levels.add(" IV");
				else if (enchantments_levels[i] == 5)
					good_enchantments_levels.add(" V");
			} else if (enchantments[i] != null)
				bad_enchantments.add(enchantments[i]);
			else
				absent_enchantments.add(enchantments_names[i]);
		}
		// display results
		if (good_enchantments.size() == 1)
			player.sendMessage(ChatColor.BLUE + magic_words[(int) (Math.random() * magic_words.length)] + " Your " + item_name + " is now enchanted with "
					+ ChatColor.GRAY + enchantment_names.get(good_enchantments.get(0)) + good_enchantments_levels.get(0) + ChatColor.BLUE + ".");
		else if (good_enchantments.size() == 2)
			player.sendMessage(ChatColor.BLUE + magic_words[(int) (Math.random() * magic_words.length)] + " Your " + item_name + " is now enchanted with "
					+ ChatColor.GRAY + enchantment_names.get(good_enchantments.get(0)) + good_enchantments_levels.get(0) + ChatColor.BLUE + " and "
					+ ChatColor.GRAY + enchantment_names.get(good_enchantments.get(1)) + good_enchantments_levels.get(1) + ChatColor.BLUE + ".");
		else if (good_enchantments.size() > 2) {
			String message_beginning = ChatColor.BLUE + magic_words[(int) (Math.random() * magic_words.length)] + " Your " + item_name
					+ " is now enchanted with ";
			for (int i = 0; i < good_enchantments.size() - 1; i++)
				message_beginning = message_beginning + ChatColor.GRAY + enchantment_names.get(good_enchantments.get(i)) + good_enchantments_levels.get(i)
						+ ChatColor.BLUE + ", ";
			player.sendMessage(message_beginning + " and " + ChatColor.GRAY + enchantment_names.get(good_enchantments.get(good_enchantments.size() - 1))
					+ good_enchantments_levels.get(good_enchantments_levels.size() - 1) + ChatColor.BLUE + ".");
		}
		if (bad_enchantments.size() > 0) {
			String message_beginning = ChatColor.RED + "";
			if (good_enchantments.size() > 0)
				message_beginning = ChatColor.RED + "However, ";
			if (bad_enchantments.size() == 1)
				player.sendMessage(message_beginning + ChatColor.GRAY + enchantment_names.get(bad_enchantments.get(0)) + ChatColor.RED + " doesn't work on "
						+ item_name + "s. Sorry.");
			else if (bad_enchantments.size() == 2)
				player.sendMessage(message_beginning + ChatColor.GRAY + enchantment_names.get(bad_enchantments.get(0)) + ChatColor.RED + "and" + ChatColor.GRAY
						+ enchantment_names.get(bad_enchantments.get(1)) + ChatColor.RED + " don't work on " + item_name + "s. Sorry.");
			else if (bad_enchantments.size() > 2) {
				for (int i = 0; i < bad_enchantments.size() - 1; i++)
					message_beginning = message_beginning + ChatColor.GRAY + enchantment_names.get(bad_enchantments.get(i)) + ChatColor.RED + ", ";
				player.sendMessage(message_beginning + " and " + ChatColor.GRAY + enchantment_names.get(bad_enchantments.get(bad_enchantments.size() - 1))
						+ ChatColor.RED + " don't work on " + item_name + "s. Sorry.");
			}
		}
		if (absent_enchantments.size() > 0) {
			String message_beginning = ChatColor.RED + "I can't find any enchantments at all that start with \"";
			if (bad_enchantments.size() > 0)
				message_beginning = ChatColor.RED + "Also, I can't find any enchantments at all that start with \"";
			else if (good_enchantments.size() > 0)
				message_beginning = ChatColor.RED + "However, I can't find any enchantments at all that start with \"";
			if (absent_enchantments.size() == 1)
				player.sendMessage(message_beginning + absent_enchantments.get(0) + ".\"");
			else if (absent_enchantments.size() == 2)
				player.sendMessage(message_beginning + absent_enchantments.get(0) + "\" or \"" + absent_enchantments.get(1) + ".\"");
			else if (absent_enchantments.size() > 2) {
				for (int i = 0; i < absent_enchantments.size() - 1; i++)
					message_beginning = message_beginning + absent_enchantments.get(i) + "\", \"";
				player.sendMessage(message_beginning + " or \"" + absent_enchantments.get(absent_enchantments.size() - 1) + ".\"");
			}
		}
	}

	private void getRecipe(CommandSender sender) {
		// assemble the query
		String query = "";
		for (String parameter : parameters)
			query = query + parameter.toLowerCase() + " ";
		query = query.substring(0, query.length() - 1);
		// if the query is an I.D., get the item name
		int id = -1;
		try {
			id = Integer.parseInt(query);
			query = item_IDs.get(id);
			if (query == null) {
				sender.sendMessage(ChatColor.RED + "There's no item with the I.D. " + id + ".");
				return;
			}
		} catch (NumberFormatException exception) {
		}
		for (Object temp : item_recipes.keySet().toArray()) {
			String item = (String) temp;
			if (item.toLowerCase().startsWith(query)) {
				if (item.toLowerCase().startsWith("a") || item.toLowerCase().startsWith("e") || item.toLowerCase().startsWith("i")
						|| item.toLowerCase().startsWith("o") || item.toLowerCase().startsWith("u"))
					sender.sendMessage(ChatColor.BLUE + "Here's how you craft an " + item + ":");
				else
					sender.sendMessage(ChatColor.BLUE + "Here's how you craft a " + item + ":");
				sender.sendMessage(item_recipes.get(item));
				return;
			}
		}
		for (String uncraftable_item : uncraftable_items)
			if (uncraftable_item.toLowerCase().startsWith(query)) {
				if (uncraftable_item.toLowerCase().startsWith("a") || uncraftable_item.toLowerCase().startsWith("e")
						|| uncraftable_item.toLowerCase().startsWith("i") || uncraftable_item.toLowerCase().startsWith("o")
						|| uncraftable_item.toLowerCase().startsWith("u"))
					sender.sendMessage(ChatColor.RED + "An " + uncraftable_item + " isn't craftable!");
				else
					sender.sendMessage(ChatColor.RED + "A " + uncraftable_item + " isn't craftable!");
				return;
			}
		if (query.toLowerCase().startsWith("a") || query.toLowerCase().startsWith("e") || query.toLowerCase().startsWith("i")
				|| query.toLowerCase().startsWith("o") || query.toLowerCase().startsWith("u"))
			sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what an \"" + query + "\" is.");
		else
			sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what a \"" + query + "\" is.");
	}
}
