package me;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  public static Main plugin;
  
  public void onEnable() {
    plugin = this;
    saveDefaultConfig();
    File dataFolder = new File(getDataFolder(), "placeholderData");
    if (!dataFolder.exists())
      dataFolder.mkdir(); 
    for (String key : getConfig().getConfigurationSection("placeholders").getKeys(false))
      new Placeholder(getConfig().getConfigurationSection("placeholders." + key)); 
    for (String key : getConfig().getConfigurationSection("placeholders-text").getKeys(false))
      new PlaceholderText(getConfig().getConfigurationSection("placeholders-text." + key)); 
    new Commands();
    (new PlaceholderRegister(this)).register();
  }
}
