package cc.kickbyte.condexp;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expansion extends PlaceholderExpansion {

    private static final Pattern PATTERN = Pattern.compile("^(!?)\\{(.*)}\\?\\{(.*)}:\\{(.*)}$");
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
        return "1.0.3";
    }

    // %cond_{^player_name^==Notch}?{true}:{false}%
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        Matcher matcher = PATTERN.matcher(params);
        if (matcher.matches()) {
            String prefix = matcher.group(1);
            boolean inverse = prefix != null && !prefix.isEmpty() && prefix.charAt(0) == '!';

            String condition = matcher.group(2);
            String trueValue = matcher.group(3);
            String falseValue = matcher.group(4);

            condition = placeholders(player, condition);

            boolean result = checker.check(condition);
            if (inverse) result = !result;

            // Повторение - мать учения. :D
            return placeholders(player, result ? trueValue : falseValue);
        }

        return null;
    }

    private String placeholders(OfflinePlayer player,String input) {
        return PlaceholderAPI.setPlaceholders(player, input.replace("^", "%"));
    }

}