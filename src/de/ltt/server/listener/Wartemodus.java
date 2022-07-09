package de.ltt.server.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class Wartemodus implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender sender,Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length >= 1) {
					if(args[0].equals("on")) {
						Main.getPlugin().getConfig().set("Server.wartemodus.on", true);
						Main.getPlugin().saveConfig();
						p.sendMessage("§aDu hast den Wartenmodus §2angeschalten");
						for (Player OnlineP : Bukkit.getOnlinePlayers()) {
							if(!Main.Admin.contains(OnlineP.getUniqueId().toString())) {
								if(!Main.AddWartungPlayer.contains(OnlineP.getUniqueId().toString())) {
								OnlineP.kickPlayer("§cDer Server wurde in Wartung gesetzt\n");
								}
							}
						}
					} else if(args[0].equals("off")) {
						Main.getPlugin().getConfig().set("Server.wartemodus.on", false);
						Main.getPlugin().saveConfig();
						p.sendMessage("§aDu hast den Wartungsmodus §4ausgeschalten");
					} else if(args[0].equals("add")) {
						if(args.length >= 2) {
							OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
							if(!Main.AddWartungPlayer.contains(t.getUniqueId().toString())) {
							p.sendMessage("§aDu hast den Spieler hinzugefügt");
							SQLData.addWartungsPlayer(t.getUniqueId().toString());
							}
						} else
							p.sendMessage("§cBenutz bitte /wartung on/off/list/listremove/listremoveplayer || add/remove <Spieler>");
					} else if(args[0].equals("remove")) {
						OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
						if(Main.AddWartungPlayer.contains(t.getUniqueId().toString())) {
							SQLData.removeWartungsPlayer(t.getUniqueId().toString());
							if(t.isOnline() == true) {
								Player t2 = Bukkit.getPlayer(t.getName());
								t2.kickPlayer("§cDu wurdest von den Wartungarbeiten ausgeschlossen");
							}
							p.sendMessage("§aDu hast den Spieler entfernt");
						} else 
							p.sendMessage("§cDer Spieler wurde bereits entfernt");
					} else if(args[0].equals("list")) {
						p.sendMessage("§6============PlayerJoin============");
								for (String string : Main.JoinPlayerZahl.keySet()) {
									p.sendMessage("§6" + Bukkit.getOfflinePlayer(UUID.fromString(string)) + ": " + Main.JoinPlayerZahl.get(string));
							}
						p.sendMessage("§6================================");
					} else if(args[0].equals("listremove")) {
						SQLData.removeAllJoinPlayer();
						p.sendMessage("§6Du hast die PlayerJoin Liste gelöscht");
					} else if(args[0].equals("listremoveplayer")) {
						OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
						SQLData.removeJoinPlayer(t.getUniqueId().toString());
						p.sendMessage("§6Du hast person aus der PlayerJoin Liste gelöscht");
					}
				} else
					p.sendMessage("§cBenutz bitte /wartung on/off/list/listremove/listremoveplayer || add/remove <Spieler>");
				
				
			} else 
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else if(sender instanceof ConsoleCommandSender) {
			ConsoleCommandSender c = (ConsoleCommandSender) sender;
			if(args.length >= 1) {
				if(args[0].equals("on")) {
						Main.getPlugin().getConfig().set("Server.wartemodus.on", true);
						Main.getPlugin().saveConfig();
						c.sendMessage("§aDu hast den Wartenmodus §2angeschalten");
						for (Player OnlineP : Bukkit.getOnlinePlayers()) {
							if(!Main.Admin.contains(OnlineP.getUniqueId().toString())) {
								if(!Main.AddWartungPlayer.contains(OnlineP.getUniqueId().toString())) {
								OnlineP.kickPlayer("§cDer Server wurde in Wartung gesetzt\n");
								}
							}
						}
				} else if(args[0].equals("off")) {
					Main.getPlugin().getConfig().set("Server.wartemodus.on", false);
					Main.getPlugin().saveConfig();
					c.sendMessage("§aDu hast den Wartungsmodus §4ausgeschalten");
				} else if(args[0].equals("add")) {
					OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
					SQLData.addWartungsPlayer(t.getUniqueId().toString());
					c.sendMessage("§aDu hast den Spieler hinzugefügt");
				} else if(args[0].equals("remove")) {
					OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
					if(Main.AddWartungPlayer.contains(t.getUniqueId().toString())) {
						SQLData.removeWartungsPlayer(t.getUniqueId().toString());
						if(t.isOnline() == true) {
							Player t2 = Bukkit.getPlayer(t.getName());
							t2.kickPlayer("§cDu wurdest von den Wartungarbeiten ausgeschlossen");
						}
						c.sendMessage("§aDu hast den Spieler entfernt");
					} else 
						c.sendMessage("§cDer Spieler wurde bereits entfernt");
				}
			} else
				c.sendMessage("§cBenutz bitte /wartemodus on/off");
		}
		return false;
	}
}
