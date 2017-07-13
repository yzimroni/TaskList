package net.yzimroni.tasklist.menu.menus;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.yzimroni.tasklist.TaskListPlugin;
import net.yzimroni.tasklist.menu.Menu;
import net.yzimroni.tasklist.menu.MenuManager;
import net.yzimroni.tasklist.task.Task;
import net.yzimroni.tasklist.utils.Utils;

public class TaskListMenu extends Menu {

	private static final int TASKS_PER_ROW = 7;
	private static final int ROWS_PER_PAGE = 3;
	private static final int TASKS_PER_PAGE = TASKS_PER_ROW * ROWS_PER_PAGE;

	private static final int START_SPACING = 1;
	private static final int END_SPACING = 1;

	private int page;

	private int currentSlot = 0;
	private int currentSlotRow = 0;

	public TaskListMenu(int page) {
		super();
		this.page = page;
	}

	@Override
	public Inventory createInventory() {
		Inventory i = Bukkit.createInventory(null, 6 * 9, "Task List - Page " + page);
		updateInvenotry(i);
		return i;
	}

	public void updateInvenotry(Inventory i) {
		/*
		 * TODO find out a way to change the window title After it, we can change the
		 * back/next buttons to only update the current window, and not open another one
		 */
		currentSlot = 9;
		currentSlotRow = 0;
		List<Task> tasks = getTasksForPage();
		tasks.forEach(t -> {
			i.setItem(getNextTaskSlot(), t.getItemStack());
		});
		if (hasPreviousPage()) {
			// 48
			i.setItem(48, Utils.ITEM_PREVIOUS);
		}

		ItemStack diamond = new ItemStack(Material.DIAMOND);
		diamond.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta meta = diamond.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Tasks");
		meta.setLore(Arrays.asList("Total tasks: " + TaskListPlugin.get().getManager().getTasks().size(),
				"Completed tasks: " + TaskListPlugin.get().getManager().getTasks(true).size(),
				"In progress tasks: " + TaskListPlugin.get().getManager().getTasks(false).size()));
		diamond.setItemMeta(meta);
		i.setItem(49, diamond);

		if (hasNextPage()) {
			// 50
			i.setItem(50, Utils.ITEM_NEXT);
		}
	}

	public int getNextTaskSlot() {
		if (currentSlotRow == TASKS_PER_ROW) {
			currentSlotRow = 1;
			return currentSlot += (END_SPACING + START_SPACING + 1);
		}
		currentSlotRow++;
		return ++currentSlot;
	}

	public boolean hasPreviousPage() {
		return page > 1;
	}

	public boolean hasNextPage() {
		List<Task> tasks = TaskListPlugin.get().getManager().getTasks();
		int index_end = (page) * TASKS_PER_PAGE;
		return tasks.size() > index_end;
	}

	public List<Task> getTasksForPage() {
		List<Task> tasks = TaskListPlugin.get().getManager().getTasks();
		int index_start = (page - 1) * TASKS_PER_PAGE;
		int index_end = (page) * TASKS_PER_PAGE;
		if (index_end > (tasks.size())) {
			index_end = tasks.size();
		}
		return tasks.subList(index_start, index_end);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		if (i.isSimilar(Utils.ITEM_PREVIOUS)) {
			// page--;
			// updateInvenotry(e.getClickedInventory());
			MenuManager.get().open(p, new TaskListMenu(--page));
			return;
		} else if (i.isSimilar(Utils.ITEM_NEXT)) {
			// page++;
			// updateInvenotry(e.getClickedInventory());
			MenuManager.get().open(p, new TaskListMenu(++page));
			return;
		}
		
		if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
			Task t = TaskListPlugin.get().getManager().getTaskByName(ChatColor.stripColor(i.getItemMeta().getDisplayName()));
			if (t != null) {
				MenuManager.get().open(p, new TaskMenu(t));
				return;
			}
		}
	}

}
