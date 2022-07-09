package de.ltt.areas;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DenyHouseRequest implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if (AreaRequest.selectedArea.containsKey(p)) {
				List<Integer> selected = AreaRequest.selectedArea.get(p);
				int LocSX = selected.get(0);
				int LocLX = selected.get(1);
				int LocSZ = selected.get(2);
				int LocLZ = selected.get(3);
				int py = selected.get(4);
				Location testloc;
				testloc = new Location(p.getWorld(), LocSX, py, LocSZ);
				while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ()).getBlock()
						.isEmpty()) {
					testloc.setY(testloc.getY() + 1);
				}
				while (testloc.getBlock().isEmpty()) {
					testloc.setY(testloc.getY() - 1);
				}
				BlockState state = testloc.getBlock().getState();
				state.update();
				testloc.setX(LocLX);
				while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ()).getBlock()
						.isEmpty()) {
					testloc.setY(testloc.getY() + 1);
				}
				while (testloc.getBlock().isEmpty()) {
					testloc.setY(testloc.getY() - 1);
				}
				state = testloc.getBlock().getState();
				state.update();
				testloc.setZ(LocLZ);
				while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ()).getBlock()
						.isEmpty()) {
					testloc.setY(testloc.getY() + 1);
				}
				while (testloc.getBlock().isEmpty()) {
					testloc.setY(testloc.getY() - 1);
				}
				state = testloc.getBlock().getState();
				state.update();
				testloc.setX(LocSX);
				while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ()).getBlock()
						.isEmpty()) {
					testloc.setY(testloc.getY() + 1);
				}
				while (testloc.getBlock().isEmpty()) {
					testloc.setY(testloc.getY() - 1);
				}
				state = testloc.getBlock().getState();
				state.update();
				p.sendMessage("§4Vorgang wurde abgebrochen!");
				AreaRequest.selectedArea.remove(p);
				if(AreaRequest.selectedBlocks.containsKey(p)) {
					AreaRequest.selectedBlocks.remove(p);
				}
			} else
				p.sendMessage("§cBenutze zuerst §6/haus kaufen §cund wähle einen Bereich aus!");
		}
		return false;
	}

}
