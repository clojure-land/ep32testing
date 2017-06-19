(ns ep32testing.core)

(defn assert= [x y]
  (when (not= x y)
    (throw (AssertionError. (str "Expected " (pr-str y)
                                 " but got " (pr-str x))))))

(defn hiccup->cljxml [h]
  (let [attrs?  (map? (second h))
        attrs   (if attrs? (second h) {})
        content (drop (if attrs? 2 1) h)]
    {:tag (first h), :attrs attrs, :content content}))

(defn test-hiccup->cljxml []
  (assert= (hiccup->cljxml [:p]) {:tag :p, :attrs {}, :content ()})
  (assert= (hiccup->cljxml [:div]) {:tag :div, :attrs {}, :content ()})
  (assert= (hiccup->cljxml [:p "hello!"]) {:tag :p, :attrs {}, :content '("hello!")})
  (assert= (hiccup->cljxml [:p {:class "info"} "hello!"])
           {:tag :p, :attrs {:class "info"}, :content '("hello!")}))

(test-hiccup->cljxml)
