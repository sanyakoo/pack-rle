package edu.spbstu.rle;

import java.io.IOException;
import java.io.Writer;

/**
 * RLE encoder. Writes encoded text to underlying stream
 */
public class RleWriter extends Writer {

    private Writer writer;
    private Character curChar = null;
    private int counter = 0;

    public RleWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (len > 0) {
            if (curChar == null) {
                curChar = cbuf[off];
            }
            for (int i = off; i < off + len; i++) {
                if (curChar.equals(cbuf[i])) {
                    counter++;
                } else {
                    appendEncodedSequence(counter, curChar);
                    curChar = cbuf[i];
                    counter = 1;
                }
            }
        }
    }

    private void appendEncodedSequence(int counter, char curChar) throws IOException {
        if (counter > 2) {
            writer.append("" + counter);
            writer.append(curChar);
        } else {
            // if sequence consist of 1 or 2 symbols:
            for (int j = 0; j < counter; j++) {
                writer.append(curChar);
            }
        }
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        appendEncodedSequence(counter, curChar);
    }
}