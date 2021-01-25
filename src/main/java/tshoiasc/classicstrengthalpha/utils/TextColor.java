package tshoiasc.classicstrengthalpha.utils;

public class TextColor {
    public static String getColorCode(int percent) {
        if (percent <= 30) {
            return "§b§l";
        }
        if (percent <= 60)
            return "§d§l";

        if (percent <= 80)
            return "§e§l";
        else
            return "§6§l";

    }
}
