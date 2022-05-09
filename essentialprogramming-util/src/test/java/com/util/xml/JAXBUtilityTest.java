package com.util.xml;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JAXBUtilityTest {

    @Test
    void marshal() {
        final TestPojo test = TestPojo.builder().firstName("John").lastName("Nick").build();
        final String xml = JAXBUtility.marshalObject(test);
        final TestPojo object = JAXBUtility.unmarshal(xml, TestPojo.class);

        assertEquals(test, object);
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @XmlRootElement(name = "client")
    static class TestPojo {
        String firstName;
        String lastName;


    }
}
