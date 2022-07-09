package de.ltt.staat.bauamt;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Bauamt implements CommandExecutor{

	public static HashMap<Player, Player>  inviteMap = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			BauamtInfo bi = new BauamtInfo();
			if(bi.getMember().contains(p.getUniqueId().toString()) || Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length >= 1) {
					if(args[0].equalsIgnoreCase("einstellen")) {
						if(bi.getRank(p) == 2 || Main.Admin.contains(p.getUniqueId().toString())) {
							if(args.length == 2) {
								Player t = Bukkit.getPlayer(args[1]);
								if(t != null) {
									if(!bi.getMember().contains(t.getUniqueId().toString())) {
										t.sendMessage("§6" + p.getName() + "§a möchte dich in das Bauamt einladen");
										t.sendMessage("§aWähle eine Aktion:");
										TextComponent tc = new TextComponent();
										tc.setText("§7[§aAnnehmen§7]");
										tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptbauamtinvite"));
										tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAnnehmen").create()));
										tc.setBold(true);
										TextComponent leer = new TextComponent();
										leer.setText("  ");
										leer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
										leer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
										leer.setBold(true);
										TextComponent tc2 = new TextComponent();
										tc2.setText("§7[§4Ablehnen§7]");
										tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/denybauamtinvite"));
										tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Ablehnen").create()));
										tc2.setBold(true);
										tc.addExtra(leer);
										tc.addExtra(tc2);
										t.spigot().sendMessage(tc);
										p.sendMessage("§aEinladung abgesendet!");
										inviteMap.put(t, p);
									}else
										p.sendMessage("§cDieser Spieler arbeitet bereits im Bauamt!");
								}else
									p.sendMessage("§cDieser Spieler ist nicht online oder existiert nicht!");
							}else
								p.sendMessage(Main.KEINE_RECHTE_BAUAMT);
						}
					}else if(args[0].equalsIgnoreCase("kündigen")) {
						
					}else if(args[0].equalsIgnoreCase("anträge")) {
						
					}
				}
			}else
				p.sendMessage("§cDazu musst du im §6Bauamt §csein!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	
	
	//private Inventory openAntragInv(int page) {
		
	//}
	
	

}
