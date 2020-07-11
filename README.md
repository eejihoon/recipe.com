# django
# 인덱스 이미지 수정
레시피 컨트롤러, 레시피 리스트 페이징

IndexConroller에서 레시피를 조회할 수 있는 메서드를
RecipeController로 이동

페이지 정보 가지고 조회, 목록으로

레시피 조회 페이지 이슈
'목록' 버튼을 눌렀을 때,
index페이지에서 접근했다면 index페이지로
recipe페이지에서 접근했다면 list페이지로 redirect해줘야 한다.