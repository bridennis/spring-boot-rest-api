package restaurant.vote.system.rest;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private RequestPostProcessor requestPostProcessor;

    @Before
    public void setup() {
        requestPostProcessor = httpBasic("admin", "admin");
    }

	@Test
	public void restaurantsMenusShouldReturnStatusOk() throws Exception {
       mockMvc.perform(get("/restaurants/menus"))
               .andExpect(status().isOk());
	}

    @Test
    public void restaurantsVotesResultsShouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/restaurants/votes/results"))
                .andExpect(status().isOk());
    }

    @Test
    public void usersReqIsCompletelySuccessful() throws Exception {

        Map<String, String> data = new HashMap<>();
        data.put("login", "test");
        data.put("password", "password");

        mockMvc.perform(post("/users/reg")
                .contentType(MediaType.APPLICATION_JSON)
                .content((new JSONObject(data)).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.login").value("test"));
    }

    @Test
    public void userRoleHasNoAccessToUsersData() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void adminRoleHasAccessToUsersData() throws Exception {
        mockMvc.perform(get("/users")
                .with(requestPostProcessor))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
