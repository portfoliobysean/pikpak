window.addEventListener("load",() => {
	const expiryTime = localStorage.getItem("expiryTime");
	startTimer(expiryTime);
});

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
			window.location.reload();
		}
		else{
			alert("시스템 오류가 났습니다. 다시 한번 로그인해주세요.");
			location.href = '/login';
		}
	})
	.catch(error => {
		alert(error);
	});
}

const startTimer = (expiryTime) => {
	const activeDuration = expiryTime - Date.now();
	let expirationTimerArea = document.getElementById("expiration_timer");
	
	if (activeDuration > 0) {
		let timer = activeDuration;
		
		const interval = setInterval(() => {
            if (timer <= 0) {
                clearInterval(interval);
                expirationTimerArea.textContent = "시간 초과";
                localStorage.clear();
                setTimeout(() => {
                	location.href = "/auth/session-expired"
                }, 2000);
                return;
            }
			const min = Math.floor((timer % (1000 * 60 * 60)) / (1000 * 60));
			const sec = Math.floor((timer % (1000 * 60)) / 1000);
            const minutes = min.toLocaleString("en-US",{minimumIntegerDigits: 2, useGrouping:false})
            const seconds = sec.toLocaleString("en-US",{minimumIntegerDigits: 2, useGrouping:false})
            expirationTimerArea.textContent = `${minutes} : ${seconds}`;

            timer -= 1000; // Decrease by 1 second
        }, 1000);
	}
	else {
		expirationTimerArea.textContent = "시간 초과";
		localStorage.clear();
		setTimeout(() => {
        	location.href = "/auth/session-expired"
        }, 2000);
	}
}