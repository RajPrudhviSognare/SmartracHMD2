package com.experis.smartrac_HMD2;

//API CALLS
public class Constants {

    public static String PREFERENCE_NAME = "MtracDemoAttendanceTrackingPreference";
    //public static String TAG_ATTENDANCE_TYPE = "In Time";

    public static boolean GEOFENCE_ENABLED = false;
    public static String ASSOCIATE_ID = "";
    public static String ASSOCIATE_CODE = "";
    public static String TL_ID = "";
    public static String OUTLET_ID = "";
    public static String ATTENDANCE_TYPE = "";
    public static String ATTENDANCE_IMAGE = "";
    public static String CURRENT_LAT = "0.0";
    public static String CURRENT_LONG = "0.0";
    //public static String ATTENDANCE_DATE = "";
    //public static String ATTENDANCE_DATE = "yyyy-mm-dd";
    public static String ATTENDANCE_DATE = "0000-00-00";
    public static String REASON = "";
    public static String LEAVE_TYPE = "";
    public static String REMARKS = "";
    public static String DISTANCE = "0";

    public static String UNIV_LAT = "0.0";
    public static String UNIV_LONG = "0.0";
    public static String UNIV_LAT1 = "0.0";
    public static String UNIV_LONG1 = "0.0";
    public static String UNIV_RADIUS = "500.0";

    public static boolean FROM_PUSH = false;
    public static String PUSH_TYPE = "";
    public static String PUSH_FOR = "";

    ///////////////////////////////////

    //Local Demo Server (Common Demo)
	/*public static String BASE_URL = "http://10.194.5.12/projects10/smartrac_demo/index.php/api";
	public static String BASE_URL_CLIENT_LOGO = "http://10.194.5.12/projects10/smartrac_demo/uploads/client_logo/";
	public static String BASE_URL_ATTENDANCE_LOGO = "http://10.194.5.12/projects10/smartrac_demo/uploads/locations/";*/

	/*public static String BASE_URL = "http://10.194.5.12/projects10/smartrac_demo/index.php/api";
	public static String BASE_URL_CLIENT_LOGO = "http://10.194.5.12/projects10/smartrac_demo/uploads/client_logo/";
	public static String BASE_URL_ATTENDANCE_LOGO = "http://10.194.5.12/projects10/smartrac_demo/uploads/locations/";*/

    public static String BASE_URL = "http://smtrac.pairserver.com/smartrac_hmd2/api/";
    public static String BASE_URL_CLIENT_LOGO = "http://smtrac.pairserver.com/smartrac_hmd2/uploads/client_logo/";
    public static String BASE_URL_ATTENDANCE_LOGO = "http://smtrac.pairserver.com/smartrac_hmd2/uploads/locations/";

	/*public static String BASE_URL = "http://www.experisindia.com/temp/smartrac_demo/api";
	public static String BASE_URL_CLIENT_LOGO = "http://www.experisindia.com/temp/smartrac_demo/uploads/client_logo/";
	public static String BASE_URL_ATTENDANCE_LOGO = "http://www.experisindia.com/temp/smartrac_demo/uploads/locations/";*/

