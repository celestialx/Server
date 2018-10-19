package com.ruseps.world.content.combat;

import java.util.List;

import com.ruseps.engine.task.Task;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.Achievements;
import com.ruseps.world.content.Achievements.AchievementData;
import com.ruseps.world.content.Sounds;
import com.ruseps.world.content.combat.CombatContainer.CombatHit;
import com.ruseps.world.content.combat.strategy.impl.Nex;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.NPCMovementCoordinator.CoordinateState;
import com.ruseps.world.entity.impl.player.Player;

/**
 * A {@link Task} implementation that deals a series of hits to an entity after
 * a delay.
 *
 * @author lare96
 */
public class CombatHitTask extends Task {

    /**
     * The attacker instance.
     */
    private Character attacker;

    /**
     * The victim instance.
     */
    private Character victim;

    /**
     * The attacker's combat builder attached to this task.
     */
    private CombatBuilder builder;

    /**
     * The attacker's combat container that will be used.
     */
    private CombatContainer container;

    /**
     * The total damage dealt during this hit.
     */
    private int damage;

    /**
     * Create a new {@link CombatHit}.
     *
     * @param builder the combat builder attached to this task.
     * @param container the combat hit that will be used.
     * @param delay the delay in ticks before the hit will be dealt.
     * @param initialRun if the task should be ran right away.
     */
    public CombatHitTask(CombatBuilder builder, CombatContainer container,
            int delay, boolean initialRun) {
        super(delay, builder.getCharacter(), initialRun);
        this.builder = builder;
        this.container = container;
        this.attacker = builder.getCharacter();
        this.victim = builder.getVictim();
    }

    public CombatHitTask(CombatBuilder builder, CombatContainer container) { //Instant attack, no task
        this.builder = builder;
        this.container = container;
        this.attacker = builder.getCharacter();
        this.victim = builder.getVictim();
    }

    @Override
    public void execute() {

        handleAttack();

        this.stop();
    }
    public static boolean weaknessForAir, weaknessForEarth, weaknessForWater, weaknessForFire;

	/**
	 * Checks if the entity has a weakness
	 *	public void checkWeakness() {
		weaknessForAir = weaknessForEarth = weaknessForWater = weaknessForFire = false;
		if(victim != null){
			if(victim.isNpc() && victim.getCombatBuilder().getStrategy().getWeakness() != null){
		switch (victim.getCombatBuilder().getStrategy().getWeakness()) {
			case AIR:
				weaknessForAir = true;
				break;
			case EARTH:
				weaknessForEarth = true;
				break;
			case WATER:
				weaknessForWater = true;
				break;
			case FIRE:
				weaknessForFire = true;
				break;
			case NONE:
				break;
			case RANGED:
				break;
			case MAGIC:
				break;
			case CRUMBLED_UNDEAD:
				break;
			case SHARP:
				break;
			case TWO_HANDED:
				break;
			case WHIPS:
				break;
			default:
				break;
		}
		}
		}
	}
*/
    public void handleEntityInterface(Character attacker, Character victim, int damage) {
        if (attacker.isPlayer()) {
            Player p = (Player) attacker;
            
            if (victim.isPlayer()) {//plrs
                Player v = (Player) victim;
                int maximumHealth = v.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
                int currentHealth = v.getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
                String entityName = v.getUsername();
                p.getPacketSender().sendEntityInterface(victim.isPlayer() ? 1 : 0, maximumHealth, currentHealth, entityName);
            } else if (victim.isNpc()) {//npcs
                NPC v = (NPC) victim;
                int maximumHealth = v.getDefaultConstitution();
                int currentHealth = v.getConstitution();
                String entityName = v.getDefinition().getName();
                p.getPacketSender().sendEntityInterface(victim.isPlayer() ? 1 : 0, maximumHealth, currentHealth, entityName);
            }
        }
    }

