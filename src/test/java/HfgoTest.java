import com.hfgo.analysis.lucene.HFGOAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

/**
 * Created by haonanwang on 2020/7/29.
 */
public class HfgoTest {
    @Test
    public void testAnalyzer() throws Exception {
        HFGOAnalyzer analyzer = new HFGOAnalyzer();
        TokenStream ts = analyzer.tokenStream("text", "中国联");
        CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        while (ts.incrementToken()) {
            System.out.println(term.toString());
        }
        ts.end();
        ts.close();
    }

}
