package com.mesor.journey.addmark.model;

/**
 * Created by Limeng on 2016/9/9.
 */
public class InfoDir implements Comparable<InfoDir> {
    public String nameString;
    public String pathString;
    public String firstFilePath;
    public int fileCount;
    public boolean isShowing;

    @Override
    public int compareTo(InfoDir another) {
        return fileCount - another.fileCount;
    }
}
