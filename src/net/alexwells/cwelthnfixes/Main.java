package net.alexwells.cwelthnfixes;

import lib.PatPeter.SQLibrary.Database;
import lib.PatPeter.SQLibrary.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static String PLUGIN_NAME = "CwelthNFixes";

    private static Main instance;

    private Database db;
    private Logger logger;

    private HomesRepository homesRepository;

    @Override
    public void onEnable() {
        instance = this;

        logger = Logger.getLogger(PLUGIN_NAME);

        initDatabase();
        initHomes();

        Bukkit.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    private void initDatabase() {
        db = new SQLite(logger,
                "[" + PLUGIN_NAME + "]",
                this.getDataFolder().getAbsolutePath(),
                "database");

        try {
            db.open();
            db.query("CREATE TABLE IF NOT EXISTS homes (" +
                    "`id` INTEGER PRIMARY KEY, " +
                    "`player_name` VARCHAR(64) NOT NULL, " +
                    "`location` VARCHAR(255)" +
                    ");");
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            getPluginLoader().disablePlugin(this);
        }
    }

    private void initHomes() {
        homesRepository = new HomesRepository();

        homesRepository.load();
    }

    public Database database() {
        return db;
    }

    public HomesRepository repository() {
        return homesRepository;
    }

    public static Main instance() {
        return instance;
    }

}
