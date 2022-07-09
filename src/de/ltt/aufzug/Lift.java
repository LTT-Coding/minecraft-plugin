package de.ltt.aufzug;

import java.util.List;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.LiftInfo;

public class Lift implements CommandExecutor {

	public static List<Player> registerLift = new ArrayList<>();
	public static List<Player> removeLift = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1) {
				if(Main.Admin.contains(p.getUniqueId().toString())) {
					if(args[0].equalsIgnoreCase("register")) {
						if(!registerLift.contains(p) ) {
							if(!removeLift.contains(p)) {
								registerLift.add(p);
								p.sendMessage("§eWähle die Schilder aus, die den Aufzug darstellen!");
							}else
								p.sendMessage("§cDu entfernst bereits einen Lift!");
						}else
							p.sendMessage("§cDu registrierst bereits einen Lift!");
					}else if(args[0].equalsIgnoreCase("remove")) {
						if(!registerLift.contains(p)) {
							if(!removeLift.contains(p)) {
								removeLift.add(p);
								p.sendMessage("§eWähle den Aufzug aus, den du löschen möchtest!");
							}else
								p.sendMessage("§cDu entfernst bereits einen Lift!");
						}else
							p.sendMessage("§cDu registrierst bereits einen Lift!");
					}else if(args[0].equalsIgnoreCase("finish")) {
						if (registerLift.contains(p)) {
							if(RegisterLift.selected.containsKey(p)) {
								if(RegisterLift.selected.get(p).size() > 1) {
									new LiftInfo().addLift(RegisterLift.selected.get(p));
									p.sendMessage("§aDer Aufzug wurde registriert!");
									registerLift.remove(p);
									RegisterLift.selected.remove(p);
								}else
									p.sendMessage("§cWähle mindestens §62§cAufzüge!");
							}else
								p.sendMessage("§cWähle zuerst die Aufzüge!");
						}else
							p.sendMessage("§cBenutze zuerst §6/registerLift§c!");
					}else if(args[0].equalsIgnoreCase("cancel")) {
						if(registerLift.contains(p)) {
							registerLift.remove(p);
							p.sendMessage("§aDu registrierst keinen Lift mehr!");
							if(RegisterLift.selected.containsKey(p)) {
								RegisterLift.selected.remove(p);
							}
						}
						if(removeLift.contains(p)) {
							p.sendMessage(removeLift + "");
							removeLift.remove(p);
							p.sendMessage("§aDu entfernst keinen Lift mehr!");
						}
					}else
						p.sendMessage("§cBenutze §6/lift register/remove/cancel§c!");
				}else
					p.sendMessage(Main.KEINE_RECHTE_ADMIN);
			}else
				p.sendMessage("§cBenutze §6/lift register/remove/cancel§c!");
		}
		return false;
	}

}
