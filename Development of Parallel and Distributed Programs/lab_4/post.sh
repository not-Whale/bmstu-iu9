#!/bin/bash

curl -H { 'Content-Type: application/json' --data '{"packageId":"11", "jsScript": "var divideFn = function(a,b) {return a/b}", "functionName": "divideFn", "tests": [ {"testName": "test1", "expectedResult": "2.0", "params": [2,1] }, {"testName": "test2", "expectedResult": "2.0", "params": [4,2] } ] }' https://localhost:8000
