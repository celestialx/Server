package com.ruseps.world.content.cluescroll;

import java.util.Objects;

import com.ruseps.world.content.treasuretrails.ClueScroll;
import com.ruseps.world.entity.impl.player.Player;

public final class Clue {

	/**
	 * A representation of the type of clue a scroll is.
	 * 
	 * @author Michael | Chex
	 *
	 */
	public static enum ClueType {

		/**
		 * A map clue.
		 */
		MAP,

		/**
		 * A cryptic clue.
		 */
		CRYPTIC,

		/**
		 * An emote clue.
		 */
		EMOTE,

		/**
		 * A coordinate clue.
		 */
		COORDINATE
	}

	/**
	 * The clue type for this clue.
	 */
	private final ClueType clueType;

	/**
	 * The data required to display the clue.
	 */
	private final Object[] data;

	/**
	 * Constructs a new Clue.
	 * 
	 * @param clueType
	 *            - The type of clue.
	 * @param data
	 *            - The data required to display the clue.
	 */
	public Clue(ClueType clueType, Object... data) {
		this.clueType = clueType;
		this.data = Objects.requireNonNull(data);
	}

	/**
	 * Displays the clue for a player.
	 * 
	 * @param player
	 *            - The player to display the clue for.
	 */
	public void display(Player player) {
		switch (clueType) {
		
		case MAP:
			//player.send(new SendInterface(Integer.parseInt(String.valueOf(data[0]))));
			player.getPacketSender()
			.sendInterface(Integer.parseInt(String.valueOf(data[0])));
			break;
			
		case COORDINATE:
		case CRYPTIC:
			//player.send(new SendRemoveInterfaces());
			//player.send(new SendInterface(6965));
			player.getPacketSender()
			.sendInterface(6965);
			
			for (int i = 6968; i <= 6975; i++) {
				//player.send(new SendString(String.valueOf(data[i - 6968]), i));
				player.getPacketSender().sendString(i, String.valueOf(data[i - 6968]));
			}
			break;
			
		case EMOTE:
		//	player.send(new SendRemoveInterfaces());
			//player.send(new SendInterface(6965));
			player.getPacketSender()
			.sendInterface(6965);
			
			for (int i = 6968; i <= 6975; i++) {
			//	player.send(new SendString(String.valueOf(data[1 + i - 6968]), i));
				player.getPacketSender().sendString(i, String.valueOf(data[1 + i - 6968]));

			}
			break;

		}
	}

	public ClueType getClueType() {
		return clueType;
	}

}
