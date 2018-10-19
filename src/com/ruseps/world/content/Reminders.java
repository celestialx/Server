package com.ruseps.world.content;

import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;

/*
 * @author Aj
 * www.Simplicity-ps.com
 */

public class Reminders {
	
	
    private static final int TIME = 1200000; //20 minutes
	private static Stopwatch timer = new Stopwatch().reset();
	private static String prefix = "<img=10><shad=0><col=FF0077> ";
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = { 
			{"@blu@[SERVER]"+prefix+" Remember to ::vote for rewards every 12 hours!"},
			{"@blu@[SERVER]"+prefix+" Use the command 'drops' for drop tables"},
			{"@blu@[SERVER]"+prefix+" Use the ::help command to ask staff for help"},
			{"@blu@[SERVER]"+prefix+" Make sure to read the forums for information www.CelestialX.org/forums"},
			{"@blu@[SERVER]"+prefix+" Remember to spread the word and invite your friends to play!"},
			{"@blu@[SERVER]"+prefix+" Use ::commands to find a list of commands"},
		//	{"@blu@[SERVER]"+prefix+" See where you stand on the Hiscores www.CelestialX.org/hiscores"},
			{"@blu@[SERVER]"+prefix+" Donate to help the server grow!"},
			{"@blu@[SERVER]"+prefix+" Toggle your client settings to your preference in the wrench tab!"},
			{"@blu@[SERVER]"+prefix+" Register and post on the forums to keep them active! ::Forums"},
			{"@blu@[SERVER]"+prefix+" Donators + can use ::title to set a custom loyalty title"},
			{"@blu@[SERVER]"+prefix+" Join any of the following clan chats: Help, Gamble, Market, Pvm"},

		
	};

	/*
	 * Sequence called in world.java
	 * Handles the main method
	 * Grabs random message and announces it
	 */
	public static void sequence() {
		if(timer.elapsed(TIME)) {
			timer.reset();
			{
				
			currentMessage = MESSAGE_DATA[Misc.getRandom(MESSAGE_DATA.length - 1)][0];
			World.sendMessage(currentMessage);
					
					
				}
				
			new Thread(() -> { World.savePlayers(); }).start();
			}
		

          }
  }
