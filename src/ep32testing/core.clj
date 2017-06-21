(ns ep32testing.core)

(declare ^:dynamic *test-results*)

(defn update-report! [& args]
  (apply swap! *test-results* update args))

(defmacro defcheck [name & body]
  `(defn ~(with-meta name {:test `(var ~name)}) []
     ~@body))

(defn assert= [x y]
  (update-report! :assertions inc)
  (if (= x y)
    (update-report! :pass inc)
    (do
      (update-report! :fail inc)
      (update-report! :messages conj
                      (str "Expected " (pr-str y)
                           " but got " (pr-str x))))))

(defn- print-report! [{:keys [assertions tests pass fail messages]}]
  (run! #(println % "\n\n") messages)
  (println "Ran" tests "tests containing" assertions "assertions.")
  (println fail "failures."))

(defn run-all-tests []
  (binding [*test-results* (atom {:assertions 0 :tests 0 :pass 0 :fail 0 :messages []})]
    (doseq [test (->> (all-ns)
                      (mapcat ns-interns)
                      (keep #(-> % second meta :test)))]
      (update-report! :tests inc)
      (test))
    (print-report! @*test-results*)))

(run-all-tests)
