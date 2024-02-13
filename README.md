# WagonLoader

DSL for easy transformations of Json using contexts.


# Overview
The Wagon Loaderis a DSL that enables programers to dynamically transform a JSON using a given context data.

So the folowing JSON, would tell WagonLoader to find the value behind the key `all_books` inside a given context
  ```json
  Request Json
  {
    "id":"iasd9812563",
    "user":"beltrano",
    "books":"FIND(all_books)"
  }
  ``` 

  ```json
  Context Data
  {
    "all_books":["Lost paradise", "Dracula", "Five Rings"]
  }
  ``` 
  And the output would be:
  ```json
  Output Json
  {
    "id":"iasd9812563",
    "user":"beltrano",
    "books":["Lost paradise", "Dracula", "Five Rings"]
  }
  ``` 

  # Usage:
  In order to use the WagonLoader, you need to follow the steps:
  1. Create Wagon Worker (Or use the default one in this repo)
  2. Update WagonWorker with Wagon Data (context)
  3. Use the fill_payload function

  ```python
  from wagonloader.wagon_loader import fill_payload

  node = {
    "key1":"value1
  }

  wagon_worker = Worker()
  # Wagon Worker code (Check next section)

  output:json = fill_payload(node, wagon_worker)

  ```


  ## Wagon Worker
  The responsible for interpreting the methods of the DSl. For example is at this class that 
  ```json
  `FIND(all_books)`
  ```
   is associate with the value:
  ```json
  ["Lost paradise", "Dracula", "Five Rings"]
  ```
  
  ### Using Default Wagon Worker
  The WagonLoader has a default Wagon Worker with some capabilities. 
  In order to use it you need to:
  1. import
  2. Instanciate
  3. Update the wagon_data (The context)
  4. Upload methods

  The methods are callables that will be executed everytime the pattern METHOD(arg1, arg2) is found in the request json. 
  
  Below is an example on how to use it:

  ```
    from wagonloader.wagon_worker.wagon_worker_default import WagonWorkerDefault # step1
    from wagonloader.wagon_worker.methods.find_method import find


    context = {
      "key1":"value1"
    }

    wagon_worker = WagonWorkerDefault() # step2
    wagon_worker.update_wagon_data(context) # step3
    wagon_worker.upload_method(method_key="FIND", method_callable=find) # step4
  ```
  If you want to expand the Wagon Worker with more methods, you can do so with the class method `upload_method()`. There are 2 requirements necessary:
  1.  `method_key` must be uppercase and optionally have underscores, for example:  

    FIND [Ok]  
    CONCAT [Ok]  
    BUILD_URL [Ok]  
    _FIND [Nok]  
    FIND_ [Nok]  
    find [Nok]  
    FIND_some [Nok]  

  The Wagon Worker Default follows the REGEX (It cant be changed by User yet):  

  ```re
  ^([A-Z]+(?:_[A-Z]+)*)\((.*?)\)$
  ```
  2.  method_callable must follow the type MethodType: 
  ```python

  JsonType = None | int | str | bool | list["JsonType"] | dict[str, "JsonType"]

  MethodType = Callable[[list[str], JsonType], JsonType]
  ```

  Which means takes 2 arguments, the first must be a list of strings and the second must be a JsonType defined above.

  #### Default Methods
  A few default methods are already built within the Wagon Loader. 

  ##### Recursive note
  All Methods with the default Wagon Worker are recusive, in such way that FIND(CONCAT(hi, FIND(greeting))) would work with the appropriate context.

  ##### FIND
  - Takes only 1 argument, a string containing alphanumeric characters
  - It can be use to get through nested dictionaries or lists
  ###### Examples
  ```
  Example 1
  Getting value from nested dictionary

  wagon_data = {
    "key1":{
      "key2":{
        "key3":value
      }
    }
  }


  FIND(key1.key2.key3) -> returns `value`
  ```


  ```
  Example 2
  Getting value from nested dictionary and nested list
  
  wagon_data = [
    "key1":{
      "key2":[
        {
          "key3":"value"
        }
      ]
    }
  ]


  FIND(0.key1.key2.0.key3) -> returns `value`
  ```

  ##### CONCAT
  Concatenate every argument. Every argument must be able to transform into a string
  ```
  CONCAT(nest_dict1, . , nest_dict2) -> "nest_dict1.nest_dict2"
  ```

  ### Creating you own Wagon Worker
  
  In order to create a Wagon worker, you must follow the WagonWorkerInterface, which is a python Protocol

  ```python
  from wagonloader.wagon_worker_interface import WagonWorkerInterface

  class WagonWorkerDefault(WagonWorkerInterface):
    ...

    def evaluate(self, node) -> dict|list|str:
      ...
  ```

  ### Notes
  
  #### Use of '' for text or passwords
  The following use is allowed in the request json
  ``` 
  Request Json:
  {
    "key1": "CONCAT('ghost in the ', Shell)"
  }

  Output Json:
  {
    "key1": "ghost in the Shell"
  }
  ```
  Or building a string with passwords that have complicated charcaters:
  ```
  Request Json                   Arg1                Arg2                 Arg3                    Arg4
  {                 --------------------------------  -- --------------------------------------   -----
    "mongo_string":"CONCAT(mongodb://, FIND(username), :,'#*¨%#!),notAMethod(@#¨GHSA,DJH*&¨!(#)_)', @host)"
  }


  Output Json
  {
    "mongo_string": "mongodb://JohnSmith:#*¨%#!),notAMethod(@#¨GHSA,DJH*&¨!(#)_)@host"
  }
  ```



