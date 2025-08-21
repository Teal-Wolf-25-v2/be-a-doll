package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.item.DollcraftItem;
import io.github.afamiliarquiet.be_a_doll.item.RibbonItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.PlaySoundConsumeEffect;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class BeACollector {
	public static final ComponentType<BeADoll.Variant> DOLL_VARIANT_COMPONENT = registerComponent(
		"doll_variant", builder -> builder.codec(BeADoll.Variant.CODEC).packetCodec(BeADoll.Variant.PACKET_CODEC)
	);

	public static final Item CARVING_KNIFE = registerItem("carving_knife", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).attributeModifiers(weapon(4, -2.4f))
		.component(BeACollector.DOLL_VARIANT_COMPONENT, BeADoll.Variant.WOODEN));
	public static final Item MODELING_TOOL = registerItem("modeling_tool", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).attributeModifiers(weapon(2, -1.3f))
		.component(BeACollector.DOLL_VARIANT_COMPONENT, BeADoll.Variant.CLAY));
	public static final Item SEWING_NEEDLE = registerItem("sewing_needle", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).attributeModifiers(weapon(3, -2f))
		.component(BeACollector.DOLL_VARIANT_COMPONENT, BeADoll.Variant.CLOTH));
	public static final Item FLUSH_CUTTER = registerItem("flush_cutter", DollcraftItem::new, new Item.Settings()
		.repairable(Items.IRON_INGOT).maxDamage(310).attributeModifiers(weapon(3.5f, -1.6f))
		.component(BeACollector.DOLL_VARIANT_COMPONENT, BeADoll.Variant.PLASTIC));

	public static final Item ESSENCE_FRAGMENT = registerItem("essence_fragment", Item::new, new Item.Settings()
		.maxCount(1)
		.rarity(Rarity.EPIC)
		.component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
		.component(DOLL_VARIANT_COMPONENT, BeADoll.Variant.REPRESSED)
		.component(DataComponentTypes.CONSUMABLE, new ConsumableComponent(
			3.1f, UseAction.EAT, SoundEvents.ENTITY_GENERIC_EAT, true,
			List.of(
				new ApplyEffectsConsumeEffect(new StatusEffectInstance(BeAWitch.FRAGMENTED, 7200, 5)),
				new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 0)),
				new PlaySoundConsumeEffect(RegistryEntry.of(BeABirdwatcher.ESSENCE_EAT_HEY_WAIT_WHAT_DO_YOU_MEAN_EATEN))
			)
		))
	);

	public static final Item DOLL_RIBBON = registerItem("ribbon", RibbonItem::new, new Item.Settings());



	public static void inquireAboutTheCollection() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> {
			itemGroup.addAfter(Items.BRUSH, CARVING_KNIFE, MODELING_TOOL, SEWING_NEEDLE, FLUSH_CUTTER, DOLL_RIBBON);
		});
	}



	public static Item registerItem(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
		Item item = factory.apply(settings.registryKey(key));
		if (item instanceof BlockItem blockItem) {
			blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
		}

		return Registry.register(Registries.ITEM, key, item);
	}

	public static Item registerItem(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return registerItem(key(id), factory, settings);
	}

	public static RegistryKey<Item> key(String thing) {
		return RegistryKey.of(RegistryKeys.ITEM, BeADoll.id(thing));
	}



	private static <T> ComponentType<T> registerComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
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
