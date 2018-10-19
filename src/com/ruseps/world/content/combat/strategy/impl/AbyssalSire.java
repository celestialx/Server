package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Projectile;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.HitQueue;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Handles Abyssal Sire combat script
 * @Author Jonny
 */

public class AbyssalSire implements CombatStrategy {

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        NPC sire = (NPC) entity;
        if (sire.isChargingAttack() || sire.getConstitution() <= 0) {
            return true;
        }
        Player target = (Player) victim;
        int chance = Misc.inclusiveRandom(1, 3);
        if(chance == 2) {
            sire.setChargingAttack(true);
            sire.performAnimation(new Animation(5368));
            TaskManager.submit(new Task(1, target, false) {
                int tick = 0;
                @Override
                public void execute() {
                    if(tick == 1) {
                        new Projectile(sire, target, 1067, 44, 3, 43, 30, 0).sendProjectile();
                    }
                    if(tick == 2) {
                    //    new HitQueue.CombatHit().handleAttack();
                        sire.setChargingAttack(false);
                        stop();
                    }
                    tick++;
                }
            });
        } else {
            sire.performAnimation(new Animation(sire.getDefinition().getAttackAnimation()));
            sire.getCombatBuilder().setContainer(new CombatContainer(sire, victim, 1, 0, CombatType.MELEE, true));
        }
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 2;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
}