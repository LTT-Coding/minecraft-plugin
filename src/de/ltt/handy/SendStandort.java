package de.ltt.handy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class SendStandort implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			PlayerInfo pi = new PlayerInfo(p);
			pi.runRoute(Main.PlayerSendStandort.get(p));
			p.sendMessage("§6Die Route wurde gestartet");
			Main.PlayerSendStandort.remove(p);
		}
		return false;
	}

}
