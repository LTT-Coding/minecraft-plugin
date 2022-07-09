package de.ltt.server.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

import de.ltt.FakePlayer.NPC;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PlayerConnection;

public class Join implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Main.vanish.contains(e.getPlayer().getUniqueId().toString())) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if(!Main.Admin.contains(p.getUniqueId().toString())) {
					p.hidePlayer(e.getPlayer());
				}
			}
		} else {
			if(!Main.Admin.contains(e.getPlayer().getUniqueId().toString())) {
				for (String string : Main.vanish) {
					if(Bukkit.getPlayer(UUID.fromString(string)) != null) {
						e.getPlayer().hidePlayer(Bukkit.getPlayer(UUID.fromString(string)));
					}
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(Main.TEXTUREPACK != "") {
					e.getPlayer().setResourcePack(Main.TEXTUREPACK);
					
				}
			}
		}, 1);
		PlayerInfo pi = new PlayerInfo(e.getPlayer());
		if(pi.getMailBox().size() != 0) {
			if(pi.getMailBoxJoin()) {
				e.getPlayer().sendMessage("§6===========MailBox===========");
				for(String current : pi.getMailBox()) {
					e.getPlayer().sendMessage("§6- " + current);
				}
				e.getPlayer().sendMessage("§6=============================");
				pi.clearMailBox();
			}
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if(p.getName() != e.getPlayer().getName()) {
				if(Main.tabNPC.containsKey(p.getName())) {
					List<EntityPlayer> npcs = Main.tabNPC.get(p.getName());
					EntityPlayer npc = Main.tabNPC.get(p.getName()).get(63);
					PlayerConnection co = ((CraftPlayer)p).getHandle().playerConnection;
					co.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
					String OnlinePlayer = "§6 " + Bukkit.getOnlinePlayers().size() + " / 50";
					npc.listName = new ChatMessage(OnlinePlayer, "");
					co.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
					npcs.set(63, npc);
					Main.tabNPC.put(p.getName(), npcs);
				}
			}
		}
		
		e.getPlayer().setFallDistance(0);
		Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
		sb.getTeam("NoName").setNameTagVisibility(NameTagVisibility.NEVER);
		sb.getTeam("NoName").addPlayer(e.getPlayer());
		
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
	//	NPC.addJoinPacket(e.getEntity());
		//Main.ject(true);
		e.setDeathMessage("");
	}
	
	@EventHandler
	public void onKills(PlayerRespawnEvent e) {
		NPC.addJoinPacket(e.getPlayer());
		Main.ject(true);
		e.getPlayer().sendMessage("gehts");
	}
}
