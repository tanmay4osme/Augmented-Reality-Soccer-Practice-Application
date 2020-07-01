# 증강현실 축구 연습 어플리케이션
## Augmented-Reality-Soccer-Practice-Application
사용자가 코치의 도움없이 여러 훈련을 개인적으로 할 수 있도록 도와주는 어플리케이션입니다.

It is a soccer-practice-application that helps the user to do various training personally without the help of a coach.

### 이찬솔 이기열 이교범 최철환
## Representative email : cch01024857239@gmail.com

### 이 문서와 프로젝트는 한국산업기술대학교 컴퓨터공학부의 “종합설계”교과목에서 프로젝트“스마트 축구 연습 어플리케이션”을 수행하는  팀원(팀번호: S5-2)들이 작성한 것으로 사용하기 위해서는 팀원들의 허락이 필요합니다.
### These document and project are written by team members (Team No . S5-2) who perform the project “Augmented-Reality-Soccer-Practice-Application” in the “Graduation Thesis” course of the Korea Polytechnic University.
## Copyright 2020. "Graduation Thesis Team No .S5-2 of the Korea Polytechnic University" All document and project Cannot Be Copied Wihour Permission.

# 개발환경 (Development environment)
환경|버전
---|---|
Android Studio|3.4.2|
Aws RDS-MY SQL|8.0.15|
Ubuntu Server|18.0.4|

# 라이브러리 (Library environment)
라이브러리|버전
---|---|
Opencv|4.1.0|
Tensorflow|tensorflow_lite|

# 목차
![Contents](./image/start.JPG)

# 서론
## 1.1 작품선정 배경 및 필요성
 20세기부터 축구는 이미 세계 인기 스포츠, 시장규모, 선수 연봉에서 1위라는 순위를 유지하고 있다. 이러한 인기로 인해 축구를 배우고자 하는 사람은 수많이 존재하고 있다. 하지만 축구를 배우기 위한 레슨에는 시간당 36,000원, 유학에는 2개월에 약 360만 원이라는 고비용이 필요하다. 보다 적은 비용으로 혼자 다양한 연습을 할 수 있는 대안이 필요하고, 코치 없이 실시간으로 피드백을 받을 수 있는 환경이 필요하다고 생각하여 졸업작품으로 선정하였다. 

## 1.2 기존 연구/기술 동향 분석
 관련 제품으로는 스마트폰을 이용한 농구 연습 어플리케이션인 홈코트가 존재한다. 이 제품은 증강현실을 이용하여 혼자서 슛, 드리블의 훈련이 가능한 농구 연습 어플리케이션으로 인공지능을 이용한 자세 교정을 통해 비싼 코치 없이 혼자 연습이 가능하다는 장점을 갖고 있다. 하지만 연습 가능한 스포츠가 농구로 한정되어 있다.

## 1.3 개발 목표
 최종 목표로는 스마트 축구 연습 어플리케이션을 개발하여, 보다 적은 비용으로 다양한 연습을 도와줌과 동시에 개인 단위로 혼자 연습할 수 있는 환경을 만들어 주는 것이다. 단계별로 들어가자면 일단 opencv를 이용하여 파이참 환경에서 구현해 본 뒤, 안드로이드 스튜디오 환경에서 공을 인식하여 공의 궤적과 속력, 도착한 지점 등을 영상 처리를 통한 시각화 구현과 간단한 UI 작성과 서버 구축이다. 다음 단계로는 opencv와 tensorflow_lite를 이용한 자세 인식구현 및 자세 인식의 신경망 및 딥러닝 구축으로 안드로이드 스튜디오 환경에서 신경망의 기본 형식을 구현한 후 CNN을 이용하여 세부적인 부분을 추가하여 신경망을 구축한다. 그 후 사용자의 자세를 신경망에 학습된 자세와 비교하여 사용자의 자세를 교정하는 것을 구축한다. 마지막 단계로는 1단계에 간단히 작성되었던 UI를 좀 더 사용자 편의에 맞게 구축하고 어플리케이션으로 쉽게 연습 선택, 영상 시청, 연습 기록을 수치화 등의 기능을 제공하고 서버에 저장된 영상을 사용자에게 제공할 수 있게 인터페이스를 구축한다. 

