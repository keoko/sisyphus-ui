(ns sisyphus-ui.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com :refer [h-box v-box box gap single-dropdown input-text checkbox label title hyperlink-href p]]
              [re-com.dropdown :refer [filter-choices-by-keyword single-dropdown-args-desc]]
              [re-com.util     :refer [item-for-id]]
              [reagent.core    :as    reagent]))

(declare demo7)


(def profiles [{:id "app1-prd" :label "application 1 - production"}
               {:id "app1-stg" :label "application 1 - staging"}])

(def countries [{:id "au" :label "Australia"}
                {:id "us" :label "United States"}
                {:id "uk" :label "United Kingdom"}
                {:id "ca" :label "Canada"}
                {:id "nz" :label "New Zealand"}])

(def variants [{:id "p01" :label "A" :profile-id "app1-prd"}
               {:id "p02" :label "A/B" :profile-id "app1-prd"}
               {:id "p03" :label "A/G" :profile-id "app1-stg"}
               {:id "s04" :label "A/B/S" :profile-id "app1-stg"}
               {:id "s05" :label "A/B/C" :profile-id "app1-stg"}])

(def groups [{:id "g1" :label "jobs" :variant-id "p01"}
             {:id "g2" :label "tests" :variant-id "p01"}
             {:id "g3" :label "jobs" :variant-id "p03"}])


(def cities [{:id "01" :label "Sydney"       :country-id "au"}
             {:id "02" :label "Melbourne"    :country-id "au"}
             {:id "03" :label "Brisbane"     :country-id "au"}
             {:id "04" :label "Adelaide"     :country-id "au"}
             {:id "05" :label "Perth"        :country-id "au"}
             {:id "06" :label "Canberra"     :country-id "au"}
             {:id "07" :label "Hobart"       :country-id "au"}
             {:id "08" :label "Darwin"       :country-id "au"}
             {:id "09" :label "New York"     :country-id "us"}
             {:id "10" :label "Los Angeles"  :country-id "us"}
             {:id "11" :label "Dallas"       :country-id "us"}
             {:id "12" :label "Washington"   :country-id "us"}
             {:id "13" :label "Orlando"      :country-id "us"}
             {:id "14" :label "London"       :country-id "uk"}
             {:id "15" :label "Manchester"   :country-id "uk"}
             {:id "16" :label "Glasgow"      :country-id "uk"}
             {:id "17" :label "Brighton"     :country-id "uk"}
             {:id "18" :label "Birmingham"   :country-id "uk"}
             {:id "19" :label "Toronto"      :country-id "ca"}
             {:id "20" :label "Montreal"     :country-id "ca"}
             {:id "21" :label "Calgary"      :country-id "ca"}
             {:id "22" :label "Ottawa"       :country-id "ca"}
             {:id "23" :label "Edmonton"     :country-id "ca"}
             {:id "24" :label "Auckland"     :country-id "nz"}
             {:id "25" :label "Wellington"   :country-id "nz"}
             {:id "26" :label "Christchurch" :country-id "nz"}
             {:id "27" :label "Hamilton"     :country-id "nz"}
             {:id "28" :label "Dunedin"      :country-id "nz"}])


;; home

(defn home-title []
  (let [profile-id (re-frame/subscribe [:current-profile-id])]
    (fn []
      [re-com/title
       :label (str "current profile: " (name@profile-id))
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


(defn profiles-dropdown [selected-profile-id filtered-variants selected-variant-id selected-group-id filtered-groups]
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


(defn selected-profile-box [selected-profile-id]
  [:div
   [:strong "selected profile: "]
   (if (nil? @selected-profile-id)
     "None"
     (str (:label (item-for-id @selected-profile-id profiles)) " [" @selected-profile-id "]"))])


(defn variants-dropdown [selected-variant-id filtered-variants filtered-groups]
  [single-dropdown
   :choices filtered-variants
   :model selected-variant-id
   :width "300px"
   :on-change #(do
                 (reset! selected-variant-id %)
                 (reset! filtered-groups (vec (filter-choices-by-keyword groups :variant-id @selected-variant-id))))])


(defn selected-variant-box [selected-variant-id]
  [:div
   [:strong "selected variant: "]
   (if (nil? @selected-variant-id)
     "None"
     (str (:label (item-for-id @selected-variant-id variants)) " [" @selected-variant-id "]"))])


(defn groups-dropdown [selected-group-id filtered-groups]
  [single-dropdown
   :choices filtered-groups
   :model selected-group-id
   :width "300px"
   :on-change #(reset! selected-group-id %)])

(defn selected-group-box [selected-group-id]
  [:div
   [:strong "selected group: "]
   (if (nil? @selected-group-id)
     "None"
     (str (:label (item-for-id @selected-group-id variants)) " [" @selected-group-id "]"))])



(defn profiles-panel []
  (let [selected-profile-id (reagent/atom nil)
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
                   :children [[profiles-dropdown selected-profile-id filtered-variants selected-variant-id selected-group-id filtered-groups]
                              [selected-profile-box selected-profile-id]
                              [gap :size "10px"]
                              [h-box
                               :gap "10px"
                               :align :center
                               :children [[variants-dropdown selected-variant-id filtered-variants filtered-groups]
                                          [selected-variant-box selected-variant-id]
                                          [gap :size "10px"]
                                          (h-box
                                           :gap "10px"
                                           :align :center
                                           :children [[groups-dropdown selected-group-id filtered-groups]
                                                      [selected-group-box selected-group-id]])]]]]]])))

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









(defn demo7
  []
  (let [selected-country-id (reagent/atom nil)
        filtered-cities     (reagent/atom [])
        selected-city-id    (reagent/atom nil)]
    (fn []
      [v-box
       :gap      "10px"
       :children [[p "Two dropdowns can be tied together in a parent-child relationship. In this case, countries and their cities."]
                  [h-box
                   :gap      "10px"
                   :align    :center
                   :children [[single-dropdown
                               :choices   countries
                               :model     selected-country-id
                               :width     "300px"
                               :on-change #(do
                                            (reset! selected-country-id %)
                                            (reset! filtered-cities (vec (filter-choices-by-keyword cities :country-id @selected-country-id)))
                                            (reset! selected-city-id nil))]
                              [:div
                               [:strong "Selected country: "]
                               (if (nil? @selected-country-id)
                                 "None"
                                 (str (:label (item-for-id @selected-country-id countries)) " [" @selected-country-id "]"))]]]
                  [gap :size "10px"]
                  [h-box
                   :gap      "10px"
                   :align    :center
                   :children [[single-dropdown
                               :choices   filtered-cities
                               :model     selected-city-id
                               :width     "300px"
                               :on-change #(reset! selected-city-id %)]
                              [:div
                               [:strong "Selected city: "]
                               (if (nil? @selected-city-id)
                                 "None"
                                 (str (:label (item-for-id @selected-city-id cities)) " [" @selected-city-id "]"))]]]]])))

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


