package kitchenpos.ordertable.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 관리한다.")
    @Test
    void manageOrderTable() {
        // when
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(orderTableRequest);

        // then
        주문_테이블_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_응답됨(findResponse);
        주문_테이블_목록_포함됨(findResponse, Arrays.asList(createResponse));

        // when
        orderTableRequest.setEmpty(false);
        ExtractableResponse<Response> emptyResponse = 주문_테이블_주문상태_변경_요청(createResponse, orderTableRequest);

        // then
        주문_테이블_응답됨(emptyResponse);
        주문_테이블_주문상태_변경됨(emptyResponse);

        // when
        int changeNumberOfGuests = 10;
        orderTableRequest.setNumberOfGuests(changeNumberOfGuests);
        ExtractableResponse<Response> guestNumberResponse = 주문_테이블_손님수_변경_요청(createResponse, orderTableRequest);

        // then
        주문_테이블_응답됨(guestNumberResponse);
        주문_테이블_손님수_변경됨(changeNumberOfGuests, guestNumberResponse);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_주문상태_변경_요청(ExtractableResponse<Response> createResponse, OrderTableRequest orderTableRequest) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put(location + "/empty")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_손님수_변경_요청(ExtractableResponse<Response> createResponse, OrderTableRequest orderTableRequest) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put(location + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_되어있음(OrderTableRequest orderTableRequest) {
        return 주문_테이블_생성_요청(orderTableRequest);
    }

    private void 주문_테이블_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_테이블_응답됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문_테이블_목록_포함됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedProductIds = createResponses.stream()
                .map(response -> Long.parseLong(response.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> actualProductIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(actualProductIds).containsAll(expectedProductIds);
    }

    private void 주문_테이블_주문상태_변경됨(ExtractableResponse<Response> emptyResponse) {
        OrderTableResponse response = emptyResponse.as(OrderTableResponse.class);
        assertThat(response.isEmpty()).isFalse();
    }

    private void 주문_테이블_손님수_변경됨(int expectedGuestNumber, ExtractableResponse<Response> guestNumberResponse) {
        OrderTableResponse response = guestNumberResponse.as(OrderTableResponse.class);
        assertThat(response.getNumberOfGuests()).isEqualTo(expectedGuestNumber);
    }
}
