package com.kd8lvt.kdsbonemealablepots;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KdsBonemealablePots implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("kds-bonemealable-pots");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockState state = world.getBlockState(hitResult.getBlockPos());
			if (player.isSpectator() || !player.getMainHandStack().isItemEqual(new ItemStack(Registry.ITEM.get(Identifier.tryParse("minecraft:bone_meal")))) || state.getBlock().getClass() != FlowerPotBlock.class) {
				return ActionResult.PASS;
			}

			if (state.getBlock().getPickStack(world,hitResult.getBlockPos(),state).isOf(Registry.ITEM.get(Identifier.tryParse("minecraft:flower_pot")))) {
				return ActionResult.PASS;
			}

			if (player.getInventory().getEmptySlot() >= 0 || player.getInventory().contains(state.getBlock().getPickStack(world,hitResult.getBlockPos(),state))) {
				BoneMealItem.createParticles(world,hitResult.getBlockPos(),30);
				player.getInventory().offerOrDrop(state.getBlock().getPickStack(world, hitResult.getBlockPos(), state).copy());
				if (!player.isCreative()) {
					player.getMainHandStack().decrement(1);
				}
			}

			return ActionResult.CONSUME_PARTIAL;
		});
	}
}
