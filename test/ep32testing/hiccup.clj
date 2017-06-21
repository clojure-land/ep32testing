(ns ep32testing.hiccup
  (:require #_[ep32testing.core :refer [assert= defcheck]]
            [clojure.test :refer [deftest testing is are]]))

(defn hiccup->cljxml [h]
  {:pre [(or (vector? h) (string? h))]}
  (if (vector? h)
    (let [attrs?  (map? (second h))
          attrs   (if attrs? (second h) {})
          content (drop (if attrs? 2 1) h)]
      {:tag (first h), :attrs attrs, :content (map hiccup->cljxml content)})
    h))

(deftest test-hiccup->cljxml
  (testing "single tags"
    (are [hiccup cljxml] (= (hiccup->cljxml hiccup) cljxml)
      [:p]          {:tag :p, :attrs {}, :content ()}
      [:div]        {:tag :div, :attrs {}, :content ()}
      [:p "hello!"] {:tag :p, :attrs {}, :content '("bonjour!")}))

  (testing "nested tags"
    (is (= (hiccup->cljxml [:p {:class "info"} "hello!"])
           {:tag :p, :attrs {:class "info"}, :content '("hello!")}))
    (is (= (hiccup->cljxml [:p [:strong "hello"]])
           {:tag :p,
            :attrs {},
            :content '({:tag :strong,
                        :attrs {},
                        :content ("hello")})}))))
