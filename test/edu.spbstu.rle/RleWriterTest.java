package edu.spbstu.rle;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class RleWriterTest {
    public static final int BUF_SIZE = 3;


    @Test
    public void write() throws IOException {

        try (Reader r = new StringReader("eeeerrrrryyyyyyyyyyyyyyyyyyyyyyyyyyyfagttyyh")) {

            StringWriter rleSb = new StringWriter();

            try (Writer rleWriter = new RleWriter(rleSb)) {
                int read;
                char[] buf = new char[BUF_SIZE];

                while ((read = r.read(buf, 0, BUF_SIZE)) != -1) {
                    rleWriter.write(buf, 0, read);
                }
            }

            String rleEncoded = rleSb.toString();
            assertEquals("4e5r27yfagttyyh", rleEncoded);
        }
    }
    @Test
    public void write1() throws IOException {

        try (Reader r = new StringReader("eerr")) {

            StringWriter rleSb = new StringWriter();

            try (Writer rleWriter = new RleWriter(rleSb)) {
                int read;
                char[] buf = new char[BUF_SIZE];

                while ((read = r.read(buf, 0, BUF_SIZE)) != -1) {
                    rleWriter.write(buf, 0, read);
                }
            }

            String rleEncoded = rleSb.toString();
            assertEquals("eerr", rleEncoded);
        }
    }
}