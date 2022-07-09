package de.ltt.server.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import de.ltt.other.PlayerInteractInv;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerDismount implements Listener{
	
	public static HashMap<Player, BukkitRunnable> waitStun = new HashMap<Player, BukkitRunnable>();
	public static HashMap<Player, Player> stunMap = new HashMap<Player, Player>();
	public static HashMap<Player, Integer> stunTime = new HashMap<Player, Integer>();
	
	@EventHandler
	public void onPlayerDismount(EntityDismountEvent e) {
		if(e.getEntity() instanceof Player) {
			Player t = (Player) e.getEntity();
			if(e.getDismounted() instanceof Player) {
				Player p = (Player) e.getDismounted();
				if(PlayerInteractInv.packMap.containsKey(p)) {
					if(PlayerInteractInv.packMap.get(p).equals(t)) {
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
							@Override
							public void run() {
								p.setPassenger(t);
								t.sendMessage("§cDu bist gepackt!");								
							}
						}, 1);
						return;
					}
				}
			}else if(e.getDismounted() instanceof ArmorStand) {
				Player p = (Player) e.getEntity();
				PlayerInfo pi = new PlayerInfo(p);
				if(pi.isSitting()) {
					pi.setSitting(false);
					e.getDismounted().remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
			Player p = (Player) e.getWhoClicked();
			PlayerInfo pi = new PlayerInfo(p);
			if(pi.haspacked()) {
				Player t = PlayerInteractInv.packMap.get(p);
				if(e.getView().getTitle().equals("§eWähle eine Aktion")) {
						t = PlayerInteractInv.packMap.get(p);
						if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6" + t.getName())) {
							Inventory Inv = Bukkit.createInventory(null, 9, "§eWas möchtest du mit §6" + t.getName() + "§e machen");
							
							ItemStack freilassenItem = new ItemBuilder(Material.LEGACY_LEASH).setName("§6Freilassen").build();
							ItemStack fesselnItem = new ItemBuilder(Material.LEGACY_LEASH).setName("§6Fesseln").build();
							
							Inv.addItem(freilassenItem);
							Inv.addItem(fesselnItem);
							p.openInventory(Inv);
						}
					e.setCancelled(true);
				}else if(e.getView().getTitle().equals("§eWas möchtest du mit §6" + t.getName() + "§e machen")) {
					PlayerInfo ti = new PlayerInfo(t);
					if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Freilassen")) {
						ti.setispacked(false);				
						pi.sethaspacked(false);
						p.sendMessage("§eDu hast §6" + t.getName() + " §efreigelassen!");
						t.sendMessage("§aDu wurdest freigelassen!");
						PlayerInteractInv.packMap.remove(p);
						t.getVehicle().setPassenger(null);
						t.teleport(p);
						p.showPlayer(t);
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Fesseln")) {
						if(!ti.isstunned()) {
							ti.setispacked(false);
							pi.sethaspacked(false);
							p.sendMessage("§eDu fesselst §6" + t.getName());
							t.sendMessage("§4Du wirst von §6" + p.getName() + "§4 gefesselt!");
							t.sendMessage("§eVersuch zu fliehen");
							PlayerInteractInv.packMap.remove(p);
							t.getVehicle().setPassenger(null);
							t.teleport(p);
							p.showPlayer(t);
							t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 4));
							ti.setcanJump(false);
							stunMap.put(p, t);
							int i = 0;
							stunTime.put(p, i);
							waitStun.put(p, new BukkitRunnable() {
								
								@Override
								public void run() {
									if(stunTime.get(p) < 15) {
										if(p.getLocation().distance(stunMap.get(p).getLocation()) > 2) {
											waitStun.get(p).cancel();
											PlayerInfo ti = new PlayerInfo(stunMap.get(p));
											ti.setcanJump(true);
											p.sendMessage("§6" + stunMap.get(p).getName() + "§4 ist entkommen!");
											stunMap.get(p).sendMessage("§aDu bist entkommen!");
											stunMap.get(p).removePotionEffect(PotionEffectType.SLOW);
										}else {
											stunTime.put(p, stunTime.get(p) + 1);
											String progress = "";
											for(int l = 1; l <= stunTime.get(p); l++) {
												progress += "§6⬛";
											}

											for(int a = progress.length()/3; a <= 15 ; a++) {
												progress += "§4⬛";
											}
											
											TextComponent tc = new TextComponent();
											tc.setText(progress);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, tc);
										}
									}else {
										PlayerInfo ti = new PlayerInfo(stunMap.get(p));
										ti.setisstunned(true);
										ti.setcanJump(true);
										stunMap.get(p).sendMessage("§4Du bist nun gefesselt!");
										p.sendMessage("§6" + stunMap.get(p).getName() + "§a ist nun gefesselt!");
										stunMap.remove(p);
										waitStun.get(p).cancel();
										waitStun.remove(p);
									}
								}
							});
							waitStun.get(p).runTaskTimer(Main.getPlugin(), 0, 20);
						}else
							p.sendMessage("§cDieser Spieler ist bereits gefesselt!");
					}
					e.setCancelled(true);
					p.closeInventory();
				}
			}
	}

}
