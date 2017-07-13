package net.yzimroni.tasklist.menu.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.yzimroni.tasklist.TaskListPlugin;
import net.yzimroni.tasklist.menu.Menu;
import net.yzimroni.tasklist.task.Task;

public class TaskListMenu extends Menu {

	private static final int TASKS_PER_ROW = 7;
	private static final int ROWS_PER_PAGE = 3;
	private static final int TASKS_PER_PAGE = TASKS_PER_ROW * ROWS_PER_PAGE;

	private static final int START_SPACING = 1;
	private static final int END_SPACING = 1;

	private int page;

	private int currentSlot = 9;
	private int currentSlotRow = 0;

	public TaskListMenu(int page) {
		super();
		this.page = page;
	}

	@Override
	public Inventory createInventory() {
		Inventory i = Bukkit.createInventory(null, 6 * 9, "Task List");
		List<Task> tasks = getTasksForPage();
		tasks.forEach(t -> {
			i.setItem(getNextTaskSlot(), t.getItemStack());
		});
		if (hasPreviousPage()) {
			//47
			i.setItem(48, new ItemStack(Material.GOLD_INGOT));
		}
		if (hasNextPage()) {
			//49
			i.setItem(50, new ItemStack(Material.DIAMOND));
		}
		return i;
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

}
