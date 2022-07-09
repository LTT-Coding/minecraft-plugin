package de.ltt.other.Schneeball;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class Schnee implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;	
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				Bukkit.getWorld(p.getWorld().getUID()).setBiome(10000, 10000, Biome.SNOWY_BEACH);
			}
		}
		return false;
	}
	
	

}
