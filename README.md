# ads-txt-crawler

An implementation of a crawler for Ads.txt files written in Clojure.

A blog post associated with this release is available here:

[http://blog.bradlucas.com/posts/2017-09-30-a-clojure-ads-txt-crawler/](http://blog.bradlucas.com/posts/2017-09-30-a-clojure-ads-txt-crawler/)

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

There is now an additional test file containing the top 100 programmatic domains. Also note, that errors are printed to STDERR. To capture the results separately than the errors review the following example which runs ads-txt-crawler using the top 100 domains file.

```
$ java -jar ./target/uberjar/ads-txt-crawler-standalone.jar -t ./doc/top-100-programmatic-domains.txt >results.csv 2>err.log
```



## License

Copyright © 2017 Brad Lucas

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
