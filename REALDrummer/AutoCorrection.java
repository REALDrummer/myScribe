package REALDrummer;

public class AutoCorrection {
    public String to_correct, to_correct_to, unless, target = null;
    public boolean before, case_sensitive;

    public AutoCorrection(String to_correct, String to_correct_to, String unless, boolean before, String target, boolean case_sensitive) {
        this.to_correct = to_correct;
        this.to_correct_to = to_correct_to;
        this.unless = unless;
        this.before = before;
        this.target = target;
        this.case_sensitive = case_sensitive;
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

    public AutoCorrection(String save_line) {
        // Change "[to correct]" ("(case-sensitive)") to "[to correct to]" (unless "[unless]" comes ["before"/after"]) (for [player]).
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

    @Override
    public String toString() {
        String save_line = "Change \"" + to_correct + "\" ";
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
        return save_line + ".";
    }
}
