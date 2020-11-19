package me;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderRegister extends PlaceholderExpansion {
  private Main plugin;
  
  public PlaceholderRegister(Main plugin) {
    this.plugin = plugin;
  }
  
  public boolean persist() {
    return true;
  }
  
  public boolean canRegister() {
    return true;
  }
  
  public String getAuthor() {
    return this.plugin.getDescription().getAuthors().toString();
  }
  
  public String getIdentifier() {
    return "customplaceholders";
  }
  
  public String getVersion() {
    return this.plugin.getDescription().getVersion();
  }
  
  public String onPlaceholderRequest(Player player, String identifier) {
    if (player == null)
      return ""; 
    try {
      return Placeholder.getPlaceholderByName(identifier).getToDisplay(player);
    } catch (Exception x) {
      try {
        return PlaceholderText.getPlaceholderByName(identifier).getToDisplay(player);
      } catch (Exception y) {
        return null;
      } 
    } 
  }
}
