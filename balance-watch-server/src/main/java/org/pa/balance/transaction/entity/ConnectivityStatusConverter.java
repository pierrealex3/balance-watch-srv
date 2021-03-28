package org.pa.balance.transaction.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ConnectivityStatusConverter implements AttributeConverter<ConnectivityStatus, String> {
    @Override
    public String convertToDatabaseColumn(ConnectivityStatus connectivityStatus) {
        if (connectivityStatus == null)
            return null;

        switch(connectivityStatus) {
            case FORBIDDEN:
                return "F";
            case ORPHAN:
                return "O";
            default:
                throw new IllegalArgumentException(String.format("%s:[%s] not supported", this.getClass().getName(), connectivityStatus));
        }
    }

    @Override
    public ConnectivityStatus convertToEntityAttribute(String s) {
        if (s == null)
            return null;
        
        switch(s) {
            case "F":
                return ConnectivityStatus.FORBIDDEN;
            case "O":
                return ConnectivityStatus.ORPHAN;
            default:
                throw new IllegalArgumentException(String.format("%s:[%s] not supported", this.getClass().getName(), s));
        }
    }
}
