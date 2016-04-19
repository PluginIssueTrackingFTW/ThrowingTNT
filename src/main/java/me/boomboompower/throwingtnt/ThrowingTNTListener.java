package me.boomboompower.throwingtnt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by boomboompower on 19-Apr-16.
 */

@SuppressWarnings({"unused"})
public class ThrowingTNTListener implements Listener {

    private ThrowingTNT throwingTNT;
    private String uniqueID;

    public ThrowingTNTListener(ThrowingTNT throwingTNT) {
        this.throwingTNT = throwingTNT;
        Bukkit.getPluginManager().registerEvents(this, throwingTNT);
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerInteract(PlayerInteractEvent e) {
        // If the hand is not their main hand,
        // don't run any of the code below.
        if (!(e.getHand() == EquipmentSlot.HAND)) return;

        // Gets the player, their inventory
        // and also their location
        Player p = e.getPlayer();
        PlayerInventory inv = p.getInventory();
        Location loc = p.getLocation();

        // If the item in their main hand is tnt
        if (p.getInventory().getItemInMainHand().getType().equals(Material.TNT)) {
            // Removes 1 tnt from player
            inv.remove(new ItemStack(Material.TNT, 1));

            // Creates a variable for the tnt.
            TNTPrimed tnt = (TNTPrimed) p.getWorld().spawn(loc, TNTPrimed.class);

            // Makes the tnt fly through the air!
            tnt.setVelocity(loc.getDirection().normalize().multiply(2));

            // Stores the tnt's uuid (used for later)
            this.uniqueID = tnt.getUniqueId().toString();

            if (throwingTNT.getConfig().getBoolean("CountThrows")) {
                FileConfiguration con = throwingTNT.getConfig();
                Integer thrown = throwingTNT.getConfig().getInt("Throws");

                con.set("Throws", thrown + 1);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityExplode(EntityExplodeEvent e) {
        // Makes a variable with the entities uuid
        String uuid = e.getEntity().getUniqueId().toString();

        // Checks if the entities uuid is the
        // same as the tnt we spawned earlier
        if (uuid.equals(this.uniqueID)) {
            // Stop the explosion breaking blocks
            e.blockList().clear();
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlace(BlockPlaceEvent e) {
        // If the block being placed is
        // tnt stop it being placed
        if (e.getBlockPlaced().getType().equals(Material.TNT)) {
            e.setCancelled(true);
        }
    }
}
