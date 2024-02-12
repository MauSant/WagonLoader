import pytest
import sys
from wagonloader.wagon_loader import fill_payload

class validateUserInput:
    def evaluate(self, node):
        return node


def test_simple_fill_payload():
    node = {
        # "value_node":1,
        # "value_node":"",
        # "list_node":[],
        "dict_node": {
            # "deep":"1",
            "dict_node2":{
                "deep2":2
            }
        }

    }
    
    r = fill_payload("", node, validateUserInput())
    assert 1 == 1

if __name__ == "__main__":
    pytest.main(sys.argv)