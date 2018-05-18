package com.businessapp.logic;


import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Equipment;

import java.io.IOException;
import java.util.Collection;

public class EquipmentDataSource implements EquipmentDataIntf {
    private final GenericEntityContainer<Equipment> equipments;
    private PersistenceProviderIntf persistenceProvider = null;

    /**
     * Factory method that returns a CatalogItem data source.
     *
     * @return new instance of data source.
     */
    public static EquipmentDataIntf getController(String name, PersistenceProviderIntf persistenceProvider) {
        EquipmentDataIntf cds = new EquipmentDataSource(name);
        cds.inject(persistenceProvider);
        return cds;
    }

    /**
     * Private constructor.
     */
    private EquipmentDataSource(String name) {
        this.equipments = new GenericEntityContainer<Equipment>(name, Equipment.class);
    }

    @Override
    public void inject(ControllerIntf dep) {
        if (dep instanceof PersistenceProviderIntf) {
            this.persistenceProvider = (PersistenceProviderIntf) dep;
        }
    }

    @Override
    public void inject(Component parent) {

    }

    @Override
    public void start() {
        if (persistenceProvider != null) {
            try {
                /*
                 * Attempt to load container from persistent storage.
                 */
                persistenceProvider.loadInto(equipments.getId(), (entity) -> {
                    this.equipments.store((Equipment) entity);
                    return true;
                });
            } catch (IOException e) {
                System.out.print("," );
                System.err.print("No data: " + equipments.getId());
            }
        }
    }

    //TODO: remaining methods of EquipmentDataIntf.java

    @Override
    public void stop() {

    }

    @Override
    public Equipment findEquipmentById(String id) {
        return equipments.findById(id);
    }

    @Override
    public Collection<Equipment> findAllEquipments() {
        return equipments.findAll();
    }

    @Override
    public Equipment newEquipment(String name) {
        Equipment c = new Equipment(name);
        equipments.store(c);
        if (persistenceProvider != null ) {
            persistenceProvider.save(equipments, equipments.getId());
        }
        return c;
    }

    @Override
    public void updateEquipment(Equipment c) {
        equipments.update(c);
        if (persistenceProvider != null ) {
            persistenceProvider.save(equipments, equipments.getId());
        }
    }

    @Override
    public void deleteEquipment(Collection<String> ids) {
        equipments.delete(ids);
        if (persistenceProvider != null ) {
            persistenceProvider.save(equipments, equipments.getId());
        }
    }
}
