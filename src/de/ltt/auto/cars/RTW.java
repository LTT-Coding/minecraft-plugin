package de.ltt.auto.cars;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import de.ltt.auto.Car;
import de.ltt.auto.CarType;
import de.ltt.server.reflaction.ItemBuilder;

public class RTW extends Car{
	public RTW(String owner, String registerNumber, Location loc) {
		super(CarType.MEDIC, 80D, 0.02D, 0.07D, owner, registerNumber, 100D, true, loc, 40, 0.05);
		
	}
	public RTW(UUID id) {
		super(id);
		carType = CarType.MEDIC;
	}
	
	@Override
	public void move() {
		loc = horse.getLocation();
		ArmorStand stand1 = stands.get(0);
		ArmorStand stand2 = stands.get(1);
		ArmorStand stand3 = stands.get(2);
		ArmorStand stand4 = stands.get(3);
		Location loc1 = loc.toVector().add(loc.getDirection().multiply(2).setY(0).rotateAroundY(67.5)).toLocation(loc.getWorld()).subtract(0, 1, 0);
		Location loc2 = loc.toVector().add(loc.getDirection().multiply(2).setY(0).rotateAroundY(135)).toLocation(loc.getWorld()).subtract(0, 1, 0);
		Location loc3 = loc1.toVector().add(loc.getDirection().multiply(2).setY(0).rotateAroundY(135)).toLocation(loc.getWorld());
		stand1.teleport(loc);
		stand2.teleport(loc1);
		stand3.teleport(loc2);
		stand4.teleport(loc3);
	}
	
	@Override
	public void clickStand(ArmorStand stand, Player p) {
		if(stands.contains(stand)) {
			for(ArmorStand current : stands) {
				if(current.getPassenger() != null)
					if(current.getPassenger().equals(p))
						return;
			}
			p.sendMessage(stands.indexOf(stand) + "");
			if(stands.indexOf(stand) == 0) {
				rider = p;
				stand.setPassenger(p);
				p.sendMessage("§eDu bist in das Auto eingestiegen.");
				spawnHorse();
				startMove();
				p.getInventory().setHeldItemSlot(4);

			}else {
				stand.setPassenger(p);
			}
		}
	}
	
	@Override
	public void spawnCar() {
		this.stands = new ArrayList<ArmorStand>();
		Location loc1 = loc.toVector().add(loc.getDirection().multiply(2).setY(0).rotateAroundY(67.5)).toLocation(loc.getWorld());
        Location loc2 = loc.toVector().add(loc.getDirection().multiply(2).setY(0).rotateAroundY(135)).toLocation(loc.getWorld());
        Location loc3 = loc1.toVector().add(loc.getDirection().multiply(2).setY(0).rotateAroundY(135)).toLocation(loc.getWorld());
		ArmorStand stand1 = loc.getWorld().spawn(loc, ArmorStand.class);
		stand1.setVisible(false);
		stand1.setInvulnerable(true);
		stand1.setCollidable(false);
		stand1.setGravity(false);
		stand1.setHelmet(new ItemBuilder(Material.HEART_OF_THE_SEA).setCustomModelData(10000022).build());
		ArmorStand stand2 = loc.getWorld().spawn(loc1, ArmorStand.class);
		stand2.setVisible(false);
		stand2.setInvulnerable(true);
		stand2.setCollidable(false);
		stand2.setGravity(false);
		ArmorStand stand3 = loc.getWorld().spawn(loc2, ArmorStand.class);
		stand3.setVisible(false);
		stand3.setInvulnerable(true);
		stand3.setCollidable(false);
		stand3.setGravity(false);
		ArmorStand stand4 = loc.getWorld().spawn(loc3, ArmorStand.class);
		stand4.setVisible(false);
		stand4.setInvulnerable(true);
		stand4.setCollidable(false);
		stand4.setGravity(false);
		stands.add(stand1);
		stands.add(stand2);
		stands.add(stand3);
		stands.add(stand4);
	}
	

}
