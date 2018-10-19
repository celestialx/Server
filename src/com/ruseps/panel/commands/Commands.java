package com.ruseps.panel.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
 
import org.json.JSONObject;
 
import com.ruseps.GameServer;
import com.ruseps.panel.configuration.Configuration;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.world.World;
//import com.ruseps.world.content.FountainOfLuck;
import com.ruseps.world.content.PlayerPunishment;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.grandexchange.GrandExchangeOffers;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.entity.impl.player.PlayerSaving;
 
/*
 * @author Luka Furlan
 */
public class Commands {
   
    public static Map<String, String> trivia = new HashMap<>();
 
    public static String getPlayerCount(String idk) {
        String playerCount = World.getPlayers().size() + "";
        /**
         * Added this for github ;) 
         * **/
        return playerCount;
    }
   
    public static String getPlayers(String idk) {
        Map<String, String> playerList = new HashMap<>();
        for (Player players : World.getPlayers()) {
            if (players != null) {
                playerList.put(players.getUsername(), players.getRights().toString());
            }
        }
        JSONObject playerData = new JSONObject(playerList);
        return playerData.toString();
    }
   
    public static String isOnline(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        Map<String, String> playerList = new HashMap<>();
        for (Player players : World.getPlayers()) {
            if (players != null) {
                playerList.put(players.getUsername(), players.getRights().toString());
            }
        }
        if(playerList.get(params.get("playerName")) != null) {
            return "true";
        } else {
            return "false";
        }
    }
   
    public static void saveAll(String idk) {
        World.savePlayers();
        return;
    }
   
    public static void sendGlobalMessage(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        World.sendMessage("<img=10><col=2E64FE> "+ params.get("message").replace("-", " "));
    }
   
    public static void teleport(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        Player player = World.getPlayerByName(params.get("playerName"));
        int x = Integer.parseInt(params.get("x"));
        int y = Integer.parseInt(params.get("y"));
        player.moveTo(new Position(x, y, 0));
        player.getPacketSender().sendMessage("<img=10><col=2E64FE> You have been teleported by Server");
    }
   
    public static void updateServer(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        if(Integer.parseInt(params.get("ticks")) > 0) {
            GameServer.setUpdating(true);
            for (Player players : World.getPlayers()) {
                if (players == null)
                    continue;
                players.getPacketSender().sendSystemUpdate(Integer.parseInt(params.get("ticks")));
            }
            TaskManager.submit(new Task(Integer.parseInt(params.get("ticks"))) {
                @Override
                protected void execute() {
                    for (Player player : World.getPlayers()) {
                        if (player != null) {
                            World.deregister(player);
                        }
                    }
                    //FountainOfLuck.save();
                    GrandExchangeOffers.save();
                    ClanChatManager.save();
                    GameServer.getLogger().info("Update task finished!");
                    System.exit(0);
                    stop();
                }
            });
        }
    }
   
    public static String giveItem(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        Player player = World.getPlayerByName(params.get("playerName"));
        if(player.getInventory().getFreeSlots() >= Integer.parseInt(params.get("quantity"))) {
            return "false";
        } else {
            player.getInventory().add(Integer.parseInt(params.get("item")), Integer.parseInt(params.get("quantity")));
            return "true";
        }
    }
   
    public static void promote(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        Player player = World.getPlayerByName(params.get("playerName"));
        switch(params.get("promote")) {
        case "serverSupport":
            player.setRights(PlayerRights.SUPPORT);
            World.deregister(player);
            break;
        case "moderator":
            player.setRights(PlayerRights.MODERATOR);
            World.deregister(player);
            break;
        case "administrator":
            player.setRights(PlayerRights.ADMINISTRATOR);
            World.deregister(player);
            break;
        case "demote":
            player.setRights(PlayerRights.PLAYER);
            World.deregister(player);
            break;
        }
    }
   
   
    public static String getPlayer(String parameters) throws FileNotFoundException {
        Map<String, String> params = Configuration.queryToMap(parameters);
        Scanner scanner = new Scanner( new File("./data/saves/characters/" + params.get("playerName") + ".json") );
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        return text;
    }
   
    public static void ban(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        String playerToBan = params.get("playerName");
        if(!PlayerSaving.playerExists(playerToBan)) {
            return;
        } else {
            if(PlayerPunishment.banned(playerToBan)) {
                return;
            }
            PlayerPunishment.ban(playerToBan);
            Player toBan = World.getPlayerByName(playerToBan);
            if(toBan != null) {
                World.deregister(toBan);
            }
        }
        return;
    }
   
    public static void kick(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        String playerToKick = params.get("playerName");
        Player toKick = World.getPlayerByName(playerToKick);
        if(toKick == null) {
            return;
        } else if(toKick.getLocation() != Location.WILDERNESS) {
            World.deregister(toKick);
        }
        return;
    }
   
    public static void mute(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        String playerToMute = params.get("playerName");
        Player toMute = World.getPlayerByName(playerToMute);
        if(!PlayerSaving.playerExists(playerToMute)) {
            return;
        } else {
            if(PlayerPunishment.muted(playerToMute)) {
                return;
            }
            PlayerPunishment.mute(playerToMute);
            toMute.getPacketSender().sendMessage("<img=10><col=2E64FE> You have been muted by Server");
        }
        return;
    }
   
    public static void addTriviaQuestion(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        trivia.put(params.get("question").replace("-", " "), params.get("anwser").replace("-", " "));
        World.sendMessage("<img=10><col=2E64FE>[TRIVIA] "+params.get("question").replace("-", " "));
    }
   
    public static void removeTriviaQuestion(String parameters) {
        Map<String, String> params = Configuration.queryToMap(parameters);
        trivia.remove(params.get("question").replace("-", " "));
        World.sendMessage("<img=10><col=2E64FE>[TRIVIA] Question: "+params.get("question").replace("-", " ")+ " is no longer active");
    }
   
    public static String getTriviaQuestions(String parameters) {
        JSONObject triviaObject = new JSONObject(trivia);
        return triviaObject.toString();
    }
 
}