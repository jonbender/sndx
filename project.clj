(defproject sndx "0.1.0-SNAPSHOT"
  :description "Searchable index of simpsons quotes"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [ [org.clojure/clojure "1.4.0"] [org.apache.lucene/lucene-core "4.3.0"] [org.apache.lucene/lucene-analyzers-common "4.3.0"] [org.apache.lucene/lucene-queryparser "4.3.0"] [org.apache.lucene/lucene-highlighter "4.3.0"] ]
  :java-source-paths ["src/main/java"]
  :main sndx.core)
