package de.ltt.staat.bürgermeister;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class Bürgermeister implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.bürgermeister != "" && Main.bürgermeister != null) {
				OfflinePlayer bürgermeister = Bukkit.getOfflinePlayer(UUID.fromString(Main.bürgermeister));
				p.sendMessage("§eBürgermeister: §6" + bürgermeister.getName());
				if(Main.vizeBürgermeister != "" && Main.vizeBürgermeister != null) {
					OfflinePlayer vizeBürgermeister = Bukkit.getOfflinePlayer(UUID.fromString(Main.vizeBürgermeister));
					p.sendMessage("§eVizebürgermeister: §6" + vizeBürgermeister.getName());
				}else
					p.sendMessage("§cEs gibt aktuell keinen Vizebürgermeister!");
			}else
				p.sendMessage("§cEs gibt aktuell keinen Bürgermeister!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
