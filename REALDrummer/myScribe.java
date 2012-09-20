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
import org.bukkit.command.PluginCommand;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class myScribe extends JavaPlugin implements Listener {
	private static Server server;
	private static ConsoleCommandSender console;
	private String monetary_symbol = "";
	private String[] parameters;
	private static final String[] profanities = { "fuck", "fck", "fuk", "Goddamn", "Goddam", "damn", "shit", "sht", "dammit", "bastard", "bitch", "btch",
			"damnit", "cunt", "asshole" }, magic_words = { "Sha-ZAM!", "ALAKAZAM!", "POOF!", "BOOM!", "KA-POW!", "Sha-FWAAAH!", "Kali-kaPOW!", "TORTELLINI!",
			"Kras-TOPHALEMOTZ!", "Wah-SHAM!", "Wa-ZAM!", "Wha-ZOO!", "KERFUFFLE!", "WOOOOWOWOWOWOW!", "CREAMPUFF WADLEEDEE!", "FLUFFENNUGGET!",
			"FALALALALAAAAAA-lala-la-LAAAA!", "SHNITZ-LIEDERHOSEN!", "BWAAAAAAAAAAAAH!", "FEE-FI-FO-FUM!", "ROTISSERIE!", "LALA-BIBIAY!", "Kurlaka-FWAH!" },
			borders = { "[]", "\\/", "\"*", "_^", "-=", ":;", "&%", "#@", ",.", "<>", "~$", ")(" }, yeses = { "yes", "yeah", "yep", "sure", "why not", "okay",
					"do it", "fine", "whatever", "very well", "accept", "tpa", "cool", "hell yeah", "hells yeah", "hells yes", "come" }, nos = { "no", "nah",
					"nope", "no thanks", "no don't", "shut up", "ignore", "it's not", "its not", "creeper", "unsafe", "wait", "one ", "1 " };
	private String[][] item_IDs = { { "air" }, {} };
	private HashMap<String, String> epithets_by_user = new HashMap<String, String>();
	private HashMap<String[], String> item_recipes = new HashMap<String[], String>();
	private HashMap<String, Double> myConomy_accounts = new HashMap<String, Double>();
	private HashMap<String, ArrayList<String>> death_messages_by_cause = new HashMap<String, ArrayList<String>>();
	private HashMap<Player, String> message_beginnings = new HashMap<Player, String>(), command_beginnings = new HashMap<Player, String>();
	private static ArrayList<String> strings_to_correct = new ArrayList<String>(), corrected_strings = new ArrayList<String>(),
			unless_strings = new ArrayList<String>(), AFK_players = new ArrayList<String>(), players_who_have_accepted_the_rules = new ArrayList<String>(),
			players_who_have_read_the_rules = new ArrayList<String>(), login_messages = new ArrayList<String>(), logout_messages = new ArrayList<String>();
	private static HashMap<Enchantment, String> enchantment_names = new HashMap<Enchantment, String>();
	private static ArrayList<Boolean> true_means_before = new ArrayList<Boolean>();
	private static String rules = "", default_epithet = "", default_message_format = "", say_format = "";
	private static boolean players_must_accept_rules = true, AutoCorrect_on = true, capitalize_first_letter = true, end_with_period = true,
			change_all_caps_to_italics = true, cover_up_profanities = true, insert_command_usages = true, true_username_required = true,
			display_death_messages = true, monetary_symbol_comes_before = false;

	// TODO: set up config questions for true_username_required and
	// cover_up_profanities and insert_command_usages

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
		loadTheAutoCorrections(console);
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
		if ((command_label.equalsIgnoreCase("myScribe") || command_label.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("e") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("e")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheEpithets(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myChat load" + ChatColor.RED + ".");
			return true;
		} else if ((command_label.equalsIgnoreCase("myScribe") || command_label.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("A") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("A")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheAutoCorrections(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myChat load" + ChatColor.RED + ".");
			return true;
		} else if ((command_label.equalsIgnoreCase("myScribe") || command_label.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("d") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("d")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheDeathMessages(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myChat load" + ChatColor.RED + ".");
			return true;
		} else if ((command_label.equalsIgnoreCase("myScribe") || command_label.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("login")
						|| (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2].toLowerCase().startsWith("login"))
						|| (parameters.length > 2 && parameters[1].equalsIgnoreCase("log") && parameters[2].equalsIgnoreCase("in")) || (parameters.length > 3
						&& parameters[1].equalsIgnoreCase("the") && parameters[2].equalsIgnoreCase("log") && parameters[3].equalsIgnoreCase("in")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheLoginMessages(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myChat load" + ChatColor.RED + ".");
			return true;
		} else if ((command_label.equalsIgnoreCase("myScribe") || command_label.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("logout")
						|| (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2].toLowerCase().startsWith("logout"))
						|| (parameters.length > 2 && parameters[1].equalsIgnoreCase("log") && parameters[2].equalsIgnoreCase("out")) || (parameters.length > 3
						&& parameters[1].equalsIgnoreCase("the") && parameters[2].equalsIgnoreCase("log") && parameters[3].equalsIgnoreCase("out")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheLogoutMessages(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myChat load" + ChatColor.RED + ".");
			return true;
		} else if ((command_label.equalsIgnoreCase("myScribe") || command_label.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("r") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("r")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheRules(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myChat load" + ChatColor.RED + ".");
			return true;
		}
		// TODO add loading logout messages and rules
		else if ((command_label.equalsIgnoreCase("myScribe") || command_label.equalsIgnoreCase("mS")) && parameters.length > 0
				&& parameters[0].equalsIgnoreCase("load")) {
			if (!(sender instanceof Player) || sender.isOp()) {
				loadTheEpithets(sender);
				loadTheAutoCorrections(sender);
				loadTheDeathMessages(sender);
				loadTheLoginMessages(sender);
				loadTheLogoutMessages(sender);
				loadTheRules(sender);
			} else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myChat load" + ChatColor.RED + ".");
			return true;
		}
		// TODO add partial saves
		else if ((command_label.equalsIgnoreCase("myScribe") || command_label.equalsIgnoreCase("mS")) && parameters.length > 0
				&& parameters[0].equalsIgnoreCase("save")) {
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
			if (!(sender instanceof Player) || sender.isOp())
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
				server.broadcastMessage(colorCode(replace(say_format, "[message]", AutoCorrect(message), null, true, false)));
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
			}
			return true;
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
			}
			return true;
		} else if (command_label.equalsIgnoreCase("recipe") || command_label.equalsIgnoreCase("craft")) {
			if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what item you want the recipe for!");
			else
				getRecipe(sender);
		} else if (command_label.equalsIgnoreCase("ids") || command_label.equalsIgnoreCase("id")) {
			if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "You forgot to give me an I.D. or an item name!");
			else {
				String query = "";
				for (String parameter : parameters)
					if (query.equals(""))
						query = parameter;
					else
						query = query + " " + parameter;
				try {
					int id = Integer.parseInt(query);
					String item = item_IDs[id][0];
					if (item != null)
						sender.sendMessage(ChatColor.BLUE + item + " has the I.D. " + id);
					else
						sender.sendMessage(ChatColor.RED + "No item has the I.D. " + id);
				} catch (NumberFormatException exception) {
					for (int id = 0; id < item_IDs.length; id++)
						for (String item_name : item_IDs[id])
							if (item_name.toLowerCase().startsWith(query.toLowerCase())) {
								sender.sendMessage(ChatColor.BLUE + item_name + " has the I.D. " + id + ".");
								return true;
							}
					if (query.toLowerCase().startsWith("a") || query.toLowerCase().startsWith("e") || query.toLowerCase().startsWith("i")
							|| query.toLowerCase().startsWith("o") || query.toLowerCase().startsWith("u"))
						sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what an \"" + query + "\" is.");
					else
						sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what a \"" + query + "\" is.");
				}
			}
			return true;
		} else if (((command_label.equalsIgnoreCase("money") || command_label.equalsIgnoreCase("cash") || command_label.equalsIgnoreCase("funds"))
				&& parameters.length > 0 && parameters[0].equalsIgnoreCase("top"))
				|| command_label.equalsIgnoreCase("richest") || command_label.equalsIgnoreCase("wealthiest")) {
			ArrayList<String> richest_people = new ArrayList<String>();
			for (Object key : myConomy_accounts.keySet().toArray()) {
				String player = (String) key;
				int insertion_index = richest_people.size() - 1;
				for (String rich_person : richest_people) {
					if (myConomy_accounts.get(player) != null && myConomy_accounts.get(player) > myConomy_accounts.get(richest_people))
						insertion_index--;
				}
				// TODO
			}
			return true;
		} else if (command_label.equalsIgnoreCase("money") || command_label.equalsIgnoreCase("cash") || command_label.equalsIgnoreCase("funds")) {
			if (!(sender instanceof Player) && parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "Consoles can't have money! Duh! They would just spend it all on shoes!");
			else {
				String target = sender.getName();
				if (parameters.length > 0)
					target = parameters[0];
				for (Object key : myConomy_accounts.keySet().toArray()) {
					String account_holder = (String) key;
					if (account_holder.toLowerCase().startsWith(target.toLowerCase()))
						if (sender instanceof Player && account_holder.equals(sender.getName()) && sender.hasPermission("myscribe.money"))
							if (monetary_symbol_comes_before)
								sender.sendMessage(ChatColor.BLUE + "You have " + monetary_symbol + myConomy_accounts.get(account_holder) + ".");
							else
								sender.sendMessage(ChatColor.BLUE + "You have " + myConomy_accounts.get(account_holder) + monetary_symbol + ".");
						else if (!(sender instanceof Player) || (!account_holder.equals(sender.getName()) && sender.hasPermission("myscirbe.money.other")))
							if (monetary_symbol_comes_before)
								sender.sendMessage(ChatColor.BLUE + account_holder + " has " + monetary_symbol + myConomy_accounts.get(account_holder) + ".");
							else
								sender.sendMessage(ChatColor.BLUE + account_holder + " has " + myConomy_accounts.get(account_holder) + monetary_symbol + ".");
						else if (!sender.hasPermission("myscribe.money"))
							sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use the monetary system.");
						else
							sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to see other people's money.");
					break;
				}
			}
			return true;
		} else if (command_label.equals("trade") || command_label.equals("exchange")) {
			// TODO
		}
		return false;
	}

	// intra-command methods
	private String AutoCorrect(String message) {
		if (AutoCorrect_on && !message.startsWith("http") && !message.startsWith("www.")) {
			while (message.endsWith("/") || message.endsWith(",") || message.endsWith(">"))
				message = message.substring(0, message.length() - 1);
			// use spaces to make punctuation separate words
			if (message.length() > 1)
				for (int i = 1; i < message.length(); i++)
					if (!message.substring(i - 1, i).toLowerCase().equals(message.substring(i - 1, i).toUpperCase())
							&& message.substring(i, i + 1).toLowerCase().equals(message.substring(i, i + 1).toUpperCase())
							&& !message.substring(i, i + 1).equals(" ") && !message.substring(i, i + 1).equals("'")
							&& (i + 2 > message.length() || !isColorCode(message.substring(i, i + 2), null, null))) {
						try {
							Integer.parseInt(message.substring(i, i + 1));
						} catch (NumberFormatException exception) {
							message = message.substring(0, i) + " " + message.substring(i);
						}
					}
			message = " " + message + " ";
			// perform corrections
			for (int i = 0; i < strings_to_correct.size(); i++)
				message = replace(message, strings_to_correct.get(i), corrected_strings.get(i), unless_strings.get(i), true_means_before.get(i), true);
			String[] words = message.split(" ");
			// change all capital words to italics
			if (change_all_caps_to_italics) {
				for (int i = 0; i < words.length; i++) {
					String temp = words[i];
					if (temp.contains("&") || temp.contains("%"))
						for (int j = 0; j < temp.length() - 1; j++)
							if (isColorCode(temp.substring(j, j + 2), null, null)) {
								temp = temp.substring(0, j) + temp.substring(j + 2);
								if (temp.contains("&") || temp.contains("%"))
									j--;
								else
									break;
							}
					if ((temp.length() > 1 || (i > 0 && words[i - 1].contains("&o")) || (i < words.length - 1 && words[i + 1].contains("&o")))
							&& !temp.contains(":") && !temp.contains("=") && !temp.contains("\\") && temp.equals(temp.toUpperCase())
							&& !temp.toLowerCase().equals(temp) && !replace(temp, " ", "", null, true, true).equalsIgnoreCase("XD")) {
						if (i == words.length - 1 && !temp.endsWith(".") && !temp.endsWith("!") && !temp.endsWith("?") && !temp.endsWith(".\"")
								&& !temp.endsWith("!\"") && !temp.endsWith("?\""))
							if (words[i].endsWith("\""))
								words[i] = words[i].substring(0, words[i].length() - 1) + "!\"";
							else
								words[i] = words[i] + "!";
						words[i] = "&o" + words[i].toLowerCase() + "%o";
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
				message = replace(message, " ass ", " a&kss%k ", null, true, true);
				message = replace(message, " dumbass ", " dumba&kss%k ", null, true, true);
				message = replace(message, " badass ", " bada&kss%k ", null, true, true);
				for (String profanity : profanities)
					message = replace(message, profanity, profanity.substring(0, 1) + "&k" + profanity.substring(1) + "%k", null, true, true);
			}
			if (insert_command_usages)
				for (int i = 0; i < message.length() - 1; i++) {
					if (!message.contains("./"))
						break;
					if (message.substring(i, i + 2).equals("./")) {
						int end_index = i + 2;
						while (end_index < message.length()) {
							if (!message.substring(end_index, end_index + 1).toLowerCase().equals(message.substring(end_index, end_index + 1).toUpperCase()))
								end_index++;
							else
								try {
									Integer.parseInt(message.substring(end_index, end_index + 1));
									end_index++;
								} catch (NumberFormatException exception) {
									break;
								}
						}
						PluginCommand command = server.getPluginCommand(message.substring(i + 2, end_index));
						if (command != null) {
							String color_code = "f";
							if (command.getPlugin().getName().equals("myScribe"))
								color_code = "9";
							else if (command.getPlugin().getName().equals("myUltraWarps"))
								color_code = "a";
							else if (command.getPlugin().getName().equals("myZeus"))
								color_code = "b";
							else if (command.getPlugin().getName().equals("myGuardDog"))
								color_code = "e";
							else if (command.getPlugin().getName().equals("myCarpet"))
								color_code = "6";
							else if (command.getPlugin().getName().equals("myOpAids"))
								color_code = "7";
							String usage = "&" + color_code + replace(command.getUsage(), "<command>", command.getName(), null, true, true) + "%" + color_code;
							if (command.getAliases().size() > 0) {
								usage = usage + " (";
								for (String alias : command.getAliases()) {
									if (!usage.endsWith("("))
										usage = usage + " ";
									usage = usage + "or &" + color_code + "/" + alias + "%" + color_code;
								}
								usage = usage + ")";
							}
							message = replace(message, "./" + command.getName(), usage, null, true, true);
						}
					}
				}
			// capitalize the first letter of every sentence if it is not a
			// correction
			if (capitalize_first_letter)
				for (int i = 0; i < words.length; i++)
					if (message.length() > 0 && !message.startsWith("*") && !message.endsWith("*")
							&& (i == 0 || (words[i - 1].endsWith(".") || words[i - 1].endsWith("!") || words[i - 1].endsWith("?")))) {
						int first_letter_index = 0;
						while (first_letter_index + 1 <= message.length() && message.substring(first_letter_index, +1).equals(" "))
							first_letter_index++;
						while (first_letter_index + 2 <= message.length()
								&& isColorCode(message.substring(first_letter_index, first_letter_index + 2), null, null))
							first_letter_index = first_letter_index + 2;
						message = message.substring(0, first_letter_index) + message.substring(first_letter_index, first_letter_index + 1).toUpperCase()
								+ message.substring(first_letter_index + 1);
					}
			// eliminate extra spaces between letters and punctuation
			int quote_counter = 0;
			for (int i = 1; i < message.length(); i++) {
				if (message.substring(i, i + 1).toLowerCase().equals(message.substring(i, i + 1).toUpperCase())
						&& (i + 2 > message.length() || !isColorCode(message.substring(i, i + 2), null, null))) {
					try {
						Integer.parseInt(message.substring(i, i + 1));
					} catch (NumberFormatException exception) {
						if (message.substring(i, i + 1).equals("\""))
							quote_counter++;
						if (!message.substring(i, i + 1).equals("(") && !message.substring(i, i + 1).equals("[") && !message.substring(i, i + 1).equals("{")
								&& !(message.substring(i, i + 1).equals("\"") && quote_counter % 2 == 1))
							while (i > 0 && message.substring(i - 1, i).equals(" "))
								message = message.substring(0, i - 1) + message.substring(i);
					}
				}
			}
			message = replace(message, "  ", " ", null, true, true);
			while (message.length() >= 2 && isColorCode(message.substring(message.length() - 2), null, null))
				message = message.substring(0, message.length() - 2);
			// end lines with a period if no terminal punctuation exists and the
			// message doesn't start with or end with a * (correction) and the
			// message's last or second to last characters are not colons
			// (emoticons)
			if (message.endsWith("%o"))
				message = message.substring(0, message.length() - 2);
			else if (message.endsWith("%o."))
				message = message.substring(0, message.length() - 3) + ".";
			else if (message.endsWith("%o!"))
				message = message.substring(0, message.length() - 3) + "!";
			else if (message.endsWith("%o?"))
				message = message.substring(0, message.length() - 3) + "?";
			if (message.endsWith(".") || message.endsWith("!") || message.endsWith("?"))
				message = message.substring(0, message.length() - 1) + "%k" + message.substring(message.length() - 1);
			else if (message.endsWith(".\"") || message.endsWith("!\"") || message.endsWith("?\""))
				message = message.substring(0, message.length() - 2) + "%k" + message.substring(message.length() - 2);
			else if (end_with_period
					&& message.length() > 0
					&& !(message.startsWith("*")
							|| message.endsWith("*")
							|| message.endsWith(":")
							|| message.endsWith("=")
							|| message.endsWith(";")
							|| (message.length() >= 2 && (message.substring(message.length() - 2, message.length() - 1).equals(":")
									|| message.substring(message.length() - 2, message.length() - 1).equals("=") || message.substring(message.length() - 2,
									message.length() - 1).equals(";"))) || message.toUpperCase().endsWith("XD") || message.endsWith("<3") || (message.length() >= 3
							&& message.substring(message.length() - 1).equalsIgnoreCase(message.substring(message.length() - 3, message.length() - 2)) && (message
							.toLowerCase().endsWith("o") || message.toLowerCase().endsWith("t") || message.endsWith("-")))) && !message.endsWith("\\"))
				if (!message.endsWith("\""))
					message = message + "%k.";
				else
					message = message.substring(0, message.length() - 1) + "%k.\"";
		}
		return replace(message, "\\", "", "\\", false, true);
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
		if (((true_non_anti_null_either == null || true_non_anti_null_either) && text.substring(0, 1).equals("&"))
				|| ((true_non_anti_null_either == null || !true_non_anti_null_either) && text.substring(0, 1).equals("%")))
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
		if (!to_return.toLowerCase().contains(to_change.toLowerCase()))
			return to_return;
		for (int i = 0; to_return.length() >= i + to_change.length(); i++) {
			if (to_return.substring(i, i + to_change.length()).equalsIgnoreCase(to_change)
					&& (unless == null || (true_means_before && (i < unless.length() || !to_return.substring(i - unless.length(), i).equals(unless))) || (!true_means_before && (to_return
							.length() < i + to_change.length() + unless.length() || !to_return.substring(i + to_change.length(),
							i + to_change.length() + unless.length()).equalsIgnoreCase(unless))))) {
				to_return = to_return.substring(0, i) + to_change_to + to_return.substring(i + to_change.length());
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

	private static Boolean getResponse(CommandSender sender, String unformatted_response, String current_status_line, String current_status_is_true_message) {
		boolean said_yes = false, said_no = false;
		String formatted_response = unformatted_response;
		// elimiate unnecessary spaces and punctuation
		while (formatted_response.startsWith(" "))
			formatted_response = formatted_response.substring(1);
		while (formatted_response.endsWith(" "))
			formatted_response = formatted_response.substring(0, formatted_response.length() - 1);
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
						unformatted_response = unformatted_response.substring(1);
					sender.sendMessage(ChatColor.RED + "I don't know what \"" + unformatted_response + "\" means.");
				}
				while (current_status_line.startsWith(" "))
					current_status_line = current_status_line.substring(1);
				if (current_status_line.startsWith(current_status_is_true_message))
					return true;
				else
					return false;
			} else
				return null;
		}
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
			if (command_beginnings.get(event.getPlayer()) != null) {
				command_beginning = command_beginnings.get(event.getPlayer());
				command_beginnings.remove(event.getPlayer());
			}
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
				formatted_message = replace(replace(formatted_message, "[epithet]", epithet, null, true, false), "[message]", AutoCorrect(full_chat_message),
						null, true, false);
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

	@EventHandler
	public void commandProcessing(PlayerCommandPreprocessEvent event) {
		if (!players_who_have_accepted_the_rules.contains(event.getPlayer().getName()) && !event.getPlayer().isOp()
				&& !event.getMessage().toLowerCase().startsWith("rules") && !event.getMessage().toLowerCase().startsWith("accept")) {
			event.getPlayer().sendMessage(ChatColor.RED + "You have to read and accept the rules first.");
			event.setCancelled(true);
		} else if (event.getMessage().endsWith("[...]")) {
			String command_beginning = command_beginnings.get(event.getPlayer());
			if (command_beginning == null)
				command_beginning = "";
			command_beginnings.put(event.getPlayer(), command_beginning + event.getMessage().substring(1, event.getMessage().length() - 5));
			event.getPlayer().sendMessage(ChatColor.BLUE + "You may continue typing.");
			event.setCancelled(true);
		} else {
			String command = event.getMessage().substring(1);
			if (command_beginnings.get(event.getPlayer()) != null) {
				command = command_beginnings.get(event.getPlayer()) + command;
				command_beginnings.remove(event.getPlayer());
			}
			event.setMessage("/" + command);
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
	private void loadTheAutoCorrections(CommandSender sender) {
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
				while (save_line.startsWith(" "))
					save_line = save_line.substring(1);
				boolean already_progressed = false;
				if (save_line.startsWith("Would you like to use AutoCorrections in your server's chat?"))
					AutoCorrect_on = getResponse(sender, save_line.substring(60), in.readLine(), "Right now, AutoCorrections are enabled.");
				else if (save_line.startsWith("Would you like me to capitalize the first letter of every sentence?"))
					capitalize_first_letter = getResponse(sender, save_line.substring(67), in.readLine(),
							"   Right now, first letter capitalization is enabled.");
				else if (save_line.equals("Would you like me to put periods at the end of any sentences that have no terminal punctuation?"))
					end_with_period = getResponse(sender, save_line.substring(95), in.readLine(), "Right now, period addition is enabled.");
				else if (save_line.equals("Would you like me to change any all-caps words or sentences to lowercase italics?"))
					change_all_caps_to_italics = getResponse(sender, save_line.substring(81), in.readLine(),
							"Right now, caps to italics conversion is enabled.");
				else if (save_line.equals("Would you like me to cover up profanities using magic (meaning the &k color code, not fairy dust)?"))
					cover_up_profanities = getResponse(sender, save_line.substring(98), in.readLine(), "Right now, profanity coverup is enabled.");
				else if (save_line.equals("Would you like me to replace all in-text commands with their usages when they start with a \"./\"?"))
					insert_command_usages = getResponse(sender, save_line.substring(96), in.readLine(), "Right now, command usage insertion is enabled.");
				else {
					if (save_line.length() > 8 && save_line.substring(0, 8).equalsIgnoreCase("change: ")) {
						String string_to_correct = save_line.substring(8);
						if (string_to_correct.startsWith("\"") && string_to_correct.endsWith("\""))
							string_to_correct = string_to_correct.substring(1, string_to_correct.length() - 1);
						strings_to_correct.add(string_to_correct);
						save_line = in.readLine();
						already_progressed = true;
						if (save_line != null && save_line.length() >= 4 && save_line.substring(0, 4).equals("to: ")) {
							if (save_line.length() > 4) {
								String corrected_string = save_line.substring(4);
								if (corrected_string.startsWith("\"") && corrected_string.endsWith("\""))
									corrected_string = corrected_string.substring(1, corrected_string.length() - 1);
								corrected_strings.add(corrected_string);
							} else
								corrected_strings.add("");
							save_line = in.readLine();
							if (save_line != null && save_line.length() > 8 && save_line.substring(0, 8).equalsIgnoreCase("unless: ")) {
								String unless_string = save_line.substring(8);
								if (unless_string.startsWith("\"") && unless_string.endsWith("\""))
									unless_string = unless_string.substring(1, unless_string.length() - 1);
								unless_strings.add(unless_string);
								save_line = in.readLine();
								if (save_line != null && save_line.length() >= 13 && save_line.substring(0, 13).equalsIgnoreCase("comes: before")) {
									true_means_before.add(true);
									save_line = in.readLine();
								} else if (save_line != null && save_line.length() >= 12 && save_line.substring(0, 12).equalsIgnoreCase("comes: after")) {
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
			if (sender instanceof Player)
				if (strings_to_correct.size() == 0)
					console.sendMessage(ChatColor.BLUE + sender.getName() + " loaded your AutoCorrect settings from file.");
				else if (strings_to_correct.size() == 1)
					console.sendMessage(ChatColor.BLUE + sender.getName() + " loaded your AutoCorrect settings and your one AutoCorrection from file.");
				else
					console.sendMessage(ChatColor.BLUE + sender.getName() + " loaded your AutoCorrect settings and your " + strings_to_correct.size()
							+ " AutoCorrections from file.");
		}
	}

	private void loadTheDeathMessages(CommandSender sender) {
		boolean failed = false;
		death_messages_by_cause = new HashMap<String, ArrayList<String>>();
		// check the death messages file
		File death_messages_file = new File(this.getDataFolder(), "death messages.txt");
		if (!death_messages_file.exists()) {
			this.getDataFolder().mkdir();
			try {
				sender.sendMessage(ChatColor.YELLOW + "I couldn't find an death messages.txt file. I'll make a new one.");
				death_messages_file.createNewFile();
			} catch (IOException exception) {
				sender.sendMessage(ChatColor.DARK_RED + "I couldn't create an death messages.txt file! Oh nos!");
				exception.printStackTrace();
				failed = true;
			}
		}
		// read the death messages.txt file
		try {
			BufferedReader in = new BufferedReader(new FileReader(death_messages_file));
			String save_line = in.readLine();
			while (save_line != null) {
				while (save_line.startsWith(" "))
					save_line = save_line.substring(1);
				if (save_line.startsWith("Do you want hilarious death messages to appear when people die?"))
					display_death_messages = getResponse(sender, save_line.substring(63), in.readLine(),
							"Right now, hilarious death messages will appear when someone dies.");
				save_line = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "The death messages.txt I created a few milliseconds ago doesn't exist. -_-");
			exception.printStackTrace();
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your death messages.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed) {
			saveTheDeathMessages(sender, false);
			if (death_messages_by_cause.size() == 0)
				sender.sendMessage(ChatColor.BLUE + "You don't have any death messages to load.");
			else if (death_messages_by_cause.size() == 1)
				sender.sendMessage(ChatColor.BLUE + "Your only death message has been loaded.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your " + death_messages_by_cause.size() + " death messages have been loaded.");
			if (sender instanceof Player)
				if (death_messages_by_cause.size() == 0)
					console.sendMessage(ChatColor.BLUE + sender.getName() + " tried to laod your death messages from file, but there were none.");
				else if (death_messages_by_cause.size() == 1)
					console.sendMessage(ChatColor.BLUE + sender.getName() + " loaded your only death message from file.");
				else
					console.sendMessage(ChatColor.BLUE + sender.getName() + " loaded your " + death_messages_by_cause.size() + " death messages from file.");
		}
	}

	private void loadTheEpithets(CommandSender sender) {
		boolean failed = false;
		epithets_by_user = new HashMap<String, String>();
		default_epithet = "<[player]>";
		default_message_format = "[epithet]&f: [message]";
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
			out.newLine();
			out.write("universal AutoCorrections:");
			out.newLine();
			for (int i = 0; i < strings_to_correct.size(); i++) {
				if (strings_to_correct.get(i) != null && !strings_to_correct.get(i).equals("") && corrected_strings.get(i) != null) {
					out.write("  change: \"" + strings_to_correct.get(i) + "\"");
					out.newLine();
					out.write("  to: \"" + corrected_strings.get(i) + "\"");
					out.newLine();
					if (unless_strings.get(i) != null) {
						out.write("  unless: \"" + unless_strings.get(i) + "\"");
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
				sender.sendMessage(ChatColor.BLUE + "You don't actually have any death messages to save.");
			else if (strings_to_correct.size() == 1)
				sender.sendMessage(ChatColor.BLUE + "Your server's only death message has been saved.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your server's " + strings_to_correct.size() + " death messages have been saved.");
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
			say_format = epithet;
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
		// get the item's I.D.
		int id = -1;
		try {
			id = Integer.parseInt(query);
		} catch (NumberFormatException exception) {
			for (int i = 0; i < item_IDs.length; i++) {
				for (String item_name : item_IDs[i])
					if (item_name.toLowerCase().startsWith(query.toLowerCase())) {
						id = i;
						break;
					}
				if (id != -1)
					break;
			}
		}
		String recipe = item_recipes.get(id);
		if (recipe != null)
			sender.sendMessage(colorCode(recipe));
		else if (item_IDs[id] != null)
			sender.sendMessage(ChatColor.BLUE + "You can't craft a " + item_IDs[id][0] + "!");
		else if (query.toLowerCase().startsWith("a") || query.toLowerCase().startsWith("e") || query.toLowerCase().startsWith("i")
				|| query.toLowerCase().startsWith("o") || query.toLowerCase().startsWith("u"))
			sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what an \"" + query + "\" is.");
		else
			sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what a \"" + query + "\" is.");
	}
}
