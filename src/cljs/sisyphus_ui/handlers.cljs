(ns sisyphus-ui.handlers
    (:require [re-frame.core :as re-frame]
              [sisyphus-ui.db :as db]
              [ajax.core :refer [GET POST]]))


(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))


(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))


(re-frame/register-handler
 :request-group-data
 (fn [db [_ profile-id variant-id group-id]]
   (.log js/console (str "--- http://localhost:3000/group/" profile-id "/" group-id))
   (GET (str "http://localhost:3000/group/" profile-id "/" group-id)
        {:handler #(re-frame/dispatch [:process-response %1])
         :response-format :json
         :error-handler #(re-frame/dispatch [:process-bad-response %1])})
   (assoc db :loading? true)))


(re-frame/register-handler
 :process-response
 (fn [db [_ response]]
   (.log js/console (str "log:" response))

   (-> db
       (assoc :loading? false)
       (assoc :group-data (get (js->clj response) "data")))))


(re-frame/register-handler
 :process-bad-response
 (fn [db [_ response]]
   (.alert js/window "request failed :'(")
   db))


(re-frame/register-handler
 :change-group-data
 (fn [db [_ data]]
   (println "changing group data:" data)
   (assoc db :group-data data)))


(re-frame/register-handler
 :reset-group-data
 (fn [db [_ data]]
   (assoc db :group-data "")))



