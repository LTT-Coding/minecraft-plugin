package de.ltt.reports;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class DoneReport implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(Main.frChat.containsKey(player)) {
				Player target = Main.frChat.get(player);
				Main.frChat.remove(player);
				Main.frChat2.remove(target);
				player.sendMessage("§9Report wurde beendet");
				target.sendMessage("§1§l" + player.getName() + "§r§9 hat den Report beendet");
			}
		}
		return false;
	}

}
