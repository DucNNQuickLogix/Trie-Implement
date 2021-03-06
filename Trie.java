/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searching;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nhand
 */
public class Trie {

    //suffixprefix
    private class TrieNode {

        Map<Character, TrieNode> children;
        boolean endOfWord;
        String path="";

        public TrieNode() {
            children = new HashMap<>();
            endOfWord = false;
            //path = "";
        }
    }

    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Iterative implementation of insert into trie
     */
    public void insert(String word, String path) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.children.get(ch);
            if (node == null) {
                node = new TrieNode();
                current.children.put(ch, node);
            }
            current = node;
        }
        //mark the current nodes endOfWord as true
        current.endOfWord = true;
        if(current.endOfWord == true){
            current.path = path;    
        } 
    }

    /**
     * Recursive implementation of insert into trie
     */
    public void insertRecursive(String word) {
        insertRecursive(root, word, 0);
    }

    private void insertRecursive(TrieNode current, String word, int index) {
        if (index == word.length()) {
            //if end of word is reached then mark endOfWord as true on current node
            current.endOfWord = true;
            return;
        }
        char ch = word.charAt(index);
        TrieNode node = current.children.get(ch);

        //if node does not exists in map then create one and put it into map
        if (node == null) {
            node = new TrieNode();
            current.children.put(ch, node);
        }
        insertRecursive(node, word, index + 1);
    }

    /**
     * Iterative implementation of search into trie.
     */
    public String search(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.children.get(ch);
            //if node does not exist for given char then return false
            if (node == null) {
                return "Not found.";
                //return false;
            }
            current = node;
        }
        //return true of current's endOfWord is true else return false.
        return "Found in "+current.path;
    }

    /**
     * Recursive implementation of search into trie.
     */
    public boolean searchRecursive(String word) {
        return searchRecursive(root, word, 0);
    }

    private boolean searchRecursive(TrieNode current, String word, int index) {
        if (index == word.length()) {
            //return true of current's endOfWord is true else return false.
            return current.endOfWord;
        }
        char ch = word.charAt(index);
        TrieNode node = current.children.get(ch);
        //if node does not exist for given char then return false
        if (node == null) {
            return false;
        }
        return searchRecursive(node, word, index + 1);
    }

    /**
     * Delete word from trie.
     */
    public void delete(String word) {
        delete(root, word, 0);
    }

    /**
     * Returns true if parent should delete the mapping
     */
    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            //when end of word is reached only delete if currrent.endOfWord is true.
            if (!current.endOfWord) {
                return false;
            }
            current.endOfWord = false;
            //if current has no other mapping then return true
            return current.children.size() == 0;
        }
        char ch = word.charAt(index);
        TrieNode node = current.children.get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1);

        //if true is returned then delete the mapping of character and trienode reference from map.
        if (shouldDeleteCurrentNode) {
            current.children.remove(ch);
            //return true if no mappings are left in the map.
            return current.children.size() == 0;
        }
        return false;
    }
    
    public static void main(String[] args) {
        Trie dict = new Trie();

        //Read folder
        String path = "D:\\ND-Java\\SearchingOnTrie\\Dataset";
        File file;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                file = listOfFiles[i];
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Scanner readfile = new Scanner(fis);
                    while (readfile.hasNext()) {
                        String tmp = readfile.nextLine().toLowerCase().trim();
                        int j = 0;
                        String tmpsplit[] = tmp.split(" ");
                        for (String w : tmpsplit) {
                            dict.insert(w, listOfFiles[i].getName());
                        }
                        System.out.println(tmp);
                        //dict.insert(tmp, listOfFiles[i].getName());
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Trie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        System.out.println("----------------------------------------------------");

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().toLowerCase().trim();

        while (!input.equals("...")) {
            int j = 0;
            String tmpinput[] = input.split(" ");
 
            double t0 = System.currentTimeMillis();
            for (int i = 0; i < tmpinput.length; i++) {
                System.out.println(tmpinput[i]+" "+dict.search(tmpinput[i]));
                /*
                if (dict.search(tmpinput[i]) == true) {
                    System.out.println(tmpinput[i] + " found in " + dict.root.path);
                } else {
                    System.out.println(tmpinput[i] + " 0");
                }
                */
            }
            double t1 = System.currentTimeMillis();
            double dur = t1-t0;
            System.out.printf("Time to search: %.8f%n", dur);
            System.out.println();
            input = sc.nextLine().toLowerCase().trim();
        }

    }
}
