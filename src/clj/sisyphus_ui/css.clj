(ns sisyphus-ui.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {:color "red"}]
  [:.level1 {:color "green"}])

(defstyles editor
  [:#editor {:position "relative !important"
            :margin-top "0px"
            :height "500px"
            :width "100%"}])

