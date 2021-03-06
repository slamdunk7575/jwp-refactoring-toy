package kitchenpos.menugroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 관리한다.")
    @Test
    void manageMenuGroup() {
        // given
        MenuGroupRequest 추천메뉴 = new MenuGroupRequest("추천메뉴");

        // when
        ExtractableResponse<Response> createResponse = 메뉴_그룹_등록_요청(추천메뉴);

        // then
        메뉴_그룹_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_응답됨(findResponse);
        메뉴_그룹_포함됨(findResponse, Arrays.asList(createResponse));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(MenuGroupRequest menuGroupRequest) {
        return 메뉴_그룹_등록_요청(menuGroupRequest);
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_그룹_응답됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_그룹_포함됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponse) {
        createResponse.forEach(create -> System.out.println(create.headers()));

        List<Long> createMenuGroupIds = createResponse.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        // System.out.println(findResponse.jsonPath().get().toString());
        List<Long> findMenuGroupIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(findMenuGroupIds).containsAll(createMenuGroupIds);
    }
}
