package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.item.DollcraftItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
	public static final Item CARVING_KNIFE = register("carving_knife", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).enchantable(17).attributeModifiers(weapon(4, -2.4f)));
	public static final Item MODELING_TOOL = register("modeling_tool", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).enchantable(17).attributeModifiers(weapon(2, -1.3f)));
	public static final Item SEWING_NEEDLE = register("sewing_needle", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).attributeModifiers(weapon(3, -2f)));
		.repairable(Items.IRON_INGOT).maxDamage(310).enchantable(17).attributeModifiers(weapon(3, -2f)));

	public static void inquireAboutTheCollection() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> {
			itemGroup.addAfter(Items.BRUSH, CARVING_KNIFE, MODELING_TOOL, SEWING_NEEDLE);
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

	public static AttributeModifiersComponent weapon(float attackDamage, float attackSpeed) {
		return AttributeModifiersComponent.builder()
			.add(
				EntityAttributes.ATTACK_DAMAGE,
				new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage, EntityAttributeModifier.Operation.ADD_VALUE),
				AttributeModifierSlot.MAINHAND
			)
			.add(
				EntityAttributes.ATTACK_SPEED,
				new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
				AttributeModifierSlot.MAINHAND
			)
			.build();
	}
}
