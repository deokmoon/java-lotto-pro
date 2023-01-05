package user.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import user.application.UserService;

@ExtendWith(MockitoExtension.class)
class UserMockControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @DisplayName("회원 가입 성공")
    @Test
    void signUpSuccess() throws Exception {
        // given
        SignUpRequest request = signUpRequest();
        UserResponse response = userResponse();

        doReturn(response).when(userService)
                .signUp(any(SignUpRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("email", response.getEmail()).exists())
                .andExpect(jsonPath("pw", response.getPw()).exists())
                .andExpect(jsonPath("role", response.getRole()).exists());
    }


    private SignUpRequest signUpRequest() {
        return SignUpRequest.builder()
                .email("test@test.test")
                .pw("test")
                .build();
    }

    private UserResponse userResponse() {
        return UserResponse.builder()
                .email("test@test.test")
                .pw("test")
                .role(UserRole.ROLE_USER)
                .build();
    }
}
