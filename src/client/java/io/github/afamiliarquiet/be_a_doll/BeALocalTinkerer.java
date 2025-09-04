package io.github.afamiliarquiet.be_a_doll;

import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.Config;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Processor;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import io.github.afamiliarquiet.be_a_doll.letters.C2SKeysmashConfigSyncLetter;

@SuppressWarnings("unused")
@Processor("markDirtyForSync")
public class BeALocalTinkerer extends ReflectiveConfig {
	public transient boolean dirty = false;

	public void markDirtyForSync(Config.Builder builder) {
		// crap. i forgot the callback is called before modification... that's annoying.
		// forced to use ReflectiveConfig instead of WrappedConfig because of that!
		// that was the whole appeal of quilt config to me, that it was so blissfully simple
		// and now i'm stuck in ReflectiveHell..

		// this gets fired a bunch of times if you modify multiple things at once. annoying.
		// well, now client tick start will check this for grime and send it.
		builder.callback(config -> {
			dirty = true;
//			if (ClientPlayNetworking.canSend(C2SKeysmashConfigSyncLetter.ID)) {
//				ClientPlayNetworking.send(writtenForAFriend());
//			}
		});
	}

	public C2SKeysmashConfigSyncLetter writtenForAFriend() {
		// feels like there should be a way to not do this. but whatever
		BeADoll.log(String.valueOf(readableSelf.value()));
		return new C2SKeysmashConfigSyncLetter(useKeysmashing.value(), readableSelf.value(), alwaysReadableOthers.value(), letterPoolOverride.value(), restockThreshold.value(), useOrderedSpooling.value(), baseClarityChance.value(), startingClarityScore.value(), keysmashedMultiplier.value(), spokenLoudlyClarity.value(), nonletterClarity.value());
	}

	@Comment("Set this to false if you do not want your speech inhibited as a doll.")
	@Comment("Only affects the client's sent messages, not the messages of other players")
	public final TrackedValue<Boolean> useKeysmashing = value(true);

	@Comment("Set this to true if you want to read your own messages through the filter.")
	public final TrackedValue<Boolean> readableSelf = value(false);

	@Comment("Set this to false if you don't want to read others' messages through the filter.")
	@Comment("This only affects the check for doll-to-doll communication, not the proximity check.")
	public final TrackedValue<Boolean> alwaysReadableOthers = value(true);

	@Comment("If not empty, this will be the pool of letters that your keysmashes consist of.")
	@Comment("By default, there's a few different options that are selected from based on your UUID.")
	public final TrackedValue<String> letterPoolOverride = value("");

	@Comment("Controls the percentage of your pool that must have been used in keysmash before letters can repeat")
	@Comment("The pool must be this % full or less to trigger restock")
	public final TrackedValue<Float> restockThreshold = value(0.13f);

	@Comment("If true, your letter pool will be chosen from in order instead of randomly.")
	@Comment("This will also trigger a restock every time the filter processes a non-letter.")
	public final TrackedValue<Boolean> useOrderedSpooling = value(false);

	@Comment("Any non-letter characters only have a chance to be included based on a 'clarity' level of your message.")
	@Comment("This controls the minimum chance that non-letters are included")
	public final TrackedValue<Float> baseClarityChance = value(0.31f);

	@Comment("The clarity score is what becomes the chance of inclusion when adding a non-letter")
	@Comment("This chance is calculated as (clarityScore / (1 + keysmash length)) - so the first character starts at (1 + baseClarity)")
	public final TrackedValue<Float> startingClarityScore = value(1f);

	@Comment("When a character is added that is converted to keysmashing, the clarity score is /multiplied/ by this value")
	public final TrackedValue<Float> keysmashedMultiplier = value(0.8f);

	@Comment("When a character is added that is not converted to keysmashing, this is added to the clarity score")
	public final TrackedValue<Float> spokenLoudlyClarity = value(1.3f);

	@Comment("When a non-letter is added, this is added to the clarity score")
	@Comment("(for smileys and symbols and non-english messages, it's probably best to have this at a neutral 1)")
	public final TrackedValue<Float> nonletterClarity = value(1f);
}
