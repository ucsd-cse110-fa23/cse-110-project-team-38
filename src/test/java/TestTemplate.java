package test.java;



import org.junit.Test;
import org.junit.Before;
// import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestTemplate {
    @Test
    public void exampleTest(){
        assertEquals(true,1 == 1);
    }

    @Test
    public void terribleTest(){
        assertEquals(true,true);
    }
}
