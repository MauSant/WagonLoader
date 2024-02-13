import pytest
import sys
from wagonloader.wagon_loader import fill_payload
from wagonloader.wagon_worker.wagon_worker_default import WagonWorkerDefault
from wagonloader.wagon_worker.methods.find_method import find

@pytest.fixture
def wagon_data():
    return {
        "nest_dict1":{
            "nest_dict2":{
                "nest_dict3":"value",
            }
        },
        "nest_list":[1,2,["a","b", ["c"]] ],
        "simple":"value1",
        "nest_dict_recursion":"nest_dict1"

    }

@pytest.mark.parametrize("node, expected",
                        [
                            ("FIND(simple)", "value1"),
                            ("FIND(nest_dict1.nest_dict2)", {"nest_dict3":"value"}),
                            ("FIND(nest_list)", [1,2,["a","b", ["c"]] ]),
                            ("FIND(nest_list.2.2.0)", "c"),
                            ("FIND(FIND(nest_dict_recursion))", {"nest_dict2":{"nest_dict3":"value"}}),
                        ] 
)
def test_simple_fill_payload(node, expected, wagon_data:dict):
    wagon_worker_default = WagonWorkerDefault()
    wagon_worker_default.update_wagon_data(wagon_data)
    wagon_worker_default.upload_method(method_key="FIND", method_callable=find)


    result = fill_payload(node, wagon_worker_default)
    assert result == expected

if __name__ == "__main__":
    pytest.main(sys.argv)