(ns sisyphus-ui.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com :refer [h-box v-box box gap single-dropdown input-text checkbox label title hyperlink-href p button]]
              [re-com.dropdown :refer [filter-choices-by-keyword single-dropdown-args-desc]]
              [re-com.util     :refer [item-for-id]]
              [reagent.core    :as    reagent]
              cljsjs.codemirror))


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


(defn editor-textarea []
  (let [editor (reagent/atom {})]
      (reagent/create-class
       {:display-name "config-editor-component"

        :component-did-mount
        (fn [this]
          (let [cm (.fromTextArea  js/CodeMirror
                                   (reagent/dom-node this)
                                   #js {:mode "yaml"
                                        :lineNumbers true})]
            (reset! editor {:editor cm :cursor (.getCursor cm)})
            (.on cm "change" 
                 #(do
                    (let [val (.getValue %)]
                      ;; workaround: accessing directly app-db
                      (when (not= val (:group-data @re-frame.db/app-db))
                        (swap! editor assoc :cursor (.getCursor %))
                        (re-frame/dispatch-sync [:change-group-data val]))))))) 

        :component-did-update 
        (fn [comp]
          (let [[_ data] (reagent/argv comp)]
            (doto (:editor @editor)
              (.setValue  data)
              (.setCursor (:cursor @editor)))))

        :reagent-render
        (fn []
          [:textarea {:auto-complete "off"}])})))


(defn editor-textarea-outer []
  (let [data (re-frame/subscribe [:group-data])]
    (fn []
      [editor-textarea  @data])))



(defn profiles-dropdown [profiles variants selected-profile-id filtered-variants selected-variant-id selected-group-id filtered-groups]
  [single-dropdown
   :placeholder "select a profile"
   :choices profiles
   :model selected-profile-id
   :width "300px"
   :on-change #(do
                 (re-frame/dispatch [:set-profile-id %])
                 #_(reset! selected-profile-id %)
                 (reset! filtered-variants (vec (filter-choices-by-keyword variants :profile-id %)))
                 (re-frame/dispatch [:set-variant-id nil])
                 (re-frame/dispatch [:set-group-id nil])                 
                 #_(reset! selected-variant-id nil)
                 #_(reset! selected-group-id nil)
                 (reset! filtered-groups []))])



(defn variants-dropdown [variants groups selected-variant-id filtered-variants filtered-groups]
  [single-dropdown
   :placeholder "select a variant"
   :choices filtered-variants
   :model selected-variant-id
   :width "300px"
   :on-change #(do
                 #_(reset! selected-variant-id %)
                 (re-frame/dispatch [:set-variant-id %])                 
                 (re-frame/dispatch [:set-group-id nil])    
                 (reset! filtered-groups (vec (filter-choices-by-keyword groups :variant-id %))))])



(defn groups-dropdown [variants 
                       selected-profile-id
                       selected-variant-id
                       selected-group-id
                       filtered-groups]
  [single-dropdown
   :placeholder "select a group"
   :choices filtered-groups
   :model selected-group-id
   :width "300px"
   :on-change #(do 
                 #_(reset! selected-group-id %)
                 (re-frame/dispatch [:set-group-id %])
                 (re-frame/dispatch [:request-group-data @selected-profile-id @selected-variant-id %]))])


(defn profiles-panel []
  (let [profiles (re-frame/subscribe [:profiles])
        variants (re-frame/subscribe [:variants])
        groups (re-frame/subscribe [:groups])
        selected-profile-id (re-frame/subscribe [:selected-profile-id])
        selected-variant-id (re-frame/subscribe [:selected-variant-id])
        selected-group-id (re-frame/subscribe [:selected-group-id])
        filtered-variants (reagent/atom [])
        filtered-groups (reagent/atom [])]
    (fn []
      [v-box
       :gap "10px"
       :children [[h-box
                   :gap "10px"
                   :align :center
                   :children [[profiles-dropdown @profiles @variants selected-profile-id filtered-variants selected-variant-id selected-group-id filtered-groups]
                              [gap :size "10px"]
                              [h-box
                               :gap "10px"
                               :align :center
                               :children [[variants-dropdown @variants @groups selected-variant-id filtered-variants filtered-groups]
                                          [gap :size "10px"]
                                          (h-box
                                           :gap "10px"
                                           :align :center
                                           :children [[groups-dropdown 
                                                       @variants
                                                       selected-profile-id
                                                       selected-variant-id
                                                       selected-group-id 
                                                       filtered-groups]])]]]]]])))


(defn editor-push-button []
  (let [selected-profile-id (re-frame/subscribe [:selected-profile-id])
        selected-variant-id (re-frame/subscribe [:selected-variant-id])
        selected-group-id (re-frame/subscribe [:selected-group-id])
        group-data (re-frame/subscribe [:group-data])]
  [button
   :label "push"
   :on-click #(if (and @selected-profile-id @selected-variant-id @selected-group-id)
                (re-frame/dispatch [:push-group-data @selected-profile-id @selected-variant-id @selected-group-id @group-data])
                (.alert js/window "TODO: create profile or variant or group"))
   :class "btn-primary"]))


(defn editor-cancel-button []
  (let [selected-profile-id (re-frame/subscribe [:selected-profile-id])
        selected-variant-id (re-frame/subscribe [:selected-variant-id])
        selected-group-id (re-frame/subscribe [:selected-group-id])]
    [button
     :label "cancel"
     :on-click #(if (and @selected-profile-id @selected-variant-id @selected-group-id)
                  (re-frame/dispatch [:request-group-data @selected-profile-id @selected-variant-id @selected-group-id])
                  (re-frame/dispatch [:reset-group-data]))
     :class "btn-danger"]))


(defn editor-panel []
  (let [])
  [:div
   [editor-textarea-outer]
   [editor-push-button]
   [editor-cancel-button]])

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [profiles-panel] [editor-panel]]])


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


