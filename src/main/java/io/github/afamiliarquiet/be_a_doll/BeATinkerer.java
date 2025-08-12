package io.github.afamiliarquiet.be_a_doll;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;

public class BeATinkerer extends WrappedConfig {
	@Comment("Set this to false if you do not want your speech inhibited as a doll.")
	@Comment("Only affects the client's sent messages, not the messages of other players")
	public Boolean useKeysmashing = true;

	@Comment("If not empty, this will be the pool of letters that your keysmashes consist of.")
	@Comment("By default, there's a few different options that are selected from based on your UUID.")
	public String letterPoolOverride = "";

	@Comment("Controls the percentage of your pool that must have been used in keysmash before letters can repeat")
	@Comment("The pool must be this % full or less to trigger restock")
	public float restockThreshold = 0.13f;

	@Comment("Any non-letter characters only have a chance to be included based on a 'clarity' level of your message.")
	@Comment("This controls the minimum chance that non-letters are included")
	public float baseClarityChance = 0.31f;

	@Comment("The clarity score is what becomes the chance of inclusion when adding a non-letter")
	@Comment("This chance is calculated as (clarityScore / (1 + keysmash length)) - so the first character starts at (1 + baseClarity)")
	public float startingClarityScore = 1f;

	@Comment("When a character is added that is converted to keysmashing, the clarity score is /multiplied/ by this value")
	public float keysmashedMultiplier = 0.8f;

	@Comment("When a character is added that is not converted to keysmashing, this is added to the clarity score")
	public float spokenLoudlyClarity = 1.3f;

	@Comment("When a non-letter is added, this is added to the clarity score")
	@Comment("(for smileys and symbols and non-english messages, it's probably best to have this at a neutral 1)")
	public float nonletterClarity = 1f;
}
