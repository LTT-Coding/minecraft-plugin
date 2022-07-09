package de.ltt.staat.b�rgermeister;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class SetB�rgermeister implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
					if(t.hasPlayedBefore()) {
						Main.b�rgermeister = t.getUniqueId().toString();
						Main.getPlugin().getConfig().set("Staat.b�rgermeister", Main.b�rgermeister);
						p.sendMessage("�6" + t.getName() + "�a ist nun B�rgermeister!");
						if(t.isOnline()) {
							t.getPlayer().sendMessage("�aDu wurdest von einem �6Admin�a zum B�rgermeister ernannt!");
						}
						Main.getPlugin().saveConfig();
					}else
						p.sendMessage("�cDer Spieler �6" + t.getName() + "�c war noch nie auf diesem Server!");
				}else
					p.sendMessage("�cBenutze �6/setb�rgermeister �c um einen B�rgermeister manuell einzustellen!");
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);

		return false;
	}

}
