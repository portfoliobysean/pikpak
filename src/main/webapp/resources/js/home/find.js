const sendCredResetForm = () => {
	let sendCondition = new credResetDataTools().checkEmpty();
	if (sendCondition == "Y") {
		if (confirm("비밀번호가 초기화됩니다. 계속하시겠습니까?")) {
			let responseArea = document.getElementById("response");
			let formData = new FormData(credResetForm);
			
			fetch('/login/find/reset',{
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
					alert("비밀번호 초기화 되었습니다");
					location.href = '/login';
				}
				else {
					responseArea.innerHTML = "※ 비밀번호 초기화 싶래하였습니다. 관리자 문의 부탁합니다.";
					responseArea.style.opacity = "1";
				}
			})
			.catch(error => {
				console.log(error);
				responseArea.innerHTML = "시스템 에러발생! 관리자 문의 부탁합니다.";
				responseArea.style.opacity = "1";
			})
		}
		else {
			location.href = '/login';
		}
	}
}

const verifyUser = () => {
	let responseArea = document.getElementById("response");
	let formData = new FormData(credResetForm);
	
	fetch('/login/find/verify',{
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
			sendCredResetForm();
		}
		else {
			responseArea.innerHTML = "※ 입력하신 사용자 정보가 일치하지않습니다";
			responseArea.style.opacity = "1";
		}
	})
	.catch(error => {
		console.log(error);
		responseArea.innerHTML = "시스템 에러발생! 관리자 문의 부탁합니다.";
		responseArea.style.opacity = "1";
	})
}

class credResetDataTools{
	checkEmpty(){
		let userType = document.querySelector("[name='user_type']");
		let userId = document.querySelector("[name='user_id']");
		let userMail = document.querySelector("[name='user_mail']");
		let responseArea = document.getElementById("response");
		let sendCondition = "N";
		
		if (userType.value == "") {
			responseArea.innerHTML = "※ 사용자 유형을 선책해주세요"
			userType.focus();
		}
		else if (userId.value == "※ 아이디를 입력해주세요") {
			userId.focus();
		}
		else if (userMail.value == "※ 이메일을 입력해주세요") {
			userMail.focus();
		}
		else {
			sendCondition = "Y";
		}
		responseArea.style.opacity = "1";
		return sendCondition;
	}
}