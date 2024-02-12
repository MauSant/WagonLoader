import functools
import operator
from typing import Sequence

def wagon_get_item(sequence:Sequence, index:str):
    try:
        if isinstance(sequence,list):
            index = int(index)
    except Exception as e:
        print("index is not a integer!")

    return operator.getitem(sequence, index)


def find(params:list[str], wagon_data:dict|list|str) -> str:
    if len(params) > 1:
        raise ValueError("Too many parameters for method FIND")
    elif len(params) == 0:
        raise ValueError("Too few parameters for method FIND")
    
    str(params[0])

    return functools.reduce(wagon_get_item, params[0], wagon_data)

