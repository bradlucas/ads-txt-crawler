#!/bin/bash

# Build a new database
sqlite3 ads-txt.db < ./sql/create.sql


rm -f error.log


java -jar ./target/uberjar/ads-txt-crawler-standalone.jar -t ./doc/top-100-programmatic-domains.txt -d ads-txt.db 2>error.log

# Show results
echo 'select * from adstxt;' | sqlite3 ads-txt.db
echo "done"



