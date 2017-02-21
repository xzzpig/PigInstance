package com.xzzpig.piginstance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp.CommandInstance;
import com.github.xzzpig.pigapi.bukkit.TMessage;

public class CommandRunners {
	private static HashMap<String, Runnable> confirmmap = new HashMap<>();

	public static boolean pi_ins_create(CommandInstance ins) {
		String name = null;
		try {
			name = ins.args[2];
		} catch (Exception e) {
			ins.sendMsg("&4副本名称name不可为空");
			return true;
		}
		final String name_f = name;
		if (Instance.instances.stream().anyMatch(inst -> inst.name.equals(name_f))) {
			ins.sendMsg("&4名称为&2" + name + "&4的副本已存在");
			return true;
		}
		Instance i = new Instance();
		i.name = name;
		Instance.instances.add(i);
		Main.instance.saveInstances();
		ins.sendMsg("副本区域&3" + name + "&r创建成功");
		Main.instance.getLogger().info(ins.sender.getName() + "创建了副本区域" + name);
		return true;
	};

	public static boolean pi_ins_delete(CommandInstance ins) {
		String name = null;
		try {
			name = ins.args[2];
		} catch (Exception e) {
			ins.sendMsg("&4副本名称name不可为空");
			return true;
		}
		Instance i = Instance.getByName(name);
		if (i == null) {
			ins.sendMsg("&4该副本不存在");
			return true;
		}
		final String name_f = name;
		addConirm(ins, () -> {
			Instance.instances.remove(i);
			Main.instance.saveInstances();
			ins.sendMsg("副本区域&3" + name_f + "&r删除成功");
			Main.instance.getLogger().info(ins.sender.getName() + "删除了副本区域" + name_f);
		});
		return true;
	};

	public static boolean pi_ins_list(CommandInstance ins) {
		ins.sendMsg("所在的副本区域列表:");
		Instance.instances.stream().filter(i -> i.contains(((Player) ins.sender).getLocation()))
				.forEach(i -> ins.sender.sendMessage(i.name));
		return true;
	};

	public static boolean pi_ins_listall(CommandInstance ins) {
		ins.sendMsg("所有副本区域列表:");
		Instance.instances.forEach(i -> {
			ins.sender.sendMessage(i.name);
		});
		return true;
	};

	public static boolean pi_ins_info(CommandInstance ins) {
		String name = null;
		try {
			name = ins.args[2];
		} catch (Exception e) {
			ins.sendMsg("&4副本名称name不可为空");
			return true;
		}
		Instance i = Instance.getByName(name);
		if (i == null) {
			ins.sendMsg("&4该副本不存在");
			return true;
		}
		ins.sendMsg("副本区域&3" + name + "&r的信息:&2" + Instance.getByName(name).toString());
		return true;
	};

	public static boolean pi_ins_image(CommandInstance ins) {
		String name = null;
		try {
			name = ins.args[2];
		} catch (Exception e) {
			ins.sendMsg("&4副本名称name不可为空");
			return true;
		}
		Instance i = Instance.getByName(name);
		if (i == null) {
			ins.sendMsg("&4该副本不存在");
			return true;
		}
		File f = new File(Main.instance.getDataFolder(), name + ".gif");
		f.delete();
		try {
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			ImageIO.write(i.getImage(), "gif", fo);
		} catch (IOException e) {
			e.printStackTrace();
			ins.sendMsg(name + ".gif&4图片写出失败");
			return true;
		}
		ins.sendMsg(name + ".gif&2图片写出成功,文件位于&3" + f.getAbsolutePath());
		return true;
	};

