package de.ltt.staat.bürgermeister;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class SetNominationLoc implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Main.nominationLoc = p.getLocation();
			Main.getPlugin().getConfig().set("Staat.nominationLoc.x", Main.nominationLoc.getX());
			Main.getPlugin().getConfig().set("Staat.nominationLoc.y", Main.nominationLoc.getY());
			Main.getPlugin().getConfig().set("Staat.nominationLoc.z", Main.nominationLoc.getZ());
			Main.getPlugin().getConfig().set("Staat.nominationLoc.world", Main.nominationLoc.getWorld().getName());
			Main.getPlugin().saveConfig();
			p.sendMessage("§aDer Punkt zum Nominieren wurde verschoben!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
