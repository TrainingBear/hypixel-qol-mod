package com.trbear96.mixin;

import com.trbear96.qol.perkembangan_teknologi.SawitGameplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
//	@Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"))
//	private void onUpdateBreakingHead(
//			BlockPos pos,
//			Direction direction,
//			CallbackInfoReturnable<Boolean> cir
//	) {
//		// If target block changes, allow re-start
//		if (SawitGameplay.breakingPos != null && !SawitGameplay.breakingPos.equals(pos)) {
//			SawitGameplay.breakingPos = null;
//		}
//	}
//
//	@Inject(method = "updateBlockBreakingProgress", at = @At("TAIL")
//	)
//	private void onUpdateBreakingTail(
//			BlockPos pos,
//			Direction direction,
//			CallbackInfoReturnable<Boolean> cir
//	) {
//		// If block is gone → reset
//		var client = MinecraftClient.getInstance();
//		if (client.world == null) return;
//
//		if (client.world.isAir(pos)) {
//			SawitGameplay.breakingPos = null;
//		}
//	}
//
//	@Inject(method = "updateBlockBreakingProgress",
//			at = @At("TAIL"))
//	public void updateBlockBreakingProgress(CallbackInfoReturnable<Boolean> cir) {
//		if (!cir.getReturnValue()) {
//			var client = MinecraftClient.getInstance();
//			var player = client.player;
//			if(player == null) return;
////			SawitGameplay.breakingPos = false;
//			player.swingHand(Hand.MAIN_HAND);
//			System.out.println("Swing!");
//		}
//	}
@Inject(method = "breakBlock", at = @At("HEAD"))
private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
	SawitGameplay.breakingPos = null;
//	System.out.println("Blocks has been broken");
}
}
