package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.model.Animation;
import com.ruseps.model.CombatIcon;
import com.ruseps.model.Graphic;
import com.ruseps.model.Hit;
import com.ruseps.model.Hitmask;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.Skill;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.impl.VladimirSpawning;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/** @author Live:nrpker7839 <-- my skype; **/

public class AnimatedBook implements CombatStrategy	 {

	public int phase = 0;
	public int i = 0;
	public int attackSpeed = 5;
	
	private static final Animation anim = new Animation(13169);

	
	@Override
	public boolean canAttack(Character entity, Character victim) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		int random = Misc.getRandom(10);
		Player player = (Player)victim;
		
		if(phase == 0) {

			new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
			
			entity.performAnimation(anim);
			player.performGraphic(new Graphic(435));
			if(random == 5) {
				entity.forceChat("We Follow our lord and saviour, Lexicus");
			}
			if(entity.getConstitution() < 7000) {
				phase = 1;
				attackSpeed = 2;
			}
			
		} else if(phase == 1) {
			i++;
			player.dealDamage(new Hit(250, Hitmask.RED, CombatIcon.NONE));
			
			if(i == 6) {
				phase = 2;
				i = 0;
				attackSpeed = 3;
			}
			
		} else if(phase == 2) {
			if(player.getConstitution() > 0) {
				if(i == 0) {
				} else if(i == 1) {
				} else if(i == 2) {
					entity.forceChat("I think you should put Mage pray on!");
				}
				
				if(i > 2 && i < 10) {
					new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
					new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
					entity.performAnimation(anim);

					player.performGraphic(new Graphic(1194));
				} else if(i == 10) {
				} else if(i == 11) {
					player.dealDamage(new Hit(500, Hitmask.RED, CombatIcon.NONE));
				} else if(i >= 12) {
					new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
					entity.performAnimation(anim);

					player.performGraphic(new Graphic(434));

				}
				i++;
				if(entity.getConstitution() < 4000) {
					phase = 3;
					i = 0;
				}
			}

		} else if(phase == 3) {
			if(i == 0) {
				entity.forceChat("Fear us, "+player.getUsername());
			} else if(i > 0 && i < 7) {
				player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getCurrentLevel(Skill.PRAYER) - 100);
			} else if(i >= 7 && i < 11) {
				attackSpeed = 1;
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
				entity.performAnimation(anim);

				player.performGraphic(new Graphic(433));

			} else if(entity.getConstitution() < 2000 && i >= 11) {
				if(i == 12) {
				}
				attackSpeed = 6;
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
				entity.performAnimation(anim);

				player.performGraphic(new Graphic(433));

			} else {
				attackSpeed = 5;
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				entity.performAnimation(anim);

				player.performGraphic(new Graphic(94));
				player.performGraphic(new Graphic(93));

				if(random == 5) {
				}
			}
				
			i++;
		}
		if(player.getConstitution() <= 0) {
			phase = 0;
			attackSpeed = 5;
		}
		return false;
	}

	@Override
	public int attackDelay(Character entity) {
		// TODO Auto-generated method stub
		return attackSpeed;
	}

	@Override
	public int attackDistance(Character entity) {
		// TODO Auto-generated method stub
		return 3;
	}


	@Override
	public CombatType getCombatType() {
		// TODO Auto-generated method stub
		return null;
	}

}
