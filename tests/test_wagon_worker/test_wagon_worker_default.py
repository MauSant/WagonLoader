import pytest
import sys
from wagonloader.wagon_worker.wagon_worker_default import WagonWorkerDefault



def test_simple_evaluate():
    wagon_worker = WagonWorkerDefault()
    result:str = wagon_worker.evaluate("FIND")
    expected = "FIND"
    assert result == expected

def test_upload_wagon_data():
    wagon_worker = WagonWorkerDefault()
    wagon_worker.upload_wagon_data({"wagon":"data"})
    assert wagon_worker.wagon_data == {"wagon":"data"}


# def test_evaluate_no_match():
#     result:str = evaluate("FIND")
#     expected = "FIND"
#     assert result == expected

if __name__ == "__main__":
    pytest.main(sys.argv)