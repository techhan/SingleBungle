# ⭐️Portfolio - SingleBungle

<!-- contents -->
<details open="open">
  <summary>Contents</summary>
  <ol>
    <li>
      <a href="#개요">개요</a>
    </li>
    <li>
      <a href="#내용">내용</a>
    </li>
    <li><a href="#구현-기능">구현 기능</a>
      <ul>
        <li><a href="#bungleMarketList">벙글장터 목록 조회</a></li>
        <li><a href="#bungleMarketView">벙글장터 상세 조회</a></li>
        <li><a href="#bungleMarketMypage">벙글장터 활동 내역</a></li>
        <li><a href="#bungleMarketInsert">벙글장터 게시글 작성</a></li>
        <li><a href="#bungleMarketUpdate">벙글장터 게시글 수정</a></li>
        <li><a href="#bungleMarketReport">벙글장터 게시글 신고</a></li>
        <li><a href="#bungleMarketReply">벙글장터 댓글</a></li>
        <li><a href="#bungleMarketDelete">벙글장터 게시글 삭제</a></li>
      </ul>
    </li>
  </ol>
</details>

# ![강아지](https://user-images.githubusercontent.com/73421820/112724211-97667b00-8f55-11eb-997b-520fc526278c.png)개요

* 프로젝트 명 : SingleBungle

* 일정 : 2021년 01월 29일 ~ 2021년 03월 11일

* 개발 목적 : 1인 가구의 정보를 공유하고 소통할 수 있는 커뮤니티 사이트 제작

* 개발 환경
  - O/S : Windows 10
  - Server : Apache-tomcat-8.5.61
  - Java EE IDE : Eclipse ( version 2020-06 (4.16.0) )
  - Database : Oracle SQL Developer ( version 20.2.0 )
  - Programming Language : JAVA, HTML, CSS, JavaScript, JSP, SQL
  - Framework/flatform : Spring, mybatis, jQuery 3.5.1, Bootstrap v4.6.x
  - API : WebSocket, Kakao map, summernote
  - Version management : Git

------------

# 📝내용

* 팀원별 역할
  - 공통 : 기획, 요구 사항 정의, 와이어 프레임, DB 설계
  - 강수정 : 만남의 광장 게시판 CRUD, 채팅
  - 강보령 : 싱글이의 영수증 게시판 CRUD, 쪽지
  - 김현혜 : 공지사항 게시판 CRUD, 고객센터 게시판 CRUD, 관리자
  - 신주희 : 회원가입, 로그인, 마이페이지
  - 이솔이 : 일상을 말해봐 게시판 CRUD, 먹보의 하루 게시판 CRUD
  - 이한솔 : 벙글 장터 게시판 CRUD

* 설계의 주안점
  - 먹보의 하루 게시판 글 작성 시 맛집 위치를 지도에 표시할 수 있도록 할 것.
  - 벙글 장터 게시판 글 작성 시 작성자의 동네 위치를 인증할 것.
  - 만남의 광장 게시판 참여 회원 간의 채팅이 가능할 수 있도록 할 것.
  - 원활한 소통을 위해 회원 간의 쪽지 보내기/받기가 가능하게 할 것.

