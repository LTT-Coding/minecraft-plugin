package de.ltt.other;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;

public class PlayerInteractInv implements Listener{
	
	public static HashMap<Player, Player> interactMap = new HashMap<Player, Player>();
	public static HashMap<Player, Player> packMap = new HashMap<Player, Player>();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked() instanceof Player) {
			Player p = e.getPlayer();
			if(p.getItemInHand().getType().equals(Material.AIR)) {
				Player t = (Player) e.getRightClicked();
				interactMap.put(p, t);
				
				Inventory Inv = Bukkit.createInventory(null, 9*3, "§eWähle eine Aktion aus");
				
				ItemStack packenItem = new ItemBuilder(Material.LEGACY_LEASH).setName("§6Packen").build();
				Inv.setItem(10, packenItem);
				p.openInventory(Inv);
			}
		}
	}
	
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		try {
			if(e.getView().getTitle().equals("§eWähle eine Aktion aus")) {
				Player p = (Player) e.getWhoClicked();
				Player t = interactMap.get(p);
				PlayerInfo pi = new PlayerInfo(p);
				PlayerInfo ti = new PlayerInfo(t);
				if(p.getLocation().distance(t.getLocation()) > 2) {
					p.closeInventory();
					p.sendMessage("§cDu hast dich zu weit von der Person entfernt!");
				}else {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Packen")) {
						if(!pi.ispacked()) {
							if(!pi.haspacked()) {
								if(!ti.haspacked()) {
									if(!ti.ispacked()) {
										if(!pi.isstunned()) {
											p.setPassenger(t);
											p.hidePlayer(t);
											p.sendMessage("§aDu hast die Person §agepackt!");
											t.sendMessage("§4Du wurdest §4gepackt!");
											pi.sethaspacked(true);
											ti.setispacked(true);
											packMap.put(p, t);
										}else
											p.sendMessage("§cDu bist gefesselt, du kannst niemanden Packen");
									}else
										p.sendMessage("§cDer Spieler ist bereits von jemandem anders gepackt!");
								}else
									p.sendMessage("§cDer Spieler hat bereits jemanden gepackt und kann daher nicht gepackt werden!");
							}else
								p.sendMessage("§cDu hast bereits jemanden gepackt!");
						}else
							p.sendMessage("§cDu bist gepackt und kannst daher keine anderen Spieler packen!");
					}
				}
				
				e.setCancelled(true);
				p.closeInventory();
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

}
