package com.example.ooad.service.sysparam;

public class TypedParamValue {
    private Object value;
    private String dataType;

    public TypedParamValue(Object value, String dataType) {
        this.value = value;
        this.dataType = dataType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
