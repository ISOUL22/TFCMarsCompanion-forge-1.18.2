from mcresources import ResourceManager
from constants import *

from typing import Tuple, Any, Dict

def generate(rm: ResourceManager):

    block = rm.blockstate('block_of_mars_token').with_block_model(textures='tfcmars:block/block_of_mars_token')
    block.with_item_model().with_lang(lang('block of mars token')).with_tag('minecraft:mineable/pickaxe').with_block_loot('tfcmars:block_of_mars_token')

    block = rm.blockstate('mars_berries', variants=four_rotations('tfcmars:block/mars_berries', (90, None, 180, 270)))
    block.with_lang(lang('mars berries')).with_tag('minecraft:mineable/pickaxe').with_block_loot('1-3 tfcmars:mars_berry')
    rm.item_model('mars_berries', parent='tfcmars:block/mars_berries', no_textures=True)

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
