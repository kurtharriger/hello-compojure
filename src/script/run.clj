(use 'ring.adapter.jetty)
(require 'Hello.core)

(run-jetty #'Hello.core/app {:port 8080})

