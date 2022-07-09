package de.ltt.FakePlayer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.Packet;

public class PacketReader {

	Channel channel;
	public static HashMap<UUID, Channel> channels = new HashMap<UUID, Channel>();
	
	public void inject(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		channel = cp.getHandle().playerConnection.networkManager.channel;
		channels.put(p.getUniqueId(), channel);
		
		if(channel.pipeline().get("PacketInjector") != null) {
			return;
		}
		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
			@Override
			protected void decode(ChannelHandlerContext channel, Packet<?> packet, List<Object> arg) throws Exception {		
				arg.add(packet);
				readPacket(p, packet);
			}
		});
	}
	
	public void uninject(Player p) {
		channel = channels.get(p.getUniqueId());
		if(channel.pipeline().get("PacketInjector") != null)
			channel.pipeline().remove("PacketInjector");
	}
	
	public void readPacket(Player p, Packet<?> packet) {
		//System.out.println(packet);
		if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
			
			
			if(getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")) return;
			if(getValue(packet, "d").toString().equalsIgnoreCase("OFF_HAND")) return;
			if(getValue(packet, "action").toString().equalsIgnoreCase("INTERACT_AT")) return;
		
			int id = (int) getValue(packet, "a");
			
			if(getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {
				for (EntityPlayer ep: NPC.getNPCs()) {
					if(ep.getId() == id) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							
							@Override
							public void run() {
								Bukkit.getPluginManager().callEvent(new RightClickNPC(p, ep));
								
							}
						}, 0);
					}
				}
			}
		}
	}
	
	private Object getValue(Object instance, String name) {
		Object result = null;
		
		try {
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			result = field.get(instance);
			
			field.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
}
