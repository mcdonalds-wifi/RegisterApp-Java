document.addEventListener("DOMContentLoaded",function(event) {
	//define handlers for each button, on page load
	document.getElementById('startTransactionButton').addEventListener(
		"click",() => {displayError('Functionality has not yet been implemented.');}
	);
	document.getElementById('viewProductsButton').addEventListener(
		"click",() => {window.location.assign("/productListing");}
	);
	document.getElementById('createEmployeeButton').addEventListener(
		"click",() => {window.location.assign("/employeeDetail");}
	);
	document.getElementById('salesReportButton').addEventListener(
		"click",() => {displayError('Functionality has not yet been implemented.');}
	);
	document.getElementById('cashierReportButton').addEventListener(
		"click",() => {displayError('Functionality has not yet been implemented.');}
	);
});
