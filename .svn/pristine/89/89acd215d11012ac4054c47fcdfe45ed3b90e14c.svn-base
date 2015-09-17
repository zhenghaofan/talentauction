package com.auction.util;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.apache.lucene.analysis.util.FilesystemResourceLoader;
import org.wltea.analyzer.lucene.IKTokenizer;

/** 
 * 可以加载同义词库的Lucene 
 * 专用IK分词器 
 *  
 *  
 * */  
public class IKSynonymsAnalyzer extends Analyzer {  
  
       
    @Override  
    protected TokenStreamComponents createComponents(String arg0, Reader arg1) {  
          
        Tokenizer token=new IKTokenizer(arg1, true);//开启智能切词  
          
        Map<String, String> paramsMap=new HashMap<String, String>();  
        paramsMap.put("luceneMatchVersion", "LUCENE_43");
        paramsMap.put("synonyms", "lucene/synonyms.txt");
        SynonymFilterFactory factory=new SynonymFilterFactory(paramsMap);  
        try {
			factory.inform(new FilesystemResourceLoader());
		} catch (IOException e) {
			e.printStackTrace();
		}
        return new TokenStreamComponents(token, factory.create(token));  
    }  
      
      
      
  
}  
