package de.ltt.other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class GC implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 0) {
					if(Main.gc.contains(p.getUniqueId())) {
						Main.gc.remove(p.getUniqueId());
						p.sendMessage("§aDu hast den globalen Chat verlassen!");
					}else {
						Main.gc.add(p.getUniqueId());
						p.sendMessage("§aDu hast den globalen Chat betreten!");
					}
				}else {
					String message = "";
					for(int i = 0; i < args.length; i++) {
						message += args[i] + " ";
					}
					Bukkit.broadcastMessage("§7[§5ADMIN§7] §5" + p.getName() + ": §3" + message);
				}
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}
		return false;
	}

}
