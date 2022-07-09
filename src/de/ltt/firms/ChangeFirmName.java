package de.ltt.firms;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.Rights;

public class ChangeFirmName implements Listener{

	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		if(ChatInfo.getChat(e.getPlayer()).equals(ChatType.CHANGEFIRMNAME)) {
			Player p = e.getPlayer();
			e.setCancelled(true);
			if(e.getMessage().toLowerCase().equals("cancel")) {
				ChatInfo.removeChat(p);
				p.sendMessage("§4Vorgang abgebrochen!");
			}else {
				ChatInfo.removeChat(p);
				PlayerInfo pi = new PlayerInfo(p);
				if(pi.getJob() > 0) {
					FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
					if(fi.hasrank(p, Rights.MAIN_CHANGENAME)) {
						if(fi.nameIsFree(e.getMessage())) {
							fi.setFirmname(e.getMessage());
							p.sendMessage("§aDer Firmenname wurde zu §6" + e.getMessage() + "§a geändert!");
						}else {
							p.sendMessage("§cEs gibt bereits eine Firma mit diesem Namen!");
							p.sendMessage("§4Vorgang abgebrochen!");
						}
					}else
						p.sendMessage("§cDir wurden soeben die Rechte dazu weggenommen!");
				}else
					p.sendMessage("§cDu bist in keiner Firma mehr!");
			}
		}
	}
	
}
