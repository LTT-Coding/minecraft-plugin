package de.ltt.casino.allgemein;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class SeeJeton implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 1) {

				int jeton = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId().toString() + ".jeton");

				p.sendMessage("§aDu hast §6" + jeton + " Jetons§a.");


			} else
				p.sendMessage("§cBenutze §6/seejeton [Spieler] §cum deine Jetons zu sehen!");

		} else
			sender.sendMessage("§cDazu hast du keine Rechte!");

		return false;
	}

}
