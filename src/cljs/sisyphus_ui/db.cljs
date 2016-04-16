(ns sisyphus-ui.db)


(def profiles [{:id "app1-prd" :label "application 1 - production"}
               {:id "app1-stg" :label "application 1 - staging"}])

(def variants [{:id "A" :label "A" :profile-id "app1-prd"}
               {:id "A/B" :label "A/B" :profile-id "app1-prd"}
               {:id "A" :label "A" :profile-id "app1-stg"}
               {:id "A/B" :label "A/B" :profile-id "app1-stg"}
               {:id "A/B/C" :label "A/B/C" :profile-id "app1-stg"}])

(def groups [{:id "foo.yml" :label "foo.yml" :variant-id "A"}
             {:id "foo.yml" :label "tests" :variant-id "A"}
             {:id "jobs" :label "jobs" :variant-id "A/G"}])

(def editor-data "")

(def default-db
  {:profiles profiles
   :variants variants
   :groups groups
   :selected-profile-id nil
   :selected-variant-id nil
   :selected-group-id nil
   :group-data editor-data
   :loading? false
   :notification {}})
