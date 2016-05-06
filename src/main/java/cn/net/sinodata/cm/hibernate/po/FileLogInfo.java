package cn.net.sinodata.cm.hibernate.po;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.jdbc.BatchingBatcher;

import com.google.protobuf.ByteString;

import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgFileInfo;
import cn.net.sinodata.cm.util.DateFormatUtil;


@Entity
@Table(name = "cm_file_log")
public class FileLogInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2515484923133034420L;
	
	/** 主键 无意义 */
	@Id
	@Column(name = "logid")
	private String logId;
	/** 批次号 */
	@Column(name = "batchid")
	private String batchId;
	/** 文件序号 */
	@Column(name="fileid")
    private String fileId;
	/** 文件序号 */
	@Column(name="filename")
    private String fileName;
    /** 操作时间*/
	@Column(name="opertime")
    private Date operTime;
	/** 操作类型*/
	@Column(name="operation")
    private String operation;
	/** 操作用户*/
	@Column(name="userid")
    private String userId;
	/** 发票号 */
	@Column(name="invoice_no")
    private String invoiceNo;
	

	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getOperTime() {
		return operTime;
	}
	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	public static FileLogInfo fromFileInfo(FileInfo fileInfo){
		FileLogInfo fileLogInfo = new FileLogInfo();
		fileLogInfo.setLogId(UUID.randomUUID().toString());
		fileLogInfo.setBatchId(fileInfo.getBatchId());
		fileLogInfo.setOperation(fileInfo.getOperation().toString());
		fileLogInfo.setOperTime(new Date());
		fileLogInfo.setUserId(fileInfo.getCreator());
		fileLogInfo.setFileId(fileInfo.getFileId());
		fileLogInfo.setFileName(fileInfo.getFileName());
		fileLogInfo.setInvoiceNo(fileInfo.getInvoiceNo());
		return fileLogInfo;
	}
}
