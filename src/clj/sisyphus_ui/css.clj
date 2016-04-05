(ns sisyphus-ui.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {:color "red"}]
  [:.level1 {:color "green"}])

(defstyles editor
  [:#editor {:position "absolute"
            :top 0
            :right 0
            :bottom 0
            :left 0}])
