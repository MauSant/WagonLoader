import re
from functools import lru_cache
from typing import Iterator

@lru_cache(maxsize=30)
def create_pattern(regex:str) ->  re.Pattern:
    return re.compile(regex)


def split_parser(sentence: str) -> list[str]:
    
    def is_in_apostrophee_range(
        apostrophee_ranges:list[tuple[int]],
        index:int
    ) -> bool:
        #Is the index part of apostrophee string eg: 'Hey, How are you'
        for r in apostrophee_ranges:
            if  r[0] <= index <= r[1]:
                return True
        return False

    
    p_count = 0
    begin_index = 0
    outer_commas = []
    multiple_params = []

    #Mark the setence index where we have strings inside apostrophee
    apostrophee_ranges:list[tuple[int]] = _apostrophee_marker(sentence)
    
    
    for index, char in enumerate(sentence):
        if is_in_apostrophee_range(apostrophee_ranges, index):
            continue
        
        #Counting open and closing parenthesis
        if char == '(':
            p_count = p_count + 1
        elif char == ')':
            p_count = p_count - 1

        #If inside or none-a-parenthesis mark comma
        if (char == ",") and (p_count == 0):
            outer_commas.append(index)
            
    outer_commas.append(len(sentence))

    for i in outer_commas:
        param = sentence[begin_index:i]
        param = param.strip().strip(",").strip()
        multiple_params.append(param)
        begin_index = i

    return multiple_params

def _apostrophee_marker(sentence:str)-> list[tuple[int]]:
    pattern = create_pattern("'(.*?)'")
    match_iter:Iterator = pattern.finditer(sentence)
    return [(m.start(),m.end()-1) for m in match_iter]
     