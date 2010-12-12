(ns Hello.core
  (:use compojure.core
        ring.adapter.jetty
        hiccup.core))

(defroutes main-routes
  (GET "/" [] (html [:h1 "Hello World"]))
  (GET "/:name" [name] (html [:h1 "Hello " name]))
)

(run-jetty main-routes {:port 8080})