    public void handleAttack() {
     
        
        if (attacker.getConstitution() <= 0 || !attacker.isRegistered()) {
            return;
        }

		// Do any hit modifications to the container here first.
        if (container.getModifiedDamage() > 0) {
            container.allHits(context -> {
                context.getHit().setDamage(container.getModifiedDamage());
                context.setAccurate(true);
            });
        }

		// Now we send the hitsplats if needed! We can't send the hitsplats
        // there are none to send, or if we're using magic and it splashed.
        if (container.getHits().length != 0 && container.getCombatType() != CombatType.MAGIC || container.isAccurate()) {

            /**
             * PRAYERS *
             */
            CombatFactory.applyPrayerProtection(container, builder);

            this.damage = container.getDamage();
            
            if(victim != null) {
            	
            	victim.getCombatBuilder().addDamage(attacker, damage);
                container.dealDamage();

                handleEntityInterface(attacker, victim, damage);
                
            }

            /**
             * MISC *
             */
            if (attacker.isPlayer()) {
                Player p = (Player) attacker;
                if (damage > 0) {
                    if (p.getLocation() == Location.PEST_CONTROL_GAME) {
                        p.getMinigameAttributes().getPestControlAttributes().incrementDamageDealt(damage);
                    } else if (p.getLocation() == Location.DUNGEONEERING) {
                        p.getMinigameAttributes().getDungeoneeringAttributes().incrementDamageDealt(damage);
                    }
                    /**
                     * ACHIEVEMENTS *
                     */
                    if (container.getCombatType() == CombatType.MELEE) {
                        Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_MELEE, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_MELEE, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_MELEE, damage);
                    } else if (container.getCombatType() == CombatType.RANGED) {
                        Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_RANGED, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_RANGED, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_RANGED, damage);
                    } else if (container.getCombatType() == CombatType.MAGIC) {
                        Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_MAGIC, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_MAGIC, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_MAGIC, damage);
                    }
                    if (victim.isPlayer()) {
                        Achievements.finishAchievement(p, AchievementData.FIGHT_ANOTHER_PLAYER);
                    }
                }
            } else {
                if (victim.isPlayer() && container.getCombatType() == CombatType.DRAGON_FIRE) {
                    Player p = (Player) victim;
                    if (Misc.getRandom(20) <= 15 && p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283 || p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613) {
                        p.setPositionToFace(attacker.getPosition().copy());
                        CombatFactory.chargeDragonFireShield(p);
                    }
                    if (damage >= 160) {
                        ((Player) victim).getPacketSender().sendMessage("You are badly burnt by the dragon's fire!");
                    }
                }
            }
        }

        // Give experience based on the hits.
        CombatFactory.giveExperience(builder, container, damage);

        if (!container.isAccurate()) {
            if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
                victim.performGraphic(new Graphic(85, GraphicHeight.MIDDLE));
                attacker.getCurrentlyCasting().finishCast(attacker, victim, false, 0);
                attacker.setCurrentlyCasting(null);
            }
        } else if (container.isAccurate()) {

            CombatFactory.handleArmorEffects(attacker, victim, damage, container.getCombatType());
            CombatFactory.handlePrayerEffects(attacker, victim, damage, container.getCombatType());
            CombatFactory.handleSpellEffects(attacker, victim, damage, container.getCombatType());

            attacker.poisonVictim(victim, container.getCombatType());
			int random = Misc.getRandom(3);
			if (random == 2)
				attacker.venomVictim(victim, container.getCombatType());
			
            // Finish the magic spell with the correct end graphic.
            if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
                attacker.getCurrentlyCasting().endGraphic().ifPresent(victim::performGraphic);
                attacker.getCurrentlyCasting().finishCast(attacker, victim, true, damage);
                attacker.setCurrentlyCasting(null);
            }
        }

        // Send the defensive animations.
        if (victim != null && victim.getCombatBuilder().getAttackTimer() <= 2) {
            if (victim.isPlayer()) {
            	Player plr = (Player) victim;
        	
        		if(plr.getWeapon().getSpeed() <= 4) {

        		} else {
                    victim.performAnimation(new Animation(WeaponAnimations.getBlockAnimation(((Player) victim))));

        		}
            	
				if (((Player) victim).getInterfaceId() > 0) {
					if(victim.getLocation() == Location.KRAKEN) {
					} else {
						((Player) victim).getPacketSender().sendInterfaceRemoval();
					}
				}
            } else if (victim.isNpc()) {
                if (!(((NPC) victim).getId() >= 6142 && ((NPC) victim).getId() <= 6145)) {
                    victim.performAnimation(new Animation(((NPC) victim).getDefinition().getDefenceAnimation()));
                }
            }
        }

        // Fire the container's dynamic hit method.
        container.onHit(damage, container.isAccurate());
        
		//Handles Kraken whirlpools
        if (victim.isNpc() && attacker.isPlayer()) {
    		
			NPC vic = ((NPC) victim);
			if (vic.getId() == 7750 || vic.getId() == 7703 || vic.getId() == 7702 ) {
				victim.getCombatBuilder().attack(attacker);
				vic.setFindNewTarget(false);
			}
			if (vic.getId() == 708) {
				attacker.getCombatBuilder().reset(true);
				vic.appendDeath();

			}
			if (World.getNpcs().contains(vic)) {
				Player player = ((Player) attacker);
				if (vic.getId() == 150) {
					player.getKraken().incrementPools(player, vic);
				} else if (vic.getId() == 149) {
					if (player.getKraken().getPoolsDisturbed() == 4) {
						player.getKraken().incrementPools(player, vic);
					}
				}
			} else {
				Player player = ((Player) attacker);
				if (player.getLocation() == Location.KRAKEN) {
					if (!World.getNpcs().contains(vic)) {
						return;
					}
				}
			}
		}
				
        // And finally auto-retaliate if needed.
        if (!victim.getCombatBuilder().isAttacking() || victim.getCombatBuilder().isCooldown()
				|| victim.isNpc() && ((NPC) victim).findNewTarget()) {

			if (victim.isPlayer() && ((Player) victim).isAutoRetaliate() && !victim.getMovementQueue().isMoving()
					&& ((Player) victim).getWalkToTask() == null) {

				victim.getCombatBuilder().setDidAutoRetaliate(true);
				victim.getCombatBuilder().attack(attacker);
			}
			else if (attacker.isNpc()) {
				NPC npc = (NPC) attacker;

				if (!npc.getDefinition().isAttackable()) {
					return;
				}
				if (npc.getId() != 7691 && npc.getId() != 10 && npc.getId() != 7707) {
					return;
				}

				victim.getCombatBuilder().setDidAutoRetaliate(true);
				victim.getCombatBuilder().attack(attacker);
			} else if (victim.isNpc()) {
				NPC npc = (NPC) victim;
				if (!(attacker.isNpc() && ((NPC) attacker).isSummoningNpc())) {
					if (npc.getMovementCoordinator().getCoordinateState() == CoordinateState.HOME
							&& npc.getLocation() != Location.PEST_CONTROL_GAME && npc.getId() != 7691
							&& npc.getId() != 10 && npc.getId() != 7706 && npc.getId() != 7707) {
						victim.getCombatBuilder().attack(attacker);
						npc.setFindNewTarget(false);

					}
				}
			}

		}

        if (attacker.isNpc() && victim.isPlayer()) {
            NPC npc = (NPC) attacker;
            Player p = (Player) victim;
            if (npc.switchesVictim() && Misc.getRandom(6) <= 1) {
                if (npc.getDefinition().isAggressive()) {
                    npc.setFindNewTarget(true);
                } else {
                    if (p.getLocalPlayers().size() >= 1) {
                        List<Player> list = p.getLocalPlayers();
                        Player c = list.get(Misc.getRandom(list.size() - 1));
                        npc.getCombatBuilder().attack(c);
                    }
                }
            }

            Sounds.sendSound(p, Sounds.getPlayerBlockSounds(p.getEquipment().get(Equipment.WEAPON_SLOT).getId()));
            /**
             * CUSTOM ON DAMAGE STUFF *
             */
            if (victim.isPlayer() && npc.getId() == 13447) {
                Nex.dealtDamage(((Player) victim), damage);
            }

        } else if (attacker.isPlayer()) {
            Player player = (Player) attacker;

            /**
             * SKULLS *
             */
            if (player.getLocation() == Location.WILDERNESS
					&& victim.isPlayer()) {
				if (!player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isBeingAttacked()
						&& Location.inMulti(player)) {
					if(!player.getCombatBuilder().didAutoRetaliate()) {
						if(((Player) victim).getSkullTimer() > 0) {
							if(player.getCombatBuilder().getLastAttacker() != victim){
								CombatFactory.skullPlayer(player);
							}
						} else {
							CombatFactory.skullPlayer(player);
						}
					}
				}
			}

            player.setLastCombatType(container.getCombatType());

            Sounds.sendSound(player, Sounds.getPlayerAttackSound(player));

            /**
             * CUSTOM ON DAMAGE STUFF *
             */
            if (victim.isNpc() && ((NPC) victim).getId() == 13447) {
                Nex.takeDamage(player, damage);
            } else if (victim.isPlayer()) {
                Sounds.sendSound((Player) victim, Sounds.getPlayerBlockSounds(((Player) victim).getEquipment().get(Equipment.WEAPON_SLOT).getId()));
            }
        }
    }
}
