package de.ltt.bank.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class Money implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,  String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(args.length == 4) {
				if(Main.checkCharName(args[2])) {
					Player t = Main.getChar(args[2]);
					PlayerInfo pi = new PlayerInfo(t);
					try {
						double betrag = Double.parseDouble(args[3]);
						if(!args[3].startsWith("0")) {
							if(args[0].equalsIgnoreCase("konto")) {
								if(args[1].equalsIgnoreCase("add")) {
									pi.addMoney(betrag);
									p.sendMessage("§6Du hast erfolgreich  §6dem Konto von " + pi.getFirstName() + " " + pi.getLastName() + " " + betrag + "€ hinzugefügt");
									t.sendMessage("§6Dir wurden von einem Admin " + betrag + "€ auf dein Konto hinzugefügt");
								} else if(args[1].equalsIgnoreCase("remove")) {
									pi.subtractMoney(betrag);
									p.sendMessage("§6Du hast §eerfolgreich  §6dem Konto von " + pi.getFirstName() + " " + pi.getLastName() + " " + betrag + "€ entfernt");
								} else if(args[1].equalsIgnoreCase("set")) {
									pi.setMoney(betrag);
									p.sendMessage("§6Du hast §eerfolgreich  §6dem Konto von " + pi.getFirstName() + " " + pi.getLastName() + " auf " + betrag + "€ gesetzt");
								} else
									p.sendMessage("§cBitte benutze /money [konto/hand] [add/remove/set] [Vorname_Nachname] [Betrag]");
							} else if(args[0].equalsIgnoreCase("hand")) {
								if(args[1].equalsIgnoreCase("add")) {
									pi.addMoneyInHand(betrag);
									p.sendMessage("§6Du hast §eerfolgreich  §6den Geldbeutel von " + pi.getFirstName() + " " + pi.getLastName() + " " + betrag + "€ hinzugefügt");
									t.sendMessage("§6Dir wurden von einem Admin " + betrag + "€ in dein Geldbeutel gelegt");
								} else if(args[1].equalsIgnoreCase("remove")) {
									pi.subtractMoneyInHand(betrag);
									p.sendMessage("§6Du hast §eerfolgreich  §6den Geldbeutel von " + pi.getFirstName() + " " + pi.getLastName() + " " + betrag + "€ entfernt");
								} else if(args[1].equalsIgnoreCase("set")) {
									pi.setMoneyInHand(betrag);
									p.sendMessage("§6Du hast §eerfolgreich  §6den Geldbeutel von " + pi.getFirstName() + " " + pi.getLastName() + " auf " + betrag + "€ gesetzt");
								} else
									p.sendMessage("§cBitte benutze /money [konto/hand] [add/remove/set] [Vorname_Nachname] [Betrag]");
							}
						} else 
							p.sendMessage("§cBitte gebe ein Betrag ohne einer 0 vorne an");
					} catch (Exception e) {
						p.sendMessage("§cBitte gebe als Betrag eine Zahl an");
					}
				}
			} else
				p.sendMessage("§cBitte benutze /money [konto/hand] [add/remove/set] [Vorname_Nachname] [Betrag]");
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	

}
