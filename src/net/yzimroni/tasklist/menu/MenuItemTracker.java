package net.yzimroni.tasklist.menu;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuItemTracker {

	private HashMap<ItemStack, ItemHandler> itemHandlers = new HashMap<ItemStack, ItemHandler>();
	private HashMap<Function<ItemStack, Boolean>, ItemHandler> itemMatcherHandlers = new HashMap<Function<ItemStack, Boolean>, ItemHandler>();

	public MenuItemTracker() {

	}

	public void addItemHandler(ItemStack item, ItemHandler handler) {
		itemHandlers.put(item, handler);
	}

	public void addItemMatcherHandler(Function<ItemStack, Boolean> matcher, ItemHandler handler) {
		itemMatcherHandlers.put(matcher, handler);
	}

	public void onInventoryClick(ItemStack item, Player player) {
		if (item == null || item.getType() == null || item.getType() == Material.AIR) {
			return;
		}
		for (Entry<ItemStack, ItemHandler> e : itemHandlers.entrySet()) {
			if (e.getKey().isSimilar(item)) {
				e.getValue().onClick(item, player);
				return;
			}
		}

		for (Entry<Function<ItemStack, Boolean>, ItemHandler> e : itemMatcherHandlers.entrySet()) {
			if (e.getKey().apply(item)) {
				e.getValue().onClick(item, player);
				return;
			}
		}
	}

	public void clearItemHandlers() {
		itemHandlers.clear();
	}

	public void clearItemMatchersHandlers() {
		itemMatcherHandlers.clear();
	}

	public void clearAll() {
		clearItemHandlers();
		clearItemMatchersHandlers();
	}

}
