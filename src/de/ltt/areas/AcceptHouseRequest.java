package de.ltt.areas;

import java.util.List;
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

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.OwnerType;
import de.ltt.server.reflaction.area.AreaInfo;
import de.ltt.staat.bauamt.BauamtInfo;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class AcceptHouseRequest implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender instanceof Player) {
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
				p.sendMessage("§aAbfrage wurde abgesendet!");
				AreaInfo ai = new AreaInfo().addRequest(LocSX, LocLX, LocSZ, LocLZ, OwnerType.PRIVATE, p.getUniqueId().toString());
				AreaRequest.selectedArea.remove(p);
				AreaRequest.overBlock.get(p).getBlock().setType(Material.DARK_OAK_SIGN);
				Sign sign = (Sign) AreaRequest.overBlock.get(p).getBlock().getState();
				sign.setLine(0, "§3Kaufantrag ");
				sign.setLine(1, "§3in");
				sign.setLine(2, "§3Bearbeitung");
				sign.setLine(3, "§3Privatgrundstück");
				sign.update();
				BauamtInfo bi = new BauamtInfo();
				for(String current : bi.getMember()) {
					OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(current));
					if(t.isOnline()) {
						t.getPlayer().sendMessage("§6" + p.getName() + "§e hat ein Grundstück beantragt!");
						TextComponent tc = new TextComponent();
						tc.setText("§eKlicke hier um dir das Grundstück anzusehen");
						tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/seehouserequest " + ai.getAreaid()));
						tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cGrundstück ansehen").create()));
						t.getPlayer().spigot().sendMessage(tc);
						t.getPlayer().sendTitle("§aEin Grundstück wurde beantragt", "", 5, 40, 5);
					}
				}
			} else
				p.sendMessage("§cBenutze zuerst §6/haus kaufen §cund wähle einen Bereich aus!");
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
