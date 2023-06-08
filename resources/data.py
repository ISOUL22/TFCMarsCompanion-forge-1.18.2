from mcresources import ResourceManager


def generate(rm: ResourceManager):

    ### BLOCK TAGS
    rm.block_tag('base_insulation', 'tfcmars:standard_casing', 'minecraft:glass')

    ### ITEM TAGS
    rm.item_tag('spacesuit', 'minecraft:iron_helmet', 'minecraft:iron_chestplate', 'minecraft:iron_leggings', 'minecraft:iron_boots')

