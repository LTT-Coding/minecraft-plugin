package de.ltt.casino.allgemein;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class SeeJetonAdmin implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);

					int jeton = Main.getPlugin().getConfig()
							.getInt("Spieler." + target.getUniqueId().toString() + ".jeton");

					p.sendMessage("§e" + target.getName() + "§a hat §6" + jeton + " Jetons§a.");

				} else
					p.sendMessage("§4Bitte benutze /seejetonadmin <Name>");

			} else
				sender.sendMessage("§cDazu hast du keine Rechte!");
		}
		return false;
	}
}
