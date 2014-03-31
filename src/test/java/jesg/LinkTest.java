package jesg;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByPartialLinkText;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(Parameterized.class)
public class LinkTest {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Pattern COMMA = Pattern.compile(",");
    
    @Parameter(0)
    public String url;
    
    @Parameter(1)
    public String linkText;
    
    @Parameter(2)
    public String expectedTitle;
    
    @Parameters(name="{index}: link[{1}] -> title[{2}]")
    public static Collection<String[]> data() throws IOException {
        List<String> lines = FileUtils.readLines(new File("links.csv"));
        List<String[]> csvData = new LinkedList<String[]>();
        for(String line : lines){
            csvData.add(COMMA.split(line));
        }
        return csvData;
    }

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void testLink() {
        driver.get(url);
        driver.findElement(By.partialLinkText(linkText)).click();
        wait.until(ExpectedConditions.titleIs(expectedTitle));
        assertEquals(expectedTitle, driver.getTitle());
    }

}
