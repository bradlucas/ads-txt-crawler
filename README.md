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
$ java -jar ads-txt-crawler-0.1.0-standalone.jar [options]

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


## License

Copyright © 2017 Brad Lucas

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
