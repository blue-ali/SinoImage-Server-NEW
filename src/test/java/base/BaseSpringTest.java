package base;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 */

/**
 * @author manan
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/resources/spring/applicationContext-base.xml")
public class BaseSpringTest extends AbstractJUnit4SpringContextTests{
	
	@BeforeClass
	public static void setUpBeforeClass(){
		
	}
	
	@Test
	public void test(){
		
	}

}
