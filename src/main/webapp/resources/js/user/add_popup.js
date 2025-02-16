const checkUserType = () => {
	var userType = document.querySelector("[name='user_type']").value;
	var userLvRow = document.getElementById("user_lv_row");
	var userCoRow = document.getElementById("user_co_row");
	
	var userCoPikPak = document.getElementById("user_co_pikpak");
	var userCoFixed = document.getElementById("user_co_fixed");
	
	var supplierSelectArea = document.getElementById("supplier_select_area");
	var supplierSelect = document.getElementById("supplier_select");
	var vendorSelectArea = document.getElementById("vendor_select_area");
	var vendorSelect = document.getElementById("vendor_select");
	
	var companyCd = document.querySelector("[name='company_cd']");
	
	
	$("#supplier_select").val('').trigger('change');
	$("#vendor_select").val('').trigger('change');
	
	if (userType == "") {
		userCoRow.style.visibility = "collapse";
		userCoPikPak.style.display = "none";
		supplierSelectArea.style.display = "none";
		vendorSelectArea.style.display = "none";
	}
	else {
		userCoRow.style.visibility = "visible";
		
		if (userType == "supplier") {
			userLvRow.style.visibility = "collapse";
			
			supplierSelectArea.style.display = "inline";
			supplierSelect.name = "user_co";
			
			vendorSelectArea.style.display = "none";
			vendorSelect.removeAttribute("name");
			
			userCoPikPak.style.display = "none";
			userCoFixed.removeAttribute("name");
		}
		else if (userType == "vendor") {
			userLvRow.style.visibility = "collapse";
			
			vendorSelectArea.style.display = "inline";
			vendorSelect.name = "user_co";
			
			supplierSelectArea.style.display = "none";
			supplierSelect.removeAttribute("name");
			
			userCoPikPak.style.display = "none";
			userCoFixed.removeAttribute("name");
		}
		else {
			userLvRow.style.visibility = "visible";
			
			supplierSelectArea.style.display = "none";
			supplierSelect.removeAttribute("name");
			
			vendorSelectArea.style.display = "none";
			vendorSelect.removeAttribute("name");
			
			userCoPikPak.style.display = "inline";
			userCoFixed.name = "user_co";
			companyCd.value = "P0001";
		}
	}
}


const checkUserId = () => {
	var userId = document.getElementById("user_id");
	var responseArea = document.getElementById("id-check-response");
	let checkedStatus = document.getElementById("id-checked-status");
	let responseMsg = "";
	let searchParams = new URLSearchParams();
	searchParams.append("user_id", userId.value);
	
	responseArea.style.opacity = "0";
	
	
	if (userId.value == "") {
		responseMsg = "※ 아이디를 입력하셔야 합니다";
		responseArea.innerHTML = responseMsg;
		userId.focus();
	}
	else{
		fetch('/admin/check/id',{
			method : "POST",
			headers : {
				"content-type" : "application/x-www-form-urlencoded"
			},
			body : searchParams
		})
		.then(response => {
			if(response.ok){
				return response.text();
			}
			else {
				throw new Error("알 수 없는 이유로 조회가 실패하였습니다. 관리자에게 문의하세요.");
			}
		})
		.then(result => {
			if (result == "Y") {
				responseMsg = "사용 가능한 아이디입니다";
				responseArea.style.color = "green";
				userId.setAttribute("readonly", true);
				//userId.style.border = "2px solid grey";
				userId.style.backgroundColor = "#DCDCDC";
				checkedStatus.value = "Y";
			}
			else {
				responseMsg = "※ 사용 불가능한 아이디입니다";
			}
			responseArea.innerHTML = responseMsg;
		})
		.catch(error => {
			responseMsg = "시스템 에러발생! 관리자 문의 부탁합니다.";
			responseArea.innerHTML = responseMsg;
		})
	}
	responseArea.style.opacity = "1";
}

const checkPassword = () => {
	new addUserDataTools().checkPasswordMatch();
}

const checkUserTel = () => {
	let userTel = document.getElementById("user_tel");
	let userType = document.getElementById("user_type");
	var responseArea = document.getElementById("tel-check-response");
	let checkedStatus = document.getElementById("tel-checked-status");
	let searchParams = new URLSearchParams();
	let responseMsg = "";
	searchParams.append("user_tel", userTel.value);
	searchParams.append("user_type", userType.value);
	
	responseArea.style.opacity = "0";
	
	if (userTel.value == "") {
		responseMsg = "※ 연락처를 입력하셔야 합니다";
		responseArea.style.color = "red";
		responseArea.innerHTML = responseMsg;
		userTel.focus();
	}
	else if (userType.value == "") {
		responseMsg = "※ 회원 유형를 선택하셔야 합니다";
		responseArea.style.color = "red";
		responseArea.innerHTML = responseMsg;
		userType.focus();
	}
	else {
		fetch('/admin/check/tel',{
			method : "POST",
			headers : {
				"content-type" : "application/x-www-form-urlencoded"
			},
			body : searchParams
		})
		.then(response => {
			if(response.ok){
				return response.text();
			}
			else {
				throw new Error("알 수 없는 이유로 조회가 실패하였습니다. 관리자에게 문의하세요.");
			}
		})
		.then(result => {
			if (result == "Y") {
				responseMsg = "연락처가 중복 되지 않습니다";
				responseArea.style.color = "green";
				userTel.setAttribute("readonly", true);
				//userId.style.border = "2px solid grey";
				userTel.style.backgroundColor = "#DCDCDC";
				checkedStatus.value = "Y";
			}
			else {
				responseMsg = "※ 해당 연락처는 이미 사용중입니다";
			}
			responseArea.innerHTML = responseMsg;
		})
		.catch(error => {
			responseMsg = "시스템 에러발생! 관리자 문의 부탁합니다.";
			responseArea.style.color = "red";
			responseArea.innerHTML = responseMsg;
		})
	}
	responseArea.style.opacity = "1";
}

