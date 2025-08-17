package io.github.afamiliarquiet.be_a_doll.letters;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record S2CDollRepairedLetter(int entityId, ItemStack material) implements CustomPayload {
	public static final CustomPayload.Id<S2CDollRepairedLetter> ID = new CustomPayload.Id<>(BeADoll.id("doll_repaired_letter"));

	public static final PacketCodec<RegistryByteBuf, S2CDollRepairedLetter> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.INTEGER,
		S2CDollRepairedLetter::entityId,
		ItemStack.PACKET_CODEC,
		S2CDollRepairedLetter::material,
		S2CDollRepairedLetter::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
