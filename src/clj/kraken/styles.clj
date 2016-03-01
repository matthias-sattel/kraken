(ns kraken.styles
  (:require [garden.def :refer [defrule defstyles]]
            [garden.stylesheet :refer [rule]]
            [garden.core :refer [css]]))



(defstyles screen                    
  ;(let [body (rule :body)]           
    ((rule :body)
     {:background-color "#ffffff"    
      :font-family "Helvetica Neue"  
      :font-size   "1.0em"            
      :line-height 1.5})
  ((rule :.connections-list)
   {:font-weight "normal"
    :font-family "Arial"
    :font-size "0.8em"
    :background-color "#94CC87"})
  ((rule :.connection-list-item:hover)
   {:background-color "#996E56"})
   ; )
  )
