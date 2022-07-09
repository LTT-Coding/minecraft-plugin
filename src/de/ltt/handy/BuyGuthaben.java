package de.ltt.handy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class BuyGuthaben implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			int guthaben = Main.getPlugin().getConfig().getInt("Handy.Spieler." + p.getUniqueId() + ".guthaben");
			Main.getPlugin().getConfig().set("Handy.Spieler." + p.getUniqueId() + ".guthaben", guthaben + 10);
			Main.getPlugin().saveConfig();
			int guthaben2 = Main.getPlugin().getConfig().getInt("Handy.Spieler." + p.getUniqueId() + ".guthaben");
			
			p.sendMessage("Du hast dein guthaben aufgefüllt " + guthaben2);
		}
		return false;
	}
}