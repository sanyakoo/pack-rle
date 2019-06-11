package edu.spbstu.rle;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class RleReaderTest {

    public static final int BUF_SIZE = 3;

    @Test
    public void read() throws IOException {
        String str = "qw3a11eadf";
        StringReader stringReader = new StringReader(str);
        try (RleReader reader = new RleReader(stringReader)) {
            char[] buf = new char[BUF_SIZE];
            StringBuilder sb = new StringBuilder();
            int readChars;
            while ((readChars = reader.read(buf)) != -1){
                sb.append(buf, 0, readChars);
            }
            assertEquals("qwaaaeeeeeeeeeeeadf", sb.toString());
        }
    }
    @Test
    public void read1() throws IOException {
        String str = "qwwqwqw";
        StringReader stringReader = new StringReader(str);
        try (RleReader reader = new RleReader(stringReader)) {
            char[] buf = new char[BUF_SIZE];
            StringBuilder sb = new StringBuilder();
            int readChars;
            while ((readChars = reader.read(buf)) != -1){
                sb.append(buf, 0, readChars);
            }
            assertEquals("qwwqwqw", sb.toString());
        }
    }
}