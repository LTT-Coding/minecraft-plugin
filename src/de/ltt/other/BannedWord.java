package de.ltt.other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class BannedWord implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender,  Command cmd,  String label,String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 2) {
					if(args[0].equals("add")) {
						SQLData.addBannedWords(args[1]);
						p.sendMessage("§aDu hast das Wort hinzugefügt");
					} else if(args[0].equals("remove")) {
						SQLData.removeBannedWords(args[1]);
						p.sendMessage("§aDu hast das Wort entfernt");
					} else
						p.sendMessage("§cBitte benutze /bannedword add/remove [wort]");
				} else
					p.sendMessage("§cBitte benutze /bannedword add/remove [wort]");
			} else
				 p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
