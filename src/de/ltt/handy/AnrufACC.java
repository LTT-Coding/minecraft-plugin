package de.ltt.handy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;


public class AnrufACC implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
				if(!Main.AnrufChat.containsKey(p)) {
						if(Main.AnrufMap.containsKey(p)) {
							if(Main.handyOnOff.get(p.getUniqueId())) {
								Player t = Main.AnrufMap.get(p);
								Main.AnrufChat.put(p, t);
								Main.AnrufeChat2.put(t, p);
								p.sendMessage("§aDu bist im Anruf mit §6" + Main.PlayerNummer.get(t.getUniqueId().toString()) + "§a!");
								t.sendMessage("§6" + Main.PlayerNummer.get(p.getUniqueId().toString()) + "§a ist mit dir im Anruf!");
								p.sendMessage("§eUm Aufzulegen benutz §6/auflegen");
								t.sendMessage("§eUm Aufzulegen benutz §6/auflegen");
							}
						}else
							p.sendMessage("§cDu wirst nicht angerufen!");
				}else
					p.sendMessage("§cDu bist bereits in einem Anruf!");
		}else
			sender.sendMessage("Das geht nur als Spieler!");
		return false;
	}
}
