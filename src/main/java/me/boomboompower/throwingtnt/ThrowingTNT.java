package me.boomboompower.throwingtnt;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"unused"})
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
