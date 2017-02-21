package com.xzzpig.piginstance;

import java.io.Serializable;

import org.bukkit.Location;

import com.github.xzzpig.pigapi.json.JSONObject;

public class Area implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3533320565571980979L;

	JSONObject data;

	public Area(Location loc) {
		this(loc, loc);
	}

	Area(JSONObject json) {
		this.data = json;
	}

	public Area(Location l1, Location l2) {
		data = new JSONObject();
		int x, y, z, a, b, c;
		data.put("world", l1.getWorld().getName());
		if (l1.getBlockX() < l2.getBlockX()) {
			x = l1.getBlockX();
			a = l2.getBlockX() - l1.getBlockX();
		} else {
			x = l2.getBlockX();
			a = l1.getBlockX() - l2.getBlockX();
		}
		if (l1.getBlockY() < l2.getBlockY()) {
			y = l1.getBlockY();
			b = l2.getBlockY() - l1.getBlockY();
		} else {
			y = l2.getBlockY();
			b = l1.getBlockY() - l2.getBlockY();
		}
		if (l1.getBlockZ() < l2.getBlockZ()) {
			z = l1.getBlockZ();
			c = l2.getBlockZ() - l1.getBlockZ();
		} else {
			z = l2.getBlockZ();
			c = l1.getBlockZ() - l2.getBlockZ();
		}
		data.put("x", x).put("y", y).put("z", z).put("a", a).put("b", b).put("c", c);
	}

	public boolean contains(Area area) {
		if (!area.data.getString("world").equalsIgnoreCase(this.data.getString("world")))
			return false;
		if (!this.contains(area.data.getInt("x"), area.data.getInt("y"), area.data.getInt("z")))
			return false;
		if (!this.contains(area.data.getInt("x") + area.data.getInt("a"), area.data.getInt("y") + area.data.getInt("b"),
				area.data.getInt("z") + area.data.getInt("c")))
			return false;
		return true;
	}

	public boolean contains(int x, int y, int z) {
		if (x < this.data.getInt("x") || y < this.data.getInt("y") || z < this.data.getInt("z"))
			return false;
		if (x > this.data.getInt("x") + this.data.getInt("a") || y > this.data.getInt("y") + this.data.getInt("b")
				|| z > this.data.getInt("z") + this.data.getInt("c"))
			return false;
		return true;
	}

	public boolean contains(Location loc) {
		if (!this.data.getString("world").equalsIgnoreCase(loc.getWorld().getName()))
			return false;
		return this.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	@Override
	public String toString() {
		return data.toString();
	}

	public int getX() {
		return data.getInt("x");
	}

	public int getY() {
		return data.getInt("y");
	}

	public int getZ() {
		return data.getInt("z");
	}

	public int getA() {
		return data.getInt("a");
	}

	public int getB() {
		return data.getInt("b");
	}

	public int getC() {
		return data.getInt("c");
	}

	public int getX2() {
		return getX() + getA();
	}

	public int getY2() {
		return getY() + getB();
	}

	public int getZ2() {
		return getZ() + getC();
	}

	public String getWorld() {
		return data.getString("world");
	}

}