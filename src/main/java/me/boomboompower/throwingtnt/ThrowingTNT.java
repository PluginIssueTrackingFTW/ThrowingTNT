package me.boomboompower.throwingtnt;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by boomboompower on 19-Apr-16.
 */

public class ThrowingTNT extends JavaPlugin {

    @Override
    public void onEnable() {
        new ThrowingTNTListener(this);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
