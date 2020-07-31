package com.hfgo.analysis.lucene;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Created by haonanwang on 2020/7/31.
 */
public class HFGOFilter extends TokenFilter {

    private final CharTermAttribute termAtt		= addAttribute(CharTermAttribute.class);

    public HFGOFilter(TokenStream input) {
        super(input);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        boolean res = this.input.incrementToken();
        if (res) {
            char[] chars = termAtt.buffer();
            int length = termAtt.length();
            if(length > 0){
                for(int i=0;i<length;i++){
                    chars[i]= Character.toLowerCase(chars[i]);
                }
            }
        }
        return res;
    }
}
