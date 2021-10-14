package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.acceptance.OrderTableAcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse orderTableResponse;
    private MenuResponse menuResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTableResponse = OrderTableAcceptanceTest.주문_테이블_등록_되어있음(new OrderTableRequest(3, false)).as(OrderTableResponse.class);

        MenuGroup 추천메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음(new MenuGroupRequest("추천메뉴")).as(MenuGroup.class);

        ProductResponse 양념치킨 = ProductAcceptanceTest.상품_등록되어_있음(new ProductRequest("양념치킨", BigDecimal.valueOf(16000))).as(ProductResponse.class);
        ProductResponse 후라이드치킨 = ProductAcceptanceTest.상품_등록되어_있음(new ProductRequest("후라이드치킨", BigDecimal.valueOf(15000))).as(ProductResponse.class);

        MenuProductRequest 양념치킨_menuProduct = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest 후라이드치킨_menuProduct = new MenuProductRequest(후라이드치킨.getId(), 1);

        menuResponse = MenuAcceptanceTest.메뉴_등록_되어있음("양념+후라이드", BigDecimal.valueOf(31000), 추천메뉴,
                Arrays.asList(양념치킨_menuProduct, 후라이드치킨_menuProduct)).as(MenuResponse.class);
    }

    @DisplayName("주문을 관리한다.")
    @Test
    void manageOrder() {
        // given
        OrderRequest orderRequest = new OrderRequest(orderTableResponse.getId(), Arrays.asList(new OrderLineItemRequest(menuResponse.getId(), 1L)));

        // when
        ExtractableResponse<Response> createResponse = 주문_생성_요청(orderRequest);

        // then
        주문_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 주문_목록_조회_요청();

        // then
        주문_응답됨(findResponse);
        주문_목록_포함됨(findResponse, Arrays.asList(createResponse));

        // when
        orderRequest.setOrderStatus("COMPLETION");
        ExtractableResponse<Response> updateResponse = 주문_상태_변경_요청(createResponse, orderRequest);

        // then
        주문_응답됨(updateResponse);

        // when
        orderRequest.setOrderStatus("MEAL");
        ExtractableResponse<Response> wrongResponse = 주문_상태_변경_요청(createResponse, orderRequest);

        // then
        주문_응답_실패함(wrongResponse);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest orderRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 주문_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_응답됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponses) {
        List<Long> createOrderIds = createResponses.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> findOrderIds = findResponse.jsonPath().getList("id", Long.class);

        assertThat(findOrderIds).containsAll(createOrderIds);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> createResponse, OrderRequest orderRequest) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().put(location+ "/order-status")
                .then().log().all()
                .extract();
    }

    public static void 주문_응답_실패함(ExtractableResponse<Response> wrongResponse) {
        assertThat(wrongResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
