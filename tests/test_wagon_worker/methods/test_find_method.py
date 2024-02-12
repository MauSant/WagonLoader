from typing import Iterable
import pytest
import sys
import re
import operator
import functools
from wagonloader.wagon_worker.methods.find_method import find



def test_simple_find_in_list():
    result = find(params=["k.v.0"],wagon_data={"k":{"v":["a","b","c","d"]}})
    expected = "a"

    assert result == expected

def test_simple_find_in_dict():
    result = find(params=["k.v"],wagon_data={"k":{"v":["a","b","c","d"]}})
    expected = ["a","b","c","d"]

    assert result == expected

def test_wrong_index_in_find():
    with pytest.raises(ValueError) as excinfo:
        result = find(params=["k.v.10"],wagon_data={"k":{"v":["a","b","c","d"]}})

    assert excinfo.type is ValueError

def test_wrong_key_in_find():
    with pytest.raises(ValueError) as excinfo:
        result = find(params=["k.b"],wagon_data={"k":{"v":["a","b","c","d"]}})
    assert excinfo.type is ValueError

if __name__ == "__main__":
    pytest.main(sys.argv)


