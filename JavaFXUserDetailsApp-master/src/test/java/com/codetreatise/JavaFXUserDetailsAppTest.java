package com.codetreatise;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

public class JavaFXUserDetailsAppTest extends ApplicationTest {

		@Before
	    public void setUp() throws Exception {	     
	     ApplicationTest.launch(Main.class);
	       
	    }
	    
	    @Override
	    public void start(Stage stage) throws Exception{	    	       
	    	stage.show();
	    }

	    @After
	    public void afterTest() throws java.util.concurrent.TimeoutException{
	    	FxToolkit.hideStage();
	    	release(new KeyCode[]{});
	    	release(new MouseButton[]{});
	    }
	    
	    public <T extends Node> T find(final String q){
	    	return (T) lookup(q).queryAll().iterator().next();
	    }
	    
	  @Test
      public void firstTest() {		  
		  write("a");
	    type(KeyCode.TAB);
	      write("a");
	      clickOn("#btnLogin");
	      sleep(2000);	    
          //rest of code to test
      }
	
}
