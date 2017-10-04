# ads-txt-crawler

An implementation of a crawler for Ads.txt files written in Clojure.

## Background

IAB Tech Lab released a specification for Ads.txt files. See [https://iabtechlab.com/ads-txt/](https://iabtechlab.com/ads-txt/).

Along with the specification they released a reference crawler written in Python. The repository for that project is [https://github.com/InteractiveAdvertisingBureau/adstxtcrawler](https://github.com/InteractiveAdvertisingBureau/adstxtcrawler).

This project demonstrates a crawler for Ads.txt files written in Clojure. As of this writing the Python project differs from this project in that it saves it's data to a SQLite database. This project simply outputs the data to STDOUT and writes errors to STDERR. As a proof of concept this has been more than adequent to investigate a large number of domains and their Ads.txt files.


## Building

Build the project with the `lein uberjar` command.

```
$ lein uberjar
```

## Usage Example

```
$ java -jar ads-txt-crawler-standalone.jar [options]

Options:
          -t FILE, --targets=FILE
                                   list of domains to crawler ads.txt from
```

## Targets File

The targets file is simply a list of domains and URLS to crawl. For each line the crawler will extract the domain and make a request to `http://DOMAIN/ads.txt`.

The data returned will be parsed to ignore blank and commented lines. Each valid line will be parsed according to the Ads.txt specification.

## Example

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

- [https://github.com/bradlucas/top-100-domains-ads-txt])https://github.com/bradlucas/top-100-domains-ads-txt)

## Notes

For background information on the project please review some recent blog posts on the project.

- [http://blog.bradlucas.com/tags/ads.txt/](http://blog.bradlucas.com/tags/ads.txt/)


## License

Copyright © 2017 Brad Lucas

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

LocalWords:  adequent
