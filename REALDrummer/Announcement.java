package REALDrummer;

import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.ChatColor;

public class Announcement {
	public String announcement, creator, save_line, save_line1, save_line2;
	public long time_created;
	public ArrayList<String> players_who_have_read_it;
	public Boolean is_important;
	public byte is_important_index;

	public Announcement(String my_announcement, String my_creator, ArrayList<String> my_players_who_have_read_it, Boolean my_is_important) {
		announcement = my_announcement;
		creator = my_creator;
		time_created = Calendar.getInstance().getTimeInMillis();
		players_who_have_read_it = my_players_who_have_read_it;
		is_important = my_is_important;
		if (is_important == null)
			is_important_index = 1;
		else if (is_important)
			is_important_index = 0;
		else
			is_important_index = 2;
		// construct the save line
		save_line1 = "At time = " + time_created + ", " + creator + " made an ";
		if (is_important != null && is_important)
			save_line1 = save_line1 + "important ";
		else if (is_important != null)
			save_line1 = save_line1 + "unimportant ";
		save_line1 = save_line1 + " announcement: \"" + announcement + "\".";
		if (players_who_have_read_it.size() == 0)
			save_line2 = "No one has read the announcement.";
		else if (players_who_have_read_it.size() == 1)
			save_line2 = players_who_have_read_it.get(0) + " is the only one who has read the announcement.";
		else if (players_who_have_read_it.size() == 2)
			save_line2 = players_who_have_read_it.get(0) + " and " + players_who_have_read_it.get(1) + " have read the announcement.";
		else {
			save_line2 = "";
			for (int i = 0; i < players_who_have_read_it.size(); i++) {
				if (i > 0)
					save_line2 = save_line2 + ", ";
				if (i == players_who_have_read_it.size() - 1)
					save_line2 = save_line2 + "and ";
				save_line2 = save_line2 + players_who_have_read_it.get(i);
			}
			save_line2 = save_line2 + " have read the announcement.";
		}
		save_line = save_line1 + " " + save_line2;
	}

	public Announcement(String my_save_line) {
		// At time = [time_created], [creator] made an ("unimportant"/"important") announcement: "[announcement]".
		// save line part 2 options:
		// for 0 players: No one has read the announcement.
		// for 1 player: [player] is the only one who has read the announcement.
		// for 2 players: [player1] and [player2] have read the announcement.
		// for +2 players: [player1], [player2], [...], and [playern] have read the announcement.
		save_line = my_save_line;
		save_line1 = save_line.substring(save_line.substring(save_line.length() - 2).lastIndexOf("\".") + 2);
		save_line2 = save_line.substring(save_line.length() + 1);
		try {
			time_created = Long.parseLong(save_line1.substring(save_line1.indexOf("time = ") + 7, save_line1.indexOf(",")));
		} catch (NumberFormatException exception) {
			myScribe.tellOps(ChatColor.DARK_RED + "I couldn't read the time this announcement was created!", true);
			myScribe.tellOps(ChatColor.DARK_RED + "This is the save line of the announcement: " + ChatColor.WHITE + save_line1 + ChatColor.DARK_RED + ".", true);
			exception.printStackTrace();
			return;
		}
		creator = save_line1.substring(save_line1.indexOf(", ") + 2, save_line1.indexOf(" made an "));
		is_important = null;
		if (save_line1.substring(save_line1.indexOf(" made an ") + 9, save_line1.indexOf("announcement: ")).contains("important"))
			if (save_line1.substring(save_line1.indexOf(" made an ") + 9, save_line1.indexOf("announcement: ")).contains("unimportant"))
				is_important = false;
			else
				is_important = true;
		if (is_important == null)
			is_important_index = 1;
		else if (is_important)
			is_important_index = 0;
		else
			is_important_index = 2;
		announcement = save_line1.substring(save_line1.indexOf("announcement: ") + 15, save_line1.length() - 2);
		players_who_have_read_it = new ArrayList<String>();
		if (save_line2.startsWith("No one"))
			return;
		else if (!save_line2.contains(" and "))
			players_who_have_read_it.add(save_line2.substring(0, save_line2.indexOf(" ")));
		else if (!save_line2.contains(",")) {
			players_who_have_read_it.add(save_line2.split(" ")[0]);
			players_who_have_read_it.add(save_line2.split(" ")[2]);
		} else {
			String[] temp = save_line2.split(", ");
			for (int i = 0; i < temp.length - 1; i++)
				players_who_have_read_it.add(temp[i]);
			players_who_have_read_it.add(temp[temp.length - 1].substring(4, save_line2.substring(4).indexOf(" ")));
		}
		// TEMPORARY
		myScribe.console.sendMessage("time created = " + time_created + "; creator: \"" + creator + "\"; is important: " + is_important.toString() + "; announcement: \"" + announcement + "\"");
		myScribe.console.sendMessage("players who have read it:");
		for (String player : players_who_have_read_it)
			myScribe.console.sendMessage("\"" + player + "\"");
		// END TEMPORARY
	}
}
