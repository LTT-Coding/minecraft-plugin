package de.ltt.staat.police.policeComputer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class RegisterComputer implements CommandExecutor, Listener{

	public static List<Player> addPC = new ArrayList<Player>();
	public static List<Player> removePC = new ArrayList<Player>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					if(args[0].equals("add")) {
						p.sendMessage("§6Mach ein rechtsklick auf den Computer");
						addPC.add(p);
					} else if(args[0].equals("remove")) {
						p.sendMessage("§6Mach ein rechtsklick auf den Computer");
						removePC.add(p);
					} else 
						p.sendMessage("§cBitte benutze /polizeicomputer add/remove");
				} else 
					p.sendMessage("§cBitte benutze /polizeicomputer add/remove");
			} else 
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else 
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	@EventHandler
	public void onPoliceComputer_R_A(PlayerInteractEvent e) {
		if(addPC.contains(e.getPlayer())) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(!Main.PoliceComputerLoc.contains(e.getClickedBlock().getLocation())) {
					e.getPlayer().sendMessage("§aDu hast erfolgreich ein Polizei Computer eingerichtet");
					SQLData.addPoliceComputer(e.getClickedBlock().getLocation());
					addPC.remove(e.getPlayer());
				} else {
					e.getPlayer().sendMessage("§cHier ist schon ein Polizei Computer");
					addPC.remove(e.getPlayer());
				}
			}
		} else if(removePC.contains(e.getPlayer())) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(Main.PoliceComputerLoc.contains(e.getClickedBlock().getLocation())) {
					e.getPlayer().sendMessage("§aDu hast erfolgreich ein Polizei Computer entfernt");
					SQLData.removePoliceComputer(e.getClickedBlock().getLocation());
					removePC.remove(e.getPlayer());
				} else {
					e.getPlayer().sendMessage("§cHier ist kein Polizei Computer");
					removePC.remove(e.getPlayer());
				}
			
			}
		}
	}

}
