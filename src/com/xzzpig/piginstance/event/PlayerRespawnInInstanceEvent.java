package com.xzzpig.piginstance.event;

import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.xzzpig.pigapi.event.Event;
import com.xzzpig.piginstance.Instance;

public class PlayerRespawnInInstanceEvent extends Event {
	private final PlayerRespawnEvent e;
	private final Instance ins;

	public  PlayerRespawnInInstanceEvent(PlayerRespawnEvent e, Instance i) {
		this.e = e;
		this.ins = i;
	}

	public PlayerRespawnEvent getPlayerRespawnEvent() {
		return e;
	}

	public Instance getInstance() {
		return ins;
	}
}
