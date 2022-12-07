(ns gfront.events
  (:require [ajax.core :refer [GET POST]]
            [clojure.walk :as walk]
            [gfront.db :as db]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-db
 ::process-response
 (fn [db [_ response]]
   (assoc db
          :loading? false
          :marketplace-data (walk/keywordize-keys response))))

(re-frame/reg-event-db
 ::bad-response
 (fn [db [_ _]]
   (assoc db :loading? false)))

(re-frame/reg-event-db
 ::get-marketplace-data
 (fn [db _]
   (GET
     "http://localhost:3000/api/1/marketplace"
     {:handler #(re-frame/dispatch [::process-response %1])
      :error-handler #(re-frame/dispatch [::bad-response %1])})
   (assoc db :loading? true)))

(re-frame/reg-fx
 ::post-guess
 (fn [guess]
   (POST "http://localhost:3000/api/1/guess"
     {:params guess})))

(re-frame/reg-event-fx
 ::submit-guess
 (fn [{:keys [db]} [_ guess]]
   (when guess
     {:db (assoc db :current-guess guess)
      :fx [[:dispatch [::post-guess guess]]]})))

(re-frame/reg-event-db
 ::toggle-table-visible
 (fn [db _]
   (update db :table-visible? not)))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
