package de.ltt.rights;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.PlayerInfo;

public class Moderator implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(args.length == 1) {
			OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]); 
			PlayerInfo ti = new PlayerInfo(t);
			if(sender instanceof Player) {
				//wenn spieler
				Player p = (Player) sender;
				if(Bukkit.getOnlinePlayers().contains(t)) {
					if(Main.Admin.contains(p.getUniqueId().toString())) {
						if(!Main.Moderatoren.contains(t.getUniqueId().toString())) {
							if(t.isOnline()) {
								SQLData.addMod(t.getUniqueId().toString());
								p.sendMessage("§6" + t.getName() + "§a wurde als Moderator hinzugefügt!");
								t.getPlayer().sendMessage("§aDu wurdest von §6" + p.getName() + "§a als Moderator hinzugefügt!");
								t.getPlayer().sendMessage("§aUm deine Rechte als Moderator einzusehen gebe §6/modrechte §aein!");
								t.setOp(true);
							}else
								p.sendMessage("§cDieser Spieler muss online sein um als §6Moderator §chinzugefügt zu werden!");
						}else {
							SQLData.removeMod(t.getUniqueId().toString());
							p.sendMessage("§6" + t.getName() + "§c wurde als Moderator entfernt!");
							if(t.isOnline()) {
								t.getPlayer().sendMessage("§cDu wurdest von §6" + p.getName() + "§c als Moderator entfernt!");
							}else
								ti.addToMailBox("§cDu wurdest von §6" + p.getName() + "§c als Moderator entfernt!");
						}
					}else
						p.sendMessage(Main.KEINE_RECHTE_ADMIN);
				}else
					p.sendMessage("§cDieser Spieler war noch nie auf diesem Server!");
			}else {
				//Wenn kein Spieler
				if(!Main.Moderatoren.contains(t.getUniqueId().toString())) {
					if(t.isOnline()) {
						SQLData.addMod(t.getUniqueId().toString());
						sender.sendMessage( Main.PREFIX + t.getName() + "wurde als Moderator hinzugefügt!");
						t.getPlayer().sendMessage("§aDu wurdest über die Konsole als Moderator hinzugefügt!");
						t.getPlayer().sendMessage("§aUm deine Rechte als Moderator einzusehen gebe §6/modrechte §aein!");
					}else {
						sender.sendMessage( Main.PREFIX + "Dieser Spieler muss online sein um als Moderator §chinzugefügt zu werden!");
					}
				}else {
					SQLData.removeMod(t.getUniqueId().toString());
					sender.sendMessage(Main.PREFIX + t.getName() + " wurde als Moderator entfernt!");
					if(t.isOnline()) {
						t.getPlayer().sendMessage("§cDu wurdest über die Konsole als Moderator entfernt!");
					}else
						ti.addToMailBox("§cDu wurdest über die Konsole als Moderator entfernt!");
				}
			}
		}else if(sender instanceof Player) {
			sender.sendMessage("§cBenutze §6/moderator [Name]§c um Leute als Moderator hinzuzufügen oder zu entfernen!");
		}else
			sender.sendMessage(Main.PREFIX + "Benutze /moderator [Name] um Leute als Moderator hinzuzufügen oder zu entfernen!");
		return false;
	}
	
}
