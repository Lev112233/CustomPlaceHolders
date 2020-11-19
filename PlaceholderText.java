package me;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlaceholderText implements Listener {
  private static ArrayList<PlaceholderText> placeholders = new ArrayList<>();
  
  private String placeholderName;
  
  private String zeroDisplay;
  
  private String normalDisplay;
  
  private HashMap<Player, String> playersToCheck = new HashMap<>();
  
  private FileConfiguration data;
  
  private File file;
  
  public String getToDisplay(Player p) {
    if (this.playersToCheck.containsKey(p))
      return this.normalDisplay.replace("%value%", this.playersToCheck.get(p)); 
    return this.zeroDisplay;
  }
  
  public static PlaceholderText getPlaceholderByName(String name) {
    for (PlaceholderText placeholder : placeholders) {
      if (placeholder.placeholderName.equals(name))
        return placeholder; 
    } 
    return null;
  }
  
  public void setValue(String value, String player) {
    if (!value.equalsIgnoreCase("null")) {
      value = value.replace("_", " ");
      this.data.set(player, value);
      Player p = Bukkit.getPlayerExact(player);
      if (p != null)
        this.playersToCheck.put(p, value); 
    } else {
      this.data.set(player, null);
      Player p = Bukkit.getPlayerExact(player);
      if (p != null)
        this.playersToCheck.remove(p); 
    } 
    try {
      this.data.save(this.file);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public PlaceholderText(ConfigurationSection section) {
    this.placeholderName = section.getString("name");
    this.zeroDisplay = section.getString("zero-display");
    this.normalDisplay = section.getString("normal-display");
    this.file = new File(Main.plugin.getDataFolder(), "placeholderData/" + this.placeholderName + ".yml");
    try {
      if (!this.file.exists())
        this.file.createNewFile(); 
    } catch (Exception x) {
      x.printStackTrace();
    } 
    this.data = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
    Bukkit.getPluginManager().registerEvents(this, (Plugin)Main.plugin);
    placeholders.add(this);
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    String value = this.data.getString(e.getPlayer().getName());
    if (value != null)
      this.playersToCheck.put(e.getPlayer(), value); 
  }
  
  @EventHandler
  public void onLeave(PlayerQuitEvent e) {
    this.playersToCheck.remove(e.getPlayer());
  }
}
