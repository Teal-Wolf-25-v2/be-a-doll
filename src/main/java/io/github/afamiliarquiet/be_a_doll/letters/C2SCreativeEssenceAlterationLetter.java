package io.github.afamiliarquiet.be_a_doll.letters;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.BeASelf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

// me sowing: haha fsdf;lkj yeah!!! thanks creative inventory screen now i know how to do an evil and strange slot!!
// me reaping: well this fdj;lksing sucks. why are you being evil to me too.
public record C2SCreativeEssenceAlterationLetter(boolean inserting, ItemStack statedCursorStack) implements CustomPayload {
	public static final Id<C2SCreativeEssenceAlterationLetter> ID = new Id<>(BeADoll.id("creative_essence_alteration_letter"));

	public static final PacketCodec<RegistryByteBuf, C2SCreativeEssenceAlterationLetter> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.BOOLEAN,
		C2SCreativeEssenceAlterationLetter::inserting,
		ItemStack.OPTIONAL_PACKET_CODEC,
		C2SCreativeEssenceAlterationLetter::statedCursorStack,
		C2SCreativeEssenceAlterationLetter::new
	);

	public static void receive(C2SCreativeEssenceAlterationLetter letter, ServerPlayNetworking.Context context) {
		if (!context.player().isInCreativeMode()) {
			return; // how dare you. this is for creative only
		}
		// fire this off still for the tf effects n such to happen on server
		BeASelf.clickSelf(letter.statedCursorStack(), context.player(), letter.inserting());
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
