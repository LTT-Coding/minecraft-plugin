package de.ltt.staat.bürgermeister;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class Wahl implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("start")) {
						if(!Main.iswahl) {
							LocalDateTime date = LocalDateTime.now();
							DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
							String formatedDate = date.format(format);
							Main.wahlStart = formatedDate;
							Main.iswahl = true;
							Main.getPlugin().getConfig().set("Staat.isWahl", Main.iswahl);
							Main.getPlugin().getConfig().set("Staat.wahlStart", Main.wahlStart);
							Main.getPlugin().saveConfig();
							p.sendMessage("§aWahl wurde gestartet!");
						}else
							p.sendMessage("§cDie Wahl wurde bereits am §6" + Main.wahlStart + "§c gestartet!");
					}else if(args[0].equalsIgnoreCase("stop")) {
						if(Main.iswahl) {
							String bürgermeister = "";
							String fizeBürgermeister = "";
							for(String current : Main.wahlstats.keySet()) {
								if(!bürgermeister.equals("") ) {
									if(Main.wahlstats.get(current) > Main.wahlstats.get(bürgermeister)) {
										bürgermeister = current;
									}else if(!fizeBürgermeister.equals("")) {
										if(Main.wahlstats.get(current) > Main.wahlstats.get(fizeBürgermeister)) {
											fizeBürgermeister = current;
										}
									}else
										fizeBürgermeister = current;
								}else {
									bürgermeister = current;
								}
							}
							Main.bürgermeister = bürgermeister;
							Main.vizeBürgermeister = fizeBürgermeister;
							Main.iswahl = false;
							Main.wahlStart = "";
							Main.nominated.clear();
							Main.wähler.clear();
							Main.wahlstats.clear();
							Main.getPlugin().getConfig().set("Staat.wahlStats.key", null);
							Main.getPlugin().getConfig().set("Staat.wahlStats.value", null);
							Main.getPlugin().getConfig().set("Staat.bürgermeister", bürgermeister);
							Main.getPlugin().getConfig().set("Staat.vizeBürgermeister", fizeBürgermeister);
							Main.getPlugin().getConfig().set("Staat.nominated", Main.nominated);
							Main.getPlugin().getConfig().set("Staat.wähler", Main.wähler);
							Main.getPlugin().getConfig().set("Staat.isWahl", Main.iswahl);
							Main.getPlugin().getConfig().set("Staat.wahlStart", Main.wahlStart);
							Main.getPlugin().saveConfig();
							p.sendMessage("§aWahl wurde beendet!");
						}else
							p.sendMessage("§cEs ist keine Wahl gestartet!");
					}else
						p.sendMessage("§cBenutze §6/wahl start/stop §cum eine Wahl für den Bürgermeister zu starten/stoppen!");
				}else
					p.sendMessage("§cBenutze §6/wahl start/stop §cum eine Wahl für den Bürgermeister zu starten/stoppen!");
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
