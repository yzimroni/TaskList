package net.yzimroni.tasklist.menu.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.yzimroni.tasklist.TaskListPlugin;
import net.yzimroni.tasklist.menu.Menu;
import net.yzimroni.tasklist.menu.MenuManager;
import net.yzimroni.tasklist.menu.builder.MenuBuilder;
import net.yzimroni.tasklist.task.Task;
import net.yzimroni.tasklist.utils.ItemBuilder;
import net.yzimroni.tasklist.utils.Utils;

public class TaskDeleteMenu extends Menu {

	private static final ItemStack DELETE = new ItemBuilder(Material.REDSTONE_BLOCK)
			.displayName(ChatColor.RED + "I'm sure!")
			.lore(ChatColor.RED + "CLICK ONLY IF YOU ARE SURE", ChatColor.RED + "YOU WANT TO DELETE THIS TASK!")
			.build();

	private Task task;

	public TaskDeleteMenu(Task task) {
		super();
		this.task = task;
		addItemTrackers();
	}

	private void addItemTrackers() {
		getItemTracker().addItemHandler(Utils.ITEM_BACK, (i, p) -> {
			MenuManager.get().open(p, new TaskMenu(task));
		});
		getItemTracker().addItemHandler(DELETE, (i, p) -> {
			TaskListPlugin.get().getManager().removeTask(task);
			p.sendMessage(ChatColor.RED + "Task '" + task.getName() + "' removed.");
			MenuManager.get().open(p, new TaskListMenu(1));
		});

	}

	@Override
	public Inventory createInventory(Inventory inventory) {
		Inventory i = Bukkit.createInventory(null, 3 * 9, "Are you sure you want to delete '" + task.getName() + "'?");
		i.setItem(4, task.getItemStack());

		MenuBuilder builder = new MenuBuilder(i);
		builder.setRowsPerPage(1);
		builder.setEntriesPerRow(7);
		builder.setStartRow(1);

		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(DELETE);
		items.add(MenuBuilder.BR);
		items.add(Utils.ITEM_BACK);

		builder.create(items);
		return i;
	}

}
