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

(re-frame/register-sub
 :group-data
 (fn [db _]
   (reaction (:group-data @db))))

(re-frame/register-sub
 :selected-profile-id
 (fn [db _]
   (reaction (:selected-profile-id @db))))

(re-frame/register-sub
 :selected-variant-id
 (fn [db _]
   (reaction (:selected-variant-id @db))))

(re-frame/register-sub
 :selected-group-id
 (fn [db _]
   (reaction (:selected-group-id @db))))