## 1.4 팀 역할 분담
 역할 분담으로는 인공지능, 영상처리, 앱 UI 작성, DB 및 서버 구축으로 4개로 나누어 각자 2개씩 맡음으로써 혼자서 해결하기 어려운 문제나 막히는 부분을 상호보완하여 해결할 수 있도록 하였다. 각자 역할로는 이찬솔 인공지능-영상처리, 이기열 영상처리-DB 및 서버 구축, 이교범 인공지능-영상처리, 최철환 앱 UI 작성-DB 및 서버 구축을 맡도록 하였다. 산출물 관리는 개인 PC와 구글드라이브, 개인 메일로 3개에 공간에 백업, 보관하며 코드작성은 Github를 활용하여 수정 부분을 알 수 있도록 하였다.

## 1.5 개발 일정 
 개발 일정은 다음의 표대로 진행할 수 있도록 일정을 잡았고 매주 금요일마다 만나 서로 작성한 것을 공유하고 진행 상황을 알 수 있도록 하였다.
![calendar](./image/m1.JPG)

## 1.6 개발 환경
 개발 환경으로는 앱을 작성할 수 있도록 안드로이드 스튜디오를 사용하고 언어로는 자바를 사용하여 작성한다. 사용하는 스튜디오 버전은 3.4.2이다. 안드로이드 타깃은 최소 8.1(오레오)_API Level 27 으로, 최대 타깃은 10.0(안드로이드 10)_API Level 29이다. 다음으로 DB는 아마존에서 제공하는 AWS-RDS를 사용하여 MY-sql DB를 구축하고 서버와 연동할 수 있도록 하였다. 사용 언어로는 sql문을, 버전은 8.0.15를 사용한다. 서버 역시 마찬가지로 아마존에서 제공하는 AWS-EC2를 사용해 unbuntu 환경을 구축하고 사용 언어로 c++을 사용하여 버전 18.0.4에 구축한다. 마지막으로 영상처리, 인공지능 부분은 파이참 환경에서 파이썬을 가지고 작성할 것이다. 버전은 2019.3버전을 사용한다.
 ![environment](./image/m2.JPG)

# 본론
## 2.1 개발 내용
### UI
 ![UI](./image/ui1.JPG)
 ![UI](./image/ui2.JPG)
 ![UI](./image/ui3.JPG)
 ![UI](./image/ui4.JPG)
 ![UI](./image/ui5.JPG)
 ![UI](./image/ui6.JPG)
 ![UI](./image/ui7.JPG)
 ![UI](./image/ui8.JPG)

### 영상 처리
  ![Image processing](./image/v1.JPG)

### 서버 구축
  ![Server](./image/s1.JPG)
  ![Server](./image/s2.JPG)
 
## 2.2 문제 및 해결방안
 1. Ubuntu 서버와 Mysql DB간에 보안으로 인한 접속 문제가 발생하였다. 이를 해결한 방법으로는 Mysql DB에 접속할 특정 IP를 인바운드 규칙에서 허용하여 해당 IP주소에서 DB에 접속할 수 있도록 함으로써 해결해야 했다. 
 2. 공 트래킹을 할 때 색을 따라 트래킹을 하도록 하였는데 이 방식이 아닌 원이나 아니면 축구공을 인식할 다른 방법을 찾아서 트래킹을 하는 방법이 필요하였다. 원 검출을 이용하기 위해서 안드로이드 openCV의 Imgproc.HoughCircles함수를 이용하여 원을 검출하였다
 3. 궤적을 그려줄 때 공의 반지름이 작아질 경우 슈팅을 하였다고 인식하여 궤적이 표시되게 하려고 했으나 빠른 속도로 처리를 하는 만큼 반지름의 크기가 순서대로 저장되지 않아 올바른 궤적을 그려줄 수 없었다. 궤적을 그려주는 것을 좌표를 이용하여 슈팅한 것을 인식하도록 수정하였다.
 4. 자바와 Python 연동 문제로는 Kivy를 이용하여 안드로이드 위에 파이썬 코드를 올리는 방법을 이용하여 해결하려 했으나 카메라 제어 문제로 파이썬 openCV 코드를 안드로이드 openCV로 변환하여 해결하였다.
 
