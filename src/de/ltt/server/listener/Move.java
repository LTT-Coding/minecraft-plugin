package de.ltt.server.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import de.ltt.auto.SpawnCar;
import de.ltt.server.reflaction.PlayerInfo;

public class Move implements Listener{

	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		PlayerInfo pi = new PlayerInfo(p);
		if(pi.isstunned()) {
			if(!pi.ispacked()) {
				if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() > e.getTo().getY() ||e.getFrom().getZ() != e.getTo().getZ()) {
					e.setCancelled(true);
					p.sendMessage("§cDu kannst dich nicht bewegen, du bist gefesselt!");
				}
			}
		}else if(!pi.canJump()) {
			if(e.getFrom().getY() < e.getTo().getY()) {
				Location to = e.getTo();
				to.setY(to.getY() + 1);
				if(to.getBlock().isEmpty()) {
					e.setCancelled(true);
					p.sendMessage("§cDu wirst gefesselt, du kannst nicht Springen!");
				}
			}
		}
		
	}
}
