package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class BeALibrarian {
	// how does this librarian differ from the maid? this one handles the forbidden (experimental) knowledge!

	public static final AttachmentType<BeADoll.Variant> DOLL_VARIANT = AttachmentRegistry.create(
		BeADoll.id("doll_variant"),
		builder -> builder
			.initializer(() -> BeADoll.Variant.DEFAULT)
			.persistent(BeADoll.Variant.CODEC)
			.syncWith(BeADoll.Variant.PACKET_CODEC, AttachmentSyncPredicate.all())
	);

	public static final AttachmentType<Text> DOLL_NAME = AttachmentRegistry.create(
		BeADoll.id("doll_name"),
		builder -> builder
			.persistent(TextCodecs.CODEC)
			.syncWith(TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, AttachmentSyncPredicate.all())
	);


	public static void lookForABook() {

	}


	/**
	 * finds the doll's variant from attachment, or gets default if none found.<br/>
	 * you should check {@link io.github.afamiliarquiet.be_a_doll.BeAMaid#isDoll(PlayerEntity)} as the authority
	 * on whether a PlayerEntity is a player or a doll. remember this, quiet. i made javadoc for you.
	 * @return the doll's variant, or the default doll type (which is NOT a normal player and is still a doll type)
	 */
	public static @NotNull BeADoll.Variant inspectDollMaterial(@NotNull PlayerEntity doll) {
		BeADoll.log("i am in the inspectDollMaterial function");
		return doll.getAttachedOrCreate(DOLL_VARIANT);
	}

	// okay so it looks bad now that i'm not using the above one at all. it's interesting yeah
	// listen okay i need isDoll to be authoritative because it relies on the attributes
	// and i don't ever want a doll to lose their attributes
	// so if they lose their attributes then they must not be a doll anymore, ergo a doll has not lost their attributes
	// being lopsided is no good so this avoids that
	public static @NotNull BeADoll.Variant inspectSupposedPlayer(@NotNull PlayerEntity supposedPlayer) {
		return BeAMaid.isDoll(supposedPlayer) ? inspectDollMaterial(supposedPlayer) : BeADoll.Variant.REPRESSED;
	}

	// yeah we're just washing off the experimental api smell here
	public static void reshapeDoll(@NotNull PlayerEntity doll, @NotNull BeADoll.Variant variant) {
		BeADoll.log("i am in the reshapeDoll function");
		doll.setAttached(DOLL_VARIANT, variant);
	}

	public static @Nullable Text inspectDollLabel(@NotNull PlayerEntity doll) {
		BeADoll.log("i am in the inspectDollLabel function");
		return doll.getAttached(DOLL_NAME);
	}

	public static void relabelDoll(@NotNull PlayerEntity doll, @NotNull Text name) {
		BeADoll.log("i am in the relabelDoll function");
		doll.setAttached(DOLL_NAME, name);
	}

	public static void repress(@NotNull PlayerEntity player) {
		BeADoll.log("i am in the repress function");
		player.removeAttached(DOLL_VARIANT);
		player.removeAttached(DOLL_NAME);
	}
}
