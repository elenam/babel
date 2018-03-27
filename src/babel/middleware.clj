(ns babel.middleware
(:require [babel.processor :as processor])
(:import clojure.tools.nrepl.transport.Transport))

(defn interceptor
  [handler]
  (fn [inp-message]
    (let [transport (inp-message :transport)]
        (handler (assoc inp-message :transport
          (reify Transport
            (recv [this] (.recv transport))
            (recv [this timeout] (.recv transport timeout))
            (send [this msg]     (.send transport (processor/modify-errors msg)))))))))

(println "babel.middleware loaded")

(clojure.tools.nrepl.middleware/set-descriptor! #'interceptor
        {:expects #{} :requires #{} :handles {}})