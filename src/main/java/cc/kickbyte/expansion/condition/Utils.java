package cc.kickbyte.expansion.condition;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

@UtilityClass
public class Utils {

    public char PLACEHOLDER_CHAR = '^';

    public String parsePlaceholders(OfflinePlayer player, String input) {
        if (!containsPlaceholdersChar(input)) return input;
        input = input.replace(PLACEHOLDER_CHAR, '%');
        return PlaceholderAPI.setPlaceholders(player, input);
    }

    public boolean containsPlaceholdersChar(String input) {
        return input.indexOf(PLACEHOLDER_CHAR) != -1;
    }

    public String getPlaceholderPrefix(String input) {
        int index = input.indexOf('_');
        if (index != -1) return input.substring(0, index);
        return "default";
    }

}
