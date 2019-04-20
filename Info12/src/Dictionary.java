//  Created by Anastasia Posvystak on 25.01.19.
//  Copyright © 2019 Anastasia Posvystak. All rights reserved.

//
//
//RETRIEVAL WITH DISTANCE
//
//

import java.io.*;
import java.util.*;

public class Dictionary {
    static HashMap<String, Map<Integer, Integer>> map =new HashMap<>();
    static int[] lengths;
    static int docID = 1;
    static int filesSize;
    static ArrayList<File> files;
    static HashMap<String, Integer>[] fileMaps;
    static int averageSize = 0;

    static void index() {
        files = new ArrayList<>();
        try {
            File folder = new File("/Users/ansttss/Documents/IR/Info12/jk");
            File[] listOfFiles = folder.listFiles();
//                assert listOfFiles != null;
            for (int f = 0; f < listOfFiles.length; f++) {
                files.add(listOfFiles[f]);
            }
            filesSize = files.size();
            fileMaps = new HashMap[filesSize];
            lengths = new int[filesSize];
            for (File f:files) {
                int length = 0;
                fileMaps[docID - 1] = new HashMap<>();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    StringBuilder stringBuilder = new StringBuilder();
                    String string;
                    while ((string = br.readLine()) != null) {
                        string = string.toLowerCase();
                        stringBuilder.append(string + " ");
                    }
                    StringTokenizer stringTokenizer = new StringTokenizer(stringBuilder.toString(), " 0123456789….,:;!?\"%„[]+\t“”|‘’'-—/№*~()<>«»");
                    while (stringTokenizer.hasMoreTokens()) {
                            String token = stringTokenizer.nextToken();
                            length++;
                            Map temp = map.get(token);
                            if (temp == null) {
                                temp = new TreeMap();
                            }
                            Integer value = (Integer) temp.get(docID);
                            if (value == null) {
                                value = 0;
                            }
                            value++;
                            fileMaps[docID - 1].put(token, value);
                            temp.put(docID, value);
                            map.put(token, temp);
                        }
                    lengths[docID - 1] = length;
                docID++;
                } catch (FileNotFoundException e) {
                    System.out.println("File/s was/were not found");
                } catch (IOException e) {
                    System.out.println("Something went wrong!");
                }
            }
            System.out.println("Amount of unique words: " + map.size());

            for (int i = 0; i < filesSize; i++)
                averageSize += lengths[i];
            averageSize /= filesSize;

            PrintWriter printWriter = new PrintWriter("result.txt");
            Object[] keys = map.keySet().toArray();
            Arrays.sort(keys);
            for (Object key : keys) {
                printWriter.print(key + ": ");
                for (Object key1 : map.get(key).keySet())
                    printWriter.print(key1 + " - " + map.get(key).get(key1) + ", ");
                printWriter.println();
            }
            printWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Dictionary.index();
        BM25 bm25 = new BM25();
        int counter = 1;
        while (counter == 1) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Your request: ");
            String request = scanner.nextLine();
            if (request.length() < 1)
                System.out.println("It is empty, therefore all documents are relevant");
            else {
                request = request.toLowerCase();
                for (int i = 0; i < filesSize; i++) {
                    if (fileMaps[i].get(request) == null || map.get(request) == null)
                        System.out.println("doc" + (i+1) + " -> " + 0);
                    else
                        System.out.println("doc" + (i+1) + " - " + bm25.rank(files, lengths[i], request, fileMaps[i].get(request), map.get(request).size()));
                }
            }
            System.out.println("Do you want to continue? yes - 1, no - 0 ");
            counter = scanner.nextInt();
        }
    }
}
