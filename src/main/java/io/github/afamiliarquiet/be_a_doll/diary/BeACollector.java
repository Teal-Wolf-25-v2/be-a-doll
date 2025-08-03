package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.item.DollcraftItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class BeACollector {
	public static final Item SEWING_NEEDLE = register("sewing_needle", DollcraftItem::new, new Item.Settings());

	public static void inquireAboutTheCollection() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> {
			itemGroup.addAfter(Items.SHEARS, SEWING_NEEDLE);
		});
	}

	public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
		Item item = factory.apply(settings.registryKey(key));
		if (item instanceof BlockItem blockItem) {
			blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
		}

		return Registry.register(Registries.ITEM, key, item);
	}

	public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(key(id), factory, settings);
	}

	public static RegistryKey<Item> key(String thing) {
		return RegistryKey.of(RegistryKeys.ITEM, BeADoll.id(thing));
	}
}
