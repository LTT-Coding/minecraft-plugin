package de.ltt.server.reflaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ltt.server.main.Main;

public class LiftInfo {

	private static int liftIdn = 0;
	private int liftId;
	private double x;
	private double z;
	private List<Double> stages = new ArrayList<Double>();
	private String world;
	
	public LiftInfo() {
		if(Main.getPlugin().getConfig().get("Server.LiftIds") == null) return;
		liftIdn = Main.getPlugin().getConfig().getInt("Server.LiftIds");
	}
	
	public LiftInfo loadLift(int liftId) {
		if(!Main.getPlugin().getConfig().contains("Server.Lifts." + liftId))return this;
			x = Main.getPlugin().getConfig().getDouble("Server.Lifts." + liftId + ".x");
			z = Main.getPlugin().getConfig().getDouble("Server.Lifts." + liftId + ".z");
			stages = Main.getPlugin().getConfig().getDoubleList("Server.Lifts." + liftId + ".stages");
			world = Main.getPlugin().getConfig().getString("Server.Lifts." + liftId + ".world");
		return this;
	}
	
	public LiftInfo addLift(List<Location> lifts) {
		x = lifts.get(1).getX();
		z = lifts.get(1).getZ();
		world = lifts.get(1).getWorld().getName();
		for(Location current : lifts) {
			stages.add(current.getY());
		}
		Collections.sort(stages);
		liftId = liftIdn;
		liftIdn++;
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".x", x);
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".z", z);
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".stages", stages);
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".world", world);
		Main.getPlugin().getConfig().set("Server.LiftIds", liftIdn);
		Main.getPlugin().saveConfig();
		return this;
	}
	
	public Inventory liftInv() {
		int i;
		for(i = 0; i*9 < stages.size(); i++) {}
		Inventory inv  = Bukkit.createInventory(null, i*9, "§eWähle eine Etage");
		for(int l = 0; l < stages.size(); l++) {			
			String etage = "";
			if(l == 0) {
				etage = "Erdgeschoss";
			}else
				etage = l + "";
			ItemStack kopf = ItemSkulls.getSkull("http://textures.minecraft.net/texture/4d9b68915b1472d89e5e3a9ba6c935aae603d12c1454f3822825f43dfe8a2cac");
			ItemMeta kopfM = kopf.getItemMeta();
			kopfM.setDisplayName("§7" + etage);
			kopf.setItemMeta(kopfM);
			inv.addItem(kopf);
		}
		return inv;
		 
	}
	
	public void teleportToStage (Player p, int stage) {
		Location loc = new Location(Bukkit.getWorld(world), x, stages.get(stage), z);
		Location loc2 = loc;
		loc2.add(0.5, 0, 0);
		if(loc2.getBlock().getState() instanceof Sign) {
			loc = loc2;
		}else {
			loc2 = loc;
			loc2.subtract(0.5, 0, 0);
			if(loc2.getBlock().getState() instanceof Sign) {
				loc = loc2;
			}else
				loc2 = loc;
		}	
		loc2.add(0, 0, 0.5);
		if(loc2.getBlock().getState() instanceof Sign) {
			loc = loc2;
		}else {
			loc2.subtract(0, 0, 0.5);
			if(loc2.getBlock().getState() instanceof Sign) {
				loc = loc2;
			}else
				loc2 = loc;
		}

		p.teleport(loc);
	}
	
	public static boolean isLift(Location loc) {
		new LiftInfo();
		for(int i = 0; i < liftIdn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Lifts." + i)) {
				LiftInfo li = new LiftInfo().loadLift(i);
				if(li.getStages().contains(loc.getY()) && li.getX() == loc.getX() && li.getZ() == loc.getZ()) {
					return true;
				}
			}
		}
		return false;
	}

	public static LiftInfo getLift(Location loc) {
		for(int i = 0; i < liftIdn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Lifts." + i)) {
				LiftInfo li = new LiftInfo().loadLift(i);
				if(li.getStages().contains(loc.getY()) && li.getX() == loc.getX() && li.getZ() == loc.getZ()) {
					return li;
				}
			}
		}
		return null;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".x", x);
		Main.getPlugin().saveConfig();
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".z", z);
		Main.getPlugin().saveConfig();
	}

	public List<Double> getStages() {
		return stages;
	}

	public void setStages(List<Double> stages) {
		this.stages = stages;
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".stages", stages);
		Main.getPlugin().saveConfig();
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".world", world);
		Main.getPlugin().saveConfig();
	}
	
	public void removeLift() {
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".x", null);
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".z", null);
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".stages", null);
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId + ".world", null);
		Main.getPlugin().getConfig().set("Server.Lifts." + liftId , null);
		Main.getPlugin().saveConfig();
	}
	
	
	
	
	
}
