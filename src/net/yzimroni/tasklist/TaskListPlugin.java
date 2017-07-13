package net.yzimroni.tasklist;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.yzimroni.tasklist.menu.TaskListMenuManager;
import net.yzimroni.tasklist.task.TaskManager;

public class TaskListPlugin extends JavaPlugin {
	
	private TaskManager manager;
	private TaskListMenuManager menuManager;
	
	private static TaskListPlugin instance;
	
	@Override
	public void onEnable() {
		
		manager = new TaskManager();
		menuManager = new TaskListMenuManager();
		Bukkit.getPluginManager().registerEvents(menuManager, this);
		
		instance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static TaskListPlugin get() {
		return instance;
	}

}