	public static boolean pi_ins_add(CommandInstance ins) {
		String name = null;
		try {
			name = ins.args[2];
		} catch (Exception e) {
			ins.sendMsg("&4副本名称name不可为空");
			return true;
		}
		Instance inst = Instance.getByName(name);
		if (inst == null) {
			ins.sendMsg("&4该副本不存在");
			return true;
		}
		String name_p = ins.sender.getName();
		if (!Vars.selectedMap.containsKey(name_p + "_0")) {
			ins.sendMsg("&2位置0&4还未选择");
		}
		if (!Vars.selectedMap.containsKey(name_p + "_1")) {
			ins.sendMsg("&2位置1&4还未选择");
		}
		Location[] locs = new Location[2];
		locs[0] = Vars.selectedMap.get(name_p + "_0");
		locs[1] = Vars.selectedMap.get(name_p + "_1");
		Area area = new Area(locs[0], locs[1]);
		ins.sendMsg("&2是否将选定区域加入到副本区域" + name);
		final String name_f = name;
		addConirm(ins, () -> {
			inst.addArea(area);
			ins.sendMsg("选定区域已加入副本区域" + name_f);
			Main.instance.saveInstances();
		});
		return true;
	};

	public static boolean pi_ins_except(CommandInstance ins) {
		String name = null;
		try {
			name = ins.args[2];
		} catch (Exception e) {
			ins.sendMsg("&4副本名称name不可为空");
			return true;
		}
		Instance inst = Instance.getByName(name);
		if (inst == null) {
			ins.sendMsg("&4该副本不存在");
			return true;
		}
		String name_p = ins.sender.getName();
		if (!Vars.selectedMap.containsKey(name_p + "_0")) {
			ins.sendMsg("&2位置0&4还未选择");
		}
		if (!Vars.selectedMap.containsKey(name_p + "_1")) {
			ins.sendMsg("&2位置1&4还未选择");
		}
		Location[] locs = new Location[2];
		locs[0] = Vars.selectedMap.get(name_p + "_0");
		locs[1] = Vars.selectedMap.get(name_p + "_1");
		Area area = new Area(locs[0], locs[1]);
		ins.sendMsg("&2是否将选定区域从副本区域" + name + "排除");
		final String name_f = name;
		addConirm(ins, () -> {
			inst.exceptArea(area);
			ins.sendMsg("选定区域已从副本区域" + name_f + "排除");
			Main.instance.saveInstances();
		});
		return true;
	};

	public static boolean pi_confirm(CommandInstance ins) {
		if (!confirmmap.containsKey(ins.sender instanceof Player ? ins.sender.getName() : "#CONSOLE#")) {
			ins.sendMsg("&4你没有任何命令需要确认");
			return true;
		}
		confirmmap.get(ins.sender instanceof Player ? ins.sender.getName() : "#CONSOLE#").run();
		confirmmap.remove(ins.sender instanceof Player ? ins.sender.getName() : "#CONSOLE#");
		return true;
	};

	public static boolean pi_cancel(CommandInstance ins) {
		if (!confirmmap.containsKey(ins.sender instanceof Player ? ins.sender.getName() : "#CONSOLE#")) {
			ins.sendMsg("&4你没有任何命令需要取消");
			return true;
		}
		ins.sendMsg("&3你已取消执行该命令" + ins.toString());
		confirmmap.remove(ins.sender instanceof Player ? ins.sender.getName() : "#CONSOLE#");
		return true;
	};

	public static void addConirm(CommandInstance ci, Runnable r) {
		new TMessage("[PigInstance]").color(ChatColor.GOLD).then("请输入").then("/pi confirm").color(ChatColor.GREEN)
				.style(ChatColor.UNDERLINE).tooltip("点击快捷输入").suggest("/pi confirm").then("确认执行或").then("/pi cancel")
				.color(ChatColor.RED).style(ChatColor.UNDERLINE).tooltip("点击快捷输入").suggest("/pi cancel")
				.then("取消执行(5秒后自动取消)").send(ci.sender);
		confirmmap.put(ci.sender instanceof Player ? ci.sender.getName() : "#CONSOLE#", r);
		new Thread(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (confirmmap.containsKey(ci.sender.getName())) {
				confirmmap.remove(ci.sender.getName());
				ci.sendMsg("&2命令已自动取消");
			}
		}).start();
	}
}
