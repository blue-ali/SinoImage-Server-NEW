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
	private List<String> processingFileIds = new ArrayList<String>();

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

	public List<String> getProcessingFileIds() {
		return processingFileIds;
	}

	public void setProcessingFileIds(List<String> processingFileIds) {
		this.processingFileIds = processingFileIds;
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
		resultInfo.setProcessingFileIds(input.getProcessingFileIdsList());
		return resultInfo;
	}

	public MsgResultInfo toNetMsg() {
		MsgResultInfo.Builder builder = MsgResultInfo.newBuilder();
		builder.setMsg(this.getMsg());
		builder.setStatus(this.getStatus());
		builder.addAllProcessingFileIds(this.processingFileIds);
		return builder.build();
	}

}