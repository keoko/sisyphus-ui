(ns sisyphus-ui.db)


(def profiles [{:id "app1-prd" :label "application 1 - production"}
               {:id "app1-stg" :label "application 1 - staging"}])


(def variants [{:id "p01" :label "A" :profile-id "app1-prd"}
               {:id "p02" :label "A/B" :profile-id "app1-prd"}
               {:id "p03" :label "A/G" :profile-id "app1-stg"}
               {:id "s04" :label "A/B/S" :profile-id "app1-stg"}
               {:id "s05" :label "A/B/C" :profile-id "app1-stg"}])

(def groups [{:id "g1" :label "jobs" :variant-id "p01"}
             {:id "g2" :label "tests" :variant-id "p01"}
             {:id "g3" :label "jobs" :variant-id "p03"}])



(def default-db
  {:profiles profiles
   :variants variants
   :groups groups})
