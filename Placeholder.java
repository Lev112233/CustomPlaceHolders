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

public class Placeholder implements Listener {
  private static ArrayList<Placeholder> placeholders = new ArrayList<>();
  
  private String placeholderName;
  
  private String zeroDisplay;
  
  private String normalDisplay;
  
  private HashMap<Player, Integer> playersToCheck = new HashMap<>();
  
  private FileConfiguration data;
  
  private File file;
  
  public String getToDisplay(Player p) {
    if (this.playersToCheck.containsKey(p))
      return this.normalDisplay.replace("%value%", ((Integer)this.playersToCheck.get(p)).toString()); 
    return this.zeroDisplay;
  }
  
  public static Placeholder getPlaceholderByName(String name) {
    for (Placeholder placeholder : placeholders) {
      if (placeholder.placeholderName.equals(name))
        return placeholder; 
    } 
    return null;
  }
  
  public void setValue(Integer value, String player) {
    if (value.intValue() != 0) {
      int fromFile = value.intValue();
      this.data.set(player, Integer.valueOf(fromFile));
      Player p = Bukkit.getPlayerExact(player);
      if (p != null)
        this.playersToCheck.put(p, Integer.valueOf(fromFile)); 
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
  
  public void addValue(Integer value, String player) {
    int fromFile = this.data.getInt(player);
    fromFile += value.intValue();
    this.data.set(player, Integer.valueOf(fromFile));
    try {
      this.data.save(this.file);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    Player p = Bukkit.getPlayerExact(player);
    if (p != null)
      this.playersToCheck.put(p, Integer.valueOf(fromFile)); 
  }
  
  public void removeValue(Integer value, String player) {
    int fromFile = this.data.getInt(player);
    fromFile -= value.intValue();
    if (fromFile <= 0) {
      this.data.set(player, null);
      Player p = Bukkit.getPlayerExact(player);
      if (p != null)
        this.playersToCheck.remove(p); 
    } else {
      this.data.set(player, Integer.valueOf(fromFile));
      Player p = Bukkit.getPlayerExact(player);
      if (p != null)
        this.playersToCheck.put(p, Integer.valueOf(fromFile)); 
    } 
    try {
      this.data.save(this.file);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public Placeholder(ConfigurationSection section) {
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
    int value = this.data.getInt(e.getPlayer().getName());
    if (value != 0)
      this.playersToCheck.put(e.getPlayer(), Integer.valueOf(value)); 
  }
  
  @EventHandler
  public void onLeave(PlayerQuitEvent e) {
    this.playersToCheck.remove(e.getPlayer());
  }
}
