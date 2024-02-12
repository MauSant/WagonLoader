from typing import Protocol
from abc import abstractmethod

class WagonWorkerInterface(Protocol):
    wagon_data: dict|list|str

    @abstractmethod
    def evaluate(self, node:str) -> str:
        ...

    def update_wagon_data(self, wagon_data:dict|list|str) -> None:
        self.wagon_data = wagon_data
    



