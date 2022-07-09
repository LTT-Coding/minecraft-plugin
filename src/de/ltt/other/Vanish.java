package de.ltt.other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class Vanish implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(Main.vanish.contains(p.getUniqueId().toString())) {
					SQLData.removeVanish(p.getUniqueId().toString());
					p.sendMessage("§aDu bist jetzt §cnicht §amehr im Vanish");
					for (Player p2 : Bukkit.getOnlinePlayers()) {
						if(!Main.Admin.contains(p2.getUniqueId().toString())) {
							p2.showPlayer(p);
						}
					}
				} else {
					SQLData.addVanish(p.getUniqueId().toString());
					p.sendMessage("§aDu bist jetzt im Vanish");
					for (Player p2 : Bukkit.getOnlinePlayers()) {
						if(!Main.Admin.contains(p2.getUniqueId().toString())) {
							p2.hidePlayer(p);
						}
					}
				}
			} else
				p.sendMessage(Main.KEIN_SPIELER);
		}
		return false;
	}

}
