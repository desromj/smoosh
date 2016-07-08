package com.greenbatgames.smoosh.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.greenbatgames.smoosh.SmooshGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 1600;
		config.height = config.width * 9 / 16;

		new LwjglApplication(new SmooshGame(), config);
	}
}
