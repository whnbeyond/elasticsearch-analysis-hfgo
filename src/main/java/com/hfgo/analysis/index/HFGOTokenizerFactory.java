package com.hfgo.analysis.index;

import com.hfgo.analysis.lucene.HFGOTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;

public class HFGOTokenizerFactory extends AbstractTokenizerFactory {

    public HFGOTokenizerFactory(IndexSettings indexSettings, String ignored, Settings settings) {
        super(indexSettings, ignored, settings);
    }

    public static HFGOTokenizerFactory getHFGOTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HFGOTokenizerFactory(indexSettings, name, settings);
    }

    @Override
    public Tokenizer create() {
      return new HFGOTokenizer();
    }
}
