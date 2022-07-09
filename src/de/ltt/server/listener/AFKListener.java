package de.ltt.server.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ltt.other.AFKCommand;
import de.ltt.server.main.Main;

public class AFKListener implements Listener{
	
	public static ArrayList<Player> notAfk = new ArrayList<Player>();
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
		Main.savedLoc.put(e.getPlayer(), e.getPlayer().getLocation());
        removeAFK(e.getPlayer());
        interact(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
    	removeAFK(e.getPlayer());
    	interact(e.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
    	 removeAFK(e.getPlayer());
    	 interact(e.getPlayer());
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                removeAFK(p);
                interact(p);
            }
            
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
    	 removeAFK(e.getPlayer());
    	 interact(e.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
    	 removeAFK(e.getPlayer());
    	 interact(e.getPlayer());
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e) {
    	 removeAFK(e.getPlayer());
    	 interact(e.getPlayer());
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
    	 removeAFK(e.getPlayer());
    	 interact(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent e) {
    	 removeAFK(e.getPlayer());
    	 interact(e.getPlayer());
    }
    
    public void interact(Player p) {
    	if(!notAfk.contains(p))notAfk.add(p);
    }
    
    public static void removeAFK(Player p) {
    	if(Main.shortAFK.contains(p) || AFKCommand.temporaryAFK.contains(p)) {
    		if(Main.shortAFK.contains(p))Main.shortAFK.remove(p);
        	if(Main.longerAFK.contains(p)) {
        		Main.longerAFK.remove(p);
        		p.teleport(Main.savedLoc.get(p));
        	}
        	if(AFKCommand.temporaryAFK.contains(p))AFKCommand.temporaryAFK.remove(p);
        	p.sendMessage("§eDu bist nun nicht mehr AFK!");
        	for(Player current : Bukkit.getOnlinePlayers()) 
				if(p.getLocation().distance(current.getLocation()) < 5 && p != current) 
					current.sendMessage("§6" + p.getName() + "§e ist wieder aufmerksam!");
    	}
    }
	
}