* DB 설계<br>
![singlebungle](https://user-images.githubusercontent.com/72387870/111110444-e7c9fa00-859f-11eb-926b-ac04e8e5d0ab.png)



------------

# 📝구현 기능

## 벙글장터 게시판

1. <h2 id="bungleMarketList">벙글장터 목록 조회</h2>

* 구현 기능 설명
  - 회원 등급제로 인해 3등급이 접근할 경우 메인 페이지로 돌려 보낸다.
  - 상품을 검색할 수 있다.
  - 전체 / 팝니다 / 삽니다 카테고리를 통해 상품들을 선택 조회할 수 있다.
  - 최신순 / 좋아요 순 / 저가 순 / 고가 순으로 상품들을 정렬할 수 있다.
  - 하트 버튼을 클릭하면 해당 상품을 찜할 수 있다.
  - 예약 중이거나 거래가 완료된 상품은 썸네일이 불투명해진다.

<br><br>

- 회원 등급이 3등급일 경우 <br> ![1등급제한](https://user-images.githubusercontent.com/70805241/112332376-07c68f80-8cfd-11eb-96c9-d0fb3e206af1.gif) <br> <br> <br>

- 목록조회 <br> ![2목록조회](https://user-images.githubusercontent.com/70805241/112419828-6fb5be00-8d6f-11eb-97b4-722ee3cfbdb5.gif) <br> <br> <br>

- 카테고리 & 정렬 <br> ![3카테고리정렬](https://user-images.githubusercontent.com/70805241/112420064-e18e0780-8d6f-11eb-9a4e-e6aa391a8b70.gif) <br> <br> <br>

- 검색 <br> ![4검색](https://user-images.githubusercontent.com/70805241/112420203-29149380-8d70-11eb-904f-b4833ae57ab7.gif) <br> <br> <br>

- 목록 찜하기 <br> ![5목록찜하기](https://user-images.githubusercontent.com/70805241/112420334-5feaa980-8d70-11eb-8058-8699d329be51.gif) <br> <br> <br>

<br> <br>

------------
<br>

2. <h2 id="bungleMarketView">벙글장터 상세 조회</h2>

* 구현 기능 설명
  - 목록으로 버튼을 누르면 목록 조회 페이지로 이동한다.
  - 상품 사진은 캐러셀을 이용하여 모든 사진들을 확인할 수 있다.
  - 본인 글일 시 상품의 거래 상태를 변경할 수 있다.
  - 찜하기 버튼을 누르면 해당 상품을 찜할 수 있다.
  - 글 작성자에게 쪽지를 보낼 수 있으며 본인 글일 시 쪽지 버튼은 클릭되지 않는다.
  - 글 작성자가 위치 인증을 했을 경우 거래 지역 오른편에 마커 아이콘이 나타난다.
  - 글 작성자의 닉네임을 누르면 해당 회원의 활동 내역을 확인할 수 있다.
  - 게시글을 신고할 수 있으며, 본인 글일 시 신고하기 버튼은 보이지 않는다.
  - 댓글과 대댓글을 작성할 수 있다.
  - 댓글과 대댓글 모두 수정, 삭제, 신고가 가능하다.
  - 페이지 하단에 조회 수 기준으로 벙글장터 게시글 세 개가 보이며 거래 중인 게시글만 조회된다.


<br><br>


- 상세조회 <br> ![6상세조회](https://user-images.githubusercontent.com/70805241/112424834-74cb3b00-8d78-11eb-8c39-b3b9cce47621.gif) <br><br><br>

- 본인 글 상세조회 
  - 본인 글일 경우 거래 상태 버튼, 게시글 수정, 게시글 삭제 등이 보임.<br> ![7본인글상세조회](https://user-images.githubusercontent.com/70805241/112425026-bbb93080-8d78-11eb-8cca-2e021bc6df26.gif) <br><br>
  - 본인 글일 시 쪽지 보내기 불가능 <br> ![쪽지](https://user-images.githubusercontent.com/70805241/112425720-e48df580-8d79-11eb-9805-b485140f0fe7.png) <br><br><br>


- 거래 상태 변경 
  - 거래 상태가 예약 혹은 거래 완료일 시 목록 조회에서 `opacity` 스타일 추가
  - 거래 상태가 거래 완료일 시 더이상 상태 변경 불가능<br> ![8거래상태변경](https://user-images.githubusercontent.com/70805241/112425283-27030280-8d79-11eb-9b35-b52f9bb4b14e.gif) <br><br><br>


- 상세조회 찜하기 <br> ![9상세조회찜하기](https://user-images.githubusercontent.com/70805241/112426356-2d927980-8d7b-11eb-85ee-b579b5f45387.gif) <br><br><br>


- 벙글장터 인기게시글 
  - 거래 상태가 거래 중이면서 조회수가 가장 높은 3개의 게시글을 조회 <br> ![벙글장터인기게시글](https://user-images.githubusercontent.com/70805241/112425837-1f902900-8d7a-11eb-86de-95e897640c83.png) <br><br><br>

------------
<br>


3. <h2 id="bungleMarketMypage">벙글장터 활동내역</h2>

* 구현 기능 설명
  - '이전 상세 게시글로 이동' 버튼을 클릭하면 이전 상세 조회 페이지로 이동한다.
  - 게시글 작성자의 활동 내역들을 조회할 수 있다.
  - 사진이나 제목을 클릭하면 해당 게시글의 상세 조회 페이지로 이동한다.

<br><br>

- 상세조회 닉네임 클릭 시 이동 <br>  ![10마이페이지](https://user-images.githubusercontent.com/70805241/112430810-00959500-8d82-11eb-8ed9-b41b22cd59cb.gif) <br><br><br>

------------
<br>

4. <h2 id="bungleMarketInsert">벙글장터 게시글 작성</h2>

* 구현 기능 설명
  - 회원 등급제로 인해 2등급일 경우 글쓰기가 불가능하다.
  - 이미지 등록 영역을 클릭하면 사진을 업로드 할 수 있다.
  - 사진 오른쪽 상단에 x 버튼을 클릭하면 사진을 삭제할 수 있다.
  - 거래 지역은 주소 검색 혹은 내 위치로 가져올 수 있다.
  - 등록하기 버튼을 클릭하면 유효성 검사를 실시하고 통과되면 해당 글의 상세 조회 페이지로 이동한다.

<br><br>

- 회원 등급이 2등급일 경우 <br> ![11글쓰기2등급](https://user-images.githubusercontent.com/70805241/112431275-a21ce680-8d82-11eb-8f89-4d8be1c7ef30.gif) <br><br><br>

- 게시글 이미지 등록 <br> ![12작성이미지등록](https://user-images.githubusercontent.com/70805241/112433068-148ec600-8d85-11eb-9de4-dbd1afbda5ef.gif) <br><br><br>

- 게시글 제목 <br> ![13게시글제목](https://user-images.githubusercontent.com/70805241/112434307-c4186800-8d86-11eb-82de-3c22cf1da2b0.gif) <br><br><br>

- 거래 지역 검색 
  - 카카오 지도 API 사용 <br> ![14주소검색](https://user-images.githubusercontent.com/70805241/112436960-bfa17e80-8d89-11eb-9765-e0c1e62999a1.gif) <br><br><br>

- 거래 지역 위치 받아오기 
  - HTML5 Geolocation API 사용 <br> ![15위치받아오기](https://user-images.githubusercontent.com/70805241/112437095-ea8bd280-8d89-11eb-9f2a-45c5579ac4b4.gif) <br><br><br>

- 게시글 등록 <br> ![16등록](https://user-images.githubusercontent.com/70805241/112437796-abaa4c80-8d8a-11eb-9b19-51a21f5160cb.gif) <br><br><br>


-----------
<br>

5. <h2 id="bungleMarketUpdate">벙글장터 게시글 수정</h2>

* 구현 기능 설명
  - 업로드 했던 사진들을 확인할 수 있다.
  - 이미지를 추가로 등록할 수 있다.
  - 이미지를 삭제할 수 있다.
  - 다양한 정보들을 수정할 수 있다.


<br><br>

- 이미지, 위치 수정 <br> ![17수정](https://user-images.githubusercontent.com/70805241/112440183-54f24200-8d8d-11eb-88a9-88f130bb9173.gif) <br><br><br>

-----------
<br>

6. <h2 id="bungleMarketReport">벙글장터 신고</h2>

* 구현 기능 설명
  - 타 회원의 부적절한 게시글을 신고 버튼을 클릭하여 신고할 수 있다.
  - 신고 제목, 신고 사유, 신고 내용을 입력하여 신고 버튼을 클릭하면 신고가 접수된다.

<br><br>

- 타인의 게시글 상세 조회 페이지 <br> ![18신고](https://user-images.githubusercontent.com/70805241/112440854-23c64180-8d8e-11eb-93ff-c052a216f074.gif) <br><br><br>


-----------
<br>

7. <h2 id="bungleMarketReply">벙글장터 댓글</h2>

* 구현 기능 설명
  - 댓글 작성 시 ajax를 통해 비동기적으로 등록한 댓글이 추가된다.
  - 댓글 수정 시 댓글 수정 내용을 입력할 수 있도록 기존 내용 영역이 textarea로 변경된다.
  - 수정 버튼 클릭 시 작성 시와 마찬가지로 ajax를 통해 댓글이 수정된다.
  - 답글 버튼 클릭 시 해당 댓글 영역 밑에 답글을 입력할 수 있는 textarea가 추가된다.
  - 답글 수정 시 댓글 수정 내용을 입력할 수 있도록 기존 내용 영역이 textarea로 변경된다.
  - 삭제 버튼 클릭 시 alert 창을 출력하여 한 번 더 확인할 수 있다.
  - alert 창에서 확인 클릭 시 답글 상태가 삭제로 바뀌며 ajax를 통해 댓글이 삭제된다.

<br><br>

- 댓글 등록 <br> ![19댓글](https://user-images.githubusercontent.com/70805241/112444338-46a62500-8d91-11eb-9f20-f659beabb1e2.gif) <br><br><br>

- 댓글 수정 <br> ![20댓글수정](https://user-images.githubusercontent.com/70805241/112444959-f7142900-8d91-11eb-8256-b39d8d02927e.gif) <br><br><br>

- 답댓글 등록 <br> ![21답댓글](https://user-images.githubusercontent.com/70805241/112444963-f8455600-8d91-11eb-98ed-38756984f779.gif) <br><br><br>

- 댓글 삭제 <br> ![22댓글삭제](https://user-images.githubusercontent.com/70805241/112444967-f8455600-8d91-11eb-866b-844ad8547238.gif) <br><br><br>


------------
<br>

8. <h2 id="bungleMarketDelete">벙글장터 게시글 삭제</h2>

* 구현 기능 설명
  - 삭제 버튼 클릭 시 alert 창을 출력하여 한 번 더 확인할 수 있다.
  - alert 창에서 확인 클릭 시 글의 상태가 삭제로 바뀌며 게시글 목록으로 이동된다.

<br><br>

- 본인 게시글 삭제 <br> ![23게시글삭제](https://user-images.githubusercontent.com/70805241/112444968-f8ddec80-8d91-11eb-84ce-2e4777121ab3.gif) <br><br><br>



------------
<br>

이상입니다. <br>
감사합니다! <br>