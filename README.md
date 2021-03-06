# GiphyApiApp
(GIPHY API) Trending GIFs를 보고, 마음에 드는 GIF를 셀렉하는 앱

<br>

## 언어
- Kotlin
<br>

## 디자인 패턴
- MVP
<br>

## 라이브러리
#### Retrofit2
 - GIPHY API와 통신
 
#### Glide
 - ImageView에 gif 파일 로드
 
#### Room
 - 셀렉한 GIF를 SQLite에 저장
 
<br>

## 화면 구성
1. 하단 네비게이션 바 > 2,3으로 이동할 수 있는 탭 형식
2. Trending GIFs 화면
3. Favorites 화면
4. 아이템 클릭 시 상세 화면

<br>

## 로직

API 통신 후 얻어온 GIF URL 로드
 - 원본 url은 file size가 커서 로드 불가능
 - 빠른 로드를 위해 preview_gif url값 사용 > 'preview_gif'는 GIF의 처음 1-2초를 표시하며 50kb이하의 데이터로 가장 가벼움
 - 아이템 클릭 시 상세 화면에서는 고화질로 보여주기 위해 preview_gif보다 화질이 높은 downsized(2MB미만) url 로드 

<br>
 
Toggle 버튼 클릭 시 Room DB 저장/삭제
 - Room DB에 저장된 값을 모두 조회한 뒤 Trending GIFs 리스트를 보여줄 때 Toggle ON/OFF 상태 결정
 - Favorite 화면에서 Toggle OFF할 경우 Room DB에서 제거 > 리스트 갱신
 
<br>
 
검색 결과 30개씩 페이지네이션 처리
 - 스크롤 시 마지막 포지션이 리스트 총 카운트에 도달했을 때 다음 30개를 호출하여 리스트에 추가시킨 뒤 갱신
 - 결과 시작 위치를 알려주는 offset은 최대 4999이므로 4999이상일 경우 호출하지 않도록 처리
  

<br>

## 트러블 슈팅

처음에는 Gif List Adapter에서 Toggle 버튼을 클릭 했을 시 Room에 값이 저장/삭제 되도록 만들었습니다. <br>
하지만 Room DB를 이용하는 것을 model(비즈니스 로직)에서 처리하고 싶었고, Favorite 화면에서 Toggle이 ON 상태이고 다시 클릭해서 OFF되도록 만들면 Room에서 삭제가 되지만
리스트를 갱신시키는 부분에 어려움이 있었습니다. <br>
Trending GIFs 화면과 Favorite 화면이 공통된 어댑터를 사용합니다. Trending GIFs 화면에서는 Toggle 버튼 클릭에 따라 Room에 저장/삭제를 시켜야하지만 이에 따른 리스트 갱신이 필요하지 않고,
Favorite 화면에서는 Toggle ON 상태의 아이템만 보여주기 위해 리스트 갱신이 필요합니다. <br>

이런 부분을 해결하기 위해 ChangeGifDataStateListener라는 인터페이스를 만들었고, adapter에 이 인터페이스를 set할 수 있는 메소드를 만들었습니다.<br>
그 결과 각 화면의 model에서 ChangeGifDataStateListener를 생성하여 각각 필요한 로직을 넣어 처리할 수 있게 되었고, Room DB를 이용하는 것 또한 model에서 처리하면서 문제를 해결할 수 있었습니다.


<br>

## 결과
#### 모든 imageView는 gif로 움직이지만 캡쳐 이미지를 첨부한 점 참고 부탁드립니다.
1. Trending GIFs 화면
<img src="https://user-images.githubusercontent.com/70570798/110091163-82298100-7ddb-11eb-97a9-86a93858fd38.jpg"  width="350" height="600">
<br />

2. Trending GIFs > 아이템 클릭 > 상세 GIF 화면
<img src="https://user-images.githubusercontent.com/70570798/110091627-01b75000-7ddc-11eb-99e3-d95879f4047b.jpg"  width="350" height="600">
<br />

3. Favorites 화면 (모든 아이템은 Toggle ON 상태)
<img src="https://user-images.githubusercontent.com/70570798/110091780-2d3a3a80-7ddc-11eb-9a53-5f9838b22f69.jpg"  width="350" height="600">
