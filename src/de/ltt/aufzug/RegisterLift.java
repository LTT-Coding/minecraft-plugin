package de.ltt.aufzug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.LiftInfo;

public class RegisterLift implements Listener{
	
	public static HashMap<Player, List<Location>> selected = new HashMap<>();
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = e.getPlayer();
			if (Lift.registerLift.contains(p)) {
				if (e.getClickedBlock().getState() instanceof Sign) {
					if(!LiftInfo.isLift(e.getClickedBlock().getLocation())) {
						Sign sign = (Sign) e.getClickedBlock().getState();
						if(sign.getLine(0).equals("§6[EG][1]")) {
							if (RegisterLift.selected.containsKey(p)) {
								List<Location> selectedList = RegisterLift.selected.get(p);
								if(e.getClickedBlock().getX() == selectedList.get(0).getBlockX() && e.getClickedBlock().getZ() == selectedList.get(0).getBlockZ()) {
									if (!selectedList.contains(e.getClickedBlock().getLocation())) {
										selectedList.add(e.getClickedBlock().getLocation());
										RegisterLift.selected.put(p, selectedList);
										p.sendMessage("§aDer Block wurde hinzugefügt!");
									} else
										p.sendMessage("§cDiesen Block hast du bereits ausgewählt!");
								}else
									p.sendMessage("§cDie Schilder müssen übereinander sein!");
							} else {
								List<Location> selectedList = new ArrayList<>();
								selectedList.add(e.getClickedBlock().getLocation());
								RegisterLift.selected.put(p, selectedList);
								p.sendMessage("§aDer Block wurde hinzugefügt!");
							}
						}else {
							p.sendMessage("§cDieser Block kann kein Aufzug sein");
							p.sendMessage("§eDu Qualifiziert einen Block als Aufzug indem du ihn mit §6/lift§e in der ersten Zeile platzierst!");
						}
					} else
						p.sendMessage("§cDieser Block gehört bereits zu einem Aufzug!");
				}
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(e.getLine(0).equals("/lift")) {
			if(Main.Admin.contains(e.getPlayer().getUniqueId().toString())) {
				e.setLine(0, "§6[EG][1]");
				e.setLine(1, "§6[2][3]");
				e.setLine(2, "§6[4][5]");
				e.setLine(3, "§6[6][7]");
			}
		}
	}

}
