<%@page import="vo.User"%>
<%@page import="dto.InquiryDto"%>
<%@page import="java.util.List"%>
<%@page import="dao.InquiryDao"%>
<%@page import="vo.Inquiry"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>마켓북스</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="shortcut icon"
	href="https://res.kurly.com/images/marketkurly/logo/favicon_v2.png"
	type="image/x-icon">
<link rel="stylesheet" href="../css/board.css">
<link rel="stylesheet" href="../css/home.css">
</head>
<%  User user = (User) session.getAttribute("LOGINED_USER");
	if (user == null) {
		throw new RuntimeException("새 게시글 작성은 로그인 후 사용가능한 서비스입니다.");
	}
%>
<body class="board-list">
<!-- header -->
	<div id="header">
		<jsp:include page="../common/header.jsp">
			<jsp:param name="menu" value="form" />
		</jsp:include>
	</div>
	<div id="wrap">
		<div id="container">
			<div id="main">
				<div id="content">
					<div class="page_aticle">
						<div id="snb" class="snb_cc">
							<h2 class="tit_snb">고객센터</h2>
							<div class="inner_snb">
								<ul class="list_menu">
									<li><a href="/marketbooks/board/list.jsp">공지사항</a></li>
									<li><a href="/marketbooks/board/faq.jsp">자주하는 질문</a></li>
									<li class="on"><a href="/marketbooks/board/inquiry.jsp">1:1문의</a></li>
								</ul>
							</div>
							<a href="/marketbooks/board/form.jsp" class="link_inquire"><span
								class="emph">도움이 필요하신가요 ?</span> 1:1 문의하기</a>
						</div>
						<div class="page_section">
							<div class="head_aticle">
								<h2 class="tit">1:1 문의</h2>
							</div>
								<form name="frmList" class="row g-3 border bg-light mx-1 " method="post"
									action="add.jsp"
									onsubmit="return submitBoardForm()"
									style="border-radius: 8px;">
									
									<div class="col-12">
										<label class="form-label">제목</label> <input type="text"
											class="form-control" name="title" />
									</div>
									<div class="col-12">
										<label class="form-label">내용</label>
										<textarea rows="10" class="form-control" name="content"></textarea>
									</div>
									
									<table width="100%">
									<tbody>
										<tr>
											<td align="center">
													<button class="bhs_button yb" type="submit"
														style="float: none;">등록</span>
											</td>
										</tr>
									</tbody>
								</table>
									
								</form>
								
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>
<!-- footer include -->
<jsp:include page="../common/footer.jsp"></jsp:include>
<script>
function submitBoardForm() {
	let titleField = document.querySelector("input[name=title]");
	if (titleField.value === '') {
		alert("제목은 필수입력값입니다.");
		titleField.focus();
		return false;
	}
	let contentField = document.querySelector("textarea[name=content]");
	if (contentField.value === '') {
		alert("내용은 필수입력값입니다.");
		contentField.focus();
		return false;
	}
	return true;
}
</script>

</html>