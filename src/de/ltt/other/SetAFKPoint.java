package de.ltt.other;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class SetAFKPoint implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				Location loc = p.getLocation();
				Main.getPlugin().getConfig().set("Server.AFKLoc.x", loc.getX());
				Main.getPlugin().getConfig().set("Server.AFKLoc.y", loc.getY());
				Main.getPlugin().getConfig().set("Server.AFKLoc.z", loc.getZ());
				Main.getPlugin().getConfig().set("Server.AFKLoc.yaw", loc.getYaw());
				Main.getPlugin().getConfig().set("Server.AFKLoc.pitch", loc.getPitch());
				Main.getPlugin().getConfig().set("Server.AFKLoc.world", loc.getWorld().getName());
				Main.getPlugin().saveConfig();
				p.sendMessage("§aDie AFK-Position wurde gesetzt!");
				Main.AFKLoc = loc;
				p.sendMessage(Main.AFKLoc.toString());
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
