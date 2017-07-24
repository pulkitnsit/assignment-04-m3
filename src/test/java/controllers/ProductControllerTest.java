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
public class ProductControllerTest {

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
        String name = "iphone";
        String description = "Phone";
        String category = "cat1";
        Integer productQuantity = -10;
        Double buyPrice = 5.99;
        Double sellPrice = 8.99;

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("description", description);
        data.put("quantity", productQuantity);
        data.put("buy_price", buyPrice);
        data.put("sell_price", sellPrice);
        data.put("category", category);

        Gson gson = new Gson();

        //creating new product with negative quantity
        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        productQuantity = 10;
        data.put("quantity", productQuantity);

        //creating new product with negative quantity
        result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> res = gson.fromJson(result, type);

        String id = res.get("id");

        // getting product
        result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        type = new TypeToken<HashMap<String, HashMap<String, String>>>(){}.getType();
        HashMap<String, HashMap<String, String>> getRes = gson.fromJson(result, type);

        assertEquals(name, getRes.get("data").get("name"));
        assertEquals(description, getRes.get("data").get("description"));
        assertEquals(String.valueOf(productQuantity), getRes.get("data").get("quantity"));
        assertEquals(String.valueOf(buyPrice), getRes.get("data").get("buy_price"));
        assertEquals(String.valueOf(sellPrice), getRes.get("data").get("sell_price"));

        // getting all products
        result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // deleting product
        result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // getting product after delete
        result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        // deleting deleted product
        result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        // deleting non existing product
        result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1000"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        out.print(result);
        out.flush();
    }
}