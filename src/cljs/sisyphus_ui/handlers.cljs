(ns sisyphus-ui.handlers
    (:require [re-frame.core :as re-frame]
              [sisyphus-ui.db :as db]
              [ajax.core :refer [GET POST]]))


(def sisyphus-host "http://localhost:3000")


;; init db
(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   (GET (str sisyphus-host "/profiles/")
        {:handler #(re-frame/dispatch [:process-get-profiles-response %1])
         :response-format :json
         :error-handler #(re-frame/dispatch [:process-bad-get-profiles-response %1])})
   {}))




;; notification 
(defn reset-notification
  [db]
  (assoc db :notification {}))

(defn set-info-notification
  [db message]
  (assoc db :notification {:type :info :message message}))

(defn set-error-notification
  [db message]  
  (assoc db :notification {:type :danger :message message}))

(re-frame/register-handler
 :reset-notification
 (fn [db _]
   (reset-notification db)))



(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))


(re-frame/register-handler
 :request-group-data
 (fn [db [_ profile-id variant-id group-id]]
   (.log js/console (str "--- " sisyphus-host "/group/" profile-id variant-id group-id))
   (GET (str sisyphus-host "/group/" profile-id variant-id group-id)
        {:handler #(re-frame/dispatch [:process-get-group-data-response %1])
         :response-format :json
         :error-handler #(re-frame/dispatch [:process-bad-get-group-data-response %1])})
   (assoc db :loading? true)))


(re-frame/register-handler
 :process-get-group-data-response
 (fn [db [_ response]]
   (.log js/console (str "log:" response))

   (-> db
       (set-info-notification "loaded group data successfully.")
       (assoc :group-data (get (js->clj response) "data")))))


(re-frame/register-handler
 :process-bad-get-group-data-response
 (fn [db [_ response]]
   (set-error-notification db "data cannot be retrived at this moment, contact your administrator ;)")))


(re-frame/register-handler
 :change-group-data
 (fn [db [_ data]]
   (println "changing group data:" data)
   (assoc db :group-data data)))


(re-frame/register-handler
 :reset-group-data
 (fn [db [_ data]]
   (assoc db :group-data "")))


(re-frame/register-handler
 :process-bad-push-group-data
 (fn [db [_ response]]
   (.log js/console "error push data:" response)
   (set-error-notification db "data cannot be pushed to the server :'(")))


(re-frame/register-handler
 :push-group-data
 (fn [db [_ profile-id variant-id group-id group-data]]
   (POST (str sisyphus-host "/group/" profile-id variant-id group-id)
         {:params {:group-data group-data}
          :handler #(.log js/console "successfully pushed group-data")
          :error-handler #(re-frame/dispatch [:process-bad-push-group-data %1])})
   db))


(re-frame/register-handler
 :set-profile-id
 (fn [db [_ id]]
   (assoc db :selected-profile-id id)))


(re-frame/register-handler
 :set-variant-id
 (fn [db [_ id]]
   (assoc db :selected-variant-id id)))


(re-frame/register-handler
 :set-group-id
 (fn [db [_ id]]
   (assoc db :selected-group-id id)))

(defn parse-profiles
  [s]
  (->> (keys s)
       (map #(hash-map :id (name %) :label (name %)))
       flatten))

(defn parse-variants
  [s]
  (let [parse-variant (fn [[kp vp]] 
                        (map (fn [[k _]] 
                               {:id k :label k :profile-id (name kp)}) vp))]
    (->> s 
         (map parse-variant)
         flatten)))

(defn parse-groups
  [s]
  (let [parse-group (fn [kp k v] 
                      (map #(hash-map :id (name %) :label (name %) :variant-id k) v))
        parse-variant (fn [[kp vp]] 
                       (map (fn [[k v]] 
                              (parse-group kp k v)) vp)) ]      
    (->> s 
         (map parse-variant)
         flatten)))



(re-frame/register-handler
 :process-get-profiles-response
 (fn [db [_ response]]
   (.log js/console (str "log:" response))
   (.log js/console (str "log-clj:" (js->clj response)))
   (let [r (js->clj response)
         profiles (parse-profiles r)
         variants (parse-variants r)
         groups (parse-groups r)]
     (-> db
         (assoc :loading? false)
         (assoc :profiles profiles)
         (assoc :variants variants)
         (assoc :groups groups)))))

#_(re-frame/register-handler
 :process-get-profiles-response
 (fn [db [_ response]]
   (.log js/console (str "log:" response))
   (let [r (js->clj response)
         profiles (parse-profiles r)
         #_variants (parse-variants r)
         #_groups (parse-groups r)]
     (-> db
         (assoc :loading? false)
         #_(assoc :profiles profiles)
         #_(assoc :variants variants)
         #_(assoc :groups groups)))))


(re-frame/register-handler
 :process-bad-get-profiles-response
 (fn [db [_ response]]
   (set-error-notification db "cannot retrieved the list of profiles from the server. Please, check the server.")))
