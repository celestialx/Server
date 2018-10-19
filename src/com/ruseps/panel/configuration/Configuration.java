package com.ruseps.panel.configuration;


import java.util.HashMap;
import java.util.Map;

import com.ruseps.panel.commands.Commands;
import com.ruseps.panel.server.Server;
import com.ruseps.world.World;

public class Configuration {
  
   public static String playerCount = World.getPlayers().size() + "";
   public static final int triviaRewardID = 995;
   public static final int triviaRewardAmount = 1000000000;
   public static final String ipv6_adress = "162.144.185.195";

   public static void addRoutes() throws NoSuchMethodException {
       Server.serviceRead.put("check", Commands.class.getMethod("isOnline", String.class));
       Server.serviceRead.put("online", Commands.class.getMethod("getPlayerCount", String.class));
       Server.serviceRead.put("players", Commands.class.getMethod("getPlayers", String.class));
       Server.serviceRead.put("player", Commands.class.getMethod("getPlayer", String.class));
       Server.serviceRead.put("giveItem", Commands.class.getMethod("giveItem", String.class));
       Server.serviceRead.put("getTriviaQuestions", Commands.class.getMethod("getTriviaQuestions", String.class));
       Server.serviceMethod.put("save", Commands.class.getMethod("saveAll", String.class));
       Server.serviceMethod.put("ban", Commands.class.getMethod("ban", String.class));
       Server.serviceMethod.put("kick", Commands.class.getMethod("kick", String.class));
       Server.serviceMethod.put("mute", Commands.class.getMethod("mute", String.class));
       Server.serviceMethod.put("teleport", Commands.class.getMethod("teleport", String.class));
       Server.serviceMethod.put("promote", Commands.class.getMethod("promote", String.class));
       Server.serviceMethod.put("sendGlobalMessage", Commands.class.getMethod("sendGlobalMessage", String.class));
       Server.serviceMethod.put("updateServer", Commands.class.getMethod("updateServer", String.class));
       Server.serviceMethod.put("addTriviaQuestions", Commands.class.getMethod("addTriviaQuestion", String.class));
       Server.serviceMethod.put("removeTriviaQuestions", Commands.class.getMethod("removeTriviaQuestion", String.class));
   }
  
   public static Map<String, String> queryToMap(String query){
       Map<String, String> result = new HashMap<String, String>();
       for (String param : query.split("&")) {
           String pair[] = param.split("=");
           if (pair.length>1) {
               result.put(pair[0], pair[1]);
           }else{
               result.put(pair[0], "");
           }
       }
       return result;
   }

}