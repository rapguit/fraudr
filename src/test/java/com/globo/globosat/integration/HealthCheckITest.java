package com.globo.globosat.integration;

import com.globo.globosat.FraudrApplication;
import com.globo.globosat.repository.NetworkRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by raphael on 22/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FraudrApplication.class)
@WebAppConfiguration
public class HealthCheckITest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private NetworkRepository repo;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void health_check() throws Exception {

        this.mockMvc.perform(get("/health").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("index", responseFields(
                        fieldWithPath("status").description("The service main status."),
                        fieldWithPath("diskSpace").description("Contains the local disk info."),
                        fieldWithPath("diskSpace.status").description("The disk status."),
                        fieldWithPath("diskSpace.total").description("Total disk space."),
                        fieldWithPath("diskSpace.free").description("Free disk space."),
                        fieldWithPath("diskSpace.threshold").description("A disk space threshold. You can configure an alert based in this info.")
                )));
    }
}