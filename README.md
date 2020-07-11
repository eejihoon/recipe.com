# django
## 작업내용
### view
- 인덱스 이미지 수정
- recipe list, get페이지(django에 있던 recipe를 옮김)
    &&페이지 정보 가지고 조회, 목록으로
- 글씨체 약간 수정
- 로그인-아웃 드랍다운 바꿈

### controller
- IndexConroller에서 레시피를 조회할 수 있는 메서드를
 RecipeController로 이동


## 레시피 조회 페이지 이슈
- '목록' 버튼을 눌렀을 때,
index페이지에서 접근했다면 index페이지로
recipe페이지에서 접근했다면 list페이지로 redirect해줘야 한다.

## 해야 할 것
- myFridge페이지 컨테이너 씌우기
-