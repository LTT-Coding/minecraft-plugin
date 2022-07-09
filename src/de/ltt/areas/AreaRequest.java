package de.ltt.areas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.area.AreaInfo;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class AreaRequest implements CommandExecutor, Listener {

	
	public static HashMap<Player, List<Integer>> selectedArea = new HashMap<>();
	public static HashMap<Player, Location> selectedBlocks = new HashMap<>();
	public static List<Player> areaSelect = new ArrayList<>();
	public static List<Player> clickBlock = new ArrayList<>();
	public static HashMap<Player, Location> overBlock = new HashMap<Player, Location>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("kaufen")) {
					if (!areaSelect.contains(p)) {
						if(!AreaInfo.hasRequested(p)) {
							areaSelect.add(p);
							p.sendMessage("§eWähle den Bereich aus den du Kaufen möchtest");
						}else
							p.sendMessage("§cDu hast bereits ein Grundstück beantragt!");
					} else
						p.sendMessage("§cDu bist bereits im Auswahlmenü!");
				}
			}else if(args.length == 0) {
				
			}else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("kaufen")) {
					if(args[1].equalsIgnoreCase("cancel")) {
						if(selectedArea.containsKey(p) || selectedArea.containsKey(p) || areaSelect.contains(p)) {
							if(selectedArea.containsKey(p)) {
								List<Integer> selected = selectedArea.get(p);
								int LocSX = selected.get(0);
								int LocLX = selected.get(1);
								int LocSZ = selected.get(2);
								int LocLZ = selected.get(3);
								int py = selected.get(4);
								Location testloc;
								testloc = new Location(p.getWorld(), LocSX, py, LocSZ);
								while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ()).getBlock()
										.isEmpty()) {
									testloc.setY(testloc.getY() + 1);
								}
								while (testloc.getBlock().isEmpty()) {
									testloc.setY(testloc.getY() - 1);
								}
								BlockState state = testloc.getBlock().getState();
								state.update();
								testloc.setX(LocLX);
								while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ()).getBlock()
										.isEmpty()) {
									testloc.setY(testloc.getY() + 1);
								}
								while (testloc.getBlock().isEmpty()) {
									testloc.setY(testloc.getY() - 1);
								}
								state = testloc.getBlock().getState();
								state.update();
								testloc.setZ(LocLZ);
								while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ()).getBlock()
										.isEmpty()) {
									testloc.setY(testloc.getY() + 1);
								}
								while (testloc.getBlock().isEmpty()) {
									testloc.setY(testloc.getY() - 1);
								}
								state = testloc.getBlock().getState();
								state.update();
								testloc.setX(LocSX);
								while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ()).getBlock()
										.isEmpty()) {
									testloc.setY(testloc.getY() + 1);
								}
								while (testloc.getBlock().isEmpty()) {
									testloc.setY(testloc.getY() - 1);
								}
								state = testloc.getBlock().getState();
								state.update();
								AreaRequest.selectedArea.remove(p);
							}
							if(selectedBlocks.containsKey(p)) {
								selectedBlocks.get(p).getBlock().getState().update();
								selectedBlocks.remove(p);
							}
							if(areaSelect.contains(p))areaSelect.remove(p);
							p.sendMessage("§4Vorgang wurde abgebrochen!");
						}
					}
				}
			}
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (!clickBlock.contains(e.getPlayer())) {
				Player p = e.getPlayer();
				if (areaSelect.contains(p)) {
					if (selectedBlocks.containsKey(p)) {
						clickBlock.add(p);
						if(!AreaInfo.isOnArea(e.getClickedBlock().getLocation())) {
							if(!AreaInfo.isonRequest(e.getClickedBlock().getLocation())) {
								Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

									@Override
									public void run() {
										Location p1 = selectedBlocks.get(p);
										Location p2 = e.getClickedBlock().getLocation();
										Location OverLoc = p1.add(0, 1, 0);
										int LocSX;
										int LocSZ;
										int LocLX;
										int LocLZ;
										if (p1.getX() < p2.getX()) {
											LocSX = (int) p1.getX();
											LocLX = (int) p2.getX();
										} else {
											LocSX = (int) p2.getX();
											LocLX = (int) p1.getX();
										}

										if (p1.getZ() < p2.getZ()) {
											LocSZ = (int) p1.getZ();
											LocLZ = (int) p2.getZ();
										} else {
											LocSZ = (int) p2.getZ();
											LocLZ = (int) p1.getZ();
										}
										int fläche = (LocLX-LocSX + 1)*(LocLZ - LocSZ + 1);
										if(fläche <= 2500) {
											Location testloc;
											testloc = new Location(p.getWorld(), LocSX, p1.getY(), LocSZ);
											while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
													.getBlock().isEmpty()) {
												testloc.setY(testloc.getY() + 1);
											}
											while (testloc.getBlock().isEmpty()) {
												testloc.setY(testloc.getY() - 1);
											}
											p.sendBlockChange(testloc, Material.GREEN_CONCRETE, (byte) 5);
											testloc.setX(LocLX);
											while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
													.getBlock().isEmpty()) {
												testloc.setY(testloc.getY() + 1);
											}
											while (testloc.getBlock().isEmpty()) {
												testloc.setY(testloc.getY() - 1);
											}
											p.sendBlockChange(testloc, Material.GREEN_CONCRETE, (byte) 5);
											testloc.setZ(LocLZ);
											while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
													.getBlock().isEmpty()) {
												testloc.setY(testloc.getY() + 1);
											}
											while (testloc.getBlock().isEmpty()) {
												testloc.setY(testloc.getY() - 1);
											}
											p.sendBlockChange(testloc, Material.GREEN_CONCRETE, (byte) 5);
											testloc.setX(LocSX);
											while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1, testloc.getZ())
													.getBlock().isEmpty()) {
												testloc.setY(testloc.getY() + 1);
											}
											while (testloc.getBlock().isEmpty()) {
												testloc.setY(testloc.getY() - 1);
											}
											p.sendBlockChange(testloc, Material.GREEN_CONCRETE, (byte) 5);
											selectedBlocks.remove(p);
											areaSelect.remove(p);
											List<Integer> selected = new ArrayList<>();
											selected.add(LocSX);
											selected.add(LocLX);
											selected.add(LocSZ);
											selected.add(LocLZ);
											selected.add((int) p.getLocation().getY());
											selectedArea.put(p, selected);
											overBlock.put(p, OverLoc);
											p.sendMessage("§eBitte bestätige deine Auswahl");
											p.sendMessage("§eDas Grundstück ist §6" + fläche + "m² §egroß!");
											TextComponent tc = new TextComponent();
											tc.setText("§7[§aBestätigen§7]");
											tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accepthouserequest"));
											tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAnnehmen").create()));
											tc.setBold(true);
											TextComponent leer = new TextComponent();
											leer.setText("  ");
											leer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
											leer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
											leer.setBold(true);
											TextComponent tc2 = new TextComponent();
											tc2.setText("§7[§4Abbrechen§7]");
											tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/denyhouserequest"));
											tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Ablehnen").create()));
											tc2.setBold(true);
											tc.addExtra(leer);
											tc.addExtra(tc2);
											
											p.spigot().sendMessage(tc);
											
											/*Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

												@Override
												public void run() {

													Location testloc;
													testloc = new Location(p.getWorld(), LocSX, p1.getY(), LocSZ);
													while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1,
															testloc.getZ()).getBlock().isEmpty()) {
														testloc.setY(testloc.getY() + 1);
													}
													while (testloc.getBlock().isEmpty()) {
														testloc.setY(testloc.getY() - 1);
													}
													BlockState state = testloc.getBlock().getState();
													state.update();
													testloc.setX(LocLX);
													while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1,
															testloc.getZ()).getBlock().isEmpty()) {
														testloc.setY(testloc.getY() + 1);
													}
													while (testloc.getBlock().isEmpty()) {
														testloc.setY(testloc.getY() - 1);
													}
													state = testloc.getBlock().getState();
													state.update();
													testloc.setZ(LocLZ);
													while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1,
															testloc.getZ()).getBlock().isEmpty()) {
														testloc.setY(testloc.getY() + 1);
													}
													while (testloc.getBlock().isEmpty()) {
														testloc.setY(testloc.getY() - 1);
													}
													state = testloc.getBlock().getState();
													state.update();
													testloc.setX(LocSX);
													while (!new Location(p.getWorld(), testloc.getX(), testloc.getY() + 1,
															testloc.getZ()).getBlock().isEmpty()) {
														testloc.setY(testloc.getY() + 1);
													}
													while (testloc.getBlock().isEmpty()) {
														testloc.setY(testloc.getY() - 1);
													}
													state = testloc.getBlock().getState();
													state.update();
												}
											}, 40);*/
										}else
											p.sendMessage("§cDas Grundstück darf Maximal §62500m² §cgroß sein");
									}
								}, 1);
							}else
								p.sendMessage("§cDiese Stelle wurde bereits zum Kaufen beantragt!");
						}else
							p.sendMessage("§cDiese Stelle gehört bereits zu einem Grundstück!");
					} else {
						clickBlock.add(p);
						if(!AreaInfo.isOnArea(e.getClickedBlock().getLocation())) {
							if(!AreaInfo.isonRequest(e.getClickedBlock().getLocation())) {
								Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
									@Override
									public void run() {
										selectedBlocks.put(p, e.getClickedBlock().getLocation());
										p.sendBlockChange(e.getClickedBlock().getLocation(), Material.GREEN_CONCRETE, (byte) 5);
										p.sendMessage("§aErste Position wurde Registriert");
										p.sendMessage("§eWähle eine zweite Stelle aus!");
									}
								}, 1);
							}else
								p.sendMessage("§cDiese Stelle wurde bereits zum Kaufen beantragt!");
						}else
							p.sendMessage("§cDiese Stelle gehört bereits zu einem Grundstück!");
					}
					Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							clickBlock.remove(p);
						}
					}, 5);
					e.setCancelled(true);
				}
			}
		}
	}

}
