(ns videochat.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
  :messages
  (fn [db]
    (reaction (:messages @db))))