## 2.3 시험 시나리오 
총 5가지의 시나리오를 진행하였다. 
 첫 번째로, openCV 라이브러리를 이용하여 공 감지로 카메라에서 초록색 물체를 감지하게 되면 초록색 물체를 공으로 인식한 후 그 테두리에 원을 그려준다. 
 두 번째로, 인식한 공이 움직일 때 움직임을 감지하여 이동 경로를 따라 궤도를 표시, 저장, 출력한다.
 세 번째로, tensorflow_lite의 posenet 라이브러리를 이용하여 사람 관절을 인식하게 되면 관절을 랜드마크로 인식한 후 표시한다.
 네 번째로, 속력을 구하기 위해 거리를 구하는데 카메라로 보이는 골대 높이를 측정하여 각각 거리마다 저장한다. 충분히 쌓인 데이터를 통해 최소 자승법을 이용하여 회귀분석을 통한 골대 높이-거리의 함수를 구하고 실제 이용 시에는 골대높이를 측정하고 함수를 이용하여 거리를 구한 후 속력을 구한다.
 다섯 번째로, 서버 통신으로 aws-ec2를 이용하여 ubuntu 서비스를 구축한 후 서버에서 my-sql을 이용하여 db를 수집한다. 그 후 서버에서 aws-rds를 연동하여 db에 저장한다. 마지막으로 애플리케이션 통신으로 사용자가 애플리케이션을 통해 요구사항을 입력하게 되면 입력된 데이터를 db에 저장한다.
 
## 2.4 상세 설계
 ![Flowchart](./image/p1.JPG)
 ![Flowchart](./image/p2.JPG)
 ![Flowchart](./image/p3.JPG)
 
### 인식
 동작의 순서는 위의 시퀀스 대로 진행이 되도록 설계를 하였고 내부 코드로 들어가면 다음과 같다. 일단 공과 골대 인식 같은 경우에는 opencv를 이용하여 인식한다. 인식할 때에는 색상을 통해 인식하며 이를 위해 HSV 색상변경을 하고 특정 색상 값을 마스크로 지정하여 트래킹을 진행하게 된다. 그 후 공의 경우 슈팅 인식은 지면에서 떨어졌을 경우라고 생각을 하고 y좌표가 감소할 때에 궤적을 그려주게 된다. 모션 인식의 경우에는 tensorflow_lite의 posenet 라이브러리를 이용하여 인식하게 된다. 그래서 사용자의 관절을 인식할 수 있게 되고 인식한 관절을 통해 지면과의 각도나 관절들 간의 각도를 이용하여 올바른 자세의 피드백이 가능하도록 해준다.
 
 ### 수치화
 인식한 내용으로 사용자의 기록을 수치화해준다. 수치화는 모든 연습에서 진행이 되며 각각의 연습에서 얻는 내용을 다르게 하였다. 일단 자신의 기록을 그대로 저장해 주는 것은 물론이고 슈팅 연습의 경우에는 공의 속력을 이용하여 사용자의 슈팅 파워를 측정한다. 속력을 구하기 위해 거리를 구해야 하는데 카메라로 보이는 골대 높이를 측정하여 각각 거리마다 저장한다. 충분히 쌓인 데이터를 통해 최소 자승법을 이용하여 회귀분석을 통한 골대 높이-거리의 함수를 구하고 실제 이용 시에는 골대 높이를 측정하고 함수를 이용하여 거리를 구한 후 속력을 구한다. 우리가 구한 함수는 y=12.925x-1으로 결정계수 R2, 즉 신뢰도가 97%라는 결과를 내었다. 공이 골대에 들어간 횟수와 골대를 9등분 하였을 때 외곽에 가깝게 위치할수록 높은 점수를 주어 사용자의 정확성을 기록한다. 피지컬 연습의 경우에는 체력, 순발력 연습이 존재하는 데 시간을 기준으로 각각의 연습을 통해 점수를 얻고 그 점수를 통해 사용자의 순발력이나 체력을 측정한다. 트래핑 연습은 화면에 공을 보내야 하는 위치를 동그라미로 표시를 해주어서 자신이 원하는 곳에 한 번의 터치로 보낼 수 있는 능력을 기르는 데에 의의를 둔다. 트래핑 연습에서의 점수는 시간을 기준으로 이용하여 사용자의 트래핑 능력을 측정한다. 수치화는 각각 훈련 별로, 주차별로 저장, 출력하여 사용자가 어느 훈련을 얼마큼 했는지, 어느 훈련이 부족한지 알 수 있게 하고, 주차별로 훈련계획을 세울 수 있도록 출력한다.
 
