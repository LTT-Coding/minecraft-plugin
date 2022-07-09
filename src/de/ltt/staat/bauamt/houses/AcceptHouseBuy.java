package de.ltt.staat.bauamt.houses;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import de.ltt.areas.HouseRequestBauamt;
import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.area.AreaInfo;

public class AcceptHouseBuy implements CommandExecutor, Listener{

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(HouseRequestBauamt.clickedSignArea.containsKey(p)) {
				p.sendMessage("§eGebe einen Preis pro m² an:");
				ChatInfo.addChat(p, ChatType.SETHOUSEPRICE, 30*20);
				if(p.getName().equals("Sorania")) {
					ChatInfo.addChat(p, ChatType.SETHOUSEPRICE, 60*10*20);
					p.sendMessage("§dEXTRA FÜR DICH SORI(§b§olauter beifall im Hintergrund zu hören§r§d)");
				}
			}else
				p.sendMessage("§cWähle zuerst eine Anfrage aus!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if(ChatInfo.getChat(p).equals(ChatType.SETHOUSEPRICE)) {
			String message  = e.getMessage();
			try {
				int price = Integer.parseInt(message);
				if(price <= 5000) {
					ChatInfo.removeChat(p);
					AreaInfo ai = new AreaInfo().loadRequest(HouseRequestBauamt.clickedSignArea.get(p));
					ai.setAccepted(true);
					OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(ai.getOwner()));
					int LocSX = ai.getsX();
					int LocLX = ai.getlX();
					int LocSZ = ai.getsZ();
					int LocLZ = ai.getlZ();
					int fläche = (LocLX-LocSX + 1)*(LocLZ - LocSZ + 1);
					if(t.isOnline()) {
						t.getPlayer().sendMessage("§aDein Kaufantrag wurde akzeptiert, gebe §6/kaufantrag annehmen/ablehnen §a ein um diesen mit dem Preis zu bestätigen oder abzulehnen!");
						t.getPlayer().sendMessage("§aDas Grundstück wird §6" + fläche*price + "€ §akosten!");
					}else {
						PlayerInfo ti = new PlayerInfo(t);
						ti.addToMailBox("§aDein Kaufantrag wurde akzeptiert, gebe §6/kaufantrag annehmen/ablehnen §a ein um diesen mit dem Preis zu bestätigen oder abzulehnen!");
					}
					SQLData.addAreaPrice(ai.getAreaid(), price);
				
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
					HouseRequestBauamt.clickedSignArea.remove(p);
				}else
					p.sendMessage("§cDas Grundstück darf maximal §65000€ pro m² §ckosten!");
			} catch (Exception e2) {
				p.sendMessage("§cDeine eingabe ist keine Zahl!");
				e2.printStackTrace();
			}
			e.setCancelled(true);
		}
	}

}
