package edu.spbstu.rle;

import java.io.*;
import java.util.Scanner;

public class Main {


    private static final String EXT = ".rle";
    public static final int BUF_SIZ = 1024;

    private enum Command {PACK, UNPACK}

    public static void main(String[] args) {
        if (args.length >= 2) {
            Command command = Command.PACK;
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
                outputFileName = inputFileName + EXT;
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

    private static void pack(String inputFileName, String outputFileName) {



        StringBuilder sb = new StringBuilder();
        int bufSiz = BUF_SIZ;
        char[] buf = new char[bufSiz];
        try (Reader r = new FileReader(inputFileName)) {
            RleWriter rleWriter = new RleWriter();
            try(RleWriter rleWriter1 = rleWriter) {
                int read;
                while ((read = r.read(buf, 0, bufSiz)) != -1) {
                    rleWriter1.write(buf, 0, read);
                    sb.append(buf, 0, read);
                }
            }
            String rleEncoded = rleWriter.getData();
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

    private static void unpack(String inputFileName, String outputFileName) {
        StringBuilder sb = new StringBuilder();

        try (Reader r = new FileReader(inputFileName)) {
            int mode = r.read();

            Reader reader = r;
            if (mode == 'z') {
                reader = new RleReader(r);
            }
            int bufSiz = BUF_SIZ;
            char[] buf = new char[bufSiz];
            int read;
            while ((read = reader.read(buf, 0, bufSiz)) != -1) {
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