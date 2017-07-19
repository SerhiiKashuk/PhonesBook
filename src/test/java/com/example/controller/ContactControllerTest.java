package com.example.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collection;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import com.example.model.Contact;
import com.example.service.ContactService;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ContactController.class)
@AutoConfigureMockMvc(secure = false)
public class ContactControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@MockBean
	private ContactService contactServiceMock;

	Contact contact1;

	@Before
	public void setUpContact() throws Exception {

		contact1 = new Contact();
		contact1.setId(1);
		contact1.setSurname("Ivanov");
		contact1.setName("Oleg");
		contact1.setPatronymic("Petrovich");
		contact1.setMobile("+38(098)1234567");
		contact1.setHomephone("3235678");
		contact1.setAddress("Kyiv,st.Jenn");
		contact1.setEmail("q@mail.com");
		contact1.setUser_id(1);

	}

	@Test
	public void testList() throws Exception {
		assertThat(this.contactServiceMock).isNotNull();
		mockMvc.perform(MockMvcRequestBuilders.get("/contacts"))
		        .andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(view().name("contacts"))
				.andExpect(MockMvcResultMatchers.view().name("contacts"))
				.andExpect(content().string(Matchers.containsString("Spring Framework ")))
				.andDo(print());
	}

	@Test
	public void testShowContact() throws Exception {
		assertThat(this.contactServiceMock).isNotNull();
		when(contactServiceMock.getContactById(1)).thenReturn(contact1);

		MvcResult result = mockMvc.perform(get("/product/{id}/", 1)).andExpect(status().isOk())
				.andExpect(view().name("contactshow"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
				.andExpect(model().attribute("contact", hasProperty("id", is(1))))
				.andExpect(model().attribute("contact", hasProperty("surname", is("Ivanov"))))
				.andExpect(model().attribute("contact", hasProperty("name", is("Oleg"))))
				.andExpect(model().attribute("contact", hasProperty("patronymic", is("Petrovich"))))
				.andExpect(model().attribute("contact", hasProperty("mobile", is("+38(098)1234567"))))
				.andExpect(model().attribute("contact", hasProperty("homephone", is("3235678"))))
				.andExpect(model().attribute("contact", hasProperty("address", is("Kyiv,st.Jenn"))))
				.andExpect(model().attribute("contact", hasProperty("email", is("q@mail.com"))))
				.andExpect(model().attribute("contact", hasProperty("user_id", is(1)))).andReturn();

		MockHttpServletResponse mockResponse = result.getResponse();
		assertThat(mockResponse.getContentType()).isEqualTo("text/html;charset=UTF-8");

		Collection<String> responseHeaders = mockResponse.getHeaderNames();
		assertNotNull(responseHeaders);
		assertEquals(1, responseHeaders.size());
		assertEquals("Check for Content-Type header", "Content-Type", responseHeaders.iterator().next());
		String responseAsString = mockResponse.getContentAsString();
		assertTrue(responseAsString.contains("Spring Framework "));

		verify(contactServiceMock, times(1)).getContactById(1);
	}
}