package me.screescree.doorlock.persistenttype;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class PersistentDataType_BOOLEAN implements PersistentDataType<Byte, Boolean> {
    @Override
    public Class<Byte> getPrimitiveType() {
        return Byte.class;
    }
    
    @Override
    public Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @Override
    public Boolean fromPrimitive(Byte primitive, PersistentDataAdapterContext context) {
        return primitive == 1;
    }

    @Override
    public Byte toPrimitive(Boolean complex, PersistentDataAdapterContext context) {
        return complex ? (byte) 1 : (byte) 0;
    }
}
