package org.gesis.genconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bensmafx
 */
public class App {

    private static final String LIMES = "limes";
    private static final String SILK = "silk";
    private static final String USAGE = "Usage: <" + LIMES + "|" + SILK + "> <src dir> <tgt dir> <template> <out dir>";

    private static final String KEYWORD_SOURCEFILE = "SOURCEFILE";
    private static final String KEYWORD_TARGETFILE = "TARGETFILE";
    private static final String KEYWORD_ACCEPTFILE = "ACCEPTFILE";
    private static final String KEYWORD_REVIEWFILE = "REVIEWFILE";
    private static final String KEYWORD_SOURCEID = "SOURCEID";
    private static final String KEYWORD_TARGETID = "TARGETID";

    private static final String EXTENSION_XML = "xml";
    private static final String EXTENSION_LSL = "lsl";

    public static void main(String[] args) {
        //check param count
        if (args.length != 5) {
            System.out.println(USAGE);
            System.exit(0);
        }
        //check limes or silk
        String tool = args[0];
        if (!tool.equalsIgnoreCase(LIMES) && !tool.equalsIgnoreCase(SILK)) {
            System.out.println("Choose either " + LIMES + " or " + SILK + ".");
            System.out.println(USAGE);
            System.exit(0);
        }

        //check 1st input dir
        File inDir1 = new File(args[1]);
        if (!inDir1.isDirectory() || !inDir1.exists()) {
            System.out.println("Invalid source directory.");
            System.out.println(USAGE);
            System.exit(0);
        }
        //check 2nd input dir
        File inDir2 = new File(args[2]);
        if (!inDir2.exists()) {
            inDir2.mkdirs();
        } else if (!inDir2.isDirectory()) {
            System.out.println("Invalid target directory.");
            System.out.println(USAGE);
            System.exit(0);
        }
        //check template file
        File templateFile = new File(args[3]);
        if (!templateFile.exists() || !templateFile.isFile()) {
            System.out.println("Invalid template file.");
            System.out.println(USAGE);
            System.exit(0);
        }
        //check output dir
        File outDir = new File(args[4]);
        if(!outDir.exists() || !outDir.isDirectory()){
            System.out.println("Invalid output directory.");
            System.out.println(USAGE);
            System.exit(0);
        }
        

        ArrayList<Pair> pairList = new ArrayList<Pair>();

        //find pairs
        for (File src : inDir1.listFiles()) {
            for (File tgt : inDir2.listFiles()) {
                if (arePairable(src, tgt)) {
                    pairList.add(new Pair(src, tgt));
                }
            }
        }

        //generate configuration file
        //for limes
        if (tool.equals(LIMES)) {
            Pair p = null;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile), "UTF-8"));
                String line = br.readLine();
                String content = "";
                while (line != null) {
                    content += line + "\n";
                    line = br.readLine();

                }
                br.close();

                int cnt = 0;
                for (Pair pair : pairList) {
                    cnt++;
                    p = pair;
                    String newContent = content.replaceAll(KEYWORD_SOURCEFILE, pair.getSourceFile().getAbsolutePath());
                    newContent = newContent.replaceAll(KEYWORD_TARGETFILE, pair.getTargetFile().getAbsolutePath());
                    newContent = newContent.replaceAll(KEYWORD_ACCEPTFILE, "accept_" + removeExtension(pair.getSourceFile().getName()) + "-" + pair.getTargetFile().getName());
                    newContent = newContent.replaceAll(KEYWORD_REVIEWFILE, "review_" + removeExtension(pair.getSourceFile().getName()) + "-" + pair.getTargetFile().getName());
                    newContent = newContent.replaceAll(KEYWORD_SOURCEID, "src_"+removeExtension(pair.getSourceFile().getName()));
                    newContent = newContent.replaceAll(KEYWORD_TARGETID, "tgt_"+removeExtension(pair.getTargetFile().getName()));
                    FileWriter fw = new FileWriter(new File(outDir, LIMES + "_config_" + removeExtension(pair.getSourceFile().getName()) + "-" + removeExtension(pair.getTargetFile().getName()) + "." + EXTENSION_XML));
                    fw.write(newContent);
                    fw.close();
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Error when generating config file for " + p.toString() + ex);
                System.exit(-1);
            } catch (UnsupportedEncodingException ex) {
                System.err.println("Error when generating config file for " + p.toString() + ex);
                System.exit(-1);
            } catch (IOException ex) {
                System.err.println("Error when generating config file for " + p.toString() + ex);
                System.exit(-1);
            }

        } else if (tool.equals(SILK)) {
            Pair p = null;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile), "UTF-8"));
                String line = br.readLine();
                String content = "";
                while (line != null) {
                    content += line + "\n";
                    line = br.readLine();

                }
                br.close();

                for (Pair pair : pairList) {
                    p = pair;
                    String newContent = content.replaceAll(KEYWORD_SOURCEFILE, pair.getSourceFile().getAbsolutePath());
                    newContent = newContent.replaceAll(KEYWORD_TARGETFILE, pair.getTargetFile().getAbsolutePath());
                    newContent = newContent.replaceAll(KEYWORD_ACCEPTFILE, "accept_" + removeExtension(pair.getSourceFile().getName()) + "-" + pair.getTargetFile().getName());
                    newContent = newContent.replaceAll(KEYWORD_REVIEWFILE, "review_" + removeExtension(pair.getSourceFile().getName()) + "-" + pair.getTargetFile().getName());
                    newContent = newContent.replaceAll(KEYWORD_SOURCEID, "src_"+removeExtension(pair.getSourceFile().getName()));
                    newContent = newContent.replaceAll(KEYWORD_TARGETID, "tgt_"+removeExtension(pair.getTargetFile().getName()));
                    FileWriter fw = new FileWriter(new File(outDir, SILK + "_config_" + removeExtension(pair.getSourceFile().getName()) + "-" + removeExtension(pair.getTargetFile().getName()) + "." + EXTENSION_LSL));
                    fw.write(newContent);
                    fw.close();
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Error when generating config file for " + p.toString() + ex);
                System.exit(-1);
            } catch (UnsupportedEncodingException ex) {
                System.err.println("Error when generating config file for " + p.toString() + ex);
                System.exit(-1);
            } catch (IOException ex) {
                System.err.println("Error when generating config file for " + p.toString() + ex);
                System.exit(-1);
            }

        }

    }

    private static String removeExtension(String name) {
        int idx = name.lastIndexOf(".");
        String stub = name.substring(0, idx);
        return stub;
    }

    private static boolean arePairable(File fA, File fB) {

        String nameA = fA.getName();
        int idx = nameA.lastIndexOf("_");
        if (idx >= 0) {
            nameA = nameA.substring(0, idx);
        }

        String nameB = fB.getName();
        idx = nameB.lastIndexOf("_");
        if (idx >= 0) {
            nameB = nameB.substring(0, idx);
        }

        if (nameA.equals(nameB)) {
            return true;
        }
        return false;
    }

}
