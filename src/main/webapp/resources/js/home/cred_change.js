const checkPassword = () => {
	let userPw = document.querySelector("[name='user_pw']");
	let responseArea = document.getElementById("response");
	let searchParams = new URLSearchParams();
	searchParams.append("userPw",userPw.value);
	let verifiedStatus = document.getElementById("verifiedStatus");
	let responseMsg = "";
	
	responseArea.style.opacity = "0";
	responseArea.style.color = "red";
	
	fetch('/home/user/verify',{
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
			responseMsg = "기존 비밀번호가 확인되었습니다";
			responseArea.style.color = "green";
			verifiedStatus.value = "Y";
		}
		else {
			responseMsg = "※ 비밀번호가 일치하지않습니다";
			verifiedStatus.value = "N";
		}
		responseArea.innerHTML = responseMsg;
		
	})
	.catch(error => {
		responseMsg = "시스템 에러발생! 관리자 문의 부탁합니다.";
		responseArea.innerHTML = responseMsg;
		verifiedStatus.value = "N";
	})
	responseArea.style.opacity = "1";
}

const sendCredChangeForm = () => {
	let userPw = document.querySelector("[name='user_pw']");
	let responseArea = document.getElementById("response");
	responseArea.style.color = "red";
	if (userPw.value == "") {
		responseArea.innerHTML = "※ 기존 비밀번호를 입력해주세요";
		responseArea.style.opacity = "1";
		userPw.focus();
	}
	else {
		let sendCondition1 = new credChangeDataTools().checkPasswordMatch();
		if(sendCondition1 == "Y") {
			let sendCondition2 = document.getElementById("verifiedStatus").value;
			if (sendCondition2 != "Y") {
				let verifyPwButton = document.getElementById("verifyPwButton");
				verifyPwButton.classList.remove('flash');
				void verifyPwButton.offsetWidth; 
				verifyPwButton.classList.add('flash');
				responseArea.innerHTML = "※ 해당 버튼을 눌러 기존 비밀번호를 확인해 주세요";
				responseArea.style.opacity = "1";
			}
			else{
				let formData = new FormData(credChangeForm);
				
				responseArea.style.opacity = "0";
				
				fetch('/home/user/cred/mod',{
					method : "POST",
					body : formData
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
						alert("정상적으로 비밀변경 되었습니다");
						refreshSession();
					}
					else {
						responseArea.innerHTML = "※ 비밀번호 변경 실패하였습니다. 관리자에게 문의하세요.";
						responseArea.style.opacity = "1";
					}
				})
				.catch(error => {
					responseArea.innerHTML = "시스템 에러발생! 관리자 문의 부탁합니다.";
					responseArea.style.opacity = "1";
				})
			}
		}
	}
}

const refreshSession = () => {
	fetch("/login/refresh",{
		method : "GET"
	})
	.then(response => {
		return response.json();
	})
	.then(result => {
		const responseMsg = result.responseMsg;
		const expiryTime = result.expiryTime;
		
		if (responseMsg=="Y") {
			localStorage.setItem("expiryTime",expiryTime);
			location.href = '/home';
		}
		else{
			alert("error");
		}
	})
	.catch(error => {
		alert(error);
	});
}

class credChangeDataTools {
	checkPasswordMatch(){
		let userPw = document.querySelector("[name='new_user_pw']");
		let userPwCheck = document.querySelector("[name='user_pw_check']");
		let responseArea = document.getElementById("response");
		let responseMsg = "";
		let sendCondition = "N";
		
		responseArea.style.opacity = "0";
		
		
		if (userPw.value == "" && userPwCheck.value == "") {
			responseMsg = "※ 새 비밀번호를 입력해주세요";
			userPw.focus();
		}
		else{
			if (userPw.value != userPwCheck.value) {
				responseMsg = "※ 입력하신 새 비밀번호가 일치하지않습니다";
				userPw.focus();
			}
			else{
				sendCondition = "Y";
			}
		}
		responseArea.innerHTML = responseMsg;
		responseArea.style.opacity = "1";
		return sendCondition;
	}
}