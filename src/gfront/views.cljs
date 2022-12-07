(ns gfront.views
  (:require
   [re-frame.core :as re-frame]
   [gfront.events :as events]
   [gfront.subs :as subs]
   [goog.string :as gstring]
   ["antd" :refer [Button Form Form.Item InputNumber Table Space Typography.Paragraph Typography.Title]]))

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

(defn- submit-guess
  [state actual guess]
  (re-frame/dispatch [::events/submit-guess {:state state :actual-percent-enrolled actual :guessed-percent-enrolled guess}]))

(defn main-panel []
  (let [marketplace-data (re-frame/subscribe [::subs/marketplace-data])
        table-visible? (re-frame/subscribe [::subs/table-visible?])
        current-state (re-frame/subscribe [::subs/current-state])
        current-guess (re-frame/subscribe [::subs/current-guess])
        current-result (re-frame/subscribe [::subs/current-result])
        average-difference (re-frame/subscribe [::subs/average-difference])]
    (fn []
      [:<>
       [:> Typography.Title
        "Fun with Marketplace Enrollment Statistics"]
       [:> Space {:direction "vertical"}
        (when @current-state
          [:> Form
           {:onFinish (fn [values]
                        (submit-guess
                         (:state @current-state)
                         (js->clj (:enrolled-percentage @current-state))
                         (-> values (js->clj :keywordize-keys true) :guess)))}
           [:> Form.Item
            {:label (gstring/format "Can you guess the % enrollment for %s?" (:state @current-state))
             :name "guess"
             :rules [{:required true :message "Aw, come on take a guess!"}]}
            [:> InputNumber
             {:min 0
              :max 100
              :addonAfter "%"}]]
           [:> Form.Item
            [:> Button
             {:type "primary"
              :htmlType "submit"}
             "Submit Guess"]]])
        (when @current-result
          [:<>
           [:> Typography.Title {:level 4}
            (gstring/format "Your Guess: %d% | Actual: %d% | Difference: %d%"
                            (:guessed-percent-enrolled @current-guess)
                            (Math/round (:actual-percent-enrolled @current-guess))
                            @current-result)]
           [:> Typography.Paragraph
            (gstring/format "Mean guess error for %s: %d%" (:state @current-guess) @average-difference)]])
        [:> Button
         {:on-click #(re-frame/dispatch [::events/toggle-table-visible])}
         (if @table-visible? "Hide Marketplace Data" "Reveal Marketplace Data")]]
       (when @table-visible?
         [:> Table
          {:columns columns
           :dataSource @marketplace-data
           :pagination false}])])))
