package me;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
  public Commands() {
    Main.plugin.getCommand("customplaceholders").setExecutor(this);
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.hasPermission("customplaceholders.admin"))
      return false; 
    if (args.length > 3) {
      if (args[0].equals("add")) {
        try {
          Placeholder placeholder = Placeholder.getPlaceholderByName(args[2]);
          placeholder.addValue(Integer.valueOf(args[3]), args[1]);
          sender.sendMessage("Done!");
        } catch (Exception x) {
          sender.sendMessage("The placeholder was not found or the value is invalid");
        } 
      } else if (args[0].equals("remove")) {
        try {
          Placeholder placeholder = Placeholder.getPlaceholderByName(args[2]);
          placeholder.removeValue(Integer.valueOf(args[3]), args[1]);
          sender.sendMessage("Done!");
        } catch (Exception x) {
          sender.sendMessage("The placeholder was not found or the value is invalid");
        } 
      } else if (args[0].equals("set")) {
        try {
          Placeholder placeholder = Placeholder.getPlaceholderByName(args[2]);
          placeholder.setValue(Integer.valueOf(args[3]), args[1]);
          sender.sendMessage("Done!");
        } catch (Exception x) {
          try {
            PlaceholderText placeholder = PlaceholderText.getPlaceholderByName(args[2]);
            placeholder.setValue(args[3], args[1]);
            sender.sendMessage("Done!");
          } catch (Exception y) {
            sender.sendMessage("The placeholder was not found or the value is invalid");
          } 
        } 
      } 
    } else {
      sender.sendMessage("/customplaceholders add <player> <placeholder> <value>");
      sender.sendMessage("/customplaceholders set <player> <placeholder> <value>");
      sender.sendMessage("/customplaceholders remove <player> <placeholder> <value>");
    } 
    return true;
  }
}
