package com.xzzpig.piginstance;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.github.xzzpig.pigapi.json.JSONArray;
import com.github.xzzpig.pigapi.json.JSONObject;

public class Instance implements Serializable {
	public static List<Instance> instances = new ArrayList<>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 2755550053532830709L;

	public JSONObject data;

	public static Instance getByName(String name) {
		for (Instance instance : instances) {
			if (instance.name.equals(name))
				return instance;
		}
		return null;
	}

	public static Instance getByLoc(Location loc) {
		for (Instance instance : instances) {
			if (instance.contains(loc))
				return instance;
		}
		return null;
	}

	private List<Area> areas = new ArrayList<>();

	private List<Area> ex_areas = new ArrayList<>();

	public String name;

	public Instance() {
		data = new JSONObject();
	}

	Instance(JSONObject json) {
		this.data = json;
		this.name = json.getString("name");
		for (Object area_ : json.getJSONArray("areas")) {
			areas.add(new Area((JSONObject) area_));
		}
		for (Object area_ : json.getJSONArray("ex_areas")) {
			ex_areas.add(new Area((JSONObject) area_));
		}
	}

	public Instance addArea(Area a) {
		areas.add(a);
		return this;
	}

	public boolean contains(Area a) {
		return countInArea(a) != 0;
	}

	public boolean contains(Location loc) {
		if (!contains(new Area(loc)))
			return false;
		if (ex_areas.stream().filter(a_ -> a_.contains(loc)).count() != 0)
			return false;
		return true;
	}

	public int countInArea(Area a) {
		return listInArea(a).length;
	}

	/**
	 * 添加排除的区域(仅对Location有效)
	 */
	public Instance exceptArea(Area a) {
		ex_areas.add(a);
		return this;
	}

	/**
	 * @param a
	 * @return 列出包含区域a的Area
	 */
	public Area[] listInArea(Area a) {
		return areas.stream().filter(a_ -> a_.contains(a)).toArray(Area[]::new);
	}

	public Instance setSpawn(Location loc) {
		data.put("spawn", new Area(loc, loc));
		return this;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		data.put("name", name);
		JSONArray areas = new JSONArray();
		this.areas.forEach(area_ -> areas.put(area_.data));
		data.put("areas", areas);
		JSONArray areas_ex = new JSONArray();
		this.ex_areas.forEach(area_ -> areas_ex.put(area_.data));
		data.put("ex_areas", areas_ex);
		return data.toString();
	}

	public BufferedImage getImage() {
		if (areas.size() + ex_areas.size() == 0)
			return new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);
		List<Area> arealist = new ArrayList<>(areas);
		arealist.addAll(ex_areas);
		Area first = arealist.get(0);
		int x1 = first.data.getInt("x");
		int y1 = first.data.getInt("y");
		int z1 = first.data.getInt("z");
		int x2 = first.getX2();
		int y2 = first.getY2();
		int z2 = first.getZ2();
		for (Area a : arealist) {
			x1 = (a.data.getInt("x") < x1) ? a.data.getInt("x") : x1;
			y1 = (a.data.getInt("y") < y1) ? a.data.getInt("y") : y1;
			z1 = (a.data.getInt("z") < z1) ? a.data.getInt("z") : z1;
			x2 = (a.getX2() > x2) ? a.getX2() : x2;
			y2 = (a.getY2() > y2) ? a.getY2() : y2;
			z2 = (a.getZ2() > z2) ? a.getZ2() : z2;
		}
		BufferedImage image = new BufferedImage(x2 - x1, z2 - z1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		// g.setColor(Color.RED);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		for (Area a : areas) {
			g.setColor(new Color(255, 255, 255, 255 / 2));
			g.fillRect(a.getX() - x1, a.getZ() - z1, a.getA(), a.getC());
		}
		for (Area a : ex_areas) {
			g.setColor(Color.BLACK);
			g.fillRect(a.getX() - x1, a.getZ() - z1, a.getA(), a.getC());
		}
		image.flush();
		return image;
	}
}
