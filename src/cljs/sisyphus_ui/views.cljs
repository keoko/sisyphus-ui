(ns sisyphus-ui.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]))


;; home

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Hello from " @name ". This is the Home Page.")
       :level :level1])))

(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "go to About Page"
   :href "#/about"])


(defn config-editor []
  (let [editor (.edit js/ace "editor")]
    (.setTheme editor "ace/theme/monokai")
    (-> (.getSession editor)
        (.setMode "ace/mode/yaml"))
    [:div#editor]))

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [link-to-about-page] [config-editor]]])


;; about

(defn about-title []
  [re-com/title
   :label "This is the About Page."
   :level :level1])

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "go to Home Page"
   :href "#/"])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title] [link-to-home-page]]])


;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [(panels @active-panel)]])))
