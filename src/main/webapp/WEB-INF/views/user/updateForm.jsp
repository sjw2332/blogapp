<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp" %>

<div class="container">
	<form onSubmit="update(event,${sessionScope.principal.id})">
		  <div class="form-group">
		    <input type="text" id="username" value="${sessionScope.principal.username}" class="form-control"
		     placeholder="Enter username" required="required" maxlength="20" readonly="readonly">
		  </div>
		  
		  <div class="form-group">
		    <input type="email" id="email" value="${sessionScope.principal.email}"  class="form-control"
		     placeholder="Enter email" required="required" maxlength="50">
		  </div>
		  <button type="submit" class="btn btn-primary">정보수정</button>
	</form>
</div> 

<script>
async function update(event, id){
		//console.log(event);
		//event.preventDefault();
		//주소 : put board/id
		//update board set title=? , content=? where id=?
		let userUpdateDto = {
				email:document.querySelector("#email").value
		};
		
		//console.log(userUpdateDto);
		//console.log(JSON.stringify(userUpdateDto));
		
		//JSON.stringify (자바스크립트 오브젝트) => return 제이슨 문자열
		//JSON.parse (제이슨 문자열)	=> return 자바스크립트 함수
		
		let response = await fetch("http://localhost:8080/user/"+id, {
			method:"put",
			body:JSON.stringify(userUpdateDto),
			headers:{
				"Content-Type":"application/json; charset=utf8"
			}
		}
				
		);
		
		let parseResponse = await response.json(); //나중에 스프링 함수에서 return될 때 리턴값  확인 
		
		//response.text()로 변경해서 확인해보자.
		console.log(parseResponse);
		
		if(parseResponse.code == 1){
			alert("업데이트 성공");
			location.href="/";
		} else {
			alert("업데이트 실패" + parseResponse.msg);
		}
		
		
		//alert("나 호출됨?");
	}

</script>


<%@ include file="../layout/footer.jsp" %>


