package de.ltt.server.listener;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.PlayerInfo;
import net.minecraft.server.v1_15_R1.ServerGUI;

public class PlayerMotd implements Listener{
	
	@EventHandler
	public void getIP(PlayerLoginEvent e) {
		try {
			if(Main.getPlugin().getConfig().getBoolean("Server.wartemodus.on") == true) {
				Player p = e.getPlayer();
				if(Main.Admin.contains(p.getUniqueId().toString())) {
					e.allow();
				} else if(Main.AddWartungPlayer.contains(p.getUniqueId().toString())){
					e.allow();
				}else {
					SQLData.addJoinPlayer(e.getPlayer().getUniqueId().toString());
					Random r = new Random();
					int Zahl = r.nextInt(Main.SinnloseFakten.size());
					e.disallow(null, "§cDer Server ist zur zeit in Wartung\n\n§6Wusstest du: \n\n §e" + Main.SinnloseFakten.get(Zahl) + "\n "
						+ "§3Der Server ist noch im Aufbau aber auf Instagram kannst du uns jederzeit Fragen stellen:  https://www.instagram.com/mileplay__/");
				}
			}
				Player p = e.getPlayer();
				String ip = e.getAddress().getHostAddress();
				SQLData.setPMotd(ip, p.getUniqueId().toString());
			
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	private OfflinePlayer getOfflinePlayer(String HostAddress) {
		return Bukkit.getOfflinePlayer(UUID.fromString(Main.pMotd.get(HostAddress)));
	}

	@EventHandler
	public void Motd(ServerListPingEvent e) {
		if(!Main.getPlugin().getConfig().getBoolean("Server.wartemodus.on") == true) {
			if(Main.pMotd.containsKey(e.getAddress().getHostAddress())) {
				OfflinePlayer p = getOfflinePlayer(e.getAddress().getHostAddress());
				PlayerInfo pi = new PlayerInfo(p);
				String job;
				if(pi.getJob() == -1) {
					job = "Rettungsdienst";
				} else if(pi.getJob() == -2) {
					job = "Polizei";
				} else if(pi.getJob() != 0){
					FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
					job = fi.getFirmname();
				}else {
					job = "Arbeitslos";
				}
				
				e.setMotd("§9Mile§fPlay §7|| Dein §6§l Reallife §7& §6§lRoleplay §7Server\n"
						+ "    §7» §b" + p.getName() +"  §7|  §b" + job +" ");
				
			} else {
				e.setMotd("§9Mile§fPlay §7|| Dein §6§l Reallife §7& §6§lRoleplay §7Server\n"
						+ "     §6Werde noch Heute ein Teil unserer Community");
			}
		} else {
			e.setMotd("§9Mile§fPlay §7|| Dein §6§l Reallife §7& §6§lRoleplay §7Server\n"
					+ "                   §6Server in Wartung");
		}
		
	}
	
}
