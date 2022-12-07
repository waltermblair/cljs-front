(ns gfront.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::marketplace-data
 (fn [db]
   (map #(assoc % :key (:state %)) (:marketplace-data db))))

(re-frame/reg-sub
 ::table-visible?
 (fn [db]
   (:table-visible? db)))

(re-frame/reg-sub
 ::current-state
 :<- [::marketplace-data]
 :<- [::current-guess]
 (fn [[data guess] _]
   (when (seq data)
     (let [count (count data)
           i (rand-int (dec count))]
       (nth data i)))))

(re-frame/reg-sub
 ::current-guess
 (fn [db]
   (:current-guess db)))

(re-frame/reg-sub
 ::current-result
 :<- [::current-guess]
 (fn [{:keys [state guessed-percent-enrolled actual-percent-enrolled]} _]
   (when (and state guessed-percent-enrolled)
     (Math/abs (- actual-percent-enrolled guessed-percent-enrolled)))))

(re-frame/reg-sub
 ::average-difference
 (fn [db]
   (:average-difference db)))