    public static String LOGIN_RELATIVE_URI = "/api/login";
    public static String FORGOT_PASSWORD_RELATIVE_URI = "/api/forgot_password";
    public static String CHANGE_PASSWORD_RELATIVE_URI = "/api/change_password";
    public static String ATTENDANCE_RELATIVE_URI = "/attendanceapi/attendanceEntry";
    public static String ATTENDANCE_APPROVAL_RELATIVE_URI = "/attendanceapi/getAttendanceByTl";
    public static String ATTENDANCE_APPROVALREJECTION_RELATIVE_URI = "/attendanceapi/setApprovedAttendance";
    public static String SALES_TRACKING_GETPRODUCT_BYIMEI_RELATIVE_URI = "/sales_tracking/getProductDetails";
    public static String SALES_TRACKING_SET_SELLINFO_RELATIVE_URI = "/sales_tracking/setSalesInfo";
    public static String ASSOCIATE_REPORTS_ATTENDANCE_RELATIVE_URI = "/report/monthly_attendance";
    public static String ASSOCIATE_REPORTS_TARGETACHIEVEMENT_RELATIVE_URI = "/report/target_vs_achievements";
    public static String ASSOCIATE_REPORTS_ATTENDANCE_MONTHLY_RELATIVE_URI = "/report/getDetailsmonthlyReport";
    public static String GETCOMPETITORLIST_RELATIVE_URI = "/sales_tracking/getCompetitorList";
    public static String SETCOMPETITORSALES_RELATIVE_URI = "/sales_tracking/setCompetitorSales";
    public static String GETCATEGORY_RELATIVE_URI = "/stock/getCategory";
    public static String GETSUBCATEGORY_RELATIVE_URI = "/stock/getSubCategory";
    public static String GETOUTLET_RELATIVE_URI = "/sales_tracking/getOutlet";
    public static String GETPRODUCT_RELATIVE_URI = "/sales_tracking/getProductByOutlet";
    public static String GETPRODUCT_INFO_RELATIVE_URI = "/stock/getproductDtls";
    public static String SETPRODUCT_IMEI_RELATIVE_URI = "/stock/insProductStock";
    public static String GETASSOCIATE_BY_TEAMLEAD_RELATIVE_URI = "/target/getAssociateByTl";
    public static String GETPRODUCTTARGET_BY_ASSOCIATE_RELATIVE_URI = "/target/getProductsTargetBYAssociate";
    public static String SETPRODUCTTARGET_BY_ASSOCIATE_RELATIVE_URI = "/target/setProductsTargetBYAssociate";
    public static String SENDMESSAGE_BY_TL_RELATIVE_URI = "/message/setMessage";
    public static String GETMESSAGE_BY_ASSOCIATE_RELATIVE_URI = "/message/getMessageByAssociate";
    public static String GETSENTMESSAGE_BY_TL_RELATIVE_URI = "/message/getSendMessageByTl";
    public static String LEAVESTATUS_RELATIVE_URI = "/api/check_leave";
    public static String GET_ATTENDANCE_COUNT_DASHBOARD_RELATIVE_URI = "/report/getAttendanceCountDashboard";
    public static String GET_DISPLAY_COUNTER_DETAILS_RELATIVE_URI = "/display_counter/diplay_counter_show";
    public static String SET_DISPLAY_COUNTER_DETAILS_RELATIVE_URI = "/display_counter/display_counter_set";
    public static String SET_KYC_DETAILS_RELATIVE_URI = "/profile/insProfile";

    public static String GET_ASSET_STOCK_BY_ASSET_MANAGER_RELATIVE_URI = "/asset/getAssetAndStockByAssetmanager";
    public static String SET_ASSET_STOCK_BY_ASSET_MANAGER_RELATIVE_URI = "/asset/setAssetByAssetManager";
    public static String DISTRIBUTE_TO_USER_RELATIVE_URI = "/asset/distributeToSubUser";
    public static String SET_DISTRIBUTE_TO_USER_RELATIVE_URI = "/asset/setDistributeToSubUser";
    public static String RECEIVE_ASSETS_FROM_USER_RELATIVE_URI = "/asset/receiveFromSubUser";
    public static String SET_RECEIVE_ASSETS_FROM_USER_RELATIVE_URI = "/asset/setReceiveFromSubUser";
    public static String SET_RECEIVE_ASSETS_FROM_USER_RELATIVE_URI1 = "/asset/setReceiveFromSubUserAssociate";

    public static String VALIDATE_UNIFORM_FOR_ASSOCIATE_RELATIVE_URI = "/asset/validateUniform";
    public static String SET_DISTRIBUTE_TO_ASSOCIATE_RELATIVE_URI = "/asset/setDistributeToAssociate";
    public static String SENDMESSAGE_CREATE_GROUP_URI = "/message/create_group";
    public static String SENDMESSAGE_EXISTING_GROUPLIST_URI = "/target/getAssociatesByGroup";
    public static String SENDMESSAGE_EDIT_GROUP_URI = "/message/update_group";
    public static String SENDMESSAGE_DELETE_GROUP_URI = "/message/delete_group";
    public static String SENDMESSAGE_GROUP_DETAILS_URI = "/target/get_group_details";

    public static String ASSOCIATE_LIST_TL_RELATIVE_URI = "/report/associateList_tl";
    public static String ASSOCIATE_TL_REPORT_ATTENDANCE_DATE_RELATIVE_URI = "/report/getDetailsmonthlyReport_tl";
    public static String GET_POLICY_DOCUMENTS_URI = "/documents/get";


    public static String SAVE_INPUT = "tracking/save_input";
    public static String SALES_REPORT = "sales/record_sale";


    public static String CLIENT_ID = "5783";
    public static String base_url_default = String.format("http://outbound.manpoweronline.in/ess/ess.asmx?wsdl");
    public static final String soapRequestHeader = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">";

    /////////////////////////////////


}