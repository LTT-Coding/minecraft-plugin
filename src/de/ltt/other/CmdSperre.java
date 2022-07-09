package de.ltt.other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;



public class CmdSperre implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
 				if(args.length >= 1) {
 					if(args[0].equals("add")) {
 						String command = "";
 	 					for (int i = 1; i < args.length; i++) {
 	 						command = command + args[i];
 	 						if(i < args.length - 1) {
 	 							command += " ";
 	 						}
 						}
 	 					if(!command.startsWith("/")) {
 	 						command = "/" + command;
 	 					}
 	 					SQLData.addBlockedCMD(command);
 	 					p.sendMessage("§aDu hast erfolgreich einen neuen gesperrten Command hinzugefügt");
 					} else if(args[0].equals("list")){
 						if(Main.GesperrteCMD.size() != 0) {
 							p.sendMessage("§6============CommandSperre============");
 							for (int i = 0; i < Main.GesperrteCMD.size(); i++) {
 								p.sendMessage("§7- §6" + Main.GesperrteCMD.get(i));
 							}
 							
 							p.sendMessage("§6=====================================");
 						} else 
 							p.sendMessage("§aEs gibt keine gesperrten Commands");
 					} else if(args[0].equals("remove")) {
 						String command = "";
 	 					for (int i = 1; i < args.length; i++) {
 	 						
 	 						command = command + args[i];
 	 						if(i < args.length - 1) {
 	 							command += " ";
 	 						}
 	 					}
 	 					if(!command.startsWith("/")) {
 	 						command = "/" + command;
 	 					}
 	 					SQLData.removeBlockedCMD(command);
 	 					p.sendMessage("§aDu hast erfolgreich ein gesperrten Command entfernt");
 					} else
 						p.sendMessage("§cBitte benutze §6/cmdsperre add/remove/list <Command> §cum ein Command zu sperren!");
 				} else 
 					p.sendMessage("§cBitte benutze §6/cmdsperre add/remove/list <Command> §cum ein Command zu sperren!");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else 
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
}
