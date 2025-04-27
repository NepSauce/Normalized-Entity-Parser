package nep.util;

import nep.entityclass.RosterEntityDetails;

import java.util.ArrayList;
import java.util.LinkedList;

public class RosterObjectSplitter{
    private LinkedList<RosterEntityDetails> rosterEntityDetails;
    private LinkedList<String> rosterDirectory;
    private LinkedList<String> rosterFileName;
    private LinkedList<String> rosterLocation;
    private int arrayLength;
    
    public RosterObjectSplitter(LinkedList<RosterEntityDetails> rosterEntityDetails, int arrayLength){
        this.rosterEntityDetails = rosterEntityDetails;
        this.arrayLength = arrayLength;
        this.rosterDirectory = new LinkedList<>();
        this.rosterFileName = new LinkedList<>();
    }
    
    public void loadInformation(){
        for (int i = 0; i < rosterEntityDetails.size(); i ++){
            rosterDirectory.add(i, rosterEntityDetails.get(i).getDirectory());
            rosterFileName.add(i, rosterEntityDetails.get(i).getFileName());
            rosterLocation.add(i, rosterEntityDetails.get(i).getLocation());
        }
    }
    
    public void unloadInformation(){
        rosterDirectory.clear();
        rosterFileName.clear();
        rosterLocation.clear();
    }
    
    private boolean validateArraySize(){
        return (rosterDirectory.size() == rosterFileName.size());
    }
    
    public LinkedList<String> getRosterDirectory(){
        unloadInformation();
        loadInformation();
        return rosterDirectory;
    }
    
    public LinkedList<String> getRosterFileName(){
        unloadInformation();
        loadInformation();
        return rosterFileName;
    }
    
    public LinkedList<String> getRosterLocation(){
        unloadInformation();
        loadInformation();
        return  rosterLocation;
    }
}
