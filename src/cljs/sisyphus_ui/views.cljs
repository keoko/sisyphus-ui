(ns sisyphus-ui.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com :refer [h-box v-box box gap single-dropdown input-text checkbox label title hyperlink-href p]]
              [re-com.dropdown :refer [filter-choices-by-keyword single-dropdown-args-desc]]
              [re-com.util     :refer [item-for-id]]
              [reagent.core    :as    reagent]))



;; home

(defn home-title []
  (let [profile-id (re-frame/subscribe [:current-profile-id])]
    (fn []
      [re-com/title
       :label (str "Sisyphus UI")
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
    [:div]))


(defn profiles-dropdown [profiles variants selected-profile-id filtered-variants selected-variant-id selected-group-id filtered-groups]
  [single-dropdown
   :choices profiles
   :model selected-profile-id
   :width "300px"
   :on-change #(do
                 (reset! selected-profile-id %)
                 (reset! filtered-variants (vec (filter-choices-by-keyword variants :profile-id @selected-profile-id)))
                 (reset! selected-variant-id nil)
                 (reset! selected-group-id nil)
                 (reset! filtered-groups []))])


(defn selected-profile-box [profiles selected-profile-id]
  [:div
   [:strong "selected profile: "]
   (if (nil? @selected-profile-id)
     "None"
     (str (:label (item-for-id @selected-profile-id profiles)) " [" @selected-profile-id "]"))])


(defn variants-dropdown [variants groups selected-variant-id filtered-variants filtered-groups]
  [single-dropdown
   :choices filtered-variants
   :model selected-variant-id
   :width "300px"
   :on-change #(do
                 (reset! selected-variant-id %)
                 (reset! filtered-groups (vec (filter-choices-by-keyword groups :variant-id @selected-variant-id))))])


(defn selected-variant-box [variants selected-variant-id]
  [:div
   [:strong "selected variant: "]
   (if (nil? @selected-variant-id)
     "None"
     (str (:label (item-for-id @selected-variant-id variants)) " [" @selected-variant-id "]"))])


(defn groups-dropdown [variants selected-group-id filtered-groups]
  [single-dropdown
   :choices filtered-groups
   :model selected-group-id
   :width "300px"
   :on-change #(reset! selected-group-id %)])

(defn selected-group-box [variants selected-group-id]
  [:div
   [:strong "selected group: "]
   (if (nil? @selected-group-id)
     "None"
     (str (:label (item-for-id @selected-group-id variants)) " [" @selected-group-id "]"))])



(defn profiles-panel []
  (let [profiles (re-frame/subscribe [:profiles])
        variants (re-frame/subscribe [:variants])
        groups (re-frame/subscribe [:groups])
        selected-profile-id (reagent/atom nil)
        filtered-variants (reagent/atom [])
        selected-variant-id (reagent/atom nil)
        filtered-groups (reagent/atom [])
        selected-group-id (reagent/atom nil)]
    (fn []
      [v-box
       :gap "10px"
       :children [[p "profiles:"]
                  [h-box
                   :gap "10px"
                   :align :center
                   :children [[profiles-dropdown @profiles @variants selected-profile-id filtered-variants selected-variant-id selected-group-id filtered-groups]
                              [selected-profile-box @profiles selected-profile-id]
                              [gap :size "10px"]
                              [h-box
                               :gap "10px"
                               :align :center
                               :children [[variants-dropdown @variants @groups selected-variant-id filtered-variants filtered-groups]
                                          [selected-variant-box @variants selected-variant-id]
                                          [gap :size "10px"]
                                          (h-box
                                           :gap "10px"
                                           :align :center
                                           :children [[groups-dropdown @variants selected-group-id filtered-groups]
                                                      [selected-group-box @variants selected-group-id]])]]]]]])))

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [profiles-panel]]])


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


