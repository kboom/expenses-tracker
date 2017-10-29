package com.ggurgul.playground.extracker.auth;

import com.ggurgul.playground.extracker.auth.models.User;
import com.ggurgul.playground.extracker.auth.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * This scenarios test if the user/password authorization works correctly.
 * For most apps, or any app with "social" login, like ours, you need the "authorization code" grant
 * and that means you need a browser (or a client that behaves like a browser) to handle redirects and cookies
 * and render the user interfaces from the external providers
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = { "classpath:application-test.yml" })
@ActiveProfiles("dev")
public class DirectPasswordGrantTests {

    private static final String VALID_USER = "someone";
    private static final String VALID_USER_PASS = "valid";

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();

        userRepository.save(User.builder().username(VALID_USER).password(VALID_USER_PASS).enabled(true).build());
    }

    @After
    public void teardown() {
        userRepository.deleteAll();
    }

    @Test
    public void redirectedToLoginPageIfNoTokenPresent() throws Exception {
        mockMvc.perform(get("/me")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void unauthorizedIfMissingUser() throws Exception {
        issueTokenRequest("missing", "anything").andExpect(status().isUnauthorized());
    }

    @Test
    public void canBeSuccessfulIfEverythingValid() throws Exception {
        final String accessToken = getToken(issueTokenRequest(VALID_USER, VALID_USER_PASS));
        mockMvc.perform(get("/me")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    private ResultActions issueTokenRequest(String username, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("username", username);
        params.add("password", password);

        return mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(clientId, clientSecret)));
    }

    private String getToken(ResultActions result) throws UnsupportedEncodingException {
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

}