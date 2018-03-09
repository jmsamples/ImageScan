package com.github.jmsamples;

import com.google.common.io.Files;
import com.google.common.io.ByteSource;

import java.io.*;
import java.util.*;

/**
 *
 * ImageScan calculates the sum of the first 1000 bytes
 * of all jpg files in a given directory.
 *
 * Specify the input directory as a program argument
 * 
 * Please refer to README.md
 *
 * e.g java -jar ./build/libs/ImageScan-all-1.0.jar [dir]
 * where [dir] is the location of the image files.
 *
 * Please refer to README.md
 *
 * Sample output:
 *
 *  1961: look3.jpg
 *  2572: look1.jpg
 *  4796: look2.jpg
 *
 * @author     JM
 *
 */
public class ImageScan {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: ImageScan [dir]");
        } else {
            try {
                ImageScan app = new ImageScan();
                //accept the directory as program argument
                File dir = new File(args[0]);
                if (dir.exists()) {
                    app.start(dir);
                } else {
                    System.err.print("Directory does not exist " + dir);
                }
            } catch (Exception e) {
                System.err.print(e);
            }
        }

    }

    public void start(File dir) {

        //find all jpg files in specified directory
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });

        ArrayList<Item> entries = new ArrayList<>();
        try {
            //create a list of entries of images and their sum
            for (File file : files) {
                int sum = displayFileSize(file);
                entries.add(new Item(file.getName(), sum));
            }
        } catch (IOException e) {
            System.err.print(e);
        }

        //sort the entries by value (sum)
        Collections.sort(entries);

        //display in the format required
        for (Item item : entries) {
            System.out.println(item.getSum() + ": " + item.getFileName());
        }
    }

    public int displayFileSize(File file) throws IOException {
        int sum = 0;
        try (InputStream is = new FileInputStream(file)) {
            //only read 1000 bytes
            ByteSource source = Files.asByteSource(file).slice(0, 1000);
            byte[] bytes = source.read();
            for (int x = 0; x < bytes.length; x++) {
                sum += bytes[x];
            }
        }
        return sum;
    }


    private static class Item implements Comparable<Item> {
        private String fileName;
        private Integer sum;

        public Item(String fileName, Integer sum) {
            this.fileName = fileName;
            this.sum = sum;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Integer getSum() {
            return sum;
        }

        public void setSum(Integer sum) {
            this.sum = sum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equals(fileName, item.fileName) &&
                    Objects.equals(sum, item.sum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fileName, sum);
        }

        @Override
        public int compareTo(Item o) {
            return sum.compareTo(o.sum);
        }
    }


}