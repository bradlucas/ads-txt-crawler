# ads-txt-crawler

An implementation of a crawler for Ads.txt files written in Clojure.

## Background

IAB Tech Lab has released a specification for Ads.txt files. See [https://iabtechlab.com/ads-txt/](https://iabtechlab.com/ads-txt/).

Along with the specification they have released a reference crawler written in Python. The repo for that project is [https://github.com/InteractiveAdvertisingBureau/adstxtcrawler](https://github.com/InteractiveAdvertisingBureau/adstxtcrawler).

## Building

Build the project with the `lein uberjar` command.

```
$ lein uberjar
```


## Usage Example

```
$ java -jar ads-txt-crawler-0.0.2-standalone.jar [options]

Options:
  -t FILE, --targets=FILE
                        list of domains to crawler ads.txt from
```

## Targets File

The targets file is simply a list of domains and URLS to crawl. For each line the crawler will extract the domain make a request to `http://DOMAIN/ads.txt`.

The data returned will be parsed to ignore blank and commented lines. Each valid line will be parsed according to the Ads.txt specification.

## Running

After building the project using `lein uberjar` pass the example `target-domains.txt` file included in the docs directory using the `-t` flag.

```
$ java -jar ./target/uberjar/ads-txt-crawler-standalone.jar -t ./doc/target-domains.txt

```

For an example, see the file [./doc/top-100-programmatic-domains.txt](top-100-programmatic-domains.txt) in the doc directory.

To run this file you can either run the following command.

```
$ java -jar ./target/uberjar/ads-txt-crawler-standalone.jar -t ./doc/top-100-programmatic-domains.txt >results.csv 2>err.log
```

Or run the `run-100.sh` file in the scripts directory.

```
$ ./scripts/run-100.sh
```

Lastly, for those who want to just see some results, you can visit the following repository which contains the files from a recent Top 100 run.

- [https://github.com/bradlucas/top-100-domains-ads-txt])https://github.com/bradlucas/top-100-domains-ads-txt)

## Notes

Some blog posts related to this project are available

[http://blog.bradlucas.com/posts/2017-09-30-a-clojure-ads-txt-crawler/](http://blog.bradlucas.com/posts/2017-09-30-a-clojure-ads-txt-crawler/)
[http://blog.bradlucas.com/posts/2017-10-01-updated-clojure-ads-txt-crawler/](http://blog.bradlucas.com/posts/2017-10-01-updated-clojure-ads-txt-crawler/)
[http://blog.bradlucas.com/posts/2017-10-03-ads-txt-top-100-domain-results/](http://blog.bradlucas.com/posts/2017-10-03-ads-txt-top-100-domain-results/)


## License

Copyright © 2017 Brad Lucas

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
