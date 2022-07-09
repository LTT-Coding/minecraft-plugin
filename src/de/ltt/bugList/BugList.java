package de.ltt.bugList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class BugList implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(Main.BugsTodo.size() != 0) {
					p.sendMessage("§6===============Bugs===============");
					for(String current : Main.BugsTodo) {
						if(Main.Admin.contains(p.getUniqueId().toString())) {
							TextComponent tc = new TextComponent();
							tc.setText("§6-" + "" + current);
							tc.setColor(ChatColor.GOLD);
							TextComponent tc2 = new TextComponent();
							tc2.setText("§a[✘]");
							tc2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/bugstodo remove " + current));
							tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aErledigt").create()));
							tc.addExtra(tc2);
							p.spigot().sendMessage(tc);
						}else
							p.sendMessage("§e- " + current);
					}
					p.sendMessage("§6==================================");
				}else
					p.sendMessage("§aEs gibt keine gemeldeten Bugs :D");
			}else if(args.length > 1) {
			  if(args[0].equalsIgnoreCase("add")) {
				if(Main.Moderatoren.contains(p.getUniqueId().toString()) || Main.BetaT.contains(p.getUniqueId().toString())) {
					String BugTodo = "";
					int i = 0;
					for(String current : args) {
						if(i != 0) {
							BugTodo = BugTodo + " " + current;
						}
						i++;
					}
					if(!Main.BugsTodo.contains(BugTodo)) {
						p.sendMessage("§aBug wurde hinzugefügt!");
						SQLData.addBug(BugTodo);
					}else
						p.sendMessage("§cDieser Bug existiert bereits!");
				}else
					p.sendMessage(Main.KEINE_RECHTE_MOD);
				
			}else if(args[0].equals("remove")){
				if(Main.Admin.contains(p.getUniqueId().toString())) {
					String BugTodo = "";
					int i = 0;
					for(String current : args) {
						if(i != 0) {
							BugTodo = BugTodo + " " + current;
						}
						i++;
					}
					if(Main.BugsTodo.contains(BugTodo)) {
						p.sendMessage("§aBug wurde entfernt!");
						SQLData.removeBug(BugTodo);
					}else
						p.sendMessage("§cDieser Bug existiert nicht!");
				}else
					p.sendMessage(Main.KEINE_RECHTE_ADMIN);
			}
			}
		}else
			sender.sendMessage(Main.PREFIX + Main.KEIN_SPIELER);
		return false;
	}
}
