package com.hfgo.analysis.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public final class HFGOAnalyzer extends Analyzer {

	/**
	 * 重载Analyzer接口，构造分词组件
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer hfgoTokenizer = new HFGOTokenizer();
		HFGOFilter hfgoFilter = new HFGOFilter(hfgoTokenizer);
		return new TokenStreamComponents(hfgoTokenizer,hfgoFilter);
    }

}
