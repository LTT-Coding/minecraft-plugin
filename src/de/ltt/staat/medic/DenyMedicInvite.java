package de.ltt.staat.medic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class DenyMedicInvite implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Medic.InviAcc.containsKey(p)) {
				p.sendMessage("§4Du hast die Anfrage abgelehnt!");
				Medic.InviAcc.get(p).sendMessage("§6" + p.getName() + "§4 hat die Anfrage abgelehnt!");
				Medic.InviAcc.remove(p);
			}else
				p.sendMessage("§cDu wurdest nicht in den §6Rettungsdienst§c eingeladen!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
