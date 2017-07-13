package net.yzimroni.tasklist;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.yzimroni.tasklist.menu.MenuManager;
import net.yzimroni.tasklist.menu.menus.TaskListMenu;
import net.yzimroni.tasklist.task.TaskManager;

public class TaskListPlugin extends JavaPlugin {
	
	private TaskManager manager;
	private MenuManager menuManager;
	
	private static TaskListPlugin instance;
	
	@Override
	public void onEnable() {
		
		manager = new TaskManager();
		menuManager = new MenuManager();
		Bukkit.getPluginManager().registerEvents(menuManager, this);
		
		instance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static TaskListPlugin get() {
		return instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			MenuManager.get().open((Player) sender, new TaskListMenu(1));
		}
		return false;
	}

	public TaskManager getManager() {
		return manager;
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}
	
	

}
