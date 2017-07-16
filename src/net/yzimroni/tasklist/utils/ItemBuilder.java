package net.yzimroni.tasklist.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	private Material type;
	private int amount = 1;
	private byte data;
	private short durability;

	private String displayName;
	private List<String> lore;
	private Map<Enchantment, Integer> enchants;
	private Set<ItemFlag> itemFlags;

	public ItemBuilder(Material type) {
		this.type = type;
	}

	@SuppressWarnings("deprecation")
	public ItemBuilder(ItemStack item) {
		this.type = item.getType();
		this.amount = item.getAmount();
		this.data = item.getData().getData();
		this.durability = item.getDurability();

		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			this.displayName = meta.getDisplayName();
			this.lore = meta.getLore();
			this.enchants = meta.getEnchants();
			this.itemFlags = meta.getItemFlags();
		}
	}

	public ItemBuilder amount(int amount) {
		this.amount = amount;
		return this;
	}

	public ItemBuilder data(byte data) {
		this.data = data;
		return this;
	}

	public ItemBuilder durability(short durability) {
		this.durability = durability;
		return this;
	}

	public ItemBuilder displayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public ItemBuilder lore(List<String> lore) {
		this.lore = lore;
		return this;
	}

	public ItemBuilder lore(String... lore) {
		this.lore = Arrays.asList(lore);
		return this;
	}

	public ItemBuilder addLore(String... lore) {
		if (this.lore == null) {
			this.lore = new ArrayList<String>();
		}
		this.lore.addAll(Arrays.asList(lore));
		return this;
	}

	public ItemBuilder enchants(Map<Enchantment, Integer> enchants) {
		this.enchants = enchants;
		return this;
	}

	public ItemBuilder enchant(Enchantment enchant, int level) {
		if (this.enchants == null) {
			this.enchants = new HashMap<Enchantment, Integer>();
		}
		this.enchants.put(enchant, level);
		return this;
	}

	public ItemBuilder itemFlags(Set<ItemFlag> itemFlags) {
		this.itemFlags = itemFlags;
		return this;
	}

	public ItemBuilder itemFlag(ItemFlag flag) {
		if (this.itemFlags == null) {
			this.itemFlags = new HashSet<ItemFlag>();
		}
		this.itemFlags.add(flag);
		return this;
	}

	public ItemBuilder glow() {
		enchant(Enchantment.ARROW_DAMAGE, 1);
		itemFlag(ItemFlag.HIDE_ENCHANTS);
		return this;
	}

	@SuppressWarnings("deprecation")
	public ItemStack build() {
		ItemStack itemStack = new ItemStack(type, amount, durability, data);

		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(displayName);
		itemMeta.setLore(lore);
		if (enchants != null) {
			enchants.forEach((e, v) -> {
				itemMeta.addEnchant(e, v, true);
			});
		}
		if (itemFlags != null) {
			itemFlags.forEach(itemMeta::addItemFlags);
		}

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

}
