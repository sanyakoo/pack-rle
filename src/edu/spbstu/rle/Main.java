package edu.spbstu.rle;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class Main {


    public static final String EXT = ".rle";
    public static final String UNPACKED_EXT = ".unpacked";
    private static final int BUF_SIZE = 1024;

    private enum Command {PACK, UNPACK}

    public static void main(String... args) {
        if (args.length >= 2) {
            Command command;
            String cmdOption = args[0];
            switch (cmdOption) {
                case "-z":
                    command = Command.PACK;
                    break;
                case "-u":
                    command = Command.UNPACK;
                    break;
                default:
                    printUsage();
                    return;
            }
            String inputFileName;
            String outputFileName;
            if (args[1].equalsIgnoreCase("-out")) {
                if (args.length >= 4) {
                    outputFileName = args[2];
                    inputFileName = args[3];
                } else {
                    printUsage();
                    return;
                }
            } else {
                inputFileName = args[1];
                outputFileName = inputFileName + ((command == Command.PACK)? EXT: UNPACKED_EXT);
            }

            switch (command) {
                case PACK:
                    pack(inputFileName, outputFileName);
                    break;
                case UNPACK:
                    unpack(inputFileName, outputFileName);
                    break;
            }
        } else {
            printUsage();
            return;
        }
    }

    public static void pack(String inputFileName, String outputFileName) {
        StringBuilder sb = new StringBuilder();
        int bufSize = BUF_SIZE;
        char[] buf = new char[bufSize];
        try (Reader r = new FileReader(inputFileName)) {
            StringWriter rleSb = new StringWriter();
            try(Writer rleWriter = new RleWriter(rleSb)) {
                int read;
                while ((read = r.read(buf, 0, bufSize)) != -1) {
                    rleWriter.write(buf, 0, read);
                    sb.append(buf, 0, read);
                }
            }
            String rleEncoded = rleSb.toString();
            String plainStr = sb.toString();
            if (rleEncoded.length() >= plainStr.length()) {
                saveToFile('p', plainStr, outputFileName);
            } else {
                saveToFile('z', rleEncoded, outputFileName);
            }

        } catch (FileNotFoundException e) {
            System.out.println("No such file: " + inputFileName);
        } catch (IOException e) {
            System.out.println("Failed to read file: " + inputFileName);
        }
    }

    private static void saveToFile(Character mode, String data, String outputFileName) {
        try (Writer w = new FileWriter(outputFileName)) {
            if (mode != null) {
                w.write(mode);
            }
            w.write(data);
        } catch (IOException e) {
            System.out.println("Failed to write  to file: " + outputFileName);
        }
    }

    public static void unpack(String inputFileName, String outputFileName) {
        StringBuilder sb = new StringBuilder();

        try (Reader r = new FileReader(inputFileName)) {
            int mode = r.read();

            Reader reader = r;
            if (mode == 'z') {
                // erapping reader with RLE reader to
                reader = new RleReader(r);
            }
            int bufSize = BUF_SIZE;
            char[] buf = new char[bufSize];
            int read;
            while ((read = reader.read(buf, 0, bufSize)) != -1) {
                sb.append(buf, 0, read);
            }

        } catch (FileNotFoundException e) {
            System.out.println("No such file: " + inputFileName);
        } catch (IOException e) {
            System.out.println("Failed to read file: " + inputFileName);
        }
        saveToFile(null, sb.toString(), outputFileName);


    }

    private static void printUsage() {
        System.out.println("Command Line: pack-rle [-z|-u] [-out outputname.txt] inputname.txt");
    }
}