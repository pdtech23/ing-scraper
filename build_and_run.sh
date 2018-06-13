#!/bin/sh

mvn clean package

mvn exec:java -D exec.mainClass=scrapper.Main
