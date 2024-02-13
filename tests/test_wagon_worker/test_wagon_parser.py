from typing import Iterable
import pytest
import sys
from wagonloader.wagon_worker.wagon_parser import (
    create_pattern,
    split_parser,
    _apostrophee_marker
)
import re

def test_simple_create_pattern():
    pattern: re.Pattern = create_pattern(r'\bf[a-z]*')
    result:list = pattern.findall("which foot or hand fell fastest")
    expected = ['foot', 'fell', 'fastest']
    assert result == expected
    
@pytest.mark.parametrize("inputt, expected",
                        [
                            ("AA('33,34',F(aaa,'oi, tudo bem?'),77)", [(3,9),(17,31)]),
                            ("'full f bis'", [(0,11)]),
                            ("X(a,b), Z, Y()", [])
                        ] 
)
def test_apostrophee_pattern(
        inputt,
        expected
):
    result = _apostrophee_marker(inputt)
    assert result == expected


@pytest.mark.parametrize("inputt, expected",
                        [
                            ("X(a,b), Z, Y()", ["X(a,b)", "Z", "Y()"]),
                            ("X, Z, Y", ["X", "Z","Y"]),
                            ("X", ["X"]),
                            ("X, Z(a,b), Y(c,d)", ["X", "Z(a,b)", "Y(c,d)"]),
                            ("X, Z, Y(c,d)", ["X","Z","Y(c,d)"]),
                            ("Z(F(),K(a,L(a,b)))", ["Z(F(),K(a,L(a,b)))"]),
                            ("AA(''),77", ["AA('')","77"]),
                            ("AA('full f bis', F(oi)),   77", ["AA('full f bis', F(oi))","77"]),
                            ("AA('(a)3#$%*&¨@*¨%@(/))', F(oi)), 77", ["AA('(a)3#$%*&¨@*¨%@(/))', F(oi))","77"]),
                            ("'a&8)())))(((', 'second aa', 'third$7384-\/\/\/'", ["a&8)())))(((", "second aa","third$7384-\/\/\/"])

                        ] 
)
def test_simple_split_parser(inputt, expected):
    result = split_parser(inputt)
    assert result == expected


if __name__ == "__main__":
    pytest.main(sys.argv)


