package com.xzzpig.piginstance.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.xzzpig.pigapi.bukkit.TMessage;
import com.github.xzzpig.pigapi.event.Event;
import com.xzzpig.piginstance.Instance;
import com.xzzpig.piginstance.Vars;
import com.xzzpig.piginstance.event.PlayerRespawnInInstanceEvent;

public class PigInstanceListener_bukkit implements Listener {
	public static final PigInstanceListener_bukkit self = new PigInstanceListener_bukkit();

	private PigInstanceListener_bukkit() {
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractBlock(PlayerInteractEvent e) {
		if (!e.hasBlock())
			return;
		if (!e.hasItem())
			return;
		if (e.getItem().getTypeId() != Vars.selectItem)
			return;
		int side = -1;
		if (e.getAction() == Action.LEFT_CLICK_BLOCK)
			side = 0;
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
			side = 1;
		if (side == -1)
			return;
		Location loc = e.getClickedBlock().getLocation();
		String key = e.getPlayer().getName() + "_" + side;
		Vars.selectedMap.put(key, loc);
		new TMessage("[PigInstance]").color(ChatColor.GOLD).then("位置" + side).color(ChatColor.AQUA).then("已选在:")
				.then("{" + loc.getWorld().getName() + ",(" + loc.getBlockX() + "," + loc.getBlockY() + ","
						+ loc.getBlockZ() + ")}")
				.color(ChatColor.GREEN).style(ChatColor.UNDERLINE).tooltip(loc.toString()).send(e.getPlayer());
		e.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		Instance i = Instance.getByLoc(p.getLocation());
		if (i == null)
			return;
		Event.callEvent(new PlayerRespawnInInstanceEvent(e, i));
	}
}
