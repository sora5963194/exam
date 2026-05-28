<!-- 科目削除JSP -->
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
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス情報削除</h2>
			<form action="ClassDeleteExecute.action" method="get">

				<div>
					<p>「${class_name }（${school_cd }）」を削除してもよろしいですか</p>
				</div>

				<div class="mx-auto py-2">
					<input class="btn btn-danger" type="submit" value="削除"/>
				</div>
				<div>
					<input type="hidden" name="class_name" value="${class_name }" />
				</div>
			</form>
			<br>
			<br>
			<a href="ClassList.action">戻る</a>
		</section>
	</c:param>
</c:import>