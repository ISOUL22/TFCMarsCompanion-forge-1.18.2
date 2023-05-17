from mcresources import ResourceManager


def generate(rm: ResourceManager):

    ### CRAFTING ###
    rm.crafting_shapeless('crafting/mars_token_from_block', ('tfcmars:block_of_mars_token',), '9 tfcmars:mars_token').with_advancement('tfcmars:block_of_mars_token')
    rm.crafting_shaped('crafting/block_of_mars_token', ['XXX', 'XXX', 'XXX'], {'X': 'tfcmars:mars_token'}, 'tfcmars:block_of_mars_token').with_advancement('tfcmars:mars_token')
