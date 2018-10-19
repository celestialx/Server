
package com.ruseps.world.entity.impl.player;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.netty.channel.Channel;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.PlayerDeathTask;
import com.ruseps.engine.task.impl.WalkToTask;
import com.ruseps.model.Animation;
import com.ruseps.model.Appearance;
import com.ruseps.model.CharacterAnimations;
import com.ruseps.model.ChatMessage;
import com.ruseps.model.DwarfCannon;
import com.ruseps.model.Flag;
import com.ruseps.model.GameMode;
import com.ruseps.model.GameObject;
import com.ruseps.model.Item;
import com.ruseps.model.Locations;
import com.ruseps.model.MagicSpellbook;
import com.ruseps.model.PlayerInteractingOption;
import com.ruseps.model.PlayerRelations;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Prayerbook;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.model.container.impl.Bank.BankSearchAttributes;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.container.impl.Inventory;
import com.ruseps.model.container.impl.PriceChecker;
import com.ruseps.model.container.impl.Shop;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.model.definitions.WeaponInterfaces.WeaponInterface;
import com.ruseps.model.input.Input;
import com.ruseps.net.PlayerSession;
import com.ruseps.net.SessionState;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketSender;
import com.ruseps.util.FrameUpdater;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.content.Achievements.AchievementAttributes;
import com.ruseps.world.content.BankPin.BankPinAttributes;
import com.ruseps.world.content.BlowpipeLoading;
import com.ruseps.world.content.BonusManager;
import com.ruseps.world.content.DropLog.DropLogEntry;
import com.ruseps.world.content.KillsTracker.KillsEntry;
import com.ruseps.world.content.Kraken;
import com.ruseps.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.ruseps.world.content.PlayerDropLog;
import com.ruseps.world.content.PointsHandler;
import com.ruseps.world.content.ProfileViewing;
import com.ruseps.world.content.StartScreen.GameModes;
import com.ruseps.world.content.Trading;
import com.ruseps.world.content.achievement.AchievementHandler;
import com.ruseps.world.content.clan.ClanChat;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.ruseps.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.ruseps.world.content.combat.magic.CombatSpell;
import com.ruseps.world.content.combat.magic.CombatSpells;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.pvp.PlayerKillingAttributes;
import com.ruseps.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.ruseps.world.content.combat.strategy.CombatStrategies;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.combat.weapon.FightType;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dicing.Dicing;
import com.ruseps.world.content.grandexchange.GrandExchangeSlot;
import com.ruseps.world.content.minigames.Minigame;
import com.ruseps.world.content.minigames.MinigameAttributes;
import com.ruseps.world.content.minigames.impl.Dueling;
import com.ruseps.world.content.pos.PlayerOwnedShopManager;
import com.ruseps.world.content.skill.SkillManager;
import com.ruseps.world.content.skill.impl.construction.ConstructionData.HouseLocation;
import com.ruseps.world.content.skill.impl.construction.ConstructionData.HouseTheme;
import com.ruseps.world.content.skill.impl.construction.HouseFurniture;
import com.ruseps.world.content.skill.impl.construction.Portal;
import com.ruseps.world.content.skill.impl.construction.Room;
import com.ruseps.world.content.skill.impl.farming.Farming;
import com.ruseps.world.content.skill.impl.slayer.Slayer;
import com.ruseps.world.content.skill.impl.summoning.Pouch;
import com.ruseps.world.content.skill.impl.summoning.Summoning;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;

public class Player extends Character {
	
	/** LMS **/
	public int lmsCoffer = 0;
	
	public int getLmsCoffer() {
		return lmsCoffer;
	}

	public void setLmsCoffer(int lmsCoffer) {
		this.lmsCoffer = lmsCoffer;
	}

	public int[] oldSkillLevels1 = new int[25];
	public int[] oldSkillXP1 = new int[25];
	public int[] oldSkillMaxLevels1 = new int[25];
	
	/** Mbox **/
	
	public int winnerIndex = -1;
	public Object previousWinnings;
	
	private int[] cluesCompleted = new int[4];
	public int[] getCluesCompleted() {
		return cluesCompleted;
	}
	
	public void setCluesCompleted(int[] cluesCompleted) {
		this.cluesCompleted = cluesCompleted;
	}
	
	public void setCluesCompleted(int index, int value) {
		cluesCompleted[index] = value;
	}
	/**Achievements**/	
	public int achievementsCompleted;
	public int achievementPoints;
	public int[] easy = new int[150];
	public int[] medium = new int[150];
	public int[] hard = new int[150];
	public int[] elite = new int[150];
	public boolean loading = false;
	public int tab = -1;
	public int[] completed = new int[]{0,0,0,0};
	private AchievementHandler achievementHandler;
	
	public AchievementHandler getAchievements() {
		if (achievementHandler == null)
			achievementHandler = new AchievementHandler(this);
		return achievementHandler;
	}
	
	public int[] getCompleted() {
		return completed;
	}

	public int[] getEasy() {
		return easy;
	}

	public int[] getHard() {
		return hard;
	}

	public int[] getMedium() {
		return medium;
	}

	public int[] getElite() {
		return elite;
	}
	
	public void setCompleted(int[] completed) {
		this.completed = completed;
	}
	
	public void setEasy(int[] easy) {
		this.easy = easy;
	}

	public void setMedium(int[] medium) {
		this.medium = medium;
	}

	public void setHard(int[] hard) {
		this.hard = hard;
	}

	public void setElite(int[] elite) {
		this.elite = elite;
	}
	
	public boolean inFFA;
	public boolean inFFALobby;
	public boolean inBG;
	public boolean inBGLobby;
	public int[] oldSkillLevels = new int[25];
	public int[] oldSkillXP = new int[25];
	public int[] oldSkillMaxLevels = new int[25];
	
	private byte[] cachedUpdateBlock;
	
	private long dragonScimInjury;
	
	private boolean hidePlayer = true;
	
	private int[] maxCapeColors = { 65214, 65200, 65186, 62995 };
	
	private int[] compCapeColors = { 65214, 65200, 65186, 62995 };
	
	private int currentCape;
	
	private final Dicing dicing = new Dicing(this);
	
	public Dicing getDicing() {
		return dicing;
	}
	
	private final PlayerOwnedShopManager playerOwnedShopManager = new PlayerOwnedShopManager(this);
	
	private boolean active;
	
	private boolean shopUpdated;
	
	public PlayerOwnedShopManager getPlayerOwnedShopManager() {
		return playerOwnedShopManager;
	}
	public Map<Integer, String> interacts = new HashMap<>();
	
	public boolean hasInteract(int slot, String name) {
		return Objects.equals(interacts.get(slot), name);
	}
	
	public int[] getMaxCapeColors() {
		return maxCapeColors;
	}

	private int venomImmunity;
	
    public int getVenomImmunity() {
        return poisonImmunity;
    }
    
    public void incrementVenomImmunity(int amount) {
        venomImmunity += amount;
    }
    
    public void decrementVenomImmunity(int amount) {
        venomImmunity -= amount;
    }
	
	public long npcDropTableDelay = 0;

	public void setMaxCapeColors(int[] maxCapeColors) {
		this.maxCapeColors = maxCapeColors;
	}
	
	private String title = "";
	
	public String yellmsg = "";


    private Kraken kraken = new Kraken();
    
    /**
     * Grabs the Kraken boss instance
     * @return
     */
    public Kraken getKraken() {
        return this.kraken;
    }

    /**
     * Resets the Kraken instance
     */
    public void resetKraken() {
        this.getKraken().reset();
        this.kraken = new Kraken();
    }


