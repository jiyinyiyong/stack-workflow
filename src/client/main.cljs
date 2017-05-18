(ns client.main
  (:require [respo.core :refer [render! clear-cache! falsify-stage! render-element]]
            [client.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [client.updater.core :refer [updater]]))

(defonce ref-store (atom {:states {}}))

(defn dispatch! [op op-data]
  (let [next-store (updater ref-store op op-data)] (reset! ref-store next-store)))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (render! (comp-container @ref-store) target dispatch!)))

(def ssr-stages
  (let [ssr-element (.querySelector js/document "#ssr-stages")
        ssr-markup (.getAttribute ssr-element "content")]
    (read-string ssr-markup)))

(defn start []
  (if (not (empty? ssr-stages))
    (let [target (.querySelector js/document "#app")]
      (falsify-stage!
        target
        (render-element (comp-container @ref-store ssr-stages))
        dispatch!)))
  (render-app!)
  (add-watch ref-store :changes render-app!)
  (js/console.log "app start"))

(defn stop []
  (clear-cache!)
  (js/console.log "app stop"))

(defn ^:export init []
  (start))