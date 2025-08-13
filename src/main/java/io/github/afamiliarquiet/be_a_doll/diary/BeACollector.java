package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.item.DollcraftItem;
import io.github.afamiliarquiet.be_a_doll.item.RibbonItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
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
import net.minecraft.util.Rarity;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class BeACollector {
	// todo - it might make more sense for there to be a doll.. .variant component..... i already have that
	//  but wait i can't add that component to a diamond pickaxe. guh
	public static final Item CARVING_KNIFE = register("carving_knife", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).attributeModifiers(weapon(4, -2.4f)));
	public static final Item MODELING_TOOL = register("modeling_tool", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).attributeModifiers(weapon(2, -1.3f)));
	public static final Item SEWING_NEEDLE = register("sewing_needle", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).attributeModifiers(weapon(3, -2f)));

	public static final Item DOLL_RIBBON = register("ribbon", RibbonItem::new, new Item.Settings());

	public static final ComponentType<BeADoll.Variant> DOLL_VARIANT_COMPONENT = register(
		"doll_variant", builder -> builder.codec(BeADoll.Variant.CODEC).packetCodec(BeADoll.Variant.PACKET_CODEC)
	);
	public static final Item ESSENCE_FRAGMENT = register("essence_fragment", Item::new, new Item.Settings()
		.maxCount(1)
		.rarity(Rarity.EPIC)
		.component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
		.component(DOLL_VARIANT_COMPONENT, BeADoll.Variant.REPRESSED)
	);

	public static void inquireAboutTheCollection() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> {
			itemGroup.addAfter(Items.BRUSH, CARVING_KNIFE, MODELING_TOOL, SEWING_NEEDLE, DOLL_RIBBON);
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

	private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, BeADoll.id(id), builderOperator.apply(ComponentType.builder()).build());
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
