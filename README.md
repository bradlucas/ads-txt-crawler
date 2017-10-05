# Ads-txt-crawler

An implementation of a crawler for Ads.txt files written in Clojure.

## Ads.txt Files

IAB Tech Lab released a specification for Ads.txt files. See [https://iabtechlab.com/ads-txt/](https://iabtechlab.com/ads-txt/).

Along with the specification they released a reference crawler written in Python. The repository for that project is [https://github.com/InteractiveAdvertisingBureau/adstxtcrawler](https://github.com/InteractiveAdvertisingBureau/adstxtcrawler).

This project demonstrates a crawler for Ads.txt files written in Clojure. As of this writing this project differs slighly from the Python project. The Python project defaults to saving it's data to a SQLite database. This project defaults to sending it's output to STDOUT and it's errors to STDERR . The project does support saving it's data to a SQLite database but it is optional. 


## Installation

This project has optional support for saving data to a local SQLite database. To facilitate this install `sqlite3'.

Then to create the initial database run the following command.

```
$ sqlite3 database.db < ./sql/create.sql
```

## Build

Build the project with the `lein uberjar` command.

```
$ lein uberjar
```

## Usage

```
$ java -jar ads-txt-crawler-standalone.jar [options]

Options:
          -t FILE, --targets=FILE
                                   list of domains to crawler ads.txt from

          -d FILE, --database=FILE
                                   database to dump crawled data into
```

The `targets` file is required and the `database` file is optional. If you do not submit a database name the program will output it's data to STDOUT and it's errors to STDERR.


## Targets File

The targets file is simply a list of domains and URLS to crawl. For each line the crawler will extract the domain and make a request to `http://DOMAIN/ads.txt`.

The data returned will be parsed to ignore blank and commented lines. Each valid line will be parsed according to the Ads.txt specification.

## Example without output to STDOUT and STDERR

After building the project using `lein uberjar` pass the example `target-domains.txt` file included in the docs directory using the `-t` flag.

```
$ java -jar ./target/uberjar/ads-txt-crawler-standalone.jar -t ./doc/target-domains.txt

```

For another larger example, see the file [./doc/top-100-programmatic-domains.txt](top-100-programmatic-domains.txt) in the doc directory.

To run this file you can either run the following command.

```
$ java -jar ./target/uberjar/ads-txt-crawler-standalone.jar -t ./doc/top-100-programmatic-domains.txt >results.csv 2>err.log
```

Or run the `run-100.sh` file in the scripts directory. The `run-100.sh` will process the results and produce a few summary files.

```
$ ./scripts/run-100.sh
```

Lastly, for those who want to just see some results, you can visit the following repository which contains the output files from a recent Top 100 `run-100.sh` run.

- [https://github.com/bradlucas/top-100-domains-ads-txt](https://github.com/bradlucas/top-100-domains-ads-txt)


## Example saving to SQLite database

Create your initial database using the following command. Here I'll create a database called ads-txt.db

```
$ sqlite3 ads-txt.db < ./sql/create.sql
```

Then to run the Topp 100 domains as an example use the following command.

```
$ java -jar ./target/uberjar/ads-txt-crawler-standalone.jar -t ./doc/top-100-programmatic-domains.txt -d ads-txt.db
```

You'll notice that your errors will still show but the data will be saved into the database.

To verify the database you can dump the table with the following command.

```
$ echo 'select * from adstxt;' | sqlite3 ads-txt.db
```

Also, you can open the database with `sqlite3`.



## Notes

For background information on the project please review some recent blog posts on the project.

- [http://blog.bradlucas.com/tags/ads.txt/](http://blog.bradlucas.com/tags/ads.txt/)


## License

Copyright © 2017 Brad Lucas

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

