package de.ltt.staat.bürgermeister;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class Nomination implements CommandExecutor{
	
	final int betrag = 5000;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			PlayerInfo pi = new PlayerInfo(p);
			if(!Main.nominated.contains(p.getUniqueId().toString())) {
				if(!Main.iswahl) {
					if(Main.nominationLoc.distance(p.getLocation()) <= 3) {
						if(!pi.isvorbestraft()) {
							if(pi.getMoney() >= betrag) {
								Main.nominated.add(p.getUniqueId().toString());
								Main.getPlugin().getConfig().set("Staat.nominated", Main.nominated);
								Main.getPlugin().saveConfig();
								p.sendMessage("§aDu hast dich zum §6Bürgermeister§a nominiert!");
							}else
								p.sendMessage("§cDu musst mindestens §65000€§c besitzen um Bürgermeister werden zu können!");
						}else
							p.sendMessage("§cDu kannst dich als Vorbestrafter nicht nominieren!");
					}else
						p.sendMessage("§cDu kannst dich nur an der dafür vorgesehenen Stelle nominieren!");
				}else 
					p.sendMessage("§cDu kannst dich nicht während einer Wahl nominieren!");
			}else
				p.sendMessage("§cDu bist bereits zur Wahl aufgestellt!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
