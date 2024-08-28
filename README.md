# 1. 기획서

### 📌 프로젝트 개요

함께하는 여행을 위한 공금 관리 및 결제 솔루션

<br>

### 📌 프로젝트 배경/근거

- 최근 해외 여행 수요 급증
- 여행 중 공금 관리의 어려움
  - 모든 인원이 잔액을 알기 어려움
  - 결제를 하려면 총무를 통해 결제할 수 밖에 없음
  - 내역을 투명하게 관리하지 못 하는 경우가 있음
- 여행시 공금 관리의 어려움을 해결하고자 기획

<br>

### 📌 주요 기능

- **공금 모으기:** 여행을 함께하는 친구들을 초대해 여행 비용을 모을 수 있는 기능

- **투명한 자금 관리:** 공금의 입출금 내역을 모든 참여자가 실시간으로 확인 가능
- **가계부:** 일 별 사용 금액 조회, 카테고리 별 사용 금액 조회
- **공금 결제 및 이체:** 여행 중 발생하는 비용을 공금에서 바로 결제
- **환전 기능:** 공금 통장을 통해 환전을 편하게 할 수 있는 기능

<br>

### 📌 프로젝트를 통한 기대효과

- **여행 중 공금 관리의 편리성 증대**
  - 여러 명이 함께 여행을 할 때 발생하는 공금 관리의 번거로움을 줄여줌
  - 모든 참여자가 실시간으로 공금 상태를 확인하고 관리할 수 있어, 여행 중 불필요한 금융 스트레스를 줄일 수 있음
- **투명한 자금 사용으로 신뢰도 향상**
  - 공금의 입출금 내역이 모두에게 투명하게 공개되기 때문에, 자금 사용에 대한 불신이나 오해를 방지할 수 있음
  - 여행 참여자 간의 신뢰를 높이는 효과
- **편리한 예산 관리**
  - 가계부를 통해 일별 사용 금액을 한 눈에 확인할 수 있다.
  - 카테고리 별 사용 금액 확인을 통해 사용 현황을 한 눈에 볼 수 있다.
- **사용자 경험 향상**
  - 사용자 친화적인 인터페이스와 간편한 QR 결제 기능을 통해 사용자의 편리함을 크게 향상시킬 수 있음
- **확장성**
  - 숙소, 항공권, 교통 등 다양한 플랫폼과 연동될 수 있음
  - 향후 여행 보험 연계, 각종 할인 혜택 제공 등 다양한 추가 기능을 통해 서비스 확장 가능

<br>
<br>

# 2. 요구사항 정의서

### 1. 회원

1. 회원 가입 (0순위)

   - 사용자는 회원가입을 진행한 후 서비스를 이용할 수 있다.
   - 회원가입 시 아이디(이메일), 비밀번호, 이름, 전화번호, 생년월일을 입력받는다.
   - 아이디는 중복을 허용하지 않는다.
   - 회원가입 시 본인 인증을 진행해야 하며, 전화번호로 본인 인증을 진행할 수 있다.

2. 로그인 (0순위)

   - 사용자는 아이디(이메일), 비밀번호를 사용하여 로그인을 진행할 수 있다.

3. 로그아웃 (1순위)

   - 사용자는 로그아웃을 진행할 수 있다.

4. 회원 탈퇴 (5순위)

   - 사용자는 회원탈퇴를 진행할 수 있다.
   - 회원 탈퇴시 비밀번호를 재입력하여 비밀번호가 일치할 시 탈퇴가 가능하다.

5. 비밀번호 찾기 (5순위)
   - 사용자는 가입한 이메일로 비밀번호 찾기 링크를 전달받을 수 있다.

### 2. 전체 통장

1. 계좌 목록 조회 (0순위)
   - 사용자는 자신의 계좌 목록을 조회할 수 있다.
   - 전체 계좌를 조회할 시 각 계좌의 계좌종류, 계좌번호, 계좌 잔액이 표시된다.

### 3. 개인 통장

1. 계좌 개설 (2순위)

   - 사용자는 개인 입출금통장을 개설할 수 있다.
   - 개인 통장은 N개까지 만들 수 있다.
   - 계좌 개설시 계좌의 비밀번호를 등록해야 한다.

2. 특정 계좌 조회 (2순위)

   - 사용자는 자신의 특정 계좌를 조회할 수 있다.
   - 특정 1개의 계좌를 조회할 시 계좌종류, 계좌번호, 계좌 잔액, 계좌 내역이 표시된다.

3. 계좌 이체 (3순위)

   - 사용자는 타행 계좌로 이체가 가능하다.
   - 이체시 비밀번호를 입력하여 해당 계좌의 비밀번호와 일치할 경우에만 이체가 가능하다.

4. 계좌 해지 (5순위)

   - 사용자는 자신의 계좌를 해지할 수 있다.
   - 계좌에 잔액이 남아있는 경우 계좌 해지가 불가능하다. (타 계좌로 잔액 이체 후 해지 가능)

5. 메인 통장 선택 기능 (5순위)
   - 선택하지 않을 시 가장 오래 된 통장으로 자동 선택 된다.
   - 자신이 가지고 있는 계좌 중 메인 계좌를 선택할 수 있다.

### 4. 여행통장 (한국돈)

1. 통장 생성 (0순위)

   - 여행통장은 관리자(유저)와 사용자(유저)로 나뉜다.
   - 여행통장에 참가하거나 개설하려는 자는 계좌가 없다면 계좌를 개설해야 한다.
   - 여행통장을 개설한 유저가 관리자의 권한을 부여받는다.
   - 여행통장 개설 시 여행통장 비밀번호, 여행통장 별명 입력한다.
   - 여행통장 관리자는 통장 수정(이름 변경), 삭제 및 사용자 초대 권한을 부여받는다.
   - 여행통장 관리자가 통장 수정, 삭제 및 사용자 초대를 할 경우 대상 사용자에게 알림 or 메일 or 참여코드를 보낸다.
   - 계좌는 N개까지 만들 수 있다.

