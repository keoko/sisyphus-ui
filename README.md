# sisyphus-ui

A [re-frame](https://github.com/Day8/re-frame) application designed to ... well, that part is up to you.

## Development Mode

### Compile css:

Compile css file once.

```
lein garden once
```

Automatically recompile css file on change.

```
lein garden auto
```

### Compile css:

Compile css file once.

```
lein less once
```

Automatically recompile css file on change.

```
lein less auto
```

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

### Run tests:

```
lein clean
lein doo phantom test once
```

The above command assumes that you have [phantomjs](https://www.npmjs.com/package/phantomjs) installed. However, please note that [doo](https://github.com/bensu/doo) can be configured to run cljs.test in many other JS environments (chrome, ie, safari, opera, slimer, node, rhino, or nashorn). 

## Production Build

```
lein clean
lein cljsbuild once min
```


## TODO
- load YAML group into editor
  - load variant data

- validate and update group (check etag to avoid conflicts)
- create/delete group
- load profiles, variants and group structure
 

## DOUBTS
- how to sync server-client data? long pull? -> sente?
- client schema validation
- client YAML conversion
- YAML conversion?
- transit protocol, edn?
- issues with writing collisions, too coarse-grained updates


## RE-FRAME
- validate db with prismatic schema
- figwheel with next/prev commands
- middlewares: ex-log

Server operations:
- store YAML via post
- get list of profiles
- get profile
- delete profile?

profiles dropdown -> variants dropdown -> config-groups dropdown

## ClojureScript
- how to load cljsjs assets like CSS
- figwheel readline support


## NICE TO HAVE
- create cljsjs package for ace
- compare codewarrior and ace
- display inherited configuration
- display full configuration
- client validation
- different file formats: YAML, EDN, JSON