package lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import cn.net.sinodata.cm.hibernate.po.FileInfo;

public class MapTest {

	public static void main(String[] args) {
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		FileInfo fileInfo1 = new FileInfo();
		fileInfo1.setFileId("1");
		FileInfo fileInfo2 = new FileInfo();
		fileInfo2.setFileId("2");
		fileInfos.add(fileInfo1);
		fileInfos.add(fileInfo2);
		
		
		List<String> list = fileInfos.stream().map(x -> x.getFileId()).collect(Collectors.toList());
		list.forEach(System.out::println);
	}
}
