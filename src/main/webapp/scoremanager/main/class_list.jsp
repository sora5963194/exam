<%-- 科目管理一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp" >
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me=4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス管理</h2>
			<form action="SubjectList.action" method="get">
				<!-- クラス登録画面に遷移 -->
				<div class="my-2 text-end px-4">
					<a href="ClassCreate.action">新規登録</a>
				</div>

				<table class="table table-hover">
					<tr>
						<th>学校コード</th>
						<th>クラス名</th>
						<th></th>
						<th></th>
					</tr>

					<c:forEach var="classNum" items="${classnum }">
						<tr>
							<td>${cd }</td>
							<td>${classNum }</td>
							<td><a href="ClassUpdate.action?cd=${classNum }">変更</a></td>
							<td><a href="ClassDelete.action?cd=${classNum }">削除</a></td>
						</tr>
					</c:forEach>
				</table>
			</form>
		</section>
	</c:param>
</c:import>