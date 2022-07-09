package de.ltt.areas;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;

public class HouseNumber implements CommandExecutor, Listener{

	public static ArrayList<String> selectSign = new ArrayList<String>();
	public static HashMap<String, String> streetMap = new HashMap<String, String>();
	public static HashMap<String, String> numberMap = new HashMap<String, String>();
 	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl,String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!selectSign.contains(p.getName())) {
				if(args.length == 3) {
					if(isStreet(args[1])) {
						String street = args[1];
						if(numberIsFree(street, args[2])) {
							if(args[2].length() < 15) {
								selectSign.add(p.getName());
								streetMap.put(p.getName(), street);
								numberMap.put(p.getName(), args[1]);
								p.sendMessage("§eWähle das Schild aus, auf dem die Hausnummer stehen soll!");
								p.sendMessage("§eDu kannst das Menü mit §6/housenumber cancel §eabbrechen!");
							}else
								p.sendMessage("§cDie Hausnummer ist zu lang!");
						}else
							p.sendMessage("§cDiese Hausnummer scheint es schon zu geben!");
					}else
						p.sendMessage("§cDie angegebene Straße scheint es nicht zu geben!");
				}else
					p.sendMessage("§cBenutze §6/housenumber add/remove [Straße] [Hausnummer] §cum eine Hausnummer festzulegen!");
			}else
				p.sendMessage("§cBenutze §6/housenumber cancel §c um den Vorgang abzubrechen!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
 	
	public boolean isStreet(String street) {
		if(street.length() > 15)return false;
		for(String current : Main.streets) {
			if(current.toLowerCase().equals(street.toLowerCase()))return true;
		}
		return false;
	}
	public boolean numberIsFree(String street, String number) {
		for(String current : Main.streetNumbers.get(street)) {
			if(current.toLowerCase().equals(number.toLowerCase()))return true;
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!selectSign.contains(e.getPlayer().getName()))return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)return;
		if(!(e.getClickedBlock().getState() instanceof Sign))return;
		Player p = e.getPlayer();
		if(p.getItemInHand().getType() == Material.AIR) {
			p.sendMessage("§cNimm die Sachen aus der Hand!");
			return;
		}
		Sign sign = (Sign) e.getClickedBlock().getState();
		String street = streetMap.get(p.getName());
		String number = numberMap.get(p.getName());
		sign.setLine(1, street);
		sign.setLine(2, number);
		sign.update();
	}

}
