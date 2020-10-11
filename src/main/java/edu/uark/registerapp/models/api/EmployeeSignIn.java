package edu.uark.registerapp.models.api;

public class EmployeeSignIn extends ApiResponse{
    public String employeeID;
    public String password;

    public EmployeeSignIn(final String eID, final String pass){
        employeeID = eID;
        password = pass;
    }

}
