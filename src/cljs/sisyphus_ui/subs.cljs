(ns sisyphus-ui.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
 :current-profile-id
 (fn [db]
   (reaction (:current-profile-id @db))))

(re-frame/register-sub
 :profiles
 (fn [db]
   (reaction (:profiles @db))))

(re-frame/register-sub
 :variants
 (fn [db]
   (reaction (:variants @db))))

(re-frame/register-sub
 :groups
 (fn [db]
   (reaction (:groups @db))))

(re-frame/register-sub
 :active-panel
 (fn [db _]
   (reaction (:active-panel @db))))
