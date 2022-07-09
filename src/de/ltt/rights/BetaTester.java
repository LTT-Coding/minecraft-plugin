package de.ltt.rights;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class BetaTester implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender,  Command command,  String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					Player t = Bukkit.getPlayer(args[0]);
					if(Main.BetaT.contains(t.getUniqueId().toString())) {
						SQLData.removeTester(t.getUniqueId().toString());
						p.sendMessage("§eDu hast " + t.getName() + " den Rang BetaTester wegenommen.");
						t.sendMessage("§cDir wurde der Rang Beta Tester §4wegenommen§c!");
					} else {
						p.sendMessage("§eDu hast " + t.getName() + " den Rang BetaTester gegeben.");
						t.sendMessage("§6========================================");
						t.sendMessage("§aHerzlichen Glückwunsch!");
						t.sendMessage("§eDu hast den Rang Beta Tester erhalten :D");
						t.sendMessage("§6========================================");
						SQLData.addTester(t.getUniqueId().toString());
					}
				} else
					p.sendMessage("§cBitte benutze §6/betatester <Spieler>");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
