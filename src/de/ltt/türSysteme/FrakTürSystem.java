package de.ltt.t�rSysteme;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Openable;

import de.ltt.server.main.Main;

public class FrakT�rSystem{

	
	public static void checkDoor(Location locDoor_ObereT�rBlock) {
		Location RDoorLoc = new Location(locDoor_ObereT�rBlock.getWorld(), locDoor_ObereT�rBlock.getX() + 1, locDoor_ObereT�rBlock.getY(), locDoor_ObereT�rBlock.getZ());
		Location LDoorLoc = new Location(locDoor_ObereT�rBlock.getWorld(), locDoor_ObereT�rBlock.getX() - 1, locDoor_ObereT�rBlock.getY(), locDoor_ObereT�rBlock.getZ());;
		Location RDoorLocZ = new Location(locDoor_ObereT�rBlock.getWorld(), locDoor_ObereT�rBlock.getX(), locDoor_ObereT�rBlock.getY(), locDoor_ObereT�rBlock.getZ() + 1);;
		Location LDoorLocZ = new Location(locDoor_ObereT�rBlock.getWorld(), locDoor_ObereT�rBlock.getX(), locDoor_ObereT�rBlock.getY(), locDoor_ObereT�rBlock.getZ() - 1);;
		if(RDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
			openDoor(locDoor_ObereT�rBlock, RDoorLoc, 5);
		} else if(RDoorLocZ.getBlock().getType().toString().toLowerCase().contains("_door")) {
			openDoor(locDoor_ObereT�rBlock, RDoorLocZ, 5);
		} else if(LDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
			openDoor(locDoor_ObereT�rBlock, LDoorLoc, 5);
		} else if(LDoorLocZ.getBlock().getType().toString().toLowerCase().contains("_door")) {
			openDoor(locDoor_ObereT�rBlock, LDoorLocZ, 5);
		} else {
			openDoor(locDoor_ObereT�rBlock, 5);
		}
	}
	
	public static void openDoor(Location locDoor_ObereT�rBlock, Location locDoor2_ObereT�rBlock, int runTaskLater) {
		 if(locDoor_ObereT�rBlock.getBlock().getType().toString().toLowerCase().contains("_door") && locDoor2_ObereT�rBlock.getBlock().getType().toString().toLowerCase().contains("_door")) {
			BlockState state = locDoor_ObereT�rBlock.getBlock().getState();
			Openable openable = (Openable) state.getBlockData();
			BlockState state2 = locDoor2_ObereT�rBlock.getBlock().getState();
			Openable openable2 = (Openable) state2.getBlockData();
			if (!openable.isOpen()) {
				openable.setOpen(true);
				openable2.setOpen(true);
				state.setBlockData(openable);
				state2.setBlockData(openable2);
				state.update();
				state2.update();
				Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						if(openable.isOpen()) {
							openable.setOpen(false);
							openable2.setOpen(false);
							state.setBlockData(openable);
							state2.setBlockData(openable2);
							state.update();
							state2.update();
						}
					}
				}, runTaskLater*20);
			}
		}
	}
	
	public static void openDoor(Location locDoor_ObereT�rBlock, int runTaskLater) {
		if(locDoor_ObereT�rBlock.getBlock().getType().toString().toLowerCase().contains("_door")) {
			BlockState state = locDoor_ObereT�rBlock.getBlock().getState();
			Openable openable = (Openable) state.getBlockData();
			if (!openable.isOpen()) {
				openable.setOpen(true);
				state.setBlockData(openable);
				state.update();
				Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						if(openable.isOpen()) {
							openable.setOpen(false);
							state.setBlockData(openable);
							state.update();
						}
					}
				}, runTaskLater*20);
			}
		}
	}
}
