package de.ltt.auto;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import de.ltt.server.listener.PlayerDismount;

public class CarHandler implements Listener {

	@EventHandler
	public void onPlayerClick(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof ArmorStand) {
			ArmorStand stand = (ArmorStand) e.getRightClicked();
			if (Car.isCar(stand)) {
				Car car = Car.getCar(stand);
				car.clickStand(stand, e.getPlayer());
				e.setCancelled(true);
			}
		}
		
	}

	@EventHandler
	public void onPlayerDismount(EntityDismountEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDismounted() instanceof ArmorStand) {
				ArmorStand stand = (ArmorStand) e.getDismounted();
				if (!Car.isCar(stand))return;
				Car car = Car.getCar(stand);
				if (car.getCurrentSpeed() != 0)return;
				if (car.getStands().indexOf(stand) != 0)return;
				car.stopMove();
				car.setRider(null);
			}
		}
	}

	@EventHandler
	public void onPlayerScroll(PlayerItemHeldEvent e) {
		if (Car.isCar(e.getPlayer())) {
			e.setCancelled(true);
			Car car = Car.getCar(e.getPlayer());
			if(!car.isFullBreak()) {
				//e.getPlayer().sendMessage("s");
				int wantedSpeed = car.getWantedSpeed();
				if (e.getNewSlot() > 4) {
					wantedSpeed--;
				} else if (e.getNewSlot() < 4) {
					wantedSpeed++;
				}
				if(wantedSpeed >= 0)
					car.setWantedSpeed(wantedSpeed);
			}
		}
	}
	
	@EventHandler 
	public void onFullBreak(EntityDismountEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(Car.isCar(p)) {
				Car car = Car.getCar(p);
				if(car.getCurrentSpeed() != 0) {
					car.setWantedSpeed(0);
					car.setFullBreak(true);
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onItemSwap(PlayerSwapHandItemsEvent e) {
		if(Car.isCar(e.getPlayer())) {
			e.setCancelled(true);
			Car car = Car.getCar(e.getPlayer());
			if(car.getCurrentSpeed() == 0) {
				if(car.isGear())car.setGear(false);
				else car.setGear(true);
				e.getPlayer().sendMessage("§eDu hast den Gang gewechselt!");
			}else
				e.getPlayer().sendMessage("§cDu musst stehen um den Gang zu wechseln!");
		}
	}

}
