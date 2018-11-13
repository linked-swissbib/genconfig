package org.gesis.genconfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
 * @author bensmafx, Jonas Waeber
 */
public class App {
    private static final String USAGE = "Usage: <src dir> <tgt dir> <template> <config_out dir> <output_dir>";

    private static final String KEYWORD_SOURCEFILE = "SOURCEFILE";
    private static final String KEYWORD_TARGETFILE = "TARGETFILE";
    private static final String KEYWORD_ACCEPTFILE = "ACCEPTFILE";
    private static final String KEYWORD_REVIEWFILE = "REVIEWFILE";
    private static final String KEYWORD_SOURCEID = "SOURCEID";
    private static final String KEYWORD_TARGETID = "TARGETID";

    private static final String EXTENSION_XML = "xml";

    private static final Logger logger = Logger.getLogger("Config-Generator");

    public static void main(String[] args) {
        //check param count
        if (args.length != 5) {
            System.out.println(USAGE);
            System.exit(0);
        }

        //check 1st input dir
        File inDir1 = new File(args[0]);
        if (!inDir1.exists() || !inDir1.isDirectory()) {
            System.out.println("Invalid source directory.");
            System.out.println(USAGE);
            System.exit(0);
        }
        //check 2nd input dir
        File inDir2 = new File(args[1]);
        if (!inDir2.exists() || !inDir2.isDirectory()) {
            System.out.println("Invalid target directory.");
            System.out.println(USAGE);
            System.exit(0);
        }
        //check template file
        File templateFile = new File(args[2]);
        if (!templateFile.exists() || !templateFile.isFile()) {
            System.out.println("Invalid template file.");
            System.out.println(USAGE);
            System.exit(0);
        }
        //check output dir
        File outDir = new File(args[3]);
        if(!outDir.exists() || !outDir.isDirectory()){
            System.out.println("Invalid output directory.");
            System.out.println(USAGE);
            System.exit(0);
        }
        // check limes output dir.
        File limesOutputDirectory = new File(args[4]);
        if (!limesOutputDirectory.exists()) {
            if (!limesOutputDirectory.mkdir()) {
                System.out.println("Invalid limes output directory.");
                System.out.println(USAGE);
                System.exit(0);
            }
        }

        ArrayList<Pair> pairList = new ArrayList<>();

        //find pairs
        for (File src : inDir1.listFiles()) {
            for (File tgt : inDir2.listFiles()) {
                if (arePairable(src, tgt)) {
                    pairList.add(new Pair(src, tgt));
                }
            }
        }

        Pair p = null;
        try {
            byte[] encoded = Files.readAllBytes(templateFile.toPath());
            String result = new String(encoded, StandardCharsets.UTF_8);
            for (Pair pair : pairList) {
                p = pair;
                String newContent = result.replaceAll(KEYWORD_SOURCEFILE, pair.getSourceFile().getAbsolutePath());
                newContent = newContent.replaceAll(KEYWORD_TARGETFILE, pair.getTargetFile().getAbsolutePath());
                newContent = newContent.replaceAll(KEYWORD_ACCEPTFILE, limesOutputDirectory.getAbsolutePath() + "/accept_" + removeExtension(pair.getSourceFile().getName()) + "-" + pair.getTargetFile().getName());
                newContent = newContent.replaceAll(KEYWORD_REVIEWFILE, limesOutputDirectory.getAbsolutePath() + "/review_" + removeExtension(pair.getSourceFile().getName()) + "-" + pair.getTargetFile().getName());
                newContent = newContent.replaceAll(KEYWORD_SOURCEID, "src_" + removeExtension(pair.getSourceFile().getName()));
                newContent = newContent.replaceAll(KEYWORD_TARGETID, "tgt_" + removeExtension(pair.getTargetFile().getName()));
                FileWriter fw = new FileWriter(new File(outDir, "config_" + removeExtension(pair.getSourceFile().getName()) + "-" + removeExtension(pair.getTargetFile().getName()) + "." + EXTENSION_XML));
                fw.write(newContent);
                fw.close();
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error generating config file for " + p.toString() + ex);
            System.exit(-1);
        }

    }

    private static String removeExtension(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    private static boolean arePairable(File fA, File fB) {
        String nameA = removeExtension(fA.getName());
        int idx = nameA.lastIndexOf("_");
        if (idx >= 0) {
            nameA = nameA.substring(0, idx);
        }

        String nameB = removeExtension(fB.getName());
        idx = nameB.lastIndexOf("_");
        if (idx >= 0) {
            nameB = nameB.substring(0, idx);
        }

        return nameA.equals(nameB);
    }

}
