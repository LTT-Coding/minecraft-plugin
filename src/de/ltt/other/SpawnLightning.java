package de.ltt.other;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class SpawnLightning implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length <= 2) {
					if(args[0].equals("add")) {
						SQLData.addLightning(p.getLocation());
						Main.LightningLoc.add(p.getLocation());
						p.sendMessage("§aDu hast erfolgreich ein Blitz gesetzt!");
					} else if(args[0].equals("remove")) {
						try {
							int count = 0;
							for (Location lightning : Main.LightningLoc){
									if (lightning.distance(p.getLocation()) <= Integer.parseInt(args[1])) {
										SQLData.removeLightning(p.getLocation());
										Main.LightningLoc.remove(p.getLocation());
										count++;
									}
							}
							p.sendMessage("§eEs wurdem alle Blitze im Radius von §6" + args[1] + "m§e gelöscht");
							p.sendMessage("§6" + count + "§e Blitze wurden beseitigt!");
						} catch (Exception e) {
							p.sendMessage("§cDer angegebene Radius ist keine Zahl!");
						}
					} else if(args[0].equals("start")) {
						boolean lightningStart = Main.getPlugin().getConfig().getBoolean("Lightning");
						if(lightningStart) lightningStart = false;
						else
						if(!lightningStart) lightningStart = true;	
						Main.getPlugin().getConfig().set("Lightning", lightningStart);
						Main.getPlugin().saveConfig();
						Main.isLightningOn = Main.getPlugin().getConfig().getBoolean("Lightning");
						p.sendMessage("§eDu hast den Lightning Statuts auf = §6" + lightningStart + "§e gesetzt!");
						if(lightningStart) {
							Main.playLightning();
						}
					} else if(args[0].equals("list")){
						p.sendMessage("§e====Lightning====");
						for (Location loc : Main.LightningLoc) {
							p.sendMessage("§e X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ() + " Welt: " + loc.getWorld().getName());
						}
						p.sendMessage("§e=================");
					} else
						p.sendMessage("Benutze /lightning <add/remove/start>");
				} else
					p.sendMessage("Benutze /lightning <add/remove/start>");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
			
		}
		return false;
	}
	
	

}
