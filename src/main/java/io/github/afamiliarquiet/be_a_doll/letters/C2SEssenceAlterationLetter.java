package io.github.afamiliarquiet.be_a_doll.letters;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record C2SEssenceAlterationLetter(boolean inserting) implements CustomPayload {
	public static final CustomPayload.Id<C2SEssenceAlterationLetter> ID = new CustomPayload.Id<>(BeADoll.id("essence_alteration_letter"));

	public static final PacketCodec<ByteBuf, C2SEssenceAlterationLetter> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.BOOLEAN,
		C2SEssenceAlterationLetter::inserting,
		C2SEssenceAlterationLetter::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
