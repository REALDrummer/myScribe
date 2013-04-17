package REALDrummer;

public class AutoCorrection {
	public String to_correct, to_correct_to, unless, save_line, target = null;
	public boolean before, case_sensitive;

	public AutoCorrection(String my_to_correct, String my_to_correct_to, String my_unless, boolean my_before, String my_target, boolean my_case_sensitive) {
		to_correct = my_to_correct;
		to_correct_to = my_to_correct_to;
		unless = my_unless;
		before = my_before;
		target = my_target;
		case_sensitive = my_case_sensitive;
		// construct the save line
		save_line = "Change \"" + to_correct + "\" ";
		if (case_sensitive)
			save_line += "(case-sensitive) ";
		save_line += "to \"" + to_correct_to + "\"";
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

	public AutoCorrection(String to_correct, String to_correct_to, String unless, boolean before, String target) {
		new AutoCorrection(to_correct, to_correct_to, unless, before, target, false);
	}

	public AutoCorrection(String to_correct, String to_correct_to, String unless, boolean before, boolean case_sensitive) {
		new AutoCorrection(to_correct, to_correct_to, unless, before, null, case_sensitive);
	}

	public AutoCorrection(String to_correct, String to_correct_to, String unless, boolean before) {
		new AutoCorrection(to_correct, to_correct_to, unless, before, null, false);
	}

	public AutoCorrection(String to_correct, String to_correct_to, String target) {
		new AutoCorrection(to_correct, to_correct_to, null, true, target, false);
	}

	public AutoCorrection(String to_correct, String to_correct_to, boolean case_sensitive) {
		new AutoCorrection(to_correct, to_correct_to, null, true, null, case_sensitive);
	}

	public AutoCorrection(String to_correct, String to_correct_to) {
		new AutoCorrection(to_correct, to_correct_to, null, true, null, false);
	}

	public AutoCorrection(String my_save_line) {
		// Change "[to correct]" ("(case-sensitive)") to "[to correct to]" (unless "[unless]" comes ["before"/after"]) (for [player]).
		save_line = my_save_line;
		// TODO TEMP
		save_line.contains("(test");
		if (save_line.contains("//(case-sensitive//)")) {
			case_sensitive = true;
			to_correct = save_line.substring(8, save_line.indexOf("\" (case-sensitive) to \""));
		} else {
			case_sensitive = false;
			to_correct = save_line.substring(8, save_line.indexOf("\" to \""));
		}
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
		// if the second to last word is "for", it means it's for a specific target
		if (temp[temp.length - 2].equals("for")) {
			// use .substring() to eliminate the period
			target = temp[temp.length - 1].substring(0, temp[temp.length - 1].length() - 1);
		}
	}
}
