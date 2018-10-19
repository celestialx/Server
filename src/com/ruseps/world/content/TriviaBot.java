package com.ruseps.world.content;

import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.model.PlayerRights;
import com.ruseps.util.Misc;

/*
 * @author Aj - CelestialX rsps
 */
public class TriviaBot {
	
	public static final int TIMER = 1800; //1800
	public static int botTimer = TIMER;
	
	public static int answerCount;
	public static String firstPlace;
	public static String secondPlace;
	public static String thirdPlace;

	public static void sequence() {
		
		if(botTimer > 0)
			botTimer--;
		if(botTimer <= 0) {
			botTimer = TIMER;
			didSend = false;
			askQuestion();
		}
	}
	
	public static void attemptAnswer(Player p, String attempt) {
		
		if (!currentQuestion.equals("") && attempt.replaceAll("_", " ").equalsIgnoreCase(currentAnswer)) {
			
			if (answerCount == 0) {
				answerCount++;
				if(p.getRights() == PlayerRights.RUBY_MEMBER) {
					p.getPointsHandler().incrementTriviaPoints(20);
				} else {
					p.getPointsHandler().incrementTriviaPoints(10);	
				}
				
				p.getPacketSender().sendMessage("You Recieved 10 trivia points for @red@1st Place.");
				p.getPointsHandler().refreshPanel();
				firstPlace = p.getUsername();
				return;
			}
			if (answerCount == 1) {
				if (p.getUsername() == firstPlace) {
					p.getPacketSender().sendMessage("Already answered");
					return;
				}
				answerCount++;
				if(p.getRights() == PlayerRights.RUBY_MEMBER) {
					p.getPointsHandler().incrementTriviaPoints(12);
				} else {
					p.getPointsHandler().incrementTriviaPoints(6);	
				}
				p.getPacketSender().sendMessage("You Recieved 6 trivia points for @red@2nd Place.");
				p.getPointsHandler().refreshPanel();
				secondPlace = p.getUsername();
				return;
			
			}
			if (answerCount == 2) {
				if (p.getUsername() == firstPlace || p.getUsername() == secondPlace) {
					p.getPacketSender().sendMessage("Already answered");
					return;
				}
				if(p.getRights() == PlayerRights.RUBY_MEMBER) {
					p.getPointsHandler().incrementTriviaPoints(8);
				} else {
					p.getPointsHandler().incrementTriviaPoints(4);	
				}
				p.getPacketSender().sendMessage("You Recieved 4 trivia points for @red@3rd Place.");
				p.getPointsHandler().refreshPanel();
				thirdPlace = p.getUsername();
				World.sendMessage("@blu@[TRIVIA] @bla@[1st:@blu@" +firstPlace+"@red@ (10 pts)@bla@] @bla@[2nd:@blu@" +secondPlace+"@red@ (6 pts)@bla@] [3rd:@blu@" +thirdPlace+"@red@  (4 pts)@bla@]");

				currentQuestion = "";
				didSend = false;
				botTimer = TIMER;
				answerCount = 0;
				return;
			}
			
			
		} else {
			if(attempt.contains("question") || attempt.contains("repeat")){
				p.getPacketSender().sendMessage("<col=800000>"+(currentQuestion));
				return;
			}
			p.getPacketSender().sendMessage("[TRIVIA]Sorry! Wrong answer! "+(currentQuestion));
			return;
		}
		
	}
	
	public static boolean acceptingQuestion() {
		return !currentQuestion.equals("");
	}
	
	private static void askQuestion() {
		for (int i = 0; i < TRIVIA_DATA.length; i++) {
			if (Misc.getRandom(TRIVIA_DATA.length - 1) == i) {
				if(!didSend) {
					didSend = true;
				currentQuestion = TRIVIA_DATA[i][0];
				currentAnswer = TRIVIA_DATA[i][1];
				World.sendMessage(currentQuestion);
				
				
				}
			}
		}
	}
	
	public static boolean didSend = false;
	
