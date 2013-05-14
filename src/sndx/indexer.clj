(ns sndx.indexer
  (:gen-class)
  (:import (org.apache.lucene.analysis Analyzer) (org.apache.lucene.analysis.standard StandardAnalyzer) (org.apache.lucene.document Document Field Field$Store LongField StringField TextField) (org.apache.lucene.index IndexWriter IndexWriterConfig IndexWriterConfig$OpenMode Term) (org.apache.lucene.store Directory FSDirectory) (org.apache.lucene.util Version)))

(defn get-all-files "Gets a list of files recursively - expects a java.io.File object"
  [dir]
  (if (.isFile dir) (list dir)
  (mapcat get-all-files (seq (.listFiles dir)))))

(defn add-document [file writer]
  (let [doc (Document.) path-field (StringField. "path" (.getPath file) (Field$Store/YES)) date-field (LongField. "modified" (.lastModified file) Field$Store/NO) 
        contents (java.io.BufferedReader. (java.io.InputStreamReader. (java.io.FileInputStream. file) "UTF-8"))]
  (do
      (.add doc path-field)
      (.add doc date-field)
      (.add doc (TextField. "contents" contents)) 
      (.addDocument writer doc)
  (.getPath file)
  (.close contents)  
)))

(defn index-docs [source-path target-path] "Indexes documents from a source path to a target directory" 
  (let [files (get-all-files (java.io.File. source-path)) analyzer (StandardAnalyzer. Version/LUCENE_40) iwc (IndexWriterConfig. Version/LUCENE_40 analyzer)
        dir (FSDirectory/open (java.io.File. target-path))]
    (do (.setOpenMode iwc IndexWriterConfig$OpenMode/CREATE)
        (def writer (IndexWriter. dir iwc))
        (doseq [file files] (add-document file writer))
        (.close writer)) 
    )
)
#_(print (doc get-all-files))
#_(print (get-all-files (java.io.File. "data")))
;;(index-docs "data" "index")
