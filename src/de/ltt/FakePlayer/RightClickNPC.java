package de.ltt.FakePlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.minecraft.server.v1_15_R1.EntityPlayer;

public class RightClickNPC extends Event implements Cancellable{

	private final Player p;
	private final EntityPlayer npc;
	private boolean isCancelled;
	private static final HandlerList HANDLERS = new HandlerList();
	
	public RightClickNPC(Player p, EntityPlayer npc) {
		this.p = p;
		this.npc = npc;
	}

	public Player getPlayer() {
		return p;
	}
	
	public EntityPlayer getNPC() {
		return npc;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCancelled(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

}
