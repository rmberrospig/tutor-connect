package upc.edu.pe.tutorconnect.controllers.constants;


public class ResponseConstant {
    private ResponseConstant () {
        throw   new IllegalStateException("Utility class");
    }
    public static final String API_USER = "/v1/user";
    public static final String API_TUTOR = "/v1/tutor";
    public static final String API_SUBJECT = "/v1/subject";
    public static final String API_SCHEDULE = "/v1/schedule";
    public static final String MSG_SUCCESS = "OPERACION EXITOSA";
    public static final String MSG_ALERT = "OPERACION CON OBSERVACIONES";
    public static final String MSG_ERROR = "OPERACION CON ERROR";
    public static final String MSG_NOT_FOUND = "NO ENCONTRADO";
    public static final String MSG_NOT_CONTENT = "SIN CONTENIDO";

}
