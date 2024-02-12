from typing import Iterable
import pytest
import sys
import re
import operator
import functools

def wagon_get_item(sequence, index:str):
    try:
        if isinstance(sequence,list):
            index = int(index)
    except Exception as e:
        print("index is not a integer!")

    return operator.getitem(sequence, index)


def test_simple_reduce():
    listt = functools.reduce(wagon_get_item, "k.v.0".split("."), {"k":{"v":[1,2]}})
    print()


if __name__ == "__main__":
    pytest.main(sys.argv)


