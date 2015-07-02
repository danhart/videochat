(ns videochat.handlers
  (:require [re-frame.core :refer [register-handler dispatch path trim-v]]
            [cljs-time.core :refer [now]]
            [videochat.db :refer [default-db]]))

(register-handler
  :initialize-db
  (fn [_ _]
    default-db))

(register-handler
  :add-message
  [(path :messages) trim-v]
  (fn [messages [message]]
    (conj messages message)))

(register-handler
  :submit-message
  trim-v
  (fn [db [content]]
    ; This will have a side-effect of pushing the message over a websocket
    ; Potentially also adding our message pre-emptively
    ; All this logic will need to move server-side
    (if (:user db)
      (dispatch [:add-message {:role    :user
                               :room    (get-in db [:active-room])
                               :content content
                               :date    (now)
                               :author  (get-in db [:user :handle])}])
      (dispatch [:add-message {:role    :system
                               :date    (now)
                               :content "Please sign up or log in to chat"}]))
    db))
