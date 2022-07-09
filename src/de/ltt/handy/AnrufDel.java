package de.ltt.handy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class AnrufDel implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
				if(Main.AnrufMap.containsKey(p)) {
					Player t = Main.AnrufMap.get(p);
					Main.AnrufMap.remove(p);
					Main.AnrufMap.remove(t);
						p.sendMessage("§aDu hast den Anruf von §6" + Main.PlayerNummer.get(t.getUniqueId().toString()) + "§a abgelehnt!");
						t.sendMessage("§6" + Main.PlayerNummer.get(p.getUniqueId().toString())+ "§a hat den Anruf abgelehnt!");
				}else
					p.sendMessage("§cDu wirst nicht angerufen!");
		}else
			sender.sendMessage("Das geht nur als Spieler!");
		return false;
	}
}
