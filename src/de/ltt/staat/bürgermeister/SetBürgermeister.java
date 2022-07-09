package de.ltt.staat.bürgermeister;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class SetBürgermeister implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
					if(t.hasPlayedBefore()) {
						Main.bürgermeister = t.getUniqueId().toString();
						Main.getPlugin().getConfig().set("Staat.bürgermeister", Main.bürgermeister);
						p.sendMessage("§6" + t.getName() + "§a ist nun Bürgermeister!");
						if(t.isOnline()) {
							t.getPlayer().sendMessage("§aDu wurdest von einem §6Admin§a zum Bürgermeister ernannt!");
						}
						Main.getPlugin().saveConfig();
					}else
						p.sendMessage("§cDer Spieler §6" + t.getName() + "§c war noch nie auf diesem Server!");
				}else
					p.sendMessage("§cBenutze §6/setbürgermeister §c um einen Bürgermeister manuell einzustellen!");
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);

		return false;
	}

}
