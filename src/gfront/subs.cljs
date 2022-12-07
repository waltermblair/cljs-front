(ns gfront.subs
  (:require
   [re-frame.core :as re-frame]))


(re-frame/reg-sub
 ::marketplace-data
 (fn [{:keys [marketplace-data]}]
   (map #(assoc % :key (:state %)) marketplace-data)))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))