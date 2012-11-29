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
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
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
import org.bukkit.plugin.java.JavaPlugin;

public class myScribe extends JavaPlugin implements Listener {
	private static Server server;
	private static ConsoleCommandSender console;
	private String[] parameters;
	private static final String[] enable_messages = { "The pen is mightier than the pixelated diamond sword.",
			"I shall demonstrate unto all of you the proper way to use this thrillingly versatile language that we refer to as 'English.'",
			"I advise against the use of my AutoCorrection features if many persons that choose to enter your server do not speak English.",
			"William Shakespeare? I once knew him as \"Bill.\"", "I am rapping. ...rapping at your chamber door." },
			disable_messages = {
					"Though I am now disabled, I shall continue to spread proper literacy across the globe in the hope that some day soon, we will see people speaking proper English once again.",
					"If you believe plugins can't dream,...\n...you're wrong.", "Farewell, good literate sir.", "Good evening, Sir Operator.",
					"I shall return with the gifts of proper language upon the arrival of the upcoming morn." },
			color_color_code_chars = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" },
			formatting_color_code_chars = { "k", "l", "m", "n", "o", "r" },
			profanities = { "fuck", "fck", "fuk", "Goddamn", "Goddam", "damn", "shit", "dammit", "bastard", "bitch", "btch", "damnit", "cunt", "asshole",
					"bigass", "dumbass", "badass", "dick" },
			borders = { "[]", "\\/", "\"*", "_^", "-=", ":;", "&%", "#@", ",.", "<>", "~$", ")(" },
			yeses = { "yes", "yeah", "yep", "ja", "sure", "why not", "okay", "do it", "fine", "whatever", "very well", "accept", "tpa", "cool", "hell yeah",
					"hells yeah", "hells yes", "come" },
			nos = { "no", "nah", "nope", "no thanks", "no don't", "shut up", "ignore", "it's not", "its not", "creeper", "unsafe", "wait", "one ", "1 " },
			recipes = { null, null, null, null, "Just craft any kind of wood (logs).", null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, "Melt sand in a furnace.", null, "&1lapis lazuli\nLLL\nLLL\nLLL",
					"&8cobblestone&f, bow, &4redstone dust\n&8CCC\nC&fB&8C\nC&4R&8C", "&esand\n&0---\n-&eSS\n&0-&eSS",
					"&4redstone dust\n&e&m&nWWW\nW&4R&e&m&nW\nWWW", "wool, &e&m&nwooden planks\n&0---\n&fWWW\n&e&m&nWWW",
					"&6gold&f, &e&msticks&f, &4redstone dust\n&6G&0-&6G\nG&f&e&mS&6G\nG&4R&6G",
					"&7iron ingots&f, &8&nstone pressure plate&f, &4redstone dust\n&7I&0-&7I\nI&8&nS&7I\nI&4R&7I",
					"&aslimeballs&f, &e&npistons\n&0---\n-&aS&0-\n-&e&nP&0-", null, null, null,
					"&e&m&nwooden planks&f, &8cobblestone&f, &7iron ingots&f, &4redstone dust\n&e&m&nWWW\n&8C&7I&8C\nC&4R&8C", null,
					"string\n&0---\n-&fSS\n&0-&fSS", null, null, null, null, null, "&6gold ingots\nGGG\nGGG\nGGG", "&7iron ingots\nIII\nIII\nIII", null,
					"&5&kM &fcan be cobblestone, stone, stone bricks, wooden planks, bricks, or sandstone\n&0---\n---&bx6\n&5&kMMM",
					"&cBricks &f(which can be made by cooking clay)\n&0---\n-&cBB\n&0-&cBB", "&esand&f, &8gunpowder\n&8G&eS&8G\n&eS&8G&eS\n&8G&eS&8G",
					"&e&m&nwooden planks&f, &cbooks\n&e&m&nWWW\n&cBBB\n&e&m&nWWW", null, null, "&fcoal or charcoal, &e&msticks\n&0---\n-&fC&0-\n-&e&mS&0-",
					null, null, "&5&kM &fcan be any wooden planks, cobblestone, bricks, any stone bricks, Nether brick, or sanstone\n&0--&5&kM\n&0-&5&kMM\nMMM" };
	public static final String[][] item_IDs =
			{
					{ "air" },
					{ "stone", "rock", "smooth stone" },
					{ "grass", "grass blocks" },
					{ "dirt", "filth" },
					{ "cobblestone", "cobblies" },
					{ "wooden planks", "wood planks", "planks", "oak planks", "birch planks", "spruce planks", "pine planks", "jungle planks",
							"oak wood planks", "birch wood planks", "spruce wood planks", "pine wood planks", "jungle wood planks" },
					{ "saplings", "oak saplings", "birch saplings", "spruce saplings", "pine saplings", "jungle saplings" },
					{ "bedrock" },
					{ "water" },
					{ "stationary water" },
					{ "lava" },
					{ "stationary lava" },
					{ "sand" },
					{ "gravel", "pebbles" },
					{ "gold ore", "golden ore" },
					{ "iron ore" },
					{ "coal ore" },
					{ "logs", "wood", "oak wood", "birch wood", "spruce wood", "pine wood", "jungle wood", "oak logs", "birch logs", "spruce logs",
							"pine logs", "jungle logs" },
					{ "leaves", "leaf blocks", "oak leaves", "birch leaves", "spruce leaves", "pine leaves", "jungle leaves", "oak leaf blocks",
							"birch leaf blocks", "spruce leaf blocks", "pine leaf blocks", "jungle leaf blocks" },
					{ "sponges", "loofas" },
					{ "glass", "glass blocks" },
					{ "lapis lazuli ore", "lapis ore" },
					{ "lapis lazuli blocks", "lapis blocks", "block of lapis lazuli", "blocks of lapis lazuli" },
					{ "dispensers", "shooters" },
					{ "sandstone", "sand brick", "sandbrick", "sand stone" },
					{ "note blocks", "music blocks", "sound blocks", "speakers" },
					{ "beds" },
					{ "powered rails", "redstone rails", "powered tracks", "redstone tracks", "powered railroad tracks", "redstone railroad tracks" },
					{ "detector rails", "detection rails", "sensor rails", "sensory rails", "pressure rails", "pressure plate rails",
							"detector railroad tracks", "detection railroad tracks", "sensor railroad tracks", "sensory railroad tracks",
							"pressure railroad tracks", "pressure plate railroad tracks" },
					{ "sticky pistons", "slime pistons", "slimy pistons" },
					{ "cobwebs", "spider webs", "spiderwebs", "webs" },
					{ "tall grass", "high grass", "weeds" },
					{ "dead bushes", "dried plants" },
					{ "pistons" },
					{ "piston extensions", "piston arm", "piston pusher" },
					{ "wool", "wool blocks" },
					{ "blocks moved by pistons" },
					{ "flowers", "yellow flowers", "dandelions" },
					{ "roses", "red flowers" },
					{ "brown mushrooms", "brown shrooms", "small brown mushrooms", "little brown mushrooms" },
					{ "red mushrooms", "red shrooms", "small red mushrooms", "little red mushrooms" },
					{ "gold blocks", "block of gold", "blocks of gold" },
					{ "iron blocks", "block of iron", "blocks of iron" },
					{ "double slab blocks", "stacked slabs" },
					{ "slabs", "half blocks", "brick slabs", "stone slabs", "sandstone slabs", "cobblestone slabs", "brick half blocks", "stone half blocks",
							"sandstone half blocks", "cobblestone half blocks" },
					{ "bricks", "brick blocks" },
					{ "T.N.T.", "TNT", "dynamite", "trinitrotoluene" },
					{ "bookcases", "book cases", "bookshelves", "bookshelf", "shelves", "shelf" },
					{ "mossy stone", "mossy cobblestone", "moss stone", "moss cobblestone" },
					{ "obsidian", "volcanic glass" },
					{ "torches", "fire sticks" },
					{ "fire", "flames" },
					{ "monster spawners", "spawners" },
					{ "oak stairs", "oak wood stairs", "wooden stairs", "oak steps", "oak wood steps", "wooden steps" },
					{ "chests" },
					{ "redstone wire", "redstone dust", "wire", "redstone powder" },
					{ "diamond ore" },
					{ "diamond blocks", "blocks of diamonds" },
					{ "crafting tables", "crafting bench", "workbench", "work bench" },
					{ "wheat", "crops" },
					{ "farmland blocks" },
					{ "furnaces", "ovens", "ranges" },
					{ "burning furnaces" },
					{ "sign posts", "ground sign" },
					{ "wooden doors" },
					{ "ladders", "wooden ladders" },
					{ "rails", "iron rails", "regular rails" },
					{ "cobblestone stairs", "cobblestone stairs" },
					{ "wall signs", "posted signs" },
					{ "levers", "switches" },
					{ "stone pressure plates", "cobblestone foot switches" },
					{ "iron doors" },
					{ "wooden pressure plates", "wood pressure plates", "wooden foot switches", "wood foot switches" },
					{ "redstone ore", "redstone blocks" },
					{ "glowing redstone ore", "luminescent redstone ore" },
					{ "redstone torches" },
					{ "active redstone torches" },
					{ "buttons", "stone buttons", "cobblestone buttons" },
					{ "natural snow on the ground", "snow", "snow on the ground", "fallen snow", "fresh snow", "ground snow" },
					{ "ice", "blocks of ice", "ice blocks" },
					{ "snow blocks", "blocks of snow" },
					{ "cacti", "cactuses", "saguaros", "saguaro cacti", "saguaro cactuses" },
					{ "clay blocks", "blocks of clay" },
					{ "sugar cane", "sugar canes", "sugarcanes" },
					{ "jukeboxes", "disc player", "music box", "slotted block", ".mp3 player" },
					{ "wooden fences", "wooden fence posts", "wooden railings" },
					{ "pumpkins", "unlit Jack-o'-Lanterns", "dark Jack-o'-Lanterns" },
					{ "Netherrack", "Nether rack", "Nether dirt" },
					{ "Soul Sand", "Nether sand", "quicksand", "quick sand" },
					{ "glowstone", "glowstone blocks", "blocks of glowstone" },
					{ "Nether portal blocks", "Nether portal swirly blocks" },
					{ "Jack-o'-Lanterns", "lit Jack-o'-Lanterns", "lit Jack o Lanterns", "lit JackoLanterns" },
					{ "cakes", "cake blocks" },
					{ "repeaters", "diodes", "delayers", "redstone repeaters", "redstone diodes", "redstone delayers" },
					{ "active repeaters", "active diodes", "active delayers", "active redstone repeaters", "active redstone diodes", "active redstone delayers" },
					{ "locked chests" },
					{ "trapdoors", "ground doors" },
					{ "silverfish spawners", "monster egg blocks", "silverfish stone", "silverfish cobblestone", "monster eggs" },
					{ "stone bricks", "cobblestone bricks" },
					{ "giant brown mushroom blocks", "huge brown mushrooms", "big brown mushrooms", "giant brown 'shroom blocks", "huge brown 'shrooms",
							"big brown shrooms", "giant brown shroom blocks", "huge brown shrooms", "big brown shrooms" },
					{ "giant red mushroom blocks", "huge red mushrooms", "big red mushrooms", "giant red 'shroom blocks", "huge red 'shrooms",
							"big red shrooms", "giant red shroom blocks", "huge red shrooms", "big red shrooms" },
					{ "iron bars", "wrought iron bars" },
					{ "glass panes", "windows", "window panes" },
					{ "melon blocks", "full melons", "watermelon blocks", "whole melons", "whole watermelons" },
					{ "pumpkin stems", "pumpkin stalks", "pumpkin vines" },
					{ "melon stems", "melon stalks", "melon vines", "watermelon stems", "watermelon stalks", "watermelon vines" },
					{ "vines", "jungle vines", "swamp vines" },
					{ "fence gates", "wooden gates", "wood gates" },
					{ "brick stairs", "brick steps", "clay brick stairs", "clay brick steps" },
					{ "stone brick stairs", "stone brick steps", "stone stairs", "stone steps" },
					{ "mycelium", "mushroom grass", "shroom grass", "mushroom biome grass" },
					{ "lily pads", "lilies", "pond lilies", "lilypads", "water lily", "water lilies" },
					{ "Nether bricks", "Nether fortress bricks", "Nether dungeon bricks" },
					{ "Nether brick fences", "Nether fortress fences", "Nether dungeon fences" },
					{ "Nether brick stairs", "Nether fortress stairs", "Nether dungeon stairs", "Nether brick steps", "Nether fortress steps",
							"Nether dungeon steps" },
					{ "Nether warts", "Nether mushrooms", "Nether 'shrooms", "Nether fungi" },
					{ "enchantment tables" },
					{ "brewing stands" },
					{ "cauldrons" },
					{ "End portal blocks" },
					{ "End portal frame blocks" },
					{ "End stone", "End blocks" },
					{ "dragon eggs", "Enderdragon eggs" },
					{ "redstone lamps", "glowstone lamps" },
					{ "active redstone lamps", "active glowstone lamps" },
					{ "wooden double slabs", "double wooden plank slabs", "oak wood double slabs", "oak wood wooden plank slabs", "birch wood double slabs",
							"double birch wood plank slabs", "spruce wood double slabs", "double spruce wood plank slabs", "jungle wood double slabs",
							"double jungle wood plank slabs" },
					{ "wooden slabs", "oak wood slabs", "birch wood slabs", "spruce wood slabs", "jungle wood slabs" },
					{ "cocoa bean plants", "cocoa bean pods", "cocoa beans" },
					{ "sandstone stairs", "sandstone steps" },
					{ "emerald ore" },
					{ "Ender chests", "Enderchests" },
					{ "tripwire hooks", "tripwire mechanisms", "trip wire hooks", "trip wire mechanisms" },
					{ "tripwire", "trip wire" },
					{ "emerald blocks", "blocks of emerald", "blocks of emeralds" },
					{ "spruce stairs", "spruce wood stairs", "wooden stairs", "spruce steps", "spruce wood steps", "wooden steps" },
					{ "birch stairs", "birch wood stairs", "wooden stairs", "birch steps", "birch wood steps", "wooden steps" },
					{ "jungle stairs", "jungle wood stairs", "wooden stairs", "jungle steps", "jungle wood steps", "wooden steps" },
					{ "command blocks" },
					{ "beacons" },
					{ "cobblestone walls", "stone walls", "mossy cobblestone walls", "mossy stone walls" },
					{ "flower pots", "pots", "clay pots" },
					{ "carrots" },
					{ "potatoes", "potatos" },
					{ "wooden buttons", "wood buttons" },
					{ "monster heads", "heads" },
					{ "anvils" },
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
					{ "iron shovels", "iron spades" },
					{ "iron pickaxes", "iron picks" },
					{ "iron axes", "iron hatchets", "iron tree axe" },
					{ "flint and steel" },
					{ "apples", "red apples" },
					{ "bows", "bows and arrows", "longbows", "long bows", "shortbows", "short bows" },
					{ "arrows" },
					{ "coal" },
					{ "diamonds" },
					{ "iron", "iron ingots" },
					{ "gold", "gold ingots", "gold bars" },
					{ "iron swords" },
					{ "wooden swords", "wood swords" },
					{ "wooden shovels", "wood shovels", "wooden spades", "wood spades" },
					{ "wooden pickaxes", "wooden picks", "wood pickaxes", "wood picks" },
					{ "wooden axes", "wooden hatchets", "wooden tree axes", "wood axes", "wood hatchets", "wood tree axes" },
					{ "stone swords", "cobblestone swords" },
					{ "stone shovels", "cobblestone shovels", "stone spades", "cobblestone spades" },
					{ "stone pickaxes", "stone picks", "cobblestone pickaxes", "cobblestone picks" },
					{ "stone axes", "stone hatchets", "stone tree axes", "cobblestone axes", "cobblestone hatchets", "cobblestone tree axes" },
					{ "diamond swords" },
					{ "diamond shovels", "diamond spades" },
					{ "diamond pickaxes", "diamond picks" },
					{ "diamond axes", "diamond hatchets", "diamond tree axes" },
					{ "sticks", "twigs" },
					{ "bowls", "wooden bowls", "wood bowls", "soup bowls" },
					{ "mushroom stew", "mushroom soup", "mooshroom milk", "mooshroom cow milk" },
					{ "golden swords", "gold swords" },
					{ "golden shovels", "gold shovels", "golden spades", "gold spades" },
					{ "golden pickaxes", "golden picks", "gold pickaxes", "gold picks" },
					{ "golden axes", "golden hatchets", "golden tree axes", "gold axes", "gold hatchets", "gold tree axes" },
					{ "string" },
					{ "feathers" },
					{ "gunpowder", "sulfur", "sulphur" },
					{ "wooden hoes", "wood hoes" },
					{ "stone hoes", "cobblestone hoes" },
					{ "iron hoes" },
					{ "diamond hoes" },
					{ "golden hoes", "gold hoes" },
					{ "seeds", "seed packets" },
					{ "wheat", "crops" },
					{ "bread", "bread loaves" },
					{ "leather caps", "leather helmets", "leather helms" },
					{ "leather tunics", "leather shirts", "leather chestplates" },
					{ "leather pants", "leather leggings", "leather chaps" },
					{ "leather boots", "leather shoes" },
					{ "chainmail helmets", "chainmail caps", "chainmail helms", "chain helmets", "chain caps", "chain helms" },
					{ "chainmail chestplates", "chainmail tunics", "chainmail shirts", "chain chestplates", "chain tunics", "chain shirts" },
					{ "chainmail leggings", "chainmail pants", "chain leggings", "chain pants" },
					{ "chainmail boots", "chainmail shoes", "chain boots", "chain shoes" },
					{ "iron helmets", "iron caps", "iron helms" },
					{ "iron chestplates", "iron tunics", "iron shirts" },
					{ "iron leggings", "iron pants" },
					{ "iron boots", "iron shoes" },
					{ "diamond helmets", "diamond caps", "diamond helms" },
					{ "diamond chestplates", "diamond tunics", "diamond shirts" },
					{ "diamond leggings", "diamond pants" },
					{ "diamond boots", "diamond shoes" },
					{ "golden helmets", "golden caps", "golden helms", "gold helmets", "gold caps", "gold helms" },
					{ "golden chestplates", "gold chestplates", "golden tunics", "gold tunics", "golden shirts", "gold shirts" },
					{ "golden leggings", "gold leggings", "golden pants", "gold pants" },
					{ "golden boots", "gold boots", "golden shoes", "gold shoes" },
					{ "flint", "arrowheads" },
					{ "raw porkchops", "uncooked porkchops" },
					{ "cooked porkchops", "porkchops" },
					{ "paintings", "artwork" },
					{ "golden apples", "gold apples" },
					{ "signs", "sign posts", "posted signs", "wall signs" },
					{ "wooden doors", "wood doors" },
					{ "buckets", "pails" },
					{ "buckets of water", "water buckets" },
					{ "buckets of lava", "lava buckets" },
					{ "minecarts", "mine carts", "minecars", "mine cars", "rail cars" },
					{ "saddles", "pig saddles" },
					{ "iron doors", "metal doors" },
					{ "redstone dust", "redstone wire", "wire", "redstone powder" },
					{ "snowballs" },
					{ "boats", "wooden boats", "wood boats", "rafts", "wood rafts", "wooden rafts" },
					{ "leather", "cow hides", "cow skin", "cowskin" },
					{ "milk", "leche", "bucket of milk", "buckets of milk", "pail of milk", "pails of milk" },
					{ "bricks", "clay bricks" },
					{ "clay" },
					{ "sugarcane", "sugarcanes", "sugar canes" },
					{ "papers" },
					{ "books" },
					{ "slimeballs", "slime balls" },
					{ "storage minecarts", "storage minecars", "storage mine cars", "storage rail cars", "chest minecarts", "chest minecars",
							"chest mine cars", "chest rail cars", "minecarts with chests", "minecars with chests", "mine cars with chests",
							"rail cars with chests" },
					{ "powered minecarts", "powered minecars", "powered mine cars", "powered rail cars", "furnace minecarts", "furnace minecars",
							"furnace mine cars", "furnace rail cars", "minecarts with furnaces", "minecars with furnaces", "mine cars with furnaces",
							"rail cars with furnaces" },
					{ "eggs", "chicken eggs" },
					{ "compasses" },
					{ "fishing rods", "fishing poles" },
					{ "clocks", "watches", "pocketwatches", "pocket watches" },
					{ "glowstone dust" },
					{ "raw fish", "uncooked fish", "raw fish", "uncooked fish", "sushi" },
					{ "cooked fish", "fish" },
					{ "bonemeal or wool dyes", "wool dyes", "dyes", "bone meals", "bonemeals" },
					{ "bones" },
					{ "sugar", "processed sugar", "powdered sugar", "raw sugar", "baker's sugar" },
					{ "cakes", "birthday cakes" },
					{ "beds" },
					{ "repeaters", "diodes", "delayers", "redstone repeaters", "redstone diodes", "redstone delayers" },
					{ "cookies", "chocolate chip cookies", "oatmeal raisin cookies" },
					{ "maps", "atlases", "charts" },
					{ "shears", "clippers" },
					{ "melon slices", "slices of melon" },
					{ "pumpkin seeds" },
					{ "melon seeds" },
					{ "raw beef", "uncooked beef", "uncooked steak" },
					{ "cooked beef", "beef", "steak" },
					{ "raw chickens", "uncooked chickens" },
					{ "cooked chickens", "chickens" },
					{ "rotten flesh", "rotted flesh", "flesh", "zombie flesh", "zombie meat" },
					{ "Ender pearls", "Enderman pearls" },
					{ "Blaze rods", "glowsticks", "glow sticks" },
					{ "Ghast tears", "tears" },
					{ "gold nuggets", "golden nuggets", "gold pieces", "pieces of gold" },
					{ "Nether warts", "Nether mushrooms", "Nether 'shrooms", "Nether fungi" },
					{ "potions" },
					{ "glass bottles" },
					{ "spider eyes" },
					{ "fermented spider eyes" },
					{ "Blaze powder" },
					{ "Magma cream" },
					{ "brewing stands" },
					{ "cauldrons", "kettles", "pots" },
					{ "Eyes of Ender", "Endereyes", "Ender Eyes" },
					{ "glistening melon", "glistening melon slices", "gold melon slices", "golden melon slices", "shining melon slices" },
					{ "spawn eggs", "spawner eggs" },
					{ "Bottles o' Enchanting", "xp bottles", "exp bottles", "level botties", "experience bottles" },
					{ "fire charges", "fireballs", "cannonballs", "Ghast cannonballs", "Blaze cannonballs", "Ghast fireballs", "Blaze fireballs" },
					{ "books and quills", "book and quill" },
					{ "written-in books", "written books", "novels", "texts" },
					{ "emeralds" },
					{ "frames", "item frames" },
					{ "flower pots", "potted flowers", "potted plants" },
					{ "carrots" },
					{ "potatoes", "raw potatoes" },
					{ "baked potatoes", "cooked potatoes", "mashed potatoes" },
					{ "poisonous potatoes", "poison potatoes", "bad potatoes" },
					{ "maps", "charts", "atlases" },
					{ "golden carrots", "gold carrots", "glistening carrots", "shiny carrots" },
					{ "monster heads", "heads" },
					{ "carrots on sticks", "carrots on fishing rods", "carrots on fishing poles", "pig controller" },
					{ "Nether Stars" },
					{ "pumpkin pies" },
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
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					{ "\"13\" music discs", "\"13\" discs", "\"13\" records", "\"13\" CDs", "13 music disc", "13 CDs", "13 discs", "13 records" },
					{ "\"cat\" music discs", "\"cat\" discs", "\"cat\" records", "\"cat\" CDs", "cat music disc", "cat CDs", "cat discs", "cat records" },
					{ "\"blocks\" music discs", "\"blocks\" discs", "\"blocks\" records", "\"blocks\" CDs", "blocks music disc", "blocks CDs", "blocks discs",
							"blocks records" },
					{ "\"chirp\" music discs", "\"chirp\" discs", "\"chirp\" records", "\"chirp\" CDs", "chirp music disc", "chirp CDs", "chirp discs",
							"chirp records" },
					{ "\"far\" music discs", "\"far\" discs", "\"far\" records", "\"far\" CDs", "far music disc", "far CDs", "far discs", "far records" },
					{ "\"mall\" music discs", "\"mall\" discs", "\"mall\" records", "\"mall\" CDs", "mall music disc", "mall CDs", "mall discs", "mall records" },
					{ "\"mellohi\" music discs", "\"mellohi\" discs", "\"mellohi\" records", "\"mellohi\" CDs", "mellohi music disc", "mellohi CDs",
							"mellohi discs", "mellohi records" },
					{ "\"stal\" music discs", "\"stal\" discs", "\"stal\" records", "\"stal\" CDs", "stal music disc", "stal CDs", "stal discs", "stal records" },
					{ "\"strad\" music discs", "\"strad\" discs", "\"strad\" records", "\"strad\" CDs", "strad music disc", "strad CDs", "strad discs",
							"strad records" },
					{ "\"ward\" music discs", "\"ward\" discs", "\"ward\" records", "\"ward\" CDs", "ward music disc", "ward CDs", "ward discs", "ward records" },
					{ "\"11\" music discs", "\"11\" discs", "\"11\" records", "\"11\" CDs", "11 music disc", "11 CDs", "11 discs", "11 records" } };
	private Object[][] default_corrections = { { " i ", " I ", null, true }, { " ik ", " I know ", null, true }, { " ib ", " I'm back ", null, true },
			{ " ic ", " I see ", null, true }, { " tp ", " teleport ", null, false }, { " idk ", " I don't know ", null, true },
			{ " idc ", " I don't care ", null, true }, { " ikr ", " I know, right? ", "?", false }, { " ikr ", " I know, right ", null, true },
			{ " irl ", " in real life ", null, true }, { " wtf ", " what the fuck? ", "?", false }, { " wtf ", " what the fuck ", null, true },
			{ " wth ", " what the hell? ", "?", false }, { " wth ", " what the hell ", null, true }, { " ftw ", " for the win ", null, true },
			{ " y ", " why ", "=", false }, { " u ", " you ", null, true }, { " ur ", " your ", null, true }, { " r ", " are ", null, true },
			{ " o . o ", " \\o.\\o\\ ", null, true }, { " o ", " oh ", null, true }, { " c ", " see ", null, true }, { " k ", " okay ", null, true },
			{ " kk ", " okay ", null, true }, { " ic ", " I see ", null, true }, { " cya ", " see ya ", null, true }, { " sum1", " someone ", null, true },
			{ " some1", " someone ", null, true }, { "every1", "everyone", null, true }, { "any1", "anyone", null, true },
			{ " ttyl ", " I'll talk to you later ", null, true }, { " wb ", " welcome back ", null, true }, { " ty ", " thank you ", null, true },
			{ " yw ", " you're welcome ", null, true }, { " gb ", " goodbye ", null, true }, { " hb ", " happy birthday ", null, true },
			{ " gl ", " good luck ", null, true }, { " jk ", " just kidding ", null, true }, { " jking ", " just kidding ", null, true },
			{ " jkjk ", " just kidding ", null, true }, { " np ", " no problem ", null, true }, { " afk ", " \\a.\\f.\\k. ", "/", true },
			{ " \\a.\\f.\\k. .", " \\a.\\f.\\k.", null, true }, { " omg ", " oh my God ", null, true }, { " omfg ", " oh my fucking God ", null, true },
			{ " stfu ", " shut the fuck up ", null, true }, { " btw ", " by the way ", null, true }, { " i gtg ", " I have to go ", null, true },
			{ " i g2g ", " I have to go ", null, true }, { " igtg ", " I have to go ", null, true }, { " ig2g ", " I have to go ", null, true },
			{ " gtg ", " I have to go ", null, true }, { " g2g ", " I have to go ", null, true }, { " 2nite ", " tonight ", null, true },
			{ " l8", "late", null, true }, { " w8", " wait ", null, true }, { " m8", " mate", null, true }, { " i brb ", " I'll be right back ", null, true },
			{ " ibrb ", " I'll be right back ", null, true }, { " brb ", " I'll be right back ", null, true }, { " nvm ", " never mind ", null, true },
			{ " ppl ", " people ", null, true }, { " nm ", " never mind ", null, true }, { " tp ", " teleport ", null, true },
			{ " tpa ", " teleport ", null, true }, { " cuz ", " because ", null, true }, { " plz ", " please ", null, true },
			{ " ppl ", " people ", null, true }, { " thx ", " thanks ", null, true }, { " thnx ", " thanks ", null, true },
			{ " xmas ", " Christmas ", null, true }, { " becuz ", " because ", null, true }, { " sry ", " sorry ", null, true },
			{ " im ", " I'm ", null, true }, { " wont ", " won't ", null, true }, { " didnt ", " didn't ", null, true }, { " dont ", " don't ", null, true },
			{ " cant ", " can't ", null, true }, { " wouldnt ", " wouldn't ", null, true }, { " shouldnt ", " shouldn't ", null, true },
			{ " couldnt ", " couldn't ", null, true }, { " isnt ", " isn't ", null, true }, { " aint ", " ain't ", null, true },
			{ " doesnt ", " doesn't ", null, true }, { " youre ", " you're ", null, true }, { " hes ", " he's ", null, true },
			{ " shes ", " she's ", null, true }, { " could of ", " could have ", null, true }, { " should of ", "should have ", null, true },
			{ " would of ", " would have ", null, true }, { " itz ", " it's ", null, true }, { "wierd", "weird", null, true },
			{ "recieve", "receive", null, true }, { " blowed up ", " blew up ", null, true }, { " blowed it up ", " blew it up ", null, true },
			{ " RAM ", " \\RAM ", null, true }, { " NASA ", " \\NASA ", null, true }, { " Xbox LIVE ", " Xbox \\LIVE ", null, true },
			{ " AIDS ", " \\AIDS ", null, true }, { "!1", "!!", null, true }, { "\".", ".\"", "\"", false } };
	private HashMap<String, String> epithets_by_user = new HashMap<String, String>();
	private HashMap<String, ArrayList<String>> death_messages_by_cause = new HashMap<String, ArrayList<String>>(),
			default_death_messages = new HashMap<String, ArrayList<String>>();
	private HashMap<Player, String> message_beginnings = new HashMap<Player, String>(), command_beginnings = new HashMap<Player, String>();
	private static ArrayList<String> strings_to_correct = new ArrayList<String>(), corrected_strings = new ArrayList<String>(),
			unless_strings = new ArrayList<String>(), AFK_players = new ArrayList<String>(), players_who_have_accepted_the_rules = new ArrayList<String>(),
			players_who_have_read_the_rules = new ArrayList<String>(), login_messages = new ArrayList<String>(), logout_messages = new ArrayList<String>();
	private static ArrayList<Boolean> true_means_before = new ArrayList<Boolean>();
	private static String rules = "", default_epithet = "", default_message_format = "", say_format = "";
	private static boolean players_must_accept_rules = true, AutoCorrect_on = true, capitalize_first_letter = true, end_with_period = true,
			change_all_caps_to_italics = true, cover_up_profanities = true, insert_command_usages = true, true_username_required = true,
			display_death_messages = true;

