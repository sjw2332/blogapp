<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp" %>

<div class="container">
	<form action="/board" method="post">
		  <div class="form-group">
		    
		    <input type="text" name="title" class="form-control" placeholder="Enter title">
		  </div>
		  <div class="form-group">
		   
		    <textarea class="form-control" id="summernote" rows="5" name="content"></textarea>
		  </div>
		  
		  
		  <button type="submit" class="btn btn-primary">글쓰기</button>
	</form>
</div>

<script>
    $(document).ready(function() {
        $('#summernote').summernote({
            height:300



        });
    });
  </script>








<%@ include file="../layout/footer.jsp" %>




