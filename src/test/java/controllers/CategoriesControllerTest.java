package controllers;

import org.junit.Test;
import org.junit.Before;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.io.FileOutputStream;
import org.junit.runner.RunWith;
import com.codenation.Application;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes= Application.class)
public class CategoriesControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private PrintWriter out;

    @Before
    public void setup() throws Exception {
        out = new PrintWriter(new FileOutputStream(new File("out.txt"), true));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testUser() throws Exception {
        String description = "catDes1";

        HashMap<String, Object> data = new HashMap<>();
        data.put("description", description);

        Gson gson = new Gson();

        //creating new categories
        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> res = gson.fromJson(result, type);

        out.print(result);
        out.flush();
    }
}