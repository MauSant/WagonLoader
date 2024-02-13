
from typing import Dict, List, Union, Callable, Any, Protocol


JsonType = None | int | str | bool | list["JsonType"] | dict[str, "JsonType"]

EvaluateType = None | int | str | bool

# class MethodType(Protocol):
#     def __call__(self, params: list[str], wagon_data:JsonType) -> JsonType: ...

MethodType = Callable[[list[str], JsonType], JsonType]


