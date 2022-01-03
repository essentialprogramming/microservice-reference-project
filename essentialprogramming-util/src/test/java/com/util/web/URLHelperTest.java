package com.util.web;

import org.junit.jupiter.api.Test;

import java.util.List;

public class URLHelperTest {


    @Test
    void url_should_have_no_path_parameter(){
        String value = "https://github.com";
        int size = URLHelper.getPathParametersFromUrl(value).size();
        assert size == 0;
    }
    @Test
    void url_should_have_1_path_parameter(){
        String value = "https://github.com/essentialprogramming";
        int size = URLHelper.getPathParametersFromUrl(value).size();
        assert size == 1;
    }

    @Test
    void url_should_have_2_path_parameters(){
        String value = "https://storage.googleapis.com/essentialprogramming/book.pdf";
        int size = URLHelper.getPathParametersFromUrl(value).size();
        assert size == 2;
    }

    @Test
    void url_path_contains_a_file_called_book_pdf(){
        String value = "https://storage.googleapis.com/essentialprogramming/book.PDF?GoogleAccessId=dev-assets@essentialprogramming-dev-36262.iam.gserviceaccount.com&Expires=1604102049&Signature=ghgBRtyu5hcZllFjFDUJEdNmA";
        List<String> path = URLHelper.getPathParametersFromUrl(value);
        String file = URLHelper.getFileName(path);
        assert file.equalsIgnoreCase("book.PDF");
    }
}
