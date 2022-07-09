package de.ltt.other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HashTags implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(Main.hashTag.size() != 0 ) {
					p.sendMessage("§eAlle HashTags:");
					for(String current : Main.hashTag) {
						if(Main.Admin.contains(p.getUniqueId().toString())) {
							TextComponent tc = new TextComponent();
							tc.setText("§e- " + current + " ");
							tc.setColor(ChatColor.YELLOW);
							TextComponent tc2 = new TextComponent();
							tc2.setText("§c[✘]");
							tc2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/hashtags remove " + current));
							tc2.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cLöschen").create()));
							tc.addExtra(tc2);
							p.spigot().sendMessage(tc);
						}else
							p.sendMessage("§e- " + current);
					}
				}else
					p.sendMessage("§cEs gibt momentan noch keine Hashtags!");
			}else {
				if(args.length > 1) {
					if(args[0].equalsIgnoreCase("add")) {
						if(Main.Admin.contains(p.getUniqueId().toString())) {
							if(!args[1].startsWith("#")) {
								args[1] = "#" + args[1];
							}
							String hashtag = "";
							int i = 0;
							for(String current : args) {
								if(i != 0) {
									hashtag = hashtag + " " + current;
								}
								i++;
							}
							
							if(!Main.hashTag.contains(hashtag)) {
								p.sendMessage("§aHashtag wurde hinzugefügt!");
								SQLData.addHashTag(hashtag);
							}else
								p.sendMessage("§cDieser Hashtag existiert bereits!");
						}else
							p.sendMessage(Main.KEINE_RECHTE_ADMIN);
					}else if(args[0].equals("remove")){
						if(Main.Admin.contains(p.getUniqueId().toString())) {
							if(!args[1].startsWith("#")) {
								args[1] = "#" + args[1];
							}
							String hashtag = "";
							int i = 0;
							for(String current : args) {
								if(i != 0) {
									hashtag = hashtag + " " + current;
								}
								i++;
							}
							
							if(Main.hashTag.contains(hashtag)) {
								SQLData.removeHashTag(hashtag);
								p.sendMessage("§aHashtag wurde entfernt!");
							}else
								p.sendMessage("§cDieser HashTag existiert nicht!");
						}else
							p.sendMessage(Main.PREFIX + Main.KEINE_RECHTE_ADMIN);
					}
				}else
					p.sendMessage("§cbenutze §6/hashtags add/remove [Hashtag]§c um einen Hashtag hinzuzufügen oder zu entfernen!");
			}
		}
		return false;
	}

}
