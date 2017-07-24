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
public class OrderControllerTest {

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
    public void testOrder() throws Exception {
        Gson gson = new Gson();
        String user_id, product_id;


        // creating new user
        {
            String name = "testUser";
            String email = "testEmail";

            HashMap<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("email", email);

            String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String, String> res = gson.fromJson(result, type);

            user_id = res.get("id");
        }

        // creating new Product
        {
            String name = "prd1";
            String category_name = "cat1";
            String description = "desc1";
            String buy_price = "123.0";
            String sell_price = "234.0";
            String quantity = "100";

            HashMap<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("category", category_name);
            data.put("description", description);
            data.put("buy_price", buy_price);
            data.put("sell_price", sell_price);
            data.put("quantity", quantity);

            String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String, String> res = gson.fromJson(result, type);

            product_id = res.get("id");
        }

        // adding product to cart without user
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id);
            data.put("quantity", "5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/1000")
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        // adding product to cart
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id);
            data.put("quantity", "5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        // adding product to cart again
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id);
            data.put("quantity", "5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        // adding non existing product to cart
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", "12345");
            data.put("quantity", "5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        // adding negative quantity
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id);
            data.put("quantity", "-5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        // adding product to cart with quantity > stock
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id);
            data.put("quantity", "5000000");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

    }
}