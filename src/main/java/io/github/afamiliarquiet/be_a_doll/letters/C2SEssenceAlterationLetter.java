package io.github.afamiliarquiet.be_a_doll.letters;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.BeASelf;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.screen.PlayerScreenHandler;

public record C2SEssenceAlterationLetter(boolean inserting) implements CustomPayload {
	public static final CustomPayload.Id<C2SEssenceAlterationLetter> ID = new CustomPayload.Id<>(BeADoll.id("essence_alteration_letter"));

	public static final PacketCodec<ByteBuf, C2SEssenceAlterationLetter> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.BOOLEAN,
		C2SEssenceAlterationLetter::inserting,
		C2SEssenceAlterationLetter::new
	);

	public static void receive(C2SEssenceAlterationLetter letter, ServerPlayNetworking.Context context) {
		PlayerScreenHandler handler = context.player().playerScreenHandler;
		ItemStack clickProcessedStack = BeASelf.clickSelf(handler.getCursorStack(), context.player(), letter.inserting());
		if (clickProcessedStack != null && !context.player().isInCreativeMode()) {
			handler.setCursorStack(clickProcessedStack);
		}
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
