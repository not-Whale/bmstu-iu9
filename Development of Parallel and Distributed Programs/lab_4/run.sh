#!/bin/bash

mvn package

java -jar target/classes/JSTester.jar

bash post.sh
bash get.sh
