package de.ltt.staat.medic.violations;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.ltt.server.reflaction.PlayerInfo;

public class VioListener implements Listener{

	@EventHandler
	public void onPlayerFall(EntityDamageEvent e) {
		if(e.getCause().equals(DamageCause.FALL)) {
			if(e.getEntity().getFallDistance() > 5) {
				PlayerInfo pi = new PlayerInfo((OfflinePlayer) e.getEntity());
				Violation vio = new Bruch(Position.SCHIENBEIN_LINKS, false);
				vio.hurt(true, (Player) e.getEntity());
				pi.addViolation(vio);
			}
			e.setCancelled(true);
			
			e.getEntity().setFallDistance(0);
		}
	}
}
