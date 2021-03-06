# 키친 포스

## 요구사항 정리

### 상품 (Product)

* 상품을 생성할 수 있다.
    * 상품 가격은 0원 이상이어야 한다.
    * 등록된 상품의 아이디를 포함한 정보를 반환한다.
    

* 상품 목록을 조회할 수 있다.

____

### 메뉴 (Menu)

* 메뉴를 생성할 수 있다.
    * 메뉴의 가격은 0원 이상이어야 한다.
    * 메뉴 그룹(MenuGroup)이 등록되어 있어야 한다.
    * 메뉴 상품(MenuProduct)들이 등록되어 있어야 한다.
    * 메뉴 가격이 메뉴에 속한 상품 가격의 합보다 크지 않아야 한다.
    

* 메뉴 목록을 조회할 수 있다.

____

### 메뉴 그룹 (Menu Group)

* 메뉴 그룹을 생성할 수 있다.
    * 등록된 메뉴 그룹의 아이디를 포함한 정보를 반환한다.
    

* 메뉴 그룹 목록을 조회할 수 있다.

____

### 주문 (Order)

* 주문을 생성할 수 있다.
    * 주문 항목(OrderLineItem)은 필수 정보이다.
    * 주문 항목의 모든 메뉴가 미리 등록되어 있어야 한다.
    * 주문 테이블(OrderTable)은 등록되어 있어야 한다.
    * 주문 테이블 상태가 `비어있음`인 경우 생성할 수 없다.
    * 주문시 상태는 `조리중(COOKING)`으로 생성된다.
    * 등록된 주문의 아이디를 포함한 정보를 반환한다.
    

* 주문 목록을 조회할 수 있다.
  

* 주문 상태를 변경할 수 있다.
    * 주문은 미리 등록되어 있어야 한다.
    * 주문 상태가 `완료(COMPLITION)`인 경우 변경할 수 없다.
    * 변경된 주문 정보를 반환한다.
    
____

### 주문 테이블 (Order Table)

* 주문 테이블을 생성할 수 있다.
    * 등록된 주문 테이블의 아이디를 포함한 정보를 반환한다.
    

* 주문 테이블의 목록을 조회할 수 있다.
  

* 주문 테이블의 주문 등록 상태를 변경할 수 있다.
    * 단체 지정(TableGroup)이 되어 있으면 상태를 변경할 수 없다.
    * 주문 상태가 `조리중(COOKING)` 이거나 `식사(MEAL)` 일때는 변경할 수 없다.
    * 변경된 주문 테이블 정보를 반환한다.
    

* 주문 테이블에 방문한 손님 수를 등록한다.
    * 방문한 손님 수는 0명 미만으로 입력할 수 없다.
    * 주문 테이블 상태가 `비어있음`인 경우 등록할 수 없다.
    * 변경된 주문 테이블 정보를 반환한다.
    
____

#### 단체 지정 (Table Group)

* 단체 지정을 생성할 수 있다.
    * 주문 테이블(OrderTable)이 미리 등록되어 있어야 한다.
    * 주문 테이블이 2개 이상이어야 한다.
    * 주문 테이블 상태가 `비어있음`이 아니거나 `단체 지정`이 되어 있으면 등록할 수 없다.
    * 단체로 지정된 테이블은 상태가 `비어있지 않음`으로 변경된다.
    * 지정된 단체의 아이디를 포함한 정보를 반환한다.
    

* 단체 지정을 해제할 수 있다.
    * 주문 테이블이 `조리중(COOKING)` 이거나 `식사중(MEAL)` 일때는 단체 지정을 해제할 수 없다.
    * 단체 지정이 해제된 테이블은 상태가 `비어있음`으로 변경된다.
    
____



## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |
