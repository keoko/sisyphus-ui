(ns sisyphus-ui.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [sisyphus-ui.handlers]
              [sisyphus-ui.subs]
              [sisyphus-ui.routes :as routes]
              [sisyphus-ui.views :as views]
              [sisyphus-ui.config :as config]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [] 
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
