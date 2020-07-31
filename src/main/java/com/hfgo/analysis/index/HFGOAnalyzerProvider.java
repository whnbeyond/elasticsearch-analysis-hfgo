package com.hfgo.analysis.index;

import com.hfgo.analysis.lucene.HFGOAnalyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;

public class HFGOAnalyzerProvider extends AbstractIndexAnalyzerProvider<HFGOAnalyzer> {

    public HFGOAnalyzerProvider(IndexSettings indexSettings, String name, Settings settings) {
        super(indexSettings, name, settings);
    }

    public static HFGOAnalyzerProvider getIkAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HFGOAnalyzerProvider(indexSettings,name,settings);
    }

    @Override public HFGOAnalyzer get() {
        return new HFGOAnalyzer();
    }
}
