package io.github.afamiliarquiet.be_a_doll.letters;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

// i'll move this into its own file if i make any more. but i don't think i will
public record S2CDollDismountLetter(List<Integer> dismountingDollIds) implements CustomPayload {
	public static final Id<S2CDollDismountLetter> ID = new Id<>(BeADoll.id("doll_dismount_letter"));

	public static final PacketCodec<ByteBuf, S2CDollDismountLetter> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.INTEGER.collect(PacketCodecs.toList()),
		S2CDollDismountLetter::dismountingDollIds,
		S2CDollDismountLetter::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
