from typing import Dict, Optional, Union, Literal, get_args, NamedTuple, List, Any

from mcresources import ResourceManager, utils
import json

from mcresources.type_definitions import ResourceIdentifier, JsonObject, Json, VerticalAnchor


def generate(rm: ResourceManager):

    configured_placed_feature(rm, 'erosion', 'tfcmars:erosion')
    configured_placed_feature(rm, 'rock_layers', 'tfcmars:rock_layers')
    configured_placed_feature(rm, 'meteorite', 'tfcmars:meteor', {}, decorate_chance(100), decorate_square(), decorate_heightmap('ocean_floor_wg'), decorate_biome())
    configured_placed_feature(rm, 'crater', 'tfcmars:crater', {}, decorate_chance(14), decorate_square(), decorate_heightmap('ocean_floor_wg'), decorate_biome())
    configured_placed_feature(rm, 'sandstone_boulder', 'tfcmars:boulders', {'blocks': [{'block': 'tfc:raw_sandstone/red'}]}, decorate_chance(30), decorate_square(), decorate_heightmap('ocean_floor_wg'), decorate_biome())
    configured_placed_feature(rm, 'conglomerate_boulder', 'tfcmars:boulders', {'blocks': [{'block': 'tfc:rock/raw/conglomerate'}]}, decorate_chance(30), decorate_square(), decorate_heightmap('ocean_floor_wg'), decorate_biome())
    configured_placed_feature(rm, 'basalt_boulder', 'tfcmars:boulders', {'blocks': [{'block': 'tfc:rock/raw/basalt'}]}, decorate_chance(30), decorate_square(), decorate_heightmap('ocean_floor_wg'), decorate_biome())
    configured_placed_feature(rm, 'shale_boulder', 'tfcmars:boulders', {'blocks': [{'block': 'tfc:rock/raw/shale'}]}, decorate_chance(30), decorate_square(), decorate_heightmap('ocean_floor_wg'), decorate_biome())
    configured_patch_feature(rm, 'mars_berries', patch_config('tfcmars:mars_berries', 1, 8, 16), decorate_chance(20), decorate_square(), decorate_heightmap('ocean_floor_wg'), decorate_range(0, 70), decorate_biome())
    configured_placed_feature(rm, 'loose_rocks', 'tfcmars:loose_rocks', {}, decorate_heightmap('ocean_floor_wg'))
    configured_placed_feature(rm, 'loose_rocks_patch', 'minecraft:random_patch', {'tries': 8, 'xz_spread': 7, 'y_spread': 1, 'feature': 'tfcmars:loose_rocks'}, decorate_square())
    configured_placed_feature(rm, 'lava_lake', 'minecraft:lake', {'fluid': simple_state_provider('minecraft:lava[level=0]'), 'barrier': simple_state_provider('tfc:rock/raw/basalt')}, decorate_chance(100), decorate_square(), decorate_heightmap('ocean_floor_wg'), decorate_biome())
    rm.placed_feature('lava_lake_underground', 'tfcmars:lava_lake', decorate_chance(9), decorate_square(), decorate_range(('absolute', 0), ('below_top', 0)), decorate_scan(32, 'down', scan_not_air(), scan_world_within(-5)), decorate_heightmap_threshold('ocean_floor_wg', -5), decorate_biome())

    biome(rm, 'plains')
    biome(rm, 'basin')
    biome(rm, 'mountains')

    rm.biome_tag('claystone_dust', 'tfcmars:basin')
    rm.biome_tag('sandstone_dust', 'tfcmars:plains')
    rm.biome_tag('chert_dust', 'tfcmars:mountains')

    rm.write((*rm.resource_dir, 'data', 'minecraft', 'dimension', 'overworld'), {
        'type': 'tfcmars:mars',
        'forge:use_server_seed': True,
        'generator': {
            'biome_source': {
                'biomes': [
                    biome_params('plains', cont=[0, 2], erosion=[-2, 0.6]),
                    biome_params('basin', cont=[-2, 0]),
                    biome_params('mountains', cont=[0, 2], erosion=[0.6, 2])
                ],
                'type': 'minecraft:multi_noise'
            },
            'seed': 0,
            'settings': 'tfcmars:mars',
            'type': 'minecraft:noise',
        }
    })

    # the misode visualizer needs vanilla blocks
    # so we use a template json and then copy the file over with the correct replacements.
    replace = {'minecraft:sand': 'tfc:sand/red', 'minecraft:sandstone': 'tfc:raw_sandstone/red'}
    with open('templates/mars.json', 'r', encoding='utf-8') as f:
        text = f.read()
    for k, v in replace.items():
        k = k + "\""
        v = v + "\""
        text = text.replace(k, v)
    with open('../src/main/resources/data/tfcmars/worldgen/noise_settings/mars.json', 'w', encoding='utf-8') as f:
        f.write(text)

