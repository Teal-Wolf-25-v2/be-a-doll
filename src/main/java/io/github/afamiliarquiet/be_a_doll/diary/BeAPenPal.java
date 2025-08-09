package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public class BeAPenPal {
	public static void fillPen() {
		PayloadTypeRegistry.playS2C().register(C2SDollDismountLetter.ID, C2SDollDismountLetter.PACKET_CODEC);
	}

	// i'll move this into its own file if i make any more. but i don't think i will
	public record C2SDollDismountLetter(List<Integer> dismountingDollIds) implements CustomPayload {
		public static final CustomPayload.Id<C2SDollDismountLetter> ID = new Id<>(BeADoll.id("doll_dismount_letter"));

		public static final PacketCodec<ByteBuf, C2SDollDismountLetter> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER.collect(PacketCodecs.toList()),
			C2SDollDismountLetter::dismountingDollIds,
			C2SDollDismountLetter::new
		);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}
}
