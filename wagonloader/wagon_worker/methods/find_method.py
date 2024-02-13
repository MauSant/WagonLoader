import functools
import operator
from typing import Sequence

def wagon_get_item(sequence:Sequence, index:str) -> dict|list|str:
    try:
        if isinstance(sequence,list):
            index = int(index)
    except ValueError as e:
        raise ValueError(f"Error when trying to access list index with the string `{index}`.")

    try:
        return operator.getitem(sequence, index)
    except IndexError as e:
        raise ValueError(f"The path does not exist in the data. index `{index}` has issues")
    except KeyError as e:
        raise ValueError(f"The path does not exist in the data. key `{index}` has issues")


def find(params:list[str], wagon_data:dict|list|str) -> dict|list|str:
    if len(params) > 1:
        raise ValueError("Too many parameters for method FIND")
    elif len(params) == 0:
        raise ValueError("Too few parameters for method FIND")
    
    str(params[0])

    return functools.reduce(wagon_get_item, params[0].split("."), wagon_data)

