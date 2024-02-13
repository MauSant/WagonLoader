import functools
import operator
from typing import Sequence
from wagonloader.types import JsonType


def concat(params:list[str], wagon_data:JsonType) -> JsonType:
    return "".join(params)

