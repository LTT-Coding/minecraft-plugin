package de.ltt.bauen;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class AntiBauenAbbauen implements Listener{
	
	
	@EventHandler
	public void onPlayerBlock(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(Main.BlockSperreLoc.contains(e.getClickedBlock().getLocation())) {
				Player p = (Player) e.getPlayer();
				if(Main.Admin.contains(p.getUniqueId().toString())) {
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(Main.Admin.contains(p.getUniqueId().toString())) {
			e.setBuild(true);
		} else {
			e.setBuild(false);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Main.Admin.contains(p.getUniqueId().toString())) {
			e.setCancelled(false);
			if(Main.BlockSperreLoc.contains(e.getBlock().getLocation())) {
				SQLData.removeBlockSperre(e.getBlock().getLocation());
				p.sendMessage("§aDu hast den Block entsperrt");
			}
			if(Main.WardrobeLoc.contains(e.getBlock().getLocation())) {
				SQLData.removeWardrobe(e.getBlock().getLocation());
				p.sendMessage("§aDu hast die Wardrobe entfernt");
			}
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
    public void FrameEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ItemFrame) {
            if (e.getDamager() instanceof Player) {
                if (!Main.Admin.contains(e.getDamager())) {
                    e.setCancelled(true);
                } else {
                	e.setCancelled(false);
                }
            }
            if (e.getDamager() instanceof Projectile) {
                if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                        e.getDamager().remove();
                        e.setCancelled(true);
                }
            }
        }
    }
	
	@EventHandler
	public void AnderItemFrame(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof ItemFrame) {
			Player p = e.getPlayer();
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		} else if(Main.BlockSperreLoc.contains(e.getRightClicked().getLocation())){
			Player p = e.getPlayer();
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void AbbauenItemFrame(HangingBreakByEntityEvent e) {
		if(e.getEntity() instanceof ItemFrame) {
				try {
					Player p = Bukkit.getPlayer(e.getRemover().getName());
					if(Main.Admin.contains(p.getUniqueId().toString())) {
						e.setCancelled(false);
						if(Main.BlockSperreLoc.contains(e.getEntity().getLocation())) {
							Main.BlockSperreLoc.remove(e.getEntity().getLocation());
							p.sendMessage("§aDu hast den Block entsperrt");
						}
					} else {
						e.setCancelled(true);
					}
				} catch (Exception e2) {
				}
		} else if(e.getEntity() instanceof Painting) {
			try {
				Player p = Bukkit.getPlayer(e.getRemover().getName());
				if(Main.Admin.contains(p.getUniqueId().toString())) {
					e.setCancelled(false);
					if(Main.BlockSperreLoc.contains(e.getEntity().getLocation())) {
						Main.BlockSperreLoc.remove(e.getEntity().getLocation());
						p.sendMessage("§aDu hast den Block entsperrt");
					}
				} else {
					e.setCancelled(true);
				}
			} catch (Exception e2) {
			}
		}
	}
	
}
