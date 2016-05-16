package net.alexwells.cwelthnfixes;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;

public class EventListener implements Listener {

    private HomesRepository homes;

    /*
        Nooooope, we can't just hold PlayerInventory here. We're to use ArrayList instead.
     */
    private HashMap<Player, ArrayList<ItemStack[]>> inventories = new HashMap<>();

    public EventListener() {
        homes = Main.instance().repository();
    }

    @EventHandler
    public void playerBedEnterHome(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();
        Location loc = e.getBed().getLocation();

        homes.set(player, loc);
    }

    @EventHandler
    public void playerRespawnHome(PlayerRespawnEvent e) {
        Player player = e.getPlayer();

        Location loc = homes.get(player);

        if(loc == null) return;

        e.setRespawnLocation(loc);
    }

    /*
        Player death event returns a new player, respawned one.
        That's why we can't get inventory and we're to use EntityDeath insteadof PlayerDeath

        Note: dont argue about all this code. Watch a tip upper.
     */
    @EventHandler
    public void playerDeathInventory(EntityDeathEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        PlayerInventory inv = player.getInventory();

        ArrayList<ItemStack[]> items = new ArrayList<>();
        items.add(inv.getContents());
        items.add(inv.getArmorContents());

        inventories.put(player, items);

        e.setDroppedExp(0);
        e.getDrops().clear();
    }

    @EventHandler
    public void playerRespawnInventory(PlayerRespawnEvent e) {
        Player player = e.getPlayer();

        if(!inventories.containsKey(player)) return;

        ArrayList<ItemStack[]> oldInv = inventories.get(player);
        PlayerInventory inv = player.getInventory();

        inv.setContents(oldInv.get(0));
        inv.setArmorContents(oldInv.get(1));

        inventories.remove(player);
    }

}
