(ns gfront.views
  (:require
   [re-frame.core :as re-frame]
   [gfront.events :as events]
   [gfront.subs :as subs]
   ["antd" :refer [Button Table Typography.Title]]))

(enable-console-print!)
(def ^:private columns
  [{:title "State"
    :dataIndex "state"
    :key "state"}
   {:title "Total Eligible"
    :dataIndex "eligible"
    :key "eligible"}
   {:title "Total Enrolled"
    :dataIndex "enrolled"
    :key "enrolled"}])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        marketplace-data (re-frame/subscribe [::subs/marketplace-data])]
    (fn []
      [:div
       [:> Typography.Title
        "Hello from " @name]
       [:> Button
        {:on-click #(re-frame/dispatch [::events/get-marketplace-data])}
        "Get Marketplace Data"]
       [:> Table
        {:columns columns
         :dataSource @marketplace-data
         :pagination false}]])))
