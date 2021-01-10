
## Project :
**µMr**, formally micro map reduce.

µMr is a playground to experiments around mapreduce  pipeline.

the ultimate goal is to provide a collection of engine able to execute our map reduce "hello-world" : `word count`

**Roadmap** :
- [x] Single thread word count
- [x] Parallel word count
- [x] Dockerized Cli
- [ ] multiple output format (csv,json,avro)
- [ ] S3 Source
- [ ] Fiber based (ZIO) implementation
- [ ] back pressure (Akka stream) implementation
- [ ] Distributed (Typed actor) implementation

## requirements 

- scala 2.13
- sbt 1.4.x
- jdk 11

## project structure
api: µMr api & µMr abstraction (mode)

impl: available µMr engine Implementation

publicapi: 
- set of traits which combine `µMr` with a `mode` an `engine implementation` in a cake pattern style
- public api expose the simplest way to implement an µMr process.

## local development

build µMr : `sbt "clean;compile"`

test µMr : `sbt "clean;test"`

## example

some examples are provided :

WordCountPar leverage parallel engine and embedded file source [here](./src/main/scala/µ/mr/poc/example/WordCountPar.scala)

WordCountSimpleMonoThread use root api and provide its own source and sink functions  [here](./src/main/scala/µ/mr/poc/example/WordCountSimpleMonoThread.scala)

## local test 

```sbt "runMain µ.mr.poc.WordCount -s=./sample/*.txt -o=./output -f=CSV --mum_mapper=10 --num_reducer=2" ```

## docker
first of all we have to build µMr image
 ```sbt "docker:publishLocal"```

then just run a container with the required volumes :
```
docker run --rm --name=word-count  \
 -v $(pwd)/sample:/opt/docker/data:z \
 -v $(pwd)/output:/opt/docker/output:z \
 u-mr:2020-01-09 "-s=/opt/docker/data/*.txt" "-o=/opt/docker/output" "-f=CSV" "--mum_mapper=10" "--num_reducer=2"
```

## CLI options :
```
Usage: µMr-WordCount [options]

  -s, --source_path <./dir/*.txt>
                           Defines where data will be read.
                           You could either specify a single file <./data/book.txt>
                           or a wild card <./library/*.txt> 
  -o, --output <./output>  Allows to select output directory
  -f, --format <CSV | JSON>
                           Output format
  --mum_mapper <1->n>      Number of mappers 
  --num_reducer <1->n>     Number of reducers
  --verbose                Verbose mode (not yet implemented)

```