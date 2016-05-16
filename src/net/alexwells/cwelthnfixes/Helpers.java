package net.alexwells.cwelthnfixes;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Helpers {

    public static Location stringToLoc(String str) {
        String[] strings = str.split(":");

        Location loc = new Location(Bukkit.getWorld(strings[0]), 0, 0, 0);
        loc.setX(Double.parseDouble(strings[1]));
        loc.setY(Double.parseDouble(strings[2]));
        loc.setZ(Double.parseDouble(strings[3]));

        return loc;
    }

    public static String locToString(Location loc) {
        String[] strings = {
                loc.getWorld().getName(),
                String.valueOf(loc.getBlockX()),
                String.valueOf(loc.getBlockY()),
                String.valueOf(loc.getBlockZ())
        };

        return strJoin(strings, ":");
    }

    /*
        In case we don't use Java 8
     */
    public static String strJoin(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);

            sbStr.append(aArr[i]);
        }

        return sbStr.toString();
    }

}
