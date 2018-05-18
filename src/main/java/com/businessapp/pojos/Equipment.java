package com.businessapp.pojos;

import com.businessapp.logic.IDGen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Equipment implements EntityIntf{
    private static final long serialVersionUID = 1L;

    private static IDGen IDG = new IDGen( "C.", IDGen.IDTYPE.AIRLINE, 6 );


    // Equipment states.
    public enum EquipmentStatus {VERFUEGBAR, VERLIEHEN, DEFEKT, IN_REPERATUR, IN_WARTUNG, MUSS_WARTUNG}
    private String id = null;
    private String name;

    private Date maintenanceDate;
    private List<LogEntry> notes = new ArrayList<LogEntry>();
    private EquipmentStatus status = EquipmentStatus.VERFUEGBAR;
    @Override
    public String getId() {
        return this.id;
    }
    public Equipment(String name) {
        this.id = id==null? IDG.nextId() : id;
        this.name = name;
        this.notes.add( new LogEntry( "Equipment record created." ) );
    }

    public Date getMaintenanceDate() {
        return this.maintenanceDate;
    }

    public void setMaintenanceDate(Date date) {
        this.maintenanceDate = date;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LogEntry> getNotes() {
        return notes;
    }

}
