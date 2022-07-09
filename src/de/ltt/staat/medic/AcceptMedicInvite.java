package de.ltt.staat.medic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class AcceptMedicInvite implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Medic.InviAcc.containsKey(p)) {
				MedicInfo mi = new MedicInfo();
				PlayerInfo pi = new PlayerInfo(p);
				if(mi.getMember().contains(Medic.InviAcc.get(p).getUniqueId().toString()) || Main.Admin.contains(p.getUniqueId().toString())) {
					if(mi.hasRight(Medic.InviAcc.get(p), MedicRights.INVITE)){
						if(pi.getJob() == 0) {
							mi.addmember(p);
							p.sendMessage("§aDu bist dem §6Rettungsdienst§a beigetreten!");
							Medic.InviAcc.get(p).sendMessage("§6" + p.getName() + "§a hat die Anfrage angenommen!");
							Medic.InviAcc.remove(p);
						}else
							p.sendMessage("§cDu hast bereits eine Arbeit!");
					}else
						p.sendMessage("§cDer Spieler hat dazu keine Rechte mehr!");
				}else
					p.sendMessage("§cDer Spieler Arbeitet nicht mehr beim §6Rettungsdienst!");
			}else
				p.sendMessage("§cDu wurdest nicht in den §6Rettungsdienst§c eingeladen!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