### 딥러닝
  딥러닝은 다음의 방식으로 설계하였다. 먼저 일반적인 FCNN과 CNN의 가장 큰 차이점을 알아야 한다. CNN은 이미지 인식에 있어 이미지의 모든 픽셀을 분석하지 	않고 지역적, 공간적 상관관계를 고려한 학습을 한다. 따라서ﾠ효과적으로 Data-representation을 수행하고 이를 통해 분류 작업에 있어 높은 성능을 보인다. 매트랩을 활용한 CNN으로 좋은 자세의 데이터를 획득한다. 데이터 전처리는 유튜브를 통해 얻은 슈팅 방법별 영상을 다운로드, 편집하여 데이터를 확보하였다. 모델 설계는 분석을 위해 최소한의 CNN 구조를 2conv 계층, 1fcnn 계층으로 구성하였다. 학습 과정으로는 over fitting을 방지하기 위해 early stopping을 하였다.
  
### 데이터베이스
 데이터베이스의 설계는 다음과 같다. 회원 정보 테이블은 닉네임을 기본, 외래키로 지정하고 이름 핸드폰 생년월일 이메일 sns 회원 번호를 스키마로 지정한다. 사용자 영상 테이블은 영상번호를 기본 키로 회원 번호+닉네임을 외래키로 가지며 사용자 영상 날짜 사용자 능력(수치화)-슛, 피지컬, 트래핑을 스키마로 지정한다. 슈팅 테이블은 영상 이름을 기본 키로 슛 이름 영상(URL), 추천 능력치를 스키마로 지정하고 코멘트 테이블은 코멘트 번호를 기본 키, 영상번호를 외래키, 닉네임을 스키마로 좋아요 테이블은 좋아요 번호를 기본 키, 영상번호를 외래키, 닉네임을 스키마로 지정한다.
 
![DB](./image/db1.JPG)
![DB](./image/db2.JPG)

### UI
 마지막으로 <그림 20>는 UI의 상세 설계이다.
 
![UIdesign](./image/uip1.JPG)
![UIdesign](./image/uip2.JPG)

## 2.5 Prototype 구현
### 인식
인식의 경우 공과 골격 인식, 골대 인식, 거리 인식이 있다, 공 인식의 경우 상세 설계에서 계획하였던 것처럼 opencv를 이용한 허프 변환과 명도를 이용한 색상검출을 이용하여 인식하게 된다. 공을 인식하게 되면 리스트에 반지름이 저장되어 궤적이 저장되어 궤적을 인식하게 된다. 공인식의 최적화를 위해 궤적의 리스트를 삭제, 추가를 하게 된다.
 골격 인식의 경우 연산속도의 저하와 영상의 프레임 저하를 위해 골격 다중인식에서 싱글 인식으로 연산 처리량을 줄었으며 한 프레임마다 인식하게 하여 프레임을 증대시켰다. 더 작은 모델도 인식 가능하도록 데이터 셋을 추가하였으며 그에 대한 과부하 방지로 멀티 쓰레딩을 이용했다. 4개의 쓰레드를 연결 4개씩, 각 쓰레드의 서브 쓰레드가 스케줄이 가능하도록 알고리즘을 추가하고, 멀티 쓰레딩으로 동시 수행 가능하도록 하였다.
 
