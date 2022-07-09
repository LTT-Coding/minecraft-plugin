package de.ltt.other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Discord implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage("§eUnserem Discord kannst du mit folgendem Link beitreten: https://discord.gg/sTd8SqD");
		}
		return false;
	}

}
