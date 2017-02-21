package com.xzzpig.piginstance;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.xzzpig.pigapi.json.JSONObject;

public class Vars {
	public static File datafile;
	public static JSONObject data;
	public static FileConfiguration config;
	public static int selectItem = 280;
	public static Map<String,Location> selectedMap = new HashMap<>();
}
