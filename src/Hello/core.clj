(ns Hello.core
  (:use compojure.core
        hiccup.core
        hiccup.page-helpers
        ring.middleware.reload
        ring.middleware.stacktrace
        ring.util.response
        ring.middleware.file 
        ring.middleware.file-info
        Hello.middleware
        
))

(defn view-layout [& content]
  (html
    (doctype :html5)
    (xhtml-tag "en"
      [:head
        [:meta {:http-equiv "Content-type"
                :content "text/html; charset=utf-8"}]
        [:title "adder"]
        [:link {:href "/css/hello.css" :rel "stylesheet" :type "text/css"}]
      ]
      [:body content])))


(defn view-input [& [a b]]
  (view-layout
    [:h2 "add two numbers"]
    [:form {:method "post" :action "/"}
      (if (and a b)
        [:p "those are not both numbers!"])
      [:input.math {:type "text" :name "a" :value a}] [:span.math " + "]
      [:input.math {:type "text" :name "b" :value b}] [:br]
      [:input.action {:type "submit" :value "add"}]]))



(defn view-output [a b sum]
  (view-layout
    [:h2 "two numbers added"]
    [:p.math a " + " b " = " sum]
    [:a.action {:href "/"} "add more numbers"]))

(defn parse-input [a b]
  [(Integer/parseInt a) (Integer/parseInt b)])

(defroutes handler
  (GET "/" []
    (view-input))

  (POST "/" [a b]
    (try
      (let [[a b] (parse-input a b)
            sum   (+ a b)]
        (view-output a b sum))
      (catch NumberFormatException e
        (view-input a b))))
  (ANY "/*" [path]
    (redirect "/"))
)

(def production?
  (= "production" (get (System/getenv) "APP_ENV")))

(def development?
  (not production?))

(def app
    (-> #'handler
      (wrap-file "web-app")
      (wrap-file-info)
      (wrap-request-logging)
      (wrap-if development? wrap-reload '[Hello.middleware Hello.core])
      (wrap-exception-logging)
      (wrap-if production?  wrap-failsafe)
      (wrap-if development? wrap-stacktrace)))



