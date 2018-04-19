/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partio.controller;

import static org.apache.http.client.methods.RequestBuilder.post;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import partio.repository.ScoutRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScoutControllerTest {
    

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private ScoutRepository scoutRepo;
    
    private MockMvc mockMvc;
    //private TestHelperJson helper;
    private String idTokenString;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();        
        //helper = new TestHelperJson();
        idTokenString = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjU0MjViYjg0NjE2ZWJmOTczYWU4MGJjNjJhYzY4OGQyYTcyNzE1YWQifQ.eyJhenAiOiI3MzYwMTI0MDczLThmMWJxNG11bDQxNWhyM2tkbTE1NHZxM2M2NWxwMzZkLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiNzM2MDEyNDA3My04ZjFicTRtdWw0MTVocjNrZG0xNTR2cTNjNjVscDM2ZC5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwNjc5MjgzMzU2MzM5ODQyMDA2MCIsImVtYWlsIjoibWlua2EudGFodmFuYWluZW5AZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJTSnI4U18xMUVJQXQxMDNzdnQ4SElnIiwiZXhwIjoxNTI0MTM2NjkyLCJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwianRpIjoiYjI3YWE5ZTI4YTBmMDEwMGE1OWUyMDI2M2RkYTdmOWRiZGIzMDQxMiIsImlhdCI6MTUyNDEzMzA5MiwibmFtZSI6Ik1pbmthIFRhaHZhbmFpbmVuIiwicGljdHVyZSI6Imh0dHBzOi8vbGg2Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tcFd2bHNFV2thWTQvQUFBQUFBQUFBQUkvQUFBQUFBQUFBSGcvcUxzcTZPcF8yTzQvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6Ik1pbmthIiwiZmFtaWx5X25hbWUiOiJUYWh2YW5haW5lbiIsImxvY2FsZSI6ImZpIn0.PaVnVnqiu1m9dGw93ktvYW_RmypEHrrMnTGxnYxONsq5srF0ml1iTiw3VlrPiQDG1MT6R2qrazZ0zhzBJDTW6pGQxefGaD6gY5FpGrVZz2vza9TYJMRyaxS2kZ_nNlStK3APIyKoa7_md3PKPfo_oDDnnZ7lbxe-rvJSMNc-RvAmf86STLqrKe7z1ms9dbjdQ3ROmp8HHmqHFH8vTm-6dRjHNj6Z9qewTyyY9Zm5SN22RN0JhcuSsL22uYybs3OevdKIw-ZtyCR2R9xfZnZgo_sJoO1WBs5ovsI6WBkho4cl_R-g7q1wz5JnHrov4Xg76o7WBGQTMoWDP6qp7zDB6A";
    }//if test start fail regenerate new idTokenString.
    
    @After
    public void clean() {
        scoutRepo.deleteAll();
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/scout").header("idTokenString", idTokenString))
                .andExpect(status().isOk());
    }
    
}