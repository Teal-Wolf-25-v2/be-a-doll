package io.github.afamiliarquiet.be_a_doll.letters;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record C2SKeysmashConfigSyncLetter(boolean useKeysmashing, boolean readableSelf, boolean readableOthers, String letterPoolOverride, float restockThreshold, boolean useOrderedSpooling, float baseClarityChance, float startingClarityScore, float keysmashedMultiplier, float spokenLoudlyClarity, float nonletterClarity) implements CustomPayload {
	public static final Id<C2SKeysmashConfigSyncLetter> ID = new Id<>(BeADoll.id("keysmash_config_letter"));

	public static final PacketCodec<ByteBuf, C2SKeysmashConfigSyncLetter> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.BOOLEAN, C2SKeysmashConfigSyncLetter::useKeysmashing,
		PacketCodecs.BOOLEAN, C2SKeysmashConfigSyncLetter::readableSelf,
		PacketCodecs.BOOLEAN, C2SKeysmashConfigSyncLetter::readableOthers,
		PacketCodecs.STRING, C2SKeysmashConfigSyncLetter::letterPoolOverride,
		PacketCodecs.FLOAT, C2SKeysmashConfigSyncLetter::restockThreshold,
		PacketCodecs.BOOLEAN, C2SKeysmashConfigSyncLetter::useOrderedSpooling,
		PacketCodecs.FLOAT, C2SKeysmashConfigSyncLetter::baseClarityChance,
		PacketCodecs.FLOAT, C2SKeysmashConfigSyncLetter::startingClarityScore,
		PacketCodecs.FLOAT, C2SKeysmashConfigSyncLetter::keysmashedMultiplier,
		PacketCodecs.FLOAT, C2SKeysmashConfigSyncLetter::spokenLoudlyClarity,
		PacketCodecs.FLOAT, C2SKeysmashConfigSyncLetter::nonletterClarity,
		C2SKeysmashConfigSyncLetter::new
	);

	// this certainly isn't super ideal
	public static final C2SKeysmashConfigSyncLetter DEFAULT = new C2SKeysmashConfigSyncLetter(true, false, true, "", 0.13f, false, 0.31f, 1f, 0.8f, 1.3f, 1f);

	public static void receive(C2SKeysmashConfigSyncLetter letter, ServerPlayNetworking.Context context) {
		BeALibrarian.filePasswordManager(context.player(), letter);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
