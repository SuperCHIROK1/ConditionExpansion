package cc.kickbyte.condexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueChecker {

    private static final Pattern PATTERN = Pattern.compile("^\\s*(.+?)\\s*(==|!=|>=|<=|>|<|==!|!==!)\\s*(.+?)\\s*$");

    public boolean check(String input) {
        Matcher matcher = PATTERN.matcher(input);
        if (matcher.matches()) {

            String left = matcher.group(1);
            String operator = matcher.group(2);
            String right = matcher.group(3);
            if (right.equals("NULLABLE")) {
                return left.isEmpty();
            }
            try {
                return switch (operator) {
                    case "==" -> left.equals(right);
                    case "!=" -> !left.equals(right);
                    case "<=" -> parse(left) <= parse(right);
                    case ">=" -> parse(left) >= parse(right);
                    case "<" -> parse(left) < parse(right);
                    case ">" -> parse(left) > parse(right);
                    case "==!" -> left.equalsIgnoreCase(right);
                    case "~~=" -> left.contains(right);
                    case "$=" -> left.startsWith(right);
                    case "#=" -> left.endsWith(right);
                    default -> false;
                };
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private double parse(String string) {
        return Double.parseDouble(string);
    }

}
