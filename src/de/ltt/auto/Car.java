package de.ltt.auto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.ltt.auto.cars.Bike_Red;
import de.ltt.auto.cars.RTW;
import de.ltt.other.InvSpeichern.Serialization;
import de.ltt.server.main.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class Car {

	public static final double MULTIPLCIATOR = 0.0625;

	public static List<Car> cars = new ArrayList<Car>();

	protected UUID id;
	protected CarType carType;
	protected double maxSpeed;
	protected double currentSpeed;
	protected boolean gear;
	protected double acceleration;
	protected double deceleration;
	protected double rotation;
	protected List<Bauteil> bauteile;
	protected String owner;
	protected CarOwn type;
	protected int ownFirm;
	protected int wantedSpeed;
	protected String registerNumber;
	protected double fuel;
	protected double maxFuel;
	protected Inventory trunk;
	protected boolean hasTrunk;
	protected boolean isFullBreak;
	protected int trunkSpace;
	protected List<ArmorStand> stands;
	protected Horse horse;
	protected Location loc;
	protected Player rider;
	protected BukkitRunnable runnable;
	protected ArmorStand horseStand;

	public Car(CarType carType, double maxSpeed, double acceleration, double deceleration, String owner,
			String registerNumber, double maxFuel, boolean hasTrunk, Location loc, int trunkSpace, double rotation) {
		this.carType = carType;
		this.maxSpeed = maxSpeed/5;
		this.currentSpeed = 0D;
		this.owner = owner;
		this.type = CarOwn.PRIVATE;
		this.wantedSpeed = 0;
		this.registerNumber = registerNumber;
		this.maxFuel = maxFuel;
		this.fuel = maxFuel;
		this.hasTrunk = hasTrunk;
		this.trunkSpace = trunkSpace;
		this.loc = loc;
		this.id = UUID.randomUUID();
		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.isFullBreak = false;
		this.gear = true;
		this.rotation = rotation;
		int i;
		for (i = 0; i * 9 < trunkSpace; i++) {
		}

		trunk = Bukkit.createInventory(null, i * 9, "§eKofferraum?");
		
	}
	
	public Car(UUID id) {
		this.id = id;
	}
	
	public static Car loadCar(UUID id) {
		FileConfiguration config = Main.getPlugin().getCarConfig();
		CarType carType = CarType.valueOf(config.getString(id + ".carType"));
		double maxSpeed = config.getDouble(id + ".maxSpeed");
		double acceleration = config.getDouble(id + ".acceleration");
		double deceleration = config.getDouble(id + ".deceleration");
		double rotation = config.getDouble(id + ".rotation");
		double fuel = config.getDouble(id + ".fuel");
		double maxFuel = config.getDouble(id + ".maxFuel");
		String content = Main.getPlugin().getInvConfig().getString(id + ".trunk.items");
		ItemStack[] items = Serialization.base64ToInv(content);
		Inventory inv = Bukkit.createInventory(null, items.length, "§6Kofferraum");
		inv.setContents(items);
		boolean hasTrunk = config.getBoolean(id + ".hasTrunk");
		int trunkSpace = config.getInt(id + ".trunkSpace");
		CarOwn type = CarOwn.valueOf(config.getString(id + ".ownType"));
		
		List<String> standList = config.getStringList(id + ".stands");
		List<ArmorStand> stands = new ArrayList<ArmorStand>();
		for(String current : standList) {
			stands.add((ArmorStand) Bukkit.getEntity(UUID.fromString(current)));
		}
		boolean gear = config.getBoolean(id + ".gear");
		String registerNumber = config.getString(id + ".registerNumber");
		int ownFirm = 0;
		String owner = "";
		if(type == CarOwn.FIRM) {
			ownFirm = config.getInt(id + ".owner");
		}else {
			owner = config.getString(id + ".owner");
		}
		Car car = null;
		switch (carType) {
		case MEDIC:
			car = new RTW(id);
			break;
		case POLICE:
			break;
		case REDBIKE:
			car = new Bike_Red(id);
			break;
		case ZIVI1:
			break;
		}
		car.setMaxSpeed(maxSpeed);
		car.setAcceleration(acceleration);
		car.setDeceleration(deceleration);
		car.setRotation(rotation);
		car.setFuel(fuel);
		car.setMaxFuel(maxFuel);
		car.setTrunk(inv);
		car.setHasTrunk(hasTrunk);
		car.setTrunkSpace(trunkSpace);
		car.setType(type);
		car.setStands(stands);
		car.setRegisterNumber(registerNumber);
		car.setGear(gear);
		if(type == CarOwn.FIRM) {
			car.setOwnFirm(ownFirm);
		}else {
			car.setOwner(owner);
		}
		car.setLoc(stands.get(0).getLocation());
		return car;
	}

	public void spawnHorse() {
		horse = loc.getWorld().spawn(loc, Horse.class);
		horse.setAdult();
		horse.setAgeLock(true);
		horse.setCustomNameVisible(true);
		horse.setCustomName("I'm a horse!");
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		horse.setAI(false);
		horse.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, true));
		horse.setJumpStrength(0);
		horse.setSilent(true);
		horse.setBreed(false);
		horseStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		horse.setPassenger(horseStand);
		horseStand.setVisible(false);
	}

	public abstract void spawnCar();

	public void startMove() {
		runnable = new BukkitRunnable() {

			@Override
			public void run() {
				move();
				if(!isFullBreak) {
					if (wantedSpeed > currentSpeed) {
						if(currentSpeed > maxSpeed) {
							currentSpeed = maxSpeed;
						}else if(currentSpeed < maxSpeed) {
							if (currentSpeed + acceleration < wantedSpeed) {
								currentSpeed += acceleration;
							} else {
								currentSpeed = wantedSpeed;
							}
						}
					} else if (wantedSpeed < currentSpeed) {
						if (currentSpeed - deceleration > wantedSpeed) {
							currentSpeed -= deceleration;
						} else {
							currentSpeed = wantedSpeed;
						}
					}
				}else {
					if (wantedSpeed < currentSpeed) {
						if (currentSpeed - (deceleration*2) > wantedSpeed) {
							currentSpeed -= deceleration*2;
						} else {
							currentSpeed = wantedSpeed;
						}
					}
				}
	            Vector targetDirection = rider.getLocation().getDirection();
	            Vector direction = horseStand.getLocation().getDirection();
	            double moveDistanceSquared = rotation * rotation;
	            moveDistanceSquared *= currentSpeed * 15;
	            double distanceSquared = direction.distanceSquared(targetDirection);
	            if (distanceSquared <= moveDistanceSquared) {
	                direction = targetDirection;
	            } else {
	                targetDirection = targetDirection.subtract(direction).normalize().multiply(moveDistanceSquared);
	                direction.add(targetDirection).normalize();
	            }
	            Location loc = horseStand.getLocation(); 
	            loc.setDirection(direction);
				horseStand.setRotation(loc.getYaw(), 0);
				//horseStand.setRotation(rider.getLocation().getYaw(), 0);
				
				if (currentSpeed == 0)
					isFullBreak = false;
				loc = horse.getLocation();
				int i = 1;
				if(!gear)i=-1;
				horse.setVelocity(loc.getDirection().multiply(currentSpeed * MULTIPLCIATOR).multiply(i).setY(-10));
				rider.spigot().sendMessage(ChatMessageType.ACTION_BAR,
						TextComponent.fromLegacyText("§6" + (int) (currentSpeed * 5)));

			}
		};
		runnable.runTaskTimer(Main.getPlugin(), 0, 1);
	}

	public abstract void move();

	public void stopMove() {
		runnable.cancel();
		horse.remove();
		horseStand.remove();
	}

	public abstract void clickStand(ArmorStand stand, Player p);

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(double currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public List<Bauteil> getBauteile() {
		return bauteile;
	}

	public void setBauteile(List<Bauteil> bauteile) {
		this.bauteile = bauteile;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public CarOwn getType() {
		return type;
	}

	public void setType(CarOwn type) {
		this.type = type;
	}

	public int getOwnFirm() {
		return ownFirm;
	}

	public void setOwnFirm(int ownFirm) {
		this.ownFirm = ownFirm;
	}

	public int getWantedSpeed() {
		return wantedSpeed;
	}

	public void setWantedSpeed(int wantedSpeed) {
		this.wantedSpeed = wantedSpeed;
	}

	public String getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	public double getFuel() {
		return fuel;
	}

	public void setFuel(double fuel) {
		this.fuel = fuel;
	}

	public double getMaxFuel() {
		return maxFuel;
	}

	public void setMaxFuel(double maxFuel) {
		this.maxFuel = maxFuel;
	}

	public Inventory getTrunk() {
		return trunk;
	}

	public void setTrunk(Inventory trunk) {
		this.trunk = trunk;
	}

	public boolean isHasTrunk() {
		return hasTrunk;
	}

	public void setHasTrunk(boolean hasTrunk) {
		this.hasTrunk = hasTrunk;
	}

	public List<ArmorStand> getStands() {
		return stands;
	}

	public void setStands(List<ArmorStand> stands) {
		this.stands = stands;
	}

	public Horse getHorse() {
		return horse;
	}

	public void setHorse(Horse horse) {
		this.horse = horse;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Player getRider() {
		return rider;
	}

	public void setRider(Player rider) {
		this.rider = rider;
	}

	public void setFullBreak(boolean isFullBreak) {
		this.isFullBreak = isFullBreak;
	}

	public boolean isFullBreak() {
		return isFullBreak;
	}

	public boolean isGear() {
		return gear;
	}

	public void setGear(boolean gear) {
		this.gear = gear;
	}
	
	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getDeceleration() {
		return deceleration;
	}

	public void setDeceleration(double deceleration) {
		this.deceleration = deceleration;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public int getTrunkSpace() {
		return trunkSpace;
	}

	public void setTrunkSpace(int trunkSpace) {
		this.trunkSpace = trunkSpace;
	}

	public CarType getCarType() {
		return carType;
	}

	public UUID getId() {
		return id;
	}
	
	
	public static Car getCar(ArmorStand stand) {
		for (Car car : cars) {
			for (ArmorStand stands : car.getStands()) {
				if (stands.equals(stand)) {
					return car;
				}
			}
		}
		return null;
	}

	public static boolean isCar(ArmorStand stand) {
		if (getCar(stand) != null)
			return true;
		return false;
	}

	public static Car getCar(Player p) {
		for (Car car : cars) {
			if (car.getRider() != null)
				if (car.getRider().equals(p))
					return car;
		}
		return null;
	}

	public static boolean isCar(Player p) {
		if (getCar(p) != null)
			return true;
		return false;
	}
	

}
