package de.ltt.staat.bürgermeister;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class SetWahlLoc implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Main.wahlLoc = p.getLocation();
			Main.getPlugin().getConfig().set("Staat.wahlLoc.x", Main.wahlLoc.getX());
			Main.getPlugin().getConfig().set("Staat.wahlLoc.y", Main.wahlLoc.getY());
			Main.getPlugin().getConfig().set("Staat.wahlLoc.z", Main.wahlLoc.getZ());
			Main.getPlugin().getConfig().set("Staat.wahlLoc.world", Main.wahlLoc.getWorld().getName());
			Main.getPlugin().saveConfig();
			p.sendMessage("§aDer Punkt zum Wählen wurde verschoben!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
}
