package edu.uark.registerapp.models.api;

public class EmployeeSignIn extends ApiResponse{
    private String employeeID;
    private String password;

    public String getEmployeeId() {
		return this.employeeID;
	}
	public EmployeeSignIn setEmployeeId(final String eId) {
		this.employeeID = eId;
		return this;
	}
	public String getPassword() {
		return this.password;
	}
	public EmployeeSignIn setPassword(final String password) {
		this.password = password;
		return this;
	}
	public EmployeeSignIn() {
		this.password = StringUtils.EMPTY;
		this.employeeID = StringUtils.EMPTY;
	}

}
