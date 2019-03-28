package edu.spbstu.rle;

import java.io.IOException;
import java.io.Writer;

public class RleWriter extends Writer {

    private StringBuilder sb = new StringBuilder();
    private Character curChar = null;
    private int counter = 0;

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
                    sb.append(counter);
                    sb.append(curChar);
                    curChar = cbuf[i];
                    counter = 1;
                }
            }
        }
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
        sb.append(counter);
        sb.append(curChar);
    }

    public String getData() {
        return sb.toString();
    }
}