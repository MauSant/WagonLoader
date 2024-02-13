import pytest
import sys
from wagonloader.wagon_worker.methods.concat_method import concat



def test_simple_concat():
    result = concat(["nest_dict1",".","nest_dict2"], {})
    expected = "nest_dict1.nest_dict2"
    assert result == expected


def test_complicated_concat():
    result = concat(["mongodb://", "JohnSmith", ":", "#*¨%#!),notAMethod(@#¨GHSA,DJH*&¨!(#)_)", "@host"], {})
    expected = "mongodb://JohnSmith:#*¨%#!),notAMethod(@#¨GHSA,DJH*&¨!(#)_)@host"
    assert result == expected

if __name__ == "__main__":
    pytest.main(sys.argv)


