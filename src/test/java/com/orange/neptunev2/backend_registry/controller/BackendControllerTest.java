package com.orange.neptunev2.backend_registry.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.orange.neptunev2.backend_registry.repository.BackendRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@TestPropertySources({
		@TestPropertySource(value = "classpath:application-test.properties"),
		@TestPropertySource(value = "classpath:application.properties")
})
public class BackendControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private BackendRepository repository;

	static JSONObject json;

	@BeforeAll
	void setUp() throws Exception {
		repository.deleteAll();
		json = null;
	}

	@AfterAll
	void tearDown() throws Exception {
		repository.deleteAll();
		json = null;
	}

	@Test
	@Order(value = 0)
	public void testThatCanCreateBackend() throws Exception {
		MvcResult result = this.mvc.perform(
				MockMvcRequestBuilders
						.post("/v1/rest/backends")
						.content("{"
								+ 	"\"name\": \"idbase\","
								+ 	"\"scheme\": \"http\","
								+ 	"\"host\": \"idbase.orange.ci\","
								+ 	"\"port\": 6000,"
								+ 	"\"credential\": {"
								+ 		"\"type\": \"basic\","
								+ 		"\"value\": \"6f938090f99d11e982140242ac140003\""
								+ 	"},"
								+ 	"\"enabled\": true"
								+ "}"
						)
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", matchesPattern("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}")))
		.andExpect(jsonPath("$.name", is("idbase")))
		.andExpect(jsonPath("$.scheme", is("http")))
		.andExpect(jsonPath("$.host", is("idbase.orange.ci")))
		.andExpect(jsonPath("$.port", is(6000)))
		.andExpect(jsonPath("$.credential.type", is("basic")))
		.andExpect(jsonPath("$.credential.value", is("6f938090f99d11e982140242ac140003")))
		.andExpect(jsonPath("$.enabled", is(true)))
		/*.andExpect(jsonPath("$.createdAt", matchesPattern("(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9+-:]+)")))
		.andExpect(jsonPath("$.updatedAt", matchesPattern("(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9+-:]+)")))*/
		.andReturn()
		;

		// Keep the previous object created for future tests
		json = new JSONObject(result.getResponse().getContentAsString());
	}

	@Test
	@Order(value = 1)
	public void testThatCanCreateBackendWithoutSchemePortAndCredentialAndEnabled() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders
						.post("/v1/rest/backends")
						.content("{"
								+ 	"\"name\": \"exemple\","
								+ 	"\"host\": \"exemple.orange.ci\""
								+ "}"
						)
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", matchesPattern("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}")))
		.andExpect(jsonPath("$.name", is("exemple")))
		.andExpect(jsonPath("$.scheme", is("http")))
		.andExpect(jsonPath("$.host", is("exemple.orange.ci")))
		.andExpect(jsonPath("$.port", is(80)))
		.andExpect(jsonPath("$.credential").doesNotHaveJsonPath())
		.andExpect(jsonPath("$.enabled", is(true)))
		/*.andExpect(jsonPath("$.createdAt", matchesPattern("(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9+-:]+)")))
		.andExpect(jsonPath("$.updatedAt", matchesPattern("(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9+-:]+)")))*/
		;
	}

	@Test
	@Order(value = 2)
	public void testThatCannotCreateBackendWithNameWhichAlreadyExist() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders
						.post("/v1/rest/backends")
						.content("{"
								+ 	"\"name\": \"idbase\","
								+ 	"\"host\": \"idbases.orange.ci\""
								+ "}"
						)
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.status").exists())
		.andExpect(jsonPath("$.errors").exists())
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.errors").isArray())
		.andExpect(jsonPath("$.errors", hasSize(1)))
		;
	}

	@Test
	@Order(value = 3)
	public void testThatCannotCreateBackendWithHostWhichAlreadyExist() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders
						.post("/v1/rest/backends")
						.content("{"
								+ 	"\"host\": \"exemple.orange.ci\""
								+ "}"
						)
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.status").exists())
		.andExpect(jsonPath("$.errors").exists())
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.errors").isArray())
		.andExpect(jsonPath("$.errors", hasSize(2)))
		;
	}

	@Test
	@Order(value = 4)
	public void testThatCannotCreateBackendWithNameAndHostWhichAlreadyExist() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders
						.post("/v1/rest/backends")
						.content("{"
								+ 	"\"name\": \"idbase\","
								+ 	"\"host\": \"exemple.orange.ci\""
								+ "}"
						)
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.status").exists())
		.andExpect(jsonPath("$.errors").exists())
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.errors").isArray())
		.andExpect(jsonPath("$.errors", hasSize(2)))
		;
	}

	@Test
	@Order(value = 5)
	public void testThatCannotCreateBackendWithoutGivenName() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders
						.post("/v1/rest/backends")
						.content("{"
								+ "\"host\": \"idbases.orange.ci\""
								+ "}")
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.status").exists())
		.andExpect(jsonPath("$.errors").exists())
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.errors").isArray())
		.andExpect(jsonPath("$.errors", hasSize(1)))
		;
	}

	@Test
	@Order(value = 6)
	public void testThatCannotCreateBackendWithoutGivenHost() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders
						.post("/v1/rest/backends")
						.content("{"
								+ 	"\"name\": \"idbases\""
								+ "}"
						)
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.status").exists())
		.andExpect(jsonPath("$.errors").exists())
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.errors").isArray())
		.andExpect(jsonPath("$.errors", hasSize(1)))
		;
	}

	@Test
	@Order(value = 7)
	public void testThatCanReadBackend() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
						.get("/v1/rest/backends/" + json.getString("id"))
		)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(json.getString("id"))))
		.andExpect(jsonPath("$.name", is("idbase")))
		.andExpect(jsonPath("$.scheme", is("http")))
		.andExpect(jsonPath("$.host", is("idbase.orange.ci")))
		.andExpect(jsonPath("$.port", is(6000)))
		.andExpect(jsonPath("$.credential.type", is("basic")))
		.andExpect(jsonPath("$.credential.value", is("6f938090f99d11e982140242ac140003")))
		.andExpect(jsonPath("$.enabled", is(true)))
		.andExpect(jsonPath("$.createdAt").exists())
		.andExpect(jsonPath("$.updatedAt").exists())
