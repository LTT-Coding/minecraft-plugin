package de.ltt.türSysteme;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Openable;

import de.ltt.server.main.Main;

public class FrakTürSystem{

	
	public static void checkDoor(Location locDoor_ObereTürBlock) {
		Location RDoorLoc = new Location(locDoor_ObereTürBlock.getWorld(), locDoor_ObereTürBlock.getX() + 1, locDoor_ObereTürBlock.getY(), locDoor_ObereTürBlock.getZ());
		Location LDoorLoc = new Location(locDoor_ObereTürBlock.getWorld(), locDoor_ObereTürBlock.getX() - 1, locDoor_ObereTürBlock.getY(), locDoor_ObereTürBlock.getZ());;
		Location RDoorLocZ = new Location(locDoor_ObereTürBlock.getWorld(), locDoor_ObereTürBlock.getX(), locDoor_ObereTürBlock.getY(), locDoor_ObereTürBlock.getZ() + 1);;
		Location LDoorLocZ = new Location(locDoor_ObereTürBlock.getWorld(), locDoor_ObereTürBlock.getX(), locDoor_ObereTürBlock.getY(), locDoor_ObereTürBlock.getZ() - 1);;
		if(RDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
			openDoor(locDoor_ObereTürBlock, RDoorLoc, 5);
		} else if(RDoorLocZ.getBlock().getType().toString().toLowerCase().contains("_door")) {
			openDoor(locDoor_ObereTürBlock, RDoorLocZ, 5);
		} else if(LDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
			openDoor(locDoor_ObereTürBlock, LDoorLoc, 5);
		} else if(LDoorLocZ.getBlock().getType().toString().toLowerCase().contains("_door")) {
			openDoor(locDoor_ObereTürBlock, LDoorLocZ, 5);
		} else {
			openDoor(locDoor_ObereTürBlock, 5);
		}
	}
	
	public static void openDoor(Location locDoor_ObereTürBlock, Location locDoor2_ObereTürBlock, int runTaskLater) {
		 if(locDoor_ObereTürBlock.getBlock().getType().toString().toLowerCase().contains("_door") && locDoor2_ObereTürBlock.getBlock().getType().toString().toLowerCase().contains("_door")) {
			BlockState state = locDoor_ObereTürBlock.getBlock().getState();
			Openable openable = (Openable) state.getBlockData();
			BlockState state2 = locDoor2_ObereTürBlock.getBlock().getState();
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
	
	public static void openDoor(Location locDoor_ObereTürBlock, int runTaskLater) {
		if(locDoor_ObereTürBlock.getBlock().getType().toString().toLowerCase().contains("_door")) {
			BlockState state = locDoor_ObereTürBlock.getBlock().getState();
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
