package com.xzzpig.piginstance.listener;

import com.github.xzzpig.pigapi.event.EventHandler;
import com.github.xzzpig.pigapi.event.Listener;
import com.xzzpig.piginstance.event.PlayerRespawnInInstanceEvent;

public class PigInstanceListener_pigapi implements Listener {
	public static final PigInstanceListener_pigapi self = new PigInstanceListener_pigapi();

	@EventHandler
	public void onPlayerRespawnInInstance(PlayerRespawnInInstanceEvent e) {
	}
}
