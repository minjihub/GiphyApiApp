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

처음에는 Gif List Adapter에서 Toggle 버튼을 클릭 했을 시 Room DB에 값이 저장/삭제 되도록 만들었습니다. <br>
하지만 Room DB를 이용하는 것을 model(비즈니스 로직)에서 처리하고 싶었고, Favorites 화면에서 모든 아이템은 Toggle ON 상태인데 다시 클릭해서 OFF되도록 만들면 DB에서 삭제가 되지만
리스트를 갱신시키는 부분에 어려움이 있었습니다. <br>
Trending GIFs 화면과 Favorites 화면이 공통된 어댑터를 사용합니다. Trending GIFs 화면에서는 Toggle 버튼 클릭에 따라 DB에 저장/삭제를 시켜야하지만 이에 따른 리스트 갱신이 필요하지 않고,
Favorites 화면에서는 Toggle ON 상태의 아이템만 보여주기 위해 리스트 갱신이 필요합니다. <br>

이런 부분을 해결하기 위해 adapter를 분리해야할까 잠시 고민했습니다. 하지만 동일한 아이템 뷰를 사용하고 위의 문제를 제외하고는 공통된 부분이 많은데 분리하는 것이 비효율적이라고 느꼈습니다. <br>
그래서 다른 방법을 생각해낸 것이 두 화면에서 다르게 구현되야 될 부분만 인터페이스로 분리시키는 방법이었고, ChangeGifDataStateListener라는 인터페이스를 만들었습니다. <br>
adapter에 이 인터페이스를 set할 수 있는 메소드를 만들고, Toggle isChecked 상태의 따라 set된 인터페이스를 이용해 움직이게 됩니다. <br>
그 결과 각 화면의 model에서 ChangeGifDataStateListener를 생성하여 각각 필요한 로직을 넣어 처리할 수 있게 되었고, Room DB를 이용하는 것 또한 model에서 처리하면서 문제를 해결할 수 있었습니다.


<br>

## 트러블 슈팅2

인터넷 환경이 고르지 못한 상태에서 앱을 실행해보니 상세화면에서 gif load fail 이슈가 발생되었습니다. 이 경우 강종되지는 않으나 아무것도 load 되지 않기때문에 화면 컨트롤이 필요했습니다. <br>
우선 오류가 발생되면 Glide에서 제공하고 있는 .error() 라인을 타게 됩니다. 먼저 이 라인을 추가하여 이미지 로드 실패 이미지를 보여주려 했습니다. <br>
그리고 '고화질 로드에 실패했다면, 저화질의 preview_gif url을 시도해보는게 어떨까'라는 생각이 들어서 error 발생 시에 저화질 url을 로드하는 glide를 한번 더 생성하고, 저화질 로드조차 실패한다면 실패 이미지를 보여주는 것으로 만들게되었습니다. <br>
<br>

```kotlin

Glide.with(this.context)
          .load(detailUrl)
          .listener(requestListener)
          .error(
                      Glide.with(detailView)
                             .load(previewUrl)
                             .listener(requestListener)
                             .error(R.drawable.error_load_fail_gif)
          )
          .into(detailView)
```  
<br>

그러던 중 into 된 ImageView가 wrap_content로 되어있는데, Glide는 LayoutParams.WRAP_CONTENT 일 경우 디바이스 사이즈에 맞게 이미지를 요청해준다는 것을 알게 되었습니다.
(로드 된 gif의 width, height가 아닌 디바이스에 알맞는 크기의 gif가 보임) <br>
하지만, error를 탔을 경우 새로 실행하는 Glide 객체에서는 .with 안에 context가 아닌 ImageView를 넣어주기때문에 디바이스 사이즈를 고려하지 않은, 로드 된 gif의 width과 height이 세팅이 되면서 매우 작은 사이즈로 보였습니다. <br>
이 문제를 해결하기 위해 width를 match_parent로 변경하였으나 Dialog 객체에서는 match_parent를 적용한 ImageView에 gif가 로드 되지 않았고, 현재 디바이스의 width를 가져와서 ImageView의 width를 변경하였더니 로드가 되었습니다.(마진을 주기위해 width-200) <br> 
Dialog는 width가 match_parent인 layout이더라도 고정된 width값을 세팅해주지 않으면 적용이 안되는 것 같았고, 공식문서에 Dialog를 직접 인스턴스화하는 것을 삼가하고 대신 서브 클래스인 AlertDialog를 사용하라고 명시되어 있었습니다. <br> 
AlertDialog로 변경하였더니 match_parent가 적용되면서 현재 디바이스의 width를 가져오는 것은 불필요해졌고 모든 로드되는 gif는 동일한 너비(match_parent - margin값)로 볼 수 있게 되었습니다. <br>
   

<br>

## 결과
#### 모든 imageView는 gif로 움직이지만 캡쳐 이미지를 첨부한 점 참고 부탁드립니다.
1. Trending GIFs 화면
<img src="https://user-images.githubusercontent.com/70570798/110091163-82298100-7ddb-11eb-97a9-86a93858fd38.jpg"  width="350" height="600">
<br />

2. Trending GIFs > 아이템 클릭 > 상세 GIF 화면 (고화질)
<img src="https://user-images.githubusercontent.com/70570798/110091627-01b75000-7ddc-11eb-99e3-d95879f4047b.jpg"  width="350" height="600">
<br />

3. Favorites 화면 (모든 아이템은 Toggle ON 상태)
<img src="https://user-images.githubusercontent.com/70570798/110091780-2d3a3a80-7ddc-11eb-9a53-5f9838b22f69.jpg"  width="350" height="600">
