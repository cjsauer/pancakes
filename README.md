# pancakes

*The breakfast stack*

Objectives:

- Allow for instant prototyping of a new idea.
- Provide an immediate feedback development environment stretching from client
  to server.
- Provide clear extension points.
- Offer batteries.
- Docker.
- Provide excellent documentation.

## Usage

```Clojure
(require '[pancakes.core :refer [defserver defclient run
                                 client-call server-call]])

(defserver server
  (init [] (println "This kitchen is open for business"))
  (pass [item] (println "Passing" item "to client...")
               (client-call :receive item)))

(defclient client
  (init [] (log js/console "Asking server to pass the butter...")
           (server-call :pass :butter))
  (receive [item] (log js/console "Thanks for the" item "!")))

(run server)
(run client)
```

### defserver

- Runs an http-kit server
- pancakes handler: 
---- extracts data from requests and interprets as RPC call
---- maintains map of channels to their respective requests
- data is plain Clojure data
- the user writes the RPC calls; they're just functions
- pancakes provides built-ins for common things

Macro?

- Wraps the function definitions in the pancake router
---- The router is responsible for remote calls
- Definitions are all made in the same namespace for easy resolution

Function?

- Returns a map of name to function
- Calling `run` is what actually wraps everything
- Essentially just becomes middleware

Router

- Literally just looks up the function to call in a map