//		.andExpect(jsonPath("$.createdAt", is(json.getString("createdAt"))))
//		.andExpect(jsonPath("$.updatedAt", is(json.getString("updatedAt"))))
		;
	}

	@Test
	@Order(value = 8)
	public void testThatCannotReadBackendWithNotExistId() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
						.get("/v1/rest/backends/861476d8-72de-4de2-9805-24987aeac2c7")
		)
		.andDo(print())
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.status", is(404)))
		.andExpect(jsonPath("$.errors").isArray())
		.andExpect(jsonPath("$.errors", hasSize(1)))
		;
	}

	@Test
	@Order(value = 9)
	public void testThatCanUpdateAllBackendFields() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
						.put("/v1/rest/backends/" + json.getString("id"))
						.content("{"
								+ 	"\"name\": \"idbase1\","
								+ 	"\"scheme\": \"https\","
								+ 	"\"host\": \"idbase1.orange.ci\","
								+ 	"\"port\": 6001,"
								+ 	"\"credential\": {"
								+ 		"\"type\": \"digest\","
								+ 		"\"value\": \"214026f938090f99d11e9842ac140003\""
								+ 	"},"
								+ 	"\"enabled\": false"
								+ "}"
						)
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(json.getString("id"))))
		.andExpect(jsonPath("$.name", is("idbase1")))
		.andExpect(jsonPath("$.scheme", is("https")))
		.andExpect(jsonPath("$.host", is("idbase1.orange.ci")))
		.andExpect(jsonPath("$.port", is(6001)))
		.andExpect(jsonPath("$.credential.type", is("digest")))
		.andExpect(jsonPath("$.credential.value", is("214026f938090f99d11e9842ac140003")))
		.andExpect(jsonPath("$.enabled", is(false)))
		.andExpect(jsonPath("$.createdAt").exists())
		.andExpect(jsonPath("$.updatedAt").exists())
//		.andExpect(jsonPath("$.createdAt", is(json.getString("createdAt"))))
//		.andExpect(jsonPath("$.updatedAt", is(json.getString("updatedAt"))))
		;
	}

	@Test
	@Order(value = 10)
	public void testThatCanUpdatePartialBackendFields() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
						.put("/v1/rest/backends/" + json.getString("id"))
						.content("{\"enabled\": true}")
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(json.getString("id"))))
		.andExpect(jsonPath("$.name", is("idbase1")))
		.andExpect(jsonPath("$.scheme", is("https")))
		.andExpect(jsonPath("$.host", is("idbase1.orange.ci")))
		.andExpect(jsonPath("$.port", is(6001)))
		.andExpect(jsonPath("$.credential.type", is("digest")))
		.andExpect(jsonPath("$.credential.value", is("214026f938090f99d11e9842ac140003")))
		.andExpect(jsonPath("$.enabled", is(true)))
