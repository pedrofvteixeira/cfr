/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */
package pt.webdetails.cfr.repository;

import java.io.File;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import pt.webdetails.cfr.file.CfrFile;


public class DefaultFileRepositoryTest {
  
  
  public class DefaultFileRepositoryForTests extends DefaultFileRepository {
  
    @Override
    protected String getBasePath() {
      return "./tests";
    }        
  }
  
  @AfterClass
  public static void onTestFinish() {
    File f = new File("." + File.separator + "tests" + File.separator + "my_tests" + File.separator + "anotherLevel");
    if (f.exists()) {
      File[] files = f.listFiles();
   
      for (File fi : files)
        fi.delete();
      f.delete();
    }
    f = new File("." + File.separator + "tests" + File.separator + "my_tests");
    if (f.exists()) {
      File[] files = f.listFiles();    
      for (File fi : files)
        fi.delete();
      f.delete();
      f.getParentFile().delete();
    }
    
    f = new File("." + File.separator + "tests" + File.separator + "list_tests/anotherLevel");
    if (f.exists()) {
      File[] files = f.listFiles();    
      for (File fi : files)
        fi.delete();
      f.delete();
      f = f.getParentFile();
      files = f.listFiles();    
      for (File fi : files)
        fi.delete();
      f.delete();
      
    }
    
    
    
  }
  
  @Test
  public void testFileCreation() {
    DefaultFileRepositoryForTests fileRep = new DefaultFileRepositoryForTests();
    
    byte[] content = new byte[100];
    Assert.assertTrue(fileRep.storeFile(content, "t.txt", "my_tests"));
    
  }
  

  @Test
  public void testFileCreationMultipleLevels() {
    DefaultFileRepositoryForTests fileRep = new DefaultFileRepositoryForTests();
    
    byte[] content = "Hello, World".getBytes();
    Assert.assertTrue(fileRep.storeFile(content, "t.txt", "my_tests/anotherLevel"));
    
  }
  

  @Test
  public void testCreateFileGetFile() {
    DefaultFileRepositoryForTests fileRep = new DefaultFileRepositoryForTests();
    
    byte[] content = "Hello, World".getBytes();
    Assert.assertTrue(fileRep.storeFile(content, "t.txt", "my_tests/anotherLevel"));
    
    
    //Now get
    CfrFile f = fileRep.getFile("my_tests/anotherLevel/t.txt");
    byte[] readContent = f.getContent();
    
    Assert.assertEquals("Hello, World", new String(readContent));        
    Assert.assertEquals( "t.txt", f.getFileName());
    Assert.assertEquals("./tests/my_tests/anotherLevel/", f.getDownloadPath());
  }
  
  
  
  @Test
  public void testGetFileNonExistentFile() {
    DefaultFileRepositoryForTests fileRep = new DefaultFileRepositoryForTests();

    //Now get
    CfrFile f = fileRep.getFile("my_tests/anotherLevel/does_not_exist.txt");
    Assert.assertNull(f);    
  }
  
  
  
  @Test
  public void testListFiles() {
    DefaultFileRepositoryForTests fileRep = new DefaultFileRepositoryForTests();
    
    
    byte[] content = new byte[100];
    Assert.assertTrue(fileRep.storeFile(content, "first.txt", "list_tests"));

    Assert.assertTrue(fileRep.storeFile(content, "second.txt", "list_tests/newLevel"));
    Assert.assertTrue(fileRep.storeFile(content, "third.txt", "list_tests/newLevel"));
    
    
    File[] files = fileRep.listFiles("list_tests");
    
    Assert.assertEquals(2, files.length);
    
    Assert.assertEquals(true,files[0].isFile());
    Assert.assertEquals("first.txt",files[0].getName());
        
    Assert.assertEquals(true,files[1].isDirectory());
    Assert.assertEquals("newLevel",files[1].getName());
        
    files = fileRep.listFiles("list_tests/newLevel");
    Assert.assertEquals(2, files.length);
    Assert.assertEquals(true,files[0].isFile());
    Assert.assertEquals("second.txt",files[0].getName());
    Assert.assertEquals(true,files[1].isFile());
    Assert.assertEquals("third.txt",files[1].getName());
  }
  
  
  
}
