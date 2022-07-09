package de.ltt.server.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ltt.FakePlayer.PacketReader;
import de.ltt.other.PlayerInteractInv;
import de.ltt.reports.ReportCommand;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.PlayerInfo;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PlayerConnection;

public class Quit implements Listener{
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(PlayerInfo.getChars(e.getPlayer()).size() != 0) {
			PlayerInfo pi = new PlayerInfo(e.getPlayer());
			pi.saveInv(); // Funktioniert
			pi.saveLastLocation();
			pi.saveAktuellSkin();
		}
		for (String string : Main.vanish) {
			if(Bukkit.getPlayer(UUID.fromString(string)) != null) {
				e.getPlayer().showPlayer(Bukkit.getPlayer(UUID.fromString(string)));
			}
		}
		if(ReportCommand.fehler.contains(e.getPlayer())) {
			ReportCommand.fehler.remove(e.getPlayer());
		}
		if(ReportCommand.fragen.contains(e.getPlayer())) {
			ReportCommand.fragen.remove(e.getPlayer());
		}
		if(ReportCommand.melden.contains(e.getPlayer())) {
			ReportCommand.melden.remove(e.getPlayer());
		}
		if(Main.frChat.containsKey(e.getPlayer())) {
			Main.frChat.get(e.getPlayer()).sendMessage("§1" + e.getPlayer().getName() + "§9 Ist offline gegangen, der Report wurde beendet");
			Main.frChat2.remove(Main.frChat.get(e.getPlayer()));
			Main.frChat.remove(e.getPlayer());
		}
		if(Main.frChat2.containsKey(e.getPlayer())) {
			Main.frChat2.get(e.getPlayer()).sendMessage("§1" + e.getPlayer().getName() + "§9 Ist offline gegangen, der Report wurde beendet");
			Main.frChat.remove(Main.frChat2.get(e.getPlayer()));
			Main.frChat2.remove(e.getPlayer());
			
		}
		if(PlayerInteractInv.packMap.containsKey(e.getPlayer())) {
			PlayerInfo pi = new PlayerInfo(e.getPlayer());
			pi.sethaspacked(false);
			PlayerInfo ti = new PlayerInfo(PlayerInteractInv.packMap.get(e.getPlayer()));
			ti.setispacked(false);
			PlayerInteractInv.packMap.remove(e.getPlayer());
		}
		if(PlayerInteractInv.packMap.containsValue(e.getPlayer())) {
			
			for(Player current : Bukkit.getOnlinePlayers()) {
				current.showPlayer(e.getPlayer());
				if(PlayerInteractInv.packMap.containsKey(current)) {
					if(PlayerInteractInv.packMap.get(current).equals(e.getPlayer())){
						PlayerInfo pi = new PlayerInfo(e.getPlayer());
						pi.setispacked(false);
						PlayerInfo ti = new PlayerInfo(current);
						ti.sethaspacked(false);
						current.showPlayer(e.getPlayer());
						e.getPlayer().showPlayer(current);
						e.getPlayer().leaveVehicle();
						current.sendMessage("§cDer von dir gepackte Spieler ist Offline gegangen!");
						current.setPassenger(null);
						
					}
				}
			}
				
		}
		PlayerInfo pi = new PlayerInfo(e.getPlayer());
		if(pi.isSitting() && e.getPlayer().getVehicle() != null) {
			pi.setSitting(false);
			e.getPlayer().getVehicle().remove();
		}
		
		if(Main.tabNPC.containsKey(e.getPlayer().getName())) {
			Main.tabNPC.remove(e.getPlayer().getName());
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if(Main.tabNPC.containsKey(p.getName())) {
				Main.updateTab(p, 63, "§6 " + (Bukkit.getOnlinePlayers().size() - 1) + "/" +   Bukkit.getServer().getMaxPlayers());
			}
		}
		
		Main.ject(false);
	}
}
