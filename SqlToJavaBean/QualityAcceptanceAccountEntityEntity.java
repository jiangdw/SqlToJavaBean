@Id
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Column(name = "id", unique = true, nullable = false, length= 32)
private String id;

@Column(name = "acceptance")
private String acceptance;

@Column(name = "acceptance_part")
private String acceptancePart;

@Column(name = "acceptance_phase")
private int acceptancePhase;

@Column(name = "check_batch_id")
private String checkBatchId;

@Column(name = "code")
private String code;

@Column(name = "division_and_acceptance_basis")
private String divisionAndAcceptanceBasis;

@Column(name = "inspection_batch_name")
private String inspectionBatchName;

@Column(name = "name")
private String name;

@Column(name = "node_name")
private String nodeName;

@Column(name = "project_partitioning_id")
private String projectPartitioningId;

@Column(name = "project_partitioning_leaf_name")
private String projectPartitioningLeafName;

@Column(name = "project_partitioning_parent_id")
private String projectPartitioningParentId;

@Column(name = "project_subitem_id")
private String projectSubitemId;

@Column(name = "project_subitem_parent_id")
private String projectSubitemParentId;

@Column(name = "responsible")
private String responsible;

@Column(name = "row_number")
private int rowNumber;

@Column(name = "subitem")
private String subitem;

@Column(name = "subitem_project")
private String subitemProject;

@Column(name = "unit_project")
private String unitProject;

@Column(name = "unit_project_name")
private String unitProjectName;

@Column(name = "unit_project_name_id")
private String unitProjectNameId;

@Column(name = "check_batch_parent_num")
private int checkBatchParentNum;

}