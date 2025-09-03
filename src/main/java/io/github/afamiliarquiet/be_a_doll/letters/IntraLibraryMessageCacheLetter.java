package io.github.afamiliarquiet.be_a_doll.letters;

import net.minecraft.network.message.SentMessage;

public record IntraLibraryMessageCacheLetter(boolean senderSmashesKeys, SentMessage keysmashedMessage, SentMessage dolledMessage) {
}
