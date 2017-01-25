(ns videochat.views
    (:require [re-frame.core    :refer [dispatch subscribe]]
              [cljs-time.format :as time.format]
              [clojure.string   :refer [blank?]]))

(def date-formatter (time.format/formatter "h:mm A"))

(defn message-item [{:keys [content date author]}]
  (let [date (time.format/unparse date-formatter date)]
    [:div.message
     [:div.message__content content]
     [:div.message__author author]
     [:div.message__date date]]))

(defn messages []
  (let [messages (subscribe [:messages])]
    (fn []
      [:div.messages
       (for [message @messages]
         ^{:key message} [message-item message])])))

(defn- submit-handler [e]
  (.preventDefault e)
  (let [input-el  (-> e
                      .-target
                      (.getElementsByTagName "input")
                      (aget 0))
        input-val (.-value input-el)]
    (when-not (blank? input-val)
      (set! (.-value input-el) "")
      (dispatch [:submit-message input-val]))))

(defn message-form []
  [:form.message-form {:on-submit submit-handler}
   [:input.message-form__input {:type "text" :name "message"}]])

(defn main []
  (fn []
    [:div.main
     [messages]
     [message-form]]))
