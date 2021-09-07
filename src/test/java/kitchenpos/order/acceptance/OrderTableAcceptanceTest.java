package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.dto.OrderTableRequest;
import org.springframework.http.MediaType;

public class OrderTableAcceptanceTest {

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest.toOrderTable()) // TODO OrderTable 리팩토링시 OrderTableRequest 넘기도록 수정 필요
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_되어있음(OrderTableRequest orderTableRequest) {
        return 주문_테이블_생성_요청(orderTableRequest);
    }
}
