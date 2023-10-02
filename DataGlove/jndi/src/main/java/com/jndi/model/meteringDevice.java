package com.jndi.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class meteringDevice {
	@Id
	public String id;
	public String name;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*public int ACCOUNT_ID;//               NOT NULL NUMBER             
	public int CLIENT_CO_ID; //            NOT NULL NUMBER             
	public String CLIENT_CO_ACCOUNT_ID; //             VARCHAR2(30 CHAR)  
	public String CUSTOMER_NAME_1; //                    VARCHAR2(200 CHAR) 
	public String CUSTOMER_NAME_2 ; //                   VARCHAR2(200 CHAR) 
	public String FIRST_NAME           ; //              VARCHAR2(30 CHAR)  
	public String LAST_NAME                 ; //         VARCHAR2(40 CHAR)  
	public String NAME_TITLE                     ; //    VARCHAR2(30 CHAR)  
	public String MIDDLE_INITIAL ; //                    VARCHAR2(1 CHAR)   
	public String NAME_SUFFIX         ; //               VARCHAR2(30 CHAR)  
	public String COMPANY_NAME             ; //          VARCHAR2(200 CHAR) 
	public String STRUCT_NUMBER                 ; //     VARCHAR2(11 CHAR)  
	public String STRUCT_NUMBER_FRACTION            ; // VARCHAR2(4 CHAR)   
	public String ADDRESS_LINE_1; //                     VARCHAR2(100 CHAR) 
	public String ADDRESS_LINE_2     ; //                VARCHAR2(100 CHAR) 
	public String ADDRESS_LINE_3          ; //           VARCHAR2(100 CHAR) 
	public String CITY                        ; //       VARCHAR2(100 CHAR) 
	public String STATE                           ; //   VARCHAR2(100 CHAR) 
	public String ZIP; //                                VARCHAR2(20 CHAR)  
	public String ZIP_PLUS4; //                          VARCHAR2(12 CHAR)  
	public String PRE_DIRECTIONAL_CODE ; //              VARCHAR2(5 CHAR)   
	public String STREET_NAME              ; //          VARCHAR2(100 CHAR) 
	public String STREET_TYPE                  ; //      VARCHAR2(25 CHAR)  
	public String POST_DIRECTIONAL_CODE         ; //     VARCHAR2(5 CHAR)   
	public String SECONDARY_ADDRESS_TYPE   ; //          VARCHAR2(100 CHAR) 
	public String SECONDARY_ADDRESS_NUMBER     ; //      VARCHAR2(100 CHAR) 
	public String CITY_ST_ZIP                      ; //  VARCHAR2(225 CHAR) 
	public String PRIMARY_PHONE_NUMBER        ; //       VARCHAR2(15 CHAR)  
	public String SECONDARY_PHONE_NUMBER       ; //      VARCHAR2(15 CHAR)  
	public String EMAIL_ADDRESS               ; //       VARCHAR2(40 CHAR)  
	public String COUNTRY                        ; //    VARCHAR2(40 CHAR)  
	public String CUSTOM_ATTRIBUTE1       ; //           VARCHAR2(25 CHAR)  
	public String CUSTOM_ATTRIBUTE2            ; //      VARCHAR2(25 CHAR)  
	public String CUSTOM_ATTRIBUTE3                ; //  VARCHAR2(25 CHAR)  
	public String CUSTOM_ATTRIBUTE4   ; //               VARCHAR2(25 CHAR)  
	public String CUSTOM_ATTRIBUTE5   ; //               VARCHAR2(25 CHAR)  
	public String INSERTED_BY        ; //       NOT NULL VARCHAR2(30 CHAR)  
	public Date INSERT_TIME       ; //        NOT NULL DATE               
	public String UPDATED_BY   ; //                      VARCHAR2(30 CHAR)  
	public Date UPDATE_TIME    ; //                    DATE
	public int MDVC_ID  ;//                      NOT NULL NUMBER             
	public int LOCATION_ID  ;                  NOT NULL NUMBER             
	public int CUSTOMER_ID ;//               NOT NULL NUMBER             
	public int COUNTY_ID;//                               NUMBER             
	public int CITY_ID ;//                                NUMBER             
	public int MR_PROVIDER_ID;//                          NUMBER             
	public int MR_GATEWAY_ID ;//                          NUMBER             
	public int CLIENT_CO_ID ;//                  NOT NULL NUMBER             
	public int RATE_ID                                 NUMBER             
	MR_MDVC_NUMBER                          VARCHAR2(30 CHAR)  
	CLIENT_MDVC_NUMBER                      VARCHAR2(30 CHAR)  
	SERVICE_CODE                            VARCHAR2(1 CHAR)   
	CUST_CLASS_IND                          VARCHAR2(3 CHAR)   
	BILLING_CYCLE                           VARCHAR2(6 CHAR)   
	ROUTE_ID                                VARCHAR2(12 CHAR)  
	READ_SEQUENCE                           NUMBER(6)          
	CLIENT_RATE                             VARCHAR2(20 CHAR)  
	CLIENT_TOU_CONFIG                       VARCHAR2(20 CHAR)  
	CLIENT_SCALING_CONSTANT                 NUMBER(21,10)      
	MR_SCALING_CONSTANT                     NUMBER(21,10)      
	MULTIPLIER                              NUMBER(11,5)       
	MANUFACTURER_CODE                       VARCHAR2(10 CHAR)  
	MODEL                                   VARCHAR2(10 CHAR)  
	DIALS                                   NUMBER(2)          
	STATUS_CODE                             VARCHAR2(20 CHAR)  
	STATUS_CHANGE_DATE                      DATE               
	INSTALL_DATE                            DATE               
	REMOVE_DATE                             DATE               
	PURCHASE_YEAR                           NUMBER(4)          
	BATTERY_CODE                            VARCHAR2(15 CHAR)  
	BATTERY_CHANGE_DATE                     DATE               
	DST_CALC_IND                            VARCHAR2(1 CHAR)   
	DEMAND_INTERVAL                         NUMBER(2)          
	DEMAND_SUB_INTERVAL                     NUMBER(2)          
	KYZ_IND                                 VARCHAR2(1 CHAR)   
	KYZ_PULSE_VALUE                         NUMBER(10,6)       
	PULSES_ROT                              NUMBER             
	LOAD_PROFILE_IND                        VARCHAR2(1 CHAR)   
	LOAD_PROFILE_INTERVAL                   NUMBER(2)          
	LOAD_PROFILE_FREQUENCY                  CHAR(10 CHAR)      
	CUSTOM_ATTRIBUTE_1                      VARCHAR2(40 CHAR)  
	CUSTOM_ATTRIBUTE_2                      VARCHAR2(25 CHAR)  
	CUSTOM_ATTRIBUTE_3                      VARCHAR2(25 CHAR)  
	CUSTOM_ATTRIBUTE_4                      VARCHAR2(25 CHAR)  
	CUSTOM_ATTRIBUTE_5                      VARCHAR2(25 CHAR)  
	DEMAND_RESET_DATE                       DATE               
	LAST_BILLING_REQUEST_DATE               DATE               
	COLLECTOR_ID                            NUMBER             
	AMR_READ_TYPE                           VARCHAR2(1 CHAR)   
	READING_CYCLE                           VARCHAR2(4 CHAR)   
	PRODUCT_ID                              NUMBER(10)         
	PRODUCT_ATTRIB_TOKEN                    VARCHAR2(9 CHAR)   
	CLIENT_RATE_GROUP                       VARCHAR2(10 CHAR)  
	AUTOMATED_STATUS_CODE                   VARCHAR2(1 CHAR)   
	AUTOMATED_STATUS_CHANGE_DATE            DATE               
	METER_PASSWORD                          VARCHAR2(15 CHAR)  
	FIRMWARE_VERSION                        VARCHAR2(25 CHAR)  
	HARDWARE_VERSION                        VARCHAR2(25 CHAR)  
	K_SUB_R                                 NUMBER             
	R_SUB_P                                 NUMBER             
	SEAL_NUMBER                             VARCHAR2(25 CHAR)  
	RATE_SCHEDULE                           VARCHAR2(25 CHAR)  
	PREVIOUS_MDVC_ID                        NUMBER             
	REASON_CODE                             NUMBER             
	EFFECTIVE_DATE                          DATE               
	END_EFFECTIVE_DATE                      DATE               
	PROGRAM_ID_REF                          VARCHAR2(25 CHAR)  
	REGISTER_CONFIG_TYPE_CODE               VARCHAR2(30 CHAR)  
	REMOTE_COLLAR_ID                        VARCHAR2(25 CHAR)  
	REMOTE_COLLAR_STATUS_CHANGE_DT          DATE               
	REMOTE_COLLAR_STATUS_CODE               VARCHAR2(1 CHAR)   
	REMOTE_COLLAR_IND                       VARCHAR2(1 CHAR)   
	R_SUB_S                                 NUMBER(8)          
	IRREGULAR_USAGE_CUSTOMER_IND            VARCHAR2(1 CHAR)   
	INSERTED_BY                    NOT NULL VARCHAR2(30 CHAR)  
	INSERT_TIME                    NOT NULL DATE               
	UPDATED_BY                              VARCHAR2(30 CHAR)  
	UPDATE_TIME                             DATE               
	PROGRAM_TOU_CONFIG                      VARCHAR2(20 CHAR)  
	CLIENT_UNIQUE_METER_ID         NOT NULL VARCHAR2(30 CHAR)  
	PROGRAM_ID                              VARCHAR2(25 CHAR)  
	DR_PROGRAM_ID                           VARCHAR2(50 CHAR)  
	DR_PGM_OPT_STATUS_EFF_DATE              DATE               
	DR_PGM_OPT_STATUS                       VARCHAR2(10 CHAR)  
	MR_READING_STATUS              NOT NULL VARCHAR2(1 CHAR)   
	MR_READING_STATUS_CHG_DATE              DATE               
	METER_CONFIG_EFFECTIVE_DATE             DATE               
	END_EFFECTIVE_DATE_ORIG                 DATE               
	CLIENT_CO_LOCATION_ID          NOT NULL VARCHAR2(100 CHAR) 
	CLIENT_CO_ACCOUNT_ID           NOT NULL VARCHAR2(30 CHAR)  
	CLIENT_CO_CUSTOMER_ID          NOT NULL VARCHAR2(30 CHAR)  
	SERVICE_SUPPLIER                        VARCHAR2(20 CHAR)  
	FIRST_READ_DATE                         DATE               
	VIRTUAL_IND                             VARCHAR2(1 CHAR)   
	Name                           Null?    Type               
	------------------------------ -------- ------------------ 
	MDVC_ID                        NOT NULL NUMBER             
	LOCATION_ID                    NOT NULL NUMBER             
	ACCOUNT_ID                     NOT NULL NUMBER             
	CUSTOMER_ID                    NOT NULL NUMBER             
	COUNTY_ID                               NUMBER             
	CITY_ID                                 NUMBER             
	MR_PROVIDER_ID                          NUMBER             
	MR_GATEWAY_ID                           NUMBER             
	CLIENT_CO_ID                   NOT NULL NUMBER             
	RATE_ID                                 NUMBER             
	MR_MDVC_NUMBER                          VARCHAR2(30 CHAR)  
	CLIENT_MDVC_NUMBER                      VARCHAR2(30 CHAR)  
	SERVICE_CODE                            VARCHAR2(1 CHAR)   
	CUST_CLASS_IND                          VARCHAR2(3 CHAR)   
	BILLING_CYCLE                           VARCHAR2(6 CHAR)   
	ROUTE_ID                                VARCHAR2(12 CHAR)  
	READ_SEQUENCE                           NUMBER(6)          
	CLIENT_RATE                             VARCHAR2(20 CHAR)  
	CLIENT_TOU_CONFIG                       VARCHAR2(20 CHAR)  
	CLIENT_SCALING_CONSTANT                 NUMBER(21,10)      
	MR_SCALING_CONSTANT                     NUMBER(21,10)      
	MULTIPLIER                              NUMBER(11,5)       
	MANUFACTURER_CODE                       VARCHAR2(10 CHAR)  
	MODEL                                   VARCHAR2(10 CHAR)  
	DIALS                                   NUMBER(2)          
	STATUS_CODE                             VARCHAR2(20 CHAR)  
	STATUS_CHANGE_DATE                      DATE               
	INSTALL_DATE                            DATE               
	REMOVE_DATE                             DATE               
	PURCHASE_YEAR                           NUMBER(4)          
	BATTERY_CODE                            VARCHAR2(15 CHAR)  
	BATTERY_CHANGE_DATE                     DATE               
	DST_CALC_IND                            VARCHAR2(1 CHAR)   
	DEMAND_INTERVAL                         NUMBER(2)          
	DEMAND_SUB_INTERVAL                     NUMBER(2)          
	KYZ_IND                                 VARCHAR2(1 CHAR)   
	KYZ_PULSE_VALUE                         NUMBER(10,6)       
	PULSES_ROT                              NUMBER             
	LOAD_PROFILE_IND                        VARCHAR2(1 CHAR)   
	LOAD_PROFILE_INTERVAL                   NUMBER(2)          
	LOAD_PROFILE_FREQUENCY                  CHAR(10 CHAR)      
	CUSTOM_ATTRIBUTE_1                      VARCHAR2(40 CHAR)  
	CUSTOM_ATTRIBUTE_2                      VARCHAR2(25 CHAR)  
	CUSTOM_ATTRIBUTE_3                      VARCHAR2(25 CHAR)  
	CUSTOM_ATTRIBUTE_4                      VARCHAR2(25 CHAR)  
	CUSTOM_ATTRIBUTE_5                      VARCHAR2(25 CHAR)  
	DEMAND_RESET_DATE                       DATE               
	LAST_BILLING_REQUEST_DATE               DATE               
	COLLECTOR_ID                            NUMBER             
	AMR_READ_TYPE                           VARCHAR2(1 CHAR)   
	READING_CYCLE                           VARCHAR2(4 CHAR)   
	PRODUCT_ID                              NUMBER(10)         
	PRODUCT_ATTRIB_TOKEN                    VARCHAR2(9 CHAR)   
	CLIENT_RATE_GROUP                       VARCHAR2(10 CHAR)  
	AUTOMATED_STATUS_CODE                   VARCHAR2(1 CHAR)   
	AUTOMATED_STATUS_CHANGE_DATE            DATE               
	METER_PASSWORD                          VARCHAR2(15 CHAR)  
	FIRMWARE_VERSION                        VARCHAR2(25 CHAR)  
	HARDWARE_VERSION                        VARCHAR2(25 CHAR)  
	K_SUB_R                                 NUMBER             
	R_SUB_P                                 NUMBER             
	SEAL_NUMBER                             VARCHAR2(25 CHAR)  
	RATE_SCHEDULE                           VARCHAR2(25 CHAR)  
	PREVIOUS_MDVC_ID                        NUMBER             
	REASON_CODE                             NUMBER             
	EFFECTIVE_DATE                          DATE               
	END_EFFECTIVE_DATE                      DATE               
	PROGRAM_ID_REF                          VARCHAR2(25 CHAR)  
	REGISTER_CONFIG_TYPE_CODE               VARCHAR2(30 CHAR)  
	REMOTE_COLLAR_ID                        VARCHAR2(25 CHAR)  
	REMOTE_COLLAR_STATUS_CHANGE_DT          DATE               
	REMOTE_COLLAR_STATUS_CODE               VARCHAR2(1 CHAR)   
	REMOTE_COLLAR_IND                       VARCHAR2(1 CHAR)   
	R_SUB_S                                 NUMBER(8)          
	IRREGULAR_USAGE_CUSTOMER_IND            VARCHAR2(1 CHAR)   
	INSERTED_BY                    NOT NULL VARCHAR2(30 CHAR)  
	INSERT_TIME                    NOT NULL DATE               
	UPDATED_BY                              VARCHAR2(30 CHAR)  
	UPDATE_TIME                             DATE               
	PROGRAM_TOU_CONFIG                      VARCHAR2(20 CHAR)  
	CLIENT_UNIQUE_METER_ID         NOT NULL VARCHAR2(30 CHAR)  
	PROGRAM_ID                              VARCHAR2(25 CHAR)  
	DR_PROGRAM_ID                           VARCHAR2(50 CHAR)  
	DR_PGM_OPT_STATUS_EFF_DATE              DATE               
	DR_PGM_OPT_STATUS                       VARCHAR2(10 CHAR)  
	MR_READING_STATUS              NOT NULL VARCHAR2(1 CHAR)   
	MR_READING_STATUS_CHG_DATE              DATE               
	METER_CONFIG_EFFECTIVE_DATE             DATE               
	END_EFFECTIVE_DATE_ORIG                 DATE               
	CLIENT_CO_LOCATION_ID          NOT NULL VARCHAR2(100 CHAR) 
	CLIENT_CO_ACCOUNT_ID           NOT NULL VARCHAR2(30 CHAR)  
	CLIENT_CO_CUSTOMER_ID          NOT NULL VARCHAR2(30 CHAR)  
	SERVICE_SUPPLIER                        VARCHAR2(20 CHAR)  
	FIRST_READ_DATE                         DATE               
	VIRTUAL_IND                             VARCHAR2(1 CHAR)*/
}
