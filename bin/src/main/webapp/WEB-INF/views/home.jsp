<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="layout/header.jsp" %>


<div class="container">


	<c:forEach begin="0" end="2">
		<!-- 카드 글 시작 -->
		<div class="card">
			<div class="card-body">
				<h4 class="card-title">Title 부분입니다.</h4>
				<a href="#" class="btn btn-primary">상세보기</a>
			</div>
		</div>
		<br>
		<!-- 카드 글 끝 -->
	</c:forEach>
	<!-- 내부가 flex로 바뀌고 수평방향 CENTER -->
	<div class="d-flex justify-content-center">
		<ul class="pagination">
			<li class="page-item disabled"><a class="page-link" href="#">Prev</a></li>
			<li class="page-item"><a class="page-link" href="#">Next</a></li>
		</ul>
	</div>
</div>


<%@ include file="layout/footer.jsp" %>


