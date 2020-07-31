package com.hfgo.analysis.plugin;

import com.hfgo.analysis.index.HFGOAnalyzerProvider;
import com.hfgo.analysis.index.HFGOTokenizerFactory;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;


public class AnalysisHFGOPlugin extends Plugin implements AnalysisPlugin {

	public static String PLUGIN_NAME = "analysis-hfgo";

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();

        extra.put("hfgo_max_word", HFGOTokenizerFactory::getHFGOTokenizerFactory);

        return extra;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

        extra.put("hfgo_max_word", HFGOAnalyzerProvider::getIkAnalyzerProvider);

        return extra;
    }

}
