#!/bin/bash

# Run from the project root as ./scripts/run-100.sh
rm -f results.csv
rm -f error.log
rm -f domains-with-ads-txt.txt
rm -f domains-without-ads-txt.txt

java -jar "./target/uberjar/ads-txt-crawler-standalone.jar" -t ./doc/top-100-programmatic-domains.txt >results.csv 2>error.log


# Build list of domains with Ads.txt files
cat results.csv | cut -d, -f1 | sort | uniq > domains-with-ads-txt.txt

# Build list of domains without Ads.txt files
cat error.log | sed "s/.*http:\/\///" | cut -d/ -f1 | uniq | sort > domains-without-ads-txt.txt




