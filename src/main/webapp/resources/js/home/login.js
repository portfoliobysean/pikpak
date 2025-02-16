document.getElementById("loginForm").addEventListener("submit", 
	(event) => {
		event.preventDefault();
		
		let sendCondition = new loginDataTools().emptyCheck();
		
		if (sendCondition == "Y"){
			const data = event.target;
			const formData = new FormData(data);
			var responseArea = document.getElementById("response");
			
			fetch("/login/auth",{
				method : "POST",
				body : formData
			})
			.then(response => {
				if (response.ok) {
					return response.json();
				} else {
					throw new Error("알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.");
				}
			})
			.then(result => {
				const responseMsg = result.responseMsg;
				
				if (responseMsg=="Y") {
					const expiryTime = result.expiryTime;
					alert("로그인 성공 하였습니다");
					localStorage.setItem("expiryTime",expiryTime);
					location.href="/home";
				}
				else{
					responseArea.innerHTML = `<p>${responseMsg}</p>`;
					responseArea.style.visibility = "visible";
					responseArea.style.opacity = "1";
				}
			})
			.catch(error => {
				responseArea.innerHTML = `<p>${error}</p>`;
				//responseArea.innerHTML = `<p>알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요</p>`;
				responseArea.style.visibility = "visible";
				responseArea.style.opacity = "1";
			});
		}
	}
);

class loginDataTools{
	emptyCheck(){
		let user_id = document.querySelector("[name='user_id']");
		let user_pw = document.querySelector("[name='user_pw']");
		let returnCondition = "N";
		
		if (user_id.value == "") {
			alert("아이디를 입력해주세요");
			user_id.focus();
		}
		else if (user_pw.value == "") {
			alert("비밀번호를 입력해주세요");
			user_pw.focus();
		}
		else{
			returnCondition = "Y";
		}
		
		return returnCondition;
	}
}