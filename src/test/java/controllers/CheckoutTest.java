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
public class CheckoutTest {

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
    public void testCheckout() throws Exception {
        Gson gson = new Gson();
        String user_id, product_id1, product_id2;

        // checking out without user being created
        {
            this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cart/1000/checkout"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

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

        // checking out without product
        {
            this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cart/" + user_id + "/checkout"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
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

            product_id1 = res.get("id");
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

            product_id2 = res.get("id");
        }

        // adding product to cart
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id1);
            data.put("quantity", "5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        // adding product to cart
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id2);
            data.put("quantity", "5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        // changing product quantity with put
        {
            String name = "prd1";
            String category_name = "cat1";
            String description = "desc1";
            String buy_price = "123.0";
            String sell_price = "234.0";

            HashMap<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("category", category_name);
            data.put("description", description);
            data.put("buy_price", buy_price);
            data.put("sell_price", sell_price);
            data.put("quantity", "2");

            this.mockMvc.perform(MockMvcRequestBuilders.put("/api/products/" + product_id1)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        // checking out with product less in stock
        {
            this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cart/" + user_id + "/checkout"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        // reverting quantity back to original
        {
            String name = "prd1";
            String category_name = "cat1";
            String description = "desc1";
            String buy_price = "123.0";
            String sell_price = "234.0";

            HashMap<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("category", category_name);
            data.put("description", description);
            data.put("buy_price", buy_price);
            data.put("sell_price", sell_price);
            data.put("quantity", "100");

            this.mockMvc.perform(MockMvcRequestBuilders.put("/api/products/" + product_id1)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        // adding product to cart
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id1);
            data.put("quantity", "5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        // adding product to cart
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("product_id", product_id2);
            data.put("quantity", "5");

            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + user_id)
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        // checking out
        {
            this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cart/" + user_id + "/checkout"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        // delete product 1
        {
            this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + product_id1))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        // delete product 2
        {
            this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + product_id2))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}