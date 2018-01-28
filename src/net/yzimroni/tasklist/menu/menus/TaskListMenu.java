package net.yzimroni.tasklist.menu.menus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.yzimroni.tasklist.TaskListPlugin;
import net.yzimroni.tasklist.menu.Menu;
import net.yzimroni.tasklist.menu.MenuManager;
import net.yzimroni.tasklist.menu.builder.MenuBuilder;
import net.yzimroni.tasklist.task.Task;
import net.yzimroni.tasklist.utils.Utils;

public class TaskListMenu extends Menu {

	private static final int TASKS_PER_ROW = 7;
	private static final int ROWS_PER_PAGE = 3;
	private static final int TASKS_PER_PAGE = TASKS_PER_ROW * ROWS_PER_PAGE;

	private int page;

	public TaskListMenu(int page) {
		super();
		this.page = page;
		addItemHandlers();
	}

	@Override
	public Inventory createInventory(Inventory inventory) {
		Inventory i = Bukkit.createInventory(null, 6 * 9, "Task List");
		updateInvenotry(i);
		return i;
	}

	private void addItemHandlers() {
		getItemTracker().addItemHandler(Utils.ITEM_PREVIOUS, (i, p) -> {
			if (hasPreviousPage()) {
				page--;
				updateInvenotry(p.getOpenInventory().getTopInventory());
			}
		});
		getItemTracker().addItemHandler(Utils.ITEM_NEXT, (i, p) -> {
			if (hasNextPage()) {
				page++;
				updateInvenotry(p.getOpenInventory().getTopInventory());
			}
		});
		getItemTracker().addItemMatcherHandler((i) -> {
			return i.hasItemMeta() && i.getItemMeta().hasDisplayName() && TaskListPlugin.get().getManager()
					.getTaskByName(ChatColor.stripColor(i.getItemMeta().getDisplayName())) != null;
		}, (i, p) -> {
			Task t = TaskListPlugin.get().getManager()
					.getTaskByName(ChatColor.stripColor(i.getItemMeta().getDisplayName()));
			MenuManager.get().open(p, new TaskMenu(t));
		});
	}

	public void updateInvenotry(Inventory i) {
		i.clear();
		/*
		 * TODO find out a way to change the window title After it, we can change the
		 * back/next buttons to only update the current window, and not open another one
		 */
		MenuBuilder builder = new MenuBuilder(i);
		builder.setEntriesPerRow(TASKS_PER_ROW);
		// builder.setRowEndSpace(1);
		// builder.setRowStartSpace(1);
		builder.setRowsPerPage(ROWS_PER_PAGE);
		builder.setStartRow(1);
		List<Task> tasks = TaskListPlugin.get().getManager().getSortedList();
		builder.create(getTasksForPage(tasks).stream().map(Task::getItemStack).collect(Collectors.toList()));

		if (hasPreviousPage()) {
			// 48
			i.setItem(48, Utils.ITEM_PREVIOUS);
		}

		ItemStack diamond = new ItemStack(Material.DIAMOND);
		diamond.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta meta = diamond.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Tasks - Page " + page);
		meta.setLore(Arrays.asList("Total tasks: " + TaskListPlugin.get().getManager().getTasks().size(),
				"Completed tasks: " + TaskListPlugin.get().getManager().getTasks(true).size(),
				"In progress tasks: " + TaskListPlugin.get().getManager().getTasks(false).size()));
		diamond.setItemMeta(meta);
		i.setItem(49, diamond);

		if (hasNextPage()) {
			i.setItem(50, Utils.ITEM_NEXT);
		}
	}

	public boolean hasPreviousPage() {
		return page > 1;
	}

	public boolean hasNextPage() {
		List<Task> tasks = TaskListPlugin.get().getManager().getTasks();
		int index_end = (page) * TASKS_PER_PAGE;
		return tasks.size() > index_end;
	}

	public List<Task> getTasksForPage(List<Task> tasks) {
		int index_start = (page - 1) * TASKS_PER_PAGE;
		int index_end = (page) * TASKS_PER_PAGE;
		if (index_end > (tasks.size())) {
			index_end = tasks.size();
		}
		return tasks.subList(index_start, index_end);
	}

	public static int getPageNumberForTask(List<Task> tasks, Task task) {
		int index = tasks.indexOf(task);
		if (index == -1) {
			return 1;
		}
		int number = index + 1;
		double page = (double) number / TASKS_PER_PAGE;
		return (int) Math.ceil(page);
	}

}
