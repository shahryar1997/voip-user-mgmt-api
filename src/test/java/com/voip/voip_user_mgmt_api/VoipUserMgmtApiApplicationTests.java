package com.voip.voip_user_mgmt_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
class VoipUserMgmtApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void Should_Add_User() throws Exception{
		mockMvc.perform(post("/api/user/save")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"name\":\"John Doe\",\"extension\":\"1234567890\"}"))
		.andExpect(status().isOk());

		mockMvc.perform(get("/api/user/all"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	@Test
	void Should_Get_All_Users() throws Exception{
		mockMvc.perform(get("/api/user/all"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	@Test
	void Should_Get_User_By_Id() throws Exception{
		mockMvc.perform(get("/api/user/by-id?id=1"))
		.andExpect(status().isOk());
	}
	@Test
	void Should_Update_User() throws Exception{
		mockMvc.perform(put("/api/user/update/1")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"name\":\"John Doe khan\",\"extension\":\"1234567890\"}"))
		.andExpect(status().isOk());
	}
	@Test
	void Should_Delete_User() throws Exception{
		mockMvc.perform(delete("/api/user/delete/1"))
		.andExpect(status().isOk());
	}
	@Test
	void contextLoads() {
	}

}
