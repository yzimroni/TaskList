package net.yzimroni.tasklist.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import net.yzimroni.tasklist.TaskListPlugin;
import net.yzimroni.tasklist.sql.SQLUtils;
import net.yzimroni.tasklist.utils.Utils;

public class TaskManager implements Listener {

	private List<Task> tasks = new ArrayList<Task>();
	private List<Task> sortedList = null;

	public TaskManager() {
		/*
		 * Random r = new Random(); for (int i = 0; i < 100 + (r.nextInt(100) - 50) ;
		 * i++) {
		 * 
		 * addTask(new Task(-1, "Task" + i, r.nextInt(100) + 1,
		 * UUID.fromString("341899b6-b28f-47a3-b85e-3aa3b491d0d3"),
		 * System.currentTimeMillis() - (i * (i + 10)), r.nextBoolean() ? 0 :
		 * System.currentTimeMillis() - i)); }
		 */
		// tasks.add(new Task(1, "Test", 10,
		// UUID.fromString("341899b6-b28f-47a3-b85e-3aa3b491d0d3"), 1499962787, 0));
		// tasks.add(new Task(2, "Test1", 10,
		// UUID.fromString("341899b6-b28f-47a3-b85e-3aa3b491d0d3"), 1499962787,
		// 1499963787));
		sortTasks();
	}

	public void loadTasks() {
		tasks = SQLUtils.get().loadTasks();
		sortTasks();
	}

	public void sortTasks() {
		sortedList = tasks.stream().sorted((t1, t2) -> {
			if (t1.isCompleted() != t2.isCompleted()) {
				if (t1.isCompleted()) {
					return 1;
				} else {
					return -1;
				}
			}
			return (int) (t2.getCreated() - t1.getCreated());
		}).collect(Collectors.toList());
	}

	public void saveTasks() {
		tasks.forEach(SQLUtils.get()::saveTask);
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public List<Task> getTasks(boolean completed) {
		return tasks.stream().filter(t -> t.isCompleted() == completed).collect(Collectors.toList());
	}

	public Task getTaskByName(String name) {
		for (Task t : tasks) {
			if (t.getName().equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}

	public void addTask(Task t) {
		if (t.getId() == -1) {
			SQLUtils.get().saveTask(t);
		}
		tasks.add(t);
		sortTasks();
	}

	public void removeTask(Task t) {
		SQLUtils.get().deleteTask(t);
		tasks.remove(t);
		sortTasks();
	}

	public List<Task> getSortedList() {
		return sortedList;
	}

	public void addXp(Player p, int xp) {
		p.giveExpLevels(xp);

		new BukkitRunnable() {

			int times = 0;

			@Override
			public void run() {
				if (times++ >= 10) {
					cancel();
					return;
				}
				Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
				fw.setMetadata("fw_task_completed", new FixedMetadataValue(TaskListPlugin.get(), true));
				FireworkMeta fwm = fw.getFireworkMeta();
				Random r = new Random();
				Type type = Type.values()[r.nextInt(Type.values().length)];
				Color c1 = Utils.COLORS.get(r.nextInt(Utils.COLORS.size()));
				Color c2 = Utils.COLORS.get(r.nextInt(Utils.COLORS.size()));
				FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2)
						.with(type).trail(r.nextBoolean()).build();
				fwm.addEffect(effect);
				fwm.setPower(r.nextInt(2) + 1);
				fw.setFireworkMeta(fwm);
			}
		}.runTaskTimer(TaskListPlugin.get(), 0, 4);

	}

	@EventHandler
	public void onPlayerHitByFirework(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Firework) {
			if (e.getDamager().hasMetadata("fw_task_completed")) {
				e.setCancelled(true);
			}
		}
	}

}
