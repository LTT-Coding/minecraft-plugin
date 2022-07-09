package de.ltt.aufzug;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.reflaction.LiftInfo;

public class ClickOnAufzug implements Listener{
	
	public static HashMap<Player, LiftInfo> selectedLift = new HashMap<>();
	public static HashMap<Player, BlockFace> faces = new HashMap<>();
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!Lift.registerLift.contains(p) && !Lift.removeLift.contains(p)) {
			if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (LiftInfo.isLift(e.getClickedBlock().getLocation())) {
					LiftInfo li = LiftInfo.getLift(e.getClickedBlock().getLocation());
					p.openInventory(li.liftInv());
					selectedLift.put(p, li);
					BlockFace face = e.getBlockFace();
					faces.put(p, face);
				}
			}
		}
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
			if(e.getView().getTitle().equals("§eWähle eine Etage")) {
				LiftInfo li = selectedLift.get(p);
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Erdgeschoss")) {
					li.teleportToStage(p, 0);
					Location loc = p.getLocation();
					BlockFace face = faces.get(p);
					loc = loc.toVector().add(face.getDirection().multiply(1)).toLocation(p.getWorld());
					loc.setDirection(face.getDirection().rotateAroundY(135));
					p.teleport(loc);
				}else {
					String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§7", "");
					int stage = Integer.parseInt(name);
					li.teleportToStage(p, stage);
					Location loc = p.getLocation();
					BlockFace face = faces.get(p);
					loc = loc.toVector().add(face.getDirection().multiply(1)).toLocation(p.getWorld());
					loc.setDirection(face.getDirection().rotateAroundY(135));
					p.teleport(loc);
				}
				e.setCancelled(true);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

}