	private static final String[][] TRIVIA_DATA = {
			{"@blu@[TRIVIA]@red@ How many thieving stalls are there at the home area?", "5"},
			{"@blu@[TRIVIA]@red@ What is the name of the server?", "CelestialX"},
			{"@blu@[TRIVIA]@red@ What attack level do you need to wield an abyssal whip?", "70"},
			{"@blu@[TRIVIA]@red@ Where is the home area located?", "edgeville"},
			{"@blu@[TRIVIA]@red@ What mining level do you need to mine shooting stars?", "80"},
			{"@blu@[TRIVIA]@red@ What is the bandos boss called?", "General graardor"},
			{"@blu@[TRIVIA]@red@ What is the name of the clan chat everyone is in?", "help"},
			{"@blu@[TRIVIA]@red@ What boss drops dragon kiteshields?", "king black dragon"},
			{"@blu@[TRIVIA]@red@ What npc drops dark bows?", "phoenix"},
			{"@blu@[TRIVIA]@red@ What npc drops whips?", "abyssal demon"},
			{"@blu@[TRIVIA]@red@ What npc drops dark bows?", "dark beast"},
			{"@blu@[TRIVIA]@red@ What boss drops dragon claws?", "tormented demon"},
			{"@blu@[TRIVIA]@red@ What is the level requirement to wear skillcapes?", "99"},
			{"@blu@[TRIVIA]@red@ What is the maximum combat level in CelestialX?", "138"},
			{"@blu@[TRIVIA]@red@ What defence level is required to wear barrows?", "70"},
			{"@blu@[TRIVIA]@red@ Where can you get void armour in CelestialX?", "pest control"},
			{"@blu@[TRIVIA]@red@ Where are revenants found on CelestialX?", "ghost town"},
			{"@blu@[TRIVIA]@red@ What weapon hits with melee, but it's special attack hits with magic?", "korasi"},
			{"@blu@[TRIVIA]@red@ What is the most powerful crossbow in the game?", "armadyl crossbow"},
			{"@blu@[TRIVIA]@red@ Who is the owner of CelestialX?", "mob"},
			{"@blu@[TRIVIA]@red@ Where can you get dharoks armour?", "barrows"},
			{"@blu@[TRIVIA]@red@ What miniquest grants access to barrows gloves?", "recipe for disaster"},
			{"@blu@[TRIVIA]@red@ What combat level are tormented demons?", "450"},
			{"@blu@[TRIVIA]@red@ What combat level is nex?", "1001"},
			{"@blu@[TRIVIA]@red@ What combat level is corporeal beast", "785"},
			{"@blu@[TRIVIA]@red@ What combat level are rock crabs?", "13"},
			{"@blu@[TRIVIA]@red@ What combat level is kree'ara?", "580"},
			{"@blu@[TRIVIA]@red@ What boss drops dragon warhammer?", "lizardman shaman"},
			{"@blu@[TRIVIA]@red@ What is the best offensive range prayer in the normal prayer book?", "rigour"},
			{"@blu@[TRIVIA]@red@ What is the best offensive mage prayer in the normal prayer book?", "augury"},
			{"@blu@[TRIVIA]@red@ How many skills are there in CelestialX", "25"},
			{"@blu@[TRIVIA]@red@ What is the best offensive range prayer in the normal prayer book?", "Rigour"},
			{"@blu@[TRIVIA]@red@ What is your total level if you have 99 in every skill in CelestialX?", "2475"},
			{"@blu@[TRIVIA]@red@ What trees do you cut for magic logs?", "Magic"},
			{"@blu@[TRIVIA]@red@ What is the highest level rock to mine", "runite"},
			{"@blu@[TRIVIA]@red@ Where can you fight other players for their loot?", "wilderness"},
			{"@blu@[TRIVIA]@red@ What is the cape for complete players?", "completionist cape"},
			{"@blu@[TRIVIA]@red@ What is the cape for max players?", "max cape"},
			{"@blu@[TRIVIA]@red@ What skill makes potions?", "herblore"},
			{"@blu@[TRIVIA]@red@ What skill lets you make weapons and armour?", "smithing"},
			{"@blu@[TRIVIA]@red@ Where can you store money other than the bank", "money pouch"},
			{"@blu@[TRIVIA]@red@ Where do you store all of your items?", "bank"},
			{"@blu@[TRIVIA]@red@ What points do you get for killing bosses?", "boss points"},
			{"@blu@[TRIVIA]@red@ How many free slots does each bank tab have?", "352"},
			{"@blu@[TRIVIA]@red@ What skill advances your combat level past 126?", "summoning"},
			{"@blu@[TRIVIA]@red@ What food heals the most in CelestialX", "rocktail"},
			{"@blu@[TRIVIA]@red@ What should I do every day to help the server?", "vote"},
			{"@blu@[TRIVIA]@red@ What skill do I use when crafting runes", "runecrafting"},
			{"@blu@[TRIVIA]@red@ How many elite achievement tasks are there?", "8"},
			{"@blu@[TRIVIA]@red@ What boss drops toxic staff of the dead?", "Skotizo"},
			{"@blu@[TRIVIA]@red@ How many dungeoneering tokens is an arcane stream necklace?", "75000"},
			{"@blu@[TRIVIA]@red@ How many dungeoneering tokens are chaotics?", "200000"},
			{"@blu@[TRIVIA]@red@ What is the cube root of 216?", "6"},
			{"@blu@[TRIVIA]@red@ How many time can you vote a day?", "2"},
			{"@blu@[TRIVIA]@red@ How many auths can you get a day?", "10"},
			{"@blu@[TRIVIA]@red@ What boss drops the dragon kiteshield?", "King Black Dragon"},
			{"@blu@[TRIVIA]@red@ What slayer level does the master Sumona require?", "92"},
			{"@blu@[TRIVIA]@red@ What slayer level does the master Kuradel require?", "80"},
			{"@blu@[TRIVIA]@red@ What slayer level does the master Duradel require?", "50"},
			{"@blu@[TRIVIA]@red@ Who is the default slayer master?", "Vannaka"},
			{"@blu@[TRIVIA]@red@ What level herblore is required to make overloads?", "96"},
			{"@blu@[TRIVIA]@red@ What NPC will help you with your account security?", "Town Crier"},
			{"@blu@[TRIVIA]@red@ What summoning level is required to make a Talon Beast?", "77"},
			{"@blu@[TRIVIA]@red@ What summoning level is required to make a Ravenous Locust?", "70"},
			{"@blu@[TRIVIA]@red@ What summoning level is required to make an Iron Minotaur?", "46"},
			{"@blu@[TRIVIA]@red@ What summoning level is required to make a Spirit Larupia?", "57"},
			{"@blu@[TRIVIA]@red@ What summoning level is required to make an Moss Titan?", "79"},
			{"@blu@[TRIVIA]@red@ How much xp do you need to reach 99?", "13034431"},
			{"@blu@[TRIVIA]@red@ What is the maximum amount of cash you can hold in your inventory?", "2147483647"},
			{"@blu@[TRIVIA]@red@ At what level prayer can you use Hawk Eye?", "26"},
			{"@blu@[TRIVIA]@red@ How many Chickens are there at the Chicken Pen?", "8"},
			{"@blu@[TRIVIA]@red@ How many bales of Hay are there south east of Draynor Manor?", "27"},
			{"@blu@[TRIVIA]@red@ Which NPC will be able to give you a title?", "Sir Vyvin"},
			{"@blu@[TRIVIA]@red@ What is the 11th item in Explorer Jack's shop?", "falador teleport"},
			{"@blu@[TRIVIA]@red@ What is the 13th item in Explorer Jack's shop?", "ardougne teleport"},
			{"@blu@[TRIVIA]@red@ What is the 27th item in the Range shop?", "snakeskin bandana"},
			{"@blu@[TRIVIA]@red@ What is the 1st item in the PvP Pure shop?", "tzhaar-ket-om"},
			{"@blu@[TRIVIA]@red@ What was Herblore's name originally called?", "Herblaw"},
			{"@blu@[TRIVIA]@red@ How many coins does a monkfish alch for?", "3200"},
			{"@blu@[TRIVIA]@red@ What is the killcount requirement to enter a lair in Godwars?", "20"},
			{"@blu@[TRIVIA]@red@ In what month was CelestialX released?", "October"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "1"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "2"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "3"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "4"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "5"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "6"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "7"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "8"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "9"},
			{"@blu@[TRIVIA-GUESS]@red@ Guess a number 1-10?", "10"},
			
			{"@blu@[TRIVIA-GUESS]@red@ What smithing level is required to smith a Steel Plateskirt?", "46"},
			{"@blu@[TRIVIA-GUESS]@red@ What monster gives the examine ‘A vicious thief’?", "bandit"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the second boss to kill in the Recipe for Disaster quest?", "karamel"},
			{"@blu@[TRIVIA-GUESS]@red@ How many points does a Void Knight Deflector cost?", "350"},
			{"@blu@[TRIVIA-GUESS]@red@ What construction level is required to create Hangman?", "59"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the 40th spell on the regular spell book?", "vulnerability"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the 20th item in the Tzhaar shop?", "toktz-mej-tal"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the 19th item in the Farming shop?", "potato seed"},
			{"@blu@[TRIVIA-GUESS]@red@ How many boss points does a Fighter Torso cost?", "100"},
			{"@blu@[TRIVIA-GUESS]@red@ How many boss points does a Zamorakian Spear cost?", "75"},
			{"@blu@[TRIVIA-GUESS]@red@ How many boss points does a Fighter Hat cost?", "200"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the 17th emote?", "cry"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the 24th emote?", "goblin salute"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Bankers are there in SeersÕ Village bank?", "6"},
			{"@blu@[TRIVIA-GUESS]@red@ How many items are there in the PKP Store?", "35"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Runes are on the Magic thieving stall?", "3"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the 35th Achievement?", "fish 25 rocktails"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Green Dragons are there at West Dragons?", "4"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Inventory Spaces does a Pack Yak have?", "30"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Inventory Spaces does a Spirit Terrorbird have?", "12"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Inventory Spaces does a War Tortoise have?", "18"},
			{"@blu@[TRIVIA-GUESS]@red@ What monster gives the examine ‘The tongue of evil’?", "bloodveld"},
			{"@blu@[TRIVIA-GUESS]@red@ What monster gives the examine ‘An evil magic user’?", "infernal mage"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Agility courses are there?", "3"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Easy Tasks are there?", "30"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Medium Tasks are there?", "31"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Hard Tasks are there?", "32"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Elite Tasks are there?", "8"},
			{"@blu@[TRIVIA-GUESS]@red@ Where is ::home2 located?", "varrock"},
			{"@blu@[TRIVIA-GUESS]@red@ How much money can the Well of Goodwill hold?", "100000000"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the 8th item in the Runecrafting Shop?", "chaos talisman"},
			{"@blu@[TRIVIA-GUESS]@red@ Name the NPC that is wearing Torva", "Max"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the name of the skilling pet you get from Farming?", "tangleroot"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the name of the skilling pet you get from Woodcutting?", "beaver"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the name of the skilling pet you get from Mining?", "rock golem"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the name of the skilling pet you get from Fishing?", "Heron"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the name of the skilling pet you get from Thieving?", "Rocky"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the name of the skilling pet you get from Agility?", "giant squirrel"},
			{"@blu@[TRIVIA-GUESS]@red@ What is the name of the skilling pet you get from Runecrafting?", "rift guardian"},
			{"@blu@[TRIVIA-GUESS]@red@ Name the NPC that is holding a Bell?", "town crier"},
			{"@blu@[TRIVIA-GUESS]@red@ Which NPC sells Skill Capes?", "wise old man"},
			{"@blu@[TRIVIA-GUESS]@red@ How many Crates are there in the Varrock general store?", "10"},
			{"@blu@[TRIVIA-GUESS]@red@ How much gold do you need to pay to get through the gate to Al Kharid?", "10"},
			{"@blu@[TRIVIA-GUESS]@red@ How many thieving stalls are there at Ardougne Market place?", "16"},
			{"@blu@[TRIVIA-GUESS]@red@ What object gives the option to Dump-weeds", "compost bin"},
			{"@blu@[TRIVIA-GUESS]@red@ Which skill shows an image of a Fist", "strength"},
			{"@blu@[TRIVIA-GUESS]@red@ Which skill shows an image of a Wolf?", "summoning"},
			{"@blu@[TRIVIA-GUESS]@red@ Which skill shows an image of a Ring?", "dungeoneering"},
			{"@blu@[TRIVIA-GUESS]@red@ Which skill shows an image of Paw?", "hunter"},
			{"@blu@[TRIVIA-TYPE]@red@ type the following ::anwser jdj49a39ru357cnf", "jdj49a39ru357cnf"},
			{"@blu@[TRIVIA-TYPE]@red@ type the following ::anwser qpal29djeifh58cjid", "qpal29djeifh58cjid"},
			{"@blu@[TRIVIA-TYPE]@red@ type the following ::anwser qd85d4r0md42u2mssd", "qd85d4r0md42u2mssd"},
			{"@blu@[TRIVIA-TYPE]@red@ type the following ::anwser loski4893dhncbv7539", "loski4893dhncbv7539"},
			{"@blu@[TRIVIA-TYPE]@red@ type the following ::anwser 9esmf03na9admieutapdz9", "9esmf03na9admieutapdz9"},
			{"@blu@[TRIVIA-TYPE]@red@ type the following ::anwser djs83adm39s88s84masl", "djs83adm39s88s84masl"},
			{"@blu@[TRIVIA-TYPE]@red@ type the following ::anwser alskpwru39020dmsa3aeamap", "alskpwru39020dmsa3aeamap"}
		};
	
	public static String currentQuestion;
	private static String currentAnswer;
}