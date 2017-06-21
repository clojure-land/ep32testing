(ns ep32testing.core)

(defn assert= [x y]
  (when (not= x y)
    (throw (AssertionError. (str "Expected " (pr-str y)
                                 " but got " (pr-str x))))))

(defmacro defcheck [name & body]
  `(defn ~(with-meta name {:test `(var ~name)}) []
     ~@body))

(defcheck test-a-thing
  (assert= 5 6))

(macroexpand-1 '(defcheck test-a-thing
                  (assert= 5 6)))
;;=> (clojure.core/defn test-a-thing [] (assert= 5 6))

(meta #'test-a-thing)
;;=> {:test #'ep32testing.core/test-a-thing, :arglists ([]), :line 12, :column 1, :file "/home/arne/ep32testing/src/ep32testing/core.clj", :name test-a-thing, :ns #namespace[ep32testing.core]}

(defn run-all-tests []
  (doseq [test (->> (all-ns)
                    (mapcat ns-interns)
                    (keep #(-> % second meta :test)))]
    (test)))

(run-all-tests)
