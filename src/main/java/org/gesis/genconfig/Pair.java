/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gesis.genconfig;

import java.io.File;

/**
 *
 * @author bensmafx
 */
public class Pair {
    
    private File sourceFile = null;
    private File targetFile = null;
    
    public Pair(File src , File tgt){
        this.sourceFile=src;
        this.targetFile=tgt;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }
    
    
    @Override
    public String toString(){
        return "("+sourceFile.getName()+","+targetFile+")";
    }
    
    
}
