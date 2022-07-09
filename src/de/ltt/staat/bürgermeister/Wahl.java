package de.ltt.staat.b�rgermeister;

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
							p.sendMessage("�aWahl wurde gestartet!");
						}else
							p.sendMessage("�cDie Wahl wurde bereits am �6" + Main.wahlStart + "�c gestartet!");
					}else if(args[0].equalsIgnoreCase("stop")) {
						if(Main.iswahl) {
							String b�rgermeister = "";
							String fizeB�rgermeister = "";
							for(String current : Main.wahlstats.keySet()) {
								if(!b�rgermeister.equals("") ) {
									if(Main.wahlstats.get(current) > Main.wahlstats.get(b�rgermeister)) {
										b�rgermeister = current;
									}else if(!fizeB�rgermeister.equals("")) {
										if(Main.wahlstats.get(current) > Main.wahlstats.get(fizeB�rgermeister)) {
											fizeB�rgermeister = current;
										}
									}else
										fizeB�rgermeister = current;
								}else {
									b�rgermeister = current;
								}
							}
							Main.b�rgermeister = b�rgermeister;
							Main.vizeB�rgermeister = fizeB�rgermeister;
							Main.iswahl = false;
							Main.wahlStart = "";
							Main.nominated.clear();
							Main.w�hler.clear();
							Main.wahlstats.clear();
							Main.getPlugin().getConfig().set("Staat.wahlStats.key", null);
							Main.getPlugin().getConfig().set("Staat.wahlStats.value", null);
							Main.getPlugin().getConfig().set("Staat.b�rgermeister", b�rgermeister);
							Main.getPlugin().getConfig().set("Staat.vizeB�rgermeister", fizeB�rgermeister);
							Main.getPlugin().getConfig().set("Staat.nominated", Main.nominated);
							Main.getPlugin().getConfig().set("Staat.w�hler", Main.w�hler);
							Main.getPlugin().getConfig().set("Staat.isWahl", Main.iswahl);
							Main.getPlugin().getConfig().set("Staat.wahlStart", Main.wahlStart);
							Main.getPlugin().saveConfig();
							p.sendMessage("�aWahl wurde beendet!");
						}else
							p.sendMessage("�cEs ist keine Wahl gestartet!");
					}else
						p.sendMessage("�cBenutze �6/wahl start/stop �cum eine Wahl f�r den B�rgermeister zu starten/stoppen!");
				}else
					p.sendMessage("�cBenutze �6/wahl start/stop �cum eine Wahl f�r den B�rgermeister zu starten/stoppen!");
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
