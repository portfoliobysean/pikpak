const checkIdDuplicate = () => {
	var userId = document.getElementById("user_id");
	var responseArea = document.getElementById("id-check-response");
	let checkedStatus = document.getElementById("id-checked-status");
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
				userId.setAttribute("readonly", "readonly");
				//userId.style.border = "2px solid grey";
				userId.style.backgroundColor = "#DCDCDC";
				checkedStatus.value = "Y";
			}
			else {
				responseMsg = "※ 사용 불가능한 아이디입니다";
				userId.removeAttribute("readonly");
				userId.style.backgroundColor = "#FFFFFF";
				checkedStatus.value = "N";
				
			}
			responseArea.innerHTML = responseMsg;
		})
		.catch(error => {
			console.log(error);
			responseMsg = "시스템 에러발생! 관리자 문의 부탁합니다.";
			responseArea.innerHTML = responseMsg;
		})
	}
	responseArea.style.opacity = "1";
}

const checkTelDuplicate = () => {
	let userTel = document.getElementById("user_tel");
	let userType = document.getElementById("user_type");
	let userId = document.getElementById("user_id");
	var responseArea = document.getElementById("tel-check-response");
	let checkedStatus = document.getElementById("tel-checked-status");
	let searchParams = new URLSearchParams();
	searchParams.append("user_tel", userTel.value);
	searchParams.append("user_type", userType.value);
	
	responseArea.style.opacity = "0";
	
	if (userTel.value == "") {
		responseMsg = "※ 연락처를 입력하셔야 합니다";
		responseArea.style.color = "red";
		responseArea.innerHTML = responseMsg;
		userTel.focus();
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
				
				var newUserId = new modUserDataTools().generateNewId(userTel.value, userId.value);
				userId.value = newUserId;
				checkedStatus.value = "Y";
				checkIdDuplicate();
			}
			else {
				responseMsg = "※ 해당 연락처는 이미 사용중입니다";
				checkedStatus.value = "N";
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

const checkPassword = () => {
	new modUserDataTools().checkPasswordMatch();
}

const sendModUserForm = () => {
	let sendCondition1 = new modUserDataTools().checkEmpty();
	if(sendCondition1 == "Y") {
		
		let sendCondition2 = new modUserDataTools().checkDuplicateStatus();
		if(sendCondition2 == "Y") {
			
			let sendCondition3 = new modUserDataTools().checkPasswordMatch();
			if (sendCondition3 == "Y") {
				
				let formData = new FormData(modUserForm);
				let responseArea = document.getElementById("send-check-response");
				
				fetch("/admin/users/mod",{
					method : "POST",
					body : formData
				})
				.then(response => {
					if (response.ok) {
						return response.text();
					} else {
						throw new Error("알 수 없는 이유로 수정 실패하였습니다. 관리자에게 문의하세요.");
					}
				})
				.then(result => {
					if (result == "Y") {
						alert("정상적으로 수정되었습니다.");
						location.href="/admin/users";
					}
					else {
						responseArea.innerHTML = "서버 오류로 회원 수정 실패하였습니다. 사이트 관리자에게 문의 부탁합니다.";
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



class modUserDataTools {
	checkEmpty(){
		let userId = document.querySelector("[name='user_id']");
		let userTel = document.querySelector("[name='user_tel']");
		let userMail = document.querySelector("[name='user_mail']");
		let userType = document.getElementById("user_type");
		let userLv = document.querySelector("[name='user_lv']");
		let responseArea = document.getElementById("send-check-response");
		let responseMsg = "";
		let sendCondition = "N";
		
		if(userId.value == "") {
			responseMsg = "아이디를 입력해주세요";
			userId.focus();
		}
		else if(userTel.value == "") {
			responseMsg = "연락처를 입력해주세요";
			userTel.focus();
		}
		else if(userMail.value == "") {
			responseMsg = "이메일을 입력해주새요";
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
	
	generateNewId(userTel, userId){
		var userTelSlice = userTel.slice(-4);
		var userIdSlice = userId.split("_",2);
		var result = userIdSlice[0] + "_" + userIdSlice[1] + "_" + userTelSlice;
		return result;
	}
	
	checkDuplicateStatus(){
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
		
		if(userPw.value == "" && userPwCheck.value == ""){
			sendCondition = "Y";
		}
		
		return sendCondition;
	}
	
	
}
