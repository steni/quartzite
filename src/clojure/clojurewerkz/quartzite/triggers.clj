(ns clojurewerkz.quartzite.triggers
  (:refer-clojure :exclude [key])
  (:import [org.quartz Trigger TriggerBuilder TriggerKey ScheduleBuilder]
           org.quartz.utils.Key
           java.util.Date)
  (:use    [clojurewerkz.quartzite.conversion :only [to-job-data to-date]]))


;;
;; Implementation
;;

;; ...



;;
;; API
;;

(defn ^TriggerKey key
  ([]
     (TriggerKey. (Key/createUniqueName nil)))
  ([named]
     (TriggerKey. (name named)))
  ([named, group]
     (TriggerKey. (name named) (name group))))



(defn ^TriggerBuilder with-identity
  ([^TriggerBuilder tb s]
     (if (instance? TriggerKey s)
       (.withIdentity tb ^TriggerKey s)
       (.withIdentity tb (key s))))
  ([^TriggerBuilder tb s group]
     (.withIdentity tb (key s group))))

(defn ^TriggerBuilder with-description
  [^TriggerBuilder tb ^String s]
  (.withDescription tb s))


(defn ^TriggerBuilder with-priority
  [^TriggerBuilder tb ^long l]
  (.withPriority tb l))

(defn ^TriggerBuilder modified-by-calendar
  [^TriggerBuilder tb ^String s]
  (.modifiedByCalendar tb s))

(defn ^TriggerBuilder with-schedule
  [^TriggerBuilder tb ^ScheduleBuilder sb]
  (.withSchedule tb sb))

(defn ^TriggerBuilder start-now
  [^TriggerBuilder tb]
  (.startNow tb))


;; Seamless JodaTime integration is one
;; of the goals of Quartzite.
(defn start-at
  [^TriggerBuilder tb date]
  (.startAt tb (to-date date)))

(defn end-at
  [^TriggerBuilder tb date]
  (.endAt tb (to-date date)))



(defn for-job
  ([^TriggerBuilder tb job]
     (.forJob tb job))
  ([^TriggerBuilder tb ^String job ^String group]
     (.forJob tb job group)))


(defn ^TriggerBuilder using-job-data
  [^TriggerBuilder tb m]
  (.usingJobData tb (to-job-data m)))



(defn ^Trigger finalize
  [^TriggerBuilder tb]
  (.build tb))


(defmacro ^Trigger build
  [& body]
  `(let [tb# (TriggerBuilder/newTrigger)]
     (finalize (-> tb# ~@body))))
