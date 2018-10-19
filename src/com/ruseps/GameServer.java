package com.ruseps;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ruseps.panel.server.Server;
import com.ruseps.util.RSAKeyGenerator;
import com.ruseps.util.ShutdownHook;
import com.ruseps.world.content.Cows;

/**
 * The starting point of Ruse.
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("CelestialX");
	private static boolean updating;

	public static void main(String[] params) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {

			logger.info("Initializing the loader...");
			loader.init();
			loader.finish();
			logger.info("The loader has finished loading utility tasks.");
			logger.info("CelestialX is now online on port "+GameSettings.GAME_PORT+"!");
			Server.start();
			logger.info("Hello from Diogo!");


		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not start CelestialX! Program terminated.", ex);
			System.exit(1);
		}
	}

	public static GameLoader getLoader() {
		return loader;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		GameServer.updating = updating;
	}

	public static boolean isUpdating() {
		return GameServer.updating;
	}
}