	// TODO: the AutoCorrections go to default no matter what. Fix it.
	// TODO: fix first_letter_capitalization...AGAIN
	// TODO: set up config questions for true_username_required
	// TODO: fix abbreviation screwups
	// TODO: finish information parts: recipes, and potion recipes (/potion)
	// /potion with no parameters will tell you basics (splash potions need gunpowder, redstone extends time, etc.)
	// TODO: make all-caps-to-italics changes capitalize the first letter if the first letter is lowercase
	// TODO: make a HashMap for Bukkit commands and their usages for inserting command usages

	// plugin enable/disable and the command operator
	public void onEnable() {
		server = getServer();
		console = server.getConsoleSender();
		server.getPluginManager().registerEvents(this, this);
		loadTheEpithets(console);
		loadTheAutoCorrections(console);
		loadTheDeathMessages(console);
		loadTheLoginMessages(console);
		loadTheLogoutMessages(console);
		loadTheRules(console);
		// done enabling
		String enable_message = enable_messages[(int) (Math.random() * enable_messages.length)];
		console.sendMessage(ChatColor.BLUE + enable_message);
		for (Player player : server.getOnlinePlayers())
			if (player.isOp())
				player.sendMessage(ChatColor.BLUE + enable_message);
	}

	public void onDisable() {
		saveTheEpithets(console, true);
		saveTheAutoCorrectSettings(console, true);
		saveTheDeathMessages(console, true);
		saveTheLoginMessages(console, true);
		saveTheLogoutMessages(console, true);
		saveTheRules(console, true);
		// done disabling
		String disable_message = disable_messages[(int) (Math.random() * disable_messages.length)];
		console.sendMessage(ChatColor.BLUE + disable_message);
		for (Player player : server.getOnlinePlayers())
			if (player.isOp())
				player.sendMessage(ChatColor.BLUE + disable_message);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] my_parameters) {
		parameters = my_parameters;
		if ((command.equalsIgnoreCase("myScribe") || command.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("e") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("e")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheEpithets(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myScribe load" + ChatColor.RED + ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("a") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("a")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheAutoCorrections(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myScribe load" + ChatColor.RED + ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("d") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("d")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheDeathMessages(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myScribe load" + ChatColor.RED + ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("login")
						|| (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2].toLowerCase().startsWith("login"))
						|| (parameters.length > 2 && parameters[1].equalsIgnoreCase("log") && parameters[2].equalsIgnoreCase("in")) || (parameters.length > 3
						&& parameters[1].equalsIgnoreCase("the") && parameters[2].equalsIgnoreCase("log") && parameters[3].equalsIgnoreCase("in")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheLoginMessages(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myScribe load" + ChatColor.RED + ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("logout")
						|| (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2].toLowerCase().startsWith("logout"))
						|| (parameters.length > 2 && parameters[1].equalsIgnoreCase("log") && parameters[2].equalsIgnoreCase("out")) || (parameters.length > 3
						&& parameters[1].equalsIgnoreCase("the") && parameters[2].equalsIgnoreCase("log") && parameters[3].equalsIgnoreCase("out")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheLogoutMessages(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myScribe load" + ChatColor.RED + ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command.equalsIgnoreCase("mS"))
				&& parameters.length > 1
				&& parameters[0].equalsIgnoreCase("load")
				&& (parameters[1].toLowerCase().startsWith("r") || (parameters.length > 2 && parameters[1].equalsIgnoreCase("the") && parameters[2]
						.toLowerCase().startsWith("r")))) {
			if (!(sender instanceof Player) || sender.isOp())
				loadTheRules(sender);
			else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myScribe load" + ChatColor.RED + ".");
			return true;
		} else if ((command.equalsIgnoreCase("myScribe") || command.equalsIgnoreCase("mS")) && parameters.length > 0 && parameters[0].equalsIgnoreCase("load")) {
			if (!(sender instanceof Player) || sender.isOp()) {
				loadTheEpithets(sender);
				loadTheAutoCorrections(sender);
				loadTheDeathMessages(sender);
				loadTheLoginMessages(sender);
				loadTheLogoutMessages(sender);
				loadTheRules(sender);
			} else
				sender.sendMessage(ChatColor.RED + "Sorry, but you're not allowed to use " + ChatColor.BLUE + "/myScribe load" + ChatColor.RED + ".");
			return true;
		}
		// TODO add partial saves
		else if ((command.equalsIgnoreCase("myScribe") || command.equalsIgnoreCase("mS")) && parameters.length > 0 && parameters[0].equalsIgnoreCase("save")) {
			if (!(sender instanceof Player) || sender.hasPermission("myscribe.admin")) {
				saveTheEpithets(sender, true);
				saveTheAutoCorrectSettings(sender, true);
				saveTheDeathMessages(sender, true);
				saveTheLoginMessages(sender, true);
				saveTheLogoutMessages(sender, true);
				saveTheRules(sender, true);
			} else
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to use " + ChatColor.BLUE + "/myScribe save" + ChatColor.RED + ".");
			return true;
		} else if (command.equalsIgnoreCase("epithet")) {
			if (parameters.length > 0 && (!(sender instanceof Player) || sender.hasPermission("myscribe.epithet")))
				changeEpithet(sender);
			else if (parameters.length > 0)
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to change your epithet.");
			else
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what I should change your epithet to!");
			return true;
		} else if (command.equalsIgnoreCase("correct")) {
			if (parameters.length >= 2 && (!(sender instanceof Player) || sender.hasPermission("myscribe.correct") || sender.hasPermission("myscribe.admin")))
				addCorrection(sender);
			else if (sender instanceof Player && !sender.hasPermission("myscribe.correct"))
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to create your own AutoCorrections.");
			else
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what correction you want to make!");
			return true;
		} else if (command.equalsIgnoreCase("afklist") || (command.equalsIgnoreCase("afk") && parameters.length > 0 && parameters[0].equalsIgnoreCase("list"))) {
			AFKList(sender, false);
			return true;
		} else if (command.equalsIgnoreCase("afk") && parameters.length > 0) {
			AFKCheck(sender);
			return true;
		} else if (command.equalsIgnoreCase("afk")) {
			if (sender instanceof Player)
				AFKToggle(sender);
			else
				sender.sendMessage(ChatColor.RED + "You're a console! You can't be away from the keyboard! You're IN the computer!");
			return true;
		} else if (command.equalsIgnoreCase("rules")) {
			if (!rules.equals("")) {
				if (sender instanceof Player)
					players_who_have_read_the_rules.add(sender.getName());
				sender.sendMessage(colorCode(rules));
			} else
				sender.sendMessage(ChatColor.RED + "As I said, the rules haven't been written down yet, so you can't read them right now. Sorry.");
			return true;
		} else if (command.equalsIgnoreCase("accept")) {
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
		} else if (command.toLowerCase().startsWith("color") || command.equalsIgnoreCase("codes")) {
			sender.sendMessage(colorCode("&00 &11 &22 &33 &44 &55 &66 &77 &88 &99 &aa &bb &cc &dd &ee &ff &kk&f(k) &f&ll&f &mm&f &nn&f &oo"));
			return true;
		} else if (command.equalsIgnoreCase("say")) {
			if (!(sender instanceof Player) || sender.isOp()) {
				String message = "";
				for (String parameter : parameters)
					message = message + " " + parameter;
				server.broadcastMessage(colorCode(replace(say_format, "[message]", AutoCorrect(message), null, true, false)));
			} else
				sender.sendMessage(ChatColor.RED + "Only ops have permission to broadcast messages with " + ChatColor.BLUE + "/say" + ChatColor.RED + ".");
		} else if (command.equalsIgnoreCase("announce") || command.equalsIgnoreCase("declare") || command.equalsIgnoreCase("decree")) {
			// TODO
		} else if (command.equalsIgnoreCase("recipe") || command.equalsIgnoreCase("craft")) {
			if (parameters.length == 0)
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what item you want the recipe for!");
			else
				getRecipe(sender);
		} else if (command.equalsIgnoreCase("ids") || command.equalsIgnoreCase("id")) {
			id(sender);
			return true;
		} else if (command.equals("trade") || command.equals("exchange")) {
			// TODO
		}
		return false;
	}

	// intra-command methods
	private String AutoCorrect(String message) {
		if (AutoCorrect_on && !message.startsWith("http") && !message.startsWith("www.") && !message.startsWith("*") && !message.endsWith("*")) {
			while ((message.endsWith("/") && !message.endsWith(":/")) || (message.endsWith("\\") && !message.endsWith(":\\")) || message.endsWith(",")
					|| message.endsWith(">"))
				message = message.substring(0, message.length() - 1);
			// use spaces to make punctuation separate words
			if (message.length() > 1)
				for (int i = 1; i < message.length(); i++)
					if ((!message.substring(i, i + 1).toLowerCase().equals(message.substring(i, i + 1).toUpperCase())
							|| message.substring(i, i + 1).equals("0") || message.substring(i, i + 1).equals("1") || message.substring(i, i + 1).equals("2")
							|| message.substring(i, i + 1).equals("3") || message.substring(i, i + 1).equals("4") || message.substring(i, i + 1).equals("5")
							|| message.substring(i, i + 1).equals("6") || message.substring(i, i + 1).equals("7") || message.substring(i, i + 1).equals("8") || message
							.substring(i, i + 1).equals("9"))
							&& message.substring(i - 1, i).toLowerCase().equals(message.substring(i - 1, i).toUpperCase())
							&& !message.substring(i - 1, i).equals(" ")
							&& !message.substring(i - 1, i).equals("'")
							&& !message.substring(i - 1, i).equals("\\")
							&& !message.substring(i - 1, i).equals("-")
							&& !message.substring(i - 1, i).equals("_")
							&& !((message.substring(i - 1, i).equals(":") || message.substring(i - 1, i).equals(";") || message.substring(i - 1, i).equals("=")) && (i + 1 >= message
									.length() || message.substring(i + 1, i + 2).equals(" ")))
							&& !isColorCode(message.substring(i - 1, i + 1), null, null)
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
						message = message.substring(0, i) + " " + message.substring(i);
			message = " " + message + " ";
			// perform corrections
			for (int i = 0; i < strings_to_correct.size(); i++)
				message = replace(message, strings_to_correct.get(i), corrected_strings.get(i), unless_strings.get(i), true_means_before.get(i), true);
			String[] words = message.split(" ");
			// change all capital words to italics
			if (change_all_caps_to_italics) {
				for (int i = 0; i < words.length; i++) {
					// temporarily eliminate color codes
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
					// make the changes if, respectively, the word is longer
					// than one letter (or the word before or after it is
					// italicized); there are no underscores (found in usernames
					// or emoticons); it doesn't have a length of two and start
					// with or and with an X, a colon, a semicolon, or an equals
					// sign (emoticons); the words is all caps (of course); and
					// the
					// word is not in an abbreviation
					if ((temp.length() > 1 || (i > 0 && words[i - 1].toLowerCase().contains("&o")) || (i < words.length - 1 && words[i + 1].toLowerCase()
							.contains("&o")))
							&& !temp.contains("_")
							&& !temp.contains("\\")
							&& temp.equals(temp.toUpperCase())
							&& !temp.toLowerCase().equals(temp)
							&& (temp.length() != 2 || !(temp.startsWith("X") || temp.endsWith("X") || temp.startsWith(":") || temp.endsWith(":")
									|| temp.startsWith(";") || temp.endsWith(";") || temp.startsWith("=") || temp.endsWith("=")))
							&& (temp.length() != 3 || !(temp.startsWith(">X") || temp.startsWith(">:") || temp.startsWith(">;") || temp.startsWith(">=")))
							&& (words[i].length() > 1 || i == 0 || i == words.length || !(words[i - 1].endsWith(".") && words[i + 1].startsWith(".")))) {
						if (i == words.length - 1 && !temp.endsWith(".") && !temp.endsWith("!") && !temp.endsWith("?") && !temp.endsWith(".\"")
								&& !temp.endsWith("!\"") && !temp.endsWith("?\""))
							if (words[i].endsWith("\""))
								words[i] = words[i].substring(0, words[i].length() - 1) + "!\"";
							else
								words[i] = words[i] + "!";
						words[i] = "&o" + words[i].toLowerCase() + "%o";
						if (i > 0 && words[i - 1].length() == 1 && words[i - 1].toUpperCase().equals(words[i - 1])
								&& !words[i - 1].toLowerCase().equals(words[i - 1])) {
							words[i - 1] = "&o" + words[i - 1].toLowerCase() + "%o";
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
				message = replace(message, " ass ", " a&kss%k ", null, true, true);
				for (String profanity : profanities)
					message = replace(message, profanity, profanity.substring(0, 1) + "&k" + profanity.substring(1) + "%k", null, true, true);
			}
			message = replace(message, "./ ", "./", null, true, true);
			if (insert_command_usages)
				for (int i = 0; i < message.length() - 1; i++) {
					if (!message.contains("./"))
						break;
					if (message.substring(i, i + 2).equals("./")) {
						int end_index = i + 2;
						while (end_index < message.length())
							if (!message.substring(end_index, end_index + 1).toLowerCase().equals(message.substring(end_index, end_index + 1).toUpperCase()))
								end_index++;
							else
								try {
									Integer.parseInt(message.substring(end_index, end_index + 1));
									end_index++;
								} catch (NumberFormatException exception) {
									break;
								}
						console.sendMessage("\"" + message.substring(i + 2, end_index) + "\"");
						PluginCommand command = server.getPluginCommand(message.substring(i + 2, end_index).toLowerCase());
						if (command != null) {
							console.sendMessage("Found it.");
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
							console.sendMessage("message: \"" + message + "\"");
							message = replace(message, "./" + message.substring(i + 2, end_index), usage, null, true, true);
						}
					}
				}
			// eliminate extra spaces between letters and punctuation
			message = replace(replace(message, "... ", "...", null, true, true), "/ ", "/", null, true, true);
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
								&& !message.substring(i, i + 1).equals("\\") && !message.substring(i, i + 1).equals("+")
								&& !message.substring(i, i + 1).equals("/") && !(message.substring(i, i + 1).equals("\"") && quote_counter % 2 == 1))
							while (i > 0 && message.substring(i - 1, i).equals(" "))
								message = message.substring(0, i - 1) + message.substring(i);
						else if (message.substring(i, i + 1).equals("/")) {
							int end_index = i + 1;
							while (end_index < message.length()
									&& (!message.substring(end_index, end_index + 1).toUpperCase().equals(
											message.substring(end_index, end_index + 1).toLowerCase())
											|| message.substring(end_index, end_index + 1).equals("0")
											|| message.substring(end_index, end_index + 1).equals("1")
											|| message.substring(end_index, end_index + 1).equals("2")
											|| message.substring(end_index, end_index + 1).equals("3")
											|| message.substring(end_index, end_index + 1).equals("4")
											|| message.substring(end_index, end_index + 1).equals("5")
											|| message.substring(end_index, end_index + 1).equals("6")
											|| message.substring(end_index, end_index + 1).equals("7")
											|| message.substring(end_index, end_index + 1).equals("8") || message.substring(end_index, end_index + 1).equals(
											"9")))
								end_index++;
							// TODO: change the list of commands here to Bukkit_command_usages.keySet().contains(message.substring(i+1, end_index)
							if (server.getPluginCommand(message.substring(i + 1, end_index)) == null
									&& !(message.substring(i + 1, end_index).equalsIgnoreCase("version")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("plugins")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("reload")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("timings")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("tell")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("kill")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("me")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("help")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("?")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("kick")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("ban")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("banlist")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("pardon")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("ban-ip")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("pardon-ip")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("op")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("deop")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("tp")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("give")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("stop")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("save-all")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("save-off")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("save-on")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("list")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("say")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("whitelist")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("time")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("gamemode")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("xp")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("toggledownfall")
											|| message.substring(i + 1, end_index).equalsIgnoreCase("defaultgamemode") || message.substring(i + 1, end_index)
											.equalsIgnoreCase("seed")))
								while (i > 0 && message.substring(i - 1, i).equals(" "))
									message = message.substring(0, i - 1) + message.substring(i);
						} else if (message.substring(i, i + 1).equals("\"") || message.substring(i, i + 1).equals("(")
								|| message.substring(i, i + 1).equals("[") || message.substring(i, i + 1).equals("{"))
							while (i < message.length() - 1 && message.substring(i + 1, i + 2).equals(" "))
								if (i == message.length() - 2)
									message = message.substring(0, i + 1);
								else
									message = message.substring(0, i + 1) + message.substring(i + 2);
					}
				}
			}
			message = replace(message, "  ", " ", null, true, true);
			message = replace(replace(replace(message, "%o.", ".%o", null, true, true), "%o!", "!%o", null, true, true), "%o?", "?%o", null, true, true);
			while (message.length() >= 2 && isColorCode(message.substring(message.length() - 2), null, null))
				message = message.substring(0, message.length() - 2);
			// capitalize the first letter of every sentence if it is not a correction
			if (capitalize_first_letter && !message.startsWith("*") && !message.endsWith("*")) {
				message = "." + message;
				for (int i = 0; i < message.length(); i++) {
					String check_message = message.substring(i);
					while (check_message.startsWith(" "))
						check_message = check_message.substring(1);
					// locate terminal punctuation and make sure that the thing after them isn't an emoticon
					if ((message.substring(i, i + 1).equals(".") || message.substring(i, i + 1).equals("!") || message.substring(i, i + 1).equals("?"))
							&& !check_message.startsWith(":")
							&& !check_message.startsWith(";")
							&& !check_message.startsWith("=")
							&& !(check_message.length() >= 3 && check_message.substring(0, 1).equalsIgnoreCase(check_message.substring(2, 3)) && check_message
									.substring(0, 1).equalsIgnoreCase("o"))) {
						for (i = i; i < message.length() - 1; i++)
							// if it's not a letter or number, skip it
							if (message.substring(i, i + 1).toUpperCase().equals(message.substring(i, i + 1).toLowerCase())
									&& !message.substring(i, i + 1).equals("0") && !message.substring(i, i + 1).equals("1")
									&& !message.substring(i, i + 1).equals("2") && !message.substring(i, i + 1).equals("3")
									&& !message.substring(i, i + 1).equals("4") && !message.substring(i, i + 1).equals("5")
									&& !message.substring(i, i + 1).equals("6") && !message.substring(i, i + 1).equals("7")
									&& !message.substring(i, i + 1).equals("8") && !message.substring(i, i + 1).equals("9")) {
								// skip two characters if it's a color code
								if (i < message.length() - 2 && isColorCode(message.substring(i, i + 2), null, null))
									i++;
							}
							// both of these stop the loop, but if i=message.length+1, it means it found a "\" before the letter, so it should cancel
							// capitalization
							else if (message.substring(i, i + 1).equals("\\"))
								i = message.length() + 1;
							else
								break;
						// don't capitalize after an ellipsis or a "\" (with the presence of a "\" indicated with i==message.length()+1
						if (i < message.length() + 1 && (i < 3 || !message.substring(i - 3, i).equals("...")))
							if (i + 1 == message.length())
								message = message.substring(0, i) + message.substring(i, i + 1).toUpperCase();
							else
								message = message.substring(0, i) + message.substring(i, i + 1).toUpperCase() + message.substring(i + 1);
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
							|| (message.length() >= 2 && (message.substring(message.length() - 2, message.length() - 1).equals(":")
									|| message.substring(message.length() - 2, message.length() - 1).equals("=") || message.substring(message.length() - 2,
									message.length() - 1).equals(";"))) || message.toUpperCase().endsWith("XD") || message.endsWith("<3") || (message.length() >= 3
							&& message.substring(message.length() - 1).equalsIgnoreCase(message.substring(message.length() - 3, message.length() - 2)) && (message
							.toLowerCase().endsWith("o")
							|| message.toLowerCase().endsWith("t") || message.endsWith("-")))) && !message.endsWith("\\"))
				if (!message.endsWith("\""))
					message = message + "%k.";
				else
					message = message.substring(0, message.length() - 1) + "%k.\"";
		}
		// get rid of all color codes immediately followed by an anti color code
		// of the same kind
		for (String color_code_char : color_color_code_chars) {
			message = replace(message, "%" + color_code_char + "&" + color_code_char, "", null, true, true);
			message = replace(message, "&" + color_code_char + "%" + color_code_char, "", null, true, true);
			message = replace(message, "%" + color_code_char + " &" + color_code_char, " ", null, true, true);
			message = replace(message, "&" + color_code_char + " %" + color_code_char, " ", null, true, true);
		}
		for (String color_code_char : formatting_color_code_chars) {
			message = replace(message, "%" + color_code_char + "&" + color_code_char, "", null, true, true);
			message = replace(message, "&" + color_code_char + "%" + color_code_char, "", null, true, true);
			message = replace(message, "%" + color_code_char + " &" + color_code_char, " ", null, true, true);
			message = replace(message, "&" + color_code_char + " %" + color_code_char, " ", null, true, true);
		}
		return replace(message, "\\", "", "\\", false, true);
	}

	private static String colorCode(String text) {
		text = "&f" + text;
		// put color codes in the right order if they're next to each other
		for (int i = 0; i < text.length() - 3; i++)
			if (isColorCode(text.substring(i, i + 2), false, true) && isColorCode(text.substring(i + 2, i + 4), true, true))
				text = text.substring(0, i) + text.substring(i + 2, i + 4) + text.substring(i, i + 2) + text.substring(i + 4);
		// replace all anti color codes with non antis
		String current_color_code = "";
		for (int i = 0; i < text.length() - 1; i++) {
			if (isColorCode(text.substring(i, i + 2), null, true))
				current_color_code = current_color_code + text.substring(i, i + 2);
			else if (isColorCode(text.substring(i, i + 2), null, false)) {
				while (text.length() > i + 2 && isColorCode(text.substring(i, i + 2), null, false)) {
					current_color_code = replace(current_color_code, "&" + text.substring(i + 1, i + 2), "", null, true, true);
					if (current_color_code.equals(""))
						current_color_code = "&f";
					text = text.substring(0, i) + text.substring(i + 2);
				}
				text = text.substring(0, i) + current_color_code + text.substring(i);
			}
		}
		String colored_text = ChatColor.translateAlternateColorCodes('&', text);
		return colored_text;
	}

	private static Boolean isColorCode(String text, Boolean true_non_formatting_null_either, Boolean true_non_anti_null_either) {
		if (!text.startsWith("&") && !text.startsWith("%"))
			return false;
		if (true_non_anti_null_either != null)
			if (true_non_anti_null_either && text.startsWith("%"))
				return false;
			else if (!true_non_anti_null_either && text.startsWith("&"))
				return false;
		if (true_non_formatting_null_either == null || true_non_formatting_null_either)
			for (String color_color_code_char : color_color_code_chars)
				if (text.substring(1, 2).equalsIgnoreCase(color_color_code_char))
					return true;
		if (true_non_formatting_null_either == null || !true_non_formatting_null_either)
			for (String formatting_color_code_char : formatting_color_code_chars)
				if (text.substring(1, 2).equalsIgnoreCase(formatting_color_code_char))
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
			if (event.getMessage().toLowerCase().contains("shut up") || event.getMessage().toLowerCase().contains("stfu"))
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
			if (event.getMessage().endsWith("[...]")) {
				command_beginnings.put(event.getPlayer(), command_beginning + event.getMessage().substring(0, event.getMessage().length() - 5));
				event.getPlayer().sendMessage(ChatColor.BLUE + "You may continue typing.");
			} else
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
				String full_chat_message = event.getMessage();
				if (message_beginnings.get(event.getPlayer()) != null) {
					full_chat_message = message_beginnings.get(event.getPlayer()) + event.getMessage();
					message_beginnings.remove(event.getPlayer());
				}
				server.broadcastMessage(colorCode(replace(replace(replace(default_message_format, "[player]", event.getPlayer().getName(), null, true, true),
						"[epithet]", epithet, null, true, false), "[message]", AutoCorrect(full_chat_message), null, true, false)));
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
				&& !event.getMessage().toLowerCase().startsWith("/rules") && !event.getMessage().toLowerCase().startsWith("/accept")) {
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
				for (int i = 0; i < default_corrections.length; i++) {
					strings_to_correct.add((String) default_corrections[i][0]);
					corrected_strings.add((String) default_corrections[i][1]);
					unless_strings.add((String) default_corrections[i][2]);
					if ((Boolean) default_corrections[i][3])
						true_means_before.add(true);
					else
						true_means_before.add(false);
				}
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
					capitalize_first_letter = getResponse(sender, save_line.substring(67), in.readLine(), "Right now, first letter capitalization is enabled.");
				else if (save_line.equals("Would you like me to put periods at the end of any sentences that have no terminal punctuation?"))
					end_with_period = getResponse(sender, save_line.substring(95), in.readLine(), "Right now, period addition is enabled.");
				else if (save_line.equals("Would you like me to change any all-caps words or sentences to lowercase italics?"))
					change_all_caps_to_italics =
							getResponse(sender, save_line.substring(81), in.readLine(), "Right now, caps to italics conversion is enabled.");
				else if (save_line.equals("Would you like me to cover up profanities using magic (meaning the &k color code, not fairy dust)?"))
					cover_up_profanities = getResponse(sender, save_line.substring(98), in.readLine(), "Right now, profanity coverup is enabled.");
				else if (save_line.equals("Would you like me to replace all in-text commands with their usages when they start with a \"./\"?"))
					insert_command_usages = getResponse(sender, save_line.substring(96), in.readLine(), "Right now, command usage insertion is enabled.");
				else if (save_line.length() > 8 && save_line.substring(0, 8).equalsIgnoreCase("change: ")) {
					String string_to_correct = save_line.substring(8);
					if (string_to_correct.startsWith("\"") && string_to_correct.endsWith("\"") && string_to_correct.length() > 1)
						string_to_correct = string_to_correct.substring(1, string_to_correct.length() - 1);
					save_line = in.readLine();
					already_progressed = true;
					if (save_line != null && save_line.length() >= 4 && save_line.substring(0, 4).equals("to: ")) {
						strings_to_correct.add(string_to_correct);
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
							save_line = in.readLine();
							if (save_line != null && save_line.length() >= 12 && save_line.substring(0, 12).equalsIgnoreCase("comes before")) {
								unless_strings.add(unless_string);
								true_means_before.add(true);
								save_line = in.readLine();
							} else if (save_line != null && save_line.length() >= 11 && save_line.substring(0, 11).equalsIgnoreCase("comes after")) {
								unless_strings.add(unless_string);
								true_means_before.add(false);
								save_line = in.readLine();
							} else {
								unless_strings.add(null);
								true_means_before.add(true);
							}
						} else {
							unless_strings.add(null);
							true_means_before.add(true);
						}
					}
				}
				if (!already_progressed)
					save_line = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "The AutoCorrections.txt I created a few milliseconds ago doesn't exist. -_-");
			exception.printStackTrace();
			failed = true;
		} catch (IOException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "I got an IOException while trying to save your AutoCorrections.");
			exception.printStackTrace();
			failed = true;
		}
		if (!failed) {
			if (strings_to_correct.size() == 0) {
				for (int i = 0; i < default_corrections.length; i++) {
					strings_to_correct.add((String) default_corrections[i][0]);
					corrected_strings.add((String) default_corrections[i][1]);
					unless_strings.add((String) default_corrections[i][2]);
					if ((Boolean) default_corrections[i][3])
						true_means_before.add(true);
					else
						true_means_before.add(false);
				}
			}
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
				death_messages_by_cause = default_death_messages;
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
					display_death_messages =
							getResponse(sender, save_line.substring(63), in.readLine(), "Right now, hilarious death messages will appear when someone dies.");
				save_line = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException exception) {
			sender.sendMessage(ChatColor.DARK_RED + "The death messages.txt I created a few milliseconds ago doesn't exist. -_-");
			exception.printStackTrace();
			failed = true;
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
			failed = true;
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
						players_listed[players_listed.length - 1] =
								players_listed[players_listed.length - 1].substring(4, players_listed[players_listed.length - 1].length() - 29);
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
			failed = true;
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
				for (int i = 0; i < default_corrections.length; i++) {
					strings_to_correct.add((String) default_corrections[i][0]);
					corrected_strings.add((String) default_corrections[i][1]);
					unless_strings.add((String) default_corrections[i][2]);
					if ((Boolean) default_corrections[i][3])
						true_means_before.add(true);
					else
						true_means_before.add(false);
				}
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
			if (death_messages_by_cause.size() == 0)
				sender.sendMessage(ChatColor.BLUE + "You don't actually have any death messages to save.");
			else if (death_messages_by_cause.size() == 1)
				sender.sendMessage(ChatColor.BLUE + "Your server's only death message has been saved.");
			else
				sender.sendMessage(ChatColor.BLUE + "Your server's " + death_messages_by_cause.size() + " death messages have been saved.");
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
			if (true_username_required && player != null && !player.hasPermission("myscribe.admin")) {
				epithet_is_acceptable =
						epithet.length() > 0 && !epithet.contains("&k") && epithet.replaceAll("(&+([a-fA-Fk-oK-OrR0-9]))", "").contains(player.getName());
			}
			if ((epithet_is_acceptable && player.getName().equals(owner)) || player.hasPermission("myscribe.admin")) {
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
		if (recipes[id] != null)
			sender.sendMessage(colorCode(recipes[id]));
		else if (item_IDs[id] != null)
			sender.sendMessage(ChatColor.BLUE + "You can't craft a " + item_IDs[id][0] + "!");
		else if (query.toLowerCase().startsWith("a") || query.toLowerCase().startsWith("e") || query.toLowerCase().startsWith("i")
				|| query.toLowerCase().startsWith("o") || query.toLowerCase().startsWith("u"))
			sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what an \"" + query + "\" is.");
		else
			sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what a \"" + query + "\" is.");
	}

	private void id(CommandSender sender) {
		if (parameters.length == 0 || parameters[0].equalsIgnoreCase("this") || parameters[0].equalsIgnoreCase("that"))
			if (sender instanceof Player) {
				int id = ((Player) sender).getTargetBlock(null, 1024).getTypeId();
				if (item_IDs[id][0] != null)
					if (item_IDs[id][0].endsWith("s") && !item_IDs[id][0].endsWith("ss"))
						sender.sendMessage(ChatColor.BLUE + "Those " + item_IDs[id][0] + " you're pointing at have the I.D. " + id + ".");
					else
						sender.sendMessage(ChatColor.BLUE + "That " + item_IDs[id][0] + " you're pointing at has the I.D. " + id + ".");
				else
					sender.sendMessage(ChatColor.RED + "Uh...what in the world " + ChatColor.ITALIC + "is" + ChatColor.RED + " that thing you're pointing at?");
				id = ((Player) sender).getItemInHand().getTypeId();
				if (item_IDs[id][0] != null)
					if (item_IDs[id][0].endsWith("s") && !item_IDs[id][0].endsWith("ss"))
						sender.sendMessage(ChatColor.BLUE + "Those " + item_IDs[id][0] + " you're holding have the I.D. " + id + ".");
					else
						sender.sendMessage(ChatColor.BLUE + "That " + item_IDs[id][0] + " you're holding has the I.D. " + id + ".");
				else
					sender.sendMessage(ChatColor.RED + "Uh...what in the world " + ChatColor.ITALIC + "is" + ChatColor.RED + " that thing you're holding?");
			} else
				sender.sendMessage(ChatColor.RED + "You forgot to tell me what item or I.D. you want identified!");
		else {
			String query = "";
			for (String parameter : parameters)
				if (query.equals(""))
					query = parameter;
				else
					query = query + " " + parameter;
			try {
				int id = Integer.parseInt(query);
				if (item_IDs[id] != null)
					if (item_IDs[id][0].endsWith("s") && !item_IDs[id][0].endsWith("ss"))
						sender.sendMessage(ChatColor.BLUE + item_IDs[id][0].substring(0, 1).toUpperCase() + item_IDs[id][0].substring(1) + " have the I.D. "
								+ id + ".");
					else
						sender.sendMessage(ChatColor.BLUE + item_IDs[id][0].substring(0, 1).toUpperCase() + item_IDs[id][0].substring(1) + " has the I.D. "
								+ id + ".");
				else
					sender.sendMessage(ChatColor.RED + "No item has the I.D. " + id + ".");
			} catch (NumberFormatException exception) {
				String item_name = null;
				int id = -1;
				// complete the item name
				for (int i = 0; i < item_IDs.length; i++)
					if (item_IDs[i] != null)
						for (String my_item_name : item_IDs[i])
							if (my_item_name.toLowerCase().startsWith(query.toLowerCase()))
								// Length of the name of the item is used to determine the closest match. This ensures that searching "flint", for example, will
								// return "flint" instead of "flint and steel" even though "flint and steel" is found first since it has a smaller item I.D.
								if (item_name == null || my_item_name.length() < item_name.length()) {
									item_name = item_IDs[i][0];
									id = i;
								}
				// if it found it, send the message
				if (item_name != null) {
					if (item_name.endsWith("s") && !item_name.endsWith("ss"))
						sender.sendMessage(ChatColor.BLUE + item_name.substring(0, 1).toUpperCase() + item_name.substring(1) + " have the I.D. " + id + ".");
					else
						sender.sendMessage(ChatColor.BLUE + item_name.substring(0, 1).toUpperCase() + item_name.substring(1) + " has the I.D. " + id + ".");
					return;
				}
				if (query.toLowerCase().startsWith("a") || query.toLowerCase().startsWith("e") || query.toLowerCase().startsWith("i")
						|| query.toLowerCase().startsWith("o") || query.toLowerCase().startsWith("u"))
					sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what an \"" + query + "\" is.");
				else
					sender.sendMessage(ChatColor.RED + "Sorry, but I don't know what a \"" + query + "\" is.");
			}
		}
	}
}
