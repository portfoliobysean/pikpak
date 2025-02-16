// 회원 검색
const lockIdSearch = () => {
	let idFilter = document.querySelector("[name='userIdSearch']");
	if (idFilter.value != "") {
		idFilter.readOnly = true;
		idFilter.style.backgroundColor = "#DCDCDC";
	}
	else {
		idFilter.focus();
	}
}

const lockNameSearch = () => {
	let nameFilter = document.querySelector("[name='userNameSearch']");
	if (nameFilter.value != "") {
		nameFilter.readOnly = true;
		nameFilter.style.backgroundColor = "#DCDCDC";
	}
	else {
		nameFilter.focus();
	}
}

const applyUserSearch = () => {
	// input 검색 창
	let idFilter = document.querySelector("[name='userIdSearch']").value;
	let nameFilter = document.querySelector("[name='userNameSearch']").value;
	let typeFilter = document.querySelector("[name='userTypeSearch']").value;
	
	// 리스트 테이블 호출
	const table = document.getElementById("userDataTable");
	// 테이블 안에있는 <tr> 호출
	const tr = table.getElementsByTagName("tr");
	
	// 반복문 실행할때 header <tr> 생략
	for (let i = 1; i < tr.length; i++) {
		// 컬럼 Index 번호로 호출
        const tdId = tr[i].getElementsByTagName("td")[0];
        const tdName = tr[i].getElementsByTagName("td")[1];
        const tdType = tr[i].getElementsByTagName("td")[4];

        // 해당 데이터 값 호출
        const idText = tdId.textContent || tdId.innerText;
        const nameText = tdName.textContent || tdName.innerText;
        const typeText = tdType.textContent || tdType.innerText;

        // 검색창에 입력한 데이터를 가지고 탐색 (있으면 IF 없으면 ELSE)
        if (
            (idText.indexOf(idFilter) > -1 || !idFilter) &&
            (nameText.indexOf(nameFilter) > -1 || !nameFilter) &&
            (typeText.indexOf(typeFilter) > -1 || !typeFilter)
        ) {
            tr[i].style.display = "";  
        } else {
            tr[i].style.display = "none";  
        }
    }
}