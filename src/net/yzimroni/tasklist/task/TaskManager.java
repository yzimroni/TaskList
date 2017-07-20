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
		tasks.forEach(Task::save);
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
		float targetXp = getXPLevel(p) + xp;

		new BukkitRunnable() {

			int times = 0;

			@Override
			public void run() {
				if (!p.isOnline()) {
					cancel();
					return;
				}
				float diff = targetXp - getXPLevel(p);
				if (times++ >= 3000) {
					System.out.println("Xp animation task did not cancelled: " + p.getName() + ", targetXp: " + targetXp
							+ ", diff: " + diff);
					cancel();
					return;
				}
				if (diff > 0) {
					p.giveExp(Math.max(p.getExpToLevel() / 18, 1));
				} else if (diff < 1 && diff > 0) {
					p.giveExp((int) (diff * p.getExpToLevel()));
				} else {
					cancel();
				}
			}

		}.runTaskTimer(TaskListPlugin.get(), 0, 1);
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

	private float getXPLevel(Player p) {
		return p.getLevel() + p.getExp();
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
