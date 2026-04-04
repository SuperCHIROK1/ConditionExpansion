package cc.kickbyte.condexp;

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

    private static final ValueChecker checker = new ValueChecker();

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
        return "1.1.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        String prefix = Utils.getPlaceholderPrefix(params);

        return switch (prefix) {
            case "fallback", "fb" -> processFallback(player, params, prefix);
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

        return null;
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

        return null;
    }

}