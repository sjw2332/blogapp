<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp" %>
<h2>${sessionScope.principal.id}</h2>
<div class="container">
	<form >
	  <div class="form-group">
	    <input type="text" id="username" value="${sessionScope.principal.username}" class="form-control" placeholder="Enter username"  maxlength="20" readonly>
	  </div>
	  <div class="form-group">
	    <input type="password" id="password"  class="form-control" placeholder="Enter password"  maxlength="20">
	  </div>
	  <div class="form-group">
	    <input type="email"  id="email" value="${sessionScope.principal.email}"  class="form-control" placeholder="Enter email"  >
	  </div>
	  <button type="button" onclick="userUpdate(${sessionScope.principal.id})" class="btn btn-primary">회원수정</button>
	</form>
</div>

<script>
	async function userUpdate(id){
		//alert("실행됨ㅎ");
		
		let userUpdateDto = {
				username: document.querySelector("#username").value,
				password: document.querySelector("#password").value,
				email: document.querySelector("#email").value
		}
		
		let response = await fetch("http://localhost:8080/user/"+id ,{
			method:'put',
			body: JSON.stringify(userUpdateDto),
			headers:{
				"Content-Type":"application/json; charset=utf8"
			}
		});
		
		let parseResponse = await response.json(); //나중에 스프링 함수에서 return될 때 리턴값  확인 
			
			//response.text()로 변경해서 확인해보자.
		console.log(parseResponse);
		
		if(parseResponse.code == 1){
				alert("업데이트 성공");
				location.href="/";
			} else {
				alert("업데이트 실패");
			}
		
	}
</script>



<%@ include file="../layout/footer.jsp" %>