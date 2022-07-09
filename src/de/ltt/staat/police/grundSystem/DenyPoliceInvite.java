package de.ltt.staat.police.grundSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class DenyPoliceInvite implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Police.InviAcc.containsKey(p)) {
				p.sendMessage("§4Du hast die Anfrage abgelehnt!");
				Police.InviAcc.get(p).sendMessage("§6" + p.getName() + "§4 hat die Anfrage abgelehnt!");
				Police.InviAcc.remove(p);
			}else
				p.sendMessage("§cDu wurdest nicht in den §6Polizei§c eingeladen!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
