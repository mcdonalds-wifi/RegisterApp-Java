let hideEmployeeSavedAlertTimer = undefined;

document.addEventListener("DOMContentLoaded", () => {
	document.getElementById('saveButton').addEventListener('click',saveActionClick);
});

// Save
function saveActionClick(event) {
	if(!validateSave())//stop save functionality
		return;
	event.target.disabled=true;
	const saveRequest={
		id: document.getElementById('employeeId').value,
		managerId: document.getElementById('employeeManagerId').value,
		firstName: document.getElementById('employeeFName').value,
		lastName: document.getElementById('employeeLName').value,
		password: document.getElementById('employeePW').value,
		classification: document.getElementById('employeeType').value
	};
	var saveUrl="/api/employee/";
	if(document.getElementById('employeeId').value==null)
		saveurl="/api/employee/"+document.getElementById('employeeId');
	if(document.getElementById('employeeId').trim()=="") {//ajax post vs patch
		ajaxPost(saveUrl,saveRequest,(callbackResponse) => {
			event.target.disabled=false;
			if(isSuccessResponse(callbackResponse))
				completeSaveAction(callbackResponse);
		});
	} else {
		ajaxPatch(saveUrl,saveRequest,(callbackResponse) => {
			event.target.disabled=false;
			if(isSuccessResponse(callbackResponse))
				completeSaveAction(callbackResponse);
		});
	}
}
function validateSave() {//validate name, password, employee type. Focus on problem
	var validElem=document.getElementById('employeeFName');
	if((validElem.value==null)||(validElem.trim()=="")) {//first name should not be blank
		displayError("Please enter valid first name");
		validElem.focus();
		validElem.select();
		return false;
	}
	validElem=document.getElementById('employeeLName');
	if((validElem.value==null)||(validElem.trim()=="")) {//last name should not be blank
		displayError("Please enter valid last name");
		validElem.focus();
		validElem.select();
		return false;
	}
	validElem=document.getElementById('employeePW');
	if((validElem.value==null)||(validElem.trim()=="")) {//password should not be blank
		displayError("Please enter valid password");
		validElem.focus();
		validElem.select();
		return false;
	}
	const PWConfirm=document.getElementById('employeeConfirmPW');
	if(validElem.value!=PWConfirm.value) {//password must match confirm password
		displayError("Please enter matching passwords");
		validElem.focus();
		validElem.select();
		return false;
	}
	//employee type validation
	validElem=document.getElementById('employeeType');
	if(validElem.value<=0) {
		displayError("Please provide valid employee type");
		validElem.focus();
		return false;
	}
	return true;
}
function completeSaveAction(callbackResponse) {
	if(callbackResponse.data==null)//null exception handler
		return;
	if((callbackResponse.data.redirectUrl!=null)&&(callbackResponse.data.redirectUrl!=="")) {
		//redirect if url isn't blank
		window.location.replace(callbackResponse.data.redirectUrl);
		return;
	}
	displayEmployeeSavedAlertModal();
	//set employeeEmployeeId visible if it's hidden
	const employeeEmpIdRow=document.getElementById('employeeEmployeeId').closest("tr");
	if(employeeEmpIdRow.classList.contains('hidden') {
		document.getElementById('employeeId').value=callbackResponse.data.id;
		document.getElementById('employeeEmployeeId').value=callbackResponse.data.employeeid;
		employeeEmpIdRow.classList.remove('hidden');
	}
}

function displayEmployeeSavedAlertModal() {
	if (hideEmployeeSavedAlertTimer) {
		clearTimeout(hideEmployeeSavedAlertTimer);
	}

	const savedAlertModalElement = getSavedAlertModalElement();
	savedAlertModalElement.style.display = "none";
	savedAlertModalElement.style.display = "block";

	hideEmployeeSavedAlertTimer = setTimeout(hideEmployeeSavedAlertModal, 1200);
}

function hideEmployeeSavedAlertModal() {
	if (hideEmployeeSavedAlertTimer) {
		clearTimeout(hideEmployeeSavedAlertTimer);
	}

	getSavedAlertModalElement().style.display = "none";
}
// End save

//Getters and setters
function getSavedAlertModalElement() {
	return document.getElementById("employeeSavedAlertModal");
}
//End getters and setters
