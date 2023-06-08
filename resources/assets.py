from mcresources import ResourceManager
from constants import *

from typing import Tuple, Any, Dict

def generate(rm: ResourceManager):

    block = rm.blockstate('mars_berries', variants=four_rotations('tfcmars:block/mars_berries', (90, None, 180, 270)))
    block.with_lang(lang('mars berries')).with_tag('minecraft:mineable/pickaxe').with_block_loot('1-3 tfcmars:mars_berry')
    rm.item_model('mars_berries', parent='tfcmars:block/mars_berries', no_textures=True)

    block = rm.blockstate('vent', variants=four_rotations('tfcmars:block/vent', (90, None, 180, 270)))
    rm.block_model('vent', parent='minecraft:block/orientable', textures={'side': 'tfcmars:block/gas_pipe', 'top': 'tfcmars:block/gas_pipe', 'front': 'tfcmars:block/vent'})
    block.with_lang(lang('vent')).with_tag('minecraft:mineable/pickaxe').with_block_loot('tfcmars:vent')
    rm.item_model('vent', parent='tfcmars:block/vent', no_textures=True)

    for block, tag, loot in (
        ('meteorite', 'minecraft:mineable/pickaxe', '1-3 tfcmars:meteorite_chunk'),
        ('standard_casing', 'tfc:mineable_with_sharp_tool', 'tfcmars:standard_casing'),
        ('pressurizer', 'minecraft:mineable/pickaxe', 'tfcmars:pressurizer'),
        ('block_of_mars_token', 'minecraft:mineable/pickaxe', 'tfcmars:block_of_mars_token')
    ):
        rm.blockstate(block).with_block_model().with_lang(lang(block)).with_item_model().with_tag(tag).with_block_loot(loot)

    for dust in DUST_TYPES:
        for variant in DUST_VARIANTS:
            block = 'soil/%s_%s' % (dust, variant)
            rm.blockstate(block).with_block_model().with_lang(lang(block)).with_item_model().with_tag('minecraft:mineable/shovel').with_block_loot('tfcmars:%s' % block).with_tag('minecraft:sand')

    for name in SIMPLE_ITEMS:
        rm.item_model(name).with_lang(lang(name))

    # populates the default lang entries
    for key, value in DEFAULT_LANG.items():
        rm.lang(key, value)


def four_rotations(model: str, rots: Tuple[Any, Any, Any, Any], suffix: str = '', prefix: str = '') -> Dict[str, Dict[str, Any]]:
    return {
        '%sfacing=east%s' % (prefix, suffix): {'model': model, 'y': rots[0]},
        '%sfacing=north%s' % (prefix, suffix): {'model': model, 'y': rots[1]},
        '%sfacing=south%s' % (prefix, suffix): {'model': model, 'y': rots[2]},
        '%sfacing=west%s' % (prefix, suffix): {'model': model, 'y': rots[3]}
    }
