window.onload = () => {
    document.querySelector(".progress-bar-fill").style.width = "100%";
    
    let countdown = 3; // 5 seconds countdown
    const countdownText = document.querySelector("#countdown");

    const interval = setInterval(function() {
        countdown--;
        countdownText.textContent = countdown;
        if (countdown <= 0) {
            clearInterval(interval);
        }
    }, 1000); // Update every second

    setTimeout(() => {
		localStorage.clear();
        window.location.href = "/login";
    }, 3000); // 3000 milliseconds = 3 seconds
};