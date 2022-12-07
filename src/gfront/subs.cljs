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
 :<- [::current-state]
 :<- [::current-guess]
 (fn [[state {:keys [guess]}] _]
   (when (and state guess)
     (let [actual (->> state
                       :enrolled-percentage
                       Math/round)
           diff (Math/abs (- actual guess))]
       diff))))