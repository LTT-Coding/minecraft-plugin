package de.ltt.other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class Vote implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender,  Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage("§6Wir bitten dich jeden tag für uns zu Voten");
			p.sendMessage("§6Das geht under folgenden Links:");
			TextComponent tc = new TextComponent();
			tc.setText("§e» https://minecraft-server.eu/vote/index/214C3");
			tc.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://minecraft-server.eu/vote/index/214C3"));
			tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Vote").create()));
			tc.setColor(ChatColor.GOLD);
			p.spigot().sendMessage(tc);
			TextComponent tc2  = new TextComponent();
			tc2.setText("§e» https://www.minecraft-serverlist.net/vote/53658");
			tc2.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://www.minecraft-serverlist.net/vote/53658"));
			tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Vote").create()));
			tc2.setColor(ChatColor.GOLD);
			p.spigot().sendMessage(tc2);
	     
		}
		return false;
	}

}
