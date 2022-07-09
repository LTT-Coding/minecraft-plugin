package de.ltt.server.reflaction;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import de.ltt.server.main.Main;
import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class ItemBuilder {
	
	private ItemStack item;
	private ItemMeta itemMeta;
	
	public ItemBuilder(Material material) {
		item = new ItemStack(material);
		itemMeta = item.getItemMeta();
	}
	
	public ItemBuilder(String url) {
		item = ItemSkulls.getSkull(url);
		itemMeta = item.getItemMeta();
	}
	public ItemBuilder(ItemStack item) {
		this.item = item;
		itemMeta = item.getItemMeta();
	}
	
	public ItemBuilder setName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}
	public ItemBuilder setLore(String... lore) {
		itemMeta.setLore(Arrays.asList(lore));
		return this;
	}
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder setCustomModelData(int customModelData) {
		itemMeta.setCustomModelData(customModelData);
		return this;
	}
	
	public ItemBuilder setTag(String key, String value) {
		itemMeta.getCustomTagContainer().setCustomTag(new NamespacedKey(Main.getPlugin(), key), ItemTagType.STRING, value);
        return this;
	}
	
	public ItemStack build() {
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static String getTagValue(ItemStack item, String key) {
		if(!item.getItemMeta().getCustomTagContainer().hasCustomTag(new NamespacedKey(Main.getPlugin(), key), ItemTagType.STRING))return "";
		return item.getItemMeta().getCustomTagContainer().getCustomTag(new NamespacedKey(Main.getPlugin(), key), ItemTagType.STRING);
	}
}
