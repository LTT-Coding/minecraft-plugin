package de.ltt.navi;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class RegisterNaviPoint implements CommandExecutor, Listener{

	
	public static HashMap<Player, String> nameMap = new HashMap<Player, String>();
	public static HashMap<Player, Location> pointMap = new HashMap<Player, Location>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length >= 1) {
				if(args[0].equalsIgnoreCase("cancel")) {
					if(nameMap.containsKey(p)) {
						nameMap.remove(p);
						pointMap.remove(p);
						p.sendMessage("§4Vorgang abgebrochen!");
					}else
						p.sendMessage("§cDu registrisert keinen Punkt!");
				}else if(args[0].equalsIgnoreCase("remove")) {
					if(args.length >= 2) {
						String name = "";
						for(int i = 1; i < args.length; i++) {
							name += args[i];
							if(i < args.length - 1) {
								name += " ";
							}
						}
						if(Main.naviPoints.containsKey(name)) {
							SQLData.removeNaviPoint(name);
							p.sendMessage("§aPunkt wurde entfernt!");
						}else
							p.sendMessage("§cDieser Punkt existiert nicht!");
					}
				}else if(args[0].equalsIgnoreCase("add")){
					if(args.length >= 2) {
						Location loc = p.getLocation().subtract(0, 1, 0);
						String name = "";
						for(int i = 1; i < args.length; i++) {
							name += args[i];
							if(i < args.length - 1) {
								name += " ";
							}
						}
						if(!Main.naviPoints.containsKey(name)){
							if(name.length() <= 100) {
								pointMap.put(p, loc);
								nameMap.put(p, name);
								p.sendMessage("§eKlicke mit dem Item in die Luft, mit welchem der Punkt angezeigt werden soll!");
							}else
								p.sendMessage("§cDer Name darf maimal §6100§c Zeichen haben!");
						}else
							p.sendMessage("§cEs gibt bereits einen Punkt mit diesem Namen!");
					}
				}
			}else
				p.sendMessage("§cBenutze §6/registernavipoint add/remove/<Name>§c um einen Punkt zu registrieren!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(pointMap.containsKey(p)) {
			if(e.getItem().getType() != Material.AIR) {
				SQLData.addNaviPoint(pointMap.get(p), nameMap.get(p), e.getItem().getType());
				pointMap.remove(p);
				nameMap.remove(p);
				p.sendMessage("§aPunkt wurde registriert!");
				e.setCancelled(true);
			}else {
				p.sendMessage("§cDu musst ein Item in der Hand haben!");
				p.sendMessage("§cVorgang wurde abgebrochen!");
			}
			
		}
	}
	
	

}
