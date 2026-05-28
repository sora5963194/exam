<!-- 科目登録JSP -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp" >
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section>
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス情報登録</h2>
			<form action="ClassCreateExecute.action" method="get">

				<div>
					<label for="code">学校コード</label><br>
					<input class="form-control" type="text" name="cd" id="cd" value="${cd }" readonly />
				</div>
				
				<div>
					<label for="name">クラス名</label><br>
					<input class="form-control" type="text" id="name" name="name" value="${name }"
					required maxlength="5" placeholder="クラス名を入力してください" />
				</div>

				<div class="mx-auto py-2">
					<button class="btn btn-primary" id="create-button">登録</button>
				</div>
			</form>
			<a href="ClassList.action">戻る</a>
		</section>
	</c:param>
</c:import>