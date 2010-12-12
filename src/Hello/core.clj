(ns Hello.core
  (:use compojure.core
        hiccup.core))

(defroutes app
  (GET "/" [] (html [:h1 "Hello World"]))
  (GET "/:name" [name] (html [:h1 "Hello " name]))
)

