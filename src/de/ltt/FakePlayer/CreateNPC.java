package de.ltt.FakePlayer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.ltt.server.main.Main;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PlayerConnection;

public class CreateNPC implements CommandExecutor, Listener{

	public static List<Player> remove = new ArrayList<Player>();
	@Override
	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 3) {
					if(args[0].equals("create")) {
						NPC.createNPC(p, args[1], args[2]);
						p.sendMessage("§eDu hast ein neuen NPC erstellt mit dem Namen: " + args[1] );
					}
				} else if(args.length == 2) {
					if(args[0].equals("create")) {
						NPC.createNPC(p, args[1], p.getName());
						p.sendMessage("§eDu hast ein neuen NPC erstellt mit dem Namen: " + args[1] );
					}
				} else if(args.length == 1) {
					if(args[0].equals("remove")) {
						p.sendMessage("§eKlick auf den NPC");
						remove.add(p);
					}
				}
			}
		}
		return false;
	}

	@EventHandler
	public void KillNPC(RightClickNPC e) {
		if(remove.contains(e.getPlayer())) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
					Main.mysql.update("delete from NPC where x = " + e.getNPC().locX() + " and y = " + e.getNPC().locY() + " and z = " + e.getNPC().locZ() + ";");
					NPC.removeNPC(p, e.getNPC());
					connection.sendPacket(new PacketPlayOutEntityDestroy(e.getNPC().getId()));
				}
				e.getPlayer().sendMessage("§6Der NPC wurde entfernt");
				remove.remove(e.getPlayer());
		}
	}
	
}
