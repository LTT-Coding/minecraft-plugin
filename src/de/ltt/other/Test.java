package de.ltt.other;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import de.ltt.FakePlayer.NPC;
import de.ltt.FakePlayer.RightClickNPC;
import de.ltt.auto.Car;
import de.ltt.auto.cars.Bike_Red;
import de.ltt.auto.cars.RTW;
import de.ltt.server.main.Main;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_15_R1.PlayerConnection;

public class Test implements Listener, CommandExecutor {
	
	private int taskID;
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		Projectile projectile = e.getEntity();
		Player p = (Player) projectile.getShooter();
		if(e.getHitBlock() == null) return;
		Block block = e.getHitBlock();
		if(!(projectile.getShooter() instanceof Player)) return;
		if(block.getType() == Material.BLACK_STAINED_GLASS_PANE) {
			block.setType(Material.AIR);
			p.sendMessage("§4Du bist ja nen Raudi!");
			Main.KlickHören(block.getLocation(), Sound.BLOCK_GLASS_BREAK, (byte)17);
			p.sendMessage(e.getHitEntity() + "");
			
		}
		projectile.remove();
	}
	

	@Override
	public boolean onCommand(CommandSender sender,  Command cmd,  String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(args[0].equals("1")) {
				RTW rtw = new RTW(((Player) sender).getUniqueId().toString(), "Hello", ((Player) sender).getLocation());
				rtw.spawnCar();
				Car.cars.add(rtw);
			} else if(args[0].equals("2")) {
				Location loc = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY() + 0.7, p.getLocation().getZ());
				Bike_Red rtw = new Bike_Red(((Player) sender).getUniqueId().toString(), "Hello", loc);
				rtw.spawnCar();
				Car.cars.add(rtw);
			} else if(args[0].equals("3")) {
				NPC.addJoinPacket(p);
				p.sendMessage("NPC geladen?");
			} else if(args[0].equals("4")) {
				Main.ject(true);
				p.sendMessage("NPC JETZT geladen?");
			} else if(args[0].equals("5")) {
				List<Integer> number = new ArrayList<Integer>();
				
				for (Integer current : number) {
					for (Integer current2 : number) {
						if(current != current2) {
							if((current + current2) == 2020) {
								p.sendMessage("" + current + " + " + current2 + " = " + (current*current2));
							}
						}
					}
				}
			}
			
/*			TelegramBot bot = new TelegramBot("BOT_TOKEN");
			GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
			GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
			// Register for updates
			bot.setUpdatesListener(updates -> {
			    // ... process updates
			    // return id of last processed update or confirm them all
			    return UpdatesListener.CONFIRMED_UPDATES_ALL;
			});

			List<Update> updates = updatesResponse.updates();
			// Send messages
			Update update = getUpdates.get;
			long chatId = update.message().chat().id();
			SendResponse response = bot.execute(new SendMessage(chatId, "Hello!"));*/
		}
		return false;
	}
	
	@EventHandler
	public void lols(RightClickNPC e) {
		if(e.getNPC().getName().equals("Test")) {
			//PacketPlayOutRelEntityMove packet = new PacketPlayOutRelEntityMove(var0, var1, var2, var3, var4)
			Player p = e.getPlayer();
			EntityPlayer npc = e.getNPC();
			PlayerConnection co = ((CraftPlayer)p).getHandle().playerConnection;
			// Vector direction = p.getLocation().toVector().subtract(new Location(p.getWorld(), npc.locX(), npc.locY(), npc.locZ()).toVector());
			//Vec3D d = new Vec3D(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
			 Location npcLoc = new Location(p.getWorld(), npc.locX(), npc.locY(), npc.locZ());
			//co.sendPacket(new PacketPlayOutEntityHeadRotation());
			//co.sendPacket(new PacketPlayOutEntityVelocity());
			
			//co.sendPacket(new PacketPlayOutRelEntityMove(((EntityPlayer)npc).getId(), (short)((p.getLocation().getX() * 1024 - npcLoc.getX() * 1024)), (short)((p.getLocation().getY() * 1024 - npcLoc.getY() * 1024) ), (short)((p.getLocation().getZ() * 1024 - npcLoc.getZ() * 1024)), true));
			 //co.sendPacket(new PacketPlayOutEntityVelocity(((EntityPlayer)npc).getId(), new Vec3D(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ())));
				co.sendPacket(new PacketPlayOutRelEntityMove(((EntityPlayer)npc).getId(), (short)2400, (short)0, (short)1000, true));

			 p.sendMessage("ok?");
			p.sendMessage("" + (short)((p.getLocation().getBlockX() * 32 - npcLoc.getBlockX() * 32) * 128));
			p.sendMessage("" + (short)((p.getLocation().getBlockY() * 32 - npcLoc.getBlockY() * 32) * 128));
	        p.sendMessage("" + (short)((p.getLocation().getBlockZ() * 32 - npcLoc.getBlockZ() * 32) * 128));
Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
	
	@Override
	public void run() {
		co.sendPacket(new PacketPlayOutRelEntityMove(((EntityPlayer)npc).getId(), (short)2400, (short)0, (short)1000, true));
		npc.setHeadRotation(100f);
	}
}, 0, 2);

		}
		
	}
	

	
	
}
