package de.ltt.FakePlayer;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.ltt.server.main.Main;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;

public class NPC {

	public static List<EntityPlayer> NPC = new ArrayList<EntityPlayer>();
	
	public static void createNPC(Player p, String name, String skin) {
		UUID uuid = UUID.randomUUID();
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) Bukkit.getWorld(p.getWorld().getName())).getHandle();
		GameProfile gP = new GameProfile(uuid, name);
		EntityPlayer npc = new EntityPlayer(server, world, gP, new PlayerInteractManager(world));
		Location pLoc = p.getLocation();
		npc.setLocation(pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getYaw(), pLoc.getPitch());
		String[] skins = getSkin(p, skin);
		gP.getProperties().put("textures",new Property("textures", skins[0], skins[1]));
		NPC.add(npc);
		addNPCpacket(npc);
		Main.mysql.update("insert into NPC(Name, UUID, x, y, z, yaw, pitch, world, texture, signature) values ('" + name + "', '" + uuid + "', " + pLoc.getX() + ", " + pLoc.getY() + "," + pLoc.getZ() + ", " + pLoc.getYaw() + ", " + pLoc.getPitch() + ", '" + pLoc.getWorld().getName() + "', '" + skins[0] + "', '" + skins[1] + "');");
	}
	
	public static String[] getSkin(Player p, String name) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
			URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader2 = new InputStreamReader(url2.openStream());
			JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			String texture = property.get("value").getAsString();
			String signature = property.get("signature").getAsString();
			return new String[] {texture, signature};
		} catch (Exception e) {
			EntityPlayer p2 = ((CraftPlayer) p).getHandle();
			GameProfile gp = p2.getProfile();
			Property pro = gp.getProperties().get("textures").iterator().next();
			String texture = pro.getValue();
			String signature = pro.getSignature();
			Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "§cgetSkin hat nicht Funktioniert Grund:!" + e.getCause());
			return new String[] {texture, signature};
		}
	}
	
	public static void addNPCpacket(EntityPlayer npc) {
		for(Player current : Bukkit.getOnlinePlayers()) {
			PlayerConnection co = ((CraftPlayer)current).getHandle().playerConnection;
			co.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
			co.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			co.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					co.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
				}
			}, 20);
			
	        DataWatcher watcher = ((EntityPlayer) npc).getDataWatcher(); 
	       watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
	        co.sendPacket(new PacketPlayOutEntityMetadata( ((EntityPlayer)npc).getId(), watcher, true));
		}
	}
	
	public static void addJoinPacket(Player p) {
		for (EntityPlayer npc : NPC) {
			PlayerConnection co = ((CraftPlayer)p).getHandle().playerConnection;
			co.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, (EntityPlayer) npc));
			co.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			co.sendPacket(new PacketPlayOutEntityHeadRotation( (EntityPlayer) npc, (byte) ((npc.yaw * 256.0F) / 360.0F)));  
			//Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
			//	@Override
			//	public void run() {
			///		co.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
			//	}
			//}, 200);
	       
	        DataWatcher watcher = ((EntityPlayer) npc).getDataWatcher(); //new DataWatcher(null) //((EntityPlayer) rec.npc).getDataWatcher()
	        //watcher.register(new DataWatcherObject<>(6, DataWatcherRegistry.c), (float) 20F);
	       // watcher.register(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 70); //70 = Alles behalve cape //79 = Alles
	       watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
	        co.sendPacket(new PacketPlayOutEntityMetadata( ((EntityPlayer)npc).getId(), watcher, true));
			
		}
	}
	
	public static void removeNPC(Player p , EntityPlayer npc) {
		try {
			PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
			NPC.remove(npc);
			connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static List<EntityPlayer> getNPCs(){
		return NPC;
	}
	
	public static void loadNPC(Location loc, String name, String uuid, String texture, String signature, String world) {
	Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
		
		@Override
		public void run() {
			MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
			Location loc2 = new Location(Bukkit.getWorld(world), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
			WorldServer world = ((CraftWorld) loc2.getWorld()).getHandle();
			GameProfile gP = new GameProfile(UUID.fromString(uuid), name);
			gP.getProperties().put("textures",new Property("textures", texture, signature));
			EntityPlayer npc = new EntityPlayer(server, world, gP, new PlayerInteractManager(world));
			Location pLoc = loc;
			npc.setLocation(pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getYaw(), pLoc.getPitch());
			addNPCpacket(npc);
			NPC.add(npc);
			
		}
	}, 20);
	}
	

}
