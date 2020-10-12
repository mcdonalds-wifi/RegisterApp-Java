document.addEventListener("DOMContentLoaded", function(event) {
	//draw attention to the first input on page load
	const idElement=document.getElementById("employeeId");
	//idElement.focus();
	//idElement.select();
});
function validateForm() { //validate inputs. calls displayError from master.js
	const idElement=document.getElementById('employeeID');
	const passwordElement=document.getElementById('password');
	//id should be numeric and not blank
	if((isNaN(idElement.value))||(idElement.value==null)) {
		displayError("Please provide valid ID (numeric value)");
		return false;
	}
	//password should be not blank
	if(passwordElement.value==null) {
		displayError("Please enter password");
		return false;
	}
	return true;
}
