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

(ns au.com.permeance.liferay.clojure.test
  (:use au.com.permeance.liferay.clojure)
  (:use clojure.test))

(deftest test-run-script
  (let [clojure-scriptable (->ClojureScriptableImpl)
        input-objects {"one" 1 "two" 2 "three" 3}
        output-names #{"one" "three"}
        script "(select-keys input-objects output-names)"
        result (.run-script clojure-scriptable input-objects output-names script)]
    (is (= {"one" 1 "three" 3} result))))
