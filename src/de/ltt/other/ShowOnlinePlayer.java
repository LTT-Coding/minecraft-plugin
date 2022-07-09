package de.ltt.other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class ShowOnlinePlayer implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				p.sendMessage("§6====================");
				for (Player p2 : Bukkit.getOnlinePlayers()) {
					PlayerInfo pi = new PlayerInfo(p2);
					p.sendMessage("§7- §6" + p2.getName() + "  RP_Char(" + pi.getFirstName() + " " + pi.getLastName() + ")");
				}
				p.sendMessage("§6====================");
				
			}
			
		}
		return false;
	}

}
