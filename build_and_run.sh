#!/bin/sh

mvn clean package

mvn exec:java -D exec.mainClass=scraper.Main
