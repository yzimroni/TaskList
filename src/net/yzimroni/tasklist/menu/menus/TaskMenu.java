package net.yzimroni.tasklist.menu.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.yzimroni.tasklist.menu.Menu;
import net.yzimroni.tasklist.task.Task;
import net.yzimroni.tasklist.utils.ItemBuilder;

public class TaskMenu extends Menu {

	private static final ItemStack COMPLETE = new ItemBuilder(Material.DIAMOND).glow()
			.displayName(ChatColor.GOLD + "Complete!").build();
	private static final ItemStack UNCOMPLETE = new ItemBuilder(Material.DIAMOND_ORE).glow()
			.displayName(ChatColor.GRAY + "Mark as not completed").build();

	private Task task;

	public TaskMenu(Task task) {
		super();
		this.task = task;
	}

	@Override
	public Inventory createInventory(Inventory inventory) {
		Inventory i = Bukkit.createInventory(null, 2 * 9, "Task: " + task.getName());
		updateInvenotry(i);
		return i;
	}

	public void updateInvenotry(Inventory i) {
		i.setItem(4, task.getItemStack());
		i.setItem(13, task.isCompleted() ? UNCOMPLETE : COMPLETE);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e) {
		ItemStack i = e.getCurrentItem();
		if (COMPLETE.isSimilar(i)) {
			task.setCompleted(System.currentTimeMillis());
			updateInvenotry(e.getClickedInventory());
		} else if (UNCOMPLETE.isSimilar(i)) {
			task.setCompleted(0);
			updateInvenotry(e.getClickedInventory());
		}
	}

}