![perceive](./image/perceive.jpg)

 공인식의 경우 하프변환을 이용하여 Opencv 라이브러리를 이용하려고 하였지만, 공이 작아지면 인식이 불명확해지는 부분이 있어 공의 이미지를 딥러닝으로 훈련 시켜 개선 시키고자 하였다. 공의 이미지는 구글의 이미지와 공인구 데이터베이스 사이트로부터 BeautifulSurp를 이용해, 크롤링하였다, 궤적에 있어 공이 찌그러지는 것도 인식하기 위해 이미지를 변형하여 총 57000장을 모아 텐서플로우의 keras를 이용하여 합성곱 신경망을 구축하여 Epoch를 20, betch를 600으로 두어 학습률을 높였고 85%가 넘는 훈련된 모델을 얻었다. 이 학습된 모델을 기반으로 가중치 파일을 객체 인식을 위한 Yolo 알고리즘의 훈련모델로 전환하고 Yolo망을 구축했다. 객체 인식을 위해 각 이미지마다 라벨링을 하기 위한 형태학적 변환을 이용하였다. 공의 이미지에 가우시안 블러를 진행한다. 회색조로 변환한 Gray Scale 이미지에 임계(threshold)를 적용한다. 즉, <그림 21>의 과정대로 특정 값인 임계값 이하의 값을 갖는 픽셀을 검정색으로 변환하고, 임계값 이상의 값을 갖는 픽셀을 흰색으로 변환해 흑백 이미지를 얻는다. 불필요한 영역 등 잡영(noise)을 제거하는 효과가 있기 때문에 사물을 탐지할 때 효과적으로 Contour를 추출할 수 있다. 하지만 실제 이미지에는 잡영이 생각보다 많기 때문에 다음과 같은 과정을 거쳐 잡영을 최대한 제거해야 한다. Contour 추출 과정에 몇 가지 과정이 더 들어갔다. Morph Gradient는 사물의 테두리를 더 정확하게 추출할 수 있게 이미지를 처리하는 과정이고, Morph Close는 Contour의 영역을 찾는 과정이다. Morph Gradient는 사물의 테두리에 팽창(dilation)을 적용한 결과와 침식(erosion)을 적용한 결과의 차이로 테두리(외곽선)를 더 정확하게 추출하는 과정이다. 커널의 크기를 2 x 2픽셀로 하였고, 그 다음 findContours를 이용하여 테두리를 검출하였다. 이 결과로 얻은 공의 테두리에 라벨링, 바운딩하여 이미 훈련된 모델을 통한 전이학습을 통한 훈련을 진행하였다.
 
 ![DeepLearning](./image/deep.PNG)
 
 골대 인식의 경우 영상의 프레임을 회색조로 변환 가우시안 블러를 진행한다. 회색조로 변환한 Gray Scale 이미지에 임계(threshold)를 적용한다. 즉, <그림 21>의 과정대로 특정 값인 임계값 이하의 값을 갖는 픽셀을 검정색으로 변환하고, 임계값 이상의 값을 갖는 픽셀을 흰색으로 변환해 흑백 이미지를 얻는다. 불필요한 영역 등 잡영(noise)을 제거하는 효과가 있기 때문에 사물을 탐지할 때 효과적으로 Contour를 추출할 수 있다. 하지만 실제 이미지에는 잡영이 생각보다 많기 때문에 다음과 같은 과정을 거쳐 잡영을 최대한 제거해야 한다. Contour 추출 과정에 몇 가지 과정이 더 들어갔다. Morph Gradient는 사물의 테두리를 더 정확하게 추출할 수 있게 이미지를 처리하는 과정이고, Morph Close는 Contour의 영역을 찾는 과정이다. Morph Gradient는 사물의 테두리에 팽창(dilation)을 적용한 결과와 침식(erosion)을 적용한 결과의 차이로 테두리(외곽선)를 더 정확하게 추출하는 과정이다. 커널의 크기를 2 x 2픽셀로 설정했다.
 거리 인식의 같은 경우 좌표상의 거리 - 실제 거리 함수로 실제 거리를 추출, 속력을 구한다. 일정 거리마다 일정 비율로 작아지는 카메라의 특성을 이용하여 많은 좌표상의 거리(x)와 그에 대응하는 실제 거리(y)를 이용하여 회귀분석을 통한 최소 자승법으로 좌표상의 거리- 실제 거리 함수를 유도하였다.

