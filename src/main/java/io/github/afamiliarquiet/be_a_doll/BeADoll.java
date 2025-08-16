package io.github.afamiliarquiet.be_a_doll;

import io.github.afamiliarquiet.be_a_doll.diary.BeABirdwatcher;
import io.github.afamiliarquiet.be_a_doll.diary.BeABug;
import io.github.afamiliarquiet.be_a_doll.diary.BeACollector;
import io.github.afamiliarquiet.be_a_doll.diary.BeACook;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.github.afamiliarquiet.be_a_doll.diary.BeAPenPal;
import io.github.afamiliarquiet.be_a_doll.diary.BeAResearcher;
import io.github.afamiliarquiet.be_a_doll.diary.BeAWitch;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.IntFunction;

public class BeADoll implements ModInitializer {
	public static final String MOD_ID = "be_a_doll";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final BeATinkerer TINKERER = BeATinkerer.createToml(FabricLoader.getInstance().getConfigDir(), "", MOD_ID, BeATinkerer.class);

	@Override
	public void onInitialize() {
		log(BeAMaid.syntheticKeysmashing("oh are we logging?! HELLO WORLD! I'M READY TO MAKE SOME MORE DOLLS!", null));
		BeAMaid.bestowApron();
		BeACollector.inquireAboutTheCollection();
		BeAPenPal.fillPen();
		BeABirdwatcher.offerTea();
		BeACook.placeOrders();
		BeALibrarian.lookForABook();
		BeAResearcher.grantFunding();
		BeAWitch.putOnHat();
		BeABug.lookAtBug();
	}

	public static Identifier id(String thing) {
		return Identifier.of(MOD_ID, thing);
	}

	// probably a good habit to always log my id whenever i'm throwing things in the log, even if it's just a quick test
	public static void log(String message) {
		LOGGER.info("[Be a doll!] {}", message);
	}

	public enum Variant implements StringIdentifiable {
		REPRESSED(0, "player", ItemTags.ANVIL), // gonna look really silly in your throat.
		WOODEN(1, "wooden", BeAResearcher.WOODEN_DOLL_CARE_MATERIALS),
		PORCELAIN(2, "porcelain", BeAResearcher.PORCELAIN_DOLL_CARE_MATERIALS),
		CLOTH(3, "cloth", BeAResearcher.CLOTH_DOLL_CARE_MATERIALS);

		public static final BeADoll.Variant DEFAULT = WOODEN;
		public static final StringIdentifiable.EnumCodec<BeADoll.Variant> CODEC = StringIdentifiable.createCodec(BeADoll.Variant::values);
		private static final IntFunction<BeADoll.Variant> INDEX_MAPPER = ValueLists.createIndexToValueFunction(
			BeADoll.Variant::getIndex, values(), ValueLists.OutOfBoundsHandling.ZERO
		);
		public static final PacketCodec<ByteBuf, BeADoll.Variant> PACKET_CODEC = PacketCodecs.indexed(INDEX_MAPPER, BeADoll.Variant::getIndex);
		private final int index;
		private final String id;
		private final TagKey<Item> careMaterial;

		Variant(final int index, final String id, TagKey<Item> careMaterial) {
			this.index = index;
			this.id = id;
			this.careMaterial = careMaterial;
		}

		@Override
		public String asString() {
			return this.id;
		}

		public int getIndex() {
			return this.index;
		}

		public static BeADoll.Variant byIndex(int index) {
			return (BeADoll.Variant)INDEX_MAPPER.apply(index);
		}

		public TagKey<Item> getCareMaterialTag() {
			return this.careMaterial;
		}
	}
}
