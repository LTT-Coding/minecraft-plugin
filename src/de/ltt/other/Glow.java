package de.ltt.other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class Glow implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(p.isGlowing()) {
					p.setGlowing(false);
				}else {
					p.setGlowing(true);
				}
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else 
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
