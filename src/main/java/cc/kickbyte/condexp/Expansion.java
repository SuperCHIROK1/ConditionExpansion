package cc.kickbyte.condexp;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expansion extends PlaceholderExpansion {

    private static final Pattern PATTERN = Pattern.compile("^\\{(.*)}\\?\\{(.*)}:\\{(.*)}$");
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
        return "1.0.0";
    }

    // %cond_{^player_name^==Notch}?{true}:{false}%
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        Matcher matcher = PATTERN.matcher(params);
        if (matcher.matches()) {
            String condition = matcher.group(1);
            String trueValue = matcher.group(2);
            String falseValue = matcher.group(3);

            condition = PlaceholderAPI.setPlaceholders(player, condition.replace("^", "%"));
            return checker.check(condition) ? trueValue : falseValue;
        }

        return null;
    }

}