    public Player(PlayerSession playerIO) {
        super(GameSettings.DEFAULT_POSITION.copy());
        this.session = playerIO;
    }
private Map<String, Object> attributes = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T) attributes.get(key);
	}
	
	private Minigame minigame = null;
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key, T fail) {
		Object object = attributes.get(key);
		return object == null ? fail : (T) object;
	}
	
	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}
	
	public void removeAttribute(String key) {
		attributes.remove(key);
	}
    private int hardwareNumber;
    public int getHardwareNumber() {
        return hardwareNumber;
    }

    public Player setHardwareNumber(int hardwareNumber) {
        this.hardwareNumber = hardwareNumber;
        return this;
    }
	
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

    @Override
    public void appendDeath() {
        if (!isDying) {
            isDying = true;
            TaskManager.submit(new PlayerDeathTask(this));
        }
    }
    

	private int bossPoints;
	private int jailKills;
	private int donationPoints;
	private int prestigePoints;
	private int triviaPoints;
	private int slayerPoints;
	private int commendations;
	private int dungTokens;
	private int pkPoints;
	private double loyaltyPoints;
	private int votingPoints;
	//private int achievementPoints;
    
    private int passwordPlayer = 0;
    
	private boolean jadWave;
	
	
	public boolean jadWave() {
		return jadWave;
	}

	public void setjadWave(boolean jadWave) {
		this.jadWave = jadWave;
	}

	private boolean healerWave;

	public boolean healerWave() {
		return healerWave;
	}

	public void sethealerWave(boolean healerWave) {
		this.healerWave = healerWave;
	}
    
    public int getPasswordPlayer() {
		return passwordPlayer;
	}
	
	public void setPasswordPlayer(int passwordPlayer) {
		this.passwordPlayer = passwordPlayer;
	}
	
	public int getBossPoints() {
		return bossPoints;
	}

	public void setBossPoints(int bossPoints) {
		this.bossPoints = bossPoints;
	}
	
	public String getWildywyrmLocation() {
		return getWildywyrmLocation();
	}		
	

	public int getDonationPoints() {
		return donationPoints;
	}

	public void setDonationPoints(int donationPoints) {
		this.donationPoints = donationPoints;
	}

	public int getPrestigePoints() {
		return prestigePoints;
	}

	public void setprestigePoints(int prestigePoints) {
		this.prestigePoints = prestigePoints;
	}

	public int getpkPoints() {
		return pkPoints;
	}

	public void setpkPoints(int pkPoints) {
		this.pkPoints = pkPoints;
	}

	public double getloyaltyPoints() {
		return loyaltyPoints;
	}

	public void setloyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public int getvotingPoints() {
		return votingPoints;
	}

	public void setvotingPoints(int votingPoints) {
		this.votingPoints = votingPoints;
	}

	public int getachievementPoints() {
		return achievementPoints;
	}

	public void setachievementPoints(int achievementPoints) {
		this.achievementPoints = achievementPoints;
	}
	
	 public void setJailAmount(int jailKills) {
	    	this.jailKills = jailKills;
	    }
	    public int getAmountJailed() {
	    	return jailKills;
	    }
	    
	    public void setTotalAmount(int jailKills) {
	    	this.totalJailKills = jailKills;
	    }
	    public int getTotalAmountJailed() {
	    	return totalJailKills;
	    }
	
	/*
	 * Variables for DropTable & Player Profiling
	 *@author Levi Patton
	 *@www.rune-server.org/members/auguryps
	 */
	public Player dropLogPlayer;
	public boolean dropLogOrder;
	private PlayerDropLog playerDropLog = new PlayerDropLog();
	private ProfileViewing profile = new ProfileViewing();
	/*
	 * Variables for the DropLog
	 * @author Levi Patton
	 */
	public PacketSender getPA() {
		return getPacketSender();
	}
	public PlayerDropLog getPlayerDropLog() {
		return playerDropLog;
	}
	
	public ProfileViewing getProfile() {
		return profile;
	}

	public void setProfile(ProfileViewing profile) {
		this.profile = profile;
	}

	public void setPlayerDropLog(PlayerDropLog playerDropLog) {
		this.playerDropLog = playerDropLog;
	}

    @Override
    public int getConstitution() {
        return getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
    }

    @Override
    public Character setConstitution(int constitution) {
        if (isDying) {
            return this;
        }
        skillManager.setCurrentLevel(Skill.CONSTITUTION, constitution);
        packetSender.sendSkill(Skill.CONSTITUTION);
        if (getConstitution() <= 0 && !isDying) {
            appendDeath();
        }
        return this;
    }

    @Override
    public void heal(int amount) {
        int level = skillManager.getMaxLevel(Skill.CONSTITUTION);
        if ((skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount) >= level) {
            setConstitution(level);
        } else {
            setConstitution(skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount);
        }
    }

    @Override
    public int getBaseAttack(CombatType type) {
        if (type == CombatType.RANGED) {
            return skillManager.getCurrentLevel(Skill.RANGED);
        } else if (type == CombatType.MAGIC) {
            return skillManager.getCurrentLevel(Skill.MAGIC);
        }
        return skillManager.getCurrentLevel(Skill.ATTACK);
    }

    @Override
    public int getBaseDefence(CombatType type) {
        if (type == CombatType.MAGIC) {
            return skillManager.getCurrentLevel(Skill.MAGIC);
        }
        return skillManager.getCurrentLevel(Skill.DEFENCE);
    }

    @Override
    public int getAttackSpeed() {
        int speed = weapon.getSpeed();
        String weapon = equipment.get(Equipment.WEAPON_SLOT).getDefinition().getName();
        if (getCurrentlyCasting() != null) {
        	if (equipment.get(Equipment.WEAPON_SLOT).getId() == 13058) {
                return 4;
            }
            return 5;
        }
        int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
        if (weaponId == 1419) {
            speed -= 2;
        }
        if (fightType == FightType.CROSSBOW_RAPID || fightType == FightType.LONGBOW_RAPID || weaponId == 6522 && fightType == FightType.KNIFE_RAPID || weapon.contains("rapier")) {
            if (weaponId != 11235) {
                speed--;
            }
           

        } else if (weaponId != 6522 && weaponId != 15241 && (fightType == FightType.SHORTBOW_RAPID || fightType == FightType.DART_RAPID || fightType == FightType.KNIFE_RAPID || fightType == FightType.THROWNAXE_RAPID || fightType == FightType.JAVELIN_RAPID) || weaponId == 11730) {
            speed -= 2;
        }
        return speed;
        //	return DesolaceFormulas.getAttackDelay(this);
    }
    
    public int clue1Amount;
    public int clue2Amount;
    public int clue3Amount;
    public int clueLevel;
    public Item[] puzzleStoredItems;
    public int sextantGlobalPiece;
    public double sextantBarDegree;
    public int rotationFactor;
    public int sextantLandScapeCoords;
    public int sextantSunCoords;
    
   // private Channel channel;
    
  //  public Player write(Packet packet) {
    //    if (channel.isConnected()) {
    //        channel.write(packet);
    //    }
    //    return this;
  //  }
    
   /// public Channel getChannel() {
   //     return channel;
   // }
    
    private Bank bank = new Bank(this);
    
    public Bank getBank() {
        return bank;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;
        return p.getIndex() == getIndex() || p.getUsername().equals(username);
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void poisonVictim(Character victim, CombatType type) {
        if (type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
            CombatFactory.poisonEntity(victim, CombatPoisonData.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)));
        } else if (type == CombatType.RANGED) {
            CombatFactory.poisonEntity(victim, CombatPoisonData.getPoisonType(equipment.get(Equipment.AMMUNITION_SLOT)));
        }
    }
    
    @Override
    public void venomVictim(Character victim, CombatType type) {
        int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
        int helmet = equipment.get(Equipment.HEAD_SLOT).getId();
        if ((type == CombatType.MAGIC && weaponId == 12284)) {
            CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.WEAPON_SLOT)));
        }
        if ((type == CombatType.RANGED && weaponId == 12926 || type == CombatType.RANGED && weaponId == 12927)) {
            CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.WEAPON_SLOT)));
        } else if (helmet == 12282) {
        	CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.HEAD_SLOT)));
        }
    }

    @Override
    public CombatStrategy determineStrategy() {
        if (specialActivated && castSpell == null) {

            if (combatSpecial.getCombatType() == CombatType.MELEE) {
                return CombatStrategies.getDefaultMeleeStrategy();
            } else if (combatSpecial.getCombatType() == CombatType.RANGED) {
                setRangedWeaponData(RangedWeaponData.getData(this));
                return CombatStrategies.getDefaultRangedStrategy();
            } else if (combatSpecial.getCombatType() == CombatType.MAGIC) {
                return CombatStrategies.getDefaultMagicStrategy();
            }
        }

        if (castSpell != null || autocastSpell != null || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13058) {
            return CombatStrategies.getDefaultMagicStrategy();
        }

        RangedWeaponData data = RangedWeaponData.getData(this);
        if (data != null) {
            setRangedWeaponData(data);
            return CombatStrategies.getDefaultRangedStrategy();
        }

        return CombatStrategies.getDefaultMeleeStrategy();
    }

    public void process() {
        process.sequence();
    }

    public void dispose() {
        save();
        packetSender.sendLogout();
    }

    public void save() {
        if (session.getState() != SessionState.LOGGED_IN && session.getState() != SessionState.LOGGING_OUT) {
            return;
        }
        PlayerSaving.save(this);
    }

    public boolean logout() {
        if (getCombatBuilder().isBeingAttacked()) {
            getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
            return false;
        }
        if (getConstitution() <= 0 || isDying || settingUpCannon || crossingObstacle) {
            getPacketSender().sendMessage("You cannot log out at the moment.");
            return false;
        }
        return true;
    }

    public void restart() {
        setFreezeDelay(0);
        setOverloadPotionTimer(0);
        setPrayerRenewalPotionTimer(0);
        setSpecialPercentage(100);
        setSpecialActivated(false);
        CombatSpecial.updateBar(this);
        setHasVengeance(false);
        setSkullTimer(0);
        setSkullIcon(0);
        setTeleblockTimer(0);
        setPoisonDamage(0);
        setVenomDamage(0);
        setStaffOfLightEffect(0);
        performAnimation(new Animation(65535));
        getPacketSender().sendConstitutionOrbPoison(false);
        getPacketSender().sendConstitutionOrbVenom(false);
        WeaponInterfaces.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponAnimations.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
        PrayerHandler.deactivateAll(this);
        CurseHandler.deactivateAll(this);
        getEquipment().refreshItems();
        getInventory().refreshItems();
        for (Skill skill : Skill.values()) {
            getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
        }
        setRunEnergy(100);
        setDying(false);
        getMovementQueue().setLockMovement(false).reset();
        getUpdateFlag().flag(Flag.APPEARANCE);
    }

    public boolean busy() {
        return interfaceId > 0 || isBanking || dicing.inDice || shopping || trading.inTrade() || dueling.inDuelScreen || isResting;
    }

    /*
     * Fields
     */
    /**
     * * STRINGS **
     */
    private String username;
    private String password;
    private String serial_number;
    private String emailAddress;
    private String hostAddress;
    private String clanChatName;
    
	private HouseLocation houseLocation;
	
	private HouseTheme houseTheme;

    /**
     * * LONGS *
     */
    private Long longUsername;
    private long moneyInPouch;
    private long totalPlayTime;
    //Timers (Stopwatches)
    private final Stopwatch sqlTimer = new Stopwatch();
    private final Stopwatch protpraydelay = new Stopwatch().headStart(9000);
    private final Stopwatch foodTimer = new Stopwatch();
	private final Stopwatch karamBwanTimer = new Stopwatch();
    private final Stopwatch potionTimer = new Stopwatch();
    private final Stopwatch lastRunRecovery = new Stopwatch();
    private final Stopwatch clickDelay = new Stopwatch();
    private final Stopwatch lastItemPickup = new Stopwatch();
    private final Stopwatch lastYell = new Stopwatch();
    private final Stopwatch lastSql = new Stopwatch();
    private final Stopwatch krakenRespawn = new Stopwatch();

    private final Stopwatch lastVengeance = new Stopwatch();
    private final Stopwatch emoteDelay = new Stopwatch();
    private final Stopwatch specialRestoreTimer = new Stopwatch();
    private final Stopwatch lastSummon = new Stopwatch();
    private final Stopwatch recordedLogin = new Stopwatch();
    private final Stopwatch creationDate = new Stopwatch();
    private final Stopwatch tolerance = new Stopwatch();
    private final Stopwatch lougoutTimer = new Stopwatch();

    /**
     * * INSTANCES **
     */
    private final CopyOnWriteArrayList<KillsEntry> killsTracker = new CopyOnWriteArrayList<KillsEntry>();
    private final CopyOnWriteArrayList<DropLogEntry> dropLog = new CopyOnWriteArrayList<DropLogEntry>();
    private ArrayList<HouseFurniture> houseFurniture = new ArrayList<HouseFurniture>();
    private ArrayList<Portal> housePortals = new ArrayList<>();
    private final List<Player> localPlayers = new LinkedList<Player>();
    private final List<NPC> localNpcs = new LinkedList<NPC>();

    private PlayerSession session;
    private final PlayerProcess process = new PlayerProcess(this);
    private final PlayerKillingAttributes playerKillingAttributes = new PlayerKillingAttributes(this);
    private final MinigameAttributes minigameAttributes = new MinigameAttributes();
    private final BankPinAttributes bankPinAttributes = new BankPinAttributes();
    private final BankSearchAttributes bankSearchAttributes = new BankSearchAttributes();
    private final AchievementAttributes achievementAttributes = new AchievementAttributes();
    private CharacterAnimations characterAnimations = new CharacterAnimations();
    private final BonusManager bonusManager = new BonusManager();
    private final PointsHandler pointsHandler = new PointsHandler(this);


    private final PacketSender packetSender = new PacketSender(this);
    private final Appearance appearance = new Appearance(this);
    private final FrameUpdater frameUpdater = new FrameUpdater();
    private PlayerRights rights = PlayerRights.PLAYER;
    private SkillManager skillManager = new SkillManager(this);
    private PlayerRelations relations = new PlayerRelations(this);
    private ChatMessage chatMessages = new ChatMessage();
    private Inventory inventory = new Inventory(this);
    private Equipment equipment = new Equipment(this);
    private PriceChecker priceChecker = new PriceChecker(this);
    private Trading trading = new Trading(this);
    private Dueling dueling = new Dueling(this);
    private Slayer slayer = new Slayer(this);

    private Farming farming = new Farming(this);
    private Summoning summoning = new Summoning(this);
    private Bank[] bankTabs = new Bank[9];
    private Bank[] tempBankTabs = null;
    public Bank[] getTempBankTabs() {
		return tempBankTabs;
	}

	public void setTempBankTabs(Bank[] tempBankTabs) {
		this.tempBankTabs = tempBankTabs;
	}

	private Room[][][] houseRooms = new Room[5][13][13];
    private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
    private GameMode gameMode = GameMode.NORMAL;
    private CombatType lastCombatType = CombatType.MELEE;
    private FightType fightType = FightType.UNARMED_PUNCH;
    private Prayerbook prayerbook = Prayerbook.NORMAL;
    private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
    private LoyaltyTitles loyaltyTitle = LoyaltyTitles.NONE;

    private ClanChat currentClanChat;
    private Input inputHandling;
    private WalkToTask walkToTask;
    private Shop shop;
    private GameObject interactingObject;
    private Item interactingItem;
    private Dialogue dialogue;
    private DwarfCannon cannon;
    private CombatSpell autocastSpell, castSpell, previousCastSpell;
    private RangedWeaponData rangedWeaponData;
    private CombatSpecial combatSpecial;
    private WeaponInterface weapon;
    private Item untradeableDropItem;
    private Object[] usableObject;
    private GrandExchangeSlot[] grandExchangeSlots = new GrandExchangeSlot[6];
    private Task currentTask;
    private Position resetPosition;
    private Pouch selectedPouch;
    private BlowpipeLoading blowpipeLoading = new BlowpipeLoading(this);

    /**
     * * INTS **
     */
    public int destination = 0;
	public int lastClickedTab = 0;
	public int bossInterface = 0;
	public int prayblock = 0;
	
    private int[] brawlerCharges = new int[9];
    private int[] forceMovement = new int[7];
    private int[] leechedBonuses = new int[7];
    private int[] ores = new int[2];
    private int[] constructionCoords;
    private int recoilCharges;
    private int runEnergy = 100;
    private int currentBankTab;
    private int interfaceId, walkableInterfaceId, multiIcon;
    private int dialogueActionId;
    private int overloadPotionTimer, prayerRenewalPotionTimer;
    private int fireImmunity, fireDamageModifier;
    private int amountDonated;
    
    private int totalJailKills;
    private int wildernessLevel;
    private int fireAmmo;
    private int specialPercentage = 100;
    private int skullIcon = -1, skullTimer;
    private int teleblockTimer;
    private int dragonFireImmunity;
    private int poisonImmunity;
    public static int scrollAmount;
    private int shadowState;
    private int effigy;
    public int gameType;
    private int dfsCharges;
    private int playerViewingIndex;
    private int staffOfLightEffect;
    private int minutesBonusExp = -1;
    private int selectedGeSlot = -1;
    private int selectedGeItem = -1;
    private int geQuantity;
    private int gePricePerItem;
    private int selectedSkillingItem;
    private int currentBookPage;
    private int storedRuneEssence, storedPureEssence;
    private int trapsLaid;
    private int skillAnimation;
    private int houseServant;
    private int houseServantCharges;
    private int servantItemFetch;
    private int portalSelected;
    private int constructionInterface;
    private int buildFurnitureId;
    private int buildFurnitureX;
    private int buildFurnitureY;
    private int combatRingType;

    /**
     * * BOOLEANS **
     */
    private boolean unlockedLoyaltyTitles[] = new boolean[12];
    private boolean[] crossedObstacles = new boolean[7];
    private boolean processFarming;
    private boolean crossingObstacle;
    private boolean targeted;
    private boolean isBanking, noteWithdrawal, swapMode;
    private boolean regionChange, allowRegionChangePacket;
    private boolean isDying;
    private boolean isRunning = true, isResting;
    private boolean experienceLocked;
    private boolean clientExitTaskActive;
    private boolean drainingPrayer;
    private boolean shopping;
    private boolean settingUpCannon;
    private boolean hasVengeance;
    private boolean killsTrackerOpen;
    private boolean acceptingAid;
    private boolean autoRetaliate;
    private boolean autocast;
    private boolean specialActivated;
    public boolean playerInstanced;
    private boolean isCoughing;
    private boolean playerLocked;
    private boolean recoveringSpecialAttack;
    private boolean soundsActive, musicActive;
    private boolean newPlayer;
    private boolean passPlayer;
    private boolean openBank;
    private boolean inActive;
	public int timeOnline;
    private boolean inConstructionDungeon;
    private boolean isBuildingMode;
    private boolean voteMessageSent;
    private boolean receivedStarter;

    /*
     * Getters & Setters
     */
    public PlayerSession getSession() {
        return session;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public PriceChecker getPriceChecker() {
        return priceChecker;
    }

    /*
     * Getters and setters
     */
    public String getUsername() {
        return username;
    }

    public Player setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getLongUsername() {
        return longUsername;
    }

    public Player setLongUsername(Long longUsername) {
        this.longUsername = longUsername;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String address) {
        this.emailAddress = address;
    }

    public Player setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public Player setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
        return this;
    }

    public String getSerialNumber() {
        return serial_number;
    }

    public Player setSerialNumber(String serial_number) {
        this.serial_number = serial_number;
        return this;
    }

    public FrameUpdater getFrameUpdater() {
        return this.frameUpdater;
    }

    public PlayerRights getRights() {
        return rights;
    }

    public Player setRights(PlayerRights rights) {
        this.rights = rights;
        return this;
    }

    public ChatMessage getChatMessages() {
        return chatMessages;
    }

    public PacketSender getPacketSender() {
        return packetSender;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public PlayerRelations getRelations() {
        return relations;
    }

    public PlayerKillingAttributes getPlayerKillingAttributes() {
        return playerKillingAttributes;
    }

    public PointsHandler getPointsHandler() {
        return pointsHandler;
    }

    
    public boolean isImmuneToDragonFire() {
        return dragonFireImmunity > 0;
    }

    public int getDragonFireImmunity() {
        return dragonFireImmunity;
    }

    public void setDragonFireImmunity(int dragonFireImmunity) {
        this.dragonFireImmunity = dragonFireImmunity;
    }

    public void incrementDragonFireImmunity(int amount) {
        dragonFireImmunity += amount;
    }

    public void decrementDragonFireImmunity(int amount) {
        dragonFireImmunity -= amount;
    }
	
	
    public int getPoisonImmunity() {
        return poisonImmunity;
    }

    public void setPoisonImmunity(int poisonImmunity) {
        this.poisonImmunity = poisonImmunity;
    }
    
    public int getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(int scrollAmount) {
        this.scrollAmount = scrollAmount;
    }
    
    
    public void setVenomImmunity(int venomImmunity) {
        this.venomImmunity = venomImmunity;
    }

    public void incrementPoisonImmunity(int amount) {
        poisonImmunity += amount;
    }

    public void decrementPoisonImmunity(int amount) {
        poisonImmunity -= amount;
    }

    public boolean isAutoRetaliate() {
        return autoRetaliate;
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }

    /**
     * @return the castSpell
     */
    public CombatSpell getCastSpell() {
        return castSpell;
    }

    /**
     * @param castSpell the castSpell to set
     */
    public void setCastSpell(CombatSpell castSpell) {
        this.castSpell = castSpell;
    }

    public CombatSpell getPreviousCastSpell() {
        return previousCastSpell;
    }

    public void setPreviousCastSpell(CombatSpell previousCastSpell) {
        this.previousCastSpell = previousCastSpell;
    }

    /**
     * @return the autocast
     */
    public boolean isAutocast() {
        return autocast;
    }

    /**
     * @param autocast the autocast to set
     */
    public void setAutocast(boolean autocast) {
        this.autocast = autocast;
    }

    /**
     * @return the skullTimer
     */
    public int getSkullTimer() {
        return skullTimer;
    }

    /**
     * @param skullTimer the skullTimer to set
     */
    public void setSkullTimer(int skullTimer) {
        this.skullTimer = skullTimer;
    }

    public void decrementSkullTimer() {
        skullTimer -= 50;
    }

    /**
     * @return the skullIcon
     */
    public int getSkullIcon() {
        return skullIcon;
    }

    /**
     * @param skullIcon the skullIcon to set
     */
    public void setSkullIcon(int skullIcon) {
        this.skullIcon = skullIcon;
    }

    /**
     * @return the teleblockTimer
     */
    public int getTeleblockTimer() {
        return teleblockTimer;
    }

    private final CopyOnWriteArrayList<NPC> npc_faces_updated = new CopyOnWriteArrayList<NPC>();

    public CopyOnWriteArrayList<NPC> getNpcFacesUpdated() {
    	return npc_faces_updated;
    }
    
    /**
     * @param teleblockTimer the teleblockTimer to set
     */
    public void setTeleblockTimer(int teleblockTimer) {
        this.teleblockTimer = teleblockTimer;
    }

    public void decrementTeleblockTimer() {
        teleblockTimer--;
    }

    /**
     * @return the autocastSpell
     */
    public CombatSpell getAutocastSpell() {
        return autocastSpell;
    }

    /**
     * @param autocastSpell the autocastSpell to set
     */
    public void setAutocastSpell(CombatSpell autocastSpell) {
        this.autocastSpell = autocastSpell;
    }

    /**
     * @return the specialPercentage
     */
    public int getSpecialPercentage() {
        return specialPercentage;
    }

    /**
     * @param specialPercentage the specialPercentage to set
     */
    public void setSpecialPercentage(int specialPercentage) {
        this.specialPercentage = specialPercentage;
    }

    /**
     * @return the fireAmmo
     */
    public int getFireAmmo() {
        return fireAmmo;
    }

    /**
     * @param fireAmmo the fireAmmo to set
     */
    public void setFireAmmo(int fireAmmo) {
        this.fireAmmo = fireAmmo;
    }

    public int getWildernessLevel() {
        return wildernessLevel;
    }

    public void setWildernessLevel(int wildernessLevel) {
        this.wildernessLevel = wildernessLevel;
    }

    /**
     * @return the combatSpecial
     */
    public CombatSpecial getCombatSpecial() {
        return combatSpecial;
    }

    /**
     * @param combatSpecial the combatSpecial to set
     */
    public void setCombatSpecial(CombatSpecial combatSpecial) {
        this.combatSpecial = combatSpecial;
    }

    /**
     * @return the specialActivated
     */
    public boolean isSpecialActivated() {
        return specialActivated;
    }

    /**
     * @param specialActivated the specialActivated to set
     */
    public void setSpecialActivated(boolean specialActivated) {
        this.specialActivated = specialActivated;
    }
    
   

    public void decrementSpecialPercentage(int drainAmount) {
        this.specialPercentage -= drainAmount;

        if (specialPercentage < 0) {
            specialPercentage = 0;
        }
    }

    public void incrementSpecialPercentage(int gainAmount) {
        this.specialPercentage += gainAmount;

        if (specialPercentage > 100) {
            specialPercentage = 100;
        }
    }

    /**
     * @return the rangedAmmo
     */
    public RangedWeaponData getRangedWeaponData() {
        return rangedWeaponData;
    }

    /**
     * @param rangedAmmo the rangedAmmo to set
     */
    public void setRangedWeaponData(RangedWeaponData rangedWeaponData) {
        this.rangedWeaponData = rangedWeaponData;
    }

    /**
     * @return the weapon.
     */
    public WeaponInterface getWeapon() {
        return weapon;
    }

    public ArrayList<Integer> walkableInterfaceList = new ArrayList<>();
	public long lastHelpRequest;
	public long lastAuthClaimed;
	public GameModes selectedGameMode;
	private boolean areCloudsSpawned;
    
    public void resetInterfaces() {
        walkableInterfaceList.stream().filter((i) -> !(i == 41005 || i == 41000)).forEach((i) -> {
            getPacketSender().sendWalkableInterface(i, false);
        });
        
        walkableInterfaceList.clear();
    }
    
    public void sendParallellInterfaceVisibility(int interfaceId, boolean visible) {
        if (this != null && this.getPacketSender() != null) {
            if (visible) {
                if (walkableInterfaceList.contains(interfaceId)) {
                    return;
                } else {
                    walkableInterfaceList.add(interfaceId);
                }
            } else {
                if (!walkableInterfaceList.contains(interfaceId)) {
                    return;
                } else {
                    walkableInterfaceList.remove( (Object) interfaceId);
                }
            }
            
            getPacketSender().sendWalkableInterface(interfaceId, visible);
        }
    }

    /**
     * @param weapon the weapon to set.
     */
    public void setWeapon(WeaponInterface weapon) {
        this.weapon = weapon;
    }

    /**
     * @return the fightType
     */
    public FightType getFightType() {
        return fightType;
    }

    /**
     * @param fightType the fightType to set
     */
    public void setFightType(FightType fightType) {
        this.fightType = fightType;
    }

    public Bank[] getBanks() {
        return tempBankTabs == null ? bankTabs : tempBankTabs;
    }

    public Bank getBank(int index) {
        return tempBankTabs == null ? bankTabs[index] : tempBankTabs[index];
    }

    public Player setBank(int index, Bank bank) {
    	if(tempBankTabs == null) {
    		this.bankTabs[index] = bank;
    	} else {
    		this.tempBankTabs[index] = bank;
    	}
        return this;
    }

    public boolean isAcceptAid() {
        return acceptingAid;
    }

    public void setAcceptAid(boolean acceptingAid) {
        this.acceptingAid = acceptingAid;
    }

    public Trading getTrading() {
        return trading;
    }

    public Dueling getDueling() {
        return dueling;
    }

    public CopyOnWriteArrayList<KillsEntry> getKillsTracker() {
        return killsTracker;
    }

    public CopyOnWriteArrayList<DropLogEntry> getDropLog() {
        return dropLog;
    }

    public void setWalkToTask(WalkToTask walkToTask) {
        this.walkToTask = walkToTask;
    }

    public WalkToTask getWalkToTask() {
        return walkToTask;
    }

    public Player setSpellbook(MagicSpellbook spellbook) {
        this.spellbook = spellbook;
        return this;
    }

    public MagicSpellbook getSpellbook() {
        return spellbook;
    }

    public Player setPrayerbook(Prayerbook prayerbook) {
        this.prayerbook = prayerbook;
        return this;
    }

    public Prayerbook getPrayerbook() {
        return prayerbook;
    }

    /**
     * The player's local players list.
     */
    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    /**
     * The player's local npcs list getter
     */
    public List<NPC> getLocalNpcs() {
        return localNpcs;
    }

    public Player setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
        return this;
    }

    public int getInterfaceId() {
        return this.interfaceId;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean isDying) {
        this.isDying = isDying;
    }

    public int[] getForceMovement() {
        return forceMovement;
    }

    public Player setForceMovement(int[] forceMovement) {
        this.forceMovement = forceMovement;
        return this;
    }

    /**
     * @return the equipmentAnimation
     */
    public CharacterAnimations getCharacterAnimations() {
        return characterAnimations;
    }

    /**
     * @return the equipmentAnimation
     */
    public void setCharacterAnimations(CharacterAnimations equipmentAnimation) {
        this.characterAnimations = equipmentAnimation.clone();
    }

    public LoyaltyTitles getLoyaltyTitle() {
        return loyaltyTitle;
    }

    public void setLoyaltyTitle(LoyaltyTitles loyaltyTitle) {
        this.loyaltyTitle = loyaltyTitle;
    }

    public void setWalkableInterfaceId(int interfaceId2) {
        this.walkableInterfaceId = interfaceId2;
    }

    public PlayerInteractingOption getPlayerInteractingOption() {
        return playerInteractingOption;
    }

    public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
        this.playerInteractingOption = playerInteractingOption;
        return this;
    }

    public int getMultiIcon() {
        return multiIcon;
    }

    public Player setMultiIcon(int multiIcon) {
        this.multiIcon = multiIcon;
        return this;
    }

    public int getWalkableInterfaceId() {
        return walkableInterfaceId;
    }

    public boolean soundsActive() {
        return soundsActive;
    }

    public void setSoundsActive(boolean soundsActive) {
        this.soundsActive = soundsActive;
    }

    public boolean musicActive() {
        return musicActive;
    }

    public void setMusicActive(boolean musicActive) {
        this.musicActive = musicActive;
    }

    public BonusManager getBonusManager() {
        return bonusManager;
    }

    public int getRunEnergy() {
        return runEnergy;
    }

    public Player setRunEnergy(int runEnergy) {
        this.runEnergy = runEnergy;
        return this;
    }

    public Stopwatch getLastRunRecovery() {
        return lastRunRecovery;
    }

    public Player setRunning(boolean isRunning) {
        this.isRunning = isRunning;
        return this;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Player setResting(boolean isResting) {
        this.isResting = isResting;
        return this;
    }

    public boolean isResting() {
        return isResting;
    }

    public void setMoneyInPouch(long moneyInPouch) {
        this.moneyInPouch = moneyInPouch;
    }

    public long getMoneyInPouch() {
        return moneyInPouch;
    }

    public int getMoneyInPouchAsInt() {
        return moneyInPouch > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) moneyInPouch;
    }

    public boolean experienceLocked() {
        return experienceLocked;
    }

    public void setExperienceLocked(boolean experienceLocked) {
        this.experienceLocked = experienceLocked;
    }

    public void setClientExitTaskActive(boolean clientExitTaskActive) {
        this.clientExitTaskActive = clientExitTaskActive;
    }

    public boolean isClientExitTaskActive() {
        return clientExitTaskActive;
    }

    public Player setCurrentClanChat(ClanChat clanChat) {
        this.currentClanChat = clanChat;
        return this;
    }

    public ClanChat getCurrentClanChat() {
        return currentClanChat;
    }

    public String getClanChatName() {
        return clanChatName;
    }

    public Player setClanChatName(String clanChatName) {
        this.clanChatName = clanChatName;
        return this;
    }

    public void setInputHandling(Input inputHandling) {
        this.inputHandling = inputHandling;
    }

    public Input getInputHandling() {
        return inputHandling;
    }

    public boolean isDrainingPrayer() {
        return drainingPrayer;
    }

    public void setDrainingPrayer(boolean drainingPrayer) {
        this.drainingPrayer = drainingPrayer;
    }

    public Stopwatch getClickDelay() {
        return clickDelay;
    }

    public int[] getLeechedBonuses() {
        return leechedBonuses;
    }

    public Stopwatch getLastItemPickup() {
        return lastItemPickup;
    }

    public Stopwatch getLastSummon() {
        return lastSummon;
    }

    public BankSearchAttributes getBankSearchingAttribtues() {
        return bankSearchAttributes;
    }

    public AchievementAttributes getAchievementAttributes() {
        return achievementAttributes;
    }

    public BankPinAttributes getBankPinAttributes() {
        return bankPinAttributes;
    }

    public int getCurrentBankTab() {
        return currentBankTab;
    }

    public Player setCurrentBankTab(int tab) {
        this.currentBankTab = tab;
        return this;
    }

    public boolean isBanking() {
        return isBanking;
    }

    public Player setBanking(boolean isBanking) {
        this.isBanking = isBanking;
        return this;
    }

    public void setNoteWithdrawal(boolean noteWithdrawal) {
        this.noteWithdrawal = noteWithdrawal;
    }

    public boolean withdrawAsNote() {
        return noteWithdrawal;
    }

    public void setSwapMode(boolean swapMode) {
        this.swapMode = swapMode;
    }

    public boolean swapMode() {
        return swapMode;
    }

    public boolean isShopping() {
        return shopping;
    }

    public void setShopping(boolean shopping) {
        this.shopping = shopping;
    }

    public Shop getShop() {
        return shop;
    }

    public Player setShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public GameObject getInteractingObject() {
        return interactingObject;
    }

    public Player setInteractingObject(GameObject interactingObject) {
        this.interactingObject = interactingObject;
        return this;
    }

    public Item getInteractingItem() {
        return interactingItem;
    }

    public void setInteractingItem(Item interactingItem) {
        this.interactingItem = interactingItem;
    }

    public Dialogue getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    public int getDialogueActionId() {
        return dialogueActionId;
    }

    public void setDialogueActionId(int dialogueActionId) {
        this.dialogueActionId = dialogueActionId;
    }

    public void setSettingUpCannon(boolean settingUpCannon) {
        this.settingUpCannon = settingUpCannon;
    }

    public boolean isSettingUpCannon() {
        return settingUpCannon;
    }

    public Player setCannon(DwarfCannon cannon) {
        this.cannon = cannon;
        return this;
    }

    public DwarfCannon getCannon() {
        return cannon;
    }

    public Stopwatch getKarambwanTimer() {
		return karamBwanTimer;
	}
    public int getOverloadPotionTimer() {
        return overloadPotionTimer;
    }

    public void setOverloadPotionTimer(int overloadPotionTimer) {
        this.overloadPotionTimer = overloadPotionTimer;
    }

    public int getPrayerRenewalPotionTimer() {
        return prayerRenewalPotionTimer;
    }

    public void setPrayerRenewalPotionTimer(int prayerRenewalPotionTimer) {
        this.prayerRenewalPotionTimer = prayerRenewalPotionTimer;
    }

    public Stopwatch getSpecialRestoreTimer() {
        return specialRestoreTimer;
    }

    public boolean[] getUnlockedLoyaltyTitles() {
        return unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitles(boolean[] unlockedLoyaltyTitles) {
        this.unlockedLoyaltyTitles = unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitle(int index) {
        unlockedLoyaltyTitles[index] = true;
    }

    public Stopwatch getEmoteDelay() {
        return emoteDelay;
    }

    public MinigameAttributes getMinigameAttributes() {
        return minigameAttributes;
    }
    
    public Minigame getMinigame() {
    	return minigame;
    }
    
	public void setMinigame(Minigame minigame) {
		this.minigame = minigame;
	}

    public int getFireImmunity() {
        return fireImmunity;
    }

    public Player setFireImmunity(int fireImmunity) {
        this.fireImmunity = fireImmunity;
        return this;
    }

    public int getFireDamageModifier() {
        return fireDamageModifier;
    }

    public Player setFireDamageModifier(int fireDamageModifier) {
        this.fireDamageModifier = fireDamageModifier;
        return this;
    }

    public boolean hasVengeance() {
        return hasVengeance;
    }

    public void setHasVengeance(boolean hasVengeance) {
        this.hasVengeance = hasVengeance;
    }

    public Stopwatch getLastVengeance() {
        return lastVengeance;
    }
    
  
	public void setHouseRooms(Room[][][] houseRooms) {
		this.houseRooms = houseRooms;
	}

	
	public void setHousePortals(ArrayList<Portal> housePortals) {
		this.housePortals = housePortals;
	}
	
	/*
	 * Construction instancing Aj
	 */
	public boolean isVisible() {
		if(getLocation() == Locations.Location.CONSTRUCTION) {
			return false;
		}
		return true;
	}

	
	public void setHouseFurtinture(ArrayList<HouseFurniture> houseFurniture) {
		this.houseFurniture = houseFurniture;
	}


    public Stopwatch getTolerance() {
        return tolerance;
    }

    public boolean isTargeted() {
        return targeted;
    }

    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }

    public Stopwatch getLastYell() {
        return lastYell;
    }

    public Stopwatch getLastSql() {
        return lastSql;
    }
    
    public int getAmountDonated() {
        return amountDonated;
    }

    public void incrementAmountDonated(int amountDonated) {
        this.amountDonated += amountDonated;
    }
	
	
    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(long amount) {
        this.totalPlayTime = amount;
    }

    public Stopwatch getRecordedLogin() {
        return recordedLogin;
    }
    
    /*
     * jail cell
     */
   
   

    public Player setRegionChange(boolean regionChange) {
        this.regionChange = regionChange;
        return this;
    }

    public boolean isChangingRegion() {
        return this.regionChange;
    }

    public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
        this.allowRegionChangePacket = allowRegionChangePacket;
    }

    public boolean isAllowRegionChangePacket() {
        return allowRegionChangePacket;
    }

    public boolean isKillsTrackerOpen() {
        return killsTrackerOpen;
    }

    public void setKillsTrackerOpen(boolean killsTrackerOpen) {
        this.killsTrackerOpen = killsTrackerOpen;
    }

    public boolean isCoughing() {
        return isCoughing;
    }

    public void setCoughing(boolean isCoughing) {
        this.isCoughing = isCoughing;
    }

    public int getShadowState() {
        return shadowState;
    }

    public void setShadowState(int shadow) {
        this.shadowState = shadow;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isPlayerLocked() {
        return playerLocked;
    }

    public Player setPlayerLocked(boolean playerLocked) {
        this.playerLocked = playerLocked;
        return this;
    }
    /*
     * Handles setting and checking of boss instancing
     */
    public boolean isPlayerInstanced() {
        return playerInstanced;
    }
    public Player setPlayerInstanced(boolean playerInstanced) {
        this.playerInstanced = playerInstanced;
        return this;
    }
    
    public Stopwatch getSqlTimer() {
        return sqlTimer;
    }

    public Stopwatch getProtPrayDelay() {
        return protpraydelay;
    }
    
    public Stopwatch getFoodTimer() {
        return foodTimer;
    }

    public Stopwatch getPotionTimer() {
        return potionTimer;
    }

    public Item getUntradeableDropItem() {
        return untradeableDropItem;
    }

    public void setUntradeableDropItem(Item untradeableDropItem) {
        this.untradeableDropItem = untradeableDropItem;
    }

    public boolean isRecoveringSpecialAttack() {
        return recoveringSpecialAttack;
    }

    public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
        this.recoveringSpecialAttack = recoveringSpecialAttack;
    }

    public CombatType getLastCombatType() {
        return lastCombatType;
    }

    public void setLastCombatType(CombatType lastCombatType) {
        this.lastCombatType = lastCombatType;
    }

    public int getEffigy() {
        return this.effigy;
    }

    public void setEffigy(int effigy) {
        this.effigy = effigy;
    }
    
  

    public int getDfsCharges() {
        return dfsCharges;
    }

    public void incrementDfsCharges(int amount) {
        this.dfsCharges += amount;
    }

    public void setNewPlayer(boolean newPlayer) {
        this.newPlayer = newPlayer;
    }

    public boolean newPlayer() {
        return newPlayer;
    }
    
    public void setPassPlayer(boolean passPlayer) {
        this.passPlayer = passPlayer;
    }

    public boolean passPlayer() {
        return passPlayer;
    }

    public Stopwatch getLogoutTimer() {
        return lougoutTimer;
    }

    public Player setUsableObject(Object[] usableObject) {
        this.usableObject = usableObject;
        return this;
    }

    public Player setUsableObject(int index, Object usableObject) {
        this.usableObject[index] = usableObject;
        return this;
    }

    public Object[] getUsableObject() {
        return usableObject;
    }

    public int getPlayerViewingIndex() {
        return playerViewingIndex;
    }

    public void setPlayerViewingIndex(int playerViewingIndex) {
        this.playerViewingIndex = playerViewingIndex;
    }

    public boolean hasStaffOfLightEffect() {
        return staffOfLightEffect > 0;
    }

    public int getStaffOfLightEffect() {
        return staffOfLightEffect;
    }

    public void setStaffOfLightEffect(int staffOfLightEffect) {
        this.staffOfLightEffect = staffOfLightEffect;
    }

    public void decrementStaffOfLightEffect() {
        this.staffOfLightEffect--;
    }

    public boolean openBank() {
        return openBank;
    }

    public void setOpenBank(boolean openBank) {
        this.openBank = openBank;
    }

    public int getMinutesBonusExp() {
        return minutesBonusExp;
    }

    public void setMinutesBonusExp(int minutesBonusExp, boolean add) {
        this.minutesBonusExp = (add ? this.minutesBonusExp + minutesBonusExp : minutesBonusExp);
    }

    public void setInactive(boolean inActive) {
        this.inActive = inActive;
    }

    public boolean isInActive() {
        return inActive;
    }

    public int getSelectedGeItem() {
        return selectedGeItem;
    }

    public void setSelectedGeItem(int selectedGeItem) {
        this.selectedGeItem = selectedGeItem;
    }

    public int getGeQuantity() {
        return geQuantity;
    }

    public void setGeQuantity(int geQuantity) {
        this.geQuantity = geQuantity;
    }

    public int getGePricePerItem() {
        return gePricePerItem;
    }

    public void setGePricePerItem(int gePricePerItem) {
        this.gePricePerItem = gePricePerItem;
    }

    public GrandExchangeSlot[] getGrandExchangeSlots() {
        return grandExchangeSlots;
    }

    public void setGrandExchangeSlots(GrandExchangeSlot[] GrandExchangeSlots) {
        this.grandExchangeSlots = GrandExchangeSlots;
    }

    
    
    public void setGrandExchangeSlot(int index, GrandExchangeSlot state) {
        this.grandExchangeSlots[index] = state;
    }

    public void setSelectedGeSlot(int slot) {
        this.selectedGeSlot = slot;
    }

    public int getSelectedGeSlot() {
        return selectedGeSlot;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public int getSelectedSkillingItem() {
        return selectedSkillingItem;
    }

    public void setSelectedSkillingItem(int selectedItem) {
        this.selectedSkillingItem = selectedItem;
    }

    public boolean shouldProcessFarming() {
        return processFarming;
    }

    public void setProcessFarming(boolean processFarming) {
        this.processFarming = processFarming;
    }

    public Pouch getSelectedPouch() {
        return selectedPouch;
    }

    public void setSelectedPouch(Pouch selectedPouch) {
        this.selectedPouch = selectedPouch;
    }

    public int getCurrentBookPage() {
        return currentBookPage;
    }

    public void setCurrentBookPage(int currentBookPage) {
        this.currentBookPage = currentBookPage;
    }

    public int getStoredRuneEssence() {
        return storedRuneEssence;
    }

    public void setStoredRuneEssence(int storedRuneEssence) {
        this.storedRuneEssence = storedRuneEssence;
    }

    public int getStoredPureEssence() {
        return storedPureEssence;
    }

    public void setStoredPureEssence(int storedPureEssence) {
        this.storedPureEssence = storedPureEssence;
    }

    public int getTrapsLaid() {
        return trapsLaid;
    }

    public void setTrapsLaid(int trapsLaid) {
        this.trapsLaid = trapsLaid;
    }

    public boolean isCrossingObstacle() {
        return crossingObstacle;
    }

    public Player setCrossingObstacle(boolean crossingObstacle) {
        this.crossingObstacle = crossingObstacle;
        return this;
    }

    public boolean[] getCrossedObstacles() {
        return crossedObstacles;
    }

    public boolean getCrossedObstacle(int i) {
        return crossedObstacles[i];
    }

    public Player setCrossedObstacle(int i, boolean completed) {
        crossedObstacles[i] = completed;
        return this;
    }

    public void setCrossedObstacles(boolean[] crossedObstacles) {
        this.crossedObstacles = crossedObstacles;
    }

    public int getSkillAnimation() {
        return skillAnimation;
    }

    public Player setSkillAnimation(int animation) {
        this.skillAnimation = animation;
        return this;
    }

    public int[] getOres() {
        return ores;
    }

    public void setOres(int[] ores) {
        this.ores = ores;
    }

    public void setResetPosition(Position resetPosition) {
        this.resetPosition = resetPosition;
    }

    public Position getResetPosition() {
        return resetPosition;
    }

    public Slayer getSlayer() {
        return slayer;
    }
  
    public Summoning getSummoning() {
        return summoning;
    }

    public Farming getFarming() {
        return farming;
    }

    public boolean inConstructionDungeon() {
        return inConstructionDungeon;
    }

    public void setInConstructionDungeon(boolean inConstructionDungeon) {
        this.inConstructionDungeon = inConstructionDungeon;
    }

    public int getHouseServant() {
        return houseServant;
    }
    public HouseLocation getHouseLocation() {
		return houseLocation;
	}
	public HouseTheme getHouseTheme() {
		return houseTheme;
	}

	public void setHouseTheme(HouseTheme houseTheme) {
		this.houseTheme = houseTheme;
	}
	public void setHouseLocation(HouseLocation houseLocation) {
		this.houseLocation = houseLocation;
	}

    public void setHouseServant(int houseServant) {
        this.houseServant = houseServant;
    }

    public int getHouseServantCharges() {
        return this.houseServantCharges;
    }

    public void setHouseServantCharges(int houseServantCharges) {
        this.houseServantCharges = houseServantCharges;
    }

    public void incrementHouseServantCharges() {
        this.houseServantCharges++;
    }

    public int getServantItemFetch() {
        return servantItemFetch;
    }

    public void setServantItemFetch(int servantItemFetch) {
        this.servantItemFetch = servantItemFetch;
    }

    public int getPortalSelected() {
        return portalSelected;
    }

    public void setPortalSelected(int portalSelected) {
        this.portalSelected = portalSelected;
    }

    public boolean isBuildingMode() {
        return this.isBuildingMode;
    }

    public void setIsBuildingMode(boolean isBuildingMode) {
        this.isBuildingMode = isBuildingMode;
    }

    public int[] getConstructionCoords() {
        return constructionCoords;
    }

    public void setConstructionCoords(int[] constructionCoords) {
        this.constructionCoords = constructionCoords;
    }

    public int getBuildFurnitureId() {
        return this.buildFurnitureId;
    }

    public void setBuildFuritureId(int buildFuritureId) {
        this.buildFurnitureId = buildFuritureId;
    }

    public int getBuildFurnitureX() {
        return this.buildFurnitureX;
    }

    public void setBuildFurnitureX(int buildFurnitureX) {
        this.buildFurnitureX = buildFurnitureX;
    }

    public int getBuildFurnitureY() {
        return this.buildFurnitureY;
    }

    public void setBuildFurnitureY(int buildFurnitureY) {
        this.buildFurnitureY = buildFurnitureY;
    }

    public int getCombatRingType() {
        return this.combatRingType;
    }

    public void setCombatRingType(int combatRingType) {
        this.combatRingType = combatRingType;
    }

    public Room[][][] getHouseRooms() {
        return houseRooms;
    }

    public ArrayList<Portal> getHousePortals() {
        return housePortals;
    }

    public ArrayList<HouseFurniture> getHouseFurniture() {
        return houseFurniture;
    }

    public int getConstructionInterface() {
        return this.constructionInterface;
    }

    public void setConstructionInterface(int constructionInterface) {
        this.constructionInterface = constructionInterface;
    }

    public int[] getBrawlerChargers() {
        return this.brawlerCharges;
    }

    public void setBrawlerCharges(int[] brawlerCharges) {
        this.brawlerCharges = brawlerCharges;
    }

    
    public int getRecoilCharges() {
        return this.recoilCharges;
    }

    public int setRecoilCharges(int recoilCharges) {
        return this.recoilCharges = recoilCharges;
    }

    public boolean voteMessageSent() {
        return this.voteMessageSent;
    }

    public void setVoteMessageSent(boolean voteMessageSent) {
        this.voteMessageSent = voteMessageSent;
    }

    public boolean didReceiveStarter() {
        return receivedStarter;
    }
    
	public void sendMessage(String string) {
		packetSender.sendMessage(string);
	}

    public void setReceivedStarter(boolean receivedStarter) {
        this.receivedStarter = receivedStarter;
    }
    
    public BlowpipeLoading getBlowpipeLoading() {
    	return blowpipeLoading;
    }

	public boolean cloudsSpawned() {
		return areCloudsSpawned;
	}
	
	public void setCloudsSpawned(boolean cloudsSpawned) {
		this.areCloudsSpawned = cloudsSpawned;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isShopUpdated() {
		return shopUpdated;
	}

	public void setShopUpdated(boolean shopUpdated) {
		this.shopUpdated = shopUpdated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYellmsg() {
		return yellmsg;
	}

	public void setYellMsg(String yellmsg) {
		this.yellmsg = yellmsg;
	}
	
	public void write(Packet packet) {
		// TODO Auto-generated method stub
		
	}

	public boolean isHidePlayer() {
		return hidePlayer;
	}

	public void setHidePlayer(boolean hidePlayer) {
		this.hidePlayer = hidePlayer;
	}

	public int[] getCompCapeColors() {
		return compCapeColors;
	}

	public void setCompCapeColors(int[] compCapeColors) {
		this.compCapeColors = compCapeColors;
	}

	public int getCurrentCape() {
		return currentCape;
	}

	public void setCurrentCape(int currentCape) {
		this.currentCape = currentCape;
	}

	public boolean isPrayerInjured() {
		// TODO Auto-generated method stub
		return false;
	}

	public long getDragonScimInjury() {
		return dragonScimInjury;
	}

	public void setDragonScimInjury(long dragonScimInjury) {
		this.dragonScimInjury = dragonScimInjury;
	}

	public Stopwatch getKrakenRespawn() {
		return krakenRespawn;
	}

	public byte[] getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}

	public void setCachedUpdateBlock(byte[] cachedUpdateBlock) {
		this.cachedUpdateBlock = cachedUpdateBlock;
	}

	public Position selectedPos = null;
	
	public Position getSelectedpos() {
		return selectedPos;
	}
	
	public void setSelectedpos(Position selectedPos) {
		this.selectedPos = selectedPos;
	}

}