### 자세피드백
 처음에는 자세 인식을 딥러닝을 이용해 학습을 시키려고 하였으나 이미 Posenet 을 사용하여 프로그램이 너무 무거워져 공의 좌표나 몸의 좌표가 끊기게 되어 <그림 25> 「세가지 축구 슈팅 동작의 운동학적 비교 –1999,한국체육학회지_진영완_동의대학교,최지영,신제민_연세대학교」의 논문의 <그림 25> 표를 기반으로 Posenet에서 제공하는 양쪽 발목, 무릎, 골반, 손목, 팔꿈치, 어깨의 위치 좌표를 사용하여 그 좌표간의 각도나 거리를 이용해 피드백을 주는 것으로 수정하였다. 자세한 자세 인식은 공을 인식해 공의 중심좌표를 찾아내고 공의 중심좌표와 양쪽 발목의 좌표의 거리가 일정 거리 이하로 줄어들면 그때부터 리스트에 공과 몸의 위치의 좌표를 기억한다. 그 후 슈팅이 끝난 것을 공의 위치로 특정해 낸 다음 아까 저장한 위치 좌표를 불러와 슈팅하기 전 디딤발 딛는 순간과 공에 한쪽 발목이 만난 순간, 슈팅하고 난 직후의 순간을 뽑아내고 이를 이용해 디딤발과 공의 거리, 발목과 무릎의 각도, 골반과 무릎의 각도, 손목과 팔꿈치 어깨를 이용한 상체의 기울기를 계산하고 올바른 자세의 각도를 입력한 값과 비교를 한다. 그 후 각도가 얼마나 차이가 나는지 디딤발의 위치에 대해 피드백을 해준다. 
 
![peedback](./image/peed.jpg)
 
### 데이터 베이스
데이터베이스의 경우 상세 설계에서 계획했던 스키마 설계를 서버 데이터베이스에 등록하여 구현을 완료하였고, 최종적으로는 타 SNS 계정을 등록하여 회원가입이 가능하도록 하려하며, 현재는 유저 정보의 nickname을 아이디처럼 키로 활용하여 로그인 및 회원가입 기능을 대체하고 있다. 서버 데이터베이스와의 데이터는 php파일을 경유하여 어플리케이션과 주고받으며 동영상의 업로드 역시 같은 방식으로 진행하였다. 서버는 유동 퍼블릭 IP를 사용하였었으나, 최종 사용 시 IP의 잦은 변동은 불편이 많을 것이라 생각되어 고정 IP를 할당하였다. 다중 사용자의 S3 스토리지 접근 및 권한 관리를 위해 Cognito를 활용하여 불특정 다수의 사용자들에게 필수적인 자료 업로드 및 다운로드 권한만을 주도록 하였다. 스토리지에 보관된 영상에 사용자들이 접근하는 방식에 대해선 현재 방식을 논의 중이며 데이터 이동 소요가 많은 다운로드보다 URL을 통한 일시적 영상 스트리밍을 고려 중이다.

