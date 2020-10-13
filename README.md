# django
## 작업내용
### view
- header.html 인덱스 이미지 수정,
    && 로그인-아웃 dropdown 변경
- recipe폴더 recipe list, get페이지(django에 있던 recipe를 옮김)
    &&페이지 정보 가지고 조회, 목록으로
- django폴더 - recipe.html 삭제 -> recipe폴더 안에 get.html으로 대체
- index.html 'ooo님 추천 메뉴' 글씨체 수정
- myFridge.html 컨테이너 씌우기
- footer수정
- index페이지 접속 시, 로그인 아닐 경우 레시피 안 나오게.


### controller
- IndexConroller - 레시피 조회 메서드(recipe()) 삭제. RecipeController로 이동.
&& 레시피 20개씩 나오도록 수정
- RecipeController - 레시피 조회 메세드 생성
- OCRController - scan() return값 /django/index -> /django/myFridge로 변경.


## 레시피 조회 페이지 이슈
- '목록' 버튼을 눌렀을 때,
index페이지에서 접근했다면 index페이지로
recipe페이지에서 접근했다면 list페이지로 redirect해줘야 한다.

## 해야 할 것
