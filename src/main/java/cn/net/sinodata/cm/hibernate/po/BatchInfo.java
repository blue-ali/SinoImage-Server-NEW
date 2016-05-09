package cn.net.sinodata.cm.hibernate.po;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.net.sinodata.cm.pb.ProtoBufInfo.EBatchStatus;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgBatchInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgFileInfo;
import cn.net.sinodata.cm.pb.bean.ResultInfo;
import cn.net.sinodata.cm.util.DateFormatUtil;
import cn.net.sinodata.cm.util.Util;

@Entity
@Table(name = "cm_batch_info")
public class BatchInfo implements Serializable {

	private static final long serialVersionUID = -4489094278284547383L;
	/** 批次号 */
	@Id
	@Column(name = "batchid")
	private String batchId;
	/** 系统编号 */
	@Column(name = "sysid")
	private String sysId;
	/** 机构号 */
	@Column(name = "orgid")
	private String orgId;
	/** 创建时间 */
	@Column(name = "createtime")
	private Date createTime;
	@Column(name = "version")
	private String version;
	/** 最后修改时间 */
	@Column(name = "lastmodified")
	private Date lastModified;
	/** 创建人 */
	@Column(name = "creator")
	private String creator;
	/** 批次处理状态，用于断点续传，未完成提交0， 完成提交1 */
	@Column(name = "state")
	private int state;
	/** 密码 */
	@Transient
	private String password;
	/** 包含的文件 */
	@Transient
	private List<FileInfo> fileInfos = new ArrayList<FileInfo>();
	@Transient
	private EOperType operation = EOperType.eFROM_SERVER_NOTCHANGE;
	
	/** 批次状态 ，断点续传模式下，第一提交批次信息时为NEW，第二次提交批次数据时为PROCESSING*/
	@Transient
	private EBatchStatus status;
	
	/** 返回结果，用于获取批次等操作 */
	@Transient
	private ResultInfo resultInfo;
	
	/** 上一次操作 */
	@Column(name = "last_operation")
	private String lastOperation;

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<FileInfo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileInfo> fileInfos) {
		this.fileInfos = fileInfos;
	}

	public EOperType getOperation() {
		return operation;
	}

	public void setOperation(EOperType operation) {
		this.operation = operation;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getLastOperation() {
		return lastOperation;
	}

	public void setLastOperation(String lastOperation) {
		this.lastOperation = lastOperation;
	}

	public EBatchStatus getStatus() {
		return status;
	}

	public void setStatus(EBatchStatus status) {
		this.status = status;
	}

	public ResultInfo getResultInfo() {
		if(resultInfo == null)
			resultInfo = new ResultInfo();
		return resultInfo;
	}

	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}

	public static BatchInfo fromNetMsg(MsgBatchInfo input) throws ParseException {
		BatchInfo batchInfo = new BatchInfo();
		batchInfo.setBatchId(input.getBatchNO6());
		batchInfo.setCreateTime(DateFormatUtil.parseStringDate(input.getCreateTime4()));
		batchInfo.setCreator(input.getAuthor1());
		batchInfo.setLastModified(new Date()); // TODO 控件现在不传这个字段，用服务端时间
		batchInfo.setOrgId(input.getOrgID10());
		batchInfo.setSysId(input.getBusiSysId11());
		batchInfo.setOperation(input.getOperation8());
		batchInfo.setVersion(String.valueOf(input.getVersion2()));
		batchInfo.setPassword(input.getPassword16());
		batchInfo.setLastOperation(input.getOperation8().toString());
		List<MsgFileInfo> mFileInfos = input.getFileInfos9List();
		if (mFileInfos != null) {
			for (MsgFileInfo mInfo : mFileInfos) {
				FileInfo fileInfo = FileInfo.FromPBMsg(mInfo);
				batchInfo.getFileInfos().add(fileInfo);
			}
		}
		batchInfo.setStatus(input.getStatus());
		return batchInfo;
	}

	public static BatchInfo fromPBFile(String fname) throws Exception {
		FileInputStream input = new FileInputStream(new File(fname));
		MsgBatchInfo msg = MsgBatchInfo.parseFrom(input);
		BatchInfo batchInfo = BatchInfo.fromNetMsg(msg);
		return batchInfo;
	}

	public MsgBatchInfo toNetMsg() throws ParseException {
		MsgBatchInfo.Builder mBuilder = MsgBatchInfo.newBuilder();
		mBuilder.setBatchNO6(this.getBatchId());
		mBuilder.setCreateTime4(DateFormatUtil.formatDate(this.getCreateTime()));
		mBuilder.setAuthor1(this.getCreator());
		// mBuilder.setLastModified(DateUtil.format(this.getLastModified(),
		// GlobalVars.client_date_format));
		// mBuilder.setPassword16(this.getPassword());
		mBuilder.setPassword16("");
		mBuilder.setOrgID10(this.getOrgId());
		mBuilder.setBusiSysId11(this.getSysId());
		mBuilder.setVersion2(Integer.valueOf(this.getVersion()));
		mBuilder.setOperation8(EOperType.eFROM_SERVER_NOTCHANGE);
//		mBuilder.setTransMode(this.getTransMode());
		// mBuilder.setOperation(this.getOperation());
		List<FileInfo> fileInfos = this.getFileInfos();
		if (fileInfos != null) {
			for (FileInfo info : this.fileInfos) {
				// ret.addFileinfos(info.ToPBMsg());
				mBuilder.addFileInfos9(info.ToPBMsg());
			}
			// mBuilder.setFileCount(fileInfos.size());
		} else {
			// ret.setFileCount(0); // 注：这个字段，或者应该清除，或者应该保留，保留的话的作用是作为校验作用
		}
		return mBuilder.build();
	}

	public Boolean isFileDataComplete() {
		for (FileInfo fileinfo : this.getFileInfos()) {
			if (fileinfo.getOperation() == EOperType.eADD || fileinfo.getOperation() == EOperType.eUPD) {
				if (!fileinfo.isNullData()) {
					continue;
				}
				if (fileinfo.isUploaded() != true) {
					return false;
				}
			}
		}
		return true;
	}

	public Boolean updateFileData(FileInfo info) {
		if (info.getBatchId() != this.getBatchId()) {
			return false;
		}
		for (FileInfo fileinfo : this.getFileInfos()) {
			if (fileinfo.getFileName() == info.getFileName()) {
				// TODO,应该减少服务器内存使用，直接把数据落地为文件

				fileinfo.setData(info.getData());
				return true;
			}
		}
		return false;
	}

	public void addFileInfo(FileInfo fileInfo) {
		fileInfos.add(fileInfo);
	}

	public void updateFileState(FileInfo _fileInfo) {
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.getFileName().equals(_fileInfo.getFileName())) {
				fileInfo.setUploaded(true);
			}
		}
	}

	public List<InvoiceInfo> getInvoiceInfos() {
		List<InvoiceInfo> invoiceInfos = new ArrayList<InvoiceInfo>();
		for (FileInfo fileInfo : fileInfos) {
			if (!Util.isStrEmpty(fileInfo.getInvoiceNo())) {
				InvoiceInfo invoiceInfo = new InvoiceInfo();
				invoiceInfo.setBatchId(fileInfo.getBatchId());
				invoiceInfo.setAuthor(this.getCreator());
				invoiceInfo.setCreatetime(this.getCreateTime());
				invoiceInfo.setFileName(fileInfo.getFileName());
				invoiceInfo.setInvoiceNo(fileInfo.getInvoiceNo());
				invoiceInfos.add(invoiceInfo);
			}
		}
		return invoiceInfos;
	}
}