### 영상기록
 camera2 라이브러리와 MediaRecorder를 이용하여 슈팅 연습을 시작하고 공을 차는 순간부터 동영상 녹화가 시작되고, 공이 골대에 들어간 순간 녹화가 종료된다. 세부적인 순서는 슈팅 연습을 시작하면 openCamera 메소드에 의해 카메라가 켜지고 camera manager를 통해 여러 카메라 정보를 얻는다. 그 뒤, camera device를 열고 cameracapturesession 메소드를 이용하여 surfaceview로부터 영상의 미리 보기를 할 수 있도록 설정해준다. 다음으로 직접 정의한 startrecording과 stoprecording을 이용하여 영상을 녹화하고 정지해줄 수 있다. startrecording는 녹화를 시작해주는 메소드로서, 먼저 MediaRecord 객체를 이용하여 오디오와 영상의 저장경로나 인코더, 소스 등을 지정해주고 prepare 함수를 이용하여 녹화 시작의 준비를 해준다. 다음으로  MediaRecorder 객체의 start 함수를 이용하여 녹화를 시작할 수 있다. stoprecording은 MediaRecorder 객체의 stop 함수를 통해 녹화를 멈추고 지정한 저장경로에 저장해준다. 마지막으로 release 함수를 이용하여 영상을 다시 녹화할 수 있도록 메모리 해제한다.

### 증강현실
한 달간 Unity의 pose track driver와 3d human body, vufroira, ARFoundation, XRcore를 이용하여 작성했었지만, 해당 여러 기능 중 사용하려는 핵심기능들이 ios의 최신 기기 몇 종에서만 제한적으로 사용할 수 있다는 것을 알고 안드로이드 스튜디오의 ARCore를 이용하여 화면에 AR을 출력해주고 모션 인식에 사용했던 Posenet을 이용하여 관절을 인식하고 해당 객체를 터치하거나 뛰어넘는 방식으로 연습이 가능할 수 있도록 진행 중이다. 

### 딥러닝
 슈팅 연습 등에 사용할 공과 골대의 인식을 위해 각각의 객체를 DeepLearning으로 검출하고자 하였다. 데이터 라벨링 초기에는 Faster-Rcnn을 사용하여 딥러닝을 진행하려 했지만, 축구의 경우 공의 움직임이 매우 빠른 스포츠이기 때문에 인식의 정확도를 약간 희생하더라도 빠르고 지속적인 인식이 필요했다. 이로 인해 초당 처리 속도가 상대적으로 뛰어난 Yolo를 사용하기로 하였다. 추가로 Yolo는 F-Rcnn에 비해 훨씬 적은 False-Positive(목표 객체가 아닌 대상을 목표 객체로 판단하는 오류)를 보여주어 적은 background error가 발생한다는 장점이 있다.
 
![inference](./image/inference.JPG)

Yolo 는 빠른 객체 검출을 위한 딥러닝 기법이다. 이미지를 분할 하지 않고 한 번에 인식하기 때문에, 각 객체에 대한 검출 오류가 상대적으로 적다. 
 전체적인 추론 과정은 다음과 같다.
1. 입력 이미지를 S x S 그리드로 나눈다.
2. 각각 grid cell은 B개의 bounding box와 그에 대한 confidence score를 갖는다. (객체가 없다면 score=0)
3. 각 grid cell은 C개의 conditional class probability를 갖는다.
4. 각 bounding box와 x, y, w, h, confidence로 구성된다.
5. grid cell의 여러 bounding box 중 ground-truth box와 IOU가 가장 높은 box를 predictor로 설정한다.
6. 객체가 존재하는 grid cell을 분류한다.
7. ground-truth box의 중심점이 일부 grid cell 내부에 위치하면 해당 cell에 객체가 있다고 인식한다.

### UI
피그마를 이용하여 모든 디자인 설계를 완성하고 와이어 프레임을 구축하여 메인페이지, 로그인 페이지, 가입하기 페이지, 아이디/비밀번호 찾기 페이지, 영상 페이지, 포스트 페이지, 프로필 페이지, 훈련 페이지, 각 훈련 시작 전 페이지, 설정 페이지, 게시물 관리 페이지, 나의 정보 관리 페이지, 갤러리에서 사진 불러오기, 네비게이션 하단 바, 평가하기 페이지, 프로그램 정보 페이지, 공지사항 페이지 등을 완료하였고 각 페이지 별 여러 버튼들의 이벤트와 intent 설정을 완료하였으면 각 모듈들을 합병할때 각 버튼의 이벤트에 맞게 구축만 하면 완료되는 상황입니다.

