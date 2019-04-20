import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    TreeMap<String, TreeMap<String, Set<String>>> dictionary = new TreeMap<>();
    private static Stack<File> files;
    static double q1 = 0.5;
    static double q2 = 0.2;
    static double q3 = 0.3;

    public Main() throws IOException {
        File folder = new File("/Users/ansttss/Documents/Info7/books");
        File[] listOfFiles = folder.listFiles();
        files = new Stack();
        for (int f = 0; f < listOfFiles.length; f++) {
            files.push(listOfFiles[f]);
        }
        while (!files.empty()) {
            File current = files.pop();
            if (current.getName().substring(current.getName().indexOf('.') + 1, current.getName().length()).equals("fb2")) {
                String text = readSection(current);
                StringTokenizer stringTokenizer = new StringTokenizer(text, " 0123456789….,:;!?\"%„[]+\t“”|‘’'-—/№*~()<>«»");
                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken();
                    TreeMap<String, Set<String>> temp = dictionary.get(token);
                    if (temp == null)
                        temp = new TreeMap<>();
                    Set<String> tempSet = temp.get("body");
                    if (tempSet == null)
                        tempSet = new TreeSet<>();
                    tempSet.add(current.getName());
                    temp.put("body", tempSet);
                    dictionary.put(token, temp);
                }


                String author = readAuthor(current);
                stringTokenizer = new StringTokenizer(author);
                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken();
                    TreeMap<String, Set<String>> temp = dictionary.get(token);
                    if (temp == null)
                        temp = new TreeMap<>();
                    Set<String> tempSet = temp.get("author");
                    if (tempSet == null)
                        tempSet = new TreeSet<>();
                    tempSet.add(current.getName());
                    temp.put("author", tempSet);
                    dictionary.put(token, temp);
                }

                String title = readTitle(current);
                stringTokenizer = new StringTokenizer(title);
                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken();
                    TreeMap<String, Set<String>> temp = dictionary.get(token);
                    if (temp == null)
                        temp = new TreeMap<>();
                    Set<String> tempSet = temp.get("title");
                    if (tempSet == null)
                        tempSet = new TreeSet<>();
                    tempSet.add(current.getName());
                    temp.put("title", tempSet);
                    dictionary.put(token, temp);
                }
            }
        }
    }

    String readAuthor(File file) throws IOException {
        StringTokenizer tokenizer;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String author = "";
        String line = bufferedReader.readLine();
        boolean bool = true;
        while (line != null && line.length() != 0) {
            line = line.substring(line.indexOf('<') + 1, line.length());
            tokenizer = new StringTokenizer(line, " >");
            while (tokenizer.hasMoreTokens()) {
                String field = tokenizer.nextToken();
                if (field.equals("author")) {
                    bool = false;
                    break;
                }
            }
            if (bool)
                line = bufferedReader.readLine();
            else {
                bool = true;
                tokenizer = new StringTokenizer(line, "<> ");
                while (tokenizer.hasMoreTokens()) {
                    if (tokenizer.nextToken().equals("/author")) {
                        bool = false;
                        break;
                    }
                }
                break;
            }
        }
        boolean bool1 = true;
        if (bool) {
            String line1 = bufferedReader.readLine();
            while (line1 != null) {
                tokenizer = new StringTokenizer(line1, "<> ");
                while (tokenizer.hasMoreTokens()) {
                    if (tokenizer.nextToken().equals("/author")) {
                        bool1 = false;
                        break;
                    }
                }
                if (bool1) {
                    line += ' ' + line1;
                    line1 = bufferedReader.readLine();
                } else break;
            }
        }
        while (line.length() != 0) {
            if (line.indexOf('<') != -1) {
                String word = line.substring(line.indexOf('>') + 1, line.indexOf('<'));
                if (word.length() > 0)
                    author += word + ' ';

                line = line.substring(line.indexOf('<') + 1, line.length());
            } else break;
        }

        return author;
    }


    String readSection(File file) throws IOException {
        StringTokenizer tokenizer;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String section = "";
        String line = bufferedReader.readLine();
        boolean bool = true;
        while (line != null && line.length() != 0) {
            line = line.substring(line.indexOf('<') + 1, line.length());
            tokenizer = new StringTokenizer(line, " >");
            while (tokenizer.hasMoreTokens()) {
                String field = tokenizer.nextToken();
                if (field.equals("section")) {
                    bool = false;
                    break;
                }
            }
            if (bool)
                line = bufferedReader.readLine();
            else {
                //checking if it is in one line
                bool = true;
                tokenizer = new StringTokenizer(line, "<> ");
                while (tokenizer.hasMoreTokens()) {
                    if (tokenizer.nextToken().equals("/body")) {
                        bool = false;
                        break;
                    }
                }
                break;
            }
        }

        boolean bool1 = true;
        if (bool) {
            String line1 = bufferedReader.readLine();
            while (line1 != null) {
                tokenizer = new StringTokenizer(line1, "<> ");
                while (tokenizer.hasMoreTokens()) {
                    if (tokenizer.nextToken().equals("/section")) {
                        bool1 = false;
                        break;
                    }
                }
                if (bool1) {
                    line += ' ' + line1;
                    line1 = bufferedReader.readLine();
                } else break;
            }
        }

        while (line.length() != 0) {
            if (line.indexOf('<') != -1) {
                String word = line.substring(line.indexOf('>') + 1, line.indexOf('<'));
                if (word.length() > 0)
                    section += word + ' ';

                line = line.substring(line.indexOf('<') + 1, line.length());
            } else break;
        }

        return section;
    }


    String readTitle(File file) throws IOException {
        StringTokenizer tokenizer;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String title = "";
        String line = bufferedReader.readLine();
        boolean bool = true;
        while (line != null && line.length() != 0) {
            line = line.substring(line.indexOf('<') + 1, line.length());
             tokenizer = new StringTokenizer(line, " >");
            while (tokenizer.hasMoreTokens()) {
                String field = tokenizer.nextToken();
                if (field.equals("book-title")) {
                    bool = false;
                    break;
                }
            }
            if (bool)
                line = bufferedReader.readLine();
            else {
                bool = true;
                tokenizer = new StringTokenizer(line, "<> ");
                while (tokenizer.hasMoreTokens()) {
                    if (tokenizer.nextToken().equals("/book-title")) {
                        bool = false;
                        break;
                    }
                }
                break;
            }
        }

        boolean bool1 = true;
        if (bool) {
            String line1 = bufferedReader.readLine();
            while (line1 != null) {
                tokenizer = new StringTokenizer(line1, "<> ");
                while (tokenizer.hasMoreTokens()) {
                    if (tokenizer.nextToken().equals("/book-title")) {
                        bool1 = false;
                        break;
                    }
                }
                if (bool1) {
                    line += ' ' + line1;
                    line1 = bufferedReader.readLine();
                } else break;
            }
        }

        while (line.length() != 0) {
            if (line.indexOf('<') != -1) {
                String word = line.substring(line.indexOf('>') + 1, line.indexOf('<'));
                if (word.length() > 0)
                    title += word + ' ';

                line = line.substring(line.indexOf('<') + 1, line.length());
            } else break;
        }

        return title;
    }

    private void search(String author, String title, String body) {
        ArrayList<String> authorDocs = new ArrayList<>();
        for (String s : dictionary.get(author).get("author"))
            authorDocs.add(s);
        ArrayList<String> titleDocs = new ArrayList<>();
        for (String s : dictionary.get(title).get("title"))
            titleDocs.add(s);
        ArrayList<String> bodyDocs = new ArrayList<>();
        for (String s : dictionary.get(body).get("body"))
            bodyDocs.add(s);
        TreeMap<String, Double> map = new TreeMap<>();

        for (String s : authorDocs) {
            if (map.get(s) == null)
                map.put(s, 0.0);
            map.put(s, map.get(s) + q1);
        }
        for (String s : titleDocs) {
            if (map.get(s) == null)
                map.put(s, 0.0);
            map.put(s, map.get(s) + q2);
        }
        for (String s : bodyDocs) {
            if (map.get(s) == null)
                map.put(s, 0.0);
            map.put(s, map.get(s) + q3);
        }

        for (Object o : map.keySet())
            if (map.get(o) == 1)
                System.out.println(o.toString() + " " + map.get(o));
        for (Object o : map.keySet())
            if (map.get(o) == 0.8)
                System.out.println(o.toString() + " " + map.get(o));
        for (Object o : map.keySet())
            if (map.get(o) == 0.7)
                System.out.println(o.toString() + " " + map.get(o));
        for (Object o : map.keySet())
            if (map.get(o) == 0.5)
                System.out.println(o.toString() + " " + map.get(o));
        for (Object o : map.keySet())
            if (map.get(o) == 0.3)
                System.out.println(o.toString() + " " + map.get(o));
        for (Object o : map.keySet())
            if (map.get(o) == 0.2)
                System.out.println(o.toString() + " " + map.get(o));

    }

    public static void main(String[] args) {
        try {
            Main main = new Main();
            main.search("Leo", "Anna", "unhappy");
        }
        catch (NullPointerException e)
        {
            System.out.println("No files found!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
