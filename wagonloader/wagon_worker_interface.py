from typing import Protocol
from abc import abstractmethod
from wagonloader.types import JsonType, EvaluateType

class WagonWorkerInterface(Protocol):
    wagon_data: JsonType

    @abstractmethod
    def evaluate(self, node:EvaluateType) -> JsonType:
        ...

    def update_wagon_data(self, wagon_data:JsonType) -> None:
        self.wagon_data = wagon_data
    