def biome(rm: ResourceManager, name: str, temp: float = 2):
    features = [
        ['tfcmars:crater', 'tfcmars:rock_layers', 'tfcmars:erosion'],  # Raw Generation
        ['tfcmars:lava_lake_underground', 'tfcmars:lava_lake'],  # Lakes
        [],  # Local Modifications
        [],  # Underground Structures
        [*['tfcmars:%s_boulder' % b for b in ('conglomerate', 'sandstone', 'basalt', 'shale')], 'tfcmars:meteorite'],  # Surface Structures
        [],  # Strongholds
        ['tfc:vein/gravel', 'tfc:vein/gypsum', 'tfc:vein/graphite', 'tfc:vein/kaolinite', 'tfc:vein/normal_hematite', 'tfc:vein/deep_hematite', 'tfc:vein/normal_limonite', 'tfc:vein/deep_limonite'],  # Underground Ores
        [],  # Underground Decoration
        ['minecraft:spring_lava'],  # Fluid Springs (we use this for Large Features in tfc)
        ['tfcmars:mars_berries_patch', 'tfcmars:loose_rocks_patch'],  # Vegetal Decoration
        []  # Top Layer Modification
    ]

    effects = {
        'sky_color': 0xa6782e,
        'fog_color': 0xa6782e,
        'water_color': 4159204,
        'water_fog_color': 4159204
    }
    carvers = ['minecraft:cave', 'minecraft:cave_extra_underground', 'minecraft:canyon']

    rm.biome(name, features=features, effects=effects, air_carvers=carvers, temperature=temp, downfall=0, category='desert')

def biome_params(name: str = 0, temp: float | List = 0, humid: float | List = 0, cont: float | List = 0, erosion: float | List = 0, weird: float | List = 0, depth: float | List = 0, offset: float | List = 0):
    return {
        'biome': 'tfcmars:%s' % name,
        'parameters': {
            'temperature': temp,
            'humidity': humid,
            'continentalness': cont,
            'erosion': erosion,
            'weirdness': weird,
            'depth': depth,
            'offset': offset
        }
    }


Heightmap = Literal['motion_blocking', 'motion_blocking_no_leaves', 'ocean_floor', 'ocean_floor_wg', 'world_surface', 'world_surface_wg']
HeightProviderType = Literal['constant', 'uniform', 'biased_to_bottom', 'very_biased_to_bottom', 'trapezoid', 'weighted_list']

class PatchConfig(NamedTuple):
    block: str
    y_spread: int
    xz_spread: int
    tries: int
    any_water: bool
    salt_water: bool
    custom_feature: str
    custom_config: Json

def patch_config(block: str, y_spread: int, xz_spread: int, tries: int = 64, water: Union[bool, Literal['salt']] = False, custom_feature: Optional[str] = None, custom_config: Json = None) -> PatchConfig:
    return PatchConfig(block, y_spread, xz_spread, tries, water == 'salt' or water == True, water == 'salt', custom_feature, custom_config)

def configured_patch_feature(rm: ResourceManager, name_parts: ResourceIdentifier, patch: PatchConfig, *patch_decorators: Json, extra_singular_decorators: Optional[List[Json]] = None, biome_check: bool = True):
    feature = 'minecraft:simple_block'
    config = {'to_place': {'type': 'minecraft:simple_state_provider', 'state': utils.block_state(patch.block)}}
    singular_decorators = []

    if patch.any_water:
        feature = 'tfc:block_with_fluid'
        if patch.salt_water:
            singular_decorators.append(decorate_matching_blocks('tfc:fluid/salt_water'))
        else:
            singular_decorators.append(decorate_air_or_empty_fluid())
    else:
        singular_decorators.append(decorate_replaceable())

    if patch.custom_feature is not None:
        assert patch.custom_config
        feature = patch.custom_feature
        config = patch.custom_config

    heightmap: Heightmap = 'world_surface_wg'
    if patch.any_water:
        heightmap = 'ocean_floor_wg'
        singular_decorators.append(decorate_would_survive_with_fluid(patch.block))
    else:
        singular_decorators.append(decorate_would_survive(patch.block))

    if extra_singular_decorators is not None:
        singular_decorators += extra_singular_decorators
    if biome_check:
        patch_decorators = [*patch_decorators, decorate_biome()]

    res = utils.resource_location(rm.domain, name_parts)
    patch_feature = res.join() + '_patch'
    singular_feature = utils.resource_location(rm.domain, name_parts)

    rm.configured_feature(patch_feature, 'minecraft:random_patch', {
        'tries': patch.tries,
        'xz_spread': patch.xz_spread,
        'y_spread': patch.y_spread,
        'feature': singular_feature.join()
    })
    rm.configured_feature(singular_feature, feature, config)
    rm.placed_feature(patch_feature, patch_feature, *patch_decorators)
    rm.placed_feature(singular_feature, singular_feature, decorate_heightmap(heightmap), *singular_decorators)

