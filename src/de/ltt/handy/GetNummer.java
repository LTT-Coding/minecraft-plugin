package de.ltt.handy;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.PlayerInfo;

public class GetNummer implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			PlayerInfo pi = new PlayerInfo(p);

			if (args.length == 1) {
				if (Main.Admin.contains(p.getUniqueId().toString())) {
					Player t = Bukkit.getPlayer(args[0]);
					pi.removeNummer(t);
				} else
					p.sendMessage("§cDu bist kein Admin");

			} else {
				if (Main.PlayerNummer.containsKey(p.getUniqueId().toString())) {
					p.sendMessage("§eDu hast bereits die Nummer: " + pi.getNummer());

				} else {
					Random generator = new Random();
					generator.setSeed(System.currentTimeMillis());
					int num = generator.nextInt(899999) + 100000;

					while (Main.NummerPlayer.containsKey(num)) {
						generator.setSeed(System.currentTimeMillis());
						num = generator.nextInt(899999) + 100000;

					}  {
						
						p.sendMessage("§eDeine Nummer ist: " + num);
						pi.setNummer(num);
						
					}
				}
			}
		}
		return false;
	}
}
