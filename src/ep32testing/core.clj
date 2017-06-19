(ns ep32testing.core)

(defn assert= [x y]
  (when (not= x y)
    (throw (AssertionError. (str "Expected " (pr-str y)
                                 " but got " (pr-str x))))))

(defn hiccup->cljxml [h]
  {:pre [(or (vector? h) (string? h))]}
  (if (vector? h)
    (let [attrs?  (map? (second h))
          attrs   (if attrs? (second h) {})
          content (drop (if attrs? 2 1) h)]
      {:tag (first h), :attrs attrs, :content (map hiccup->cljxml content)})
    h))

(defn test-hiccup->cljxml []
  (assert= (hiccup->cljxml [:p]) {:tag :p, :attrs {}, :content ()})
  (assert= (hiccup->cljxml [:div]) {:tag :div, :attrs {}, :content ()})
  (assert= (hiccup->cljxml [:p "hello!"]) {:tag :p, :attrs {}, :content '("hello!")})
  (assert= (hiccup->cljxml [:p {:class "info"} "hello!"])
           {:tag :p, :attrs {:class "info"}, :content '("hello!")})
  (assert= (hiccup->cljxml [:p [:strong "hello"]])
           {:tag :p,
            :attrs {},
            :content '({:tag :strong,
                        :attrs {},
                        :content ("hello")})}))

(comment
  (test-hiccup->cljxml))
