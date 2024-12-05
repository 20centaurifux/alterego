(ns alterego.specs
  (:require [clojure.spec.alpha :as s]
            [clojure.string :refer [blank? split starts-with? ends-with?]]))

(s/def ::dns-label (s/and string?
                          #(re-matches #"(?i)(?=.{1,63}$)^[a-z0-9]([a-z0-9-]*[a-z0-9])?" %)))

(s/def ::fqdn (s/and string?
                     #(>= 255 (count %) 1)
                     #(not (starts-with? % "."))
                     #(not (ends-with? % "."))
                     (fn [fqdn]
                       (every? (fn [label]
                                 (s/valid? ::dns-label label))
                               (split fqdn #".")))))

(s/def ::hostname (s/or :dns-label ::dns-label
                        :fqdn ::fqdn))

(s/def ::port (s/and int? #(>= 65535 % 0)))

(s/def ::filled-string (s/and string? #(not (blank? %))))

(s/def ::address ::filled-string)

(s/def ::binding (s/keys :req-un [::port]
                         :opt-un [::address]))