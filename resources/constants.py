
SIMPLE_ITEMS = ('mars_berry', 'mars_token', 'meteorite_chunk')

DEFAULT_LANG = {
    'itemGroup.tfcmarstab': 'TFC Mars Items',
    'tfcmars.tooltip.exact_time': '%s, Sol %04d',
    'tfcmars.tooltip.very_dramatic_tooltip': 'You are still very far from home.',
    'tfcmars.tooltip.room_found': 'Sealed Room found of %s blocks',
}

# this is a function that converts a thing_like_this into a Thing Like This
def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()

