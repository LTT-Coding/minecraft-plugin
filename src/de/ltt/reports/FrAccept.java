package de.ltt.reports;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class FrAccept implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(Main.Supporter.contains(player.getUniqueId().toString())) {
				if(!Main.frChat.containsKey(player)) {
					if(!ReportCommand.fragen.isEmpty()) {
						Player target = ReportCommand.fragen.get(0);
						ReportCommand.fragen.remove(target);
						Main.frChat.put(player, target);
						Main.frChat2.put(target, player);
						player.sendMessage("§aDu hast den Fragechat mit §6" + target.getName() + "§a geöffnet!");
						target.sendMessage("§6" + player.getName() + "§a hat den Fragenchat mit dir betreten!");
					}else
						player.sendMessage("§cEs gibt keine neuen Fragen");
					
				}else
					player.sendMessage("§cDu bist bereits in einem Report");
			}else
				player.sendMessage(Main.KEINE_RECHTE_SUPPORT);
		}else
			sender.sendMessage("Das geht nur als Spieler!");
		return false;
	}

}
