package me.boomboompower.throwingtnt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

/**
 * Created by boomboompower on 19-Apr-16.
 */

public class ThrowingTNTListener implements Listener {

    private ThrowingTNT throwingTNT;
    private ArrayList uuid;

    public ThrowingTNTListener(ThrowingTNT throwingTNT) {
        this.throwingTNT = throwingTNT;
        this.uuid = new ArrayList();

        Bukkit.getPluginManager().registerEvents(this, throwingTNT);
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerAnimation(PlayerAnimationEvent e) {
        e.setCancelled(false); // Used to stop the event being auto cancelled
        // Gets the player, their inventory
        // and also their location
        Player p = e.getPlayer();
        PlayerInventory inv = p.getInventory();
        Location loc = p.getLocation();

        // If the item is tnt and also not null
        if (p.getItemInHand().getType() == Material.TNT) {
            // Removes 1 tnt from player
            inv.remove(new ItemStack(Material.TNT, 1));

            // Creates a variable for the tnt.
            TNTPrimed tnt = (TNTPrimed) p.getWorld().spawn(loc, TNTPrimed.class);

            // Makes the tnt fly through the air!
            tnt.setVelocity(loc.getDirection().normalize().multiply(2));

            // Stores the entities uuid in the array
            // This is used later to stop blocks being
            this.uuid.add(tnt.getUniqueId().toString());
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityExplode(EntityExplodeEvent e) {
        // Makes a variable with the entities uuid
        String uuid = e.getEntity().getUniqueId().toString();

        // Checks if the entities uuid is the
        // same as the tnt we spawned earlier
        if (this.uuid.contains(uuid)) {
            // Stop the explosion breaking blocks
            e.blockList().clear();

            // Remove the UUID from
            // the ArrayList.
            this.uuid.remove(uuid);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlace(BlockPlaceEvent e) {
        // Make one variable holding the block
        // type and one with the player.
        Material type = e.getBlock().getType();
        Player p = e.getPlayer();

        // If the block being placed is
        // tnt and player isn't sneaking,
        // stop the block being placed.
        if (type == Material.TNT) {
            e.setCancelled(true);
        }
    }
}
