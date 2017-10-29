package com.ggurgul.playground.extracker.auth.functional.expenses;


import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest;
import com.ggurgul.playground.extracker.auth.models.User;
import com.ggurgul.playground.extracker.auth.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


public class DirectionPasswordGrantFunctionalTest extends AbstractFunctionalTest {

    private static final String VALID_USER = "someone";
    private static final String VALID_USER_PASS = "valid";
    private static final String VALID_CLIENT_ID = "expenses-tracker-service";
    private static final String VALID_CLIENT_SECRET = "expenses-tracker-service-secret";

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        userRepository.save(User.builder().username(VALID_USER).password(VALID_USER_PASS).enabled(true).build());
    }

    @Test
    public void canUseToken() throws Exception {
        final String token = getToken(issueTokenRequest(VALID_USER, VALID_USER_PASS));

        reset();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .get("/me")
                .then()
                .statusCode(200)
                .body("_embedded.expenses", hasSize(equalTo(1)))
                .body("_embedded.expenses[0].name", equalTo("Ps4"));
    }

    private Response issueTokenRequest(String username, String password) throws Exception {
        final HashMap<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", VALID_CLIENT_ID);
        params.put("username", username);
        params.put("password", password);

        return given()
                .contentType("application/x-www-form-urlencoded")
                .params(params)
                .auth()
                .preemptive()
                .basic(VALID_CLIENT_ID, VALID_CLIENT_SECRET)
                .post("/oauth/token");
    }

    private String getToken(Response result) {
        result.then().statusCode(200);
        return result.jsonPath().getString("access_token");
    }

}