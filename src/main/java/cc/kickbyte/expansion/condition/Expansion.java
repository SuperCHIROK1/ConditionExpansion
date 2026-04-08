package cc.kickbyte.expansion.condition;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expansion extends PlaceholderExpansion {

    private static final Pattern DEFAULT_PATTERN =
            Pattern.compile("^(!?)\\{(.*)}\\?\\{(.*)}:\\{(.*)}$");
    private static final Pattern FALLBACK_PATTERN =
            Pattern.compile("^\\{(.*);(.*)}$");
    private static final Pattern SWITCH_PATTERN =
            Pattern.compile("^\\{(.*)}_\\{(.*)}$");
    private static final Pattern CASE_PATTERN =
            Pattern.compile("^\\s*(.*?)\\s*==\\s*(.*)\\s*$");

    private final ValueChecker checker = new ValueChecker();

    @Override
    public @NotNull String getIdentifier() {
        return "cond";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SuperCHIROK1";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        String prefix = Utils.getPlaceholderPrefix(params);

        return switch (prefix) {
            case "fallback", "fb" -> processFallback(player, params, prefix);
            case "switch" -> processSwitch(player, params);
            default -> processDefault(player, params);
        };
    }

    private String processFallback(OfflinePlayer player, String params, String prefix) {
        params = Utils.parsePlaceholders(player, params.substring(prefix.length()+1));
        Matcher matcher = FALLBACK_PATTERN.matcher(params);
        if (matcher.matches()) {
            String value = matcher.group(1);
            String replacement = matcher.group(2);

            return value.isEmpty() ? replacement : value;
        }

        return params;
    }

    private String processDefault(OfflinePlayer player, String params) {
        params = Utils.parsePlaceholders(player, params);
        Matcher matcher = DEFAULT_PATTERN.matcher(params);
        if (matcher.matches()) {
            String prefix = matcher.group(1);
            boolean inverse = prefix != null && !prefix.isEmpty() && prefix.charAt(0) == '!';

            String condition = matcher.group(2);
            String trueValue = matcher.group(3);
            String falseValue = matcher.group(4);

            boolean result = checker.check(condition);
            if (inverse) result = !result;

            return result ? trueValue : falseValue;
        }

        return params;
    }

    private String processSwitch(OfflinePlayer player, String params) {
        params = Utils.parsePlaceholders(player, params.substring(7));
        Matcher matcher = SWITCH_PATTERN.matcher(params);
        if (!matcher.matches()) return params;

        String input = matcher.group(1);
        String[] cases = matcher.group(2).split(";");

        String defaultValue = "";
        for (String impl : cases) {
            Matcher m = CASE_PATTERN.matcher(impl);
            if (m.matches()) {
                if (m.group(1).trim().equals(input)) {
                    return m.group(2);
                }
            } else {
                defaultValue = impl;
            }
        }

        return defaultValue;
    }

}