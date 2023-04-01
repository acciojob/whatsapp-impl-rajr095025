package com.driver.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.driver.*;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCases {

    @Test
    public void testAddGroup(){
        WhatsappController whatsappController = new WhatsappController();
        List<User> list = new ArrayList<>();
        list.add(new User("rocky","8825933760"));
        list.add(new User("rakesh","8825933767"));
        list.add(new User("raj","882593376das 7"));

        Group group = whatsappController.createGroup(list);
        /*
        if(Objects.nonNull(group)){
            assertEquals("Group 1",group.getName());
        }
        else{
            assertEquals("Group 1",null);
        }
        */
        List<User> list2 = new ArrayList<>();
        list.add(new User("rocky","8825933760"));
        list.add(new User("rakesh","8825933767"));
        list.add(new User("raj","88259337"));
        Group group2 = whatsappController.createGroup(list);
        if(Objects.nonNull(group2)){
            assertEquals("Group 2",group2.getName());
        }
        else{
            assertEquals("Group 2",null);
        }



    }
}
