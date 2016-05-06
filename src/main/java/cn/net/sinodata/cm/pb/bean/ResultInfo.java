package cn.net.sinodata.cm.pb.bean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EResultStatus;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgFileInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgResultInfo;


public class ResultInfo {
	
	private String msg = "";
	private EResultStatus status = EResultStatus.eSuccess;
	private List<FileInfo> fileInfos = new ArrayList<FileInfo>();

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public EResultStatus getStatus() {
		return status;
	}

	public void setStatus(EResultStatus status) {
		this.status = status;
	}
	
	public List<FileInfo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileInfo> fileInfos) {
		this.fileInfos = fileInfos;
	}

	public static ResultInfo fromPBBytes(byte[] bytes) throws Exception {
		MsgResultInfo mResultInfo = MsgResultInfo.parseFrom(bytes);
		ResultInfo resultInfo = ResultInfo.fromNetMsg(mResultInfo);
		return resultInfo;
	}
	
	public static ResultInfo fromNetMsg(MsgResultInfo input) throws ParseException {
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setMsg(input.getMsg());
		resultInfo.setStatus(input.getStatus());
		List<MsgFileInfo> msgFileInfos = input.getFileInfosList();
		for (MsgFileInfo msgFileInfo : msgFileInfos) {
			resultInfo.fileInfos.add(FileInfo.FromPBMsg(msgFileInfo));
		}
		return resultInfo;
	}
	//////////////////////////////////////////////////
	public MsgResultInfo toNetMsg() {
		MsgResultInfo.Builder builder = MsgResultInfo.newBuilder();
		builder.setMsg(this.getMsg());
		builder.setStatus(this.getStatus());
		List<FileInfo> fileInfos = this.getFileInfos();
		for (FileInfo fileInfo : fileInfos) {
			try {
				builder.addFileInfos(fileInfo.ToPBMsg());
			} catch (ParseException e) {
				builder.setStatus(EResultStatus.eFailed);
				builder.setMsg(e.getMessage());
				e.printStackTrace();
			}
		}
		return builder.build();
	}

}