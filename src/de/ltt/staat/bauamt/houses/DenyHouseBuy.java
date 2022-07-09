package de.ltt.staat.bauamt.houses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.areas.HouseRequestBauamt;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.area.AreaInfo;

public class DenyHouseBuy implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(HouseRequestBauamt.clickedSignArea.containsKey(p)) {
				p.sendMessage("§4Anfrage wurde abgelehnt!");
				AreaInfo ai = new AreaInfo().loadRequest(HouseRequestBauamt.clickedSignArea.get(p));
				int LocSX = ai.getsX();
				int LocLX = ai.getlX();
				int LocSZ = ai.getsZ();
				int LocLZ = ai.getlZ();
				int py = p.getLocation().subtract(0, 1, 0).getBlockY();
				Location testloc;
				testloc = new Location(p.getWorld(), LocSX, py, LocSZ);
				while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
						.getBlock().isEmpty() && !(new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
						.getBlock().getState() instanceof Sign)) {
					testloc.setY(testloc.getY() + 1);
				}
				while (testloc.getBlock().isEmpty() || (testloc.getBlock().getState() instanceof Sign)) {
					testloc.setY(testloc.getY() - 1);
				}
				testloc.add(0, 1, 0);
				testloc.getBlock().setType(Material.AIR);
				testloc.subtract(0, 1, 0);
				BlockState state = testloc.getBlock().getState();
				state.update();
				testloc.setX(LocLX);
				testloc.setX(LocLX);
				while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
						.getBlock().isEmpty() && !(new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
								.getBlock().getState() instanceof Sign)) {
					testloc.setY(testloc.getY() + 1);
				}
				while (testloc.getBlock().isEmpty() || (testloc.getBlock().getState() instanceof Sign)) {
					testloc.setY(testloc.getY() - 1);
				}
				testloc.add(0, 1, 0);
				testloc.getBlock().setType(Material.AIR);
				testloc.subtract(0, 1, 0);
				state = testloc.getBlock().getState();
				state.update();
				testloc.setZ(LocLZ);
				while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
						.getBlock().isEmpty() && !(new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
								.getBlock().getState() instanceof Sign)) {
					testloc.setY(testloc.getY() + 1);
				}
				while (testloc.getBlock().isEmpty() || (testloc.getBlock().getState() instanceof Sign)) {
					testloc.setY(testloc.getY() - 1);
				}
				testloc.add(0, 1, 0);
				testloc.getBlock().setType(Material.AIR);
				testloc.subtract(0, 1, 0);
				state = testloc.getBlock().getState();
				state.update();
				testloc.setX(LocSX);
				while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
						.getBlock().isEmpty() && !(new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
								.getBlock().getState() instanceof Sign)) {
					testloc.setY(testloc.getY() + 1);
				}
				while (testloc.getBlock().isEmpty() || (testloc.getBlock().getState() instanceof Sign)) {
					testloc.setY(testloc.getY() - 1);
				}
				testloc.add(0, 1, 0);
				testloc.getBlock().setType(Material.AIR);
				testloc.subtract(0, 1, 0);
				state = testloc.getBlock().getState();
				state.update();
				ai.removeRequest();
			}else
				p.sendMessage("§cWähle zuerst eine Anfrage aus!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	

}
