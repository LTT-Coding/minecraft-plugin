package de.ltt.handy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class AnrufBE implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(Main.AnrufChat.containsKey(player)) {
				Player target = Main.AnrufChat.get(player);
				Main.AnrufChat.remove(player);
				Main.AnrufeChat2.remove(target);
				player.sendMessage("§eDer Anruf wurde beendet");
				target.sendMessage("§e" + player.getName() + "§e hat den Anruf beendet");
				
			} else if(Main.AnrufeChat2.containsKey(player)) {
				Player target = Main.AnrufeChat2.get(player);
				Main.AnrufeChat2.remove(player);
				Main.AnrufChat.remove(target);
				player.sendMessage("§eDer Anruf wurde beendet");
				target.sendMessage("§e" + player.getName() + "§e hat den Anruf beendet");
			} else {
				player.sendMessage("§cDu bist in keinem Anruf!");
			}
		}
		return false;
	}
}
