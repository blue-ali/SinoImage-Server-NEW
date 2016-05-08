package cn.net.sinodata.cm.hibernate.po;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.protobuf.ByteString;

import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgFileInfo;
import cn.net.sinodata.cm.util.DateFormatUtil;


@Entity
@Table(name = "cm_file_info")
@IdClass(FileInfoKey.class)
public class FileInfo implements Serializable{

	private static final long serialVersionUID = -4456286447538350463L;
	/** 批次号 */
	@Id
	@Column(name = "batchid")
	private String batchId;
	/** 影像ID */
	@Id
	@Column(name = "fileid")
	private String fileId;
	/** 影像名称 */
	@Column(name = "filename")
	private String fileName;
    /** MD5码 */
	@Column(name = "MD5")
    private String fileMd5;
	/** 提交时间 */
	@Column(name="createtime")
    private Date createTime;
	/** 创建者 */
	@Column(name="creator")
    private String creator;
	/** 分类 */
	@Column(name="category")
	private String Category;
	
	/** 文件状态 */
	@Column(name="state")
	private int state;
	
	/** 上一次操作 */
	@Column(name = "last_operation")
	private String lastOperation;
	
	@Transient
    /** 最后修改时间*/
    private String lastModTime;
	@Transient
    /** 版本信息， 用于下载，只能get */
    private int version;
	@Transient
    /** 文件类型 */
    private String mimeType;
    /** 影像大小 */
	@Column(name="filesize")
    private int fileSize;
	@Transient
	/** 文件内容 */
	private byte[] data;
	@Transient
	private String fileUrl;
	@Transient
	private EOperType operation;
	@Transient
	private String invoiceNo="";
	
	@Transient
	private boolean uploaded;

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(String lastModTime) {
		this.lastModTime = lastModTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public EOperType getOperation() {
		return operation;
	}

	public void setOperation(EOperType operation) {
		this.operation = operation;
	}
	
	public boolean isUploaded() {
		return uploaded;
	}

	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}

	public boolean isNullData(){
		return data == null || data.length == 0;
	}
	
	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}
	
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getLastOperation() {
		return lastOperation;
	}

	public void setLastOperation(String lastOperation) {
		this.lastOperation = lastOperation;
	}

	public MsgFileInfo ToPBMsg() throws ParseException
	{
		MsgFileInfo.Builder mBuilder = MsgFileInfo.newBuilder();
		mBuilder.setBatchNO13(this.getBatchId());
		mBuilder.setCreateTime4(DateFormatUtil.formatDate(this.getCreateTime()));
		mBuilder.setAuthor1(this.getCreator());
		mBuilder.setFileMD59(this.getFileMd5());
		mBuilder.setFileName6(this.getFileName());
		mBuilder.setFileNO8(this.getFileId());
		mBuilder.setFileSize10(this.getFileSize());
//		mBuilder.setFileURLBytes(this.getfile)
		mBuilder.setExFaPiaoCode16(this.getInvoiceNo());
		mBuilder.setFileURL7(this.getFileUrl());
		mBuilder.setVersion2(this.getVersion());
		mBuilder.setCategory14(this.getCategory());
		if (this.getData() != null)	{
			mBuilder.setData11(ByteString.copyFrom(this.getData()));
		}
		mBuilder.setOperation12(EOperType.eFROM_SERVER_NOTCHANGE);
		
//		ret.setBatchNOLink(this.getBatchNOLink());
		
		// ret.getNotesList().clear();
//		for (NNoteInfo info : this.getNotesList())
//		{
//			ret.addNotes(info.ToPBMsg());
//		}
		
		return mBuilder.build();
	}
	
	public static FileInfo FromPBMsg(MsgFileInfo input) throws ParseException
	{
		FileInfo fileInfo = new FileInfo();
		fileInfo.setBatchId(input.getBatchNO13());
		//TODO parse date
		fileInfo.setCreateTime(new Date());
		fileInfo.setCreator(input.getAuthor1());
		fileInfo.setFileId(input.getFileNO8());
		fileInfo.setFileMd5(input.getFileMD59());
		fileInfo.setFileName(input.getFileName6());
		fileInfo.setFileSize(input.getFileSize10());
//		fileInfo.setLastModTime(input.get);
		fileInfo.setInvoiceNo(input.getExFaPiaoCode16());
		fileInfo.setOperation(input.getOperation12());
		fileInfo.setVersion(input.getVersion2());
		fileInfo.setCategory(input.getCategory14());
		fileInfo.setFileUrl(input.getFileURL7());
		fileInfo.setLastOperation(input.getOperation12().toString());
		if (input.getData11() != null)
		{
			fileInfo.setData(input.getData11().toByteArray());
		}
		/*ret.getNotesList().clear();
		for (MsgNoteInfo info : input.getNotesList())
		{
			ret.getNotesList().add(NNoteInfo.FromPBMsg(info));
		}*/
		
		return fileInfo;
	}
	
}
