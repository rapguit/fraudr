package com.globo.globosat.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globo.globosat.FraudrApplication;
import com.globo.globosat.controller.dto.CollisionDTO;
import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.model.Node;
import com.globo.globosat.repository.NetworkRepository;
import org.junit.After;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by raphael on 22/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FraudrApplication.class)
@WebAppConfiguration
public class FraudrControllerITest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NetworkRepository repo;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @After
    public void tearDown() throws Exception {
        repo.init();
    }

    @Test
    public void list_networks() throws Exception {
        createSampleNetwork();

        this.mockMvc.perform(get("/fraudr/network").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("list-networks", responseFields(
                        fieldWithPath("[].id").description("The network's id."),
                        fieldWithPath("[].collisions").description("The network's collisions."),
                        fieldWithPath("[].collisions[].left").description("The collision's left node."),
                        fieldWithPath("[].collisions[].right").description("The collision's right node."),
                        fieldWithPath("[].collisions[].left.id").description("The nodes's left id."),
                        fieldWithPath("[].collisions[].right.id").description("The nodes's right id.")
                )));
    }

    @Test
    public void find_a_network() throws Exception {
        Network sampleNetwork = createSampleNetwork();

        this.mockMvc.perform(get("/fraudr/network/{id}", sampleNetwork.getId()).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("find-network",
                        pathParameters(
                            parameterWithName("id").description("The requested network id.")
                        ),
                        responseFields(
                                fieldWithPath("id").description("The network's id."),
                                fieldWithPath("collisions").description("The network's collisions."),
                                fieldWithPath("collisions[].left").description("The collision's left node."),
                                fieldWithPath("collisions[].right").description("The collision's right node."),
                                fieldWithPath("collisions[].left.id").description("The nodes's left id."),
                                fieldWithPath("collisions[].right.id").description("The nodes's right id.")
                        )
                ));
    }

    @Test
    public void add_a_collision() throws Exception {
        createSampleNetwork();
        CollisionDTO collision = new CollisionDTO("B", "C");

        this.mockMvc.perform(
                post("/fraudr/network")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(collision)))
                .andExpect(status().isCreated())
                .andDo(document("add-collision",
                        requestFields(
                                fieldWithPath("leftNodeId").description("The collision's left node id."),
                                fieldWithPath("rightNodeId").description("The collision's right node id.")
                        ),
                        responseHeaders(headerWithName(LOCATION).description("The path of new resource created."))
                ));
    }

    @Test
    public void check_a_collision() throws Exception {
        createSampleNetwork();
        CollisionDTO collision = new CollisionDTO("A", "B");

        this.mockMvc.perform(
                post("/fraudr/network/collision")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(collision)))
                .andExpect(status().isOk())
                .andDo(document("check-collision",
                        requestFields(
                                fieldWithPath("leftNodeId").description("The collision's left node id."),
                                fieldWithPath("rightNodeId").description("The collision's right node id.")
                        ),
                        responseFields(
                                fieldWithPath("belongsToNetwork").description("Assertive that's represents if the specified collision is present in a same network."),
                                fieldWithPath("networkId").description("Network's id that specified collision is nested.")
                        )
                ));
    }

    private Network createSampleNetwork() {
        List<Collision> collisions = new ArrayList<>();
        collisions.add(new Collision(new Node("A"), new Node("B")));
        return repo.save(new Network(collisions));
    }

}