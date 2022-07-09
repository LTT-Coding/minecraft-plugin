package de.ltt.firms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.PlayerInfo;

public class InviteAcc implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(Main.inviteMap.containsKey(p)) {
				Player own = Main.inviteMap.get(p);
				PlayerInfo pi = new PlayerInfo(own);
				FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
				fi.addMember(p);
				p.sendMessage("§aDu bist der Firma §6" + fi.getFirmname() + " §a beigetreten");
				own.sendMessage("§aDer Spieler §6" + p.getName() + " §aist deiner Firma beigetreten");
				Main.inviteMap.remove(p);
			}else
				p.sendMessage("§CDu wurdes von keiner Firma eingeladen!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
