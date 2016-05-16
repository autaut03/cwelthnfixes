package net.alexwells.cwelthnfixes;
import lib.PatPeter.SQLibrary.Database;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class HomesRepository {

    private HashMap<Player, Location> homes = new HashMap<>();

    private Database db;

    public HomesRepository() {
        db = Main.instance().database();
    }

    public void set(Player player, Location loc) {
        String playerName = player.getName();
        String locString = Helpers.locToString(loc);

        try {
            // if there's a player in cache, then we're sure it is also in database
            if(has(player)) {
                db.query("UPDATE homes SET " +
                        "`location` = '" + locString + "' " +
                        "WHERE `player_name` = '" + playerName + "';");
            } else {
                db.query("INSERT INTO homes " +
                        "(`player_name`, `location`) VALUES " +
                        "('" + playerName + "', '" + locString + "');");
            }

            homes.put(player, loc);
        } catch (SQLException e) {

        }
    }

    public Location get(Player player) {
        return homes.get(player);
    }

    public boolean has(Player player) {
        return get(player) != null;
    }

    /*
        Loading all the homes to a cache.
     */
    public void load() {
        try {
            ResultSet result = db.query("SELECT * FROM homes;");

            if(result == null) return;

            while(result.next()) {
                Player player = Bukkit.getPlayer(result.getString("player_name"));
                Location loc = Helpers.stringToLoc(result.getString("location"));

                homes.put(player, loc);
            }
        } catch (SQLException e) {

        }
    }

}
