; This file is part of liferay-clojure-integration.
;
; liferay-clojure-integration is free software: you can redistribute it and/or
; modify it under the terms of the GNU General Public License as published by the
; Free Software Foundation, either version 3 of the License, or (at your option)
; any later version.
;
; liferay-clojure-integration is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY ; without even the implied warranty of MERCHANTABILITY
; or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
; more details.
;
; You should have received a copy of the GNU General Public License along with
; liferay-clojure-integration. If not, see <http://www.gnu.org/licenses/>.

(ns au.com.permeance.liferay.clojure
  (:use clojure.contrib.with-ns))

(def ^:dynamic input-objects)

(def ^:dynamic output-names)

(def ^:dynamic script)

(defprotocol ClojureScriptable
  (run-script
    [this input-objects output-names script]))

(deftype ClojureScriptableImpl
  []
  ClojureScriptable
  (run-script
    [this input-objects output-names script]
    (require 'au.com.permeance.liferay.clojure)
    (binding [input-objects input-objects
              output-names output-names
              script script]
      (use 'clojure.contrib.with-ns)
      (clojure.contrib.with-ns/with-temp-ns
        (refer 'au.com.permeance.liferay.clojure)
        (load-string script)))))
