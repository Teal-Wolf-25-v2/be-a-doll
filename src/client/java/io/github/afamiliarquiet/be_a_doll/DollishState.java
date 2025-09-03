package io.github.afamiliarquiet.be_a_doll;

import net.minecraft.text.Text;

public interface DollishState {
	void be_a_doll$setVariant(BeADoll.Variant variant);
	BeADoll.Variant be_a_doll$getVariant();
	void be_a_doll$setDoll(boolean doll);
	boolean be_a_doll$isDoll();
	void be_a_doll$setTargeted(boolean targeted);
	boolean be_a_doll$isTargeted();
	void be_a_doll$setDollName(Text name);
	Text be_a_doll$getDollName();
}
