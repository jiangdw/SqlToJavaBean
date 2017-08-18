@Entity
@Table(name = "ss_quy_insplot_acpt_rectifi_record")
@Display("检验批验收整改记录")
public class SsQuyInsplotAcptRectifiRecordEntity extends SuperSubEntity{

@Id
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Column(name = "id", unique = true, nullable = false, length= 32)
private String id;

@Display("编码")
@Column(name = "code")
private String code;

@Column(name = "name")
private String name;

@Display("主表id")
@Column(name = "pid")
private String pid;

@Display("整改次数")
@Column(name = "rectification_times")
private int rectificationTimes;

@Display("整改状态")
@Column(name = "rectification_state")
private int rectificationState;

@Display("整改时间")
@Column(name = "rectification_date")
private Date rectificationDate;

@Display("复查时间")
@Column(name = "review_date")
private Date reviewDate;

}