package com.hfgo.analysis.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;

public final class HFGOTokenizer extends Tokenizer {

	private Log	LOG	 = LogFactory.getLog(HFGOTokenizer.class);
	private char[] buffer = new char[4096];
	private int baseOffset = 0;
	private int soldierFlag = 0;
	private static int MAX_SIZE;
	private int termSize;

	private final CharTermAttribute	termAtt		= addAttribute(CharTermAttribute.class);
	private final OffsetAttribute	offsetAtt	= addAttribute(OffsetAttribute.class);

	@Override
	public boolean incrementToken() throws IOException {
		try {
			clearAttributes();

			if (termSize != -1) {
				termSize = input.read(buffer);
				if (termSize >= 0) {
					MAX_SIZE = termSize;
				}
			}

			if (baseOffset >= MAX_SIZE) {
				return false;
			}

			termAtt.copyBuffer(buffer, baseOffset, baseOffset + (MAX_SIZE - soldierFlag));

			termAtt.setLength(MAX_SIZE - soldierFlag);
			if (baseOffset >= 0
					&& (baseOffset + (MAX_SIZE - soldierFlag)) >= 0
					&& (baseOffset + (MAX_SIZE - soldierFlag)) >= baseOffset) {
				offsetAtt.setOffset(correctOffset(baseOffset), correctOffset(baseOffset + (MAX_SIZE - soldierFlag)));
			}
			soldierFlag++;
			if (soldierFlag >= MAX_SIZE) {
				baseOffset++;
				soldierFlag = baseOffset;
			}


		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return true;
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		synchronized (this) {
			this.soldierFlag = 0;
			this.baseOffset = 0;
			this.termSize = 1;
			MAX_SIZE = 0;
		}
	}

	@Override
	public void end() throws IOException {
		super.end();
		synchronized (this) {
			int finalOffset = MAX_SIZE-1;
			if (finalOffset >= 0) {
				offsetAtt.setOffset(correctOffset(finalOffset), correctOffset(finalOffset));
			}
		}
	}
}
