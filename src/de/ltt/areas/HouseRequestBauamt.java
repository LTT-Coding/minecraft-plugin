package de.ltt.areas;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.area.AreaInfo;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HouseRequestBauamt implements Listener {

	public static HashMap<Player, Integer> clickedSignArea = new HashMap<Player, Integer>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(new PlayerInfo(e.getPlayer()).isBauamt() || Main.Admin.contains(e.getPlayer().getUniqueId().toString())) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (e.getClickedBlock().getState() instanceof Sign) {
					Sign sign = (Sign) e.getClickedBlock().getState();
					if (sign.getLine(0).equals("§3Kaufantrag ") 
						&& sign.getLine(1).equals("§3in")
						&& sign.getLine(2).equals("§3Bearbeitung")
						&& sign.getLine(3).equals("§3Privatgrundstück")) {
						Player p = e.getPlayer();
						new AreaInfo();
						if (AreaInfo.isonRequest(e.getClickedBlock().getLocation())) {
							AreaInfo ai = AreaInfo.getRequest(e.getClickedBlock().getLocation());
							int LocSX = ai.getsX();
							int LocLX = ai.getlX();
							int LocSZ = ai.getsZ();
							int LocLZ = ai.getlZ();
							int py = e.getClickedBlock().getLocation().getBlockY();
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
							p.sendBlockChange(testloc, Material.GREEN_CONCRETE, (byte) 5);
							testloc.setX(LocLX);
							while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
									.getBlock().isEmpty() && !(new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
											.getBlock().getState() instanceof Sign)) {
								testloc.setY(testloc.getY() + 1);
							}
							while (testloc.getBlock().isEmpty() || (testloc.getBlock().getState() instanceof Sign)) {
								testloc.setY(testloc.getY() - 1);
							}
							p.sendBlockChange(testloc, Material.GREEN_CONCRETE, (byte) 5);
							testloc.setZ(LocLZ);
							while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
									.getBlock().isEmpty() && !(new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
											.getBlock().getState() instanceof Sign)) {
								testloc.setY(testloc.getY() + 1);
							}
							while (testloc.getBlock().isEmpty() || (testloc.getBlock().getState() instanceof Sign)) {
								testloc.setY(testloc.getY() - 1);
							}
							p.sendBlockChange(testloc, Material.GREEN_CONCRETE, (byte) 5);
							testloc.setX(LocSX);
							while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
									.getBlock().isEmpty() && !(new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
											.getBlock().getState() instanceof Sign)) {
								testloc.setY(testloc.getY() + 1);
							}
							while (testloc.getBlock().isEmpty() || (testloc.getBlock().getState() instanceof Sign)) {
								testloc.setY(testloc.getY() - 1);
							}
							p.sendBlockChange(testloc, Material.GREEN_CONCRETE, (byte) 5);
							int fläche = (LocLX-LocSX + 1)*(LocLZ - LocSZ + 1);
							p.sendMessage("§eDas Grundstück hat §6" + fläche + "m²");
							clickedSignArea.put(p, ai.getAreaid());
							
							TextComponent tc = new TextComponent();
							tc.setText("§7[§aAnnehmen§7]");
							tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accepthousebuy"));
							tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAnnehmen").create()));
							tc.setBold(true);
							TextComponent leer = new TextComponent();
							leer.setText("  ");
							leer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
							leer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
							leer.setBold(true);
							TextComponent tc2 = new TextComponent();
							tc2.setText("§7[§4Ablehnen§7]");
							tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/denyhousebuy"));
							tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Ablehnen").create()));
							tc2.setBold(true);
							tc.addExtra(leer);
							tc.addExtra(tc2);
							
							p.spigot().sendMessage(tc);
						}
					}
				}
			}
		}
	}

}
