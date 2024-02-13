import pytest
import sys
from wagonloader.wagon_worker.wagon_worker_default import WagonWorkerDefault
from wagonloader.wagon_worker.methods.find_method import find
from wagonloader.wagon_worker.methods.concat_method import concat



def test_simple_evaluate():
    wagon_worker = WagonWorkerDefault()
    result:str = wagon_worker.evaluate("FIND")
    expected = "FIND"
    assert result == expected

def test_upload_wagon_data():
    wagon_worker = WagonWorkerDefault()
    wagon_worker.update_wagon_data({"wagon":"data"})
    assert wagon_worker.wagon_data == {"wagon":"data"}

@pytest.mark.parametrize("inputt, expected",
                        [
                            ({"wagon_data":{"key1":"value1"},"node":"FIND(key1)"}, "value1"),
                            ({"wagon_data":{"key1":"key2","key2":"value2"},"node":"FIND(FIND(key1))"}, "value2"),
                            (
                                {"wagon_data":{"username":"JohnSmith"},
                                    "node":"CONCAT(mongodb://, FIND(username), :,'#*¨%#!),notAMethod(@#¨GHSA,DJH*&¨!(#)_)', @host)"
                                },
                                "mongodb://JohnSmith:#*¨%#!),notAMethod(@#¨GHSA,DJH*&¨!(#)_)@host"
                            ),
                        ] 
)
def test_evaluate_recursion(inputt, expected):
    wagon_worker = WagonWorkerDefault()
    wagon_worker.update_wagon_data(inputt["wagon_data"])
    wagon_worker.upload_method(method_key="FIND", method_callable=find)
    wagon_worker.upload_method(method_key="CONCAT", method_callable=concat)


    result:str = wagon_worker.evaluate(inputt["node"])
    assert result == expected



if __name__ == "__main__":
    pytest.main(sys.argv)