const sendAddUserForm = () => {
	let sendCondition1 = new addUserDataTools().checkEmpty();
	if(sendCondition1 == "Y") {
		
		let sendCondition2 = new addUserDataTools().checkDuplicate();
		if(sendCondition2 == "Y") {
			
			let sendCondition3 = new addUserDataTools().checkPasswordMatch();
			if (sendCondition3 == "Y") {
				let formData = new FormData(addUserForm);
				let responseArea = document.getElementById("send-check-response");
				
				fetch("/admin/users/add",{
					method : "POST",
					body : formData
				})
				.then(response => {
					if (response.ok) {
						return response.text();
					} else {
						throw new Error("알 수 없는 이유로 등록 실패하였습니다. 관리자에게 문의하세요.");
					}
				})
				.then(result => {
					if (result == "Y") {
						alert("정상적으로 등록되었습니다.");
						window.location.reload();
					}
					else {
						responseArea.innerHTML = "서버 오류로 회원 등록 실패하였습니다. 사이트 관리자에게 문의 부탁합니다.";
						responseArea.style.visibility = "visible";
						responseArea.style.opacity = "1";
					}
				})
				.catch(error => {
					responseArea.innerHTML = "시스템 에러발생! 사이트 관리자에게 문의 부탁합니다.";
					responseArea.style.visibility = "visible";
					responseArea.style.opacity = "1";
				});
			}
		}
	}
}

class addUserDataTools {
	checkEmpty(){
		let userType = document.querySelector("[name='user_type']");
		let userId = document.querySelector("[name='user_id']");
		let userName = document.querySelector("[name='user_nm']");
		let userPw = document.querySelector("[name='user_pw']");
		let userTel = document.querySelector("[name='user_tel']");
		let userMail = document.querySelector("[name='user_mail']");
		let userCo = document.querySelector("[name='user_co']");
		let userLv = document.querySelector("[name='user_lv']");
		let responseArea = document.getElementById("send-check-response");
		let responseMsg = "";
		let sendCondition = "N";
		
		responseArea.style.opacity = "0";
		
		if(userType.value == "") {
			responseMsg = "사용자 유형를 선택해주세요";
			userType.focus();
		}
		else if(userId.value == "") {
			responseMsg = "아이디를 입력해주세요";
			userId.focus();
		}
		else if(userName.value == "") {
			responseMsg = "이름을 입력해주세요";
			userName.focus();
		}
		else if(userPw.value == "") {
			responseMsg = "비밀번호를 입력해주세요";
			userPw.focus();
		}
		else if(userTel.value == "") {
			responseMsg = "연락처를 입력해주세요";
			userTel.focus();
		}
		else if(userMail.value == "") {
			responseMsg = "이메일을 입력해주새요";
		}
		else if(userCo.value == "") {
			responseMsg = "소속을 선택해주세요";
			userCo.focus();
		}
		else if((userType.value == "operator" || userType.value =="admin") && userLv.value == "") {
			responseMsg = "등급을 선택해주세요";
			userLv.focus();
		}
		else{
			sendCondition = "Y";
		}
		
		if(responseMsg != ""){
			responseArea.innerHTML = responseMsg;
			responseArea.style.visibility = "visible";
			responseArea.style.opacity = "1";
		}
		
		return sendCondition;
	}
	
	checkDuplicate(){
		let checkedStatus1 = document.getElementById("id-checked-status");
		let checkedStatus2 = document.getElementById("tel-checked-status");
		let responseArea = document.getElementById("send-check-response");
		let responseMsg = "";
		let sendCondition = "N";
		
		if(checkedStatus1.value == "N"){
			responseMsg = "아이디 중복 확인 해주세요";
			responseArea.innerHTML = responseMsg;
			responseArea.style.visibility = "visible";
			responseArea.style.opacity = "1";
		}
		else if (checkedStatus2.value == "N") {
			responseMsg = "연락처 중복 확인 해주세요";
			responseArea.innerHTML = responseMsg;
			responseArea.style.visibility = "visible";
			responseArea.style.opacity = "1";
		}
		else{
			sendCondition = "Y";
		}
		
		return sendCondition;
	}
	
	checkPasswordMatch(){
		let userPw = document.querySelector("[name='user_pw']");
		let userPwCheck = document.querySelector("[name='pw_check']");
		let responseArea = document.getElementById("send-check-response");
		let responseMsg = "";
		let sendCondition = "N";
		
		responseArea.style.visibility = "collapse";
		responseArea.style.opacity = "0";
		
		if(userPwCheck.value != "") {
			if (userPw.value != userPwCheck.value) {
				responseMsg = "비밀번호가 일치하지않습니다";
				responseArea.innerHTML = responseMsg;
				responseArea.style.visibility = "visible";
				responseArea.style.opacity = "1";
			}
			else{
				sendCondition = "Y";
			}
		}
		
		return sendCondition;
	}
}
