package cn.net.sinodata.cm.content;

import java.io.File;
import java.util.List;

import cn.net.sinodata.cm.common.Constants;
import cn.net.sinodata.cm.common.GlobalVars;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.util.DateUtil;
import cn.net.sinodata.cm.util.Util;
import cn.net.sinodata.framework.util.FileUtil;


public abstract class BaseContentService implements IContentService {

	protected abstract List<? extends BaseContent> batchInfo2Content(BatchInfo batchInfo);
	
	/**
	 * 构造保存路径，/SysId/CreateTime/OrgId/BatchId
	 * @param batchInfo
	 * @return
	 */
	protected String buildPath(BatchInfo batchInfo){
		
		StringBuffer sb = new StringBuffer();
		appendPath(sb, GlobalVars.local_root_path);
		appendPath(sb, batchInfo.getSysId());
		appendPath(sb, DateUtil.format(batchInfo.getCreateTime(), GlobalVars.fs_date_format));
		appendPath(sb, batchInfo.getOrgId());
		appendPath(sb, batchInfo.getBatchId());
		return sb.toString();
	}
	
	private void appendPath(StringBuffer sb, String path){
		if(!Util.isStrEmpty(path))
			sb.append(path).append(File.separator);
	}
	
	protected int getBatchCurVersion(BatchInfo batchInfo) {
		String fileName = "";
		int index = 0;
		while (true) {
			index++;
			fileName = buildPath(batchInfo) + index + Constants.PBOPEEXT;// ".pbope";
			if (FileUtil.isFileExists(fileName))
				continue;
			else
				break;
		}
		index--;
		return index;
	}
	
	protected String getBatchNameCurVersion(BatchInfo batchInfo) {
		String fileName = "";
		String scanName = "";
		int index = 0;
		while (true) {
			index++;
			scanName = buildPath(batchInfo) + index + Constants.PBOPEEXT;// ".pbope";
			if (FileUtil.isFileExists(fileName)){
				fileName = scanName;
			}
			else{
				break;
			}
		}
		return fileName;
	}
	
	protected int getBatchNextVersion(BatchInfo batchInfo){
		return getBatchCurVersion(batchInfo) + 1;
	}
	
	protected String getBatchNameNextVersion(BatchInfo batchInfo){
		String fileName = "";
		String scanName = "";
		int index = 0;
		while (true) {
			index++;
			scanName = buildPath(batchInfo) + index + Constants.PBOPEEXT;// ".pbope";
			if (!FileUtil.isFileExists(fileName)){
				fileName = scanName;
				break;
			}
		}
		return fileName;
	}
}
