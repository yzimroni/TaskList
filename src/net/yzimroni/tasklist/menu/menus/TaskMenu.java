package net.yzimroni.tasklist.menu.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.yzimroni.tasklist.TaskListPlugin;
import net.yzimroni.tasklist.menu.Menu;
import net.yzimroni.tasklist.menu.MenuManager;
import net.yzimroni.tasklist.menu.builder.MenuBuilder;
import net.yzimroni.tasklist.task.Task;
import net.yzimroni.tasklist.utils.ItemBuilder;
import net.yzimroni.tasklist.utils.Utils;

public class TaskMenu extends Menu {

	private static final ItemStack COMPLETE = new ItemBuilder(Material.DIAMOND).glow()
			.displayName(ChatColor.GOLD + "Complete!").build();
	private static final ItemStack UNCOMPLETE = new ItemBuilder(Material.DIAMOND_ORE).glow()
			.displayName(ChatColor.GRAY + "Mark as not completed").build();
	private static final ItemStack CHANGE_XP = new ItemBuilder(Material.EXP_BOTTLE)
			.displayName(ChatColor.GREEN + "Change xp").build();
	private static final ItemStack DELETE = new ItemBuilder(Material.REDSTONE_BLOCK)
			.displayName(ChatColor.RED + "Delete this task").build();

	private Task task;

	public TaskMenu(Task task) {
		super();
		this.task = task;
		addItemTrackers();
	}

	private void addItemTrackers() {
		getItemTracker().addItemHandler(COMPLETE, (i, p) -> {
			if (!task.isCompleted()) {
				task.complete(p);
				updateInvenotry(p.getOpenInventory().getTopInventory());
			}
		});
		getItemTracker().addItemHandler(UNCOMPLETE, (i, p) -> {
			if (task.isCompleted()) {
				task.setCompleted(0);
				for (UUID u : Utils.DEFAULT_PLAYERS) {
					Player player = Bukkit.getPlayer(u);
					if (player != null) {
						p.setLevel(Math.max(p.getLevel() - task.getXp(), 0));
					}
				}
				updateInvenotry(p.getOpenInventory().getTopInventory());
			}
		});
		getItemTracker().addItemHandler(DELETE, (i, p) -> {
			MenuManager.get().open(p, new TaskDeleteMenu(task));
		});
		getItemTracker().addItemHandler(Utils.ITEM_BACK, (i, p) -> {
			MenuManager.get().open(p, new TaskListMenu(
					TaskListMenu.getPageNumberForTask(TaskListPlugin.get().getManager().getSortedList(), task)));
		});
		getItemTracker().addItemHandler(CHANGE_XP, (i, p) -> {
			if (!task.isCompleted()) {
				MenuManager.get().open(p, new ChangeXpMenu(task, 1));
			}
		});
	}

	@Override
	public Inventory createInventory(Inventory inventory) {
		Inventory i = Bukkit.createInventory(null, 3 * 9, "Task: " + task.getName());
		updateInvenotry(i);
		return i;
	}

	public void updateInvenotry(Inventory i) {
		i.setItem(4, task.getItemStack());

		MenuBuilder builder = new MenuBuilder(i);
		builder.setRowsPerPage(1);
		builder.setEntriesPerRow(7);
		builder.setStartRow(1);

		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(task.isCompleted() ? UNCOMPLETE : COMPLETE);
		if (!task.isCompleted()) {
			items.add(CHANGE_XP);
		}
		items.add(DELETE);
		items.add(MenuBuilder.BR);
		items.add(Utils.ITEM_BACK);

		builder.create(items);
	}

}
