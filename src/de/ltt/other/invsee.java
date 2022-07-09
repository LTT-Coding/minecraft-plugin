package de.ltt.other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class invsee implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null) {
						p.openInventory(target.getInventory());
						p.sendMessage("§aInventar von §6" + target.getName() + "§a wurde geöffnet!");
						target.sendMessage("§eDein Inventar wird von einem §6Admin§e angesehen!");
					}else
						p.sendMessage("§cDer angegebene Spieler ist nicht online oder existiert nicht!");
				}else
					p.sendMessage("§cBenutze §6/invsee [Spieler] §cum das Inventar eines Spielers einzusehen");
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
