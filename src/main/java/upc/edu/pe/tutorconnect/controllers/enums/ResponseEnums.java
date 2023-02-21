package upc.edu.pe.tutorconnect.controllers.enums;

public enum ResponseEnums {

    ERROR(-1,"Error"),
    ALERT(0,"Alerta"),
    SUCCESS(1,"Exito");

    private Integer value;
    private String name;

    private ResponseEnums(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }


    public String getName() {
        return name;
    }

}
