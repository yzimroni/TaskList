package net.yzimroni.tasklist.menu.builder;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

public class MenuBuilder {
	
	public static ItemStack BR = new ItemStack(Material.BEACON);
	private final static int SLOTS_PER_ROW = 9;

	private Inventory inventory;

	// General settings
	private int entriesPerRow = -1;
	private int rowsPerPage = 3;
	private MenuRowAlign alignment = MenuRowAlign.CENTER;

	// Spacing
	private int rowStartSpace = -1;
	private int rowEndSpace = -1;
	private int startRow;

	// Build values
	private int currentSlot = 0;
	private int currentSlotRow = 0;
	private int itemsLeft = 0;
	private int[] currentRowAlign = null;

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
		currentSlot = ((startRow * SLOTS_PER_ROW) - 1) + rowStartSpace;
		currentSlotRow = 0;
		itemsLeft = items.size();
		items.forEach(i -> {
			if (i == BR) {
				currentSlot += (entriesPerRow - currentSlotRow);
				currentSlotRow = entriesPerRow;
			} else {
				inventory.setItem(getNextSlot(), i);
			}
			itemsLeft--;
		});
		return inventory;
	}

	private void checkValid() {
		if (entriesPerRow == -1) {
			entriesPerRow = (SLOTS_PER_ROW - rowStartSpace) - rowEndSpace;
		} else {
			if (entriesPerRow == SLOTS_PER_ROW) {
				rowStartSpace = 0;
				rowEndSpace = 0;
			} else {
				int[] center = MenuRowAlign.CENTER.calculateAlign(entriesPerRow, SLOTS_PER_ROW);
				rowStartSpace = center[0];
				rowEndSpace = center[1];
			}
		}
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
			if (itemsLeft < entriesPerRow) {
				//currentRowAlign = alignment.calculateAlign(itemsLeft, total)
			}
			return currentSlot += (rowEndSpace + rowStartSpace + 1);
		}
		currentSlotRow++;
		return ++currentSlot;
	}
	
	public int getInventoryRowNumber() {
		return inventory.getSize() / SLOTS_PER_ROW;
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
		Preconditions.checkArgument(entriesPerRow <= SLOTS_PER_ROW, "entriesPerRow must be 9 or lower");
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
	
	public void setRowSpace(int start, int end) {
		setRowStartSpace(start);
		setRowEndSpace(end);
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		Preconditions.checkArgument(startRow < getInventoryRowNumber(),
				"startRow is too big, the inventory is not big enough");
		this.startRow = startRow;
	}

	public MenuRowAlign getAlignment() {
		return alignment;
	}

	public void setAlignment(MenuRowAlign alignment) {
		this.alignment = alignment;
	}

}
