package de.ltt.other;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class GM implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("0")) {
						if (!(p.getGameMode() == GameMode.SURVIVAL)) {
							p.setGameMode(GameMode.SURVIVAL);
							p.sendMessage("§aDu bist jetzt im Survival modus.");

						} else
							p.sendMessage("§4Du bist bereits im Survival modus.");

					} else if (args[0].equalsIgnoreCase("1")) {
						if (!(p.getGameMode() == GameMode.CREATIVE)) {
							p.setGameMode(GameMode.CREATIVE);
							p.sendMessage("§aDu bist jetzt im Kreativ modus.");

						} else
							p.sendMessage("§4Du bist bereits im Kreativ modus.");

					} else if (args[0].equalsIgnoreCase("2")) {
						if (!(p.getGameMode() == GameMode.ADVENTURE)) {
							p.setGameMode(GameMode.ADVENTURE);
							p.sendMessage("§aDu bist jetzt im Abenteuer modus.");

						} else
							p.sendMessage("§4Du bist bereits im Abenteuer modus.");

					} else if (args[0].equalsIgnoreCase("3")) {
						if (!(p.getGameMode() == GameMode.SPECTATOR)) {
							p.setGameMode(GameMode.SPECTATOR);
							p.sendMessage("§aDu bist jetzt im Beobachter modus.");

						} else
							p.sendMessage("§4Du bist bereits im Beobachter modus.");

					} else
						p.sendMessage("§4Bitte benutze /gm (0, 1, 2, 3)");

				} else if (args.length == 2) {
					Player t = Bukkit.getPlayer(args[1]);
					if (args[0].equalsIgnoreCase("0")) {
						if (!(t.getGameMode() == GameMode.SURVIVAL)) {
							t.setGameMode(GameMode.SURVIVAL);
							t.sendMessage("§aDu bist jetzt im Survival modus.");

						} else
							t.sendMessage("§4Du bist bereits im Survival modus.");

					} else if (args[0].equalsIgnoreCase("1")) {
						if (!(t.getGameMode() == GameMode.CREATIVE)) {
							t.setGameMode(GameMode.CREATIVE);
							t.sendMessage("§aDu bist jetzt im Kreativ modus.");

						} else
							t.sendMessage("§4Du bist bereits im Kreativ modus.");

					} else if (args[0].equalsIgnoreCase("2")) {
						if (!(t.getGameMode() == GameMode.ADVENTURE)) {
							t.setGameMode(GameMode.ADVENTURE);
							t.sendMessage("§aDu bist jetzt im Abenteuer modus.");

						} else
							t.sendMessage("§4Du bist bereits im Abenteuer modus.");

					} else if (args[0].equalsIgnoreCase("3")) {
						if (!(t.getGameMode() == GameMode.SPECTATOR)) {
							t.setGameMode(GameMode.SPECTATOR);
							t.sendMessage("§aDu bist jetzt im Beobachter modus.");

						} else
							t.sendMessage("§4Du bist bereits im Beobachter modus.");

					} else
						p.sendMessage("§4Bitte benutze /gm (0, 1, 2, 3)");

				} else
					p.sendMessage("§4Bitte benutze /gm (0, 1, 2, 3)");

			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);

		}

		return false;
	}

}
