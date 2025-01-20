# OCR_OpenCV_Android

#### 📢 핸드폰 카메라 기본 어플을 활성화시켜 TessTwo로 촬영된 이미지의 문자를 인식하는 어플리케이션
🖥️ 개발환경 : Java, Android Studio, Tesseract(TessTwo)  
📜 기존 OCR_Android 에서 응용버전이라고 생각할 수 있다. (깃허브 주소: https://github.com/inaeee/OCR_Android.git)         

📜 Tesseract ?
> OCR 광학 문자 인식 오픈 소스 라이브러리. 
> 다양한 OS를 지원하기 위한 엔진으로 무료 소프트웨어이며 Apache 라이선스이다.

### 영상 인식 알고리즘
1. Preprocessor
> 최초 들어온 이미지를 처리하기 위해 히스토그램 스트레칭과 히스토그램 평활화, 이진화 작업과
영상 작업을 통하여 보다 효율적인 출력물을 얻기 위한 전처리 작업.
히스토그램, 이치화, 역상
2. Segmentation
> Preprocessor 작업을 거친 뒤 이미지에서 글자 단위로 혼합의 Text를 분류하는 작업

📜 Tesseract 원리
> 오프라인 문자인식 기법으로 입력된 input 이미지의 특징점을 추출하고 그것을 사용하여 문자를 인식한다.
기본적으로 상위의 Preprocessor와 Segmentation 과정을 거쳐 나온 이미지를 시녕망 기법과 template matching 기법을 사용하여
input 이미지를 인식하고 출력한다.

📜 Training
> Tesseract는 인식률 향상을 위해서  Training 데이터를 적용할 수 있다.
미리 학습 시켜놓은 데이터들을 Tesseract 데이터 베이스에 저장하여 글자와 트레이닝 데이터와 유사율을 비교하여 인식 결과를 반환한다.

<br>

#### CameraView.java 기본 카메라 권한 요청 및 호출, 촬영
1. android.permission.CAMERA 카메라 권한
2. hasPermissions 권한 확인, requestNecessaryPermissions 권한 요청, onRequestPermissionsResult 권한 결과값
3. mLoaderCallback 퍼미션 확인 후 카메라 활성화
4. onCreate 카메라 설정 및 뷰 선언, 상태바와 네비게이션 바 없애고 풀 스크린 상태 생성, 방향 센서 인식 및 전환 
5. onClickButton 촬영 버튼 클릭 시, 사진에 필요한 부분만 자르고 ROI 영역 안에 표시 / 재촬영 액션 시, 촬영 이미지 제거 후 모든 뷰가 초기 상태로 돌아온다.

#### MainActivity.java
1. Tesseract 모듈 초기화 및 CameraView 호출
2. startActivityForResult : 선언한 Intent 호출출
3. onActivityResult : OCR 결과 출력 
4. AssetManager : assets 폴더에 넣었던 tessdata를 기기에 다운받을 수 있게 한다.
5. Tesseract 객체는 static 으로 선언하여 CameraView에서도 사용 가능하게 한다.
