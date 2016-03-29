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
  ((rule :.connections-list-offline)
   {:font-weight "normal"
    :font-family "Arial"
    :font-size "0.8em"
    :background-color "#ff6347"})
  ((rule :.connections-list-online)
   {:font-weight "normal"
    :font-family "Arial"
    :font-size "0.8em"
    :background-color "green"})
  ((rule :.connection-tile)
   {:background-color "gray"
    :padding "10px"
    :margin "10px"})
  ((rule :.connection-active)
   {:background-color "#7fff00"})
  ((rule :.connection-inactive)
   {:background-color "#4ED086"})
  ((rule :.connection-error)
   {:background-color "#ff4500"})
  ((rule :.connection-warning)
   {:background-color "#ffd700"})
  ((rule :.connection-tile:hover)
   {:background-color "#00ffff"})
  ((rule :.connection-menu)
   {:margin "2x"})
  ((rule :.connection-menu-item)
   {:padding "2x"
    :margin "2px"})
   ; )
  )
