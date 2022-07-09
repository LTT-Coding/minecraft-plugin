package de.ltt.aufzug;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.reflaction.LiftInfo;

public class RemoveLift implements Listener{
	
	 @EventHandler(priority = EventPriority.LOW)
	 public void onPlayerInteract(PlayerInteractEvent e) {
		 if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			 Player p = e.getPlayer();
			 if(Lift.removeLift.contains(p)) {
				 if(LiftInfo.isLift(e.getClickedBlock().getLocation())) {
					LiftInfo li = LiftInfo.getLift(e.getClickedBlock().getLocation());
					li.removeLift();
					p.sendMessage("§aDer Aufzug wurde entfernt!");
					e.setCancelled(true);
				 }
				 Lift.removeLift.remove(p);
			 }
		 }
	 }
}
