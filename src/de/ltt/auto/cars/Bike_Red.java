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

public class Bike_Red extends Car{
	public Bike_Red(String owner, String registerNumber, Location loc) {
		super(CarType.REDBIKE, 50D, 0.05D, 0.10D, owner, registerNumber, 40D, true, loc, 4, 30);
		
	}
	
	public Bike_Red(UUID id) {
		super(id);
		carType = CarType.REDBIKE;
	}

	@Override
	public void move() {
		loc = horse.getLocation();
		ArmorStand stand1 = stands.get(0);
		stand1.teleport(loc.subtract(0, 0.8, 0));
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
				p.sendMessage("§eDu bist auf dem Motorrad.");
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
		ArmorStand stand1 = loc.getWorld().spawn(loc.subtract(0, 0.8, 0), ArmorStand.class);
		stand1.setVisible(false);
		stand1.setInvulnerable(true);
		stand1.setCollidable(false);
		stand1.setGravity(false);
		stand1.setHelmet(new ItemBuilder(Material.HEART_OF_THE_SEA).setCustomModelData(10000023).build());
		stands.add(stand1);
	}

}
