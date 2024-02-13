import re
from  typing import Any, Protocol, Callable

from wagonloader.wagon_worker.wagon_parser import create_pattern, split_parser
from wagonloader.wagon_worker_interface import WagonWorkerInterface
from wagonloader.types import JsonType, EvaluateType

MethodType = Callable[[Any, JsonType], str]


class WagonWorkerDefault(WagonWorkerInterface):
    workers_records: dict[str,MethodType] = {}
    wagon_data: JsonType

    def evaluate(self, node: EvaluateType) -> JsonType:
        #Transforms the node into a string
        try:
            node = str(node)
        except Exception as e:
            print("Error when trying to transform node into string")
            raise
        
        #Regex splitting the node
        pattern:re.Pattern = create_pattern("^([A-Z]+(?:_[A-Z]+)*)\\((.*?)\\)$")
        matcher: re.Match = pattern.match(node)
        if matcher is None:
            print("No match! Simple node, return it")
            return node

        method_key = matcher.group(1)

        multiple_params:list[str] = split_parser(matcher.group(2))

        #Evaluates each of the multiple params
        params = [self.evaluate(single_param) for single_param in multiple_params]
        
        #Call for the method
        return self.call_method(method_key=method_key, params=params)

    def call_method(self, method_key:str, params:list[str]) -> JsonType:
        method:MethodType = self.workers_records[method_key]
        return method(params, self.wagon_data)

    def upload_method(self, method_key:str, method_callable:MethodType) -> None:
        self.workers_records[method_key] = method_callable

    def upload_multiple_methods(self, methods:tuple[str,MethodType]) -> None:
        for m in methods: #Todo test if we can unpack
            self.upload_method(method_key=m[0], method_callable=m[1])
    