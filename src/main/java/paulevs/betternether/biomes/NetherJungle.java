package paulevs.betternether.biomes;

import java.util.Random;
import java.util.function.BiFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import paulevs.betternether.BlocksHelper;
import paulevs.betternether.registry.NetherBlocks;
import paulevs.betternether.registry.NetherEntities;
import paulevs.betternether.registry.SoundsRegistry;
import paulevs.betternether.structures.StructureType;
import paulevs.betternether.structures.plants.StructureBlackVine;
import paulevs.betternether.structures.plants.StructureBloomingVine;
import paulevs.betternether.structures.plants.StructureEggPlant;
import paulevs.betternether.structures.plants.StructureEye;
import paulevs.betternether.structures.plants.StructureFeatherFern;
import paulevs.betternether.structures.plants.StructureGoldenVine;
import paulevs.betternether.structures.plants.StructureJellyfishMushroom;
import paulevs.betternether.structures.plants.StructureJungleMoss;
import paulevs.betternether.structures.plants.StructureJunglePlant;
import paulevs.betternether.structures.plants.StructureLucis;
import paulevs.betternether.structures.plants.StructureMagmaFlower;
import paulevs.betternether.structures.plants.StructureReeds;
import paulevs.betternether.structures.plants.StructureRubeus;
import paulevs.betternether.structures.plants.StructureRubeusBush;
import paulevs.betternether.structures.plants.StructureStalagnate;
import paulevs.betternether.structures.plants.StructureWallBrownMushroom;
import paulevs.betternether.structures.plants.StructureWallMoss;
import paulevs.betternether.structures.plants.StructureWallRedMushroom;
import ru.bclib.api.biomes.BCLBiomeBuilder;

public class NetherJungle extends NetherBiome {
	public static class Config extends NetherBiomeConfig {
		public Config(String name) {
			super(name);
		}
		
		@Override
		protected void addCustomBuildData(BCLBiomeBuilder builder) {
			builder.fogColor(62, 169, 61)
				   .loop(SoundsRegistry.AMBIENT_NETHER_JUNGLE)
				   .additions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				   .mood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				   //TODO: 1.18 Surface Blocks
				   //.surface(NetherBlocks.JUNGLE_GRASS)
				   .spawn(NetherEntities.JUNGLE_SKELETON, 40, 2, 4)
				   .structure(NetherBiomeBuilder.VANILLA_STRUCTURES.getBASTION_REMNANT());;
		}
		
		@Override
		public BiFunction<ResourceLocation, Biome, NetherBiome> getSupplier() {
			return NetherJungle::new;
		}
		
		@Override
		public boolean spawnVanillaMobs() {
			return false;
		}
	}
	
	public NetherJungle(ResourceLocation biomeID, Biome biome) {
		super(biomeID, biome);
		
		addStructure("nether_reed", new StructureReeds(), StructureType.FLOOR, 0.5F, false);
		addStructure("stalagnate", new StructureStalagnate(), StructureType.FLOOR, 0.2F, false);
		addStructure("rubeus_tree", new StructureRubeus(), StructureType.FLOOR, 0.1F, false);
		addStructure("bush_rubeus", new StructureRubeusBush(), StructureType.FLOOR, 0.1F, false);
		addStructure("magma_flower", new StructureMagmaFlower(), StructureType.FLOOR, 0.5F, false);
		addStructure("egg_plant", new StructureEggPlant(), StructureType.FLOOR, 0.05F, true);
		addStructure("jellyfish_mushroom", new StructureJellyfishMushroom(), StructureType.FLOOR, 0.03F, true);
		addStructure("feather_fern", new StructureFeatherFern(), StructureType.FLOOR, 0.05F, true);
		addStructure("jungle_plant", new StructureJunglePlant(), StructureType.FLOOR, 0.1F, false);
		addStructure("lucis", new StructureLucis(), StructureType.WALL, 0.1F, false);
		addStructure("eye", new StructureEye(), StructureType.CEIL, 0.1F, true);
		addStructure("black_vine", new StructureBlackVine(), StructureType.CEIL, 0.1F, true);
		addStructure("golden_vine", new StructureGoldenVine(), StructureType.CEIL, 0.1F, true);
		addStructure("flowered_vine", new StructureBloomingVine(), StructureType.CEIL, 0.1F, true);
		addStructure("jungle_moss", new StructureJungleMoss(), StructureType.WALL, 0.8F, true);
		addStructure("wall_moss", new StructureWallMoss(), StructureType.WALL, 0.2F, true);
		addStructure("wall_red_mushroom", new StructureWallRedMushroom(), StructureType.WALL, 0.8F, true);
		addStructure("wall_brown_mushroom", new StructureWallBrownMushroom(), StructureType.WALL, 0.8F, true);

		addStructures(structureFormat("ruined_temple", -4, StructureType.FLOOR, 10F));
		addStructures(structureFormat("jungle_temple_altar", -2, StructureType.FLOOR, 10F));
		addStructures(structureFormat("jungle_temple_2", -2, StructureType.FLOOR, 10F));

		addStructures(structureFormat("jungle_bones_1", 0, StructureType.FLOOR, 20F));
		addStructures(structureFormat("jungle_bones_2", 0, StructureType.FLOOR, 20F));
		addStructures(structureFormat("jungle_bones_3", 0, StructureType.FLOOR, 20F));

		this.setNoiseDensity(0.5F);
	}

	@Override
	public void genSurfColumn(LevelAccessor world, BlockPos pos, Random random) {
		BlocksHelper.setWithoutUpdate(world, pos, NetherBlocks.JUNGLE_GRASS.defaultBlockState());
	}
}