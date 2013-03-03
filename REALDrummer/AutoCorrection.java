package REALDrummer;

public class AutoCorrection {
	public String to_correct, to_correct_to, unless, save_line, target = null;
	public boolean before;

	public AutoCorrection(String my_to_correct, String my_to_correct_to, String my_unless, boolean my_before, String my_target) {
		to_correct = my_to_correct;
		to_correct_to = my_to_correct_to;
		unless = my_unless;
		before = my_before;
		target = my_target;
		// construct the save line
		save_line = "Change \"" + to_correct + "\" to \"" + to_correct_to + "\"";
		if (unless != null) {
			save_line += " unless \"" + unless + "\" comes ";
			if (before)
				save_line += "before";
			else
				save_line += "after";
		}
		if (target != null)
			save_line += " for " + target;
		save_line += ".";
	}

	public AutoCorrection(String my_save_line) {
		// Change "[to correct]" to "[to correct to]" (unless "[unless]" comes ["before"/after"]) (for [player]).
		save_line = my_save_line;
		to_correct = save_line.substring(8, save_line.indexOf("\" to \""));
		if (save_line.contains("\" unless \"")) {
			to_correct_to = save_line.substring(to_correct.length() + 14, save_line.indexOf("\" unless \""));
			String[] temp = save_line.split("\" comes ");
			unless = temp[0].substring(to_correct.length() + to_correct_to.length() + 24);
			if (temp[1].endsWith("before"))
				before = true;
			else
				before = false;
		} else {
			to_correct_to = save_line.substring(to_correct.length() + 14, save_line.length() - 2);
			unless = null;
			before = true;
		}
		String[] temp = save_line.split(" ");
		// if the second to last word is "for", it means it's an individual player
		if (temp[temp.length - 2].equals("for")) {
			// use .substring() to eliminate the period
			target = temp[temp.length - 1].substring(0, temp[temp.length - 1].length() - 1);
		}
	}
}
