
SIMPLE_ITEMS = ('mars_berry', 'mars_token')

DEFAULT_LANG = {
    'itemGroup.tfcMarsTab': 'TFC Mars Items',
}

# this is a function that converts a thing_like_this into a Thing Like This
def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()

