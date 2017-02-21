package com.xzzpig.piginstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigapi.bukkit.TConfig;
import com.github.xzzpig.pigapi.event.Event;
import com.github.xzzpig.pigapi.json.JSONArray;
import com.github.xzzpig.pigapi.json.JSONObject;
import com.github.xzzpig.pigapi.json.JSONTokener;
import com.xzzpig.piginstance.listener.PigInstanceListener_bukkit;
import com.xzzpig.piginstance.listener.PigInstanceListener_pigapi;

public class Main extends JavaPlugin {
	public static Main instance;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return Help._PigInstance.runCommand(Help._PigInstance.new CommandInstance(sender, command, label, args));
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		saveData();
		getLogger().info(getName() + "插件已被停用 ");
		Event.unregListener(PigInstanceListener_pigapi.self);
	}

	public void saveData() {
		saveInstances();
		FileWriter fw = null;
		try {
			fw = new FileWriter(Vars.datafile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Vars.data.write(fw);
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveInstances() {
		JSONArray inss = new JSONArray();
		Instance.instances.forEach(ins -> {
			ins.toString();
			inss.put(ins.data);
		});
		Vars.data.put("instances", inss);
	}

	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		instance = this;
		this.getServer().getPluginManager().registerEvents(PigInstanceListener_bukkit.self, this);
		Event.registListener(PigInstanceListener_pigapi.self);
		saveDefaultConfig();
		loadConfig();
		loadData();
		loadInstances();
	}

	public void loadConfig() {
		Vars.config = TConfig.getConfigFile(getName(), "config.yml");
		Vars.selectItem = Vars.config.getInt("piginstance.selectitem", 280);
	}

	public void loadData() {
		Vars.datafile = new File(getDataFolder(), "data.json");
		if (!Vars.datafile.exists())
			try {
				Vars.datafile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		FileReader fr = null;
		try {
			fr = new FileReader(Vars.datafile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JSONTokener jt = new JSONTokener(fr);
		try {
			Vars.data = new JSONObject(jt);
		} catch (Exception e) {
			Vars.data = new JSONObject();
		}
		try {
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadInstances() {
		Instance.instances = new ArrayList<>();
		try {
			for (Object o : Vars.data.getJSONArray("instances")) {
				Instance i = new Instance((JSONObject) o);
				Instance.instances.add(i);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Help._PigInstance.getTabComplete(getName(), sender, command, alias, args);
	}
}
