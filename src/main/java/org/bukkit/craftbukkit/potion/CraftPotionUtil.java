package org.bukkit.craftbukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import net.minecraft.potion.Potion;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.potion.PotionData;

public class CraftPotionUtil {

    private static final BiMap<PotionType, String> regular = ImmutableBiMap.<PotionType, String>builder()
            .put(PotionType.UNCRAFTABLE, "empty")
            .put(PotionType.WATER, "water")
            .put(PotionType.MUNDANE, "mundane")
            .put(PotionType.THICK, "thick")
            .put(PotionType.AWKWARD, "awkward")
            .put(PotionType.NIGHT_VISION, "night_vision")
            .put(PotionType.INVISIBILITY, "invisibility")
            .put(PotionType.JUMP, "leaping")
            .put(PotionType.FIRE_RESISTANCE, "fire_resistance")
            .put(PotionType.SPEED, "swiftness")
            .put(PotionType.SLOWNESS, "slowness")
            .put(PotionType.WATER_BREATHING, "water_breathing")
            .put(PotionType.INSTANT_HEAL, "healing")
            .put(PotionType.INSTANT_DAMAGE, "harming")
            .put(PotionType.POISON, "poison")
            .put(PotionType.REGEN, "regeneration")
            .put(PotionType.STRENGTH, "strength")
            .put(PotionType.WEAKNESS, "weakness")
            .put(PotionType.LUCK, "luck")
            .build();
    private static final BiMap<PotionType, String> upgradeable = ImmutableBiMap.<PotionType, String>builder()
            .put(PotionType.JUMP, "strong_leaping")
            .put(PotionType.SPEED, "strong_swiftness")
            .put(PotionType.INSTANT_HEAL, "strong_healing")
            .put(PotionType.INSTANT_DAMAGE, "strong_harming")
            .put(PotionType.POISON, "strong_poison")
            .put(PotionType.REGEN, "strong_regeneration")
            .put(PotionType.STRENGTH, "strong_strength")
            .build();
    private static final BiMap<PotionType, String> extendable = ImmutableBiMap.<PotionType, String>builder()
            .put(PotionType.NIGHT_VISION, "long_night_vision")
            .put(PotionType.INVISIBILITY, "long_invisibility")
            .put(PotionType.JUMP, "long_leaping")
            .put(PotionType.FIRE_RESISTANCE, "long_fire_resistance")
            .put(PotionType.SPEED, "long_swiftness")
            .put(PotionType.SLOWNESS, "long_slowness")
            .put(PotionType.WATER_BREATHING, "long_water_breathing")
            .put(PotionType.POISON, "long_poison")
            .put(PotionType.REGEN, "long_regeneration")
            .put(PotionType.STRENGTH, "long_strength")
            .put(PotionType.WEAKNESS, "long_weakness")
            .build();

    public static String fromBukkit(PotionData data) {
        String type;
        if (data.isUpgraded()) {
            type = upgradeable.get(data.getType());
        } else if (data.isExtended()) {
            type = extendable.get(data.getType());
        } else {
            type = regular.get(data.getType());
        }
        Preconditions.checkNotNull(type, "Unknown potion type from data " + data);

        return "minecraft:" + type;
    }

    public static PotionData toBukkit(String type) {
        if (type == null) {
            return new PotionData(PotionType.UNCRAFTABLE, false, false);
        }
        if (type.startsWith("minecraft:")) {
            type = type.substring(10);
        }
        PotionType potionType = null;
        potionType = extendable.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, true, false);
        }
        potionType = upgradeable.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, false, true);
        }
        potionType = regular.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, false, false);
        }
        return new PotionData(PotionType.UNCRAFTABLE, false, false);
    }

    public static net.minecraft.potion.PotionEffect fromBukkit(PotionEffect effect) {
        Potion type = Potion.getPotionById(effect.getType().getId());
        return new net.minecraft.potion.PotionEffect(type, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
    }

    public static PotionEffect toBukkit(net.minecraft.potion.PotionEffect effect) {
        PotionEffectType type = PotionEffectType.getById(Potion.getIdFromPotion(effect.getPotion()));
        int amp = effect.getAmplifier();
        int duration = effect.getDuration();
        boolean ambient = effect.getIsAmbient();
        boolean particles = effect.doesShowParticles();
        return new PotionEffect(type, duration, amp, ambient, particles);
    }

    public static boolean equals(Potion mobEffect, PotionEffectType type) {
        PotionEffectType typeV = PotionEffectType.getById(Potion.getIdFromPotion(mobEffect));
        return typeV.equals(type);
    }
}
