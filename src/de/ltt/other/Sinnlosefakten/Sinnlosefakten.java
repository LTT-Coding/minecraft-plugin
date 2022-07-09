package de.ltt.other.Sinnlosefakten;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.ltt.other.LagerFeuer.LagerfeuerInfo;
import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class Sinnlosefakten implements CommandExecutor{

	public static HashMap<Player, BukkitRunnable> FaktModus = new HashMap<Player, BukkitRunnable>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
				if(args.length >= 1) {
					if(Main.Admin.contains(p.getUniqueId().toString())) {
					if(args[0].equalsIgnoreCase("add")) {
						if(args.length >= 2){
						if(!Main.SinnloseFakten.contains(args[1])) {
							String wort = "";
							for (int i = 1; i < args.length; i++) {
								wort = wort + args[i];
	 	 						if(i < args.length - 1) {
	 	 							wort += " ";
	 	 						}
	 						}
							SQLData.addSinnlosefakten(wort);
							p.sendMessage("§aDu hast erfolgreich ein Sinnlosenfakt hinzugefügt");
						} else
							p.sendMessage("§cDiesen Fakt gibt es schon!");
						} else 
							p.sendMessage("§cBitte benutze /sinnlosefakten add/remove <Fakt>|| /sinnlosefakten list");
					} else if(args[0].equalsIgnoreCase("remove")) {
                        if(args.length >= 2){
                        	if(Main.SinnloseFakten.contains(args[1])) {
                            	String wort = "";
                            	for (int i = 1; i < args.length; i++) {
                            		wort = wort + args[i];
         	 						if(i < args.length - 1) {
         	 							wort += " ";
         	 						}
         						}
                            	SQLData.removeSinnlosefakten(wort);
                            	p.sendMessage("§aDu hast erfolgreich ein Sinnlosenfakt entfernt");
    						} else
    							p.sendMessage("§cDiesen Fakt gibt es nicht!");
                        } else 
							p.sendMessage("§cBitte benutze /sinnlosefakten add/remove <Fakt>|| /sinnlosefakten list");
						
					} else if(args[0].equalsIgnoreCase("list")) {
						p.sendMessage("§6========Sinnlosefakten========");
						for (int i = 0; i < Main.SinnloseFakten.size(); i++) {
							p.sendMessage("§7- §6" + Main.SinnloseFakten.get(i));
						}
						p.sendMessage("§6==============================");
					} else 
						p.sendMessage("§cBitte benutze /sinnlosefakten add/remove <Fakt>|| /sinnlosefakten list");
					} else 
						p.sendMessage(Main.KEINE_RECHTE_ADMIN);	
				} else if(args.length == 0) {
					if(FaktModus.containsKey(p)) {
						FaktModus.get(p).cancel();
						FaktModus.remove(p);
						p.sendMessage("§aDu bekommst keine Fakten mehr");
					} else {
						runSinnloseFakten(p);
						p.sendMessage("§aDu bekommst Fakten");
						Random r = new Random();
						 int Zahl = r.nextInt(Main.SinnloseFakten.size());
		            	 p.sendMessage("§6================================");
		            	 p.sendMessage("§6       Wusstest du schon:");
		            	 p.sendMessage("§6" + Main.SinnloseFakten.get(Zahl));
		            	 p.sendMessage("§6================================");
					}
				} else
					p.sendMessage("§cBitte benutze /sinnlosefakten add/remove <Fakt>|| /sinnlosefakten list");
					
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
    public void runSinnloseFakten(Player p){
    	FaktModus.put(p, new BukkitRunnable() {
             
             @Override
             public void run() {
            	 Random r = new Random();
				 int Zahl = r.nextInt(Main.SinnloseFakten.size());
            	 p.sendMessage("§6================================");
            	 p.sendMessage("§6       Wusstest du schon:");
            	 p.sendMessage("§6" + Main.SinnloseFakten.get(Zahl));
            	 p.sendMessage("§6================================");
             }
         });
    	FaktModus.get(p).runTaskTimer(Main.getPlugin(),(long) 900*20, (long) 900*20);
     }
	

}
