
(ns stack-workflow.main
  (:require [respo.core :refer [render! clear-cache! falsify-stage! render-element]]
            [respo.util.format :refer [mute-element]]
            [stack-workflow.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]))

(defn dispatch! [op op-data] )

(defonce store-ref (atom {}))

(defonce states-ref (atom {}))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (render! (comp-container @store-ref) target dispatch! states-ref)))

(def ssr-stages
  (let [ssr-element (.querySelector js/document "#ssr-stages")
        ssr-markup (.getAttribute ssr-element "content")]
    (read-string ssr-markup)))

(defn -main! []
  (enable-console-print!)
  (render-app!)
  (add-watch store-ref :changes render-app!)
  (add-watch states-ref :changes render-app!)
  (if (not (empty? ssr-stages))
    (let [target (.querySelector js/document "#app")]
      (falsify-stage!
       target
       (mute-element (render-element (comp-container @store-ref ssr-stages) states-ref))
       dispatch!)))
  (println "app started!"))

(defn on-jsload! [] (clear-cache!) (render-app!) (println "code update."))

(set! (.-onload js/window) -main!)