## 2.6 시험 / 테스트 결과
### test
 YOLO를 이용한 훈련으로 윈도우의 정지영상에서는 객체 위치추정의 인식율이 꽤 높았으나 훈련된 모델 파일을android 환경에 적합한 파일로 변환하는 과정에서 정확도가 손실되는 상황이 발생하여 여러 방법을 시도한 결과 Opencv 라이브러리를 이용하여 HSV 색상과 허프 변환을 이용한 공 위치 추적방법을 개선하기로 하였다. HSV 색상의 범위를 증대시켜 공의 회전과 공이 멀어질 때 프레임 상 색상 변화에서도 인식 가능케 하였다.
다음, ROI를 이용하였다. 공을 인식하게 되면 추적에 성공한 구역의 각각의 RGB 값을 받아 평균을 내어서 저장한 다음 추적 대상을 놓쳐버린 시점에서 마지막으로 추적 성공한 구역의 상단 3방향을 탐색, 각각 RGB 값을 평균값을 비교하여 허프 변환으로는 추적이 불가능한 고속 이동 물체에 대해서도 보다 정확한 추적이 가능하게 하였다. 객체 위치 추적을 위하여 불필요한 연산이 증가하게 되었고, 이를 줄이기 위해 추적에 이용되는 프레임의 수를 줄여 연산을 줄여 이전보다 효율을 높일 수 있었다.

![test](./image/test.JPG)

### Cord 
![method](./image/method1.JPG)

![method](./image/method2.JPG)

![method](./image/method3.JPG)
![method](./image/method4.JPG)

![method](./image/method5.JPG)
![method](./image/method6.JPG)
![method](./image/method6.JPG)

### Demo
[데모영상](https://youtu.be/4QWJPG0yJGU)
[![Video Label](https://github.com/cch230/balloon/blob/master/KakaoTalk_20200128_193729200.gif)](https://youtu.be/QDb0zTH6a1Q?t=0s) 
![demo](./image/demo.JPG)

# 참고 문헌
*  [앱과 DB연동](https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/java-rds.html)
*  [외부 클라이언트와 DB서버 연결](https://docs.aws.amazon.com/ko_kr/AmazonRDS/latest/UserGuide/USER_VPC.html)
* [딥러닝 라이브러리 언어별 종류](https://aikorea.org/blog/dl-libraries)
* [딥러닝에 대해](http://t-robotics.blogspot.com/2015/05/deep-learning.html#.XfOaHegzaUk)
* [트래킹 코드](https://diy-project.tistory.com/96)
* [공 관련 메소드](https://liveupdate.tistory.com/281)
* [open cv ROI 추출](https://yeolco.tistory.com/57)
* [Openpose라이브러리](https://m.blog.naver.com/worb1605/221297566317)
* [Imgproc.HoughCircles 함수](http://docs.opencv.org/2.4/doc/tutorials/imgproc/imgtrans/hough_circle/hough_circle.html)
* [안드로이드  tensorflow_lite에 대해](https://github.com/tensorflow/examples)
* [최소자승법에 대해](https://m.blog.naver.com/PostView.nhn?blogId=moojigai07&logNo=120186757908&proxyReferer=https%3A%2F%2Fwww.google.com%2F)
* [어플리케이션 와이어프레임 figma](https://www.figma.com)
* [CNN에 대해](https://untitledtblog.tistory.com/150)
* [YOLO에 대해](https://pjreddie.com/darknet/yolo/)
* > Haar-like feature 프로토타입 관련-2011_송자혜,장연진_강원대학_IT대학 전기전자공학부 전기전자공학 졸업논문
* > 세가지 축구 슈팅 동작의 운동학적 비교-1999_한국체육학회지_진영완_동의대학교,최지영,신제민_연세대학교
