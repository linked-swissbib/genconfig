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
class Pair {
    
    private File sourceFile;
    private File targetFile;
    
    Pair(File src , File tgt){
        this.sourceFile = src;
        this.targetFile = tgt;
    }

    File getSourceFile() {
        return sourceFile;
    }

    File getTargetFile() {
        return targetFile;
    }
    
    @Override
    public String toString(){
        return "(" + sourceFile.getName() + "," + targetFile.getName() + ")";
    }
    
    
}
