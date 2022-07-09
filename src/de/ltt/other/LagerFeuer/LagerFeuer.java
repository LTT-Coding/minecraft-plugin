package de.ltt.other.LagerFeuer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.Cauldron;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;

public class LagerFeuer implements Listener {

	public static List<Player> Campfire = new ArrayList<Player>();
	public static HashMap<Player, Integer> ClickFeuer = new HashMap<Player, Integer>();

	@EventHandler
	public void FeuerAus(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getClickedBlock().getType().equals(Material.CAMPFIRE)) {
				Player p = e.getPlayer();
				LagerfeuerInfo li = LagerfeuerInfo.getFireByLoc(e.getClickedBlock().getLocation());
				if (p.getItemInHand().getType() == Material.POTION) {
					PotionMeta BM = (PotionMeta) e.getItem().getItemMeta();
					PotionData water = new PotionData(PotionType.WATER);
					Lightable s2 = (Lightable) e.getClickedBlock().getBlockData();
					if (BM.getBasePotionData().equals(water)) {
						if (s2.isLit() == true) {
							li.stopFeuer();
							p.setItemInHand(new ItemStack(Material.GLASS_BOTTLE));
							p.sendMessage("§6Du hast das Lagerfeuer gelöscht");

						}
					}
				} else if (p.getItemInHand().getType() == Material.FLINT_AND_STEEL) {
					Lightable s2 = (Lightable) e.getClickedBlock().getBlockData();
					e.setCancelled(true);
					if (s2.isLit() == false) {
						if (li.getHolz() > 0) {
							li.runFeuer();
							s2.setLit(true);
							e.getClickedBlock().setBlockData(s2);
							e.getClickedBlock().getState().update();
							p.sendMessage("§6Du hast das Lagerfeuer angemacht");
						} else
							p.sendMessage("§cDas Lagerfeuer hat nicht genug Holz");
					}
				}
			}
		}
	}

	@EventHandler
	public void Water(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getClickedBlock().getType().equals(Material.CAULDRON)) {
				if(e.getItem() != null) {
					if (e.getItem().getType()  == Material.GLASS_BOTTLE) { // Änderung 
						if (e.getItem().getAmount() == 1) {
							Cauldron cd = (Cauldron) e.getClickedBlock().getState().getData();
							if (!cd.isEmpty()) {
								Block ed = e.getClickedBlock();
								Levelled da = (Levelled) ed.getBlockData();
								da.setLevel(da.getLevel() - 1);
								ed.setBlockData(da);
								ItemStack item = p.getItemInHand();
								item.setAmount(item.getAmount() - 1);
								p.setItemInHand(item);
								ItemStack wasserB = new ItemStack(Material.POTION);
								PotionMeta BM = (PotionMeta) wasserB.getItemMeta();
								BM.setBasePotionData(new PotionData(PotionType.WATER));
								wasserB.setItemMeta(BM);
								p.setItemInHand(wasserB);

								e.setCancelled(true);
							}
						} else
							e.setCancelled(true);
					}
				}
			}

		}

	}

	@EventHandler
	public void CampfirePlace(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.CAMPFIRE) {
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

				@Override
				public void run() {
					LagerfeuerInfo li = new LagerfeuerInfo().addFeuer(e.getBlock().getLocation());
					li.stopFeuer();

				}
			}, 1);
		}

	}

	@EventHandler
	public void CampfireBreak(BlockBreakEvent e) {
		try {
			if (e.getBlock().getType() == Material.CAMPFIRE) {
				LagerfeuerInfo li = LagerfeuerInfo.getFireByLoc(e.getBlock().getLocation());
				li.removeFire();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			e.getPlayer().sendMessage("§cEin Fehle ist aufgetreten, bitte melde diesen bei der Administration");
		}
	}

	@EventHandler
	public void CampInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.CAMPFIRE) {
				Player p = e.getPlayer();
				LagerfeuerInfo li = LagerfeuerInfo.getFireByLoc(e.getClickedBlock().getLocation());
				if (p.isSneaking()) {
					if (p.getItemInHand().getType() == Material.AIR) {
						Inventory inv = Bukkit.createInventory(null, 9, "§eLagerfeuer");
						for (int i = 0; i < li.getHolz(); i++) {
							ItemStack Holz = new ItemBuilder(Material.OAK_LOG).setName("§6" + (i + 1)).build();
							inv.setItem(i * 2, Holz);
						}

						inv.setItem(8, new ItemBuilder(Material.GUNPOWDER).setName("§7Asche").setAmount(li.getAsche())
								.build());
						inv.setItem(1, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build());
						inv.setItem(3, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build());
						inv.setItem(5, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build());
						inv.setItem(7, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build());

						p.openInventory(inv);
						ClickFeuer.put(p, li.getId());
						e.setCancelled(true);
					} else if (p.getItemInHand().getType() == Material.OAK_LOG
							|| p.getItemInHand().getType() == Material.ACACIA_LOG
							|| p.getItemInHand().getType() == Material.BIRCH_LOG
							|| p.getItemInHand().getType() == Material.JUNGLE_LOG
							|| p.getItemInHand().getType() == Material.DARK_OAK_LOG
							|| p.getItemInHand().getType() == Material.SPRUCE_LOG) {
						if (li.getHolz() < 4) {
							p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
							li.addHolz();
						} else
							p.sendMessage("§cDas Lagerfeuer hat genug Holz");
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void CampInvInteract(InventoryClickEvent e) {
		try {
			if (e.getView().getTitle().equals("§eLagerfeuer")
					&& e.getView().getTopInventory().equals(e.getClickedInventory())) {
				if (e.getSlot() < 8) {
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Asche")) {
					e.setCancelled(true);
					LagerfeuerInfo li = new LagerfeuerInfo().loadFeuer(ClickFeuer.get(e.getWhoClicked()));
					e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
					li.setAsche(0);
					e.getWhoClicked().closeInventory();
				}
				e.setCancelled(true);
			} else if (e.getView().getTitle().equals("§eLagerfeuer")
					&& e.getView().getBottomInventory().equals(e.getClickedInventory())) {
				e.setCancelled(true);
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}

	}
}