def simple_state_provider(name: str) -> Dict[str, Any]:
    return {'type': 'minecraft:simple_state_provider', 'state': utils.block_state(name)}

def decorate_block_predicate(predicate: Json) -> Json:
    return {
        'type': 'block_predicate_filter',
        'predicate': predicate
    }


def decorate_matching_blocks(*blocks: str) -> Json:
    return decorate_block_predicate({
        'type': 'matching_blocks',
        'blocks': list(blocks)
    })


def decorate_would_survive(block: str) -> Json:
    return decorate_block_predicate({
        'type': 'would_survive',
        'state': utils.block_state(block)
    })


def decorate_would_survive_with_fluid(block: str) -> Json:
    return decorate_block_predicate({
        'type': 'tfc:would_survive_with_fluid',
        'state': utils.block_state(block)
    })

def decorate_replaceable() -> Json:
    return decorate_block_predicate({'type': 'tfc:replaceable'})

def decorate_air_or_empty_fluid() -> Json:
    return decorate_block_predicate({'type': 'tfc:air_or_empty_fluid'})

def scan_world_within(blocks: int) -> Json:
    return {
        "offset": [0, blocks, 0],
        "type": "minecraft:inside_world_bounds"
    }

def scan_not_air() -> Json:
    return {
        "predicate": {
            "blocks": "minecraft:air",
            "type": "minecraft:matching_blocks"
        },
        "type": "minecraft:not"
    }

def decorate_scan(steps: int, direction: str, *predicates: Json):
    return {
        'direction_of_search': direction,
        'type': 'minecraft:environment_scan',
        'max_steps': steps,
        'target_condition': {'predicates': [p for p in predicates], 'type': 'minecraft:all_of'}
    }

def decorate_heightmap(heightmap: Heightmap) -> Json:
    assert heightmap in get_args(Heightmap)
    return 'minecraft:heightmap', {'heightmap': heightmap.upper()}

def decorate_heightmap_threshold(heightmap: Heightmap, max_inclusive: int) -> Json:
    assert heightmap in get_args(Heightmap)
    return 'minecraft:surface_relative_threshold_filter', {'heightmap': heightmap.upper(), 'max_inclusive': max_inclusive}


def height_provider(min_y: VerticalAnchor, max_y: VerticalAnchor, height_type: HeightProviderType = 'uniform') -> Dict[str, Any]:
    assert height_type in get_args(HeightProviderType)
    return {
        'type': height_type,
        'min_inclusive': utils.as_vertical_anchor(min_y),
        'max_inclusive': utils.as_vertical_anchor(max_y)
    }

def decorate_range(min_y: VerticalAnchor, max_y: VerticalAnchor, bias: HeightProviderType = 'uniform') -> Json:
    return {
        'type': 'minecraft:height_range',
        'height': height_provider(min_y, max_y, bias)
    }


def decorate_carving_mask(min_y: Optional[VerticalAnchor] = None, max_y: Optional[VerticalAnchor] = None) -> Json:
    return {
        'type': 'tfc:carving_mask',
        'step': 'air',
        'min_y': utils.as_vertical_anchor(min_y) if min_y is not None else None,
        'max_y': utils.as_vertical_anchor(max_y) if max_y is not None else None
    }


def decorate_square() -> Json:
    return 'minecraft:in_square'


def decorate_biome() -> Json:
    return 'minecraft:biome'


def decorate_chance(rarity_or_probability: Union[int, float]) -> Json:
    return {'type': 'minecraft:rarity_filter', 'chance': round(1 / rarity_or_probability) if isinstance(rarity_or_probability, float) else rarity_or_probability}


def decorate_count(count: int) -> Json:
    return {'type': 'minecraft:count', 'count': count}



def configured_placed_feature(rm: ResourceManager, name_parts: ResourceIdentifier, feature: Optional[ResourceIdentifier] = None, config: JsonObject = None, *placements: Json):
    res = utils.resource_location(rm.domain, name_parts)
    if feature is None:
        feature = res
    rm.configured_feature(res, feature, config)
    rm.placed_feature(res, res, *placements)

