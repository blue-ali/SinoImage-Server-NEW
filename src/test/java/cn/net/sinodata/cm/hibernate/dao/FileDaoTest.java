package cn.net.sinodata.cm.hibernate.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import base.BaseDaoTest;
import base.BaseSpringTest;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import junit.framework.Assert;

public class FileDaoTest extends BaseDaoTest{

	@Resource
	FileDao fileDao;
	
	@Test
	public void test(){
		List<FileInfo> list = fileDao.queryAll();
		System.out.println(list.get(0).getFileId());
	}
	
	@Test
	public void testQueryProcessingFileIds(){
		List<String> fileIds = fileDao.queryProcessingFileIds("123");
		Assert.assertTrue(fileIds.size()>0);
	}
}
