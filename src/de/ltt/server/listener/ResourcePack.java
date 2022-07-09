package de.ltt.server.listener;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import de.ltt.FakePlayer.NPC;
import de.ltt.FakePlayer.PacketReader;
import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.PlayerInfo;

public class ResourcePack implements CommandExecutor, Listener{
	
	@EventHandler
	public void onResourcePackStatusEvent(PlayerResourcePackStatusEvent e) {
		Player p = e.getPlayer();
		Status status = e.getStatus();
		if(status == Status.ACCEPTED || status == Status.SUCCESSFULLY_LOADED) {
			if(status == Status.SUCCESSFULLY_LOADED) {
				PlayerInfo pi = new PlayerInfo(p);
				p.sendMessage("1");
				if(PlayerInfo.getChars(p).size() == 0) {
					Main.openCharCreate(p);
				} else if(PlayerInfo.getChars(p).size() > 1) {
					Main.openCharInv(p);
					Main.loadTab(e.getPlayer());
					p.sendMessage("2");
				} else {
					pi.loadAktuellSkin();
					Main.loadTab(e.getPlayer());
				//	NPC.addJoinPacket(p);
				//	Main.ject(true);
				}
				p.sendMessage("Ende");
			}
		}else
			p.kickPlayer("§cDu musst das Resourcenpaket akzeptieren! \n"
					+ "§6Um das Resourcenpaket musst du folgendes machen \n"
					+ "§e Auf den Server klicken -> Bearbeiten -> Server-Resourcenpakete \n"
					+ "§eMelde dich bei Problemen bitte auf unserem Discord Server");
	}

	@Override
	public boolean onCommand( CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 0) {
					for(Player current : Bukkit.getOnlinePlayers()) {
						current.setResourcePack(Main.TEXTUREPACK);
						current.sendMessage("§aDein Resourcenpaket wurde von einem §6Admin§a aktualisiert!");
					}
					p.sendMessage("§aDu hast das Resourcepack für den ganzen Server aktualisiert!");
				}else {
					Player t = Bukkit.getPlayer(args[0]);
					if(t !=null) {
						t.setResourcePack(Main.TEXTUREPACK);
						p.sendMessage("§aDu hast das Resourcepack von §6" + t.getName() + "§a aktualisiert!");
						t.sendMessage("§aDein Resourcepack wurde von einem §6Admin§a aktualisiert!");
					}else
						p.sendMessage("§cDieser Spieler ist nicht online oder existiert nicht!");
				}
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