//		.andExpect(jsonPath("$.createdAt", is(json.getString("createdAt"))))
		.andExpect(jsonPath("$.updatedAt", not(json.getString("updatedAt"))))
		;

		//assertThat(LocalDateTime.parse(json.getString("updatedAt")).isBefore(LocalDateTime.parse(json.getString("updatedAt")))).isTrue();
	}

	@Test
	@Order(value = 11)
	public void testThatCanListBackend() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
						.get("/v1/rest/backends")
		)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.page", is(1)))
		.andExpect(jsonPath("$.itemPerPage", is(10)))
		.andExpect(jsonPath("$.itemOffset", is(0)))
		.andExpect(jsonPath("$.fullyItems", is(1)))
		.andExpect(jsonPath("$.pageNumber", is(1)))
		.andExpect(jsonPath("$.items", hasSize(1)))
		.andExpect(jsonPath("$.items[0].id", is(json.getString("id"))))
		.andExpect(jsonPath("$.items[0].name", is("idbase1")))
		.andExpect(jsonPath("$.items[0].scheme", is("https")))
		.andExpect(jsonPath("$.items[0].host", is("idbase1.orange.ci")))
		.andExpect(jsonPath("$.items[0].port", is(6001)))
		.andExpect(jsonPath("$.items[0].enabled", is(true)))
		.andExpect(jsonPath("$.items[0].createdAt").exists())
		.andExpect(jsonPath("$.items[0].updatedAt").exists())
		.andExpect(jsonPath("$.items[0].credential").doesNotExist())
		;
		
		mvc.perform(
				MockMvcRequestBuilders
						.get("/v1/rest/backends?details")
		)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.page", is(1)))
		.andExpect(jsonPath("$.itemPerPage", is(10)))
		.andExpect(jsonPath("$.itemOffset", is(0)))
		.andExpect(jsonPath("$.fullyItems", is(1)))
		.andExpect(jsonPath("$.pageNumber", is(1)))
		.andExpect(jsonPath("$.items", hasSize(1)))
		.andExpect(jsonPath("$.items[0].id", is(json.getString("id"))))
		.andExpect(jsonPath("$.items[0].name", is("idbase1")))
		.andExpect(jsonPath("$.items[0].scheme", is("https")))
		.andExpect(jsonPath("$.items[0].host", is("idbase1.orange.ci")))
		.andExpect(jsonPath("$.items[0].port", is(6001)))
		.andExpect(jsonPath("$.items[0].enabled", is(true)))
		.andExpect(jsonPath("$.items[0].createdAt").exists())
		.andExpect(jsonPath("$.items[0].updatedAt").exists())
		.andExpect(jsonPath("$.items[0].credential").exists())
		.andExpect(jsonPath("$.items[0].credential.type", is("digest")))
		.andExpect(jsonPath("$.items[0].credential.value", is("214026f938090f99d11e9842ac140003")))
		;
	}


	@Test
	@Order(value = 12)
	public void testThatCanDeleteBackend() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
						.delete("/v1/rest/backends/" + json.getString("id"))
		)
		.andExpect(status().isNoContent());
	}

	@Test
	@Order(value = 13)
	public void testThatCannotReadBackendDeleted() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
						.get("/v1/rest/backends/" + json.getString("id"))
		)
		.andDo(print())
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.status", is(404)))
		.andExpect(jsonPath("$.errors").isArray())
		.andExpect(jsonPath("$.errors", hasSize(1)))
		;
	}

	@Test
	@Order(value = 14)
	public void testThatCannotCreateBackendWithBadFieldvalue() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders
						.post("/v1/rest/backends")
						.content("{"
								+ 	"\"name\": \"idbase\","
								+ 	"\"scheme\": \"httpss\","
								+ 	"\"host\": \"idbase.orange.ci\","
								+ 	"\"port\": 6001,"
								+ 	"\"credential\": {"
								+ 		"\"type\": \"digests\","
								+ 		"\"value\": \"214026f938090f99d11e9842ac140003\""
								+ 	"},"
								+ 	"\"enabled\": false"
								+ "}"
						)
						.contentType(MediaType.APPLICATION_JSON)
		)
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.errors").isArray())
		.andExpect(jsonPath("$.errors", hasSize(1)))
		;
	}
}
