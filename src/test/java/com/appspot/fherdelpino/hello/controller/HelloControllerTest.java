package com.appspot.fherdelpino.hello.controller;

import com.appspot.fherdelpino.security.configuration.WebSecurityConfig;
import com.appspot.fherdelpino.security.filter.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = HelloController.class, excludeFilters = {
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class),
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
})
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void greetingDefaultShouldReturnMessageFromService() throws Exception {
        this.mockMvc.perform(get("/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello world!")));
    }

    @Test
    public void greetingWithNameShouldReturnMessageFromService() throws Exception {
        this.mockMvc.perform(get("/hello?name=Fernando"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello Fernando!")));
    }
}
