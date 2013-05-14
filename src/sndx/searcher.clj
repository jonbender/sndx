(ns sndx.searcher
  (:gen-class)
  (:use [clojure.string :only (split)])
  (:import (org.apache.lucene.analysis Analyzer) (org.apache.lucene.analysis.standard StandardAnalyzer) (org.apache.lucene.document Document Field Field$Store LongField StringField TextField) (org.apache.lucene.index IndexWriter IndexWriterConfig IndexWriterConfig$OpenMode Term DirectoryReader IndexReader) (org.apache.lucene.store Directory FSDirectory) (org.apache.lucene.util Version) (org.apache.lucene.search Query PhraseQuery ScoreDoc TopDocs IndexSearcher) (org.apache.lucene.search.highlight Highlighter QueryScorer) (org.apache.lucene.queryparser.classic QueryParser))
)

(defonce analyzer (StandardAnalyzer. Version/LUCENE_40))
(defonce parser (QueryParser. Version/LUCENE_40 "contents" analyzer))
(defn query-index [q-string]
  (with-open [directory (FSDirectory/open (java.io.File. "index"))]
    (with-open [ireader (DirectoryReader/open directory)]
      (let [isearcher (IndexSearcher. ireader) query (.parse parser q-string) hits (seq (.scoreDocs (.search isearcher query nil 100))) ]
         (def map-hits (map #(.doc %) (take 5 hits)))
        (doall (map #(.doc isearcher %) map-hits)))
    )
))

(defn get-contents [d] 
  (slurp (.get d "path")))

(defn get-best-fragments [contents highlighter]
  (.getBestFragments highlighter analyzer "contents" contents 10)
  )

(defn get-fragments [q-string]
  (let [query (.parse parser q-string) highlighter (Highlighter. (QueryScorer. query)) 
        query-results (query-index q-string) contents-seq (map get-contents query-results)]
        (mapcat #(get-best-fragments % highlighter) contents-seq))
)
