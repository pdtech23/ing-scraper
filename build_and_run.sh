#!/bin/sh

mvn clean package

mvn exec:java -D exec.mainClass=challenge.Main