2. 통장 조회 (0순위)
   - 여행통장에 참여한 모든 유저는 여행통장을 조회할 수 있다.
   - 여행통장에서 조회 가능한 정보
     - 여행통장 관리자
     - 여행통장 별명
     - 여행통장 계좌번호
     - 여행통장 잔액
     - 여행통장 입출금내역
3. 통장 수정 (3순위)

   - 여행통장 관리자는 여행통장의 별명을 수정할 수 있다.

4. N빵 나누기 기능 (3순위)

   - 메인 계좌로 자동으로 들어간다 (선택을 안 하면 제일 처음에 만든게 메인)

5. 통장 삭제 (4순위)

   - 여행통장 관리자는 여행통장의 잔액이 0원이면 여행통장을 해지할 수 있다.

6. 회비 설정 (2순위)

   - 여행통장 관리자가 목표일, 목표금액을 설정한다.

7. 회비 수정 (2순위)
   - 여행통장 관리자가 목표일, 목표금액을 수정할 수 있다.

### 5. 여행통장 (외국돈)

1. 통장 생성 (0순위)

   - 여행통장(한국돈)에서 여행통장(외국돈) 통장 생성 기능을 제공한다.
   - 사용할 화폐 종류를 고른다.
   - 여행통장(한국돈) 별로 하나의 여행통장(외국돈)만 생성할 수 있다.
   - 여행통장(한국돈) 개설 시 입력된 정보를 기반으로 개설된다.

2. 통장 조회 (2순위)

   - 여행통장(한국돈)과 동일

3. 통장 수정 (2순위)

   - 여행통장(한국돈)과 동일

4. 통장 삭제 (5순위)
   - 여행통장 관리자는 여행통장의 잔액이 0원이면 여행통장을 해지할 수 있다.
   - 여행통장(외국돈)의 잔액이 0원이 아닌데 해지를 하고 싶을 경우 여행통장(한국돈)으로 전액 환전하도록 권장한다.

### 6. 환전

1. 환전 기능 (2순위)

   - 여행통장 관리자는 여행통장(한국돈)에서 여행통장(외국돈), 혹은 반대로 환전을 할 수 있다.
   - 여행통장 관리자가 환전을 시도할 경우 환전될 통장이 존재해야 한다.
   - (?) 환전이 가능한 계좌는 여행통장과 관련이 있는 계좌여야 한다.
   - 여행통장 관리자가 환전을 시도할 경우 환전 후 금액, 잔액 및 환전 수수료를 고지한다.
   - 환율 및 환전은 시중은행의 환전 방식과 동일 or 유사하게 구현한다.
   - 환전시 예상 금액을 보여준다.

2. 환율 조회 (4순위)
   - 메인 페이지 or 환전 페이지에서 실시간 환율에 대한 표 or 차트를 제공한다.
   - 특정 기간 대비 현재 환율이 싼지 비싼지 알려준다.

### 7. 결제 및 이체

1. 한도 설정 (5순위)

   - 관리자는 결제 및 이체가 가능한 금액의 한도를 설정할 수 있다.
   - 일일, 주간, 월간 한도를 설정 가능
   - 한도를 수정할 수 있는 기능

2. 결제 (1순위)

   - 여행 통장에서 자동으로 나간다.
   - 여행 통장 내의 카메라를 켜서 QR 인식을 한다.
   - 가게의 QR을 통해서 결제정보를 확인
   - 확인 후에 결제를 진행
   - 결제 결과를 확인할 수 있다.
   - 실패, 완료, 영수증 등

3. 이체 (2순위)

   - 사용자는 이체할 계좌를 선택한다. (한국통장만)
   - 비밀번호를 이체 전 물어본다.
   - 사용자는 이체 관련 정보(이체 금액, 사용자 이름, 계좌 번호, 은행 이름 등)를 통해 이체를 진행한다.
   - 올바른 계좌번호인지 검증한다.
   - 이체가 불가능한 이체 관련 정보는 진행하지 못하도록 한다.

4. 승인 (1순위)

   - 사용자는 결제 및 이체를 진행하기 전에 승인을 진행한다.
   - 보안을 강화하기 위해 생체 인식, 비밀번호 등의 인증을 통해 진행한다.

5. 알림 (3순위)

   - 결제 및 이체가 완료되었음을 알리는 알림
   - 한도 관련한 알림을 제공하는 기능
   - 결제 및 이체 승인, 결제 실패, 결제 취소, 한도 초과 등

6. 결제 취소 (5순위)

   - 사용자는 결제 완료 후, 결제 취소를 원할 경우 취소를 진행한다.
   - 사용자가 결제 취소를 요청할 수 있다.
   - 취소 요청 후 환불 절차 및 소요 시간을 안내한다.

7. 내역 조회 (1순위)
   - 사용자에게 결제 및 이체의 모든 거래 내역을 조회할 수 있는 기능
   - 결제, 이체, 결제 취소 등
   - 필터링 기능(입금, 출금, 날짜별) 을 통해 편리한 조회 기능
   - 각 거래의 상세 정보를 확인할 수 있는 기능

### 8. 가계부

1. 일별 사용 금액 조회 (2순위)

   - 일별 사용금액을 한 눈에 볼 수있다.

2. 사용 내역 분류 (3순위)

   - 사용 내역을 카테고리별로 분류하여 볼 수 있다. (교통비, 식비, 등등)
