package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.utils.data.PunchcardTextWriter;
import com.fallenreaper.createutilities.utils.data.PunchcardWriter;

import java.util.*;

public class PunchwriterNetwork {
    public Map<UUID, PunchcardTextWriter> savedWriterText;
    public List<PunchcardTextWriter> writerList;
    public Map<UUID, PunchcardWriter> savedWriters;

    public void add(PunchcardTextWriter type, UUID key) {
        savedWriterText.put(key, type);
        writerList.add(type);
    }

    public void addWriter(PunchcardWriter type, UUID key) {
       savedWriters.put(key, type);
    }

    public PunchwriterNetwork() {
        clean();
    }


    protected void clean(){
        savedWriterText = new HashMap<>();
        savedWriters = new HashMap<>();
        writerList = new LinkedList<>();
    }
}
