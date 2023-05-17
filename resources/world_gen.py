from typing import Dict

from mcresources import ResourceManager, utils
import json


def generate(rm: ResourceManager):

    rm.biome('plains')

    rm.write((*rm.resource_dir, 'data', 'minecraft', 'dimension', 'overworld'), {
        'type': 'tfcmars:mars',
        'forge:use_server_seed': True,
        'generator': {
            'biome_source': {
                'biomes': [
                    biome_params('plains')
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
        [],  # Raw Generation
        ['minecraft:lava_lake_underground', 'minecraft:lava_lake_surface'],  # Lakes
        [],  # Local Modifications
        [],  # Underground Structures
        [],  # Surface Structures
        [],  # Strongholds
        [],  # Underground Ores
        [],  # Underground Decoration
        ['minecraft:spring_lava'],  # Fluid Springs (we use this for Large Features in tfc)
        [],  # Vegetal Decoration
        []  # Top Layer Modification
    ]

    effects = {
        'sky_color': 0xa6782e,
        'fog_color': 0xa6782e,
        'water_color': 4159204,
        'water_fog_color': 4159204
    }
    carvers = ['minecraft:cave', 'minecraft:cave_extra_underground', 'minecraft:canyon']
    spawners = {
        'monster': {
            'type': 'minecraft:zombie',
            'weight': 95,
            'minCount': 4,
            'maxCount': 4
        }
    }

    rm.biome(name, features=features, effects=effects, air_carvers=carvers, spawners=spawners, temperature=temp, downfall=0, category='desert')

def biome_params(name: str = 0, temp: float = 0, humid: float = 0, cont: float = 0, erosion: float = 0, weird: float = 0, depth: float = 0, offset: float = 0):
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

def load(fn: str):
    with open(fn, 'r', encoding='utf-8') as f:
        return json.load(f)

