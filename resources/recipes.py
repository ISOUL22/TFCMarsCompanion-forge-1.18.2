from mcresources import ResourceManager, utils
from constants import *

def generate(rm: ResourceManager):

    ### CRAFTING ###
    rm.crafting_shapeless('crafting/mars_token_from_block', ('tfcmars:block_of_mars_token',), '9 tfcmars:mars_token').with_advancement('tfcmars:block_of_mars_token')
    rm.crafting_shaped('crafting/block_of_mars_token', ['XXX', 'XXX', 'XXX'], {'X': 'tfcmars:mars_token'}, 'tfcmars:block_of_mars_token').with_advancement('tfcmars:mars_token')

    ### LANDSLIDE ###
    for dust in DUST_TYPES:
        for variant in DUST_VARIANTS:
            basic_landslide(rm, '%s_%s' % (dust, variant), 'tfcmars:soil/%s_%s' % (dust, variant))

def basic_landslide(rm: ResourceManager, name: str, block: str):
    rm.block_tag('tfc:can_landslide', block)
    landslide_recipe(rm, name, block, block)

def landslide_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, result: utils.Json):
    rm.recipe(('landslide', name_parts), 'tfc:landslide', {
        'ingredient': ingredient,
        'result': result
    })
