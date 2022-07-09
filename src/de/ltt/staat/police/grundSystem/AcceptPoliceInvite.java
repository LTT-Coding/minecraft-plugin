package de.ltt.staat.police.grundSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class AcceptPoliceInvite implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Police.InviAcc.containsKey(p)) {
				PoliceInfo mi = new PoliceInfo();
				PlayerInfo pi = new PlayerInfo(p);
				if(mi.getMember().contains(Police.InviAcc.get(p).getUniqueId().toString()) || Main.Admin.contains(p.getUniqueId().toString())) {
					if(mi.hasRight(Police.InviAcc.get(p), PoliceRights.INVITE)){
						if(pi.getJob() == 0) {
							mi.addmember(p);
							p.sendMessage("§aDu bist der §6Polizei§a beigetreten!");
							Police.InviAcc.get(p).sendMessage("§6" + p.getName() + "§a hat die Anfrage angenommen!");
							Police.InviAcc.remove(p);
						}else
							p.sendMessage("§cDu hast bereits eine Arbeit!");
					}else
						p.sendMessage("§cDer Spieler hat dazu keine Rechte mehr!");
				}else
					p.sendMessage("§cDer Spieler Arbeitet nicht mehr beim §6Polizei!");
			}else
				p.sendMessage("§cDu wurdest nicht in den §6Polizei§c eingeladen!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
