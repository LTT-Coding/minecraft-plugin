package de.ltt.rights;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.PlayerInfo;

public class AddAdmin implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(args.length == 1) {
			OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]); 
			PlayerInfo ti = new PlayerInfo(t);
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(Bukkit.getOnlinePlayers().contains(t)) {
					if(Main.Admin.contains(p.getUniqueId().toString())) {
						if(!Main.Admin.contains(t.getUniqueId().toString())) {
							if(t.isOnline()) {
								SQLData.addAdmin(t.getUniqueId().toString());
								p.sendMessage("§6" + t.getName() + "§a wurde als Admin hinzugefügt!");
								t.getPlayer().sendMessage("§aDu wurdest von §6" + p.getName() + "§a als Admin hinzugefügt!");
								t.getPlayer().sendMessage("§aUm deine Rechte als Admin einzusehen gebe §6/adminrechte §aein!");
								t.setOp(true);
								for (String p2 : Main.vanish) {
									if(Bukkit.getPlayer(UUID.fromString(p2)) != null) {
										t.getPlayer().showPlayer(Bukkit.getPlayer(UUID.fromString(p2)));
									}
								}
							}else
								p.sendMessage("§cDieser Spieler muss online sein um als §6Admin §chinzugefügt zu werden!");
						}else {
							SQLData.removeAdmin(t.getUniqueId().toString());
							p.sendMessage("§6" + t.getName() + "§c wurde als Admin entfernt!");
							if(t.isOnline()) {
								t.getPlayer().sendMessage("§cDu wurdest von §6" + p.getName() + "§c als Admin entfernt!");
								t.setOp(false);
								SQLData.removeVanish(t.getUniqueId().toString());
								for (String p2 : Main.vanish) {
									if(Bukkit.getPlayer(UUID.fromString(p2)) != null) {
										t.getPlayer().hidePlayer(Bukkit.getPlayer(UUID.fromString(p2)));
									}
								}
								for (Player p3: Bukkit.getOnlinePlayers()) {
									p3.showPlayer(Bukkit.getPlayer(UUID.fromString(t.getUniqueId().toString())));
								}
								if(Main.getPlugin().getConfig().getBoolean("Server.wartemodus.on") == true) {
									Player tp = Bukkit.getPlayer(t.getName());
									tp.kickPlayer("§cDu wurdest von den Wartungen ausgeschlossen");
								}
							}else
								ti.addToMailBox("§cDu wurdest von §6" + p.getName() + "§c als Admin entfernt!");
						}
					}else
						p.sendMessage(Main.KEINE_RECHTE_ADMIN);
				}else
					p.sendMessage("§cDieser Spieler war noch nie auf diesem Server!");
			}else {
				//Wenn kein Spieler
				if(!Main.Admin.contains(t.getUniqueId().toString())) {
					if(t.isOnline()) {
						SQLData.addAdmin(t.getUniqueId().toString());
						sender.sendMessage( Main.PREFIX + t.getName() + "wurde als Admin hinzugefügt!");
						t.getPlayer().sendMessage("§aDu wurdest über die Konsole als Admin hinzugefügt!");
						t.getPlayer().sendMessage("§aUm deine Rechte als Admin einzusehen gebe §6/adminrechte §aein!");
						t.setOp(true);
						for (String p2 : Main.vanish) {
							if(Bukkit.getPlayer(UUID.fromString(p2)) != null) {
								t.getPlayer().showPlayer(Bukkit.getPlayer(UUID.fromString(p2)));
							}
						}
					}else {
						sender.sendMessage( Main.PREFIX + "Dieser Spieler muss online sein um als Admin §chinzugefügt zu werden!");
					}
				}else {
					SQLData.removeAdmin(t.getUniqueId().toString());
					sender.sendMessage(Main.PREFIX + t.getName() + " wurde als Admin entfernt!");
					if(t.isOnline()) {
						t.getPlayer().sendMessage("§cDu wurdest über die Konsole als Admin entfernt!");
						t.setOp(false);
						for (String p2 : Main.vanish) {
							if(Bukkit.getPlayer(UUID.fromString(p2)) != null) {
								t.getPlayer().hidePlayer(Bukkit.getPlayer(UUID.fromString(p2)));
							}
						}
						for (Player p3: Bukkit.getOnlinePlayers()) {
							p3.showPlayer(Bukkit.getPlayer(UUID.fromString(t.getUniqueId().toString())));
						}
						if(Main.getPlugin().getConfig().getBoolean("Server.wartemodus.on") == true) {
							Player tp = Bukkit.getPlayer(t.getName());
							tp.kickPlayer("§cDu wurdest von den Wartungen ausgeschlossen");
						}
					}else
						ti.addToMailBox("§cDu wurdest über die Konsole als Admin entfernt!");
				}
			}
		}else if(sender instanceof Player) {
			sender.sendMessage("§cBenutze §6/admin [Name]§c um Leute als Admin hinzuzufügen oder zu entfernen!");
		}else
			sender.sendMessage(Main.PREFIX + "Benutze /admin [Name] um Leute als Admin hinzuzufügen oder zu entfernen!");
		return false;
	}
	
}
