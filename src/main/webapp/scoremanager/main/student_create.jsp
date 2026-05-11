<%-- 学生登録JSP --%>
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
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報登録</h2>
			<form action="StudentCreateExecute.action" method="get">
				<div>
					<label for="ent_year">入学年度</label>
					<select class="form-select" id="ent_year" name="ent_year">
						<option value="0">--------</option>
						<c:forEach var="year" items="${ent_year_set }">
							<%-- 現在のyearと選択されていたent_yearが一致していた場合selectedを追記 --%>
							<option value="${year }" <c:if test="${year==ent_year }">selected</c:if>>${year }</option>
						</c:forEach>
					</select>
				</div>
				<div class="mt-2 text-warning">${errors.get("1") }</div>
				<div>
					<label for="no">学生番号</label><br>
					<%-- requiredで未入力の場合エラーメッセージを表示、required maxlength="10"で入力文字数を10文字に制限 --%>
					<input class="form-control" type="text" id="no" name="no" value="${no }" required maxlength="10" placeholder="学生番号を入力してください" />
				</div>
				<div class="mt-2 text-warning">${errors.get("2") }</div>
				<div>
					<label for="name">氏名</label><br>
					<input class="form-control" type="text" id="name" name="name" value="${name }" required maxlength="30" placeholder="氏名を入力してください" />
				</div>
				<div class="mx-auto py-2">
					<label for="class_num">クラス</label>
					<select class="form-select" id="class_num" name="class_num">
						<c:forEach var="num" items="${class_num_set }">
							<%-- 現在のnumと選択されていたclass_numが一致していた場合selectedを追記 --%>
							<option value="${num }" <c:if test="${num==class_num }">selected</c:if>>${num }</option>
						</c:forEach>
					</select>
				</div>
				<div class="mx-auto py-2">
					<button class="btn btn-secondary" id="create-button" name="end">登録して終了</button>
				</div>
			</form>
			<a href="StudentList.action">戻る</a>
		</section>
	</c:param>
</c:import>