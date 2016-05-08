package cn.net.sinodata.cm.hibernate.po;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "cm_batch_log")
public class BatchLogInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7023038900836754920L;
	
	/** 主键 无意义 */
	@Id
	@Column(name = "logid")
	private String logId;
	/** 批次号 */
	@Column(name = "batchid")
	private String batchId;
	/** 提交时间 */
	@Column(name="createtime")
    private Date createTime;
    /** 操作时间*/
	@Column(name="opertime")
    private Date operTime;
	/** 操作类型*/
	@Column(name="operation")
    private String operation;
	/** 操作用户*/
	@Column(name="userid")
    private String userId;
	/** 操作机构*/
	@Column(name="orgid")
    private String orgId;
	

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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public static BatchLogInfo fromBatchInfo(BatchInfo batchInfo){
		BatchLogInfo batchLogInfo = new BatchLogInfo();
		batchLogInfo.setLogId(UUID.randomUUID().toString());
		batchLogInfo.setBatchId(batchInfo.getBatchId());
		batchLogInfo.setCreateTime(batchInfo.getCreateTime());
		batchLogInfo.setOperation(batchInfo.getOperation().toString());
		batchLogInfo.setOperTime(new Date());
		batchLogInfo.setOrgId(batchInfo.getOrgId());
		batchLogInfo.setUserId(batchInfo.getCreator());
		return batchLogInfo;
	}
}
