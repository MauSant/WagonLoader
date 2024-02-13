import json
from wagonloader.wagon_worker_interface import WagonWorkerInterface
from wagonloader.types import JsonType

def fill_payload(node:JsonType, wagon_worker:WagonWorkerInterface) -> JsonType:

    if type(node) is dict:
        big_node = {}

        for key, value in node.items():
            result = fill_payload("", value, wagon_worker)
            big_node[key] = result
        
        return big_node

    elif type(node) is list:
        big_list = []
        for item in node:
            result_two = fill_payload("", item, wagon_worker)
            big_list.append(result_two)
        return big_list
    
    else:
        return wagon_worker.evaluate(node)
    