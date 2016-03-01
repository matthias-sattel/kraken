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
    :background-color "#ffffff"})
  ((rule :.connection-tile)
   {:background-color "gray"
    :padding "10px"
    :margin "10px"})
  ((rule :.connection-active)
   {:background-color "#79FFB2"})
  ((rule :.connection-inactive)
   {:background-color "#4ED086"})
  ((rule :.connection-error)
   {:background-color "#E3291A"})
  ((rule :.connection-tile:hover)
   {:background-color "#996E56"})
   ; )
  )
