package net.yzimroni.tasklist.menu;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

public class MenuBuilder {

	private Inventory inventory;

	// General settings
	private int entriesPerRow = 7;
	private int rowsPerPage = 3;

	// Spacing
	private int rowStartSpace;
	private int rowEndSpace;
	private int startRow;

	// Build values
	private int currentSlot = 0;
	private int currentSlotRow = 0;

	public MenuBuilder(Inventory inventory) {
		super();
		this.inventory = inventory;
		rowsPerPage = getInventoryRowNumber();
	}

	public MenuBuilder(String name, int size) {
		this(Bukkit.createInventory(null, size, name));
	}

	public Inventory create(List<ItemStack> items) {
		checkValid();
		currentSlot = ((startRow * 9) - 1) + rowStartSpace;
		currentSlotRow = 0;
		items.forEach(i -> {
			inventory.setItem(getNextSlot(), i);
		});
		return inventory;
	}

	private void checkValid() {
		Preconditions.checkArgument(startRow + rowsPerPage <= getInventoryRowNumber(),
				"startRow + rowsPerPage must be " + getInventoryRowNumber() + " or lower");
		Preconditions.checkArgument(rowStartSpace + rowEndSpace <= 8,
				"rowStartSpace + rowEndSpace must be 8 or below in total");
		Preconditions.checkArgument(startRow + rowsPerPage <= getInventoryRowNumber(),
				"startRow + rowsPerPage must be " + getInventoryRowNumber() + " or lower");
	}

	private int getNextSlot() {
		if (currentSlotRow == entriesPerRow) {
			currentSlotRow = 1;
			return currentSlot += (rowEndSpace + rowStartSpace + 1);
		}
		currentSlotRow++;
		return ++currentSlot;
	}

	public int getInventoryRowNumber() {
		return inventory.getSize() / 9;
	}

	public int getEntriesPerPage() {
		return entriesPerRow * rowsPerPage;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getEntriesPerRow() {
		return entriesPerRow;
	}

	public void setEntriesPerRow(int entriesPerRow) {
		Preconditions.checkArgument(entriesPerRow <= 9, "entriesPerRow must be 9 or lower");
		this.entriesPerRow = entriesPerRow;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		Preconditions.checkArgument(rowsPerPage <= getInventoryRowNumber(),
				"Too many rows per page, inventory is not big enough");
		this.rowsPerPage = rowsPerPage;
	}

	public int getRowStartSpace() {
		return rowStartSpace;
	}

	public void setRowStartSpace(int rowStartSpace) {
		Preconditions.checkArgument(rowStartSpace <= 8, "RowStartSpace must be 8 or below");
		this.rowStartSpace = rowStartSpace;
	}

	public int getRowEndSpace() {
		return rowEndSpace;
	}

	public void setRowEndSpace(int rowEndSpace) {
		Preconditions.checkArgument(rowEndSpace <= 8, "rowEndSpace must be 8 or below");
		this.rowEndSpace = rowEndSpace;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		Preconditions.checkArgument(startRow < getInventoryRowNumber(),
				"startRow is too big, the inventory is not big enough");
		this.startRow = startRow;
	}

}
